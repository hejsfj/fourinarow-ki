package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import database.DatabaseManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoadController implements Initializable {
    @FXML private Button loadButton; // Value injected by FXMLLoader    
    @FXML private Button saveButton; // Value injected by FXMLLoader    
    @FXML private Button newButton; // Value injected by FXMLLoader    
    @FXML private Label infostat;
    @FXML private TableView myTable;
  
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
    
    
    
    public void buildData(){
    			
    			DatabaseManager databaseManager = new DatabaseManager();
    			ObservableList<ObservableList> data;
    	          data = FXCollections.observableArrayList();
    	
    	          try{
    	                	   	
	    	           String SQL = "SELECT "
	    	            			+ "spiel_id, "
	    	            			+ "satz_nr, "
	    	            			+ "sieger, "
	    	            			+ "startspieler"
    	            			+ " from Saetze";
    	
    	            //ResultSet
	    	        ResultSet rs = databaseManager.query(SQL);
    	
    	            /**********************************
    	
    	             * TABLE COLUMN ADDED DYNAMICALLY *
    	
    	             **********************************/
    	
    	            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
    	
    	                //We are using non property style for making dynamic table
    	
    	                final int j = i;               
    	                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
    	                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                   
    	                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                             
    	                        return new SimpleStringProperty(param.getValue().get(j).toString());                       
    	                    }                   
    	
    	                });
    	
    	             	
    	                myTable.getColumns().addAll(col);
    	                System.out.println("Column ["+i+"] ");
    	
    	            }
    	
    	 
    	
    	            /********************************
    	
    	             * Data added to ObservableList *
    	
    	            ********************************/
    	
    	            while(rs.next()){
    	
    	                //Iterate Row
    	
    	                ObservableList<String> row = FXCollections.observableArrayList();
    	
    	                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
    	
    	                    //Iterate Column
    	
    	                    row.add(rs.getString(i));
    	
    	                }
    	
    	                System.out.println("Row [1] added "+row );
    	
    	                data.add(row);
    	
    	
    	
    	            }
    	
    	
    	
    	            //FINALLY ADDED TO TableView
    	
    	            myTable.setItems(data);
    	
    	        }catch(Exception e){
    	
    	            e.printStackTrace();
    	
    	              System.out.println("Error on Building Data");            
    	
    	          }
    	
    	      }

    
    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert loadButton != null : "fx:id=\"refreshButton\" was not injected: check your FXML file";
        assert saveButton != null : "fx:id=\"saveButton\" was not injected: check your FXML file";
        assert newButton != null : "fx:id=\"saveButton\" was not injected: check your FXML file";
        assert myTable != null : "fx:id=\"myTable\" was not injected: check your FXML file";
        
        buildData();
        
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
