package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import com.pusher.client.channel.PrivateChannel;

import agentKI.AgentKI;
import agentKI.KI_MinMax;
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
 * Diese Klasse enthält die Anwendungslogik für die die Game.fxml-Datei, welche
 * das Spielfeld repräsentiert.
 * 
 * Die Anwendung ist in 3 separat agierende Benutzeroberflächen (fxml)
 * aufgeteilt, welche logisch miteinander verbunden sind. Alle Fenster des
 * Programms sind mit 830x530 Pixel fest skaliert. Der Aufbau der Fenster ist
 * identisch. Mit JavaFX wurde eine AnchorPane erstellt, an dem alle Elemente
 * des Interface verankert sind. Zudem verbindet die AnchorPane die reinen
 * Design-Dateien mit der Controllerdatei in dem Package applications, welche
 * die logischen Aktionen der Benutzeroberfläche steuert. Direkt über dem
 * AnchorPane liegt ein BorderPane, welches das Fenster in 5 Boxen zerteilt (s.
 * Abb.XX). Diese Boxen werden je nach Bedarf mit verschiedenen Elementen
 * befüllt.
 * <br>
 * <figure> <img src="doc-files/BorderPane.jpg" widht="450" height="450" alt=
 * "Grundaufbau des UIs" title="Grundaufbau des UIs"/> 
 * <figcaption>Grundaufbau des UIs</figcaption> </figure> <br>
 * <br>
 * Von der Konfigurationsoberfläche wird der Benutzer auf die
 * Spielfeldoberfläche weitergeleitet, die einen identischen strukturellen
 * Aufbau hat. Box 1 beinhaltet ebenso eine Menüleiste, sowie Textelemente,
 * verbaut in eine H-Box, die Informationen zum aktuellen Spielstand bzw.
 * Satzstand darstellen. In Box 2 und 4 werden die beiden agierenden Spieler
 * angezeigt, mittels Kreisen werden die verschiedenen Spielerfarben angezeigt
 * sowie weitere Informationen mit Hilfe eines Textelements. In Box 3 befindet
 * sich das Spielfeld, welches in Form eines GridPanes abgebildet wird. In dem
 * GridPane mit der Größe eines 4-Gewinnt Spielfelds können Kreise hinzugefügt
 * werden, die den Spielzug eines Spielers widergeben. Die Kreise nehmen jeweils
 * die Farbe des jeweiligen Spielers an. Box 5 ist wie bei der
 * Konfigurationsoberfläche mit einem Button und einem Label befüllt. Der Button
 * ist zum Start des Spiels da. Das Label gibt Statusmeldungen über das Spiel.
 * <br><br>
 * <figure> <img src="doc-files/GameScreen.jpg" widht="300" height="300" alt=
 * "UI-Aufbau des Spielebildschirms" title="UI-Aufbau des Spielebildschirms"
 * /> <figcaption>UI-Aufbau des Spielebildschirms</figcaption> </figure>
 * <p>
 * weiter schreiben-----
 * </p>
 */
public class GameController implements Initializable {

	@FXML private MenuBar menuBar;
	@FXML private Menu menuDatei;
	@FXML private Menu menuSpiel;
	@FXML private MenuItem menuItem_LoadGame;
	@FXML private MenuItem menuItem_PlayerOWinsCurrentSet;
	@FXML private MenuItem menuItem_PlayerXWinsCurrentSet;
	@FXML private MenuItem menuItem_StopCurrentSet;

	@FXML private Button newButton;
	@FXML private Button statsButton;
	@FXML private Button startButton;

	@FXML private Text tfPlayer1_Points;
	@FXML private Text tfPlayer2_Points;
	@FXML private Text tfPlayer1_Set_Wins;
	@FXML private Text tfPlayer2_Set_Wins;
	
	@FXML private Text spielerOMoveHistory;
	@FXML private Text spielerXMoveHistory;
	
	@FXML private Label lGameOverview;

