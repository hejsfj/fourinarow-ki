//package agentKI;
//
//import fileInterface.AgentfileWriter;
//import fileInterface.Serverfile;
//import fileInterface.ServerfileReader;
//import fileInterface.Agentfile;
//import gamefield.Gamefield;
//import pusherInterface.PusherConnector;
//import com.pusher.client.channel.PrivateChannelEventListener;
//
//public class spieler_x {
//	static AgentfileWriter agentfileWriter;
//	static ServerfileReader serverfileReader;
//	static PusherConnector pusher;
//	
//	public static void main(String[] args) {
//		final String sharedFolderPath = "C:/temp/abc";
//		
//		Gamefield gamefield = new Gamefield();
//		char player = 'x';
//		String usedInterface = "file";
//		AgentKI agent = new AgentKI();
//		
//		
//		agentfileWriter = new AgentfileWriter(sharedFolderPath, player);
//		serverfileReader = new ServerfileReader(sharedFolderPath, player);
//		
//		 
//		
//		// Fall wir beginnen zu spielen
//		
//
//		while (true) {
//			try {
//				
//				if (usedInterface.equals("file")){
//					Serverfile serverfile = serverfileReader.readServerfile();
//					if (serverfile.getGegnerzug() != -1){
//						gamefield.insertCoin(serverfile.getGegnerzug(), 'o');
//					}
//					if (!serverfile.isGameOver()){
//						int move = agent.calculateMove(gamefield);
//						gamefield.insertCoin(move, player);
//						agentfileWriter.writeAgentfile(new Agentfile(move));
//					} else {
//						System.out.println("Spiel vorbei. Der Gewinner ist: " + serverfile.getSieger());
//						break;
//					}
//					
//				}
//				/*
//				if (usedInterface.equals("pusher")){
//					
//					pusher = PusherConnector.getInstance();
//					pusher.subscribeToPrivateChannel(new PrivateChannelEventListener() {
//			            @Override
//			            public void onSubscriptionSucceeded(String channelName) {
//			                System.out.println("Subscribed!");
//			            }
//
//			            @Override
//			            public void onEvent(String channelName, String eventName, String data) {
//			                System.out.println("event received!!");
//			            }
//
//						@Override
//						public void onAuthenticationFailure(String arg0, Exception arg1) {
//							System.out.println(arg0);
//							System.out.println(arg1);
//							System.out.println("authentication failure!!");				
//						}
//			        });
//				}*/
//				
//			} catch (final Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//}
