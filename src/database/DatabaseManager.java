package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import database.DatabaseStructure;

public class DatabaseManager {
	
	Connection connection = null;
	DatabaseStructure DatabaseStructure;

	
	public DatabaseManager(){
		DatabaseStructure = new DatabaseStructure();
		databaseConnect();
		
		try {
			initializeDatabase();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
	      connection = DriverManager.getConnection(DatabaseStructure.path, DatabaseStructure.user, DatabaseStructure.password); 
	      
	    } catch ( SQLException e ) { 
	      e.printStackTrace(); 
	    } 

	}
	
	public void initializeDatabase() throws SQLException{
		
		execute(DatabaseStructure.createTableSpiel);
		execute(DatabaseStructure.createTableSatz);
		execute(DatabaseStructure.createTableZug);
		
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


	public void addspiel(int spiel_id, String spielerO, String spielerY, String sieger, String datum) throws SQLException{
			this.execute("INSERT INTO spiel(spielero,spielery,sieger,datum) VALUES('"+spielerY+"','"+spielerO+"','"+sieger+"','"+datum+"')");	 	
	}

	public void addsatz(int spiel_id, int satz_nr, int punktespielero,int punktespielerx, String startspieler) throws SQLException{
		this.execute("INSERT INTO satz(spiel_id, satz_nr, punktespielero, punktespielerx, startspieler)VALUES('"+spiel_id+"','"+satz_nr+"','"+punktespielero+"','"+punktespielerx+"','"+startspieler+"')");
	}
	
	public void addzug(int spiel_id, int satz_nr,int zug_nr, int spalte, String spieler) throws SQLException{
		this.execute("INSERT INTO zug(spiel_id,satz_nr,zug_nr,spalte,spieler) VALUES('"+spiel_id+"','"+satz_nr+"','"+zug_nr+"','"+spalte+"','"+spieler+"')");
	}
	
	public ResultSet getspiele(){
		return this.query("SELECT * FROM spiele");		
	}

	public ResultSet getsaetze(String SpielID){
		return this.query("SELECT * FROM satz WHERE spiel_id = "+SpielID);		
	}
	
	public ResultSet getzuege(String SpielID, String SatzNR){
		return this.query("SELECT * FROM zug WHERE spiel_id = "+SpielID+"AND satz_nr = "+SatzNR);
	}
	
	public void updateSpiel(String sieger, String id){
		this.query("UPDATE spiel SET sieger = "+sieger+", WHERE ID = "+id);
	}
}
