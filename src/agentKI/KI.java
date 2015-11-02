package agentKI;

import java.awt.*;

import gamefield.Gamefield;

public class KI {
	//Starter
	private char hatAngefangen;

	//Gewinner
	private char hatGewonnen;

	//Spielerwahl
	private char eigenerStein;
	private char gegnerStein;

	//Gibt positionen der steine wieder
	private Point eigenerPunkt = new Point(-1,-1);
	private Point gegnerPunkt = new Point(-1,-1);

	//Spielfeld anlegen
	private char[][] gamefield = new char[7][6];

	//Geplanten Spielzug hinterlegen
	private int gespeicherterZug;

	//Tiefe fuer rekursion
	private int gewuenschteTiefe;

	//Array fuer moegliche Zuege
	//Index fuer Spalte, Wert fuer Zeile, -1 falls Zeile voll ist
	private int[] moeglicheZuege = new int[7];

	private Gamefield ourGamefield;
	
	//Konstruktor fuer KI, legt Spielsteine fest, inititalisiert den spielfeldarray
	public KI(Gamefield aGamefield, char player) {
		eigenerStein = player;
		this.ourGamefield = aGamefield;
		
		setOpponentPlayer(player);		

		hatAngefangen = gegnerStein;

		for(int i=0; i<7; i++) { //initialisiert spielfeld
			for(int j=0; j<6; j++){				
				gamefield[i][j] = ' ';
			}
			moeglicheZuege[i] = -1; //initialisiert die moeglichen zuege
		}

	}
	private void setOpponentPlayer(char player){
		switch (player) {
		case 'o':
			gegnerStein = 'x';
			break;
		case'x':
			gegnerStein = 'o';
			break;
		default:
			System.out.println("Ungueltige Spielerwahl.");
			break;
		}
	}
	
	//Nimmt eine Spalte zwischen 0 und 6 entgegen
	//Gibt einen integer mit dem eigenen Spielzug zurueck
	public int calculateMove(int opponentPlayerMove){

		if(opponentPlayerMove == -1){
			hatAngefangen = eigenerStein;
			addMoveToGamefield(eigenerStein, 3);
			return 3;
		} else {
			addMoveToGamefield(gegnerStein, opponentPlayerMove);
		}


		keineZuegeMehr();

		int spalte = (int)(Math.random()*6);

		for(int i=0; i < moeglicheZuege.length; i++){
			spalte = (i+spalte)%7;
			if(mySiegMuster(eigenerStein, spalte, moeglicheZuege[spalte]) && moeglicheZuege[spalte] < 6){
				addMoveToGamefield(eigenerStein, spalte);
				return spalte;
			}
		}

		for(int i=0; i < moeglicheZuege.length; i++){
			spalte = (i+spalte)%7;
			if(mySiegMuster(gegnerStein, spalte, moeglicheZuege[spalte]) && moeglicheZuege[spalte] < 6){
				addMoveToGamefield(eigenerStein, spalte);
				return spalte;
			}
		}

		for(int i = 0; i < moeglicheZuege.length; i++){
			spalte = (i+spalte)%7;
			if(moeglicheZuege[spalte] < 6){
				addMoveToGamefield(eigenerStein, spalte);
				return spalte;
			}
		}

		return -1; //Kein Spielzug mehr moeglich
	}

	//Nimmt eine Spalte zwischen 0 und 6 entgegen
	//Gibt einen integer mit dem eigenen Spielzug zurueck
	public int gutenZugBerechnen(int opponentMove) {

		if (opponentMove == -1){
			hatAngefangen = eigenerStein;
			addMoveToGamefield(eigenerStein, 3);
			return 3;
		} else {
			addMoveToGamefield(gegnerStein, opponentMove);
		}

		for(int i=0; i < moeglicheZuege.length; i++){
			if(siegMuster(i, moeglicheZuege[i]) && moeglicheZuege[i] < 7){
				addMoveToGamefield(eigenerStein, i);
				return i;
			}
		}
		hauptProgramm(4);
		return gespeicherterZug;

	}

	//MinMax Wikipedia Pseudocode
	private void hauptProgramm(int gewuenschteTiefe){
		gespeicherterZug = -1;
		this.gewuenschteTiefe = gewuenschteTiefe;

		max(eigenerStein, gewuenschteTiefe);
		if (gespeicherterZug == -1) {
			//Es gab keine weiteren Zuege mehr
			System.out.println("Das Spiel ist beendet.");
		} else {
			System.out.println("Der Stein wird in Spalte " + gespeicherterZug + " geworfen.");
			addMoveToGamefield(eigenerStein, gespeicherterZug);
		}
	}

