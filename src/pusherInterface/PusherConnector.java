package pusherInterface;

import com.pusher.client.AuthorizationFailureException;
import com.pusher.client.Authorizer;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.PrivateChannel;
import com.pusher.client.channel.PrivateChannelEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionStateChange;
import utils.HmacSHA256;

/**
 * Die Hauptklasse f�r die Kommunikation mit dem Server.
 */
public class PusherConnector implements ConnectionEventListener {

	private final Pusher pusher;
	private final String appKey;
	private final String appSecret;
	private final long startTime = System.currentTimeMillis();
	private PrivateChannel channel;
	
	/**
	 * Instanziierung des PusherConnectors.
	 *
	 * @param pusherAppId Die AppID f�r die Pusher Kommunikation
	 * @param pusherAppKey Der AppKey f�r die Pusher Kommunikation
	 * @param pusherAppSecret Der AppSecret f�r die Pusher Kommunikation
	 */
	public PusherConnector(String pusherAppId, String pusherAppKey, String pusherAppSecret) {
		appKey = pusherAppKey;
		appSecret = pusherAppSecret;

		PusherOptions options = new PusherOptions();

		options.setAuthorizer(new Authorizer() {
			/**
			 * Der Client kann auf einem ausgew�hlten Channel authorisiert werden, 
			 * wenn er den zugeh�rigen AppKey und AppSecret kennt.
			 */
			public String authorize(String channelName, String socketId) throws AuthorizationFailureException {
				System.out.println("The clients channelName: " + channelName);
				System.out.println("The clients socketId: " + socketId);

				String message = socketId + ":" + channelName;
				String hash = HmacSHA256.getHmacSHA256HexDigest(appSecret, message);

				String signature = "{\"auth\":\"" + appKey + ":" + hash + "\"}";

				return signature;
			}
		});

		pusher = new Pusher(appKey, options);
		pusher.connect(this);
	}

	/**
	 * Schickt den Spielzug des Clients an den Server.
	 *
	 * @param channel Channel Element
	 * @param move Spalte, in die der Spielstein gesetzt wird
	 */
	public void triggerClientMove(PrivateChannel channel, int move){
		channel.trigger("client-event", "{\"move\": \"" + move + "\"}");	
		System.out.println("Client move: " + move + " losgeschickt!!");	
	}

	/**
	 * Anmeldung an einem PrivateChannel
	 *
	 * @param channelName Name des Channels
	 * @param listener Der Listener
	 * @param eventName Der Name des Events
	 * @return Das PrivateChannel Element
	 */
	public PrivateChannel subscribeToPrivateChannel(String channelName, PrivateChannelEventListener listener, String eventName){
		channel =  pusher.subscribePrivate(channelName, listener, eventName);		
		return channel;
	}
	
	/* (non-Javadoc)
	 * @see com.pusher.client.connection.ConnectionEventListener#onConnectionStateChange(com.pusher.client.connection.ConnectionStateChange)
	 */
	/* ConnectionEventListener implementation */
	@Override
	public void onConnectionStateChange(final ConnectionStateChange change) {

		System.out.println(String.format("[%d] Connection state changed from [%s] to [%s]", timestamp(),
				change.getPreviousState(), change.getCurrentState()));
	}

	/* (non-Javadoc)
	 * @see com.pusher.client.connection.ConnectionEventListener#onError(java.lang.String, java.lang.String, java.lang.Exception)
	 */
	@Override
	public void onError(final String message, final String code, final Exception e) {

		System.out.println(String.format("[%d] An error was received with message [%s], code [%s], exception [%s]",
				timestamp(), message, code, e));
	}

	private long timestamp() {
		return System.currentTimeMillis() - startTime;
	}
}
