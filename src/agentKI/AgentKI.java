package agentKI;

import java.util.Random;

import gamefield.Gamefield;

/**
 * Dient der Berechnung des n�chsten Spielzuges
 * @author Tim
 *
 */
public class AgentKI {
	
	// Zugzeit auf englisch??
	/**
	 * Zeit, die Agent zur Berechnung seines Spielzuges hat.
	 */
	private int zugzeit;
	
	/**
	 * default-Konstruktur der AgentKI-Klasse
	 */
	public AgentKI(){
		
	}
	
	/**
	 * benutzerdefinierter-Konstruktur der AgentKI-Klasse
	 * @param zugzeit {@link #zugzeit}
	 */
	public AgentKI(int zugzeit){
		
		// zusammenspiel zwischen Main, Agent_ki und Spielfeld? besser keine redundanten Sachen 
	}
	
	/**
	 * kalkuliert den n�chsten Zug
	 * @param gamefield {@link gamefield.Gamefield#Gamefield()}
	 * @return Spalte in der Spielstein gesetzt werden soll
	 */
	public int calculateMove(Gamefield gamefield){		
		// Zugzeit beachten!!!
		// m�gliche Spalten ermitteln, sprich volle Spalten ausschlie�en -> Woher kommt die Info??
		// if zugzeit < x then random 0-6
		// 
		
		if (gamefield.isEmpty()){
			return 3;
		}
		// UNBEDINGT PR�FEN: IST ZUG VALIDE!!!! SPRICH PASST DAS DING IN DIE SPALTE
		int move = 3;
		while (!gamefield.isValidMove(move)){
			move = calculateRandomMove();
		}
		return move;
	}
	
	private int calculateRandomMove(){
		Random random = new Random();

		int randomNumber = random.nextInt(7);
		return randomNumber;
	}
	
	
	
}
