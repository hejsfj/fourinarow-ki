package pusherInterface;

/**
 * Die PusherEvent Klasse.
 */
public class PusherEvent {
	private Boolean freigabe;
	private int gegnerzug;
	private String satzstatus;
	private String sieger;
	
	/**
	 *
	 * @return Der Boolsche Wert f�r den Freigabestatus
	 */
	public Boolean getFreigabe() {
		return freigabe;
	}

	/**
	 *
	 * @param freigabe Der Freigabestatus
	 */
	public void setFreigabe(Boolean freigabe) {
		this.freigabe = freigabe;
	}

	/**
	 *
	 * @return Der Integer Wert f�r den gegnerischen Spielzug
	 */
	public int getGegnerzug() {
		return gegnerzug;
	}

	/**
	 *
	 * @param gegnerzug Der Integer Wert f�r den gegnerischen Spielzug
	 */
	public void setGegnerzug(int gegnerzug) {
		this.gegnerzug = gegnerzug;
	}

	/**
	 *
	 * @return Der Stringwert f�r den Satzstatus
	 */
	public String getSatzstatus() {
		return satzstatus;
	}

	/**
	 *
	 * @param satzstatus Der Stringwert f�r den Satzstatus
	 */
	public void setSatzstatus(String satzstatus) {
		this.satzstatus = satzstatus;
	}

	/**
	 *
	 * @return Der Stringwert f�r den Sieger
	 */
	public String getSieger() {
		return sieger;
	}

	/**
	 *
	 * @param sieger Der Stringwert f�r den Sieger
	 */
	public void setSieger(String sieger) {
		this.sieger = sieger;
	}
	
	/**
	 * �berpr�fung, ob das Spiel zu Ende ist. 
	 *
	 * @return true, wenn das Spiel zu Ende ist.
	 */
	public boolean isGameOver(){
		return getFreigabe() == false && getSatzstatus().equals("beendet");
	}

}
