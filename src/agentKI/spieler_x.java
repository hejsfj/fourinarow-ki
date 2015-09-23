package agentKI;

import fileInterface.AgentfileWriter;
import fileInterface.Serverfile;
import fileInterface.ServerfileReader;
import fileInterface.Agentfile;
import gamefield.Gamefield;

public class spieler_x {

	public static void main(String[] args) {
		final String sharedFolderPath = "C:/temp/abc";
		
		Gamefield gamefield = new Gamefield();
		//PusherConnector pusher = new PusherConnector();
		char player = 'x';
		
		AgentKI agent = new AgentKI();
		
		
		AgentfileWriter agentfileWriter = new AgentfileWriter(sharedFolderPath, player);
		ServerfileReader serverfileReader = new ServerfileReader(sharedFolderPath, player);
		
		// Fall wir beginnen zu spielen
		
boolean firstLoop = true;
		while (true) {
			try {
				if (firstLoop){
					System.out.println("5 seconds till game starts");
					Thread.sleep(5000);
					firstLoop = false;
				}
				System.out.println("game started");
				Serverfile serverfile = serverfileReader.readServerfile();
				System.out.println("received serverfile");
				if (serverfile.getGegnerzug() != -1){
					gamefield.insertCoin(serverfile.getGegnerzug(), 'o');
					System.out.println("inserted coin of opponent");
				}
				
				if (serverfile.getFreigabe() == true){
					System.out.println("we have freigabe");
					int move = agent.calculateMove();
					System.out.println("we calculated our move");
					gamefield.insertCoin(move, player);
					System.out.println("we inserted our coin");
					agentfileWriter.writeAgentfile(new Agentfile(move));
					System.out.println("we wrote our agentfile");
				}
				
				
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}
}
