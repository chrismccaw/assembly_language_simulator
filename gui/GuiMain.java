package gui;

import cpu.Simulator_X86;

/**
 * This class passes the Simulator_X86 class the SimulatorView and HighLevelGUI
 * User Interfaces
 * 
 * @author Chris McCaw
 * @version 1.0
 */
public class GuiMain {

	public GuiMain(Simulator_X86 simulator) {
		SimulatorView view = new SimulatorView(simulator);
		simulator.getInstructionManager().addObserver(view);
		simulator.getMemory().getStack().addObserver(view);
		SimulatorController controller = new SimulatorController(view,
				simulator);
	}
}
