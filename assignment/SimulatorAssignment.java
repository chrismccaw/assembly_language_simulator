package assignment;

import java.util.Random;

public class SimulatorAssignment {

	private Random rand;

	public SimulatorAssignment() {
		rand = new Random();
	}

	public int registerAssignment() {
		return rand.nextInt(11);
	}

}
