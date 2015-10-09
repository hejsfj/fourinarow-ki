package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

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

	private GameProperties gameProperties;
	private Character player;
	private String usedInterface;
	private int zugZeit;
	
    @FXML void loadGame(ActionEvent event) {    
		Stage stage;
		stage = (Stage) btStart.getScene().getWindow();
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
    	//disabled
    }
    
    @FXML void choose(ActionEvent event) {
    	Stage stage = new Stage();
    	
        DirectoryChooser directoryChooser = new DirectoryChooser();
        
        File initialDirectory = new File(tfDateiPfad.getText().toString());        
        if (initialDirectory.exists()){
        	directoryChooser.setInitialDirectory(initialDirectory);
        }
    	File selectedDirectory = directoryChooser.showDialog(stage);
    	if(selectedDirectory != null){
    		selectedDirectory.getAbsolutePath();
    		tfDateiPfad.setText(selectedDirectory.getPath());
    	}    	
    }  
    
	@Override 
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		assert LoadGame != null : "fx:id=\"LoadGame\" was not injected: check your FXML file 'Settings.fxml'.";
        assert NewGame != null : "fx:id=\"NewGame\" was not injected: check your FXML file 'Settings.fxml'.";
        assert toggleGroup_Interface != null : "fx:id=\"toggleGroup_Interface\" was not injected: check your FXML file 'Settings.fxml'.";
        assert toggleGroup_Player != null : "fx:id=\"toggleGroup_Player\" was not injected: check your FXML file 'Settings.fxml'.";
        assert btStart != null : "fx:id=\"btStart\" was not injected: check your FXML file 'Settings.fxml'.";
        assert findFile != null : "fx:id=\"findFile\" was not injected: check your FXML file 'Settings.fxml'.";
        assert infostart != null : "fx:id=\"infostart\" was not injected: check your FXML file 'Settings.fxml'.";
        assert lAppId != null : "fx:id=\"lAppId\" was not injected: check your FXML file 'Settings.fxml'.";
        assert lAppKey != null : "fx:id=\"lAppKey\" was not injected: check your FXML file 'Settings.fxml'.";
        assert lAppSecret != null : "fx:id=\"lAppSecret\" was not injected: check your FXML file 'Settings.fxml'.";
        assert lDateiPfad != null : "fx:id=\"lDateiPfad\" was not injected: check your FXML file 'Settings.fxml'.";
        assert lZugZeit != null : "fx:id=\"lZugZeit\" was not injected: check your FXML file 'Settings.fxml'.";
        assert rbFile != null : "fx:id=\"rbFile\" was not injected: check your FXML file 'Settings.fxml'.";
        assert rbGelb != null : "fx:id=\"rbGelb\" was not injected: check your FXML file 'Settings.fxml'.";
        assert rbPush != null : "fx:id=\"rbPush\" was not injected: check your FXML file 'Settings.fxml'.";
        assert rbRot != null : "fx:id=\"rbRot\" was not injected: check your FXML file 'Settings.fxml'.";
        assert tfAppId != null : "fx:id=\"tfAppId\" was not injected: check your FXML file 'Settings.fxml'.";
        assert tfAppKey != null : "fx:id=\"tfAppKey\" was not injected: check your FXML file 'Settings.fxml'.";
        assert tfAppSecret != null : "fx:id=\"tfAppSecret\" was not injected: check your FXML file 'Settings.fxml'.";
        assert tfDateiPfad != null : "fx:id=\"tfDateiPfad\" was not injected: check your FXML file 'Settings.fxml'.";
        assert tfZugZeit != null : "fx:id=\"tfZugZeit\" was not injected: check your FXML file 'Settings.fxml'.";

		gameProperties = new GameProperties();
		
		 if (new File(GameProperties.DATEINAME).exists()) {
			readProperties();	
			restoreInputFieldsFromProperties();
		 } else {
			setDefaultInterface("File");
		 }

		setTextfieldHints();
		
		btStart.setOnAction((ev) -> 	{
			if (!areAllRequiredFieldsFilledCorrectly()){
				infostart.setText("Nicht alle ben�tigten Felder wurden richtig ausgef�llt");
				return;
			}
			saveUserInputToPropertiesFile();
			// get reference to the button's stage
			Stage stage = (Stage) btStart.getScene().getWindow();
			// load up OTHER FXML document
			AnchorPane page;
			try {
				page = (AnchorPane) FXMLLoader.load(getClass().getResource("Game.fxml"));
				// create a new scene with root and set the stage
				Scene scene = new Scene(page);
				stage.setScene(scene);
				stage.show();
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
		
		tfZugZeit.textProperty().addListener((observer,alt,neu)->{						
			try {
				if(!(neu.matches("\\d*"))){
	        		System.out.println("Ung�ltige Eingabe der Milisekunden");
	        		infostart.setText("Ung�ltige Eingabe");
				}
				else if(!(neu.equals(""))){
					zugZeit = Integer.parseInt(neu);
					System.out.println("Eingestellte Zugzeit: "+ zugZeit);	
				}				
			} catch(NumberFormatException nfe){
				infostart.setText("Ung�ltige Eingabe");
				tfZugZeit.setText(alt);
			}
		});
	}

	private boolean areAllRequiredFieldsFilledCorrectly() {
		if (!isPlayerSelected()){
			System.out.println("no player selected");
			return false;
		}
		if (!isZugZeitValid()){
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
		if (!isZugZeitRegistered()){
			return false;
		}
		if (infostart.getText().equals("Ung�ltige Eingabe")){
			return false;
		}
		return true;
	}

	private boolean isPlayerSelected(){
		return rbRot.isSelected() || rbGelb.isSelected();
	}
	private boolean isZugZeitRegistered(){
		return !tfZugZeit.getText().equals("");
	}
	private boolean areFileInterfaceFieldsFilledCorrectly(){
		
		return !tfDateiPfad.getText().equals("") &&
				new File(tfDateiPfad.getText()).exists();
	}
	private boolean arePusherInterfaceFieldsFilledCorrectly(){
		return	 !tfAppId.getText().equals("")		&&
				 !tfAppKey.getText().equals("")		&&
				 !tfAppSecret.getText().equals("");
	}

	private void restoreInputFieldsFromProperties() {
		setDefaultInterface(gameProperties.getProperty(GameProperties.INTERFACE));
		
		System.out.println(GameProperties.SPIELER);
		player = gameProperties.getProperty(GameProperties.SPIELER).toCharArray()[0];
		if (player == 'o') {
			rbGelb.setSelected(true);
		}
		else if (player == 'x') {
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
        } finally{
        	if(input!=null){
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
			gameProperties.setProperty(GameProperties.INTERFACE,  	usedInterface);
			gameProperties.setProperty(GameProperties.ZUGZEIT, 		tfZugZeit.getText());
			gameProperties.setProperty(GameProperties.DATEIPFAD,	tfDateiPfad.getText());
			gameProperties.setProperty(GameProperties.APP_ID, 		tfAppId.getText());
			gameProperties.setProperty(GameProperties.APP_KEY, 		tfAppKey.getText());
			gameProperties.setProperty(GameProperties.APP_SECRET, 	tfAppSecret.getText());
			gameProperties.setProperty(GameProperties.SPIELER,  	player.toString());
			
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
		if (defaultInterface.equals("File")){
			usedInterface = "File";
			rbFile.setSelected(true);
			setVisibilityOfPusherInterfaceFields(false);
		}
		else if (defaultInterface.equals("Push")){
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
