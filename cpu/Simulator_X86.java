package cpu;

import instructions.Instruction;
import instructions.ManageInstructions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JOptionPane;

import assignment.AssignmentFillIn;
import assignment.SimulatorAssignment;

import lexical_and_syntax_analysis.LexicalAnalyzer;
import lexical_and_syntax_analysis.SymbolTable;
import lexical_and_syntax_analysis.SyntaxAnalyzer;
import lexical_and_syntax_analysis.TokenLabel;
import memory.Memory;
import memory.Registers;

/**
 * The class is the core of the cpu. It create instances of the other modules,
 * lexical, syntax, memory, registers and assignment. The controller in the MVC
 * uses this class as the model to fetch and set data.
 * 
 * The class supports the following functions: 1. Running the lexical analysis
 * � The assembly language file passed to the lexical analyzer. The result is
 * passed back the CPU where it checks to see if there where any errors during
 * this stage. 2. The file produced from the lexical analysis is passing to the
 * syntax analysis. The syntax analysis executes based on the result of the
 * lexical analysis. 3. Adding the code, data, stack segment to the relevant
 * panes in the GUI. 4. Updating the instruction with register values. 5.
 * Telling the instruction manager in the instruction module to execute the next
 * instruction 6. Updating the register values after the instruction has been
 * executed. 7. Resetting the CPU to its initial state
 * 
 * @author Chris McCaw
 * @version 1.0
 */
public class Simulator_X86 {

	LexicalAnalyzer lexical;
	SyntaxAnalyzer syntax;
	ManageInstructions instruction_manager;
	Memory memory;
	Registers registers;
	SimulatorAssignment assignment;
	AssignmentFillIn assignmentFillin;
	private boolean execution;
	private StringBuffer currentProgram;
	private Vector<Instruction> instructionsFromSyntax;
	private ArrayList<String> globalVariables = new ArrayList<String>();

	public Simulator_X86() {

		lexical = new LexicalAnalyzer();
		syntax = new SyntaxAnalyzer();
		instruction_manager = new ManageInstructions();
		memory = new Memory();
		registers = new Registers();
		assignment = new SimulatorAssignment();
		assignmentFillin = new AssignmentFillIn();

	}

	/**
	 * Loads the assembler to the lexical analyzer and syntax analyzer
	 * 
	 * @param program
	 *            assembly language program
	 */
	public void loadProgram(StringBuffer program) {
		execution = true;
		currentProgram = program;
		runLexicalAnaysis();
		if (lexical.getError()) {
			JOptionPane.showMessageDialog(null, "There is a problem at token "
					+ lexical.getErrorString().trim(), "Lexical Error",
					JOptionPane.ERROR_MESSAGE);
			execution = false;
		} else {
			syntax.setCodeFromLexical(lexical.getLexicalNodes());
			instructionsFromSyntax = syntax.executeSyntaxAnalyzer();
		}
		if (syntax.getError()) {
			JOptionPane.showMessageDialog(null, syntax.getErrorString().trim()
					+ " is not a valid instruction ", "Syntax Error",
					JOptionPane.ERROR_MESSAGE);
			execution = false;
		}

		if (execution) {
			memory.addCodeSegment(instructionsFromSyntax);
			for (int j = 0; j < syntax.getDataSegmentValues().size(); j++) {
				memory.putValue(syntax.getDataSegmentValues().get(j),
						memory.dataSegmentStart + j);
			}
			instruction_manager.setInstructions(instructionsFromSyntax);
			for(int i =0 ;i < instructionsFromSyntax.size(); i++){
			}
		}
		SymbolTable.getInstance();
		Collection<TokenLabel> dataSegmentValues = SymbolTable.getSymbolTable()
				.values();
		for (TokenLabel token : dataSegmentValues) {
			memory.putValue(token.value,
					memory.dataSegmentStart + token.getLocation());
			globalVariables.add(token.labelName);
		}

	}

	public String getGlobalName(int i) {
		return globalVariables.get(i);
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public int getProgramCounter() {
		return instruction_manager.getProgramCounter();
	}

	/**
	 * Retrieves program counter
	 * 
	 * @return (int) program counter
	 */
	public SimulatorAssignment getAssignment() {
		return assignment;
	}

	/**
	 * get the Assignment Fill in the space instance
	 * 
	 * @return (AssignmentFillIn) Assignment Instance
	 */
	public AssignmentFillIn getAssignmentFillIn() {
		// TODO Auto-generated method stub
		return assignmentFillin;
	}

	/**
	 * Executes the current instruction
	 * 
	 */
	public void executeStepMode() {
		if (!instruction_manager.programIsFinished()) {
			executeInstruction();
		}
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public Registers getRegisters() {
		return registers;
	}

	/**
	 * Gets the memory Instance
	 * 
	 * @return (Memory) the memory instance
	 */
	public Memory getMemory() {
		return memory;
	}

	/**
	 * Gets ManageInstructions instance
	 * 
	 * @return (ManageInstructions) Gets ManageInstructions instance
	 */
	public ManageInstructions getInstructionManager() {
		return instruction_manager;
	}

	/**
	 * Loads the current assembly language program to the lexical analysis
	 * <code>LexicalAnalyzer</code> The lexical analyzer is executed
	 * 
	 */
	public void runLexicalAnaysis() {
		lexical.loadAssemblyFile(currentProgram);
		lexical.runAnalyzer();
	}

	/**
	 * Gets the collection of instructions that is produced from the Syntax
	 * Analyzer
	 * 
	 * @return (Vector<Instruction>) returns the collection of instructions
	 */
	public Vector<Instruction> getCodeSegmentFile() {
		return instructionsFromSyntax;
	}

	/**
	 * Resets the the collections used by a previous assemble. Calls
	 * lexical.reset(); syntax.reset(); instruction_manager.reset();
	 * memory.reset(); registers.reset(); assignmentFillin.reset();
	 * 
	 */
	public void reset() {
		lexical.reset();
		syntax.reset();
		instruction_manager.reset();
		memory.reset();
		registers.reset();
		assignmentFillin.reset();
		SymbolTable.getInstance().getSymbolTable().clear();
		execution = false;
	}

	public void resetCurrentProgram() {
		instruction_manager.resetProgram();
		registers.reset();
		memory.reset();

	}

	/**
	 * First - gets the current instruction <br />
	 * Second - passes the memory to the instruction for the instruction to get
	 * latest memory values<br />
	 * Third - The instruction gets updated<br />
	 * Fourth - Instruction is executed<br />
	 * Fifth - Update the memory with any changes that the instruction made<br />
	 * Sixth - Increments the program counter<br />
	 */
	public void executeInstruction() {
		Instruction instruction = instruction_manager.getCurrentInstruction();
		instruction.setMemory(registers);
		instruction.setMainMemory(memory);
		instruction.updateInstruction();
		instruction_manager.step();
		instruction.updateMemory();

		int stackSize = (1000 - registers.getRegisterValue("sp")) / 2;
		memory.getStack().removeStackObjects(
				memory.getStack().stack().size() - stackSize);
		
		instruction_manager.nextInstruction();
	}

	/**
	 * Gets execution flag - True is execution is successful, false otherwise
	 * 
	 * @return returns the execution flag. This is set based on the results of
	 *         the lexical and syntax analysis
	 */
	public boolean canExecute() {
		// TODO Auto-generated method stub
		return execution;
	}

}
