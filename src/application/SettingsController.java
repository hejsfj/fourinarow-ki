package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import database.DatabaseSetRecord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * Klasse enthält die Anwendungslogik für die Settings.fxml-Datei, welche
 * den Konfigurations-Bildschrim repräsentiert. Beim Start des Programms wird
 * das Einstellungsfenster (Settings.fxml) aufgerufen.<br>
 * In diesem hat der Anwender die Möglichkeit auszuwählen, welche Startoptionen er
 * wählen möchte. Die eingetragenen Startwerte sind für den Spielstart
 * essentiell. Alle 5 Boxen wurden mit V-Boxen (Aufteilung der Darstellung in
 * horizontal angeordnete Blöcke) befüllt. Box 1 wurde mit einer Menüleiste und
 * einer H-Box (Aufteilung der Darstellung in vertikal angeordnete Blöcke)
 * befüllt. Das Menü umfasst zwei MenuItems, welche Links zu den weiteren
 * Fenstern des Programms führen. Darunter wurde der Text „Neues Spiel“
 * eingefügt. <br>
 * Die Boxen 2 und 4 wurden mit einer V-Box befüllt um die Box 3 in
 * der Mitte des Fensters zu fixieren. Box 3 wurde zunächst mit einer V-Box
 * befüllt, welche wiederum mit H-Boxen befüllt ist, die alle Eingabeoptionen
 * für den Spielstart bereitstellen.<br> 
 * Box 5 umfasst wie Box 1 die gesamte Breite
 * des Fensters und beinhaltet einen zentrierten Button, der zum
 * Spielfeldfenster (Game.fxml) weiterleitet, sobald die Startoptionen korrekt
 * eingetragen wurden. Ein zentriertes Label, welches sich unter dem Button
 * befindet, zeigt abhängigkeit der noch auszufüllenden Felder unterschiedliche 
 * Rückmeldungen an.
 * 
 * <br>
 *  <figure>
 *		<img src="doc-files/SettingsScreen.jpg" width="830" height="530" alt="UI Aufbau des Setting-Screens"
 *			title="UI Aufbau des Setting-Screens">
 *		<figcaption>UI Aufbau des Setting-Screens</figcaption>
 *	</figure>
 * <br>
 */
public class SettingsController implements Initializable {

	@FXML private ResourceBundle resources;
	@FXML private URL location;
	@FXML private MenuItem LoadGame;
	@FXML private MenuItem NewGame;

	@FXML private ToggleGroup toggleGroup_Interface;
	@FXML private ToggleGroup toggleGroup_Player;

	@FXML private Button btStart;
	@FXML private Button findFile;

	@FXML private Label infostart;
	@FXML private Label lAppId;
	@FXML private Label lAppKey;
	@FXML private Label lAppSecret;
	@FXML private Label lDateiPfad;
	@FXML private Label lZugZeit;

	@FXML private RadioButton rbFile;
	@FXML private RadioButton rbGelb;
	@FXML private RadioButton rbPush;
	@FXML private RadioButton rbRot;

	@FXML private TextField tfAppId;
	@FXML private TextField tfAppKey;
	@FXML private TextField tfAppSecret;
	@FXML private TextField tfDateiPfad;
	@FXML private TextField tfZugZeit;
	
	@FXML private Text text_NewGame;

	private GameProperties gameProperties;
	private DatabaseSetRecord selectedSetFromLoadScreen;
	
	private Character player;
	private String usedInterface;
	private int zugZeit;

	/**
	 * Zeigt den Bildschirm zum Laden von Sätzen an.
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
	 * Zeigt den Bildschirm zum Starten eines neuen Spiels an.
	 *
	 * @param event
	 *            {@link javafx.event.ActionEvent}
	 */
	@FXML
	void newGame(ActionEvent event) {
		// disabled
	}

