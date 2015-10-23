package database;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;



/**
 * Diese Klasse...
 */
public class DatabaseSetRecord {
	
	/**
	 * Neue Datenbank instanziieren und Einträge setzen.
	 * 
	 * @param gameId the game id
	 * @param setId the set id
	 * @param winner the winner
	 * @param starter the starter
	 * @param pointsPlayerO the points player o
	 * @param pointsPlayerX the points player x
	 */
	public DatabaseSetRecord(String gameId, String setId, String winner, String starter, String pointsPlayerO, String pointsPlayerX){
		setGameId(gameId);
		setSetId(setId);
		setStarter(starter);
		setWinner(winner);
		setPointsPlayerO(pointsPlayerO);
		setPointsPlayerX(pointsPlayerX);
	}

	/**
	 * Spielinformationen heranziehen.
	 *
	 * @param id the id
	 * @return the string
	 */
	public String get(int id) {
		switch (id) {
		case 0:
			return getGameId();
		case 1:
			return getSetId();
		case 2:
			return getWinner();
		case 3:
			return getStarter();
		case 4:
			return getPointsPlayerO();
		case 5:
			return getPointsPlayerX();
		default:
			return "something wrong";
		}
	}
	
	 private StringProperty gameId;
     
     /**
      * Game id setzen.
      *
      * @param value the new game id
      */
     public void setGameId(String value) { gameIdProperty().set(value); }
     
     /**
      * Game id laden.
      *
      * @return the game id
      */
     public String getGameId() { return gameIdProperty().get(); }
     
     /**
      * Game id Eigenschaften.
      *
      * @return the string property
      */
     public StringProperty gameIdProperty() { 
         if (gameId == null) gameId = new SimpleStringProperty(this, "gameId");
         return gameId; 
     }
 
     private StringProperty setId;
     
     /**
      * ID wird vergeben.
      *
      * @param value the new sets the id
      */
     public void setSetId(String value) { setIdProperty().set(value); }
     
     /**
      * Vergebene ID laden.
      *
      * @return the sets the id
      */
     public String getSetId() { return setIdProperty().get(); }
     
     /**
      * ID Eigenschaften setzen.
      *
      * @return the string property
      */
     public StringProperty setIdProperty() { 
         if (setId == null) setId = new SimpleStringProperty(this, "setId");
         return setId; 
     } 

     private StringProperty winner;
     
     /**
      * Gewinner setzen.
      *
      * @param value the new winner
      */
     public void setWinner(String value) { winnerProperty().set(value); }
     
     /**
      * Gewinner laden.
      *
      * @return the winner
      */
     public String getWinner() { return winnerProperty().get(); }
     
     /**
      * Eigenschaften des Gewinners.
      *
      * @return the string property
      */
     public StringProperty winnerProperty() { 
         if (winner == null) winner = new SimpleStringProperty(this, "winner");
         return winner; 
     } 


     private StringProperty starter;
     
     /**
      * Startspieler wird gesetzt.
      *
      * @param value the new starter
      */
     public void setStarter(String value) { starterProperty().set(value); }
     
     /**
      * Startspieler wird ermittelt.
      *
      * @return the starter
      */
     public String getStarter() { return starterProperty().get(); }
     
     /**
      * Eigenschaften des Startspieler.
      *
      * @return the string property
      */
     public StringProperty starterProperty() { 
         if (starter == null) starter = new SimpleStringProperty(this, "starter");
         return starter; 
     } 
     
     private StringProperty pointsPlayerO;
     
     /**
      * Punkte des Spieler o werden gesetzt.
      *
      * @param value the new points player o
      */
     public void setPointsPlayerO(String value) { pointsPlayerOProperty().set(value); }
     
     /**
      * Punkte des Spieler o werden ermittelt.
      *
      * @return the points player o
      */
     public String getPointsPlayerO() { return pointsPlayerOProperty().get(); }
     
     /**
      * Eigenschaften Punkte von Player o.
      *
      * @return the string property
      */
     public StringProperty pointsPlayerOProperty() { 
         if (pointsPlayerO == null) pointsPlayerO = new SimpleStringProperty(this, "pointsPlayerO");
         return pointsPlayerO; 
     }
     
     private StringProperty pointsPlayerX;
     
     /**
      * Punkte des Spieler x werden gesetzt.
      *
      * @param value the new points player x
      */
     public void setPointsPlayerX(String value) { pointsPlayerXProperty().set(value); }
     
     /**
      * Punkte des Spieler x werden ermittelt.
      *
      * @return the points player x
      */
     public String getPointsPlayerX() { return pointsPlayerXProperty().get(); }
     
     /**
      * Eigenschaften Punkte von Player x.
      *
      * @return the string property
      */
     public StringProperty pointsPlayerXProperty() { 
         if (pointsPlayerX == null) pointsPlayerX = new SimpleStringProperty(this, "pointsPlayerX");
         return pointsPlayerX; 
     }

     

}
