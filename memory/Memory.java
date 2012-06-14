package memory;

import instructions.Instruction;

import java.util.*;

public class Memory {

	private Vector<Integer> memory;
	private MemoryStack stack;
	Random rand = new Random();
	public int codeSegmentStart = 16384;
	public int dataSegmentStart = 20480;
	public int stackSegmentStart = 24576;
	private final int ONE_MEG = 65535;

	public Memory() {
		memory = new Vector<Integer>(ONE_MEG); // bytes

		stack = new MemoryStack();
		for (int i = 0; i < ONE_MEG; i++)
			memory.add(i, 0);
	}

	public void addCodeSegment(Vector<Instruction> instructions) {
		for (int i = 0; i < instructions.size(); i++) {
			memory.add(i + codeSegmentStart, 1);
		}
	}

	public boolean putValue(int rdValue, int address) {
		if ((address >= 0) && (address < ONE_MEG)
				&& !(address >= 0 && address <= 1023)) {
			memory.setElementAt(rdValue, address);
			return true;
		} else
			return false;
	}

	public int getValue(int address) {
		return memory.elementAt(address);
	}

	public MemoryStack getStack() {
		return stack;
	}

	public void resetMemory() {
		for (int i = 0; i < ONE_MEG; i++)
			memory.add(i, 0);
	}

	public void reset() {
		resetMemory();
		stack.stack().clear();
		stack.oldbp = 0;
	}
}