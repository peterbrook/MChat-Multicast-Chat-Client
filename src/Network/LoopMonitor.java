package Network;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoopMonitor {
	int count;
	double updateRate;
	long startTime;
	int printPeriod;
	String loopDescription;
	public LoopMonitor(int printPeriod, String loopDescription) {
		count = 0;
		updateRate = 0;
		this.printPeriod = printPeriod;
		this.loopDescription = loopDescription;
	}
	
	public void start() {
		startTime = System.currentTimeMillis();
	}
	
	public void update() {
		long currentTime = System.currentTimeMillis();
		long timeDelta = (currentTime - startTime);
		double timeInSec = timeDelta * 1e-3;
		if (timeInSec > printPeriod) {
			// 5 seconds have passed
			startTime = currentTime;
			updateRate = count*1.0 / timeInSec;
			//Logger.getLogger("chat").log(Level.OFF, loopDescription+" Update Rate: " + updateRate + "Hz");
			count = 0;
		}
		count++;
	}

}
