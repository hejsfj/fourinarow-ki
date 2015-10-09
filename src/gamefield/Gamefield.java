package gamefield;

import javafx.geometry.HPos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Gamefield {
	private char[][] gamefield;
	private final int numRows = 6;
	private final int numColumns = 7;
	private boolean isEmpty = true;
	
	public Gamefield(){
		gamefield = new char[numRows][numColumns];
		initGame();
	}
	
	public void initGame(){
		 for(int rowIndex = 0;rowIndex < numRows;rowIndex++) {
	         for(int columnIndex=0;columnIndex<numColumns;columnIndex++) {
	        	 gamefield[rowIndex][columnIndex]=' ';
	         }
	     }
	}
	
	public boolean hasWinner(){
		boolean gewonnen = false;
        // Horizontal
        for(int i=0;!gewonnen && i<6;i++) {
            int count = gamefield[i][0] == ' ' ? 0 : 1;
            for(int j=1;!gewonnen && j<7;j++) {
                if(gamefield[i][j]!=' ' && gamefield[i][j]==gamefield[i][j-1]) {
                    count++;
                } else {
                    count=gamefield[i][j]==' ' ? 0 : 1;
                }
                if(count==4) {
                    gewonnen=true;
                }
            }
        }

        // Vertikal
        for(int j=0;!gewonnen && j<numColumns;j++) {
            int count=gamefield[0][j]==' ' ? 0 : 1;
            for(int i=1;!gewonnen && i<numRows;i++) {
                if(gamefield[i][j]!=' ' && gamefield[i][j]==gamefield[i-1][j]) {
                    count++;
                } else {
                    count=gamefield[i][j]==' ' ? 0 : 1;
                }
                if(count==4) {
                    gewonnen=true;
                }
            }
        }

        return gewonnen;
	}	
	
	public void insertCoin(GridPane gridPane, int columnIndex, char player) {		
		for (int rowIndex = numRows - 1; rowIndex >= 0; rowIndex--) {
			if (gamefield[rowIndex][columnIndex] == ' ' )
			{
				gamefield[rowIndex][columnIndex] = player;
				addNewCoinToGridPane(gridPane, columnIndex, rowIndex, player);
				if (isEmpty) { isEmpty = false;}
				break;
			}
		}
	}

	public boolean isEmpty(){
		return isEmpty;
	}
	public boolean isValidMove(int columnIndex){
		return !isColumnFull(columnIndex);
	}
	private boolean isColumnFull(int columnIndex){
		 return gamefield[0][columnIndex] != ' ';
	}
	private void addNewCoinToGridPane(GridPane pane, int columnIndex, int rowIndex, char player){
		Circle newCircle = getPlayerCircle(player);
		pane.add(newCircle, columnIndex, rowIndex);
		GridPane.setHalignment(newCircle, HPos.CENTER);
	}
	
	private Circle getPlayerCircle(char player){
		return new Circle(22.5, getColorForPlayer(player));
	}
	
	private Color getColorForPlayer(char player){
		if (player == 'o')
			return getSpielerOColor();
		else if(player == 'x')
			return getSpielerXColor();
		else 
			return new Color(1.0, 1.0, 1.0, 1.0);
	}
	
	private Color getSpielerXColor(){
		final int[] rgbValues = getRGBValuesFromColorHexCode("ff3d00");	   
	    return Color.rgb(rgbValues[0],rgbValues[1],rgbValues[2]);
	}	
	
	private Color getSpielerOColor(){
		final int[] rgbValues = getRGBValuesFromColorHexCode("fff500");	   
	    return Color.rgb(rgbValues[0],rgbValues[1],rgbValues[2]);
	}	
	
	private int[] getRGBValuesFromColorHexCode(String colorHexCode){
		final int[] ret = new int[3];
	    for (int i = 0; i < 3; i++) {
	        ret[i] = Integer.parseInt(colorHexCode.substring(i * 2, i * 2 + 2), 16);
	    }
	    return ret;		
	}
}
