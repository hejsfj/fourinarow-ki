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
	
	Character player;
	

	@Override // This method is called by the FXMLLoader when initialization is
				// complete
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		assert btStart != null : "fx:id=\"startButton\" was not injected: check your FXML file";
		assert btStats != null : "fx:id=\"statsButton\" was not injected: check your FXML file";
		assert rbPush != null : "fx:id=\"rbPush\" was not injected: check your FXML file";
		assert rbFile != null : "fx:id=\"rbFile\" was not injected: check your FXML file";
		
		
		/*Start Event*/
		
		btStart.setOnAction((ev) -> 	{
			
			System.out.println("Game startet");
			
			//TO DO File Interface Einsatz
			AgentfileWriter agentFileWriter = new AgentfileWriter(tfDateiPfad.getText(), player);
			System.out.println("FileInterface Spieler: "+ agentFileWriter.getPlayer());	
			System.out.println("FileInterface Sharedfolder: "+ agentFileWriter.getSharedFolderPath());		
			
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
	
		}//endInitialize

        
}//endClass
