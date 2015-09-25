package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import fileInterface.*;

public class StartController implements Initializable {
	
	final String propertiesFileName = "client.ini";
	Properties properties;
	/*Steuerelemente deklarieren*/
	@FXML 
	private Button btStart; // Value injected by FXMLLoader

	@FXML
	private Button btStats;

	@FXML
	private RadioButton rbPush;

	@FXML
	private RadioButton rbFile;
	
	@FXML
	private RadioButton rbRot;

	@FXML
	private RadioButton rbGelb;
	
	@FXML
	private TextField tfZugZeit;
	
	@FXML
	private TextField tfDateiPfad;
	
	@FXML
	private TextField tfAppKey;
	
	@FXML
	private TextField tfAppId;
	
	@FXML
	private TextField tfAppSecret;
	
	@FXML
	private Label lZugZeit;
	
	@FXML
	private Label lDateiPfad;
	
	@FXML
	private Label lAppKey;
	
	@FXML
	private Label lAppId;
	
	@FXML
	private Label lAppSecret;
	
	@FXML
	private Label lZugZeitInfo;
	
	/*
	Variablen zur weiteren Verarbeitung 
	TO DO: Wie Interagiert das UI mit den Funktionalen Klassen??
	Erster Versuch mit lokalen Variablen der Controller Klasse
	*/
	Character player;
	int zugZeit;
	

	@Override // This method is called by the FXMLLoader when initialization is
				// complete
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		assert btStart != null : "fx:id=\"startButton\" was not injected: check your FXML file";
		assert btStats != null : "fx:id=\"statsButton\" was not injected: check your FXML file";
		assert rbPush != null : "fx:id=\"rbPush\" was not injected: check your FXML file";
		assert rbFile != null : "fx:id=\"rbFile\" was not injected: check your FXML file";

		// wenn properties-file vorhanden, dann benutzen
		 if (new File(propertiesFileName).exists()) {
			properties = new Properties();
			readProperties();	
			restoreInputFieldsFromProperties();
		 } else {
			setDefaultInterface("File");
		 }

		setTextfieldHints();
		/*Start Event*/
		
		btStart.setOnAction((ev) -> 	{
			
			saveUserInputToPropertiesFile();
			System.out.println("Game startet");
			
			//TO DO File Interface Einsatz
//			AgentfileWriter agentFileWriter = new AgentfileWriter(tfDateiPfad.getText(), player);
//			System.out.println("FileInterface Spieler: "+ agentFileWriter.getPlayer());	
//			System.out.println("FileInterface Sharedfolder: "+ agentFileWriter.getSharedFolderPath());		
			
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

		/*Statistik Event*/
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


	private void restoreInputFieldsFromProperties() {
		setDefaultInterface(properties.getProperty("interface"));
		
		tfZugZeit.setText(properties.getProperty("zugzeit"));
		tfDateiPfad.setText(properties.getProperty("kontaktpfad"));
		tfAppId.setText(properties.getProperty("appId"));
		tfAppKey.setText(properties.getProperty("appKey"));
		tfAppSecret.setText(properties.getProperty("appSecret"));		
	}

	private void readProperties() {
		InputStream input = null;
    	
    	try {
    		input = new FileInputStream(propertiesFileName);
    		//load a properties file from class path, inside static method
    		properties.load(input);
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
		Properties newProperties = new Properties();
		OutputStream output = null;

		try {
			output = new FileOutputStream(propertiesFileName);

			newProperties.setProperty("interface",  (rbFile.isSelected()) ? "File" : "Push");
			newProperties.setProperty("zugzeit", tfZugZeit.getText());
			newProperties.setProperty("kontaktpfad", tfDateiPfad.getText());
			newProperties.setProperty("appId", tfAppId.getText());
			newProperties.setProperty("appKey", tfAppKey.getText());
			newProperties.setProperty("appSecret", tfAppSecret.getText());

			newProperties.store(output, null);

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
