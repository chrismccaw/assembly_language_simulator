package instructions;

public class InstructionStore {

	private static InstructionStore instance = null;

	public String[] allInstructions() {
		String[] temp = { "add", "mov", "sub", "cmp", "neg", "jmp", "ja",
				"jae", "jb", "jbe", "je", "jne", "jg", "jge", "jl", "jle",
				"push", "pop", "inc", "ret", "call", "mul", "dec", "div" };
		return temp;
	}

	public String[] rtype() {
		String[] temp = { "add", "mov", "sub", "cmp", "neg", "mul", "div" };
		return temp;
	}

	public String[] itype() {
		String[] temp = { "add", "mov", "sub", "cmp", "mul", "div" };
		return temp;
	}

	public String[] jtype() {
		String[] temp = { "jmp", "je", "jne", "jg", "jge", "jl", "jle", "call" };
		return temp;
	}

	public String[] nulltype() {
		String[] temp = { "push", "pop", "inc", "ret", "dec" };
		return temp;
	}

	public static InstructionStore getInstance() {
		instance = new InstructionStore();
		return instance;
	}
}
