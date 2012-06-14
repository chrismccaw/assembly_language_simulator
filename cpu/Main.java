package cpu;

import gui.GuiMain;
import gui.HighLevelGUI;

public class Main {

	public static void main(String[] args) {
		Simulator_X86 simulator = new Simulator_X86();
		GuiMain gui = new GuiMain(simulator);
	}
}
