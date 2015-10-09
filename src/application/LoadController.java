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
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoadController implements Initializable {
    @FXML private Button loadButton; // Value injected by FXMLLoader    
    @FXML private Button saveButton; // Value injected by FXMLLoader    
    @FXML private Button newButton; // Value injected by FXMLLoader    
    @FXML private Label infostat;
  
	@FXML void loadGame(ActionEvent event) {		
		Stage stage;
		stage = (Stage) saveButton.getScene().getWindow();
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
		Stage stage;
		stage = (Stage) saveButton.getScene().getWindow();
		AnchorPane page;
		
		try {
			page = (AnchorPane) FXMLLoader.load(getClass().getResource("Settings.fxml"));
			Scene scene = new Scene(page);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert loadButton != null : "fx:id=\"refreshButton\" was not injected: check your FXML file";
        assert saveButton != null : "fx:id=\"saveButton\" was not injected: check your FXML file";
        assert newButton != null : "fx:id=\"saveButton\" was not injected: check your FXML file";
        
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                infostat.setText("Die Daten werden geladen.");                           
/*               
                   //get reference to the button's stage   
                   Stage stage =(Stage)saveButton.getScene().getWindow();
                   //load up OTHER FXML document
                   
                	AnchorPane page;
					try {
						page = (AnchorPane) FXMLLoader.load(getClass().getResource("Game.fxml"));
						//create a new scene with root and set the stage
		                 Scene scene = new Scene(page);
		                 stage.setScene(scene);
		                 stage.show();					
					} catch (IOException e) {
						e.printStackTrace();
					} */                 
            }            
        });       
    } 
}
