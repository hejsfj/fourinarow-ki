package pusherInterface;

import com.pusher.client.channel.PrivateChannelEventListener;

public class PusherEventHandler implements PrivateChannelEventListener {
	private final long startTime = System.currentTimeMillis();
	private PusherEvent recentPusherEvent;
	public boolean hasEvent = false;

	@Override
	public void onEvent(final String channelName, final String eventName, final String data) {

		System.out.println(String.format("[%d] Received event [%s] on channel [%s] with data [%s]", timestamp(),
				eventName, channelName, data));
		
		hasEvent = true;
		String[] parts = data.split("\":\"");
		// String messageKey = parts[0]; // 004
		String messageValues = parts[1]; // 034556
		
		String[] values = messageValues.split(" # ");			
		String correctedSieger = values[3].replace("\"}", "");
		System.out.println("corrected sieger: " + correctedSieger);
		
		recentPusherEvent = new PusherEvent();
		System.out.println("processing new event");
		recentPusherEvent.setFreigabe(Boolean.parseBoolean(values[0]));
		recentPusherEvent.setSatzstatus(values[1]);
		recentPusherEvent.setGegnerzug(Integer.parseInt(values[2]));
		recentPusherEvent.setSieger(correctedSieger);
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
	
	public PusherEvent getPusherEvent(){
		return recentPusherEvent;
	}
	public void clearPusherEvent(){
		recentPusherEvent = null;
		System.out.println("pusherEvent cleared");
	}
}
