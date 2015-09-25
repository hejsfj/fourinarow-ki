package application;

import java.util.Properties;

public class GameProperties extends Properties{

	private static final long serialVersionUID = 1L;
	
	public GameProperties(){
		super();
	}
	public static final String DATEINAME = "client.ini";
	
	public static final String INTERFACE = "interface";
	public static final String ZUGZEIT = "zugzeit";
	public static final String SPIELER = "player";

	public static final String DATEIPFAD = "kontaktpfad";

	public static final String APP_ID = "appId";
	public static final String APP_KEY = "appKey";
	public static final String APP_SECRET = "appSecret";
}
