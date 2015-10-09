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

public class PusherConnector implements ConnectionEventListener {
	private final Pusher pusher;
	private final String appKey;
	private final String appSecret;
	private final long startTime = System.currentTimeMillis();
	private PrivateChannel channel;
	
	public PusherConnector(String pusherAppId, String pusherAppKey, String pusherAppSecret) {
		appKey = pusherAppKey;
		appSecret = pusherAppSecret;

		PusherOptions options = new PusherOptions();

		options.setAuthorizer(new Authorizer() {
			/**
			 * The user (the client) can be authorized, if he knows the key and
			 * the secret. And the user is to be authorized on a decided
			 * channel.
			 * 
			 * "Given that your application - d. h. dieser Nutzer hier -
			 * receives a POST request to /pusher/auth with the parameters:
			 * channel_name=private-foobar&socket_id=1234.1234
			 * 
			 * The auth response should be a JSON string with a an auth property
			 * with a value composed of the application key and the
			 * authentication signature separated by a colon ‘:’ as follows:
			 */
			public String authorize(String channelName, String socketId) throws AuthorizationFailureException {
				System.out.println("The clients channelName: " + channelName);
				System.out.println("The clients socketId: " + socketId);

				String message = socketId + ":" + channelName;
				String hash = HmacSHA256.getHmacSHA256HexDigest(appSecret, message);

				// <signature> ::= "{"auth":"<appId>:<hash>"}"
				String signature = "{\"auth\":\"" + appKey + ":" + hash + "\"}";

				return signature;
			}
		});

		pusher = new Pusher(appKey, options);
		pusher.connect(this);
	}

	public void triggerClientMove(PrivateChannel channel, int move){
		channel.trigger("client-event", "{\"move\": \"" + move + "\"}");	
		System.out.println("Client move: " + move + " losgeschickt!!");	
	}

	public PrivateChannel subscribeToPrivateChannel(String channelName, PrivateChannelEventListener listener, String eventName){
		channel =  pusher.subscribePrivate(channelName, listener, eventName);		
		return channel;
	}
	
	/* ConnectionEventListener implementation */
	@Override
	public void onConnectionStateChange(final ConnectionStateChange change) {

		System.out.println(String.format("[%d] Connection state changed from [%s] to [%s]", timestamp(),
				change.getPreviousState(), change.getCurrentState()));
	}

	@Override
	public void onError(final String message, final String code, final Exception e) {

		System.out.println(String.format("[%d] An error was received with message [%s], code [%s], exception [%s]",
				timestamp(), message, code, e));
	}

	private long timestamp() {
		return System.currentTimeMillis() - startTime;
	}
}
