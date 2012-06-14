package instructions;

import java.util.ArrayList;

import memory.Memory;
import memory.Registers;

public class JTypeInstruction implements Instruction {

	private String opcode;
	private int memoryLocation;
	private String label;
	private int opcodeID;
	private boolean jumpSuccessful = false;
	public boolean pcHasChanged = false;
	private int pc;
	private Registers register;
	private int highLevelStart;
	private int highLevelEnd;
	private Memory memory;

	public JTypeInstruction(ArrayList<String> parameters) {
		this.opcode = parameters.get(0);
		try {
			this.memoryLocation = Integer.parseInt(parameters.get(1));
		} catch (NumberFormatException n) {
		}
		label = parameters.get(2);
		String[] jtypes = InstructionStore.getInstance().jtype();
		for (int i = 0; i < jtypes.length; i++) {
			if (jtypes[i].equals(opcode)) {
				opcodeID = i;
				break;
			}
		}
	}

	@Override
	public void execute() {

		jumpSuccessful = false;
		switch (opcodeID) {
		case 0: // JMP
			jumpSuccessful = true;
			break;
		case 3: // JG
			if (register.getFlagValue("zf") == 0
					&& (register.getFlagValue("sf") == register
							.getFlagValue("of")))
				jumpSuccessful = true;
			break;
		case 4: // JGE
			if (register.getFlagValue("sf") == register.getFlagValue("of"))
				jumpSuccessful = true;
			break;
		case 5: // JL
			if (register.getFlagValue("sf") != register.getFlagValue("of"))
				jumpSuccessful = true;
			break;
		case 6: // JLE
			if ((register.getFlagValue("sf") != register.getFlagValue("of"))
					|| register.getFlagValue("zf") == 1)
				jumpSuccessful = true;
			break;
		case 1: // JE
			if (register.getFlagValue("zf") == 1)
				jumpSuccessful = true;
			break;
		case 2: // JNE
			if (register.getFlagValue("zf") == 0)
				jumpSuccessful = true;
			break;
		case 7: // CALL
			jumpSuccessful = true;

		}

		if (jumpSuccessful) {
			pcHasChanged = true;
			pc = memoryLocation;
		}
		jumpSuccessful = false;

	}

	@Override
	public void setMemory(Registers registers) {
		this.register = registers;
	}

	@Override
	public void updateInstruction() {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateMemory() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean pcHasChanged() {
		// TODO Auto-generated method stub
		return pcHasChanged;
	}

	@Override
	public int getPC() {
		// TODO Auto-generated method stub
		return pc;
	}

	@Override
	public String toString() {
		return opcode + " " + label;
	}

	@Override
	public boolean checkSyntax() {
		// TODO Auto-generated method stub
		boolean correctOpcode = false, correctLocation = false;
		String[] jtype = InstructionStore.getInstance().jtype();
		for (int i = 0; i < jtype.length; i++) {
			if (jtype[i].equals(opcode))
				correctOpcode = true;
		}
		if (isConstant()) {
			correctLocation = true;
		}

		if (correctOpcode && correctLocation) {
			return true;
		}

		return false;
	}

	public boolean isConstant() {
		try {
			int checkNumberic = memoryLocation;
			return true;
		} catch (NumberFormatException n) {
			return false;
		}
	}

	@Override
	public void setMainMemory(Memory memory) {
		this.memory = memory;
	}
	
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 2;
	}
}
