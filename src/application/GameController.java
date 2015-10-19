package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;

import java.util.Date;
import java.util.ResourceBundle;

import com.pusher.client.channel.PrivateChannel;


import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;


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
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pusherInterface.PusherConnector;
import pusherInterface.PusherEventReaderService;

public class GameController implements Initializable {
	
	@FXML private MenuBar menuBar;
	@FXML private MenuItem LoadGame;
	@FXML private MenuItem NewGame;
    @FXML private Button newButton; // Value injected by FXMLLoader    
    @FXML private Button statsButton; // Value injected by FXMLLoader    
	@FXML private Button startButton; // Value injected by FXMLLoader   

    @FXML private Text tfPlayer1_Game_Wins;
    @FXML private Text tfPlayer2_Game_Wins;
    @FXML private Text tfPlayer1_Set_Wins;
    @FXML private Text tfPlayer2_Set_Wins;
	
    @FXML private Circle circlePlayerO;
    @FXML private Circle circlePlayerX;
    
	@FXML private Label infostat;
	@FXML private GridPane gamefieldGrid;
	
	private DatabaseManager databaseManager;
	private GameProperties gameProperties;
	private Gamefield gamefield;
	private AgentKI agent;
	private char myPlayer;
	private char opponentPlayer;
	private String usedInterface;
	private int currentGameId;
	private int currentSetNr;
	private int zugNrCounter;
	
