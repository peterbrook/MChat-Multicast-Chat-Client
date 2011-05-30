package Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NetworkSender extends Thread implements WriteableQueue<Message> {
	private MulticastSocket socket;
	private ConcurrentLinkedQueue<Message> messages;

	public NetworkSender(MulticastSocket s) {
		socket = s;
		messages = new ConcurrentLinkedQueue<Message>();
	}

	public void run() {
		for(;;) {
			try {
				sleep(10);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (!messages.isEmpty()) {
				byte[] buf = new byte[1000];
			}
		}
	}

	@Override
	public void add(Message item) {
		messages.add(item);
	}
}
