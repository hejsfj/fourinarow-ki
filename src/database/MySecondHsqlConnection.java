package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 
public class MySecondHsqlConnection
{
    public MySecondHsqlConnection()
    {
          
    try
    { 
        // Treiberklasse laden
      Class.forName( "org.hsqldb.jdbcDriver" ); 
    } 
    catch ( ClassNotFoundException e ) 
    { 
      System.err.println( "Treiberklasse nicht gefunden!" ); 
      return; 
    } 
  
    Connection con = null; 
  
    try
    { 
      con = DriverManager.getConnection(  
              "jdbc:hsqldb:file:/fourinarow-ki/database; shutdown=true", "root", "" ); 
      Statement stmt = con.createStatement(); 
  
      String sql;
      // Tabellen erstellen
      sql = "CREATE TABLE Spieler (ID INTEGER IDENTITY,Name VARCHAR(255), Siege INTEGER, Niederlagen INTEGER)";
      stmt.executeQuery(sql);
      sql = "CREATE TABLE Spiel (ID INTEGER IDENTITY,Punkte INTEGER, PunkteGegner INTEGER , Gegner INTEGER REFERENCES Spieler(ID),Sieger INTEGER REFERENCES Spieler(ID), Datum date)";
      stmt.executeQuery(sql);
      sql = "CREATE TABLE Satz (ID INTEGER IDENTITY,SatzNr INTEGER, SpielID INTEGER REFERENCES Spiel(ID), Sieger INTEGER REFERENCES Spieler(ID),StartSpieler INTEGER REFERENCES Spieler(ID))";
      stmt.executeQuery(sql);
      sql = "CREATE TABLE Spiezug (ID INTEGER IDENTITY,ZugNr INTEGER, SatzID INTEGER REFERENCES Satz(ID), SpielerID INTEGER REFERENCES Spieler(ID),Spalte INTEGER)";
      stmt.executeQuery(sql);
      
//      public void insertSpieler(String name, )
//      // Alle Kunden ausgeben
//      String sql = "SELECT * FROM CUSTOMER";
//      ResultSet rs = stmt.executeQuery(sql); 
//  
//      while ( rs.next() ) 
//      {
//        String id = rs.getString(1);
//        String firstName = rs.getString(2);
//        String lastName = rs.getString(3);
//        System.out.println(id + ", " + firstName + " " + lastName);
//      }
//       
//      // Resultset schlieﬂen
//      rs.close(); 
  
      // Statement schlieﬂen
      stmt.close(); 
    } 
    catch ( SQLException e ) 
    { 
      e.printStackTrace(); 
      e.getMessage();
    } 
    finally
    { 
      if ( con != null ) 
      {
        try { 
            con.close(); 
            } catch ( SQLException e ) { 
                e.printStackTrace(); 
            }
      }
    } 
    }
     
    public static void main(String[] args)
    {
        new MySecondHsqlConnection();
    }
}