package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.pusher.client.channel.PrivateChannel;

import agentKI.AgentKI;
import database.DatabaseManager;
import fileInterface.Agentfile;
import fileInterface.AgentfileWriter;
import fileInterface.ServerfileReaderService;
import gamefield.Gamefield;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pusherInterface.PusherConnector;
import pusherInterface.PusherEventReaderService;

public class GameController implements Initializable {
	
    @FXML private Button newButton; // Value injected by FXMLLoader    
    @FXML private Button statsButton; // Value injected by FXMLLoader    
	@FXML private Button startButton;

    @FXML private Text tfPlayer1_Game_Wins;
    @FXML private Text tfPlayer2_Game_Wins;
    @FXML private Text tfPlayer1_Set_Wins;
    @FXML private Text tfPlayer2_Set_Wins;
	
	@FXML private Label infostat;
	@FXML private GridPane gamefieldGrid;
	
	private DatabaseManager databaseManager;
	private GameProperties gameProperties;
	private Gamefield gamefield;
	private AgentKI agent;
	private char player;
	private String usedInterface;
	
	@FXML void loadGame(ActionEvent event) {    	
    	System.out.println("New Game");
		Stage stage;
		stage = (Stage) startButton.getScene().getWindow();
		AnchorPane page;
		
		try {
			page = (AnchorPane) FXMLLoader.load(getClass().getResource("Load.fxml"));
			Scene scene = new Scene(page);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML void newGame(ActionEvent event) {
    	System.out.println("New Game");
		Stage stage;
		stage = (Stage) startButton.getScene().getWindow();
		AnchorPane page;
		
		try {
			page = (AnchorPane) FXMLLoader.load(getClass().getResource("Settings.fxml"));
			Scene scene = new Scene(page);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {		
		assert startButton != null : "fx:id=\"statsButton\" was not injected: check your FXML file";
				
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {	
				startButton.setDisable(true);		
				infostat.setText("Spiel läuft.");
				initRequiredComponents();
				
				readProperties();
				usedInterface = gameProperties.getProperty(GameProperties.INTERFACE);				
				player	      = gameProperties.getProperty(GameProperties.SPIELER).charAt(0);
				
				if (usedInterface.equals("File")){
					startFileInterfaceGame();
				}
				if (usedInterface.equals("Push")){
					startPusherInterfaceGame();
				}
			}

			private void initRequiredComponents() {
				gameProperties = new GameProperties();
				gamefield = new Gamefield();
				agent = new AgentKI();
				databaseManager = new DatabaseManager();
			}
		});
	}

	private void startPusherInterfaceGame() {
		/*
		try {
			System.out.println("vorm satz hinzufügen");
			databaseManager.addSatz(0, 0, 0, 0, "");
			System.out.println("Satz hinzugefügt");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		}*/
		
		String appKey = gameProperties.getProperty(GameProperties.APP_KEY);
		String appSecret = gameProperties.getProperty(GameProperties.APP_SECRET);
		
		PusherConnector pusher = new PusherConnector("", appKey, appSecret);

		// pusherEventReaderService hört nach neuen Events, aber in hintergrund-thread durch task!
		final PusherEventReaderService pusherEventReaderService = new PusherEventReaderService();
		
		PrivateChannel channel = pusher.subscribeToPrivateChannel("private-channel", pusherEventReaderService, "MoveToAgent");
	
		
		pusherEventReaderService.setOnSucceeded(e -> {
			System.out.println("here");
	    	 if (pusherEventReaderService.getValue().getGegnerzug() != -1){
					gamefield.insertCoin(gamefieldGrid, pusherEventReaderService.getValue().getGegnerzug(), (player == 'o') ? 'x' : 'o');
	    	  }
	    	  if (!pusherEventReaderService.getValue().isGameOver()){
	    		  int move = agent.calculateMove(gamefield);
	    		  gamefield.insertCoin(gamefieldGrid, move, player);	    		  
	    		  
	    		  pusher.triggerClientMove(channel, move);

	    		  if (gamefield.hasWinner()) {
	    			  infostat.setText("Wir haben gewonnen!");	    			  
	    		  } else if (gamefield.isFull()){
	    			  infostat.setText("Kein Sieger!");
	    		  } else {
	    			  // task wird hier erneut gestartet, da spiel noch nicht vorbei ist!!
	    			  pusherEventReaderService.restart();
	    		  }
	    	  } 
	    	  else {
	    		  System.out.println("Spiel vorbei. Der Gewinner ist: " + pusherEventReaderService.getValue().getSieger());
	    		  infostat.setText(pusherEventReaderService.getValue().getSieger() + " hat gewonnen!");
	    	  }
	    }); 
		pusherEventReaderService.start();
	}
	
	private void startFileInterfaceGame() {			
		String sharedFolderPath = gameProperties.getProperty(GameProperties.DATEIPFAD);			
		AgentfileWriter agentfileWriter = new AgentfileWriter(sharedFolderPath, player);
		
		
		ServerfileReaderService serverFileReaderService = new ServerfileReaderService(sharedFolderPath, player);		
		serverFileReaderService.setOnSucceeded(e -> {
	    	 if (serverFileReaderService.getValue().getGegnerzug() != -1){
					gamefield.insertCoin(gamefieldGrid, serverFileReaderService.getValue().getGegnerzug(), (player == 'o') ? 'x' : 'o');
	    	  }
	    	  if (!serverFileReaderService.getValue().isGameOver()){
	    		  int move = agent.calculateMove(gamefield);
	    		  gamefield.insertCoin(gamefieldGrid, move, player);
	    		  try {
					agentfileWriter.writeAgentfile(new Agentfile(move));
				} catch (Exception e1) {
					e1.printStackTrace();
				}		    		  
	    		  serverFileReaderService.restart();
	    	  } 
	    	  else {
	    		  System.out.println("Spiel vorbei. Der Gewinner ist: " + serverFileReaderService.getValue().getSieger());
	    		  infostat.setText(serverFileReaderService.getValue().getSieger() + " hat gewonnen!");
	    	  }
	    }); 	  
		
		serverFileReaderService.start();		
	}
	
	private void readProperties() {
		InputStream input = null;
    	
    	try {
    		input = new FileInputStream(GameProperties.DATEINAME);
    		gameProperties.load(input);
    	} catch (IOException ex) {
    		ex.printStackTrace();
        } finally {
        	if(input != null){
        		try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
				}
        	}
        }
    }	
}

