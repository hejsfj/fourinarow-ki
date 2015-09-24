package application;


import java.io.IOException;
import java.net.URL;
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
		
		
/* Initial ist File Schnittstelle ausgewählt */ 
	rbFile.setSelected(true);
	lAppId.setVisible(false);
	lAppKey.setVisible(false);
	lAppSecret.setVisible(false);
	tfAppId.setVisible(false);
	tfAppKey.setVisible(false);
	tfAppSecret.setVisible(false);	
	lDateiPfad.setVisible(true);
	tfDateiPfad.setVisible(true);
	
	/* Hinweise für Benutzereingaben */
	
	tfZugZeit.setText("1000");
	tfDateiPfad.setText("C:/sharedFolder");
	tfAppId.setText("123456");
	tfAppKey.setText("93c4a752a14cbeef7216");
	tfAppSecret.setText("adcd6bab9a922980c892");
		
		/*Start Event*/
		
		btStart.setOnAction((ev) -> 	{
			
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
			System.out.println(rbFile.getText()+" ausgewählt");
			lAppId.setVisible(false);
			lAppKey.setVisible(false);
			lAppSecret.setVisible(false);
			tfAppId.setVisible(false);
			tfAppKey.setVisible(false);
			tfAppSecret.setVisible(false);	
			lDateiPfad.setVisible(true);
			tfDateiPfad.setVisible(true);
				
			
		});//endSetOnActionRbFile
		
		rbPush.setOnAction((ev) -> {
			System.out.println(rbPush.getText()+" ausgewählt");
			lDateiPfad.setVisible(false);
			tfDateiPfad.setVisible(false);
			lAppId.setVisible(true);
			lAppKey.setVisible(true);
			lAppSecret.setVisible(true);
			tfAppId.setVisible(true);
			tfAppKey.setVisible(true);
			tfAppSecret.setVisible(true);	

		
		}); //endSetOnActionRbPush
		
		rbGelb.setOnAction((ev) -> {
			player = 'o';
			
		});//endRbGelb

		rbRot.setOnAction((ev) -> {
			player = 'x';
			
		});//endRbGelb
		
		tfZugZeit.focusedProperty().addListener((observer,alt,neu)->{
			
			tfZugZeit.clear();		

		});//endTfZugZeitAufKlickListener
		
		tfDateiPfad.focusedProperty().addListener((observer,alt,neu)->{
			
			tfDateiPfad.clear();		

		});//endTfDateiPfadAufKlickListener
		
		tfAppId.focusedProperty().addListener((observer,alt,neu)->{
			
			tfAppId.clear();		

		});//endTfAppIdAufKlickListener
		
		tfAppKey.focusedProperty().addListener((observer,alt,neu)->{
			
			tfAppKey.clear();		

		});//endTfAppKeyAufKlickListener
		
		tfAppSecret.focusedProperty().addListener((observer,alt,neu)->{
			
			tfAppSecret.clear();		

		});//endTfSecretAufKlickListener
		
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

        
}//endClass
