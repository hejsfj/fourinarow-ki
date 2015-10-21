package database;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DatabaseSetRecord {
	
	public DatabaseSetRecord(String gameId, String setId, String winner, String starter, String pointsPlayerO, String pointsPlayerX){
		setGameId(gameId);
		setSetId(setId);
		setStarter(starter);
		setWinner(winner);
		setPointsPlayerO(pointsPlayerO);
		setPointsPlayerX(pointsPlayerX);
	}

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
     public void setGameId(String value) { gameIdProperty().set(value); }
     public String getGameId() { return gameIdProperty().get(); }
     public StringProperty gameIdProperty() { 
         if (gameId == null) gameId = new SimpleStringProperty(this, "gameId");
         return gameId; 
     }
 
     private StringProperty setId;
     public void setSetId(String value) { setIdProperty().set(value); }
     public String getSetId() { return setIdProperty().get(); }
     public StringProperty setIdProperty() { 
         if (setId == null) setId = new SimpleStringProperty(this, "setId");
         return setId; 
     } 

     private StringProperty winner;
     public void setWinner(String value) { winnerProperty().set(value); }
     public String getWinner() { return winnerProperty().get(); }
     public StringProperty winnerProperty() { 
         if (winner == null) winner = new SimpleStringProperty(this, "winner");
         return winner; 
     } 


     private StringProperty starter;
     public void setStarter(String value) { starterProperty().set(value); }
     public String getStarter() { return starterProperty().get(); }
     public StringProperty starterProperty() { 
         if (starter == null) starter = new SimpleStringProperty(this, "starter");
         return starter; 
     } 
     
     private StringProperty pointsPlayerO;
     public void setPointsPlayerO(String value) { pointsPlayerOProperty().set(value); }
     public String getPointsPlayerO() { return pointsPlayerOProperty().get(); }
     public StringProperty pointsPlayerOProperty() { 
         if (pointsPlayerO == null) pointsPlayerO = new SimpleStringProperty(this, "pointsPlayerO");
         return pointsPlayerO; 
     }
     
     private StringProperty pointsPlayerX;
     public void setPointsPlayerX(String value) { pointsPlayerXProperty().set(value); }
     public String getPointsPlayerX() { return pointsPlayerXProperty().get(); }
     public StringProperty pointsPlayerXProperty() { 
         if (pointsPlayerX == null) pointsPlayerX = new SimpleStringProperty(this, "pointsPlayerX");
         return pointsPlayerX; 
     }

     

}
