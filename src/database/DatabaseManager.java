package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
			//int id = addSpiel("spielerO", "spielerX", null, "25.07.1990");
			//int id1 = addSpiel("spielerO", "spielerX", null, "25.07.1991");
			//addSatz(1, 1, 2, 0, "spielerO", "spielerO");
			//addSatz(1, 2, 2, 0, "spielerO", "spielerX");
			
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
		execute(DatabaseStructure.CREATE_TABLE_SPIELE);
		execute(DatabaseStructure.CREATE_TABLE_SAETZE);
		execute(DatabaseStructure.CREATE_TABLE_ZUEGE);
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
	
    public synchronized ResultSet query(String expression) {

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


//	public int addSpiel(String spielerO, String spielerX, String sieger, String datum) throws SQLException {
//			this.execute("INSERT INTO spiel ("
//							+ "spielero, "
//							+ "spielerx, "
//							+ "sieger, "
//							+ "datum)"
//						+ "VALUES('"
//							+ spielerO 	+ "','"
//							+ spielerX 	+ "','"
//							+ sieger 	+ "','"
//							+ datum 
//						+"')");	 
//			
//			int id = returnID();
//			return id;
//			
//	}
    
    public int addSpiel(String spielerO, String spielerX, String sieger, String datum){
		try {
			  Statement statement = connection.createStatement(); 
			  String sql = "INSERT INTO spiele ("
						+ "spielero, "
						+ "spielerx, "
						+ "sieger, "
						+ "datum)"
					+ "VALUES('"
						+ spielerO 	+ "','"
						+ spielerX 	+ "','"
						+ sieger 	+ "','"
						+ datum 
					+"');"
					+ "CALL IDENTITY();";
					
			  ResultSet rs = statement.executeQuery(sql);  
			  rs.next();
			  int id = rs.getInt(1);
			  System.out.println("ID: "+id);
		      rs.close(); 
		      statement.close(); 
		      connection.commit();
		      return id;
			}catch(SQLException e){
				e.printStackTrace();
				return -1;
			}
	}
	
	  

	public void addSatz(int spiel_id, int satz_nr, int punktespielero,int punktespielerx, String sieger, String startspieler) throws SQLException{
		this.execute("INSERT INTO saetze ("
						+ "spiel_id, "
						+ "satz_nr, "
						+ "punktespielero, "
						+ "punktespielerx, "
						+ "sieger, "
						+ "startspieler)"
					+ "VALUES('"
						+ String.valueOf(spiel_id) 			+ "','"
						+ String.valueOf(satz_nr) 			+ "','"
						+ String.valueOf(punktespielero) 	+ "','"
						+ String.valueOf(punktespielerx) 	+ "','"
						+ sieger						 	+ "','"
						+ startspieler 
					+ "')");
	}
	
	public void addZug(int spiel_id, int satz_nr,int zug_nr, int spalte, String spieler) throws SQLException{
		this.execute("INSERT INTO zuege("
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
		System.out.println("zug in db geschrieben");
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
	
	public void updateSatz(int spielId,  int satzNr, String sieger) throws SQLException{
		this.execute("UPDATE saetze"
					+ " SET sieger = " + "'" + sieger + "'"
					+ " WHERE spiel_id = " + String.valueOf(spielId)
							+ " AND "
						+ "satz_nr = " + String.valueOf(satzNr));
	}
}
