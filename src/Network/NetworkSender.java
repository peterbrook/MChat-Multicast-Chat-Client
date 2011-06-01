package Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NetworkSender extends Thread implements WriteableQueue<Message> {
	private MulticastSocket socket;
	private ConcurrentLinkedQueue<Message> messages;
	private boolean chatting;
	public NetworkSender(MulticastSocket s) {
		socket = s;
		messages = new ConcurrentLinkedQueue<Message>();
		chatting = true;
	}

	public void run() {
		while(chatting || !messages.isEmpty()) {
			while (!messages.isEmpty()) {
				Message m = messages.remove();
				byte[] data = m.getUnparsedContent().getBytes();
				try {
					DatagramPacket p = new DatagramPacket(data, data.length, m.getSocketAddress());
					socket.send(p);
				} catch (SocketException e) {
					System.err.println("NetworkSender: Unable to create DatagramPacket!");
				} catch (IOException e) {
					System.err.println("NetworkSender: Unable to send data on socket!");
				}
			}
			
			// Sleep a bit so that we don't max the cpu
			try { Thread.sleep(5); } catch (InterruptedException e) { }
		}
		//WOOOHOOOO GIANT F'ING HACK.
		System.exit(0);
	}

	@Override
	public void add(Message item) {
		if(chatting) messages.add(item);
	}
	
	public void stopChatting() {
		chatting = false;
	}
}
