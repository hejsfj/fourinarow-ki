package database;

class DatabaseStructure {
	
	String path = "jdbc:hsqldb:datenbank/fourinarow";
	String user = "sa";
	String password = "";
		
	String createTableSpiel = "CREATE TABLE IF NOT EXISTS spiel ( id INTEGER IDENTITY, spielero VARCHAR(256),spielerx VARCHAR(256), sieger VARCHAR(256), datum VARCHAR(256)";
	String createTableSatz = "CREATE TABLE IF NOT EXISTS satz ( spiel_id INTEGER, satz_nr INTEGER, punktespielero INTEGER, punktespielerx INTEGER, startspieler VARCHAR(256), PRIMARY KEY(spiel_id, satz_nr))";
	String createTableZug = "CREATE TABLE IF NOT EXISTS zug ( spiel_id INTEGER, satz_nr INTEGER, zug_nr INTEGER, spalte INTEGER, spieler VARCHAR(256), PRIMARY KEY(spiel_id, satz_nr, zug_nr))";
	
}