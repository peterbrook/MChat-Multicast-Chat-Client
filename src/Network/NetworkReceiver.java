
package Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NetworkReceiver extends Thread {
	private MulticastSocket socket;
	private ConcurrentLinkedQueue<Message> messages;
	
	public NetworkReceiver(MulticastSocket s) {
		socket = s;
		messages = new ConcurrentLinkedQueue<Message>();
	}
	/**
	 * 
	 * @return if there is a message to be read
	 */
	public boolean hasMessage() {
		return !messages.isEmpty();
	}
	
	/**
	 * @return The most recently received message if there is one
	 * @throws NoSuchElementException 
	 */
	public Message nextMessage() throws NoSuchElementException {
		return messages.remove(); 
	}
	
	
	public void run() {
		for(;;) {
			byte[] buf = new byte[1000];
			DatagramPacket dp = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(dp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			messages.add(new Message(dp.getData().toString(), dp.getSocketAddress()));
		}
	}
}