	private int max(char player, int tiefe){

		if(tiefe == 0 || keineZuegeMehr() || getWinner() != ' '){ 
			return bewerten(player);
		}
		int maxWert = -100;

		int startSpalte = (int)(Math.random()*6);

		//generiereMoeglicheZuege();
		for(int i=0; i<7; i++){
			int spalte = (i+startSpalte)%6;
			if(moeglicheZuege[spalte] < 6){
				int zeile = moeglicheZuege[spalte];
				fuehreNaechstenZugAus(spalte, zeile, player);
				int wert = min(gegnerStein, tiefe-1); //wert hat einen wert von 1 bis 4. 4 ist ideal
				macheZugRueckgaengig(spalte, zeile, player);

				if(wert > maxWert){
					maxWert = wert;
					if(tiefe == gewuenschteTiefe){
						gespeicherterZug = spalte;
					}
				}	
			}
		}

		return maxWert;
	}

	private int min(char player, int tiefe){

		if(tiefe == 0 || keineZuegeMehr() || getWinner()!= ' '){
			return bewerten(player);
		}
		int minWert = +100;

		//generiereMoeglicheZuege();
		for(int i=0; i<7; i++){
			if(moeglicheZuege[i] < 6){
				int spalte = i;
				int zeile = moeglicheZuege[i];
				fuehreNaechstenZugAus(spalte, zeile, player);
				int wert = max(eigenerStein, tiefe-1); //wert hat einen wert von 1 bis 4. 4 ist ideal
				macheZugRueckgaengig(spalte, zeile, player);
				if(wert < minWert){
					minWert = wert;
				}
			}
		}
		return minWert;
	}

	//bewerte die aktuelle Situation des spielers
	//3 entspricht 3 in einer reihe
	//2 entspricht 2 in einer reihe
	private int bewerten(char player){
		char gegner;

		if(player == 'o'){
			gegner = 'x';
		}else{
			gegner = 'o';
		}
		return 0;
	}

	//prueft ob noch ein spielzug moeglich ist
	private boolean keineZuegeMehr(){
		boolean keinZugMoeglich = true;

		for(int i=0; i<7; i++){
			for(int j=0; j<6; j++){
				if(gamefield[i][j] == ' '){
					moeglicheZuege[i] = j;
					keinZugMoeglich = false;
					break;
				}
				if(gamefield[i][j] != ' '){
					moeglicheZuege[i] = 7;
				}
			}
		}

		return keinZugMoeglich;
	}

	//Setzt den uebergebenen Stein ins Spielfeld
	private void fuehreNaechstenZugAus(int spalte, int zeile, char player){

		if(gamefield[spalte][zeile] == ' '){
			gamefield[spalte][zeile] = player;
		}

	}

	//Nimmt den uebergebenen Stein aus dem Spielfeld
	private void macheZugRueckgaengig(int spalte, int zeile, char player){

		if(gamefield[spalte][zeile] == player){
			gamefield[spalte][zeile] = ' ';
		}

	}

	
	private void addMoveToGamefield(char player, int move) {
		for(int i=0; i<6; i++) {
			if(gamefield[move][i] == ' '){
				gamefield[move][i] = player;
				if (player == 'o') {
					eigenerPunkt.setLocation(move, i);
				} else if (player == 'x') {
					gegnerPunkt.setLocation(move, i);
				}
				break;
			}
		}
	}
	//Gibt an ob und wer gewonnen hat
	//null falls noch niemand gewonnen hat
	private char getWinner(){
		switch (hatGewonnen) {
		case 'o':
			return 'o';
		case 'x':
			return 'x';
		default:
			return ' ';
		}
	}

