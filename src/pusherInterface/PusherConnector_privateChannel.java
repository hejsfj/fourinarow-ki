package pusherInterface;

import java.util.Map;

import com.google.gson.Gson;
import com.pusher.client.AuthorizationFailureException;
import com.pusher.client.Authorizer;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.PrivateChannel;
import com.pusher.client.channel.PrivateChannelEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionStateChange;
import utils.HmacSHA256;

public class PusherConnector_privateChannel implements ConnectionEventListener, PrivateChannelEventListener {

	private final Pusher pusher;
	private final String channelName;
	private final String eventName;
	private final long startTime = System.currentTimeMillis();
	private static PrivateChannel channel;

	public static void main(final String[] args) {
		new PusherConnector_privateChannel();
	}

	public PusherConnector_privateChannel() {
		final String apiKey = "93c4a752a14cbeef7216";
		channelName = "private-channel";
		eventName = "MoveToAgent";

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

				// String to sign ::= <webSocketId>:<channelName>
				String message = socketId + ":" + channelName;

				// calc the hash, e. g. one part of the authentication signature
				String appSecret = "adcd6bab9a922980c892";

				String hash = HmacSHA256.getHmacSHA256HexDigest(appSecret, message);

				// compose the entire authentication signature
				// <signature> ::= "{"auth":"<appId>:<hash>"}"
				String appKey = "93c4a752a14cbeef7216";
				String signature = "{\"auth\":\"" + appKey + ":" + hash + "\"}";

				return signature; // ... wer auch immer das dann auswertet.
			}
		});

		pusher = new Pusher(apiKey, options);
		pusher.connect(this);

		channel = pusher.subscribePrivate(channelName, this, eventName);
		try {
			Thread.sleep(5000);
			//channel.trigger("client-event", "{\"move\": \"" + 1 + "\"}");
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Keep main thread asleep while we watch for events or application will
		// terminate
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
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

	/* ChannelEventListener implementation */

	@Override
	public void onEvent(final String channelName, final String eventName, final String data) {

		System.out.println(String.format("[%d] Received event [%s] on channel [%s] with data [%s]", timestamp(),
				eventName, channelName, data));

		final Gson gson = new Gson();
		@SuppressWarnings("unchecked")
		final Map<String, String> jsonObject = gson.fromJson(data, Map.class);
		System.out.println(jsonObject);
		channel.trigger("client-event", "{\"move\": \"" + 1 + "\"}");
		System.out.println("Client event losgeschickt!!");
	}

	@Override
	public void onSubscriptionSucceeded(final String channelName) {

		System.out.println(String.format("[%d] Subscription to channel [%s] succeeded", timestamp(), channelName));
	}

	private long timestamp() {
		return System.currentTimeMillis() - startTime;
	}

	@Override
	public void onAuthenticationFailure(String arg0, Exception arg1) {
		System.out.println(arg0);
		System.out.println(arg1);
		System.out.println("authentication failure!!");
	}

}