	/**
	 * Wählt und überprüft den Pfad für den Dateiaustausch.
	 *
	 * @param event
	 *            {@link javafx.event.ActionEvent}
	 */
	@FXML
	void choose(ActionEvent event) {
		Stage stage = new Stage();

		DirectoryChooser directoryChooser = new DirectoryChooser();

		File initialDirectory = new File(tfDateiPfad.getText().toString());
		if (initialDirectory.exists()) {
			directoryChooser.setInitialDirectory(initialDirectory);
		}
		File selectedDirectory = directoryChooser.showDialog(stage);
		if (selectedDirectory != null) {
			selectedDirectory.getAbsolutePath();
			tfDateiPfad.setText(selectedDirectory.getPath());
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
		gameProperties = new GameProperties();

		if (new File(GameProperties.DATEINAME).exists()) {
			readProperties();
			restoreInputFieldsFromProperties();
		} else {
			setDefaultInterface("File");
		}

		setTextfieldHints();

		btStart.setOnAction((ev) -> {
			if (!areAllRequiredFieldsFilledCorrectly()) {
				infostart.setText("Nicht alle benötigten Felder wurden richtig ausgefüllt");
				return;
			}
			saveUserInputToPropertiesFile();

			try {
				System.out.println("Switching to Game Screen");
				showGameScreen();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		rbFile.setOnAction((ev) -> {
			usedInterface = "File";
			setVisibilityOfPusherInterfaceFields(false);
			setVisibilityOfFileInterfaceFields(true);
		});

		rbPush.setOnAction((ev) -> {
			usedInterface = "Push";
			setVisibilityOfPusherInterfaceFields(true);
			setVisibilityOfFileInterfaceFields(false);
		});

		rbGelb.setOnAction((ev) -> {
			player = 'o';
		});

		rbRot.setOnAction((ev) -> {
			player = 'x';
		});

		tfZugZeit.textProperty().addListener((observer, alt, neu) -> {
			try {
				if (!(neu.matches("\\d*"))) {
					System.out.println("Ungültige Eingabe der Milisekunden");
					infostart.setText("Ungültige Eingabe");
				} else if (!(neu.equals(""))) {
					zugZeit = Integer.parseInt(neu);
					System.out.println("Eingestellte Zugzeit: " + zugZeit);
				}
			} catch (NumberFormatException nfe) {
				infostart.setText("Ungültige Eingabe");
				tfZugZeit.setText(alt);
			}
		});
	}

	/**
	 * Initialisiert den Controller.
	 *
	 * @param selectedSetFromLoadScreen
	 *            Ausgewählter Datensatz aus der Datenbank.
	 */
	public void initController(DatabaseSetRecord selectedSetFromLoadScreen) {
		this.selectedSetFromLoadScreen = selectedSetFromLoadScreen;

		text_NewGame.setText("Spiel fortsetzen");
	}

	private Stage showGameScreen() throws IOException {
		Stage stage = (Stage) btStart.getScene().getWindow();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));

		stage.setScene(new Scene((Pane) loader.load()));

		if (selectedSetFromLoadScreen != null) {
			GameController gameController = loader.<GameController> getController();
			gameController.initController(selectedSetFromLoadScreen);
		}
		stage.show();
		return stage;
	}

	private Stage showLoadScreen() throws IOException {
		Stage stage = (Stage) btStart.getScene().getWindow();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Load.fxml"));

		stage.setScene(new Scene((Pane) loader.load()));

		stage.show();
		return stage;
	}

	private boolean areAllRequiredFieldsFilledCorrectly() {
		if (!isPlayerSelected()) {
			System.out.println("no player selected");
			return false;
		}
		if (!isZugZeitValid()) {
			System.out.println("zugzeit not valid");
			return false;
		}
		if (rbFile.isSelected())
			return areFileInterfaceFieldsFilledCorrectly();
		if (rbPush.isSelected())
			return arePusherInterfaceFieldsFilledCorrectly();

		System.out.println("not all fields filled");
		return false;
	}

	private boolean isZugZeitValid() {
		if (!isZugZeitRegistered()) {
			return false;
		}
		if (infostart.getText().equals("Ungültige Eingabe")) {
			return false;
		}
		return true;
	}

	private boolean isPlayerSelected() {
		return rbRot.isSelected() || rbGelb.isSelected();
	}

	private boolean isZugZeitRegistered() {
		return !tfZugZeit.getText().equals("");
	}

	private boolean areFileInterfaceFieldsFilledCorrectly() {

		return !tfDateiPfad.getText().equals("") && new File(tfDateiPfad.getText()).exists();
	}

	private boolean arePusherInterfaceFieldsFilledCorrectly() {
		return !tfAppId.getText().equals("") && !tfAppKey.getText().equals("") && !tfAppSecret.getText().equals("");
	}

	private void restoreInputFieldsFromProperties() {
		setDefaultInterface(gameProperties.getProperty(GameProperties.INTERFACE));

		player = gameProperties.getProperty(GameProperties.SPIELER).toCharArray()[0];
		if (player == 'o') {
			rbGelb.setSelected(true);
		} else if (player == 'x') {
			rbRot.setSelected(true);
		}

		tfZugZeit.setText(gameProperties.getProperty(GameProperties.ZUGZEIT));
		tfDateiPfad.setText(gameProperties.getProperty(GameProperties.DATEIPFAD));
		tfAppId.setText(gameProperties.getProperty(GameProperties.APP_ID));
		tfAppKey.setText(gameProperties.getProperty(GameProperties.APP_KEY));
		tfAppSecret.setText(gameProperties.getProperty(GameProperties.APP_SECRET));
	}

	private void readProperties() {
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

	private void saveUserInputToPropertiesFile() {
		OutputStream output = null;

		try {
			output = new FileOutputStream(GameProperties.DATEINAME);
			gameProperties.setProperty(GameProperties.INTERFACE, usedInterface);
			gameProperties.setProperty(GameProperties.ZUGZEIT, tfZugZeit.getText());
			gameProperties.setProperty(GameProperties.DATEIPFAD, tfDateiPfad.getText());
			gameProperties.setProperty(GameProperties.APP_ID, tfAppId.getText());
			gameProperties.setProperty(GameProperties.APP_KEY, tfAppKey.getText());
			gameProperties.setProperty(GameProperties.APP_SECRET, tfAppSecret.getText());
			gameProperties.setProperty(GameProperties.SPIELER, player.toString());

			gameProperties.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void setDefaultInterface(String defaultInterface) {
		if (defaultInterface.equals("File")) {
			usedInterface = "File";
			rbFile.setSelected(true);
			setVisibilityOfPusherInterfaceFields(false);
		} else if (defaultInterface.equals("Push")) {
			usedInterface = "Push";
			rbPush.setSelected(true);
			setVisibilityOfFileInterfaceFields(false);
		}
	}

	private void setVisibilityOfPusherInterfaceFields(boolean visible) {
		lAppId.setVisible(visible);
		lAppKey.setVisible(visible);
		lAppSecret.setVisible(visible);
		tfAppId.setVisible(visible);
		tfAppKey.setVisible(visible);
		tfAppSecret.setVisible(visible);

	}

	private void setVisibilityOfFileInterfaceFields(boolean visible) {
		lDateiPfad.setVisible(visible);
		tfDateiPfad.setVisible(visible);
		findFile.setVisible(visible);
	}

	private void setTextfieldHints() {
		tfZugZeit.setPromptText("1000");
		tfDateiPfad.setPromptText("C:/sharedFolder");
		tfAppId.setPromptText("123456");
		tfAppKey.setPromptText("93c4a752a14cbeef7216");
		tfAppSecret.setPromptText("adcd6bab9a922980c892");
	}
}
