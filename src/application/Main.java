package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
	@Override	
	public void start(Stage primaryStage) {
		try {
			showSettingsScreen(primaryStage);
        } catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("Oh no");
        }	
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private Stage showSettingsScreen(Stage primaryStage) throws IOException {
		
	  	FXMLLoader loader = new FXMLLoader(getClass().getResource("Settings.fxml"));
	
	  	primaryStage.setScene(new Scene((Pane) loader.load()));
        primaryStage.setTitle("4 Gewinnt immer!");
        primaryStage.setResizable(false);
        
        primaryStage.show();
	  	return primaryStage;
	}
}