package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class StartController implements Initializable {
	GameProperties gameProperties;
	
	@FXML private Button btStart;
	@FXML private Button btStats;	

	@FXML private RadioButton rbPush;
	@FXML private RadioButton rbFile;	
	
	@FXML private RadioButton rbRot;
	@FXML private RadioButton rbGelb;
	
	@FXML private Label lZugZeit;	
	@FXML private Label lZugZeitInfo;
	@FXML private TextField tfZugZeit;
	
	@FXML private TextField tfDateiPfad;	
	@FXML private Label lDateiPfad;	
	
	@FXML private Label lAppKey;	
	@FXML private Label lAppId;	
	@FXML private Label lAppSecret;	
	@FXML private TextField tfAppKey;
	@FXML private TextField tfAppId;
	@FXML private TextField tfAppSecret;
	
	Character player;
	int zugZeit;
	

	@Override 
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		assert btStart != null : "fx:id=\"startButton\" was not injected: check your FXML file";
		assert btStats != null : "fx:id=\"statsButton\" was not injected: check your FXML file";
		assert rbPush != null : "fx:id=\"rbPush\" was not injected: check your FXML file";
		assert rbFile != null : "fx:id=\"rbFile\" was not injected: check your FXML file";

		gameProperties = new GameProperties();
		
		 if (new File(GameProperties.DATEINAME).exists()) {
			readProperties();	
			restoreInputFieldsFromProperties();
		 } else {
			setDefaultInterface("File");
		 }

		setTextfieldHints();
		
		btStart.setOnAction((ev) -> 	{
			if (!areAllRequiredFieldsFilled())
				//maybe show popup or something else to warn the user
				return;
			saveUserInputToPropertiesFile();
			System.out.println("Game startet");
			
			Stage stage;

			// get reference to the button's stage
			stage = (Stage) btStart.getScene().getWindow();
			// load up OTHER FXML document

			AnchorPane page;
			try {
				page = (AnchorPane) FXMLLoader.load(getClass().getResource("Game.fxml"));
				// create a new scene with root and set the stage
				Scene scene = new Scene(page);
				stage.setScene(scene);
				stage.show();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			});//endSetOnActionStartButton

		btStats.setOnAction((ev)-> 
		{
				System.out.println("Stats startet");

				Stage stage;

				// get reference to the button's stage
				stage = (Stage) btStats.getScene().getWindow();
				// load up OTHER FXML document

				AnchorPane page;
				try {
					page = (AnchorPane) FXMLLoader.load(getClass().getResource("Stats.fxml"));
					// create a new scene with root and set the stage
					Scene scene = new Scene(page);
					stage.setScene(scene);
					stage.show();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}); //endSetOnActionStatsButton

		rbFile.setOnAction((ev) -> {
			System.out.println(rbFile.getText() + " ausgewählt");
			setVisibilityOfPusherInterfaceFields(false);
			setVisibilityOfFileInterfaceFields(true);
		});//endSetOnActionRbFile
		
		rbPush.setOnAction((ev) -> {
			System.out.println(rbPush.getText() + " ausgewählt");
			setVisibilityOfPusherInterfaceFields(true);
			setVisibilityOfFileInterfaceFields(false);
		}); //endSetOnActionRbPush
		
		rbGelb.setOnAction((ev) -> {
			player = 'o';
		});//endRbGelb

		rbRot.setOnAction((ev) -> {
			player = 'x';
		});//endRbGelb
		
		tfZugZeit.textProperty().addListener((observer,alt,neu)->{
			lZugZeitInfo.setText("in ms");
			
			try{
			if(!(neu.matches("\\d*"))){
        		System.out.println("Ungültige Eingabe der Milisekunden");
        		lZugZeitInfo.setText("Ungültige Eingabe");
        	}//endIf
			else{
				if(!(neu.equals(""))){
					zugZeit = Integer.parseInt(neu);
					System.out.println("Eingestellte Zugzeit: "+ zugZeit);	
				}//endIF
				else{
					lZugZeitInfo.setText("in ms");
				}//endElse
				
			}//endElse
			}catch(NumberFormatException nfe){
				lZugZeitInfo.setText("Ungültige Eingabe");
				tfZugZeit.setText(alt);
			}//endCatch
		});//endTfZugZeitTextListener
	}//endInitialize


	private boolean areAllRequiredFieldsFilled() {
		if (!isPlayerSelected()){
			System.out.println("no player selected");
			return false;
		}
		if (!isZugZeitRegistered()){
			System.out.println("no zugzeit registered");
			return false;
		}
		if (rbFile.isSelected())
			return areFileInterfaceFieldsFilled();
		else if (rbPush.isSelected())
			return arePusherInterfaceFieldsFilled();
		else {
			System.out.println("not all fields filled");
			return false;
		}
	}

	private boolean isPlayerSelected(){
		return rbRot.isSelected() || rbGelb.isSelected();
	}
	private boolean isZugZeitRegistered(){
		return !tfZugZeit.getText().equals("");
	}
	private boolean areFileInterfaceFieldsFilled(){
		System.out.println("not all file fields filled");
		return !tfDateiPfad.getText().equals("");		
	}
	private boolean arePusherInterfaceFieldsFilled(){
		System.out.println("not all pusher fields filled");
		return	 !tfAppId.getText().equals("")		&&
				 !tfAppKey.getText().equals("")		&&
				 !tfAppSecret.getText().equals("");
	}

	private void restoreInputFieldsFromProperties() {
		setDefaultInterface(gameProperties.getProperty(GameProperties.INTERFACE));
		
		if ((GameProperties.SPIELER).equals("o"))
			rbGelb.setSelected(true);
		else
			rbRot.setSelected(true);
		
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

			gameProperties.setProperty(GameProperties.INTERFACE,  	(rbFile.isSelected()) ? "File" : "Push");
			gameProperties.setProperty(GameProperties.ZUGZEIT, 		tfZugZeit.getText());
			gameProperties.setProperty(GameProperties.DATEIPFAD,	tfDateiPfad.getText());
			gameProperties.setProperty(GameProperties.APP_ID, 		tfAppId.getText());
			gameProperties.setProperty(GameProperties.APP_KEY, 		tfAppKey.getText());
			gameProperties.setProperty(GameProperties.APP_SECRET, 	tfAppSecret.getText());
			gameProperties.setProperty(GameProperties.SPIELER,  	(rbRot.isSelected()) ? "x" : "o");
			
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
			rbFile.setSelected(true);
			setVisibilityOfPusherInterfaceFields(false);
		}
		else if (defaultInterface.equals("Push")){
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
	}

	private void setTextfieldHints() {
		tfZugZeit.setPromptText("1000");
		tfDateiPfad.setPromptText("C:/sharedFolder");
		tfAppId.setPromptText("123456");
		tfAppKey.setPromptText("93c4a752a14cbeef7216");
		tfAppSecret.setPromptText("adcd6bab9a922980c892");
	} 
}//endClass
