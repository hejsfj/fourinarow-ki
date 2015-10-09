package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
	@Override	
	public void start(Stage primaryStage) {
		try {
            AnchorPane page = (AnchorPane) FXMLLoader.load(Main.class.getResource("Settings.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("4 Gewinnt immer!");
            primaryStage.setResizable(false);
            primaryStage.show();          
        } catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
			System.out.println("Oh no");
        }	
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}