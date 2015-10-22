package pusherInterface;

import com.pusher.client.channel.PrivateChannelEventListener;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Der Dienst für den PusherEventReader.
 */
public class PusherEventReaderService extends Service<PusherEvent> implements PrivateChannelEventListener {
	private final long startTime = System.currentTimeMillis();
	private PusherEvent recentPusherEvent;
	
	/* (non-Javadoc)
	 * @see javafx.concurrent.Service#createTask()
	 */
	@Override
	protected Task<PusherEvent> createTask() {
		 return new Task<PusherEvent>() {
                protected PusherEvent call() {
                     PusherEvent event = getPusherEvent();
                     deletePusherEvent();
                     return event;
            };
		 };
	}	

	/* (non-Javadoc)
	 * @see com.pusher.client.channel.SubscriptionEventListener#onEvent(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void onEvent(final String channelName, final String eventName, final String data) {

		System.out.println(String.format("[%d] Received event [%s] on channel [%s] with data [%s]", timestamp(),
				eventName, channelName, data));
		String[] parts = data.split("\":\"");
		// String messageKey = parts[0]; // 004
		String messageValues = parts[1]; // 034556
		
		String[] values = messageValues.split(" # ");			
		String correctedSieger = values[3].replace("\"}", "");
		
		recentPusherEvent = new PusherEvent();
		recentPusherEvent.setFreigabe(Boolean.parseBoolean(values[0]));
		recentPusherEvent.setSatzstatus(values[1]);
		recentPusherEvent.setGegnerzug(Integer.parseInt(values[2]));
		recentPusherEvent.setSieger(correctedSieger);
	}

	/* (non-Javadoc)
	 * @see com.pusher.client.channel.ChannelEventListener#onSubscriptionSucceeded(java.lang.String)
	 */
	@Override
	public void onSubscriptionSucceeded(final String channelName) {

		System.out.println(String.format("[%d] Subscription to channel [%s] succeeded", timestamp(), channelName));
	}

	/* (non-Javadoc)
	 * @see com.pusher.client.channel.PrivateChannelEventListener#onAuthenticationFailure(java.lang.String, java.lang.Exception)
	 */
	@Override
	public void onAuthenticationFailure(String arg0, Exception arg1) {
		System.out.println(arg0);
		System.out.println(arg1);
		System.out.println("authentication failure!!");
	}
	
	/**
	 *
	 * @return Das PusherEvent Element
	 */
	public PusherEvent getPusherEvent()	{
		while (recentPusherEvent == null){
			try {
				Thread.sleep(300);
				System.out.println("wartet auf PusherEvent");
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		return recentPusherEvent;
	}
	
	/**
	 * Löschen des PusherEvents
	 */
	public void deletePusherEvent(){
		recentPusherEvent = null;
	}

	private long timestamp() {
		return System.currentTimeMillis() - startTime;
	}
}