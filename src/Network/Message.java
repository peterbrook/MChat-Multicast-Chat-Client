package Network;

import java.util.Scanner;

public class Message {
	private String sender;
	private String message;
	private int sequenceNumber;
	private String messageType;
	
	public Message(String unparsedMessage) {
		Scanner string = new Scanner(unparsedMessage);
		messageType = string.next();
		
		if (messageType.equals("GBYE")) {
			sender = string.next();
		} else if (messageType.equals("GDAY")) {
			sender = string.next();
		} else if (messageType.equals("SAYS")) {
			sender = string.next();
			sequenceNumber = Integer.parseInt(string.next());
			message = string.next();
		} else if (messageType.equals("YEAH")) {
			sequenceNumber = Integer.parseInt(string.next());
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
	 * @return the contents of the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * 
	 * @return the sequence number
	 */
	public int getSequenceNumber() {
		return sequenceNumber;
	}
}
