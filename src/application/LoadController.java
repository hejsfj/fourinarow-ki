package application;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import database.DatabaseManager;
import database.DatabaseSetRecord;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;


/**
 * Klasse enthält die Anwendungslogik für die <code>Settings.fxml</code>-Datei.
 * 
 */
public class LoadController implements Initializable {
    @FXML private Button loadButton;
    @FXML private Button saveButton;  
    @FXML private Button newButton;
    @FXML private Label infostat;
    @FXML private TableView<DatabaseSetRecord> myTable;
  
	/**
	 * Load game.
	 */
	@FXML void loadGame(ActionEvent event) {
		// disabled
    }

	/**
	 * Zeigt den Bildschirm zum Starten eines neuen Spiels an.
	 *
	 * @param event
	 *            {@link javafx.event.ActionEvent}
	 */
    @FXML void newGame(ActionEvent event) {		
		try {
	    	System.out.println("Switching to Settings Screen");
			showSettingsScreen();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /* (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert loadButton != null : "fx:id=\"refreshButton\" was not injected: check your FXML file";
        assert saveButton != null : "fx:id=\"saveButton\" was not injected: check your FXML file";
        assert newButton != null : "fx:id=\"saveButton\" was not injected: check your FXML file";
        assert myTable != null : "fx:id=\"myTable\" was not injected: check your FXML file";
        
        setWidthOfTableView();

        fillTableViewWithDbData();
        
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                infostat.setText("Die Daten werden geladen.");    
                try {
                    DatabaseSetRecord selectedSet = myTable.getSelectionModel().getSelectedItem();
                	System.out.println("Switching to Settings Screen");
        			showSettingsScreen(selectedSet);
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
            }            
        });       
    } 
    
    private Stage showSettingsScreen() throws IOException {
    	Stage stage = (Stage) saveButton.getScene().getWindow();
	  	FXMLLoader loader = new FXMLLoader(getClass().getResource("Settings.fxml"));
	
	  	stage.setScene(new Scene((Pane) loader.load()));
	
	  	stage.show();
	  	return stage;
    }
    
    private Stage showSettingsScreen(DatabaseSetRecord selectedSet) throws IOException {
    	Stage stage = (Stage) saveButton.getScene().getWindow();
	  	FXMLLoader loader = new FXMLLoader(getClass().getResource("Settings.fxml"));

	  	stage.setScene(new Scene((Pane) loader.load()));	

	  	SettingsController settingsController = loader.<SettingsController>getController();
	  	settingsController.initController(selectedSet);
	  	
	  	stage.show();
	  	return stage;
    }

    private void setWidthOfTableView(){
    	myTable.setMaxWidth(660.0);
    	myTable.setMinWidth(660.0);
    	myTable.setPrefWidth(660.0);
    }
    
    private void fillTableViewWithDbData(){
    	DatabaseManager databaseManager = DatabaseManager.getInstance();
    	
     	try {
     		ResultSet dbResult = databaseManager.getAllSets();     		
     		addColumnsToTableView(dbResult);
     		insertRowsToTableView(dbResult);
     	} catch (SQLException e) {
     		e.printStackTrace();
     	}
    }
    
    private ObservableList<DatabaseSetRecord> getListOfSetsFromDbResult(ResultSet dbResult){
    	 ObservableList<DatabaseSetRecord> rows = FXCollections.observableArrayList();
    	 try {
			while (dbResult.next()) {
			     String gameId = dbResult.getString( "spiel_id" );                                
			     String setId = dbResult.getString( "satz_nr" );
			     String winner = dbResult.getString( "sieger" );
			     String starter = dbResult.getString( "startspieler" );
			     String pointsPlayerO = dbResult.getString( "punktespielero" );
			     String pointsPlayerX = dbResult.getString( "punktespielerx" );
			   
			     DatabaseSetRecord datensatz = new DatabaseSetRecord(gameId, setId, winner, starter, pointsPlayerO, pointsPlayerX);
 
			     rows.add(datensatz);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}      
         return rows;
    }
    
    @SuppressWarnings("unchecked")
	private void addColumnsToTableView(ResultSet dbResult) throws SQLException {
    	for(int i = 0 ; i < dbResult.getMetaData().getColumnCount(); i++){
            final int j = i;               
            TableColumn<DatabaseSetRecord,String> tableColumn = new TableColumn<DatabaseSetRecord,String>(dbResult.getMetaData().getColumnName(i+1));
            tableColumn.setCellValueFactory(new Callback<CellDataFeatures<DatabaseSetRecord, String>, ObservableValue<String>>() {
       	    public ObservableValue<String> call(CellDataFeatures<DatabaseSetRecord, String> p) {
       	         // p.getValue() gibt den Datensatz für eine bestimmte TableView-Zeile zurück
       	         return new SimpleStringProperty(p.getValue().get(j).toString());  
       	     }
            });
            tableColumn.setPrefWidth(110.0);
            
            myTable.getColumns().addAll(tableColumn);
        }
    }

    private void insertRowsToTableView(ResultSet dbResult){
    	ObservableList<DatabaseSetRecord> setList = getListOfSetsFromDbResult(dbResult);
 		myTable.setItems(setList);
    }
}