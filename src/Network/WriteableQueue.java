package Network;

public interface WriteableQueue<T> {

	/**
	 * Adds an item to this queue
	 * @param item The item to add
	 */
	public void add(T item);
}
