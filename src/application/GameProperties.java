package application;

import java.util.Properties;


/**
 * Klasse, die die Konfigurationsparameter für den Spieleagenten enthält.
 */
public class GameProperties extends Properties{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Instanziiert ein neues GameProperties-Objekt.
	 */
	public GameProperties(){
		super();
	}
	
	/** Der Dateiname der Konfigurationsdatei als Konstante. */
	public static final String DATEINAME = "client.ini";
	
	/** Der Schnittstellen Parameter.
	 * Als Schnittstellen kann entweder mittels Austausch von Text-Dateien oder über einen Pusher-Server mit dem 4g-Server kommuniziert werden. */
	public static final String INTERFACE = "interface";
	
	/** Die Zugzeit bis ein Spielstein gesetzt werden muss als Konstante. */
	public static final String ZUGZEIT = "zugzeit";
	
	/** Die Spieler-Konstante */
	public static final String SPIELER = "player";

	/** Der Dateipfad zum Austauschverzeichnis als Konstante. */
	public static final String DATEIPFAD = "kontaktpfad";

	/** Die APP_ID für die Pusher-Schnittstelle als Konstante. */
	public static final String APP_ID = "appId";
	
	/** Der APP_KEY für die Pusher-Schnittstelle als Konstante. */
	public static final String APP_KEY = "appKey";
	
	/** Das APP_SECRET für die Pusher-Schnittstelle als Konstante. */
	public static final String APP_SECRET = "appSecret";
}
