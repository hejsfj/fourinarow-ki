package gamefield;

import javafx.geometry.HPos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


/**
 * Dient der physikalische Repräsenation des Spielfeldes.
 * Es ist als zweidimensionales Array realisiert.
 */
public class Gamefield {
	private char[][] gamefield;
	private final int numRows = 6;
	private final int numColumns = 7;
	private boolean isEmpty = true;

	/**
	 * Instanziiert ein neues Gamefield-Objekt.
	 */
	public Gamefield(){
		gamefield = new char[numRows][numColumns];
		initGame();
	}

	/**
	 * Initialisierung des Spiels.
	 */
	public void initGame(){
		for(int rowIndex = 0;rowIndex < numRows;rowIndex++) {
			for(int columnIndex=0;columnIndex<numColumns;columnIndex++) {
				gamefield[rowIndex][columnIndex]=' ';
			}
		}
	}

	/**
	 * Überprüft ob es einen Gewinner gibt.
	 *
	 * @return true, wenn ein Gewinner exisitert.
	 */
	public boolean hasWinner(){
		return 	hasHorizontalWinner()	||
				hasVerticalWinner() 	||
				hasDiagonalWinner();
	}	

	/**
	 * Setzt einen Spielstein
	 *
	 * @param gridPane {@link javafx.scene.layout.GridPane}
	 * @param columnIndex der Spaltenindex
	 * @param player der Spielername
	 */
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

	/**
	 * Überprüft ob das Spielfeld leer ist.
	 *
	 * @return true, wenn das Spielfeld leer ist.
	 */
	public boolean isEmpty(){
		return isEmpty;
	}

