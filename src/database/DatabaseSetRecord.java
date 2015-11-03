package database;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Diese Klasse repräsentiert einen Datensatz aus der Tabelle "Satz".
 */
public class DatabaseSetRecord {

	 private StringProperty gameId;
     private StringProperty setId;
     private StringProperty winner;
     private StringProperty starter;
     private StringProperty pointsPlayerO;
     private StringProperty pointsPlayerX;
     
	/**
	 * Konstruktur zum Instanziieren eines neuen Datensatz-Objektes.
	 * 
     * @param gameId Spiel-Id
	 * @param setId Satz-Nr
	 * @param winner Gewinner
	 * @param starter Startspieler
	 * @param pointsPlayerO Punkte von Spieler O
	 * @param pointsPlayerX Punkte von Spieler X
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
	 * @return Gibt das passende Attribut zu einer Id zurück.
	 * 
	 * @param id Id
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
     
     /**
      * Setzt die Spiel-Id.
      * @param value Spiel-Id
      */
     public void setGameId(String value) { 
    	 gameIdProperty().set(value); 
     }
     
     /**
      * @return Gibt die Spiel-Id zurück.
      */
     public String getGameId() { 
    	 return gameIdProperty().get(); 
     }

     /**
      * Setzt die Satz-Id.
      * @param value Satz-Id
      */
     public void setSetId(String value) { 
    	 setIdProperty().set(value); 
     }
     
     /**
      * @return Gibt die Satz-Id zurück.
      */
     public String getSetId() { 
    	 return setIdProperty().get(); 
     }
     
     /**
      * Setzt den Gewinner.
      * @param value Gewinner
      */
     public void setWinner(String value) { 
    	 winnerProperty().set(value); 
     }
     
     /**
      * @return Gibt den Gewinner zurück.
      */
     public String getWinner() { 
    	 return winnerProperty().get(); 
     }
     
     /**
      * Setzt den Startspieler.
      * @param value Startspieler
      */
     public void setStarter(String value) { 
    	 starterProperty().set(value); 
     }
     
     /**
      * @return Gibt den Startspieler zurück.
      */
     public String getStarter() { 
    	 return starterProperty().get(); 
     }
     
     /**
      * Setzt die Punkte von Spieler O.
      * @param value Punkte von Spieler O
      */
     public void setPointsPlayerO(String value) { 
    	 pointsPlayerOProperty().set(value); 
     }
     
     /**
      * @return Gibt die Punktzahl von Spieler O zurück.
      */
     public String getPointsPlayerO() { 
    	 return pointsPlayerOProperty().get(); 
     }
     
     /**
      * Setzt die Punkte von Spieler X.
      * @param value Punkte von Spieler X
      */
     public void setPointsPlayerX(String value) { 
    	 pointsPlayerXProperty().set(value); 
     }
     
     /**
      * @return Gibt die Punktzahl von Spieler X zurück.
      */
     public String getPointsPlayerX() { 
    	 return pointsPlayerXProperty().get(); 
     }
     
     private StringProperty pointsPlayerOProperty() { 
         if (pointsPlayerO == null) pointsPlayerO = new SimpleStringProperty(this, "pointsPlayerO");
         return pointsPlayerO; 
     }
     
     private StringProperty pointsPlayerXProperty() { 
         if (pointsPlayerX == null) pointsPlayerX = new SimpleStringProperty(this, "pointsPlayerX");
         return pointsPlayerX; 
     }

     private StringProperty gameIdProperty() { 
         if (gameId == null) gameId = new SimpleStringProperty(this, "gameId");
         return gameId; 
     }
     
     private StringProperty setIdProperty() { 
         if (setId == null) setId = new SimpleStringProperty(this, "setId");
         return setId; 
     } 
     
     private StringProperty winnerProperty() { 
         if (winner == null) winner = new SimpleStringProperty(this, "winner");
         return winner; 
     } 
     
     private StringProperty starterProperty() { 
         if (starter == null) starter = new SimpleStringProperty(this, "starter");
         return starter; 
     } 
}
