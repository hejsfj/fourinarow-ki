package fileInterface;

public class Serverfile {
	private Boolean freigabe;
	private int gegnerzug;
	private String satzstatus;
	private String sieger;
	
	public Boolean getFreigabe() {
		return freigabe;
	}

	public void setFreigabe(Boolean freigabe) {
		this.freigabe = freigabe;
	}

	public int getGegnerzug() {
		return gegnerzug;
	}

	public void setGegnerzug(int gegnerzug) {
		this.gegnerzug = gegnerzug;
	}

	public String getSatzstatus() {
		return satzstatus;
	}

	public void setSatzstatus(String satzstatus) {
		this.satzstatus = satzstatus;
	}

	public String getSieger() {
		return sieger;
	}

	public void setSieger(String sieger) {
		this.sieger = sieger;
	}
	
	public boolean isGameOver(){
		return getFreigabe() == false && getSatzstatus().equals("beendet");
	}
}
