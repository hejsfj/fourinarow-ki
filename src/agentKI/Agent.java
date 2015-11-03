package agentKI;

import java.util.Random;

import gamefield.Gamefield;

/**
 * Klasse zur Berechnung des nächsten Spielzuges.
 *
 */
public class Agent {
	private boolean isAttackingFirstLineStrategyPossible = true;
	private char myPlayer;
	private char opponentPlayer;
	private char startPlayer;

	/**
	 * Default-Konstruktur der Agenten-Klasse.
	 */
	public Agent(){}
	
	/**
	 * Methode zum Setzen des Startspielers in der Agenten-Klasse.
	 * @param startPlayer
	 *            'o' entspricht "Spieler O" und 'x' entspricht "Spieler X"
	 */
	public void setStartplayer(char startPlayer){
		this.startPlayer = startPlayer;
	}
	
	/**
	 * Methode zum Setzen des gegnerischen Spielers in der Agenten-Klasse.
	 * @param opponentPlayer
	 *            'o' entspricht "Spieler O" und 'x' entspricht "Spieler X"
	 */
	public void setOpponentPlayer(char opponentPlayer){
		this.opponentPlayer = opponentPlayer;
	}
	
	/**
	 * Methode zum Setzen des eigenen Spielers in der Agenten-Klasse.
	 * @param myPlayer
	 *            'o' entspricht "Spieler O" und 'x' entspricht "Spieler X"
	 */
	public void setMyPlayer(char myPlayer){
		this.myPlayer = myPlayer;
	}
	
	/**
	 * Kalkuliert den nächsten Zug.
	 * @param gamefield {@link gamefield.Gamefield#Gamefield()}
	 * @return Spalte, in die der Spielstein gesetzt wird.
	 */
	public int calculateMove(Gamefield gamefield) {		
		if (myPlayer == startPlayer) {
			if (isAttackingFirstLineStrategyPossible){
				return playAttackingFirstLineStrategy(gamefield);
			} else {
				return playDefensiveStrategy(gamefield);
			}
		}	
		else {
			return playDefensiveStrategy(gamefield);
		}
	}
	
	private boolean isFourInARowInBottomRowPossible(Gamefield gamefield){
		return  gamefield.isFieldOccupiedForPlayer(0, 1, myPlayer) && 
				gamefield.isFieldOccupiedForPlayer(0, 2, myPlayer) &&
				gamefield.isFieldOccupiedForPlayer(0, 3, myPlayer) ||
				
				gamefield.isFieldOccupiedForPlayer(0, 2, myPlayer) && 
				gamefield.isFieldOccupiedForPlayer(0, 3, myPlayer) &&
				gamefield.isFieldOccupiedForPlayer(0, 4, myPlayer) ||
	
				gamefield.isFieldOccupiedForPlayer(0, 3, myPlayer) && 
				gamefield.isFieldOccupiedForPlayer(0, 4, myPlayer) &&
				gamefield.isFieldOccupiedForPlayer(0, 5, myPlayer);
	}
	
	private int calculateMoveToGet4InARowInBottomRow(Gamefield gamefield){
		if (gamefield.isFieldEmpty(0, 0)){
			return 0;
		} else if (gamefield.isFieldEmpty(0, 4)) {
			return 4;
		}
		
		if (gamefield.isFieldEmpty(0, 1)){
			return 1;
		} else if (gamefield.isFieldEmpty(0, 5)) {
			return 5;
		}
		
		if (gamefield.isFieldEmpty(0, 2)){
			return 2;
		} else if (gamefield.isFieldEmpty(0, 6)) {
			return 6;
		}
		// if 4inARow is not possible, calc random move
		return calculateRandomMove(gamefield);
	}
	
