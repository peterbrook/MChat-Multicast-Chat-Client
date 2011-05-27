package Network;

import java.net.MulticastSocket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NetworkSender extends Thread {
	private MulticastSocket socket;
	private ConcurrentLinkedQueue<Message> messages;
	
	public NetworkSender(MulticastSocket s) {
		socket = s;
	}
	
	
}
