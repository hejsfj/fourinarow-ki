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

	
	public static boolean hasFourInARow(char[][] spielfeld, char spieler){
		
		//Wenn einer der beiden spieler eine viererreihe hat wird true zurueckgegeben
		if(spieler == ' '){
			if(hasFourInARow(spielfeld, 'o') || hasFourInARow(spielfeld, 'x')){
				return true;
			}
		}
		
		//Laufe spalten entlang
		for(int i=0; i<7; i++){
			//Laufe zeilen hoch
			for(int j=0; j<6; j++){
				
				if(spielfeld[i][j]==spieler){
					
				//Pruefe waagerecht
					//Pruefe links -3
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i-3][j] == spieler && spielfeld[i-2][j] == spieler && spielfeld[i-1][j] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					
					//Pruefe links -2 rechts +1
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i-2][j] == spieler && spielfeld[i-1][j] == spieler && spielfeld[i+1][j] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					
					//Pruefe links -1 rechts +2
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i-1][j] == spieler && spielfeld[i+1][j] == spieler && spielfeld[i+2][j] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					
					//Pruefe rechts +3
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i+1][j] == spieler && spielfeld[i+2][j] == spieler && spielfeld[i+3][j] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					
				//Pruefe senkrecht
					//Pruefe unten -3
					try{
						if(spielfeld[i][j-1] == spieler && spielfeld[i][j-2] == spieler && spielfeld[i][j-3] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					
				//Pruefe diagonal vierer
					//Pruefe diagonal links runter -3
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i-3][j-3] == spieler && spielfeld[i-2][j-2] == spieler && spielfeld[i-1][j-1] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					
					//Pruefe diagonal links runter -2
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i-2][j-2] == spieler && spielfeld[i-1][j-1] == spieler && spielfeld[i+1][j+1] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					
					//Pruefe diagonal links runter -1
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i-1][j-1] == spieler && spielfeld[i+1][j+1] == spieler && spielfeld[i+2][j+2] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					
					//Pruefe diagonal rechts hoch
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i+1][j+1] == spieler && spielfeld[i+2][j+2] == spieler && spielfeld[i+3][j+3] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					
					//Pruefe diagonal rechts runter -3
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i+3][j-3] == spieler && spielfeld[i+2][j-2] == spieler && spielfeld[i+1][j-1] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					
					//Pruefe diagonal rechts runter -2
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i+2][j-2] == spieler && spielfeld[i+1][j-1] == spieler && spielfeld[i-1][j+1] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					
					//Pruefe diagonal rechts runter -1
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i+1][j-1] == spieler && spielfeld[i-1][j+1] == spieler && spielfeld[i-2][j+2] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					
					//Pruefe diagonal rechts links hoch
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i-3][j+3] == spieler && spielfeld[i-2][j+2] == spieler && spielfeld[i-1][j+1] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
				}			
				
			}//end zeilen for
		}//end spalten for
		
		return false;
	} //end of viererReihe
	
	public static boolean dreierReihe(char[][] spielfeld, char spieler){
		
		//Wenn einer der beiden spieler eine dreierreihe hat wird true zurueckgegeben
		if(spieler == ' '){
			if(dreierReihe(spielfeld, 'o') || dreierReihe(spielfeld, 'x')){
				return true;
			}
		}
				
		//Laufe spalten entlang
		for(int i=0; i<7; i++){
			//Laufe zeilen hoch
			for(int j=0; j<6; j++){
						
				if(spielfeld[i][j]==spieler){
							
				//Pruefe waagerecht
					//Pruefe links -2
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i-2][j] == spieler && spielfeld[i-1][j] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
							
					//Pruefe links -1 rechts +1
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i-1][j] == spieler && spielfeld[i+1][j] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
							
					//Pruefe rechts +2
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i+1][j] == spieler && spielfeld[i+2][j] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
							
				//Pruefe senkrecht
					//Pruefe unten -2
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i][j-1] == spieler && spielfeld[i][j-2] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
							
				//Pruefe diagonal dreier
					
					//Pruefe diagonal links runter -2
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i-2][j-2] == spieler && spielfeld[i-1][j-1] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					
					//Pruefe diagonal links runter -1
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i-1][j-1] == spieler && spielfeld[i+1][j+1] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					
					//Pruefe diagonal rechts hoch
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i+1][j+1] == spieler && spielfeld[i+2][j+2] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					
					//Pruefe diagonal rechts runter -2
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i+2][j-2] == spieler && spielfeld[i+1][j-1] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					
					//Pruefe diagonal rechts runter -1
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i+1][j-1] == spieler && spielfeld[i-1][j+1] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					
					//Pruefe diagonal rechts links hoch
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i-2][j+2] == spieler && spielfeld[i-1][j+1] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
				}			
						
			}//end zeilen for
		}//end spalten for
				
		return false;
	} //end of dreierReihe
	
	//Prueft ob im aktuellen Spielfeld zwei Steine einer Sorte in einer Reihe liegen
	//Falls kein Parameter uebergeben wird werden beide Arten geprueft
	public static boolean zweierReihe(char[][] spielfeld, char spieler){
						
		//Wenn einer der beiden spieler eine dreierreihe hat wird true zurueckgegeben
		if(spieler== ' '){
			if(zweierReihe(spielfeld, 'o') || zweierReihe(spielfeld, 'x')){
				return true;
			}
		}
						
		//Laufe spalten entlang
		for(int i=0; i<7; i++){
			//Laufe zeilen hoch
			for(int j=0; j<6; j++){
								
				if(spielfeld[i][j]==spieler){
									
				//Pruefe waagerecht
					//Pruefe links -1
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i-1][j] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
									
					//Pruefe rechts +1
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i+1][j] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
								
				//Pruefe senkrecht
					//Pruefe unten -1
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i][j-1] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
									
				//Pruefe diagonal zweier
					//Pruefe links unten
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i-1][j-1] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					
					//Pruefe rechts unten
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i+1][j-1] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					
					//Pruefe links oben
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i-1][j+1] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					
					//Pruefe rechts oben
					try{
						if(spielfeld[i][j-1] != ' ' && spielfeld[i+1][j+1] == spieler){
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					
				}			
								
			}//end zeilen for
		}//end spalten for
						
		return false;
	} //end of zweierReihe
	
	
	//Prueft ob im aktuellen Spielfeld drei Steine einer Sorte in einer Reihe liegen
	//Falls kein Parameter uebergeben wird werden beide Arten geprueft
	public static boolean hasThreeInARow(char[][] aGamefield, char player){

		//Wenn einer der beiden spieler eine dreierreihe hat wird true zurueckgegeben
		if(player == ' '){
			if(hasThreeInARow(aGamefield, 'o') || hasThreeInARow(aGamefield, 'x')){
				return true;
			}
		}

		//Laufe spalten entlang
		for(int i=0; i<7; i++){
			//Laufe zeilen hoch
			for(int j=0; j<6; j++){
				if(aGamefield[i][j] == player){
					//Pruefe waagerecht
					//Pruefe links -2
					try {
						if(aGamefield[i][j-1] != ' ' && aGamefield[i-2][j] == player && aGamefield[i-1][j] == player){
							return true;
						}
						//Pruefe links -1 rechts +1
						if(aGamefield[i][j-1] != ' ' && aGamefield[i-1][j] == player && aGamefield[i+1][j] == player){
							return true;
						}
						//Pruefe rechts +2
						if(aGamefield[i][j-1] != ' ' && aGamefield[i+1][j] == player && aGamefield[i+2][j] == player){
							return true;
						}
						//Pruefe senkrecht
						//Pruefe unten -2
						if(aGamefield[i][j-1] != ' ' && aGamefield[i][j-1] == player && aGamefield[i][j-2] == player){
							return true;
						}
						//Pruefe diagonal dreier
						//Pruefe diagonal links runter -2
						if(aGamefield[i][j-1] != ' ' && aGamefield[i-2][j-2] == player && aGamefield[i-1][j-1] == player){
							return true;
						}
						//Pruefe diagonal links runter -1
						if(aGamefield[i][j-1] != ' ' && aGamefield[i-1][j-1] == player && aGamefield[i+1][j+1] == player){
							return true;
						}
						//Pruefe diagonal rechts hoch
						if(aGamefield[i][j-1] != ' ' && aGamefield[i+1][j+1] == player && aGamefield[i+2][j+2] == player){
							return true;
						}
						//Pruefe diagonal rechts runter -2
						if(aGamefield[i][j-1] != ' ' && aGamefield[i+2][j-2] == player && aGamefield[i+1][j-1] == player){
							return true;
						}
						//Pruefe diagonal rechts runter -1
						if(aGamefield[i][j-1] != ' ' && aGamefield[i+1][j-1] == player && aGamefield[i-1][j+1] == player){
							return true;
						}
						//Pruefe diagonal rechts links hoch
						if(aGamefield[i][j-1] != ' ' && aGamefield[i-2][j+2] == player && aGamefield[i-1][j+1] == player){
							return true;
						}
					} catch (ArrayIndexOutOfBoundsException e){
						//e.printStackTrace();
					}
				}			
			}
		}

		return false;
	}

	//Prueft ob im aktuellen Spielfeld zwei Steine einer Sorte in einer Reihe liegen
	//Falls kein Parameter uebergeben wird werden beide Arten geprueft
	public static boolean hasTwoInARow(char[][] aGamefield, char player) {
		//Wenn einer der beiden spieler eine dreierreihe hat wird true zurueckgegeben
		if(player == ' '){
			if(hasTwoInARow(aGamefield, 'o') || hasTwoInARow(aGamefield, 'x')){
				return true;
			}
		}

		//Laufe spalten entlang
		for(int i=0; i<7; i++){
			//Laufe zeilen hoch
			for(int j=0; j<6; j++){
				if(aGamefield[i][j] == player){
					//Pruefe waagerecht
					//Pruefe links -1
					try {
						if(aGamefield[i][j-1] != ' ' && aGamefield[i-1][j] == player){
							return true;
						}
						//Pruefe rechts +1
						if(aGamefield[i][j-1] != ' ' && aGamefield[i+1][j] == player){
							return true;
						}
						//Pruefe senkrecht
						//Pruefe unten -1
						if(aGamefield[i][j-1] != ' ' && aGamefield[i][j-1] == player){
							return true;
						}
						//Pruefe diagonal zweier
						//Pruefe links unten
						if(aGamefield[i][j-1] != ' ' && aGamefield[i-1][j-1] == player){
							return true;
						}
						//Pruefe rechts unten
						if(aGamefield[i][j-1] != ' ' && aGamefield[i+1][j-1] == player){
							return true;
						}
						//Pruefe links oben
						if(aGamefield[i][j-1] != ' ' && aGamefield[i-1][j+1] == player){
							return true;
						}
						//Pruefe rechts oben
						if(aGamefield[i][j-1] != ' ' && aGamefield[i+1][j+1] == player){
							return true;
						}
					} catch (ArrayIndexOutOfBoundsException e){
						//e.printStackTrace();
					}
				}			
			}
		}

		return false;
	}

	//Prueft ob spieler in dieser runde gewinnen kann und setzt den siegstein
	//Prueft im anschluss ob gegner in dieser runde gewinnen kann und verhindert sieg
	public int siegMusterErkennung(char eigenerStein, char gegnerStein) {
		int spielzug = -1;

		//Siegmuster erkennen
		for(int i=0; i<7;i++){
			for(int j=0; j<6; j++) {
				if(gamefield[i][j] == ' '){
					//Pruefe ob Spieler gewinnen kann:
					//Pruefe waagerecht links -3 
					try {
						if(gamefield[i][j-1] != ' ' && gamefield[i-3][j] == eigenerStein && gamefield[i-2][j] == eigenerStein && gamefield[i-1][j] == eigenerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe waagerecht links -2 und rechts +1
						if(gamefield[i][j-1] != ' ' && gamefield[i-2][j] == eigenerStein && gamefield[i-1][j] == eigenerStein && gamefield[i+1][j] == eigenerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe waagerecht links -1 und rechts +2
						if(gamefield[i][j-1] != ' ' && gamefield[i-1][j] == eigenerStein && gamefield[i+1][j] == eigenerStein && gamefield[i+2][j] == eigenerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe waagerecht rechts +3
						if(gamefield[i][j-1] != ' ' && gamefield[i+1][j] == eigenerStein && gamefield[i+2][j] == eigenerStein && gamefield[i+3][j] == eigenerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe senkrecht
						if(gamefield[i][j-1] != ' ' && gamefield[i][j-1] == eigenerStein && gamefield[i][j-2] == eigenerStein && gamefield[i][j-3] == eigenerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe diagonal links runter -3 
						if(gamefield[i][j-1] != ' ' && gamefield[i-3][j-3] == eigenerStein && gamefield[i-2][j-2] == eigenerStein && gamefield[i-1][j-1] == eigenerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe diagonal links runter -2
						if(gamefield[i][j-1] != ' ' && gamefield[i-2][j-2] == eigenerStein && gamefield[i-1][j-1] == eigenerStein && gamefield[i+1][j+1] == eigenerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe diagonal links runter -1
						if(gamefield[i][j-1] != ' ' && gamefield[i-1][j-1] == eigenerStein && gamefield[i+1][j+1] == eigenerStein && gamefield[i+2][j+2] == eigenerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe diagonal rechts hoch +3
						if(gamefield[i][j-1] != ' ' && gamefield[i+1][j+1] == eigenerStein && gamefield[i+2][j+2] == eigenerStein && gamefield[i+3][j+3] == eigenerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe diagonal rechts runter -3 
						if(gamefield[i][j-1] != ' ' && gamefield[i+3][j-3] == eigenerStein && gamefield[i+2][j-2] == eigenerStein && gamefield[i+1][j-1] == eigenerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe diagonal rechts runter -2
						if(gamefield[i][j-1] != ' ' && gamefield[i+2][j-2] == eigenerStein && gamefield[i+1][j-1] == eigenerStein && gamefield[i-1][j+1] == eigenerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe diagonal rechts runter -1
						if(gamefield[i][j-1] != ' ' && gamefield[i+1][j-1] == eigenerStein && gamefield[i-1][j+1] == eigenerStein && gamefield[i-2][j+2] == eigenerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe diagonal links hoch +3
						if(gamefield[i][j-1] != ' ' && gamefield[i-1][j+1] == eigenerStein && gamefield[i-2][j+2] == eigenerStein && gamefield[i-3][j+3] == eigenerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe ob Gegner gewinnen kann:
						//Pruefe waagerecht links -3 
						if(gamefield[i][j-1] != ' ' && gamefield[i-3][j] == gegnerStein && gamefield[i-2][j] == gegnerStein && gamefield[i-1][j] == gegnerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe waagerecht links -2 und rechts +1
						if(gamefield[i][j-1] != ' ' && gamefield[i-2][j] == gegnerStein && gamefield[i-1][j] == gegnerStein && gamefield[i+1][j] == gegnerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe waagerecht links -1 und rechts +2
						if(gamefield[i][j-1] != ' ' && gamefield[i-1][j] == gegnerStein && gamefield[i+1][j] == gegnerStein && gamefield[i+2][j] == gegnerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe waagerecht rechts +3
						if(gamefield[i][j-1] != ' ' && gamefield[i+1][j] == gegnerStein && gamefield[i+2][j] == gegnerStein && gamefield[i+3][j] == gegnerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe senkrecht
						if(gamefield[i][j-1] != ' ' && gamefield[i][j-1] == gegnerStein && gamefield[i][j-2] == gegnerStein && gamefield[i][j-3] == gegnerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe diagonal links runter -3 
						if(gamefield[i][j-1] != ' ' && gamefield[i-3][j-3] == gegnerStein && gamefield[i-2][j-2] == gegnerStein && gamefield[i-1][j-1] == gegnerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe diagonal links runter -2
						if(gamefield[i][j-1] != ' ' && gamefield[i-2][j-2] == gegnerStein && gamefield[i-1][j-1] == gegnerStein && gamefield[i+1][j+1] == gegnerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe diagonal links runter -1
						if(gamefield[i][j-1] != ' ' && gamefield[i-1][j-1] == gegnerStein && gamefield[i+1][j+1] == gegnerStein && gamefield[i+2][j+2] == gegnerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe diagonal rechts hoch +3
						if(gamefield[i][j-1] != ' ' && gamefield[i+1][j+1] == gegnerStein && gamefield[i+2][j+2] == gegnerStein && gamefield[i+3][j+3] == gegnerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe diagonal rechts runter -3 
						if(gamefield[i][j-1] != ' ' && gamefield[i+3][j-3] == gegnerStein && gamefield[i+2][j-2] == gegnerStein && gamefield[i+1][j-1] == gegnerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe diagonal rechts runter -2
						if(gamefield[i][j-1] != ' ' && gamefield[i+2][j-2] == gegnerStein && gamefield[i+1][j-1] == gegnerStein && gamefield[i-1][j+1] == gegnerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe diagonal rechts runter -1
						if(gamefield[i][j-1] != ' ' && gamefield[i+1][j-1] == gegnerStein && gamefield[i-1][j+1] == gegnerStein && gamefield[i-2][j+2] == gegnerStein){
							spielzug = i;
							return spielzug;
						}
						//Pruefe diagonal links hoch +3
						if(gamefield[i][j-1] != ' ' && gamefield[i-1][j+1] == gegnerStein && gamefield[i-2][j+2] == gegnerStein && gamefield[i-3][j+3] == gegnerStein){
							spielzug = i;
							return spielzug;
						}
					} catch(ArrayIndexOutOfBoundsException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return spielzug;
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
