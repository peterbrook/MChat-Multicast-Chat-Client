package Network;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The ConversationManager keeps track of which hosts are in the conversation
 * and handles reliable transmission of messages to and from other participants
 *
 */
public class ConversationManager {
	private HashMap<SocketAddress, ParticipantManager> clients;
	private ConcurrentLinkedQueue<Message> outputQueue;
	private ConcurrentLinkedQueue<Message> messageQueue;
	private boolean quitSeen;
	
	// usernames
	// socktaddress
	// 
	
	public ConversationManager() {
		// TODO Auto-generated constructor stub
		 clients = new HashMap<SocketAddress, ParticipantManager>();
		 outputQueue = new ConcurrentLinkedQueue<Message>();
		 messageQueue = new ConcurrentLinkedQueue<Message>();
		 
		 quitSeen = false;
	}
	
	/**
	 * Checks to see if there is a new message available from the conversation
	 * @return true if a new message is available, false otherwise
	 */
	public boolean hasNextOutput() {
		return !outputQueue.isEmpty();
	}
	
	/**
	 * Checks to see if there is a new message to send to the network
	 * @return true if a new message is available, false otherwise
	 */
	public boolean hasNextMessage() {
		return !messageQueue.isEmpty();
	}

	/**
	 * Gets the next available message (text or status) from the conversation
	 * @return the next available message if one is available
	 * @throws NoSuchElementException if there is no available message
	 */
	public Message nextOutput() throws NoSuchElementException {
		return outputQueue.remove();
	}

	/**
	 * Gets the next available message to send to the network
	 * @return the next available message if one is available
	 * @throws NoSuchElementException if there is no available message
	 */
	public Message nextMessage() throws NoSuchElementException {
		return messageQueue.remove();
	}
	
	/**
	 * Add an unparsed message rec'd from the network to the this ConversationManager
	 * @param item The message to add
	 */
	public void addMessage(Message item) {
		parseMessage(item);
	}
	
	/**
	 * Adds a new input from the user to this ConversationManager
	 * @param item The input to add
	 */
	public void addInput(String item) {
		parseCommand(item);
	}

	private void parseMessage(Message m) {
		// TODO Auto-generated method stub
		
		// GDAY? check to see if the socketaddress is in the hashmap. if it is, get it, stop it, and add a new one
		
		// GBYE? check to see if the socketaddress is in the hashmap. if it is, get it and stop it
		
		
	}

	private void parseCommand(String s) {
		if (s.startsWith("/quit")) {
			quitSeen = true;
		}
	}

	/**
	 * Checks to see if we have seen an a quit command from the user
	 * @return true if we have seen a quit, false otherwise
	 */
	public boolean gotExit() {
		return quitSeen;
	}
	

}
