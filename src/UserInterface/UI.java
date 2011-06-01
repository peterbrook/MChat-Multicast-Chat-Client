package UserInterface;
import java.util.NoSuchElementException;

import Network.Message;


public interface UI {
	/**
	 * Displays the message
	 * @param message
	 */
	public void display(Message m);
	/**
	 * Does the interface have a chat message to be sent?
	 */
	public boolean hasMessage();
	/**
	 * Returns the next Message, if there is one,
	 * o.w. throws a NoSuchElementException
	 * @return next message to be sent
	 * @throws NoSuchElementException
	 */
	public String nextMessage() throws NoSuchElementException;
	
	/**
	 * Kills underlying threads, softly
	 */
	public void stopChatting();
}
