package Network;

import java.net.SocketAddress;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Periodically adds GDAY messages to its message queue
 * @author pbrook
 *
 */
public class AnnounceSender extends Thread implements ReadableQueue<Message> {
	private ConcurrentLinkedQueue<Message> messages;
	private SocketAddress multicastAddress;
	private boolean running;
	private String username;
	
	public AnnounceSender(SocketAddress multicastAddress, String username) {
		this.multicastAddress = multicastAddress;
		this.messages = new ConcurrentLinkedQueue<Message>();
		this.username = username;
		this.running = true;
	}
	
	@Override
	public void run() {
		while (running) {
			Message m = new Message("GDAY "+username, multicastAddress);
			messages.add(m);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.err.println("Announce thread interrupted, resuming");
			}
		}
	}

	@Override
	public boolean hasNext() {
		return !messages.isEmpty();
	}

	@Override
	public Message next() throws NoSuchElementException {
		return messages.remove();
	}
	
	public void stopSending() {
		running = false;
	}

}
