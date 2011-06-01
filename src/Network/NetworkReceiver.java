
package Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public class NetworkReceiver extends Thread implements ReadableQueue<Message> {
	private MulticastSocket socket;
	private ConcurrentLinkedQueue<Message> messages;
	private boolean chatting;
	
	public NetworkReceiver(MulticastSocket s) {
		socket = s;
		messages = new ConcurrentLinkedQueue<Message>();
		chatting = true;
	}
	/**
	 * 
	 * @return if there is a message to be read
	 */
	public boolean hasNext() {
		return !messages.isEmpty();
	}
	
	/**
	 * @return The most recently received message if there is one
	 * @throws NoSuchElementException 
	 */
	public Message next() throws NoSuchElementException {
		return messages.remove(); 
	}
	
	
	public void run() {
		while(chatting) {
			byte[] buf = new byte[65507];
			DatagramPacket dp = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(dp);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			boolean localMsg = false;
			SocketAddress sa = dp.getSocketAddress();
			if (sa instanceof InetSocketAddress) {
				InetSocketAddress inetSA = (InetSocketAddress) sa;
				try {
					if (inetSA.getAddress().getHostAddress().equals(InetAddress.getLocalHost().getHostAddress())
							&& inetSA.getPort() == socket.getLocalPort())
						localMsg = true;
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!localMsg) {
				String d = new String(dp.getData(), 0, dp.getLength());
				Logger.getLogger("network").setUseParentHandlers(false);
				Logger.getLogger("network").info("NetworkReceiver: rec'd packet:" + d + " " + dp.getSocketAddress());
				messages.add(new Message(d, dp.getSocketAddress()));
			}
			// No sleep needed since socket.receive blocks
		}
	}
	
	public void stopChatting() {
		chatting = false;
	}
}