	@FXML private Circle circlePlayerO;
	@FXML private Circle circlePlayerX;

	@FXML private Label infostat;
	@FXML private GridPane gamefieldGrid;

	private DatabaseManager databaseManager;
	private GameProperties gameProperties;
	private Gamefield gamefield;
	
	private KI_MinMax ki_minMax;
	private AgentKI agent;
	private char myPlayer;
	private char opponentPlayer;
	private String usedInterface;
	private int currentGameId;
	private int currentSetNr;
	private int zugNrCounter;
	private String startPlayer;
	private DatabaseSetRecord selectedSetFromLoadScreen;
	private PusherEventReaderService pusherEventReaderService;
	private ServerfileReaderService serverFileReaderService;
	private List<Integer> myPlayerMoveHistory;
	private List<Integer> opponentPlayerMoveHistory;

	/**
	 * Aktualisiert den Gewinner des aktuellen Spielsatzes.
	 *
	 * @param event
	 *            Ein Event repräsentiert eine Art von Aktion. Dieser Event-Type
	 *            wird benutzt um z.B.auf einen Button-Klick zu reagieren.
	 * @see {@link javafx.event.ActionEvent}
	 */
	@FXML
	void updateWinnerOfCurrentSet(ActionEvent event) {
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
	 * Stoppt den aktuell laufenden Spielsatz.
	 *
	 * @param event
	 *            {@link javafx.event.ActionEvent}
	 */
	@FXML
	void stopCurrentSet(ActionEvent event) {
		infostat.setText("Satz gestoppt. Kein Gewinner!");
		startButton.setDisable(false);
		menuDatei.setDisable(false);
		menuItem_PlayerOWinsCurrentSet.setDisable(false);
		menuItem_PlayerXWinsCurrentSet.setDisable(false);
		menuItem_StopCurrentSet.setDisable(true);

		if (pusherEventReaderService != null) {
			pusherEventReaderService.cancel();
		}
		if (serverFileReaderService != null) {
			serverFileReaderService.cancel();
		}

	}

	/**
	 * Zeigt den Bildschirm zum Laden von Sätzen.
	 *
	 * @param event
	 *            {@link javafx.event.ActionEvent}
	 */
	@FXML
	void loadGame(ActionEvent event) {
		try {
			System.out.println("Switching to Load Screen");
			showLoadScreen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Zeigt den Bildschirm zum Starten eines neuen Spiels.
	 *
	 * @param event
	 *            {@link javafx.event.ActionEvent}
	 */
	@FXML
	void newGame(ActionEvent event) {
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
	 * @param selectedSetFromLoadScreen
	 *            ausgewählter Satz aus der Datenbank bzw. dem Lade-Bildschirm.
	 */
	public void initController(DatabaseSetRecord selectedSetFromLoadScreen) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.fxml.Initializable#initialize(java.net.URL,
	 * java.util.ResourceBundle)
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
		myPlayer = gameProperties.getProperty(GameProperties.SPIELER).charAt(0);
		opponentPlayer = setOpponentPlayer();

		animateCurrentPlayer();

		startButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				startSet();
				determineCurrentGameIdAndSetNr();
				lGameOverview.setText("Spiel: " + currentGameId + "\nSatz: " + currentSetNr);

				if (usedInterface.equals("File")) {
					startFileInterfaceGame();
				}
				if (usedInterface.equals("Push")) {
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
		agent = new AgentKI("wir");
		ki_minMax = new KI_MinMax(gamefield, 'o');
		databaseManager = DatabaseManager.getInstance();
		
		myPlayerMoveHistory = new ArrayList<Integer>();
		opponentPlayerMoveHistory = new ArrayList<Integer>();
	}

	private void startPusherInterfaceGame() {
		String appKey = gameProperties.getProperty(GameProperties.APP_KEY);
		String appSecret = gameProperties.getProperty(GameProperties.APP_SECRET);

		PusherConnector pusher = new PusherConnector("", appKey, appSecret);

		// pusherEventReaderService hört nach neuen Events, aber in
		// hintergrund-thread durch task!
		pusherEventReaderService = new PusherEventReaderService();

		PrivateChannel channel = pusher.subscribeToPrivateChannel("private-channel", pusherEventReaderService,
				"MoveToAgent");

		zugNrCounter = 1;

		pusherEventReaderService.setOnSucceeded(e -> {
			// Gegnerzug verarbeiten
			if (pusherEventReaderService.getValue().getGegnerzug() != -1) {
				int opponentMove = pusherEventReaderService.getValue().getGegnerzug();
				gamefield.insertCoin(gamefieldGrid, opponentMove, opponentPlayer);
				updateOpponentPlayerHistory(opponentPlayerMoveHistory, opponentMove);
				try {
					databaseManager.addMove(currentGameId, currentSetNr, zugNrCounter, opponentMove,
							"spieler" + String.valueOf(opponentPlayer));
					
					if (zugNrCounter == 1){
						startPlayer = "Spieler " + String.valueOf(opponentPlayer).toUpperCase();
						databaseManager.updateStartPlayerOfSet(currentGameId, currentSetNr, startPlayer);
					}
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
				updateMyPlayerMoveHistory(myPlayerMoveHistory, move);
				try {
					databaseManager.addMove(currentGameId, currentSetNr, zugNrCounter, move,
							"spieler" + String.valueOf(myPlayer));
					if (zugNrCounter == 1){
						startPlayer = "Spieler " + String.valueOf(myPlayer).toUpperCase();
						databaseManager.updateStartPlayerOfSet(currentGameId, currentSetNr, startPlayer);
					}
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
			if (serverFileReaderService.getValue().getGegnerzug() != -1) {
				int opponentMove = serverFileReaderService.getValue().getGegnerzug();
				gamefield.insertCoin(gamefieldGrid, opponentMove, opponentPlayer);
				updateOpponentPlayerHistory(opponentPlayerMoveHistory, opponentMove);
				try {
					databaseManager.addMove(currentGameId, currentSetNr, zugNrCounter, opponentMove,
							"spieler" + String.valueOf(opponentPlayer));
					
					if (zugNrCounter == 1){
						startPlayer = "Spieler " + String.valueOf(opponentPlayer).toUpperCase();
						databaseManager.updateStartPlayerOfSet(currentGameId, currentSetNr, startPlayer);
					}
					zugNrCounter++;
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			
			// unseren Zug bestimmen und wegschicken
			if (!serverFileReaderService.getValue().isGameOver()) {
				int move = agent.calculateMove(gamefield);
				System.out.println("Move From KI: " + move);
				gamefield.insertCoin(gamefieldGrid, move, myPlayer);
				updateMyPlayerMoveHistory(myPlayerMoveHistory, move);
				try {
					agentfileWriter.writeAgentfile(new Agentfile(move));
					databaseManager.addMove(currentGameId, currentSetNr, zugNrCounter, move,
							"spieler" + String.valueOf(myPlayer));
					if (zugNrCounter == 1){
						startPlayer = "Spieler " + String.valueOf(myPlayer).toUpperCase();
						databaseManager.updateStartPlayerOfSet(currentGameId, currentSetNr, startPlayer);
					}
					zugNrCounter++;
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				serverFileReaderService.restart();
			} else {
				finishSet(serverFileReaderService.getValue().getSieger());
			}
		});

		serverFileReaderService.start();
	}
	
	
	private void determineCurrentGameIdAndSetNr() {
		if (selectedSetFromLoadScreen == null) {
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

	private int addNewGameToDb() {
		int newGameId = -1;
		try {
			newGameId = databaseManager.addGame("spielerO", "spielerX", null, new Date().toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newGameId;
	}

	private void addNewSetToGameInDb(int gameId, int setNr) {
		try {
			databaseManager.addSet(gameId, setNr, 0, 0, "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void startSet() {
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

	private void updateTextFields() throws SQLException{
		updateGameAndSetTextFields();
	}
	
	private void updateOpponentPlayerHistory(List<Integer> moveHistory, int move){
		moveHistory.add(move);
		
		List <Integer> last5Moves = getLastMovesFromMoveHistory(moveHistory, 5);
		
		Iterator<Integer> myListIterator = last5Moves.iterator(); 

	    spielerXMoveHistory.setText("");
	    
		while (myListIterator.hasNext()) {
		    Integer moveFromList = myListIterator.next();     

		    String moveHistoryOfTextField = spielerXMoveHistory.getText();

			if (myListIterator.hasNext()){
			    moveHistoryOfTextField = moveHistoryOfTextField + "Spalte: " + String.valueOf(moveFromList) + "\n";
			} else {
				moveHistoryOfTextField = moveHistoryOfTextField + "-> Spalte: " + String.valueOf(moveFromList) + "\n";
			}
		    spielerXMoveHistory.setText(moveHistoryOfTextField);
		}
	}
	
	private void updateMyPlayerMoveHistory(List<Integer> moveHistory, int move){
		//moveHistory
		moveHistory.add(move);
		
		List <Integer> last5Moves = getLastMovesFromMoveHistory(moveHistory, 5);
		
		Iterator<Integer> myListIterator = last5Moves.iterator(); 

	    spielerOMoveHistory.setText("");
	    
		while (myListIterator.hasNext()) {
		    Integer moveFromList = myListIterator.next();     
		    
		    String moveHistoryOfTextField = spielerOMoveHistory.getText();
		    
		    if (myListIterator.hasNext()){
			    moveHistoryOfTextField = moveHistoryOfTextField + "Spalte: " + String.valueOf(moveFromList) + "\n";
			} else {
				moveHistoryOfTextField = moveHistoryOfTextField + "-> Spalte: " + String.valueOf(moveFromList) + "\n";
			}
		    spielerOMoveHistory.setText(moveHistoryOfTextField);
		}
	}
	private List<Integer> getLastMovesFromMoveHistory(List<Integer> moveHistory, int numMoves){
		if (moveHistory.size() > 5){
			int lastIndex = moveHistory.size() - 1;
			return moveHistory.subList(lastIndex - numMoves, lastIndex);
		}
		return moveHistory;
	
	}
	
	private void updateGameAndSetTextFields() throws SQLException{
		ResultSet setWinners = databaseManager.getAllSetWinnersForGameId(String.valueOf(currentGameId));

		int setWinsPlayer1 = 0;
		int setWinsPlayer2 = 0;

		while (setWinners.next()) {
			String winner = setWinners.getString("sieger");
			if (winner.equals("Spieler O")) {
				setWinsPlayer1++;
			} else if (winner.equals("Spieler X")) {
				setWinsPlayer2++;
			}
		}
		tfPlayer1_Set_Wins.setText(String.valueOf(setWinsPlayer1));
		tfPlayer2_Set_Wins.setText(String.valueOf(setWinsPlayer2));
		tfPlayer1_Points.setText(String.valueOf(setWinsPlayer1 * 2));
		tfPlayer2_Points.setText(String.valueOf(setWinsPlayer2 * 2));
	}

	private char setOpponentPlayer(){
		if (myPlayer == 'o') {
			return 'x';
		} else {
			return 'o';
		}
	}
	
	private void animateCurrentPlayer() {
		Circle circle = getCircleForPlayer(myPlayer);
		
		FadeTransition fade = new FadeTransition(Duration.millis(500), circle);
		fade.setFromValue(1);
		fade.setToValue(0);
		fade.setAutoReverse(true);
		fade.setCycleCount(Timeline.INDEFINITE);
		fade.play();
	}

	private Circle getCircleForPlayer(char player){
		if (player == 'o')
			return circlePlayerO;
		else 
			return circlePlayerX;
	}
	private void readPropertiesFromFileSystem() {
		InputStream input = null;

		try {
			input = new FileInputStream(GameProperties.DATEINAME);
			gameProperties.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
