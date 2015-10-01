package application;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import com.pusher.client.channel.PrivateChannel;

import agentKI.AgentKI;
import fileInterface.Agentfile;
import fileInterface.AgentfileWriter;
import fileInterface.Serverfile;
import fileInterface.ServerfileReader;
import gamefield.Gamefield;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pusherInterface.PusherConnector;
import pusherInterface.PusherEvent;
import pusherInterface.PusherEventHandler;


public class GameController implements Initializable {
	
    @FXML private Button newButton; // Value injected by FXMLLoader    
    @FXML private Button statsButton; // Value injected by FXMLLoader    
	@FXML private Button startButton;
	
	@FXML private Label infostat;
	@FXML private GridPane gamefieldGrid;
	
	GameProperties gameProperties;

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
	            stage=(Stage)newButton.getScene().getWindow();
               
            	AnchorPane page;
				try {
					page = (AnchorPane) FXMLLoader.load(getClass().getResource("Start.fxml"));
	                stage.setScene(new Scene(page));
	                stage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}    
            }    
        });
		
		statsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Zum neuen Spiel");
				Stage stage;
				stage = (Stage) statsButton.getScene().getWindow();
				AnchorPane page;
				try {
					page = (AnchorPane) FXMLLoader.load(getClass().getResource("Stats.fxml"));
					stage.setScene(new Scene(page));
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
				infostat.setText("Das Spiel beginnt");				
				
				gameProperties = new GameProperties();
				readProperties();
				String usedInterface = gameProperties.getProperty(GameProperties.INTERFACE);
				
				if (usedInterface.equals("File")){
					startFileInterfaceGame();
				}
				if (usedInterface.equals("Push")){
					startPusherInterfaceGame();
				}
			}

			private void startPusherInterfaceGame() {
				System.out.println("PusherInterface not implemented yet");
				Gamefield gamefield = new Gamefield();
				AgentKI agent = new AgentKI();
				char player = gameProperties.getProperty(GameProperties.SPIELER).charAt(0);
				String appKey = gameProperties.getProperty(GameProperties.APP_KEY);
				String appSecret = gameProperties.getProperty(GameProperties.APP_SECRET);
				
				PusherConnector pusher = new PusherConnector("", appKey, appSecret);
				PusherEventHandler eventHandler = new PusherEventHandler();
				PrivateChannel channel = pusher.subscribeToPrivateChannel("private-channel", eventHandler, "MoveToAgent");
				PusherEvent pusherEvent;

				int eventCounter = 0;
				while (true) {
					if (eventHandler.hasEvent) {
						
						pusherEvent = eventHandler.getPusherEvent();
						eventHandler.hasEvent = false;
						
						eventCounter++;
						
						System.out.println("EventCounter: " + eventCounter);
						
						
						System.out.println("Gegnerzug: " + pusherEvent.getGegnerzug());
						
						if (pusherEvent.getGegnerzug() != -1 && pusherEvent.getGegnerzug() != -2){
							gamefield.insertCoin(gamefieldGrid, pusherEvent.getGegnerzug(), (player == 'o') ? 'x' : 'o');
						}
						if (!pusherEvent.isGameOver()){
							
							int move = agent.calculateMove(gamefield);
							System.out.println("calculated move: " + move);
							gamefield.insertCoin(gamefieldGrid, move, player);
							pusher.triggerClientMove(channel, move);
							
						} else {
							System.out.println("Spiel vorbei. Der Gewinner ist: " + pusherEvent.getSieger());
							break;
						}
					}
				}				
			}

			private void startFileInterfaceGame() {				

				AgentKI agent = new AgentKI();

				Gamefield gamefield = new Gamefield();
				String sharedFolderPath = gameProperties.getProperty(GameProperties.DATEIPFAD);
				
				char player = gameProperties.getProperty(GameProperties.SPIELER).charAt(0);
				AgentfileWriter agentfileWriter = new AgentfileWriter(sharedFolderPath, player);
				ServerfileReader serverfileReader = new ServerfileReader(sharedFolderPath, player);	

				while (true) {
					try {
						Serverfile serverfile = serverfileReader.readServerfile();
						
						if (serverfile.getGegnerzug() != -1){
							gamefield.insertCoin(gamefieldGrid, serverfile.getGegnerzug(), (player == 'o') ? 'x' : 'o');
						}
						if (!serverfile.isGameOver()){
							int move = agent.calculateMove(gamefield);
							gamefield.insertCoin(gamefieldGrid, move, player);
							agentfileWriter.writeAgentfile(new Agentfile(move));
						} else {
							System.out.println("Spiel vorbei. Der Gewinner ist: " + serverfile.getSieger());
							break;
						}
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	private void readProperties() {
		InputStream input = null;
    	
    	try {
    		input = new FileInputStream(GameProperties.DATEINAME);
    		gameProperties.load(input);
    	} catch (IOException ex) {
    		ex.printStackTrace();
        } finally{
        	if(input!=null){
        		try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	}
        }
    }
	
	
}

