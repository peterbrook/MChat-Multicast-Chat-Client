package UserInterface;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import Network.LoopMonitor;


public class TextReceiver extends Thread implements UIReceiver {
	private ConcurrentLinkedQueue<String> messages;
	
	public TextReceiver() {
		messages = new ConcurrentLinkedQueue<String>();
	}
	
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
		LoopMonitor lm = new LoopMonitor(5, "TextReceiver");
		for(;;) {
			System.out.print(">");
			lm.update();
			String message = input.nextLine();
			messages.add(message);
		}
	}
}