	private int playAttackingFirstLineStrategy(Gamefield gamefield){
		// Strategie: mit 4 Zügen zum gewinn
		// plan: erreichen von ___xx_x oder x_xx___ 
		// oder erreichen von __xxx__
		
		if (gamefield.isEmpty()){
			System.out.println("starting move! 3");
			return 3;
		} 

		if (isFourInARowInBottomRowPossible(gamefield)) {
			return calculateMoveToGet4InARowInBottomRow(gamefield);
		}
		
		if (areFieldsLeftFromBottomMidNotOccupiedByOpponent(gamefield)) {
			System.out.println("our plan is perfect left side");
			
			if (gamefield.isFieldEmpty(5, 2)){
				System.out.println(2);
				return 2;
			} 
			if (gamefield.isFieldEmpty(5, 1) && gamefield.isFieldEmpty(5, 4)){
				System.out.println(1);
				return 1;
			} 
			if (gamefield.isFieldEmpty(5, 0)){
				System.out.println(0);
				return 0;
			} else if (gamefield.isFieldEmpty(5, 1)){
				System.out.println(1);
				return 1;
			}
			if (gamefield.isFieldEmpty(5, 5)){
				System.out.println(5);
				return 5;
			}
		}
		if(areFieldsRightFromBottomMidNotOccupiedByOpponent(gamefield)) {

			System.out.println("our plan is perfect right side");
			if (gamefield.isFieldEmpty(5, 4)){
				System.out.println(4);
				return 4;
			} 
			if (gamefield.isFieldEmpty(5, 5) && gamefield.isFieldEmpty(5, 2)){
				System.out.println(5);
				return 5;
			} 
			if (gamefield.isFieldEmpty(5, 6)){
				System.out.println(6);
				return 6;
			} 
			else if (gamefield.isFieldEmpty(5, 2)){
				System.out.println(2);
				return 2;
				
			}else if (gamefield.isFieldEmpty(5, 5)){
				System.out.println(5);
				return 5;
			}
			if (gamefield.isFieldEmpty(5, 1)){
				System.out.println(1);
				return 1;
			}
		}
		isAttackingFirstLineStrategyPossible = false;
		return playDefensiveStrategy(gamefield);
	}
	
	private int playDefensiveStrategy(Gamefield gamefield){
		int move;
		if ((move = playDefensiveFirstLineStrategy(gamefield)) == -1) {
			System.out.println("firstLineStrategy over");
			if ((move = gamefield.findWinPatternAndGetWinningMove(myPlayer)) == -1){
				System.out.println("no winning pattern for our player gefunden");
				if ((move = gamefield.findWinPatternAndGetWinningMove(opponentPlayer)) == -1){
					System.out.println("no winning pattern for opponent player gefunden");
					return calculateRandomMove(gamefield);
				}
			}
		}
		if (!gamefield.isValidMove(move)){
			return calculateRandomMove(gamefield);
		}
		return move;
	}
	
	private int playDefensiveFirstLineStrategy(Gamefield gamefield){
		if (gamefield.isFieldEmpty(5, 3)){
			System.out.println("playing defensive first line");
			return 3;			
		}
		
		if (gamefield.isFieldEmpty(5, 2)){
			System.out.println("playing defensive first line");
			return 2;
		}

		if (gamefield.isFieldOccupiedForPlayer(5, 2, myPlayer) && gamefield.isFieldEmpty(5, 6)){
			System.out.println("playing defensive first line");
			return 6;
		}
		
		if (gamefield.isFieldEmpty(5, 4)){
			System.out.println("playing defensive first line");
			return 4;
		}
		
		if (gamefield.isFieldOccupiedForPlayer(5, 4, myPlayer) && gamefield.isFieldEmpty(5, 0)){
			System.out.println("playing defensive first line");
			return 0;
		}
		
		return -1;
	}
	
	private boolean areFieldsLeftFromBottomMidNotOccupiedByOpponent(Gamefield gamefield){
		return  (!gamefield.isFieldOccupiedForPlayer(5, 0, opponentPlayer)) && 
				(!gamefield.isFieldOccupiedForPlayer(5, 1, opponentPlayer)) &&
				(!gamefield.isFieldOccupiedForPlayer(5, 2, opponentPlayer));
	}
	
	private boolean areFieldsRightFromBottomMidNotOccupiedByOpponent(Gamefield gamefield){
		return  (!gamefield.isFieldOccupiedForPlayer(5, 4, opponentPlayer)) && 
				(!gamefield.isFieldOccupiedForPlayer(5, 5, opponentPlayer)) &&
				(!gamefield.isFieldOccupiedForPlayer(5, 6, opponentPlayer));
	}
	
	private int calculateRandomMove(Gamefield gamefield){
		Random random = new Random();
		int randomNumber = random.nextInt(7);
		
		System.out.println("random is much more fun");
		if (gamefield.isValidMove(randomNumber)){
			return randomNumber;
		} 
		return calculateRandomMove(gamefield);
	}
}
