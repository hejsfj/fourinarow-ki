package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import com.pusher.client.channel.PrivateChannel;

import agentKI.AgentKI;
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
import javafx.stage.Stage;
import pusherInterface.PusherConnector;
import pusherInterface.PusherEvent;
import pusherInterface.PusherEventHandler;

public class GameController implements Initializable {
	
    @FXML private Button newButton; // Value injected by FXMLLoader    
    @FXML private Button statsButton; // Value injected by FXMLLoader    
	@FXML private Button startButton;
	
	@FXML private Label infostat;
	@FXML private GridPane gamefieldGrid;
	
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
    }
    
	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {		
		assert startButton != null : "fx:id=\"statsButton\" was not injected: check your FXML file";
				
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Zum neuen Spiel");			
				infostat.setText("Das Spiel beginnt");				
				
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
			}
		});
	}

	private void startPusherInterfaceGame() {
		System.out.println("PusherInterface not implemented yet");
		
		String appKey = gameProperties.getProperty(GameProperties.APP_KEY);
		String appSecret = gameProperties.getProperty(GameProperties.APP_SECRET);
		
		PusherConnector pusher = new PusherConnector("", appKey, appSecret);
		PusherEventHandler pusherEventHandler = new PusherEventHandler();
		PrivateChannel channel = pusher.subscribeToPrivateChannel("private-channel", pusherEventHandler, "MoveToAgent");
		PusherEvent pusherEvent;
	
		while (true) {
			try {
				pusherEvent = pusherEventHandler.getPusherEvent();
				pusherEventHandler.deletePusherEvent();
				
				System.out.println("PusherEvent");
				System.out.println("Satzstatus: " + pusherEvent.getSatzstatus());
				System.out.println("Freigabe: " + pusherEvent.getFreigabe());
				System.out.println("Sieger: " + pusherEvent.getSieger());
				System.out.println("Gegnerzug: " + pusherEvent.getGegnerzug());
				System.out.println("");

				if (pusherEvent.getGegnerzug() != -1 && pusherEvent.getGegnerzug() != -2){
					gamefield.insertCoin(gamefieldGrid, pusherEvent.getGegnerzug(), (player == 'o') ? 'x' : 'o');
				}
				if (!pusherEvent.isGameOver()){
					int move = agent.calculateMove(gamefield);
					System.out.println("calculated move: " + move);
					
					gamefield.insertCoin(gamefieldGrid, move, player);
					
					pusher.triggerClientMove(channel, move);
					System.out.println("Spiel vorbei. Wir haben gewonnen");
					if (gamefield.hasWinner()){
						break;
					}
					
				} else {
					System.out.println("Spiel vorbei. Der Gewinner ist: " + pusherEvent.getSieger());
					break;
				}
			} catch (Exception e){
				e.printStackTrace();						
			}
		}				
	}
	
	private void startFileInterfaceGame() {			
		String sharedFolderPath = gameProperties.getProperty(GameProperties.DATEIPFAD);			
		AgentfileWriter agentfileWriter = new AgentfileWriter(sharedFolderPath, player);
		
		startButton.setDisable(true);		
		infostat.setText("Spiel läuft.");
		
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