	private boolean gegnerSiegMuster(int spalte, int zeile){
		//Pruefe waagerecht
		//Pruefe links -3
		try{
			if(gamefield[spalte-3][zeile] == gegnerStein && gamefield[spalte-2][zeile] == gegnerStein && gamefield[spalte-1][zeile] == gegnerStein){
				return true;
			}

			//Pruefe links -2
			if(gamefield[spalte-2][zeile] == gegnerStein && gamefield[spalte-1][zeile] == gegnerStein && gamefield[spalte+1][zeile] == gegnerStein){
				return true;
			}
			//Pruefe links -1
			if(gamefield[spalte-1][zeile] == gegnerStein && gamefield[spalte+1][zeile] ==gegnerStein && gamefield[spalte+2][zeile] == gegnerStein){
				return true;
			}
			//Pruefe rechts +3
			if(gamefield[spalte+1][zeile] == gegnerStein && gamefield[spalte+2][zeile] == gegnerStein && gamefield[spalte+3][zeile] == gegnerStein){
				return true;
			}
			//Pruefe senkrecht
			if(gamefield[spalte][zeile-3] == gegnerStein && gamefield[spalte][zeile-2] == gegnerStein && gamefield[spalte][zeile-1] == gegnerStein){
				return true;
			}
			//Pruefe waagerecht
			//Pruefe diagonal links -3
			if(gamefield[spalte-3][zeile-3] == gegnerStein && gamefield[spalte-2][zeile-2] ==gegnerStein && gamefield[spalte-1][zeile-1] == gegnerStein){
				return true;
			}
			//Pruefe diagonal links -2
			if(gamefield[spalte-2][zeile-2] == gegnerStein && gamefield[spalte-1][zeile-1] == gegnerStein && gamefield[spalte+1][zeile+1] == gegnerStein){
				return true;
			}
			//Pruefe diagonal links -1
			if(gamefield[spalte-1][zeile-1] == gegnerStein && gamefield[spalte+1][zeile+1] == gegnerStein && gamefield[spalte+2][zeile+2] == gegnerStein){
				return true;
			}
			//Pruefe diagonal rechts +3
			if(gamefield[spalte+1][zeile+1] == gegnerStein && gamefield[spalte+2][zeile+2] == gegnerStein && gamefield[spalte+3][zeile+3] == gegnerStein){
				return true;
			}
			//Pruefe diagonal rechts +3 runter
			if(gamefield[spalte+3][zeile-3] == gegnerStein && gamefield[spalte+2][zeile-2] == gegnerStein && gamefield[spalte+3][zeile-3] == gegnerStein){
				return true;
			}
			//Pruefe diagonal rechts +2 runter
			if(gamefield[spalte+2][zeile-2] == gegnerStein && gamefield[spalte+1][zeile-1] == gegnerStein && gamefield[spalte-1][zeile+1] == gegnerStein){
				return true;
			}
			//Pruefe diagonal rechts +1 runter
			if(gamefield[spalte+1][zeile-1] == gegnerStein && gamefield[spalte-1][zeile+1] == gegnerStein && gamefield[spalte-2][zeile+2] == gegnerStein){
				return true;
			}
			//Pruefe diagonal links hoch
			if(gamefield[spalte-1][zeile+1] == gegnerStein && gamefield[spalte-2][zeile+2] == gegnerStein && gamefield[spalte-3][zeile+3] == gegnerStein){
				return true;
			}
		} catch(ArrayIndexOutOfBoundsException e) { 
			e.printStackTrace();
		}

		return false;
	}

