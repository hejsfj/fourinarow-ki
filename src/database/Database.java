package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Tim Erster Versuch für lauffähige Datenbank operationen Als zweiter
 *         Schritt soll die Klasse stäker Objektorientiert angepasst werden
 *
 */
public class Database {

	Connection con;
	Statement stmt;
	ResultSet rs;
	String dbPath = "hsql\\dbTest3";

	// Konstruktur
	public Database() {
		try {
			// Treiberklasse laden
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException e) {
			System.err.println("Treiberklasse nicht gefunden!");
			return;
		}

		try {
			this.con = DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + "; shutdown=true", "root", "");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// erstmaliges erstellen der Tabellen im Filesystem des Anwenders
	public void initTabels() {
		String sql;
	
		try {
			
			stmt = this.con.createStatement();
			System.out.println(con + " " + con.getWarnings());

			sql = "CREATE TABLE IF NOT EXISTS Spieler (ID INTEGER IDENTITY,Name VARCHAR(255), Siege INTEGER, Niederlagen INTEGER)";
			stmt.executeQuery(sql);
			sql = "CREATE TABLE IF NOT EXISTS Spiel (ID INTEGER IDENTITY,Punkte INTEGER, PunkteGegner INTEGER , Gegner INTEGER REFERENCES Spieler(ID),Sieger INTEGER REFERENCES Spieler(ID), Datum date)";
			stmt.executeQuery(sql);
			sql = "CREATE TABLE IF NOT EXISTS Satz (ID INTEGER IDENTITY,SatzNr INTEGER, SpielID INTEGER REFERENCES Spiel(ID), Sieger INTEGER REFERENCES Spieler(ID),StartSpieler INTEGER REFERENCES Spieler(ID))";
			stmt.executeQuery(sql);
			sql = "CREATE TABLE IF NOT EXISTS Spiezug (ID INTEGER IDENTITY,ZugNr INTEGER, SatzID INTEGER REFERENCES Satz(ID), SpielerID INTEGER REFERENCES Spieler(ID),Spalte INTEGER)";
			stmt.executeQuery(sql);
//			stmt.close();
			System.out.println("Tabellen angelegt");
			
			sql = "INSERT INTO SPIELER VALUES (null, 'Sebastian', 2, 1);";
			stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeDB(){
		try { if (rs != null) rs.close(); } catch (Exception e) {};
	    try { if (stmt != null) stmt.close(); } catch (Exception e) {};
	    try { if (con != null) con.close(); } catch (Exception e) {};
	}
	

	public static void main(String[] args) {
		Database db = new Database();
		db.initTabels();
		
		db.closeDB();
	}
		
		
}
