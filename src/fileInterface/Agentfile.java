package fileInterface;

/**
 * Diese Klasse repr�sentiert die Textdatei, die vom Agenten geschrieben wird, um einen Spielstein zu setzen.
 */
public class Agentfile {
	private int move;	
	
	/**
	 * Kostruktur f�r die Agenten-Textdatei.
	 * 
	 * @param move Spalte in die der Spielstein geworfen werden soll.
	 */
	public Agentfile(int move){
		this.move = move;		
	}
	
	/**
	 * Getter um die Spalte, in die geworfen wird abzufragen.
	 * 
	 * @return die Spalte in die der Spielstein geworfen wird.
	 */
	public int getMove(){
		return move;		
	}
}
