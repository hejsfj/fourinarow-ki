package pusherInterface;

/**
 * Diese Klasse repr�sentiert das vom Server erhaltene Pusherevent.
 */
public class PusherEvent {
	private Boolean freigabe;
	private int gegnerzug;
	private String satzstatus;
	private String sieger;
	
	/**
	 * Gibt den Freigabe-Status zur�ck.
	 * 
	 * @return ein boolean Wert mit dem Freigabestatus
	 */
	public Boolean getFreigabe() {
		return freigabe;
	}

	/**
	 * Setzt die Freigabe.
	 * 
	 * @param freigabe die Freigabe
	 */
	public void setFreigabe(Boolean freigabe) {
		this.freigabe = freigabe;
	}

	/**
	 * Gibt den Gegnerzug zur�ck.
	 * 
	 * @return ein int-Wert mit dem Gegnerzug
	 */
	public int getGegnerzug() {
		return gegnerzug;
	}

	/**
	 * Setzt den Gegnerzug.
	 * 
	 * @param gegnerzug der Gegnerzug
	 */
	public void setGegnerzug(int gegnerzug) {
		this.gegnerzug = gegnerzug;
	}

	/**
	 * Gibt den Satzstatus zur�ck.
	 * 
	 * @return ein String-Wert mit dem Satzstatus
	 */
	public String getSatzstatus() {
		return satzstatus;
	}

	/**
	 * Setzt den Satzstatus.
	 * 
	 * @param satzstatus der Satzstatus
	 */
	public void setSatzstatus(String satzstatus) {
		this.satzstatus = satzstatus;
	}

	/**
	 * Gibt den Sieger zur�ck.
	 * 
	 * @return ein String-Wert mit dem Sieger
	 */
	public String getSieger() {
		return sieger;
	}

	/**
	 * Setzt den Sieger.
	 * 
	 * @param sieger der Sieger
	 */
	public void setSieger(String sieger) {
		this.sieger = sieger;
	}
	
	/**
	 * �berpr�ft, ob das Spiel zu Ende ist.
	 *
	 * @return true, wenn das Spiel zu Ende ist.
	 */
	public boolean isGameOver(){
		return getFreigabe() == false && getSatzstatus().equals("beendet");
	}
}
