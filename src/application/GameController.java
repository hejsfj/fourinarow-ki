package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

import com.pusher.client.channel.PrivateChannel;

import agentKI.AgentKI;
import database.DatabaseManager;
import database.DatabaseSetRecord;
import fileInterface.Agentfile;
import fileInterface.AgentfileWriter;
import fileInterface.ServerfileReaderService;
import gamefield.Gamefield;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import pusherInterface.PusherConnector;
import pusherInterface.PusherEventReaderService;


/**
 * Diese Klasse enthält die Anwendungslogik für die die Game.fxml-Datei, welche das Spielfeld repräsentiert.
 * 
 */
public class GameController implements Initializable {
	
	@FXML private MenuBar menuBar;
	@FXML private Menu menuDatei;
	@FXML private Menu menuSpiel;
	@FXML private MenuItem menuItem_LoadGame;
	@FXML private MenuItem menuItem_PlayerOWinsCurrentSet;
	@FXML private MenuItem menuItem_PlayerXWinsCurrentSet;
	@FXML private MenuItem menuItem_StopCurrentSet;
	
    @FXML private Button newButton; // Value injected by FXMLLoader    
    @FXML private Button statsButton; // Value injected by FXMLLoader    
	@FXML private Button startButton; // Value injected by FXMLLoader   

    @FXML private Text tfPlayer1_Points;
    @FXML private Text tfPlayer2_Points;
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
	private DatabaseSetRecord selectedSetFromLoadScreen;
	private PusherEventReaderService pusherEventReaderService;
	private ServerfileReaderService serverFileReaderService;
	