	/**
	 * Überprüft ob das Spielfeld voll ist.
	 *
	 * @return true, wenn das Spielfeld voll ist.
	 */
	public boolean isFull(){
		for (int columnIndex = 0; columnIndex < numColumns; columnIndex++){
			if (isColumnFull(columnIndex)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Überprüft ob eine bestimmte Stelle im Spielfeld besetzt ist.
	 *
	 * @return true, wenn die Stelle besetzt ist.
	 */
	public boolean isFieldEmpty(int rowIndex, int columnIndex){
		return gamefield[rowIndex][columnIndex] == ' ';
	}

	/**
	 * Überprüft ob eine bestimmte Stelle im Spielfeld für einen bestimmten Spieler besetzt ist.
	 *
	 * @return true, wenn die Stelle für einen Spieler besetzt ist.
	 */
	public boolean isFieldOccupiedForPlayer(int rowIndex, int columnIndex, char player){
		return gamefield[rowIndex][columnIndex] == player;
	}

	/**
	 * Überprüft ob der Spielzug valide ist.
	 *
	 * @param columnIndex der Spaltenindex in den geworfen wird
	 * @return true, wenn der Spielzug valide ist.
	 */
	public boolean isValidMove(int columnIndex){
		return !isColumnFull(columnIndex);
	}

	//Prueft ob spieler in dieser runde gewinnen kann und setzt den siegstein
	//Prueft im anschluss ob gegner in dieser runde gewinnen kann und verhindert sieg
	public int findWinPatternAndGetWinningMove(char player) {
		int move = -1;

		//Siegmuster erkennen
		for(int rowIndex = 0; rowIndex < 6;rowIndex++){
			for(int columnIndex = 0; columnIndex < 7; columnIndex++) {
				if(gamefield[rowIndex][columnIndex] == ' '){
					try {
						//Pruefe senkrecht
						if(gamefield[rowIndex+1][columnIndex] == player && gamefield[rowIndex+2][columnIndex] == player && gamefield[rowIndex+3][columnIndex] == player){
							move = columnIndex;
							System.out.println("found vertical winning Pattern");
							return move;
						}
					} catch(ArrayIndexOutOfBoundsException e) {
					}
					
					try {
						//Pruefe waagerecht links -3 
						if (rowIndex == 5){
							if(gamefield[rowIndex][columnIndex-3] == player && gamefield[rowIndex][columnIndex-2] == player && gamefield[rowIndex][columnIndex-1] == player){
								move = columnIndex;
								System.out.println("found horizontal winning Pattern");
								return move;
							}
						}
						else {
							if(gamefield[rowIndex+1][columnIndex] != ' ' && gamefield[rowIndex][columnIndex-3] == player && gamefield[rowIndex][columnIndex-2] == player && gamefield[rowIndex][columnIndex-1] == player){
								move = columnIndex;
								System.out.println("found horizontal winning Pattern");
								return move;
							}
						}
					} catch(ArrayIndexOutOfBoundsException e) {
					}

					try {
						//Pruefe waagerecht links -2 und rechts +1
						if (rowIndex == 5){
							if(gamefield[rowIndex][columnIndex-2] == player && gamefield[rowIndex][columnIndex-1] == player && gamefield[rowIndex][columnIndex+1] == player){
								move = columnIndex;
								System.out.println("found horizontal winning Pattern");
								return move;
							}
						} else {
							if(gamefield[rowIndex+1][columnIndex] != ' ' && gamefield[rowIndex][columnIndex-2] == player && gamefield[rowIndex][columnIndex-1] == player && gamefield[rowIndex][columnIndex+1] == player){
								move = columnIndex;
								System.out.println("found horizontal winning Pattern");
								return move;
							}
						}
					} catch(ArrayIndexOutOfBoundsException e) {
					}

					try {
						//Pruefe waagerecht links -1 und rechts +2
						if (rowIndex == 5){
							if(gamefield[rowIndex+1][columnIndex] != ' ' && gamefield[rowIndex][columnIndex-1] == player && gamefield[rowIndex][columnIndex+1] == player && gamefield[rowIndex][columnIndex+2] == player){
								move = columnIndex;
								System.out.println("found horizontal winning Pattern");
								return move;
							}
						} else {
							if(gamefield[rowIndex+1][columnIndex] != ' ' && gamefield[rowIndex][columnIndex+1] == player && gamefield[rowIndex][columnIndex+2] == player && gamefield[rowIndex][columnIndex+3] == player){
								move = columnIndex;
								System.out.println("found horizontal winning Pattern");
								return move;
							}
						}
						
					} catch(ArrayIndexOutOfBoundsException e) {
					}

					try {
						//Pruefe waagerecht rechts +3
						if (rowIndex == 5){
							if(gamefield[rowIndex][columnIndex+1] == player && gamefield[rowIndex][columnIndex+2] == player && gamefield[rowIndex][columnIndex+3] == player){
								move = columnIndex;
								System.out.println("found horizontal winning Pattern");
								return move;
							}
						} else {
							if(gamefield[rowIndex+1][columnIndex] != ' ' && gamefield[rowIndex][columnIndex+1] == player && gamefield[rowIndex][columnIndex+2] == player && gamefield[rowIndex][columnIndex+3] == player){
								move = columnIndex;
								System.out.println("found horizontal winning Pattern");
								return move;
							}
						}
					} catch(ArrayIndexOutOfBoundsException e) {
					}

					try {
						//Pruefe diagonal links runter -3 
						if(gamefield[rowIndex-1][columnIndex] != ' ' && gamefield[rowIndex-3][columnIndex-3] == player && gamefield[rowIndex-2][columnIndex-2] == player && gamefield[rowIndex-1][columnIndex-1] == player){
							move = columnIndex;
							System.out.println("found diagonal winning Pattern");
							return move;
						}
					} catch(ArrayIndexOutOfBoundsException e) {
					}

					try {
						//Pruefe diagonal links runter -2 und rechts hoch +1
						if(gamefield[rowIndex-1][columnIndex] != ' ' && gamefield[rowIndex-2][columnIndex-2] == player && gamefield[rowIndex-1][columnIndex-1] == player && gamefield[rowIndex+1][columnIndex+1] == player){
							move = columnIndex;
							System.out.println("found diagonal winning Pattern");
							return move;
						}
					} catch(ArrayIndexOutOfBoundsException e) {
					}

					try {
						//Pruefe diagonal links runter -1 und rechts hoch +2
						if(gamefield[rowIndex-1][columnIndex] != ' ' && gamefield[rowIndex-1][columnIndex-1] == player && gamefield[rowIndex+1][columnIndex+1] == player && gamefield[rowIndex+2][columnIndex+2] == player){
							move = columnIndex;
							System.out.println("found diagonal winning Pattern");
							return move;
						}
					} catch(ArrayIndexOutOfBoundsException e) {
					}

					try {
						//Pruefe diagonal rechts hoch +3
						if(gamefield[rowIndex-1][columnIndex] != ' ' && gamefield[rowIndex+1][columnIndex+1] == player && gamefield[rowIndex+2][columnIndex+2] == player && gamefield[rowIndex+3][columnIndex+3] == player){
							move = columnIndex;
							System.out.println("found diagonal winning Pattern");
							return move;
						}
					} catch(ArrayIndexOutOfBoundsException e) {
					}

					try {
						//Pruefe diagonal rechts runter -3 
						if(gamefield[rowIndex-1][columnIndex] != ' ' && gamefield[rowIndex+3][columnIndex-3] == player && gamefield[rowIndex+2][columnIndex-2] == player && gamefield[rowIndex+1][columnIndex-1] == player){
							move = columnIndex;
							System.out.println("found diagonal winning Pattern");
							return move;
						}
					} catch(ArrayIndexOutOfBoundsException e) {
					}

					try {
						//Pruefe diagonal rechts runter -2 und links hoch +1
						if(gamefield[rowIndex-1][columnIndex] != ' ' && gamefield[rowIndex+2][columnIndex-2] == player && gamefield[rowIndex+1][columnIndex-1] == player && gamefield[rowIndex-1][columnIndex+1] == player){
							move = columnIndex;
							System.out.println("found diagonal winning Pattern");
							return move;
						}
					} catch(ArrayIndexOutOfBoundsException e) {
					}

					try {
						//Pruefe diagonal rechts runter -1  und links hoch +2
						if(gamefield[rowIndex-1][columnIndex] != ' ' && gamefield[rowIndex+1][columnIndex-1] == player && gamefield[rowIndex-1][columnIndex+1] == player && gamefield[rowIndex-2][columnIndex+2] == player){
							move = columnIndex;
							System.out.println("found diagonal winning Pattern");
							return move;
						}
					} catch(ArrayIndexOutOfBoundsException e) {
					}

					try {
						//Pruefe diagonal links hoch +3
						if(gamefield[rowIndex-1][columnIndex] != ' ' && gamefield[rowIndex-1][columnIndex+1] == player && gamefield[rowIndex-2][columnIndex+2] == player && gamefield[rowIndex-3][columnIndex+3] == player){
							move = columnIndex;
							System.out.println("found diagonal winning Pattern");
							return move;
						}
					} catch(ArrayIndexOutOfBoundsException e) {
					}
				}
			}
		}
		return move;
	}

	private boolean isColumnFull(int columnIndex){
		return gamefield[0][columnIndex] != ' ';
	}

	private boolean hasHorizontalWinner(){
		for(int rowIndex = 0; rowIndex < 6; rowIndex++) {
			int count = gamefield[rowIndex][0] == ' ' ? 0 : 1;
			for(int columnIndex = 1; columnIndex < 7; columnIndex++) {
				if(gamefield[rowIndex][columnIndex] != ' ' && gamefield[rowIndex][columnIndex] == gamefield[rowIndex][columnIndex-1]) {
					count++;
				} else {
					count = gamefield[rowIndex][columnIndex] == ' ' ? 0 : 1;
				}
				if(count == 4) {
					System.out.println("horizontal winner");
					return true;
				}
			}
		}
		return false;
	}

	private boolean hasVerticalWinner(){
		for(int columnIndex = 0;  columnIndex < numColumns; columnIndex++) {
			int count = gamefield[0][columnIndex]==' ' ? 0 : 1;
			for(int rowIndex=1; rowIndex < numRows; rowIndex++) {
				if(gamefield[rowIndex][columnIndex] != ' ' && gamefield[rowIndex][columnIndex] == gamefield[rowIndex-1][columnIndex]) {
					count++;
				} else {
					count = gamefield[rowIndex][columnIndex]==' ' ? 0 : 1;
				}
				if(count == 4) {
					System.out.println("vertical winner");
					return true;
				}
			}
		}
		return false;
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
