package Network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import UserInterface.TextUI;
import UserInterface.UI;

public class Chat {
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		String address;
		int port;
		String nickname;
		if (args.length != 3) {
			address = "225.0.0.0";
			port = 6789;
			nickname = "uname";
		} else {
			address = args[0];
			port = Integer.parseInt(args[1]);
			nickname = args[2];
		}
		
		try {
			Logger l = Logger.getLogger("network");
			l.setUseParentHandlers(false);
			l.addHandler(new FileHandler("/tmp/network-logger"));
			
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		UI userInterface = new TextUI();
		InetAddress group = null;
		SocketAddress mcastAddress = null;
		MulticastSocket socket = null;
		try {
			group = InetAddress.getByName(address);
			mcastAddress = new InetSocketAddress(group, port);
			socket = new MulticastSocket(port);
			socket.joinGroup(group);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		
		NetworkSender multicastSender = new NetworkSender(socket);
		multicastSender.start();
		
		NetworkReceiver receiver = new NetworkReceiver(socket);
		receiver.start();
		AnnounceSender gdaySender = new AnnounceSender(mcastAddress, nickname);
		gdaySender.start();
		ConversationManager cm = new ConversationManager(nickname);
		cm.start();
		boolean chatting = true;
		
		LoopMonitor lm = new LoopMonitor(5, "Main");
		lm.start();
		while (chatting) {
			// Send GDAY messages
			if (gdaySender.hasNext()) {
				multicastSender.add(gdaySender.next());
			}
			
			// Add any commands from the UI
			if (userInterface.hasMessage()) {
				cm.addInput(userInterface.nextMessage());
			}
			
			// Read messages from the network
			if (receiver.hasNext()) {
				cm.addMessage(receiver.next());
			}
			
			// Read messages that we need to send to the network
			if (cm.hasNextMessage()) {
				multicastSender.add(cm.nextMessage());
			}
			
			// Read output that we need to push to the UI
			if (cm.hasNextOutput()) {
				userInterface.display(cm.nextOutput());
			}
			
			// If we read an exit command from the UI, quit
			if (cm.gotExit()) {
				chatting = false;
			}
			
			lm.update();
			// Sleep a bit so that we don't max the cpu
			try { Thread.sleep(10); } catch (InterruptedException e) { }
		}
		userInterface.stopChatting();
		receiver.stopChatting();
		multicastSender.stopChatting();
		gdaySender.stopSending();
		multicastSender.add(new Message("GBYE "+nickname, mcastAddress));

		//socket.leaveGroup(group);
		//multicastSender.stop();
		
	}
}
