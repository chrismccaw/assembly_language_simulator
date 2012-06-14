package instructions;

import java.util.ArrayList;

import memory.Memory;
import memory.Registers;

public class ITypeInstruction implements Instruction {

	public int rdValue;
	public int intermediateValue;
	public String opcode;
	public String rd;
	private int opcodeID;
	private Registers register;
	private Memory memory;

	public ITypeInstruction(ArrayList<String> parameters) {

		this.opcode = parameters.get(0);
		rd = parameters.get(1);
		this.intermediateValue = Integer.parseInt(parameters.get(2));

		String[] itypes = InstructionStore.getInstance().itype();
		for (int i = 0; i < itypes.length; i++) {
			if (itypes[i].equals(opcode)) {
				opcodeID = i;
				break;
			}
		}

	}

	@Override
	public String toString() {
		return opcode + " " + rd + ", " + intermediateValue;
	}

	/**
	 * @return
	 * @uml.property name="opcodeID"
	 */
	public int getOpcodeID() {
		return opcodeID;
	}

	@Override
	public void setMemory(Registers registers) {
		this.register = registers;
	}

	public boolean isRegister(String token) {
		if (token.equals("ax") || token.equals("bx") || token.equals("cx")
				|| token.equals("dx") || token.equals("bp")
				|| token.equals("sp") || token.equals("di"))
			return true;
		return false;
	}

	@Override
	public void updateInstruction() {
		// TODO Auto-generated method stub
		String location = rd.substring(1, rd.length() - 1);
		if (isMemoryLocation(rd)) {
			if (isRegister(location)) {
				rdValue = memory.getValue(register.getRegisterValue(location)
						+ memory.dataSegmentStart);
			} else if (location.contains("+")) {
				rdValue = memory.getValue(indexedAddressing(location));
			} else {
				int address = Integer.parseInt(location);
				rdValue = memory.getValue(address);
			}
		} else if (isRegister(rd))
			rdValue = register.getRegisterValue(rd);
		else
			rdValue = Integer.parseInt(rd);
	}

	public int indexedAddressing(String location) {
		String[] splitLocation = location.split("\\+");
		String first = splitLocation[0].trim();
		String second = splitLocation[1].trim();
		int f = 0;
		int s = 0;
		if (isRegister(first))
			f = register.getRegisterValue(first) + memory.dataSegmentStart;
		else
			f = Integer.parseInt(first);
		if (isRegister(second))
			s = register.getRegisterValue(second) + memory.dataSegmentStart;
		else
			s = Integer.parseInt(second);

		return f + s;
	}

	@Override
	public void updateMemory() {
		// TODO Auto-generated method stub
		if (isMemoryLocation(rd)) {
			String location = rd.substring(1, rd.length() - 1);
			if (isRegister(location))
				memory.putValue(rdValue, register.getRegisterValue(location)
						+ memory.dataSegmentStart);
			else if (location.contains("+"))
				memory.putValue(rdValue, indexedAddressing(location));
			else
				memory.putValue(rdValue, Integer.parseInt(location));
		} else if (isRegister(rd))
			register.setRegisterValue(rd, rdValue);

	}

	private boolean isMemoryLocation(String operand) {
		// TODO Auto-generated method stub
		return operand.contains("[") && operand.contains("]");
	}

	@Override
	public void execute() {
		switch (opcodeID) {
		case 0: // ADD
			rdValue = rdValue + intermediateValue;
			break;
		case 1: // MOV
			rdValue = intermediateValue;
			break;
		case 2: // SUB
			rdValue = rdValue - intermediateValue;
			break;
		case 3: // CMP
			if (rdValue > intermediateValue) {
				register.setFlagValue("cf", 0);
				register.setFlagValue("zf", 0);
			}
			if (rdValue >= intermediateValue) {
				register.setFlagValue("cf", 0);
			}
			if (rdValue < intermediateValue) {
				register.setFlagValue("cf", 1);
			}
			if (rdValue <= intermediateValue) {
				register.setFlagValue("cf", 1);
				register.setFlagValue("zf", 1);
			}
			if (rdValue == intermediateValue) {
				register.setFlagValue("zf", 1);
			}
			if (rdValue != intermediateValue) {
				register.setFlagValue("zf", 0);
			}
			if (rdValue > intermediateValue) {
				register.setFlagValue("zf", 0);
				register.setFlagValue("sf", 1);
				register.setFlagValue("of", 1);
			}
			if (rdValue >= intermediateValue) {
				register.setFlagValue("sf", 1);
				register.setFlagValue("of", 1);
			}
			if (rdValue < intermediateValue) {
				register.setFlagValue("sf", 0);
				register.setFlagValue("of", 1);
			}
			if (rdValue <= intermediateValue) {
				register.setFlagValue("zf", 1);
			}
			break;
		case 4: // MUL
			rdValue = rdValue * intermediateValue;
			break;
		case 5: // DIV
			rdValue = rdValue / intermediateValue;
			break;

		}
	}

	@Override
	public boolean pcHasChanged() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getPC() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public boolean checkSyntax() {
		// TODO Auto-generated method stub
		boolean correctOpcode = false, correctOperand1 = false, correctOperand2 = false;
		String[] itype = InstructionStore.getInstance().itype();
		for (int i = 0; i < itype.length; i++) {
			if (itype[i].equals(opcode))
				correctOpcode = true;
		}
		if (isRegister(rd) || isMemoryLocation(rd)) {
			correctOperand1 = true;
		}
		if (isConstant(String.valueOf(intermediateValue))) {
			correctOperand2 = true;
		}

		if (correctOpcode && correctOperand1 && correctOperand2) {
			return true;
		}
		return false;
	}

	public boolean isConstant(String token) {
		try {
			int checkNumberic = Integer.parseInt(token);
			return true;
		} catch (NumberFormatException n) {
			return false;
		}
	}

	@Override
	public void setMainMemory(Memory memory) {
		// TODO Auto-generated method stub
		this.memory = memory;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 2;
	}

}
