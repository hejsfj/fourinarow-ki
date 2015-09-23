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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameController implements Initializable {
	
    @FXML
    private Button startButton;

    @FXML
    private Font x31;

    @FXML
    private Color x1;

    @FXML
    private Button newButton;

    @FXML
    private Font x3;

    @FXML
    private Font x4;

    @FXML
    private Button statsButton;

    @FXML
    private Color x5;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		assert newButton != null : "fx:id=\"newButton\" was not injected: check your FXML file";
		assert statsButton != null : "fx:id=\"statsButton\" was not injected: check your FXML file";
		
		
		newButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Zum neuen Spiel");

				Stage stage;

				// get reference to the button's stage
				stage = (Stage) newButton.getScene().getWindow();
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
		
	}

    
}
}
