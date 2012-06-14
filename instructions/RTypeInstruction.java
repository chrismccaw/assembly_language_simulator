package instructions;

import java.util.ArrayList;

import memory.Memory;
import memory.Registers;

public class RTypeInstruction implements Instruction {

	public String opcode;
	public String rd;
	public String rs;
	public int rdValue;
	public int rsValue;
	private int opcodeID;
	private Registers register;
	private Memory memory;
	private boolean stackCall = false;

	public RTypeInstruction(ArrayList<String> parameters) {
		this.opcode = parameters.get(0);
		this.rd = parameters.get(1);
		this.rs = parameters.get(2);
		;

		String[] rtypes = InstructionStore.getInstance().rtype();
		for (int i = 0; i < rtypes.length; i++) {
			if (rtypes[i].equals(opcode)) {
				opcodeID = i;
				break;
			}
		}

	}

	@Override
	public String toString() {
		return opcode + " " + rd + ", " + rs;
	}

	public int getOpcodeID() {
		return opcodeID;
	}

	@Override
	public void setMemory(Registers registers) {
		this.register = registers;

	}

	@Override
	public void updateInstruction() {
		if (isMemoryLocation(rd)) {
			String rdlocation = rd.substring(1, rd.length() - 1);

			if (isRegister(rdlocation)) {
				rdValue = memory.getValue(register.getRegisterValue(rdlocation)
						+ memory.dataSegmentStart);
			} else if (rdlocation.contains("+") && !rdlocation.contains("bp")) {
				rdValue = indexedAddressing(rdlocation);
			} else if (rdlocation.contains("bp")) {

				rdValue = memory.getStack().getValue(rdlocation);

			} else {

				int address = Integer.parseInt(rdlocation);
				rdValue = memory.getValue(address);
			}

		} else if (isRegister(rd)) {
			rdValue = register.getRegisterValue(rd);
		}

		if (isMemoryLocation(rs)) {
			String rslocation = rs.substring(1, rs.length() - 1);
			if (isRegister(rslocation)) {
				rsValue = memory.getValue(register.getRegisterValue(rslocation)
						+ memory.dataSegmentStart);
			} else if (rslocation.contains("+") && !rslocation.contains("bp")) {
				rsValue = memory.getValue(indexedAddressing(rslocation));
			} else if (rslocation.contains("bp")) {
				rsValue = memory.getStack().getValue(rslocation);
			} else {
				int address = Integer.parseInt(rslocation);
				rsValue = memory.getValue(address);
			}

		} else if (isRegister(rs)) {
			rsValue = register.getRegisterValue(rs);
		}
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
		if (isMemoryLocation(rd)) {
			String location = rd.substring(1, rd.length() - 1);
			if (isRegister(location))
				memory.putValue(rdValue, register.getRegisterValue(location)
						+ memory.dataSegmentStart);
			else if (rd.contains("bp"))
				memory.getStack().setValue(location, rdValue);
			else if (location.contains("+"))
				memory.putValue(rdValue, indexedAddressing(location));
			else
				memory.putValue(rdValue, Integer.parseInt(location));
		}

		else if (!stackCall) {
			register.setRegisterValue(rd, rdValue);
		}

		if (stackCall) {
			register.setRegisterValue("sp", memory.getStack().stack().size());
		}

	}

	@Override
	public void execute() {
		switch (opcodeID) {
		case 0: // ADD
			rdValue = rdValue + rsValue;
			break;
		case 1: // MOV
			rdValue = rsValue;
			break;
		case 2: // SUB
			rdValue = rdValue - rsValue;
			break;
		case 4: // NEG
			rdValue = 0 - rdValue;
			break;
		case 3: // CMP
			if (rdValue == rsValue) {
				register.setFlagValue("zf", 1);
			}
			if (rdValue != rsValue) {
				register.setFlagValue("zf", 0);
			}
			if (rdValue > rsValue) {
				register.setFlagValue("zf", 0);
				register.setFlagValue("sf", 1);
				register.setFlagValue("of", 1);
			}
			if (rdValue >= rsValue) {
				register.setFlagValue("sf", 1);
				register.setFlagValue("of", 1);
			}
			if (rdValue < rsValue) {
				register.setFlagValue("sf", 0);
				register.setFlagValue("of", 1);
			}
			if (rdValue <= rsValue) {
				register.setFlagValue("sf", 0);
				register.setFlagValue("of", 1);
				register.setFlagValue("zf", 1);
			}
			break;
		case 5: // MUL
			rdValue = rdValue * rsValue;
			break;
		case 6: // DIV
			rdValue = rdValue / rsValue;
			break;
		}
	}

	@Override
	public boolean pcHasChanged() {
		return false;
	}

	@Override
	public int getPC() {
		return -1;
	}

	@Override
	public boolean checkSyntax() {
		boolean correctOpcode = false, correctOperand1 = false, correctOperand2 = false;
		String[] rtype = InstructionStore.getInstance().rtype();
		for (int i = 0; i < rtype.length; i++) {
			if (rtype[i].equals(opcode))
				correctOpcode = true;
		}
		if (isRegister(rd) || isMemoryLocation(rd)) {
			correctOperand1 = true;
		}
		if (isRegister(rs) || isMemoryLocation(rs)) {
			correctOperand2 = true;
		}

		if (correctOpcode && correctOperand1 && correctOperand2) {
			return true;
		}
		return false;
	}

	private boolean isMemoryLocation(String operand) {
		return operand.contains("[") && operand.contains("]");
	}

	public boolean isRegister(String token) {
		if (token.equals("ax") || token.equals("bx") || token.equals("cx")
				|| token.equals("dx") || token.equals("bp")
				|| token.equals("sp") || token.equals("di"))
			return true;
		return false;
	}

	@Override
	public void setMainMemory(Memory memory) {
		this.memory = memory;
	}
	
	@Override
	public int getSize() {
		return 2;
	}

}
