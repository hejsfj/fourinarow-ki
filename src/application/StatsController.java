
package application;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;



public class StatsController implements Initializable {
    

    @FXML //  fx:id="myButton"
    private Button loadButton; // Value injected by FXMLLoader
    
    @FXML
    private Button saveButton; // Value injected by FXMLLoader
    
    @FXML
    private Button newButton; // Value injected by FXMLLoader
    
    @FXML
    private Label infostat;
  

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert loadButton != null : "fx:id=\"refreshButton\" was not injected: check your FXML file";
        assert saveButton != null : "fx:id=\"saveButton\" was not injected: check your FXML file";
        assert newButton != null : "fx:id=\"saveButton\" was not injected: check your FXML file";


        
        loadButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Load it!");
                
                infostat.setText("Die Daten wurden aktualisiert!");
                           
//                Stage stage; 
//                
//                
//               
//                   //get reference to the button's stage         
//                   stage=(Stage)refreshButton.getScene().getWindow();
//                   //load up OTHER FXML document
//                   
//                	AnchorPane page;
//					try {
//						page = (AnchorPane) FXMLLoader.load(getClass().getResource("Start.fxml"));
//						//create a new scene with root and set the stage
//		                 Scene scene = new Scene(page);
//		                 stage.setScene(scene);
//		                 stage.show();
//					
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}    
            }
            
        });
        
        saveButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Daten werden geladen");
                
                infostat.setText("Die Daten werden geladen.");
                
                           
//                Stage stage; 
//                
//                
//               
//                   //get reference to the button's stage         
//                   stage=(Stage)saveButton.getScene().getWindow();
//                   //load up OTHER FXML document
//                   
//                	AnchorPane page;
//					try {
//						page = (AnchorPane) FXMLLoader.load(getClass().getResource("Game.fxml"));
//						//create a new scene with root and set the stage
//		                 Scene scene = new Scene(page);
//		                 stage.setScene(scene);
//		                 stage.show();
//					
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} 
                 
            }
            
        });
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
    }   

}