	private boolean mySiegMuster(char player, int spalte, int zeile){
		try{
			if(gamefield[spalte-3][zeile] == player && gamefield[spalte-2][zeile] == player && gamefield[spalte-1][zeile] == player){
				return true;
			}
			//Pruefe links -2
			if(gamefield[spalte-2][zeile] == player && gamefield[spalte-1][zeile] == player && gamefield[spalte+1][zeile] == player){
				return true;
			}
			//Pruefe links -1
			if(gamefield[spalte-1][zeile] == player && gamefield[spalte+1][zeile] == player && gamefield[spalte+2][zeile] == player){
				return true;
			}
			//Pruefe rechts +3
			if(gamefield[spalte+1][zeile] == player && gamefield[spalte+2][zeile] == player && gamefield[spalte+3][zeile] == player){
				return true;
			}
			//Pruefe senkrecht
			if(gamefield[spalte][zeile-3] == player && gamefield[spalte][zeile-2] == player && gamefield[spalte][zeile-1] == player){
				return true;
			}
			//Pruefe waagerecht
			//Pruefe diagonal links -3
			if(gamefield[spalte-3][zeile-3] == player && gamefield[spalte-2][zeile-2] == player && gamefield[spalte-1][zeile-1] == player){
				return true;
			}
			//Pruefe diagonal links -2
			if(gamefield[spalte-2][zeile-2] == player && gamefield[spalte-1][zeile-1] == player && gamefield[spalte+1][zeile+1] == player){
				return true;
			}
			//Pruefe diagonal links -1
			if(gamefield[spalte-1][zeile-1] == player && gamefield[spalte+1][zeile+1] == player && gamefield[spalte+2][zeile+2] == player){
				return true;
			}
			//Pruefe diagonal rechts +3
			if(gamefield[spalte+1][zeile+1] == player && gamefield[spalte+2][zeile+2] == player && gamefield[spalte+3][zeile+3] == player){
				return true;
			}
			//Pruefe diagonal rechts +3 runter
			if(gamefield[spalte+3][zeile-3] == player && gamefield[spalte+2][zeile-2] == player && gamefield[spalte+3][zeile-3] == player){
				return true;
			}
			//Pruefe diagonal rechts +2 runter
			if(gamefield[spalte+2][zeile-2] == player && gamefield[spalte+1][zeile-1] == player && gamefield[spalte-1][zeile+1] == player){
				return true;
			}
			//Pruefe diagonal rechts +1 runter
			if(gamefield[spalte+1][zeile-1] == player && gamefield[spalte-1][zeile+1] == player && gamefield[spalte-2][zeile+2] == player){
				return true;
			}
			//Pruefe diagonal links hoch
			if(gamefield[spalte-1][zeile+1] == player && gamefield[spalte-2][zeile+2] == player && gamefield[spalte-3][zeile+3] == player){
				return true;
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	
	private boolean siegMuster(int spalte, int zeile){
		//Pruefe waagerecht
		//Pruefe links -3
		try{
			if(gamefield[spalte-3][zeile] == eigenerStein && gamefield[spalte-2][zeile] == eigenerStein && gamefield[spalte-1][zeile] == eigenerStein){
				return true;
			}
			//Pruefe links -2
			if(gamefield[spalte-2][zeile] == eigenerStein && gamefield[spalte-1][zeile] == eigenerStein && gamefield[spalte+1][zeile] == eigenerStein){
				return true;
			}
			//Pruefe links -1
			if(gamefield[spalte-1][zeile] == eigenerStein && gamefield[spalte+1][zeile] == eigenerStein && gamefield[spalte+2][zeile] == eigenerStein){
				return true;
			}
			//Pruefe rechts +3
			if(gamefield[spalte+1][zeile] == eigenerStein && gamefield[spalte+2][zeile] == eigenerStein && gamefield[spalte+3][zeile] == eigenerStein){
				return true;
			}
			//Pruefe senkrecht
			if(gamefield[spalte][zeile-3] == eigenerStein && gamefield[spalte][zeile-2] == eigenerStein && gamefield[spalte][zeile-1] == eigenerStein){
				return true;
			}
			//Pruefe waagerecht
			//Pruefe diagonal links -3
			if(gamefield[spalte-3][zeile-3] == eigenerStein && gamefield[spalte-2][zeile-2] == eigenerStein && gamefield[spalte-1][zeile-1] == eigenerStein){
				return true;
			}
			//Pruefe diagonal links -2
			if(gamefield[spalte-2][zeile-2] == eigenerStein && gamefield[spalte-1][zeile-1] == eigenerStein && gamefield[spalte+1][zeile+1] == eigenerStein){
				return true;
			}
			//Pruefe diagonal links -1
			if(gamefield[spalte-1][zeile-1] == eigenerStein && gamefield[spalte+1][zeile+1] == eigenerStein && gamefield[spalte+2][zeile+2] == eigenerStein){
				return true;
			}
			//Pruefe diagonal rechts +3
			if(gamefield[spalte+1][zeile+1] == eigenerStein && gamefield[spalte+2][zeile+2] == eigenerStein && gamefield[spalte+3][zeile+3] == eigenerStein){
				return true;
			}
			//Pruefe diagonal rechts +3 runter
			if(gamefield[spalte+3][zeile-3] == eigenerStein && gamefield[spalte+2][zeile-2] == eigenerStein && gamefield[spalte+3][zeile-3] == eigenerStein){
				return true;
			}
			//Pruefe diagonal rechts +2 runter
			if(gamefield[spalte+2][zeile-2] == eigenerStein && gamefield[spalte+1][zeile-1] == eigenerStein && gamefield[spalte-1][zeile+1] == eigenerStein){
				return true;
			}
			//Pruefe diagonal rechts +1 runter
			if(gamefield[spalte+1][zeile-1] == eigenerStein && gamefield[spalte-1][zeile+1] == eigenerStein && gamefield[spalte-2][zeile+2] == eigenerStein){
				return true;
			}
			//Pruefe diagonal links hoch
			if(gamefield[spalte-1][zeile+1] == eigenerStein && gamefield[spalte-2][zeile+2] == eigenerStein && gamefield[spalte-3][zeile+3] == eigenerStein){
				return true;
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}

		return false;
	}

}