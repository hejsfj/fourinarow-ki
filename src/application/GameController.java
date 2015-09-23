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
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class GameController implements Initializable {
	
    @FXML //  fx:id="myButton"
    private Button newButton; // Value injected by FXMLLoader
    
    @FXML
    private Button statsButton; // Value injected by FXMLLoader
    
	@FXML
    private Button startButton;

	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		assert newButton != null : "fx:id=\"newButton\" was not injected: check your FXML file";
		assert statsButton != null : "fx:id=\"statsButton\" was not injected: check your FXML file";
		assert startButton != null : "fx:id=\"statsButton\" was not injected: check your FXML file";
		
        newButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Load it!");
          
                Stage stage; 
  
                   //get reference to the button's stage         
                   stage=(Stage)newButton.getScene().getWindow();
                   //load up OTHER FXML document
                   
                	AnchorPane page;
					try {
						page = (AnchorPane) FXMLLoader.load(getClass().getResource("Start.fxml"));
						//create a new scene with root and set the stage
		                 Scene scene = new Scene(page);
		                 stage.setScene(scene);
		                 stage.show();
					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}    
            }    
        });
		
		statsButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Zum neuen Spiel");

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
		});//endSetOnActionStartButton
		
		startButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Zum neuen Spiel");

				Stage stage;

				// get reference to the button's stage
				stage = (Stage) startButton.getScene().getWindow();
				// load up OTHER FXML document

				AnchorPane page;
				try {
					page = (AnchorPane) FXMLLoader.load(getClass().getResource("Start.fxml"));
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
		
	}
   
}

