package Network;

import java.util.NoSuchElementException;

public interface ReadableQueue<T> {
	/**
	 * Checks to see if this queue has an item available
	 * @return true if an item is available, false otherwise
	 */
	public boolean hasNext();
	
	/**
	 * Gets the next element of this queue
	 * @return the element
	 * @throws NoSuchElementException if there is no available element
	 */
	public T next() throws NoSuchElementException;
}
