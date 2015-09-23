package application;

import java.awt.Label;
import java.awt.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.RadioButton;




public class StartController implements Initializable {
	
	/*Steuerelemente*/

	@FXML // fx:id="myButton"
	private Button startButton; // Value injected by FXMLLoader

	@FXML
	private Button statsButton;

	@FXML
	private RadioButton rbPush;

	@FXML
	private RadioButton rbFile;
	
	
	private TextField tfZugZeit;
	
	
	private TextField tfDateiPfad;
	
	
	private TextField tfAppKey;
	
	
	private TextField tfAppId;
	
	
	private TextField tfAppSecret;
	
	
	private Label lZugZeit;
	
	
	private Label lDateiPfad;
	
	
	private Label lAppKey;
	
	
	private Label lAppId;
	
	
	private Label lAppSecret;
	

	@Override // This method is called by the FXMLLoader when initialization is
				// complete
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		assert startButton != null : "fx:id=\"startButton\" was not injected: check your FXML file";
		assert statsButton != null : "fx:id=\"statsButton\" was not injected: check your FXML file";
		assert rbPush != null : "fx:id=\"rbPush\" was not injected: check your FXML file";
		assert rbFile != null : "fx:id=\"rbFile\" was not injected: check your FXML file";

		startButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				System.out.println("That was easy, wasn't it?");

				Stage stage;

				// get reference to the button's stage
				stage = (Stage) startButton.getScene().getWindow();
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
			
			}
		
			});//endSetOnActionStartButton

		statsButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("That was easy, wasn't it?");

				Stage stage;

				// get reference to the button's stage
				stage = (Stage) statsButton.getScene().getWindow();
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
			}
		}); //endSetOnActionStatsButton

		rbPush.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

//				System.out.println("3 Einstellungen");
//
//				Stage stage;
//
//				// get reference to the button's stage
//				stage = (Stage) pushButton.getScene().getWindow();
//				// load up OTHER FXML document
//
//				AnchorPane page;
//				try {
//					page = (AnchorPane) FXMLLoader.load(getClass().getResource("Stats.fxml"));
//					// create a new scene with root and set the stage
//					Scene scene = new Scene(page);
//					stage.setScene(scene);
//					stage.show();
//
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

				System.out.println("3 Einstellungen");

				Stage stage;

				// get reference to the button's stage
				stage = (Stage) rbPush.getScene().getWindow();
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
			} 
		}); //endSetOnActionPushButton
		
		rbFile.setOnAction((ev) -> fileButtonAnsicht(ev));
		rbPush.setOnAction((ev) -> pushButtonAnsicht(ev));
	
	

//		private Pane createButtonBasedEditor(String initialText) {		
//		startButton.setDefaultButton(true);
//        startButton.setDisable(true);
// 
//        tfZugZeit.textProperty.addTextListener(() -> {
//            startButton.setDisable(false);
//        });
//	    startButton.setOnAction((event) -> 
//	        tfZugZeit.setText(tfZugZeit.getText()));
//	        startButton.setDisable(false);
	   

	
		}//endInitialize

	private void fileButtonAnsicht(ActionEvent ev){
		// TODO Auto-generated method stub
        RadioButton rb = (RadioButton) ev.getSource();
        System.out.printf("%s%n", rb.getText());
        
	}//endFileButtonAusgabe
	
	private void pushButtonAnsicht(ActionEvent ev){
		RadioButton rb = (RadioButton) ev.getSource();
		System.out.printf("%s%n", rb.getText());
		
	}//endFileButtonAnsicht
        
}//endClass