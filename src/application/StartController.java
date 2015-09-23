package application;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Labeled;
import javafx.scene.control.Control;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class StartController implements Initializable {

    @FXML //  fx:id="myButton"
    private Button startButton; // Value injected by FXMLLoader
    
    @FXML
    private Button statsButton;
    
    @FXML
    private RadioButton pushButton;
    
    @FXML
    private RadioButton fileButton;
    

    

public class RadioButton extends ToggleButton implements Toggle{
    final ToggleGroup group = new ToggleGroup();   
    pushButton.setToggleGroup(group);
    pushButton.setSelected(true);    
    fileButton.setToggleGroup(group);
}

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert startButton != null : "fx:id=\"myButton\" was not injected: check your FXML file";
        assert statsButton != null : "fx:id=\"myButton\" was not injected: check your FXML file";
        
        startButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("That was easy, wasn't it?");
                
                   
                Stage stage; 
       
                   //get reference to the button's stage         
                   stage=(Stage)startButton.getScene().getWindow();
                   //load up OTHER FXML document
                   
                	AnchorPane page;
					try {
						page = (AnchorPane) FXMLLoader.load(getClass().getResource("Game.fxml"));
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
                System.out.println("That was easy, wasn't it?");
                     
                Stage stage; 
                           
                   //get reference to the button's stage         
                   stage=(Stage)statsButton.getScene().getWindow();
                   //load up OTHER FXML document
                   
                	AnchorPane page;
					try {
						page = (AnchorPane) FXMLLoader.load(getClass().getResource("Stats.fxml"));
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
        
        pushButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("3 Einstellungen");
                     
                Stage stage; 
                           
                   //get reference to the button's stage         
                   stage=(Stage)pushButton.getScene().getWindow();
                   //load up OTHER FXML document
                   
                	AnchorPane page;
					try {
						page = (AnchorPane) FXMLLoader.load(getClass().getResource("Stats.fxml"));
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