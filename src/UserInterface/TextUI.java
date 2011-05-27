package UserInterface;
import java.io.Console;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

import Network.Message;


public class TextUI implements UI {
	private TextSender sender;
	private TextReceiver reciever;
	
	public TextUI() {
		sender = new TextSender();
		reciever = new TextReceiver();
		reciever.start();
	}
	
	/**
	 * Adds message to the queue of messages to be printed
	 * @param message
	 */
	public void display(Message m) {
		sender.print(m);
	}
	
	/**
	 * Does the interface have a chat message to be sent?
	 */
	public boolean hasMessage() {
		return reciever.hasMessage();
	}
	
	/**
	 * Returns the next Message, if there is one,
	 * o.w. throws a NoSuchElementException
	 * @return next message to be sent
	 * @throws NoSuchElementException
	 */
	public String nextMessage() throws NoSuchElementException {
		return reciever.nextMessage();
	}

}
