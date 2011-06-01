package UserInterface;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import Network.LoopMonitor;


public class TextReceiver extends Thread implements UIReceiver {
	private ConcurrentLinkedQueue<String> messages;
	private boolean chatting; 
	public TextReceiver() {
		messages = new ConcurrentLinkedQueue<String>();
		chatting = true;
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
		while (chatting) {
			System.out.print(">");
			try { Thread.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
			lm.update();
			// TODO: if this is changed to use polling (hasNext, etc) then we can cleanly exit
			if(input.hasNextLine()) {
				String message = input.nextLine();
				messages.add(message);
			}
		}
	}
	
	public void stopChatting() {
		chatting = false;
	}
}
