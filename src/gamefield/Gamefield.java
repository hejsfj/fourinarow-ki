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
		
		return 	hasHorizontalWinner()	||
				hasVerticalWinner() 	||
				hasDiagonalWinner();
        
	}	

	private boolean hasDiagonalWinner(){
		
		//rechts unten nach links oben
		for (int rowIndex = 0; rowIndex <= 2; rowIndex++) {
			for (int columnIndex = 0; columnIndex <= 3; columnIndex++) {
				char player = gamefield[rowIndex][columnIndex];
				if (player != ' '){
					if (player == gamefield[rowIndex + 1][columnIndex + 1] &&
						player == gamefield[rowIndex + 2][columnIndex + 2] &&
						player == gamefield[rowIndex + 3][columnIndex + 3]){
	                		System.out.println("diagonal winner");
							return true;				
							}
				}								
			}
		}		
		// links unten nach rechts oben
		for(int rowIndex = 0; rowIndex <= 2; rowIndex++) {
			for(int columnIndex  = 3; columnIndex <= 6; columnIndex++) {
				char player = gamefield[rowIndex][columnIndex];
				if (player != ' '){
					if (player == gamefield[rowIndex + 1][columnIndex - 1] &&
						player == gamefield[rowIndex + 2][columnIndex - 2] &&
						player == gamefield[rowIndex + 3][columnIndex - 3]){
                			System.out.println("diagonal winner");
							return true;			
						}	
				}
			}
		}		
		
	    return false;
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
	
	public boolean isFull(){
		for (int columnIndex = 0; columnIndex < numColumns; columnIndex++){
			if (isColumnFull(columnIndex)){
				return true;
			}
		}
		return false;
	}
	public boolean isValidMove(int columnIndex){
		return !isColumnFull(columnIndex);
	}
	private boolean isColumnFull(int columnIndex){
		 return gamefield[0][columnIndex] != ' ';
	}
	
	private boolean hasHorizontalWinner(){
        for(int rowIndex=0; rowIndex < 6; rowIndex++) {
            int count = gamefield[rowIndex][0] == ' ' ? 0 : 1;
            for(int columnIndex = 1; columnIndex < 7; columnIndex++) {
                if(gamefield[rowIndex][columnIndex]!=' ' && gamefield[rowIndex][columnIndex] == gamefield[rowIndex][columnIndex-1]) {
                    count++;
                } else {
                    count=gamefield[rowIndex][columnIndex]==' ' ? 0 : 1;
                }
                if(count==4) {
                	System.out.println("horizontal winner");
                    return true;
                }
            }
        }
        return false;
	}
	
	private boolean hasVerticalWinner(){
        for(int columnIndex = 0;  columnIndex < numColumns; columnIndex++) {
            int count=gamefield[0][columnIndex]==' ' ? 0 : 1;
            for(int rowIndex=1; rowIndex<numRows;rowIndex++) {
                if(gamefield[rowIndex][columnIndex]!=' ' && gamefield[rowIndex][columnIndex]==gamefield[rowIndex-1][columnIndex]) {
                    count++;
                } else {
                    count=gamefield[rowIndex][columnIndex]==' ' ? 0 : 1;
                }
                if(count==4) {
                	System.out.println("vertical winner");
                    return true;
                }
            }
        }
		return false;
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
