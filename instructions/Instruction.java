package instructions;

import memory.Memory;
import memory.Registers;

public interface Instruction {

	public void execute();
	public void setMemory(Registers registers);
	public void updateMemory();
	public boolean pcHasChanged();
	public int getPC();
	public String toString();
	public boolean checkSyntax();
	public void updateInstruction();
	public void setMainMemory(Memory memory);
	public int getSize();
	

}
