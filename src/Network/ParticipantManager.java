package Network;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;

public class ParticipantManager extends Thread implements
		WriteableQueue<String> {
	private SocketAddress participantAddress;
	//private Conc
	//private ConcurrentHashMap<K, V> 
	
	public ParticipantManager(SocketAddress participantAddress) {
		this.participantAddress = participantAddress;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}
	
	@Override
	public void add(String message) {
		// TODO Auto-generated method stub

	}

}
