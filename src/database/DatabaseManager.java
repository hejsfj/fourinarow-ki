package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.DatabaseStructure;

public class DatabaseManager {
	
	Connection connection = null;

	
	public DatabaseManager(){
		databaseConnect();
		System.out.println("database connected");
		try {
			System.out.println("trying to initialize database");
			initializeDatabase();
			System.out.println("database initialized");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		//insert
		//load
	}
	
	public void databaseConnect(){
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
	
	public void initializeDatabase() throws SQLException{
		execute(DatabaseStructure.CREATE_TABLE_SPIEL);
		execute(DatabaseStructure.CREATE_TABLE_SATZ);
		execute(DatabaseStructure.CREATE_TABLE_ZUG);
	}
	
	public synchronized void execute(String expression) throws SQLException {

         Statement statement = null;
         statement = connection.createStatement();

         int i = statement.executeUpdate(expression);

         if (i == -1) {
             System.out.println("database experssion error : " + expression);
         }

         statement.close();
    }  
	
    protected synchronized ResultSet query(String expression) {

        Statement statement = null;
        ResultSet resultset = null;
        try {
            statement = connection.createStatement();         
            resultset = statement.executeQuery(expression);    
            statement.close();  
            
        } catch (Exception e) {
			System.out.println("Something went wrong executing query");
        }
        
        return resultset;
    }


	public void addSpiel(String spielerO, String spielerX, String sieger, String datum) throws SQLException {
			this.execute("INSERT INTO spiel ("
							+ "spielero, "
							+ "spielery, "
							+ "sieger, "
							+ "datum)"
						+ "VALUES('"
							+ spielerO 	+ "','"
							+ spielerX 	+ "','"
							+ sieger 	+ "','"
							+ datum 
						+"')");	 	
	}

	public void addSatz(int spiel_id, int satz_nr, int punktespielero,int punktespielerx, String startspieler) throws SQLException{
		this.execute("INSERT INTO satz ("
						+ "spiel_id, "
						+ "satz_nr, "
						+ "punktespielero, "
						+ "punktespielerx, "
						+ "startspieler)"
					+ "VALUES('"
						+ String.valueOf(spiel_id) 			+ "','"
						+ String.valueOf(satz_nr) 			+ "','"
						+ String.valueOf(punktespielero) 	+ "','"
						+ String.valueOf(punktespielerx) 	+ "','"
						+ startspieler 
					+ "')");
	}
	
	public void addZug(int spiel_id, int satz_nr,int zug_nr, int spalte, String spieler) throws SQLException{
		this.execute("INSERT INTO zug("
						+ "spiel_id, "
						+ "satz_nr, "
						+ "zug_nr, "
						+ "spalte, "
						+ "spieler) "
					+ "VALUES('"
						+ String.valueOf(spiel_id) 	+"','"
						+ String.valueOf(satz_nr) 	+"','"
						+ String.valueOf(zug_nr) 	+"','"
						+ String.valueOf(spalte) 	+"','"
						+ spieler
					+"')");
	}
	
	public ResultSet getSpiele(){
		return this.query("SELECT * FROM spiele");		
	}

	public ResultSet getSaetze(String SpielID){
		return this.query("SELECT * FROM satz WHERE spiel_id = "+SpielID);		
	}
	
	public ResultSet getZuege(String SpielID, String SatzNR){
		return this.query("SELECT * FROM zug WHERE spiel_id = "+SpielID+"AND satz_nr = "+SatzNR);
	}
	
	public void updateSpiel(String sieger, String id){
		this.query("UPDATE spiel SET sieger = "+sieger+", WHERE ID = "+id);
	}
}