	/**
	 * Aktualisiert den Gewinner des aktuellen Spielsatzes.
	 *
	 * @param event Ein Event repräsentiert eine Art von Aktion.
	 * 				Dieser Event-Type wird benutzt um z.B. wenn auf einen Button-Klick reagiert werden soll.
	 * @see {@link javafx.event.ActionEvent}
	 */
	@FXML void updateWinnerOfCurrentSet(ActionEvent event) {    
		MenuItem source = (MenuItem) event.getSource();
		try {
			if (source.getId().equals(menuItem_PlayerOWinsCurrentSet.getId())) {
				databaseManager.updateWinnerOfSet(currentGameId, currentSetNr, "Spieler O");
			} else if (source.getId().equals(menuItem_PlayerXWinsCurrentSet.getId())) {
				databaseManager.updateWinnerOfSet(currentGameId, currentSetNr, "Spieler X");
			}
			updateTextFields();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Stoppt / Beendet den aktuell laufenden Spielsatz.
	 *
	 * @param event {@link javafx.event.ActionEvent}
	 */
	@FXML void stopCurrentSet(ActionEvent event) {    	
		infostat.setText("Satz gestoppt. Kein Gewinner!");
		startButton.setDisable(false);
		menuDatei.setDisable(false);
		menuItem_PlayerOWinsCurrentSet.setDisable(false);
		menuItem_PlayerXWinsCurrentSet.setDisable(false);
		menuItem_StopCurrentSet.setDisable(true);
		
		if (pusherEventReaderService != null){
			pusherEventReaderService.cancel();
		}
		if (serverFileReaderService != null){
			serverFileReaderService.cancel();
		}
		
	}
	
	/**
	 * Lädt einen Spielstand aus der Datenbank.
	 *
	 * @param event {@link javafx.event.ActionEvent}
	 */
	@FXML void loadGame(ActionEvent event) {    			
		try {
	    	System.out.println("Switching to Load Screen");
			showLoadScreen();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * Startet ein neues Spiel.
	 *
	 * @param event {@link javafx.event.ActionEvent}
	 */
    @FXML void newGame(ActionEvent event) {		
		try {
	    	System.out.println("Switching to Settings Screen");
			showSettingsScreen();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Initialisiert den Game-Controller.
     *
     * @param selectedSetFromLoadScreen ausgewählter Satz aus der Datenbank bzw. dem Lade-Bildschirm.
     */
    public void initController(DatabaseSetRecord selectedSetFromLoadScreen){
    	System.out.println("initController in GameController called!");
    	this.selectedSetFromLoadScreen = selectedSetFromLoadScreen;
    	try {
        	// bestimme alle gewinner der bisherigen sätze des spiels
			currentGameId = Integer.parseInt(selectedSetFromLoadScreen.getGameId());
			currentSetNr = Integer.parseInt(selectedSetFromLoadScreen.getSetId());
			menuItem_PlayerOWinsCurrentSet.setDisable(false);
			menuItem_PlayerXWinsCurrentSet.setDisable(false);
			updateTextFields();			
			System.out.println("Game with GameId: " + currentGameId + " and SetNr: " + currentSetNr + " loaded.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
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
				startSet();				
				determineCurrentGameIdAndSetNr();				
				
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
		pusherEventReaderService = new PusherEventReaderService();
		
		PrivateChannel channel = pusher.subscribeToPrivateChannel("private-channel", pusherEventReaderService, "MoveToAgent");
	
		zugNrCounter = 1;
		
		pusherEventReaderService.setOnSucceeded(e -> {
			// Gegnerzug verarbeiten
			if (pusherEventReaderService.getValue().getGegnerzug() != -1) {
				int opponentMove = pusherEventReaderService.getValue().getGegnerzug();
				gamefield.insertCoin(gamefieldGrid, opponentMove, opponentPlayer);
				try {
					// addZug(int spiel_id, int satz_nr,int zug_nr, int spalte, String spieler)
					databaseManager.addMove(currentGameId, currentSetNr, zugNrCounter, opponentMove, "spieler" + String.valueOf(opponentPlayer));
					zugNrCounter++;
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
	    	}
			
			// unseren Zug bestimmen und wegschicken
			if (!pusherEventReaderService.getValue().isGameOver()) {
				int move = agent.calculateMove(gamefield);
				gamefield.insertCoin(gamefieldGrid, move, myPlayer);	    	
				pusher.triggerClientMove(channel, move);
				
				try {
	    			databaseManager.addMove(currentGameId, currentSetNr, zugNrCounter, move, "spieler" + String.valueOf(myPlayer));
	    			zugNrCounter++;
	    		} catch (SQLException e1) {
	    			e1.printStackTrace();
	    		}

				pusherEventReaderService.restart();
	    	} else {
	    		finishSet(pusherEventReaderService.getValue().getSieger());
	    	}
	    }); 
		pusherEventReaderService.start();
	}
	
	private void startFileInterfaceGame() {			
		String sharedFolderPath = gameProperties.getProperty(GameProperties.DATEIPFAD);			
		AgentfileWriter agentfileWriter = new AgentfileWriter(sharedFolderPath, myPlayer);
		zugNrCounter = 1;
		serverFileReaderService = new ServerfileReaderService(sharedFolderPath, myPlayer);		
		serverFileReaderService.setOnSucceeded(e -> {
			// Gegnerzug verarbeiten
			if (serverFileReaderService.getValue().getGegnerzug() != -1){
				int gegnerZug = serverFileReaderService.getValue().getGegnerzug();
				gamefield.insertCoin(gamefieldGrid, gegnerZug, opponentPlayer);
				try {
					// addZug(int spiel_id, int satz_nr,int zug_nr, int spalte, String spieler)
					databaseManager.addMove(currentGameId, currentSetNr, zugNrCounter, gegnerZug, "spieler" + String.valueOf(opponentPlayer));
					zugNrCounter++;
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
	    	  }
	    	 
	    	 // unseren Zug bestimmen und wegschicken
	    	 if (!serverFileReaderService.getValue().isGameOver()){
	    		 int move = agent.calculateMove(gamefield);
	    		 gamefield.insertCoin(gamefieldGrid, move, myPlayer);
	    		 try {
	    			 agentfileWriter.writeAgentfile(new Agentfile(move));
	    			 databaseManager.addMove(currentGameId, currentSetNr, zugNrCounter, move, "spieler" + String.valueOf(myPlayer));
	    			 zugNrCounter++;
	    		 } catch (SQLException e1) {
	    			 e1.printStackTrace();
	    		 } catch (IOException ioe){
	    			 ioe.printStackTrace();
	    		 } 
	    		  serverFileReaderService.restart();
	    	  } 
	    	  else {
	    		  finishSet(serverFileReaderService.getValue().getSieger());
	    	  }
	    }); 	  
		
		serverFileReaderService.start();		
	}
	
	private void determineCurrentGameIdAndSetNr() {
		if (selectedSetFromLoadScreen == null ){
			currentGameId = addNewGameToDb();
			currentSetNr = 1;
			addNewSetToGameInDb(currentGameId, currentSetNr);			
		} else if (selectedSetFromLoadScreen.getWinner().equals("")) {
			currentGameId = Integer.parseInt(selectedSetFromLoadScreen.getGameId());
			currentSetNr = Integer.parseInt(selectedSetFromLoadScreen.getSetId());
		} else {
			currentGameId = Integer.parseInt(selectedSetFromLoadScreen.getGameId());
			currentSetNr = Integer.parseInt(selectedSetFromLoadScreen.getSetId()) + 1;
			addNewSetToGameInDb(currentGameId, currentSetNr);
		}
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
	
	private void startSet(){
		startButton.setDisable(true);	
		menuDatei.setDisable(true);
		menuItem_PlayerOWinsCurrentSet.setDisable(true);
		menuItem_PlayerXWinsCurrentSet.setDisable(true);
		menuItem_StopCurrentSet.setDisable(false);
		infostat.setText("Spiel läuft.");
	}
	
	private void finishSet(String winner) {
		System.out.println("Spiel vorbei. Der Gewinner ist: " + winner);
		infostat.setText(winner + " hat gewonnen!");
		menuDatei.setDisable(false);
		menuItem_PlayerOWinsCurrentSet.setDisable(false);
		menuItem_PlayerXWinsCurrentSet.setDisable(false);
		menuItem_StopCurrentSet.setDisable(true);
		try {
			databaseManager.updateWinnerOfSet(currentGameId, currentSetNr, winner);
			updateTextFields();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
    private void updateTextFields() throws SQLException {
    	ResultSet setWinners = databaseManager.getAllSetWinnersForGameId(String.valueOf(currentGameId));
		
    	int setWinsPlayer1 = 0;
		int setWinsPlayer2 = 0;
		
		while (setWinners.next()) {
			String winner = setWinners.getString( "sieger" );           
			if (winner.equals("Spieler O")){
				setWinsPlayer1++;
			} else if (winner.equals("Spieler X")){
				setWinsPlayer2++;
			}
		}
		tfPlayer1_Set_Wins.setText(String.valueOf(setWinsPlayer1));
		tfPlayer2_Set_Wins.setText(String.valueOf(setWinsPlayer2));
		tfPlayer1_Points.setText(String.valueOf(setWinsPlayer1 * 2));
		tfPlayer2_Points.setText(String.valueOf(setWinsPlayer2 * 2));
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

