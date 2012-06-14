package instructions;

import java.util.ArrayList;

import memory.Memory;
import memory.Registers;

public class NULLTypeInstruction implements Instruction {

	private ArrayList<String> parameters;
	private int opcodeID;
	private Memory memory;
	private int rdValue = 0;
	private int pc = -1;
	private Registers register;
	private boolean pcChanged = false;

	public NULLTypeInstruction(ArrayList<String> parameters) {
		this.parameters = parameters;

		String[] nulltypes = InstructionStore.getInstance().nulltype();
		for (int i = 0; i < nulltypes.length; i++) {
			if (nulltypes[i].equals(parameters.get(0))) {
				opcodeID = i;
				break;
			}
		}
	}

	@Override
	public void execute() {
		switch (opcodeID) {
		case 0: // PUSH
			if (isRegister(parameters.get(1)))
				memory.getStack().push(parameters.get(1),
						register.getRegisterValue(parameters.get(1)));
			else
				memory.getStack().push("", Integer.parseInt(parameters.get(1)));

			register.setRegisterValue("sp", (1000 - (memory.getStack().stack()
					.size() * 2)));
			break;
		case 1: // POP
			rdValue = memory.getStack().pop().value;
			register.setRegisterValue("sp", (1000 - (memory.getStack().stack()
					.size() * 2)));
			break;
		case 2: // INC
			rdValue = rdValue + 1;
			break;
		case 3: // RET
			memory.getStack().removeStackObjects(
					Integer.parseInt(parameters.get(1)) / 2);
			register.setRegisterValue("sp", (1000 - (memory.getStack().stack()
					.size() * 2)));
			pc = Integer.parseInt(parameters.get(1));
			pcChanged = true;
			break;
		case 4: // DEC
			rdValue = rdValue - 1;
			break;
		}
	}

	public boolean isRegister(String token) {
		if (token.equals("ax") || token.equals("bx") || token.equals("cx")
				|| token.equals("dx") || token.equals("bp")
				|| token.equals("sp") || token.equals("si")
				|| token.equals("di"))
			return true;
		return false;
	}

	@Override
	public String toString() {
		return parameters.get(0) + " " + parameters.get(1);

	}

	@Override
	public int getPC() {
		// TODO Auto-generated method stub
		return pc;
	}

	@Override
	public boolean pcHasChanged() {
		// TODO Auto-generated method stub
		return pcChanged;
	}

	@Override
	public void setMemory(Registers registers) {
		// TODO Auto-generated method stub
		this.register = registers;
	}

	@Override
	public void updateInstruction() {
		// TODO Auto-generated method stub
		if (isRegister(parameters.get(1))) {
			rdValue = register.getRegisterValue(parameters.get(1));
		}

	}

	@Override
	public void updateMemory() {
		// TODO Auto-generated method stub
		if (isRegister(parameters.get(1))) {
			register.setRegisterValue(parameters.get(1), rdValue);
		}

	}

	@Override
	public boolean checkSyntax() {
		// TODO Auto-generated method stub
		boolean correctOpcode = false, correctOperand1 = false;
		String[] nulltype = InstructionStore.getInstance().nulltype();
		for (int i = 0; i < nulltype.length; i++) {
			if (nulltype[i].equals(parameters.get(0)))
				correctOpcode = true;
		}
		if (isRegister(parameters.get(1)) || isConstant(parameters.get(1))) {
			correctOperand1 = true;
		}

		if (correctOpcode && correctOperand1) {
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
