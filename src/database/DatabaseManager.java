package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.DatabaseStructure;


/**
 * Diese Klasse dient zur Verwaltung der Datenbank.
 * Sie baut eine Verbindung zur Datenbank auf und enthält Methoden zur Datenbankmanipulation.
 */
public class DatabaseManager {
	private Connection connection = null;	
	private static DatabaseManager dbManagerInstance = null;

	/**
	 * Liefert eine Instanz des DatabaseManagers zurück.
	 *
	 * @return Instanz des DatabaseMangers.
	 */
	public static DatabaseManager getInstance() {
		if(dbManagerInstance == null) {
			dbManagerInstance = new DatabaseManager();
		}
		return dbManagerInstance;
	}
	
	/**
	 * Instanziiert einen neuen DatabaseManager.
	 */
	protected DatabaseManager(){
		connectToDatabase();
		System.out.println("database connected");
		try {
			System.out.println("trying to initialize database");
			initializeDatabase();
			System.out.println("database initialized");			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Stellt eine Verbindung zur Datenbank her.
	 */
	public void connectToDatabase(){
		try { 
	      Class.forName( "org.hsqldb.jdbcDriver" ); 
	    } catch ( ClassNotFoundException e ) { 
	      System.err.println( "Treiberklasse nicht gefunden" ); 
	      return; 
	    } 
	  	  
	    try{ 
	      connection = DriverManager.getConnection(DatabaseStructure.PATH, DatabaseStructure.USER, DatabaseStructure.PASSWORD);
	    } catch ( SQLException e ) { 
	      e.printStackTrace(); 
	    } 
	}
	
	/**
	 * Initialisiert die Datenbank.
	 *
	 * @throws SQLException {@link java.sql.SQLException}
	 */
	public void initializeDatabase() throws SQLException{
		execute(DatabaseStructure.CREATE_TABLE_SPIELE);
		execute(DatabaseStructure.CREATE_TABLE_SAETZE);
		execute(DatabaseStructure.CREATE_TABLE_ZUEGE);
	}

    /**
     * Fügt ein Spiel zur Datenbank hinzu.
     * 
     * @param playerO Spieler O
	 * @param playerX Spieler X
	 * @param winner Der Gewinner
	 * @param date Das Datum.
	 * 
	 * @return Liefert die erzeugte Spiel-Id zurück.
	 * @throws SQLException {@link java.sql.SQLException}
     */
    public int addGame(String playerO, String playerX, String winner, String date) throws SQLException {
		  Statement statement = connection.createStatement(); 
		  
		  String sqlQuery =   "INSERT INTO spiele ("
							+ 	"spielero, "
							+ 	"spielerx, "
							+ 	"sieger, "
							+ 	"datum)"
							+ "VALUES('"
							+ 	playerO 	+ "','"
							+ 	playerX 	+ "','"
							+ 	winner 	+ "','"
							+ 	date 
							+	"');"
							+ "CALL IDENTITY();"; //Call Identity() liefert die zuletzt generierte Identity (gameId) zurück
			
		  ResultSet resultSet = statement.executeQuery(sqlQuery);  
		  resultSet.next();
		  int id = resultSet.getInt(1);
		  
		  System.out.println("Spiel created with ID " + id);
	      resultSet.close(); 
	      statement.close(); 
	      connection.commit();
	      return id;
	}
    
	/**
	 * Fügt einen Satz der Datenbank hinzu.
     * 
     * @param gameId Spiel-Id
	 * @param setNr Satz-Nr
	 * @param pointsPlayerO Punkte von Spieler O
	 * @param pointsPlayerX Punkte von Spieler X
	 * @param winner Gewinner
	 * 
	 * @throws SQLException {@link java.sql.SQLException}
	 */
	public void addSet(int gameId, int setNr, int pointsPlayerO,int pointsPlayerX, String winner) throws SQLException{
		execute(  "INSERT INTO saetze ("
				+	 "spiel_id, "
				+	 "satz_nr, "
				+	 "punktespielero, "
				+	 "punktespielerx, "
				+	 "sieger, "
				+	 "startspieler)"
				+ "VALUES('"
				+ 	String.valueOf(gameId) 			+ "','"
				+ 	String.valueOf(setNr) 			+ "','"
				+ 	String.valueOf(pointsPlayerO) 	+ "','"
				+ 	String.valueOf(pointsPlayerX) 	+ "','"
				+ 	winner						 	+ "','"
				+ 	""
				+ "')");
	}
	
	/**
	 * Aktualisiert den Startspieler eines Satzes.
	 * 
	 * @param gameId Spiel-Id
	 * @param setNr Satz-Nr
	 * @param startPlayer Startspieler
	 * 
	 * @throws SQLException {@link java.sql.SQLException}
	 */
	public void updateStartPlayerOfSet(int gameId, int setNr, String startPlayer) throws SQLException{
		final String sqlQuery =   "UPDATE saetze "
								+ 	"SET startspieler = " + "'" + startPlayer + "'"
								+ "WHERE "
								+ 	"spiel_id = " + String.valueOf(gameId)
								+ 	" AND "
								+ 	"satz_nr = " + String.valueOf(setNr);
		execute(sqlQuery);
	}

	/**
	 * Fügt einen Zug zur Datenbank hinzu.
	 * 
	 * @param gameId Spiel-Id
	 * @param setNr Satz-Nr
	 * @param moveNr Zug-Nr
	 * @param column Zug
	 * @param player Spieler
	 * 
	 * @throws SQLException {@link java.sql.SQLException}
	 */
	public void addMove(int gameId, int setNr, int moveNr, int column, String player) throws SQLException{
		execute(  "INSERT INTO zuege("
				+	 "spiel_id, "
				+	 "satz_nr, "
				+ 	 "zug_nr, "
				+	 "spalte, "
				+	 "spieler) "
				+ "VALUES('"
				+ 	String.valueOf(gameId) 	+"','"
				+ 	String.valueOf(setNr) 	+"','"
				+ 	String.valueOf(moveNr) 	+"','"
				+ 	String.valueOf(column) 	+"','"
				+ 	player
				+ "')");
	}
	
	/**
	 * Ruft alle Spiele aus der Datenbank ab.
	 * 
	 * @return Liefert alle Spiele in einem ResultSet zurück.
	 * @throws SQLException {@link java.sql.SQLException}
	 */
	public ResultSet getAllGames() throws SQLException {
		final String sqlQuery =   "SELECT * "
								+ "FROM spiele";
		return query(sqlQuery);		
	}
	
	/**
	 * Ruft alle Gewinner der Sätze eines Spiels aus der Datenbank ab.
	 * @param gameId Spiel-Id
	 * @return Liefert alle Gewinner zu einer Spiel-Id in einem ResultSet zurück.
	 * @throws SQLException {@link java.sql.SQLException}
	 */
	public ResultSet getAllSetWinnersForGameId(String gameId) throws SQLException {
		final String sqlQuery =   "SELECT sieger "
				+ "FROM saetze "
				+ "WHERE "
				+	 "spiel_id = " + gameId;
		return query(sqlQuery);				
	}

	/**
	 * Ruft alle Sätzes eines ausgewählten Spiels auf der Datenbank ab.
	 * @param gameId Spiel-Id
	 * @return Liefert alle Sätze zu einer Spiel-Id in einem ResultSet zurück.
	 * @throws SQLException {@link java.sql.SQLException}
	 */
	public ResultSet getAllSetsForGameId(String gameId) throws SQLException {
		final String sqlQuery =   "SELECT * "
								+ "FROM saetze "
								+ "WHERE "
								+	 "spiel_id = " + gameId;
		return query(sqlQuery);		
	}
	
	/**
	 * Ruft alle gespeicherten Sätze aus der Datenbank ab.
	 * 
	 * @return Liefert alle Sätze in einem ResultSet zurück.
	 * @throws SQLException {@link java.sql.SQLException}
	 */
	public ResultSet getAllSets() throws SQLException {
		 final String sqlQuery =  "SELECT "
			     				+	"spiel_id, "
			     				+ 	"satz_nr, "
			     				+ 	"sieger, "
			     				+ 	"startspieler, "
			     				+ 	"punktespielero, "
			     				+ 	"punktespielerx "
			     				+ "FROM saetze";
		
		return query(sqlQuery);
	}
	
	/**
	 * Ruft alle Züge eines ausgewählten Satzes aus der Datenbank ab.
	 * @param gameId Spiel-Id
	 * @param setNr Satz-Nr
	 * @return Liefert alle gespielten Züge zu einer Spiel-Id und zu einer Satz-Nr in einem ResultSet zurück.
	 * @throws SQLException {@link java.sql.SQLException}
	 */
	public ResultSet getMovesForSet(int gameId, int setNr) throws SQLException {
		final String sqlQuery =   "SELECT *"
								+ "FROM zug"
								+ "WHERE "
								+ 	"spiel_id = " + String.valueOf(gameId)
								+ 		"AND "
								+ 	"satz_nr = " + String.valueOf(setNr);
		return query(sqlQuery);
	}
	
	/**
	 * Aktualisiert den Gewinner eines Satzes.
	 * 
	 * @param gameId Spiel-Id
	 * @param setNr Satz-Nr
	 * @param winner Gewinner
	 * 
	 * @throws SQLException {@link java.sql.SQLException}
	 */
	public void updateWinnerOfSet(int gameId, int setNr, String winner) throws SQLException{
		if (winner.equals("Spieler O")){
			updateWinnerOfSet(gameId, setNr, 2, 0, winner);
		}
		else if (winner.equals("Spieler X")){
			updateWinnerOfSet(gameId, setNr, 0, 2, winner);			
		}
	}
	
	private void updateWinnerOfSet(int gameId, int setNr, int pointsPlayerO, int pointsPlayerX, String winner) throws SQLException{
		final String sqlQuery =   "UPDATE saetze "
								+ 	"SET sieger = " + "'" + winner + "', "
								+ 	" punktespielero = " + pointsPlayerO + ", "
								+ 	" punktespielerx = " + pointsPlayerX + " "
								+ "WHERE "
								+ 	"spiel_id = " + String.valueOf(gameId)
								+ 	" AND "
								+ 	"satz_nr = " + String.valueOf(setNr);
		execute(sqlQuery);
	}
	
	private synchronized void execute(String expression) throws SQLException {
         Statement statement = null;
         statement = connection.createStatement();

         statement.executeUpdate(expression);
         
         statement.close();
    }  
	
    private synchronized ResultSet query(String sqlQuery) throws SQLException {
        Statement statement = null;
        ResultSet resultSet = null;
        
        statement = connection.createStatement();         
        resultSet = statement.executeQuery(sqlQuery);    
        statement.close();              
        
        return resultSet;
    }
}
