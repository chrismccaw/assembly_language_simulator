package lexical_and_syntax_analysis;

import instructions.ITypeInstruction;
import instructions.Instruction;
import instructions.JTypeInstruction;
import instructions.NULLTypeInstruction;
import instructions.RTypeInstruction;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @author Chris McCaw
 */
public class SyntaxAnalyzer {

	private Vector<Instruction> codeSegment;
	private Vector<Integer> dataSegment;
	private Vector<LexicalLine> lexicalTokens;
	private boolean error = false;
	private String errorString;
	private String currentInstruction;

	public SyntaxAnalyzer() {
		codeSegment = new Vector<Instruction>();
		dataSegment = new Vector<Integer>();
	}

	public void reset() {
		codeSegment.clear();
		dataSegment.clear();
	}

	public void setCodeFromLexical(Vector<LexicalLine> lexicalTokens) {
		this.lexicalTokens = lexicalTokens;
	}

	public boolean getError() {
		return error;
	}

	public String getErrorString() {
		return errorString;
	}

	public Vector<Integer> getDataSegmentValues() {
		return dataSegment;
	}

	public Vector<Instruction> executeSyntaxAnalyzer() {
		error = false;
		try {
			return runAnalyzer();
		} catch (Exception e) {
			e.printStackTrace();
			syntaxError();
		}
		return null;
	}

	public Vector<Instruction> runAnalyzer() {

		for (int i = 0; i < lexicalTokens.size(); i++) {
			currentInstruction = lexicalTokens.get(i).toString();
			if (lexicalTokens.get(i).possiblyRIType()) {
				String opcode = lexicalTokens.get(i).get(0).getTokenName();
				String operand1 = lexicalTokens.get(i).get(1).getTokenName();
				String operand2 = lexicalTokens.get(i).get(2).getTokenName();
				ArrayList<String> temp = new ArrayList<String>();

				SymbolTable.getInstance();
				if (SymbolTable.isSymbol(operand2)) {
					SymbolTable.getInstance();
					if (SymbolTable.getType(operand2).equals("memory")) {
						dataSegment.add(SymbolTable.getInstance().getValue(
								operand2));
						operand2 = "["
								+ String.valueOf(SymbolTable.getInstance()
										.getLocation(operand2) + 20480) + "]";
					}
				}
				temp.add(opcode);
				temp.add(operand1);
				temp.add(operand2);

				if (checkInstructionSyntax(new RTypeInstruction(temp))) {
					Instruction rtype = new RTypeInstruction(temp);
					codeSegment.add(rtype);
				} else if (checkInstructionSyntax(new ITypeInstruction(temp))) {
					Instruction itype = new ITypeInstruction(temp);
					codeSegment.add(itype);

				} else {
					syntaxError();
				}

			} else if (lexicalTokens.get(i).possiblyJNType()) {

				String opcode = lexicalTokens.get(i).get(0).getTokenName();
				String operand1 = lexicalTokens.get(i).get(1).getTokenName();
				ArrayList<String> temp = new ArrayList<String>();
				String label = operand1;
				SymbolTable.getInstance();
				if (SymbolTable.isSymbol(operand1)) {
					SymbolTable.getInstance();
					if (SymbolTable.getType(operand1).equals("code"))
						operand1 = String.valueOf(SymbolTable.getInstance()
								.getLocation(operand1));
				}
				temp.add(opcode);
				temp.add(operand1);
				temp.add(label);
				if (checkInstructionSyntax(new JTypeInstruction(temp))) {
					Instruction jtype = new JTypeInstruction(temp);
					codeSegment.add(jtype);
				} else if (checkInstructionSyntax(new NULLTypeInstruction(temp))) {
					Instruction nulltype = new NULLTypeInstruction(temp);
					temp.add(String.valueOf(codeSegment.size()));
					codeSegment.add(nulltype);
				}
			} else {
				syntaxError();
			}
		}
		return codeSegment;
	}

	public boolean checkInstructionSyntax(Instruction instruction) {
		return instruction.checkSyntax();
	}

	public void syntaxError() {
		error = true;
		errorString = currentInstruction;
	}

}
