package UserInterface;

import java.util.NoSuchElementException;

public interface UIReceiver {
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
}
