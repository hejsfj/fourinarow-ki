
package fileInterface;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;


/**
 * Dient dem Erzeugen bzw. Schreiben der Agenten-Textdatei zur Schnittstellenkommunikation.
 */
public class AgentfileWriter {

	private String sharedFolderPath;
	private char player;



	/**
	 * Instanziiert einen neuen AgentfileWriter.
	 *
	 * @param sharedFolderPath {@link #getSharedFolderPath()}
	 * @param player {@link #setPlayer(char)}
	 */
	public AgentfileWriter(String sharedFolderPath, char player){
		this.sharedFolderPath = sharedFolderPath;		
		this.player = player;
	}	
	


	/**
	 * Gibt den Pfad des Datenaustauschverzeichnisses zurück.
	 *
	 * @return einen String als Pfad zum Austauschverzeichnis
	 */
	public String getSharedFolderPath() {
		return sharedFolderPath;
	}

	/**
	 * Setzt den Pfad zum Datenaustauschverzeichnis.
	 *
	 * @param sharedFolderPath Pfad des Datenaustauschverzeichnisses
	 */
	public void setSharedFolderPath(String sharedFolderPath) {
		this.sharedFolderPath = sharedFolderPath;
	}


	/**
	 * Liefert den Spielernamen zurück.
	 *
	 * @return einen character mit dem Spielernamen (x oder o)
	 */
	public char getPlayer() {
		return player;
	}


	/**
	 * Setzt den Spielernamen (x oder o).
	 *
	 * @param player die Spielerbezeichnung
	 */
	public void setPlayer(char player) {
		this.player = player;
	}
	


	/**
	 * Schreibt bzw. Erzeugt die Agent-Textdatei.
	 *
	 * @param agentFile {@link Agentfile}
	 * @throws IOException Signalisiert, dass eine I/O Exception aufgetreten ist.
	 */
	public void writeAgentfile(Agentfile agentFile) throws IOException {					
			String filePath = sharedFolderPath + "/spieler" + player + "2server.txt";
			
			final Charset charset=Charset.forName("US-ASCII");
			
			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), charset));
			writer.write(String.valueOf(agentFile.getMove()));
			writer.close();		
	}
}
