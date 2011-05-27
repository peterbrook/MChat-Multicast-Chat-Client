package UserInterface;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;


public class TextReceiver extends Thread implements UIReceiver {
	private ConcurrentLinkedQueue<String> messages;
	
	@Override
	public boolean hasMessage() {
		return !messages.isEmpty();
	}

	@Override
	public String nextMessage() throws NoSuchElementException {
		return messages.remove();
	}
	
	@Override
	public void run() {
		Scanner input = new Scanner(System.in);
		for(;;) {
			String message = input.nextLine();
			messages.add(message);
		}
	}
}
