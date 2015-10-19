package application;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class LoadController implements Initializable {
    @FXML private Button loadButton; // Value injected by FXMLLoader    
    @FXML private Button saveButton; // Value injected by FXMLLoader    
    @FXML private Button newButton; // Value injected by FXMLLoader    
    @FXML private Label infostat;
    @FXML private TableView myTable;
  
	@FXML void loadGame(ActionEvent event) {
		try {
			showLoadScreen();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML void newGame(ActionEvent event) {		
		try {
			showSettingsScreen();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert loadButton != null : "fx:id=\"refreshButton\" was not injected: check your FXML file";
        assert saveButton != null : "fx:id=\"saveButton\" was not injected: check your FXML file";
        assert newButton != null : "fx:id=\"saveButton\" was not injected: check your FXML file";
        assert myTable != null : "fx:id=\"myTable\" was not injected: check your FXML file";
        
        fillTableViewWithDbData();
        
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                infostat.setText("Die Daten werden geladen.");                           
               
                   //get reference to the button's stage   
                   Stage stage =(Stage)saveButton.getScene().getWindow();
                   //load up OTHER FXML document
                   
                	AnchorPane page;
					try {
						page = (AnchorPane) FXMLLoader.load(getClass().getResource("Settings.fxml"));
						//create a new scene with root and set the stage
		                 Scene scene = new Scene(page);
		                 stage.setScene(scene);
		                 stage.show();					
					} catch (IOException e) {
						e.printStackTrace();
					}                 
            }            
        });       
    } 
    
    private Stage showLoadScreen() throws IOException {
    	Stage stage = (Stage) saveButton.getScene().getWindow();
	  	FXMLLoader loader = new FXMLLoader(getClass().getResource("Load.fxml"));
	
	  	stage.setScene(new Scene((Pane) loader.load()));
	
	  	stage.show();
	  	return stage;
    }
    
    private Stage showSettingsScreen() throws IOException {
    	Stage stage = (Stage) saveButton.getScene().getWindow();
	  	FXMLLoader loader = new FXMLLoader(getClass().getResource("Settings.fxml"));
	
	  	stage.setScene(new Scene((Pane) loader.load()));
	
	  	stage.show();
	  	return stage;
    }
    
    private void fillTableViewWithDbData(){
    	DatabaseManager databaseManager = DatabaseManager.getInstance();
		ObservableList<ObservableList> data;
        data = FXCollections.observableArrayList();

        try {
        	ResultSet resultSet = databaseManager.getAllSets();

            for(int i = 0 ; i < resultSet.getMetaData().getColumnCount(); i++){
                //We are using non property style for making dynamic table
                final int j = i;               
                TableColumn column = new TableColumn(resultSet.getMetaData().getColumnName(i+1));
                column.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                   
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                             
                        return new SimpleStringProperty(param.getValue().get(j).toString());                       
                    }                   
                });
                myTable.getColumns().addAll(column);
                System.out.println("Column ["+i+"] ");
            }
            /********************************
             * Data added to ObservableList *
            ********************************/
            while(resultSet.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=resultSet.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(resultSet.getString(i));
                }
                System.out.println("Row [1] added "+row );

                data.add(row);
            }
            //FINALLY ADDED TO TableView
            myTable.setItems(data);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
