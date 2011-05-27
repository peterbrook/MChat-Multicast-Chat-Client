package UserInterface;

import Network.Message;


public class TextSender implements UISender {
	public void print(Message m) {
		System.out.println(m.getSender() + ": " + m.getMessage());
	}
}
