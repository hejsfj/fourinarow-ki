package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.DatabaseStructure;

/**
 * DatabaseManager Klasse.
 */
public class DatabaseManager {
	private Connection connection = null;
	
	private static DatabaseManager dbManagerInstance = null;

	/**
	 * Gets the single instance of DatabaseManager.
	 *
	 * @return single instance of DatabaseManager
	 */
	public static DatabaseManager getInstance() {
		if(dbManagerInstance == null) {
			dbManagerInstance = new DatabaseManager();
		}
		return dbManagerInstance;
	}
	
	/**
	 * Instantiates a new database manager.
	 */
	protected DatabaseManager(){
		connectToDatabase();
		System.out.println("database connected");
		try {
			System.out.println("trying to initialize database");
			initializeDatabase();
			System.out.println("database initialized");
			
			//int id = addSpiel("spielerO", "spielerX", null, "25.07.1990");
			//int id1 = addSpiel("spielerO", "spielerX", null, "25.07.1991");
			//addSatz(1, 1, 2, 0, "spielerO", "spielerO");
			//addSatz(1, 2, 2, 0, "spielerO", "spielerX");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Datenbankverbindung.
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
	 * Datenbank initialisieren.
	 *
	 * @throws SQLException the SQL exception
	 */
	public void initializeDatabase() throws SQLException{
		execute(DatabaseStructure.CREATE_TABLE_SPIELE);
		execute(DatabaseStructure.CREATE_TABLE_SAETZE);
		execute(DatabaseStructure.CREATE_TABLE_ZUEGE);
	}

    /**
     * Spiel wird hinzugefügt.
     *
     * @param playerO the player o
     * @param playerX the player x
     * @param winner the winner
     * @param date the date
     * @return the int
     * @throws SQLException the SQL exception
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
	 * Set wird hinzugefuegt.
	 *
	 * @param gameId the game id
	 * @param setNr the set nr
	 * @param pointsPlayerO the points player o
	 * @param pointsPlayerX the points player x
	 * @param winner the winner
	 * @param starter the starter
	 * @throws SQLException the SQL exception
	 */
	public void addSet(int gameId, int setNr, int pointsPlayerO,int pointsPlayerX, String winner, String starter) throws SQLException{
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
				+ 	starter 
				+ "')");
	}
	
	/**
	 * Zuege werden hinzugefuegt.
	 *
	 * @param gameId the game id
	 * @param setNr the set nr
	 * @param moveNr the move nr
	 * @param column the column
	 * @param player the player
	 * @throws SQLException the SQL exception
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
	 * Alle Spiele laden.
	 *
	 * @return the all games
	 * @throws SQLException the SQL exception
	 */
	public ResultSet getAllGames() throws SQLException {
		final String sqlQuery =   "SELECT * "
								+ "FROM spiele";
		return query(sqlQuery);		
	}
	
	/**
	 * Gets the all set winners for game id.
	 *
	 * @param gameId the game id
	 * @return the all set winners for game id
	 * @throws SQLException the SQL exception
	 */
	public ResultSet getAllSetWinnersForGameId(String gameId) throws SQLException {
		final String sqlQuery =   "SELECT sieger "
				+ "FROM saetze "
				+ "WHERE "
				+	 "spiel_id = " + gameId;
		return query(sqlQuery);				
	}

	/**
	 * Alle Sets von game id.
	 *
	 * @param gameId the game id
	 * @return the all sets for game id
	 * @throws SQLException the SQL exception
	 */
	public ResultSet getAllSetsForGameId(String gameId) throws SQLException {
		final String sqlQuery =   "SELECT * "
								+ "FROM saetze "
								+ "WHERE "
								+	 "spiel_id = " + gameId;
		return query(sqlQuery);		
	}
	
	/**
	 * Alle Sets laden.
	 *
	 * @return the all sets
	 * @throws SQLException the SQL exception
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
	 * Alle Zuege für das Set laden.
	 *
	 * @param gameId the game id
	 * @param setNr the set nr
	 * @return the moves for set
	 * @throws SQLException the SQL exception
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
	 * Gewinner des Sets aktualisieren.
	 *
	 * @param gameId the game id
	 * @param setNr the set nr
	 * @param winner the winner
	 * @throws SQLException the SQL exception
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
