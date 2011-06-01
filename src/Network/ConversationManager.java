package Network;

import java.net.SocketAddress;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The ConversationManager keeps track of which hosts are in the conversation
 * and handles reliable transmission of messages to and from other participants
 * 
 */
public class ConversationManager extends Thread {
	private ConcurrentHashMap<SocketAddress, ParticipantManager> clients;
	private ConcurrentLinkedQueue<Message> outputQueue;
	private ConcurrentLinkedQueue<Message> messageQueue;
	private boolean quitSeen;
	private int seqNum;
	private String nickname;

	public ConversationManager(String nickname) {
		this.nickname = nickname; 
		clients = new ConcurrentHashMap<SocketAddress, ParticipantManager>();
		outputQueue = new ConcurrentLinkedQueue<Message>();
		messageQueue = new ConcurrentLinkedQueue<Message>();
		
		quitSeen = false;
	}

	@Override
	public void run() {
		while (!quitSeen) {
			for (ParticipantManager m : clients.values()) {
				while (m.hasNext()) {
					messageQueue.add(m.next());
				}
				
				while (m.hasNextForDisplay()) {
					outputQueue.add(m.nextForDisplay());
				}
			}
			
			// Sleep a bit so that we don't max the cpu
			try { Thread.sleep(10); } catch (InterruptedException e) { }
		}
	}
	
	/**
	 * Checks to see if there is a new message available from the conversation
	 * 
	 * @return true if a new message is available, false otherwise
	 */
	public boolean hasNextOutput() {
		return !outputQueue.isEmpty();
	}

	/**
	 * Checks to see if there is a new message to send to the network
	 * 
	 * @return true if a new message is available, false otherwise
	 */
	public boolean hasNextMessage() {
		return !messageQueue.isEmpty();
	}

	/**
	 * Gets the next available message (text or status) from the conversation
	 * 
	 * @return the next available message if one is available
	 * @throws NoSuchElementException
	 *             if there is no available message
	 */
	public Message nextOutput() throws NoSuchElementException {
		return outputQueue.remove();
	}

	/**
	 * Gets the next available message to send to the network
	 * 
	 * @return the next available message if one is available
	 * @throws NoSuchElementException
	 *             if there is no available message
	 */
	public Message nextMessage() throws NoSuchElementException {
		return messageQueue.remove();
	}

	/**
	 * Add an unparsed message rec'd from the network to the this
	 * ConversationManager
	 * 
	 * @param item
	 *            The message to add
	 */
	public void addMessage(Message item) {
		parseMessage(item);
	}

	/**
	 * Adds a new input from the user to this ConversationManager
	 * 
	 * @param item
	 *            The input to add
	 */
	public void addInput(String item) {
		parseCommand(item);
	}

	private void parseMessage(Message m) {
		// GDAY? check to see if the socketaddress is in the hashmap. if it is,
		// get it, stop it, and add a new one
		if (m.getMessageType().equals("GDAY") || m.getMessageType().equals("SAYS")) {
			boolean clientManagerNotPresent = stopClientManager(m);
			if (clientManagerNotPresent) {
				SocketAddress clientAddr = m.getSocketAddress();
				String clientNick = m.getSender();
				ParticipantManager clientManager = new ParticipantManager(nickname, clientNick, clientAddr);
				System.out.println(clientNick+ " @" + clientAddr + " Joined");
				clientManager.start();
				clients.put(clientAddr, clientManager);
			} else {
				// silently ignore this GDAY. Perhaps we should tell the participantmanager that its client
				// is still alive?
			}
			
			if (m.getMessageType().equals("SAYS")) {
				clients.get(m.getSocketAddress()).addMessageToAck(m);
				//outputQueue.add(m);
			}
		} else if (m.getMessageType().equals("GBYE")) {
			// TODO  CLEAN UP MESSAGE CLASS
			stopClientManager(m);
		} else if (m.getMessageType().equals("YEAH")) {
			clients.get(m.getSocketAddress()).addAckdMessage(m);
		} else {
			System.out.println("Unrecognized message:");
			System.out.println("Start:----------");
			System.out.println(m.getUnparsedContent());
			System.out.println("End:------------");
		}

	}

	/**
	 * Checks to see if we already have a ParticipantManager for the sender of this message.
	 * If we do, and this message has a different nickname, we stop the old one, otherwise
	 * we do nothing.
	 * @param m The message to get the sender and nickname from
	 * @return true if the manager for this client is not in the list at the end of the method
	 */
	private boolean stopClientManager(Message m) {
		SocketAddress clientAddr = m.getSocketAddress();
		if (clients.containsKey(clientAddr)) {
			ParticipantManager existingManager = clients.get(clientAddr);
			// If this is the same address but a different nick, remove the old manager
			if (!existingManager.getNickname().equals(m.getSender())) {
				existingManager.stopSending();
				try {
					existingManager.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				clients.remove(clientAddr);
				return true;
			}
			// if we didn't remove anything from the client list
			return false;
		}
		// if the client was never in the list
		return true;
	}

	private void parseCommand(String s) {
		if (s.startsWith("/quit")) {
			quitSeen = true;
		} else {
			sendMessageToClients(s);
		}
	}

	private void sendMessageToClients(String s) {
		for (ParticipantManager manager : clients.values()) {
			manager.addMessageToSend(seqNum++, s);
		}
	}

	/**
	 * Checks to see if we have seen an a quit command from the user
	 * 
	 * @return true if we have seen a quit, false otherwise
	 */
	public boolean gotExit() {
		return quitSeen;
	}

}
