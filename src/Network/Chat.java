package Network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;

import UserInterface.*;

public class Chat {
	
	public static void main(String[] args) {
		String address;
		int port;
		if (args.length != 2) {
			address = "1.1.1.1";
			port = 6789;
		} else {
			address = args[0];
			port = Integer.parseInt(args[1]);
		}
		
		UI userInterface = new TextUI();
		InetAddress group;
		MulticastSocket socket;
		try {
			group = InetAddress.getByName(address);
			socket = new MulticastSocket(port);
			socket.joinGroup(group);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		
		for(;;) {
			
		}
	}
}