	@FXML void loadGame(ActionEvent event) {    	
    	System.out.println("Load Game");
		
		try {
			showLoadScreen();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML void newGame(ActionEvent event) {
    	System.out.println("New Game");
		
		try {
			showSettingsScreen();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {		
		assert startButton != null : "fx:id=\"startButton\" was not injected: check your FXML file";
		assert menuBar != null : "fx:id=\"menuBar\" was not injected: check your FXML file";
		assert circlePlayerO != null : "fx:id=\"circlePlayerO\" was not injected: check your FXML file 'Game.fxml'.";
	    assert circlePlayerX != null : "fx:id=\"circlePlayerX\" was not injected: check your FXML file 'Game.fxml'.";
	       
		initRequiredComponents();

		readPropertiesFromFileSystem();
		usedInterface = gameProperties.getProperty(GameProperties.INTERFACE);				
		myPlayer	  = gameProperties.getProperty(GameProperties.SPIELER).charAt(0);

		if (myPlayer == 'o') {
			opponentPlayer = 'x';
			animateCurrentPlayer(circlePlayerO);
		} else { 
			opponentPlayer = 'o';
			animateCurrentPlayer(circlePlayerX);
		}
		
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {	
				startButton.setDisable(true);	
				menuBar.setDisable(true);
				infostat.setText("Spiel läuft.");
				
				if (isNewGame()) {
					currentGameId = addNewGameToDb();
					currentSetNr = 1;
					addNewSetToGameInDb(currentGameId, currentSetNr);
				} else {
					// currentGameId kommt aus dem Ladescreen
				}
				
				if (usedInterface.equals("File")){
					startFileInterfaceGame();
				}
				if (usedInterface.equals("Push")){
					startPusherInterfaceGame();
				}
			}
		});
	}
	
	private Stage showSettingsScreen() throws IOException {
    	Stage stage = (Stage) startButton.getScene().getWindow();
	  	FXMLLoader loader = new FXMLLoader(getClass().getResource("Settings.fxml"));
	
	  	stage.setScene(new Scene((Pane) loader.load()));
	
	  	stage.show();
	  	return stage;
    }
	
    private Stage showLoadScreen() throws IOException {
    	Stage stage = (Stage) startButton.getScene().getWindow();
	  	FXMLLoader loader = new FXMLLoader(getClass().getResource("Load.fxml"));
	
	  	stage.setScene(new Scene((Pane) loader.load()));
	
	  	stage.show();
	  	return stage;
    }
	
	private void initRequiredComponents() {
		gameProperties = new GameProperties();
		gamefield = new Gamefield();
		agent = new AgentKI();
		databaseManager = DatabaseManager.getInstance();
	}
	
	private void startPusherInterfaceGame() {
		String appKey = gameProperties.getProperty(GameProperties.APP_KEY);
		String appSecret = gameProperties.getProperty(GameProperties.APP_SECRET);
		
		PusherConnector pusher = new PusherConnector("", appKey, appSecret);

		// pusherEventReaderService hört nach neuen Events, aber in hintergrund-thread durch task!
		final PusherEventReaderService pusherEventReaderService = new PusherEventReaderService();
		
		PrivateChannel channel = pusher.subscribeToPrivateChannel("private-channel", pusherEventReaderService, "MoveToAgent");
	
		
		pusherEventReaderService.setOnSucceeded(e -> {
			System.out.println("here");
	    	 if (pusherEventReaderService.getValue().getGegnerzug() != -1){
					gamefield.insertCoin(gamefieldGrid, pusherEventReaderService.getValue().getGegnerzug(), (myPlayer == 'o') ? 'x' : 'o');
	    	  }
	    	  if (!pusherEventReaderService.getValue().isGameOver()){
	    		  int move = agent.calculateMove(gamefield);
	    		  gamefield.insertCoin(gamefieldGrid, move, myPlayer);	    		  
	    		  
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
		AgentfileWriter agentfileWriter = new AgentfileWriter(sharedFolderPath, myPlayer);
		zugNrCounter = 1;
		ServerfileReaderService serverFileReaderService = new ServerfileReaderService(sharedFolderPath, myPlayer);		
		serverFileReaderService.setOnSucceeded(e -> {
				// Gegnerzug
	    	 if (serverFileReaderService.getValue().getGegnerzug() != -1){
	    		 	int gegnerZug = serverFileReaderService.getValue().getGegnerzug();
					gamefield.insertCoin(gamefieldGrid, gegnerZug, opponentPlayer);
					try {
						// addZug(int spiel_id, int satz_nr,int zug_nr, int spalte, String spieler)
						databaseManager.addMove(currentGameId, currentSetNr, gegnerZug, "spieler" + String.valueOf(opponentPlayer));

						zugNrCounter++;
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    	  }
	    	 
	    	 // unser Zug
	    	  if (!serverFileReaderService.getValue().isGameOver()){
	    		  int move = agent.calculateMove(gamefield);
	    		  gamefield.insertCoin(gamefieldGrid, move, myPlayer);
	    		  try {
						databaseManager.addMove(currentGameId, currentSetNr, move, "spieler" + String.valueOf(myPlayer));

						zugNrCounter++;
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    		  try {
					agentfileWriter.writeAgentfile(new Agentfile(move));
				} catch (Exception e2) {
					e2.printStackTrace();
				}		    		  
	    		  serverFileReaderService.restart();
	    	  } 
	    	  else {
	    		  System.out.println("Spiel vorbei. Der Gewinner ist: " + serverFileReaderService.getValue().getSieger());
	    		  infostat.setText(serverFileReaderService.getValue().getSieger() + " hat gewonnen!");
	    		  startButton.setDisable(false);
	    		  menuBar.setDisable(false);
	    		  try {
					databaseManager.updateSet(currentGameId, currentSetNr, serverFileReaderService.getValue().getSieger());
				} catch (Exception e3) {
					e3.printStackTrace();
				}
	    	  }
	    }); 	  
		
		serverFileReaderService.start();		
	}

	private boolean isNewGame(){
		if (tfPlayer1_Set_Wins.getText().equals("0") && tfPlayer2_Set_Wins.getText().equals("0"))
			return true;
		return false;
	}
	
	private int addNewGameToDb(){
		int newGameId = -1;
		try {
			newGameId = databaseManager.addGame("spielerO", "spielerX", null, new Date().toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return newGameId;
	}
	
	private void addNewSetToGameInDb(int gameId, int setNr){
		try {
			databaseManager.addSet(gameId, setNr, 0, 0, "", "startspieler");
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	private void animateCurrentPlayer(Circle circle){
		
			FadeTransition fade = new FadeTransition(Duration.millis(500), circle);
            fade.setFromValue(1);
            fade.setToValue(0);
            fade.setAutoReverse(true);
            fade.setCycleCount(Timeline.INDEFINITE);
            fade.play();
		
	}
	
	
	private void readPropertiesFromFileSystem() {
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

