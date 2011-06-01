package UserInterface;

import Network.Message;

public interface UISender {
	/**
	 * Sends a message to the user 
	 * @param m The message to show
	 */
	public void print(Message m);
}
