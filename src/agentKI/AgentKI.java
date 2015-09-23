package agentKI;

import java.util.Random;

import com.sun.javafx.css.CalculatedValue;

import fileInterface.AgentfileWriter;
import fileInterface.Serverfile;
import fileInterface.ServerfileReader;
import gamefield.Gamefield;
import pusherInterface.PusherConnector;

public class AgentKI {
	
	// Zugzeit auf englisch??
	private int zugzeit;
	
	public AgentKI(){
		
	}
	
	public AgentKI(int zugzeit){
		
		// zusammenspiel zwischen Main, Agent_ki und Spielfeld? besser keine redundanten Sachen 
	}
	
	public int calculateMove(){		
		// Zugzeit beachten!!!
		// mögliche Spalten ermitteln, sprich volle Spalten ausschließen -> Woher kommt die Info??
		// if zugzeit < x then random 0-6
		// 
		// wenn es der erste zug ist, dann den stein in die mitte (3) setzen -> Woher kommt die Info???		
		// ansonsten irgend ne logik ausdenken, nach der die Steine gesetzt werden...
		
		// UNBEDINGT PRÜFEN: IST ZUG VALIDE!!!! SPRICH PASST DAS DING IN DIE SPALTE
		return calculateRandomMove();
	}
	
	private int calculateRandomMove(){
		Random random = new Random();

		int randomNumber = random.nextInt(7);
		return randomNumber;
	}
	
	
	
}
