package instructions;

import java.util.Observable;
import java.util.Vector;

/**
 * @author Decrypter
 */
public class ManageInstructions extends Observable {

	private int programCounter = 0;
	private boolean finished = false;
	private Vector<Instruction> instructions;

	public ManageInstructions() {
		instructions = new Vector<Instruction>();
	}

	/**
	 * @param instructions
	 * @uml.property name="instructions"
	 */
	public void setInstructions(Vector<Instruction> instructions) {
		this.instructions = instructions;
	}

	public Vector<Instruction> getCodeSegment() {
		return instructions;
	}

	public Vector<Instruction> getInstructions() {
		return instructions;
	}

	public int getProgramCounter() {
		return programCounter;
	}

	public boolean programIsFinished() {
		return finished;
	}

	public Instruction getRootInstruction() {
		// can be used for resetting program counter;
		finished = false;
		return instructions.elementAt(programCounter);
	}

	public Instruction getCurrentInstruction() {
		return instructions.elementAt(programCounter);
	}

	public void nextInstruction() {
		this.setChanged();
		this.notifyObservers();
		
		if (getCurrentInstruction().pcHasChanged())
			if (getCurrentInstruction() instanceof NULLTypeInstruction)
				programCounter = getCurrentInstruction().getPC()
						+ programCounter;
			else
				programCounter = getCurrentInstruction().getPC();
		else
			programCounter++;

		if (instructions.size() == programCounter)
			finished = true;


	}

	public void setProgramCounter(int pc) {
		programCounter = pc;
	}

	public void step() {
		getCurrentInstruction().execute();
	}

	public void reset() {
		// TODO Auto-generated method stub
		programCounter = 0;
		instructions.clear();
		finished = false;

	}

	public void resetProgram() {
		// TODO Auto-generated method stub
		programCounter = 0;
		finished = false;
	}

}
