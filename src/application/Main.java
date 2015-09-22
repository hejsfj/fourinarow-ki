package application;
	

import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;





public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
            AnchorPane page = (AnchorPane) FXMLLoader.load(Main.class.getResource("Start.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("FXML is Simple");
            primaryStage.show();
        } 
		
		catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
			System.out.println("Oh no");
        }
		
	
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
