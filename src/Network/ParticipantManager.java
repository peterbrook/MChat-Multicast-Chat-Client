package Network;

import java.net.SocketAddress;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;

public class ParticipantManager extends Thread implements ReadableQueue<Message> {
	private SocketAddress participantAddress;
	private boolean running;
	/**
	 * The raw string messages we need to send to our participant
	 */
	private ConcurrentSkipListMap<Integer, String> messagesToSend;
	/**
	 * The messages which we have gotten and need to ack
	 */
	private ConcurrentLinkedQueue<Message> messagesToAck;
	/**
	 * The queue of messages to send out to the network
	 */
	private ConcurrentLinkedQueue<Message> outputMessages;
	/**
	 * The queue of messages that have been ack'd and should be removed
	 */
	private ConcurrentLinkedQueue<Message> ackdMessages;
	
	/**
	 * The queue of messages that have been rec'd by the participant and should be displayed
	 */
	private ConcurrentLinkedQueue<Message> displayMessages;
	private String myNickname;
	private String clientNick;
	private Set<Integer> seenMessages;
	
	public ParticipantManager(String myNickname, String clientNick, SocketAddress participantAddress) {
		this.myNickname = myNickname;
		this.clientNick = clientNick;
		this.participantAddress = participantAddress;
		this.messagesToSend = new ConcurrentSkipListMap<Integer, String>();
		this.messagesToAck = new ConcurrentLinkedQueue<Message>();
		this.outputMessages = new ConcurrentLinkedQueue<Message>();
		this.ackdMessages = new ConcurrentLinkedQueue<Message>();
		this.displayMessages = new ConcurrentLinkedQueue<Message>();
		this.seenMessages = new HashSet<Integer>();
		this.running = true;
	}
	
	@Override
	public void run() {
		int loopCount = 0;
		while (running) {
			
			// Remove messages which have been sent and ack'd
			while (!ackdMessages.isEmpty()) {
				Message m = ackdMessages.remove();
				// Remove the given outstanding message from our send list
				messagesToSend.remove(m.getSequenceNumber());
			}
			
			// Ack received messages
			while (!messagesToAck.isEmpty()) {
				Message m = messagesToAck.remove();
				if (!seenMessages.contains(m.getSequenceNumber())) {
					seenMessages.add(m.getSequenceNumber());
					// output to the UI
					displayMessages.add(m);
					Message ackMessage = new Message("YEAH "+m.getSequenceNumber(), this.participantAddress);
					// and output the ack to the network
					outputMessages.add(ackMessage);
				} else {
					// silently drop this already seen message
				}
			}
			
			// Send first outstanding message
			// If enough time has elapsed
			if (!messagesToSend.isEmpty() && loopCount % 10 == 0) {
				Entry<Integer, String> e = messagesToSend.firstEntry();
				StringBuilder sb = new StringBuilder();
				sb.append("SAYS ");
				sb.append(this.myNickname);
				sb.append(" ");
				sb.append(e.getKey());
				sb.append(" ");
				sb.append(e.getValue());
				Message output = new Message(sb.toString(), this.participantAddress);
				outputMessages.add(output);
				loopCount = 0;
			} else {
				loopCount++;
			}
			// Sleep a bit so that we don't max the cpu
			// note also that this controls the rate at which things are ack'd and sent 
			try { Thread.sleep(50); } catch (InterruptedException e) { }
		}
	}
	
	public void addMessageToSend(Integer seqNum, String message) {
		messagesToSend.put(seqNum, message);
	}
	
	public void addMessageToAck(Message m) {
		messagesToAck.add(m);
	}
	
	public void addAckdMessage(Message m) {
		ackdMessages.add(m);
	}
	
	public void stopSending() {
		running = false;
	}

	@Override
	public boolean hasNext() {
		return !outputMessages.isEmpty();
	}

	@Override
	public Message next() throws NoSuchElementException {
		return outputMessages.remove();
	}
	
	
	public String getNickname() {
		return this.clientNick;
	}

	/*
	 * By making the ParticipantManager control which messages from the client
	 * are displayed, we can make sure that duplicate messages aren't displayed.
	 */
	
	public boolean hasNextForDisplay() {
		return !displayMessages.isEmpty();
	}

	public Message nextForDisplay() {
		return displayMessages.remove();
	}
	

}
