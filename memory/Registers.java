package memory;

import java.util.HashMap;

public class Registers {

	HashMap<String, Integer> generalRegisters;
	HashMap<String, Integer> flagRegisters;

	public Registers() {
		generalRegisters = new HashMap<String, Integer>();
		generalRegisters.put("ax", new Integer(0));
		generalRegisters.put("bx", new Integer(0));
		generalRegisters.put("cx", new Integer(0));
		generalRegisters.put("dx", new Integer(0));
		generalRegisters.put("sp", new Integer(1000));
		generalRegisters.put("si", new Integer(0));
		generalRegisters.put("bp", new Integer(1000));
		generalRegisters.put("ip", new Integer(0));

		flagRegisters = new HashMap<String, Integer>();
		flagRegisters.put("cf", new Integer(0)); // Carry Flag � becomes one
													// if an addition,
													// multiplication, AND, OR,
													// etc results in a value
													// larger than the register
													// meant for the result
		flagRegisters.put("pf", new Integer(0)); // PF Parity Flag � becomes 1
													// if the lower 8-bits of an
													// operation contains an
													// even number of 1 bits
		flagRegisters.put("zf", new Integer(0)); // ZF Zero Flag � becomes 1
													// if an operation results
													// in a new Integer(0)
													// writeback, or new
													// Integer(0) register
		flagRegisters.put("af", new Integer(0)); // Auxiliary Flag � Set on a
													// carry or borrow to the
													// value of the lower order
													// 4 bits
		flagRegisters.put("sf", new Integer(0)); // SF Sign Flag � is 1 if the
													// value saved is negative,
													// new Integer(0) for
													// positive
		flagRegisters.put("of", new Integer(0)); // OF Overflow Flag � becomes
													// 1 if the operation is
													// larger than available
													// space to write (eg:
													// addition which results in
													// a number >32-bits)
	}

	public void setRegisterValue(String registerName, Integer value) {
		generalRegisters.put(registerName.toLowerCase(), value);
	}

	public int getRegisterValue(String registerName) {
		return generalRegisters.get(registerName);
	}

	public void setFlagValue(String registerName, Integer value) {
		flagRegisters.put(registerName.toLowerCase(), value);
	}

	public int getFlagValue(String registerName) {
		return flagRegisters.get(registerName.toLowerCase());
	}

	public boolean isRegister(String registerCheck) {
		return generalRegisters.containsKey(registerCheck.toLowerCase());
	}

	public void reset() {
		generalRegisters = new HashMap<String, Integer>();
		generalRegisters.put("ax", new Integer(0));
		generalRegisters.put("bx", new Integer(0));
		generalRegisters.put("cx", new Integer(0));
		generalRegisters.put("dx", new Integer(0));
		generalRegisters.put("sp", new Integer(1000));
		generalRegisters.put("si", new Integer(0));
		generalRegisters.put("di", new Integer(0));
		generalRegisters.put("bp", new Integer(1000));

		flagRegisters = new HashMap<String, Integer>();
		flagRegisters.put("cf", new Integer(0));
		flagRegisters.put("pf", new Integer(0));
		flagRegisters.put("zf", new Integer(0));
		flagRegisters.put("af", new Integer(0));
		flagRegisters.put("sf", new Integer(0));
		flagRegisters.put("of", new Integer(0));
	}

	public static void main(String[] args) {
		Registers r = new Registers();
		System.out.println(r.getRegisterValue("ax"));
		r.setRegisterValue("ax", 2);
		System.out.println(r.getRegisterValue("ax"));
	}

}
