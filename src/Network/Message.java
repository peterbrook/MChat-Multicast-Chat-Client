package Network;

import java.net.SocketAddress;
import java.util.Scanner;

public class Message {
	private String sender;
	private String message;
	private int sequenceNumber;
	private String messageType;
	private SocketAddress address;
	private String unparsedContent;

	/**
	 * Create a new Message with raw string contents
	 * 
	 * @param unparsedMessage
	 *            The string contents to be parsed into fields
	 * @param a
	 *            The destination address if this is an outgoing Message, or the
	 *            sending address if this was an incoming Message
	 */
	public Message(String unparsedMessage, SocketAddress a) {
		address = a;
		unparsedContent = unparsedMessage;
		
		Scanner string = new Scanner(unparsedMessage);
		messageType = string.next();
		if (messageType.equals("GBYE")) {
			sender = string.next();
		} else if (messageType.equals("GDAY")) {
			sender = string.next();
		} else if (messageType.equals("SAYS")) {
			sender = string.next();
			sequenceNumber = string.nextInt();
			// match until the end of the string message
			string.useDelimiter("\\z");
			message = string.next();
		} else if (messageType.equals("YEAH")) {
			sequenceNumber = string.nextInt();
		}
	}

	/**
	 * 
	 * @return the sender of the message
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * 
	 * @return The destination address if this is an outgoing Message, or the
	 *         sending address if this was an incoming Message
	 */
	public SocketAddress getSocketAddress() {
		return address;
	}

	/**
	 * 
	 * @return the contents of the message if this is a SAYS message
	 */
	public String getMessageContent() {
		return message;
	}
	
	public String getUnparsedContent() {
		return unparsedContent;
	}

	public String getMessageType() {
		return messageType;
	}
	
	/**
	 * 
	 * @return the sequence number
	 */
	public int getSequenceNumber() {
		return sequenceNumber;
	}
}
