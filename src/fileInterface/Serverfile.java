package fileInterface;

// TODO: Auto-generated Javadoc
/**
 * The Class Serverfile.
 */
public class Serverfile {
	private Boolean freigabe;
	private int gegnerzug;
	private String satzstatus;
	private String sieger;
	
	/**
	 *
	 * @return ein boolean Wert mit dem Freigabestatus
	 */
	public Boolean getFreigabe() {
		return freigabe;
	}

	/**
	 *
	 * @param freigabe der Freigabestatus
	 */
	public void setFreigabe(Boolean freigabe) {
		this.freigabe = freigabe;
	}

	/**
	 * @return den Gegnerzug als Integer.
	 * Die Spalte in die der Gegner seinen Spielstein gesetzt hat.
	 */
	public int getGegnerzug() {
		return gegnerzug;
	}

	/**
	 * @param gegnerzug die Spalte in die der Gegner wirft.
	 */
	public void setGegnerzug(int gegnerzug) {
		this.gegnerzug = gegnerzug;
	}

	/**
	 * @return der satzstatus
	 */
	public String getSatzstatus() {
		return satzstatus;
	}

	/**
	 * @param satzstatus der Satzstatus
	 */
	public void setSatzstatus(String satzstatus) {
		this.satzstatus = satzstatus;
	}

	/**
	 * @return der Sieger
	 */
	public String getSieger() {
		return sieger;
	}

	/**
	 * @param sieger der Sieger
	 *            
	 */
	public void setSieger(String sieger) {
		this.sieger = sieger;
	}
	
	/**
	 * Überprüft ob das Spiel zu ende ist
	 *
	 * @return true, wenn das Spiel zu ende ist.
	 */
	public boolean isGameOver(){
		return getFreigabe() == false && getSatzstatus().equals("beendet");
	}
}
