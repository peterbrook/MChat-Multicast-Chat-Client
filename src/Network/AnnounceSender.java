package Network;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Periodically adds GDAY messages to its message queue
 * @author pbrook
 *
 */
public class AnnounceSender extends Thread implements ReadableQueue<Message> {
	private static final boolean MULTI_PORT = false;
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
			if (MULTI_PORT) {
				InetSocketAddress mcA = (InetSocketAddress) multicastAddress;
				int[] ports = {5000, 5001, 5002, 5003, 5004, 5005};
				for (int i=0; i < ports.length; i++) {
					InetSocketAddress a = new InetSocketAddress(mcA.getAddress(), ports[i]);
					Message m = new Message("GDAY "+username, a);
					messages.add(m);
				}
			} else {
				Message m = new Message("GDAY "+username, multicastAddress);
				messages.add(m);
			}
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
