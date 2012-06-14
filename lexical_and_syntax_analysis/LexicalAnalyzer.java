package lexical_and_syntax_analysis;

import instructions.InstructionStore;

import java.util.StringTokenizer;
import java.util.Vector;

public class LexicalAnalyzer {

	private StringBuffer currentProgram;
	private boolean correct;
	private Vector<LexicalLine> nodes;
	private int line = 0;
	private int dataLabel = 1;
	private int dataCount = 1;
	private boolean error = false;
	private String errorString;
	private String currentInstruction;

	public LexicalAnalyzer() {
		correct = true;
		nodes = new Vector<LexicalLine>();
	}

	public boolean getError() {
		return error;
	}

	public String getErrorString() {
		return errorString;
	}

	public void loadAssemblyFile(StringBuffer currentProgram) {
		this.currentProgram = currentProgram;
	}

	public Vector<LexicalLine> getLexicalNodes() {
		return nodes;
	}

	/**
	 * @return
	 * @uml.property name="correct"
	 */
	public boolean isCorrect() {
		return correct;
	}

	public boolean isRegister(String token) {
		if (token.equals("ax") || token.equals("bx") || token.equals("cx")
				|| token.equals("dx") || token.equals("bp")
				|| token.equals("sp") || token.equals("di"))
			return true;
		return false;
	}

	public boolean isConstant(String token) {
		try {
			int checkNumberic = Integer.parseInt(token);

		} catch (NumberFormatException n) {
			return false;
		}
		return true;
	}

	public void runAnalyzer() {
		error = false;
		String lineSep = System.getProperty("line.separator");
		StringTokenizer tokens = new StringTokenizer(currentProgram.toString(),
				lineSep);

		while (tokens.hasMoreTokens() && !error) {
			LexicalLine t = null;
			String token = tokens.nextToken().trim();
			if (token.contains("[") && token.contains("]")) {
				int start = token.indexOf(" ");
				String opcodeString = token.substring(0, start);
				String restOfString = token.substring(start, token.length())
						.replaceAll(" ", "");
				token = opcodeString + " " + restOfString;
			}

			try {
				t = tokenizeInstruction(token.trim());
				if (!t.isEmpty()) {
					line++;
					nodes.add(t);
				}
			} catch (Exception e) {
				e.printStackTrace();
				error = true;
				errorString = token;
			}

		}
	}

	public LexicalLine tokenizeInstruction(String token) {
		LexicalLine tokenContainer = new LexicalLine();
		StringTokenizer tokens = new StringTokenizer(token, " ;,<>{}()");
		int tokenNumber = 1;
		while (tokens.hasMoreTokens()) {
			String name = tokens.nextToken().trim();
			currentInstruction = name;
			String type = getType(name);
			if (tokenNumber == 1 && type.equals("label") && name.contains(":")) {
				SymbolTable.getInstance();
				SymbolTable.addSymbol(name.replace(":", ""), new TokenLabel(
						"code", line, name.replace(":", "")));
			} else if (tokenNumber == 1 && type.equals("label")) {
				TokenLabel label = new TokenLabel("memory", dataCount, name);
				String labeltype = tokens.nextToken();
				if (isRegister(labeltype) || isMemoryLocation(labeltype)) {
					lexicalError();
				}
				label.value = (Integer.parseInt(tokens.nextToken()));
				SymbolTable.getInstance();
				SymbolTable.addSymbol(name, label);
				dataCount++;
			} else {
				tokenContainer.add(new TokenNode(name, type));
			}
			tokenNumber++;
		}
		return tokenContainer;
	}

	public String getType(String token) {

		if (isConstant(token)) {
			return "constant";
		} else if (isLabel(token)) {
			return "label";
		} else if (isRegister(token)) {
			return "register";
		} else if (isKeyword(token)) {
			return "keyword";
		} else if (isMemoryLocation(token)) {
			return "memory";
		}
		return "";
	}

	private boolean isMemoryLocation(String token) {
		return token.contains("[") && token.contains("]");
	}

	public boolean isKeyword(String token) {
		String[] allInstructions = InstructionStore.getInstance()
				.allInstructions();
		for (int i = 0; i < allInstructions.length; i++) {
			if (allInstructions[i].equals(token)) {
				return true;
			}

		}
		return false;
	}

	public boolean isLabel(String token) {
		boolean isNumber = true;
		try {
			int n = Integer.parseInt(token);
		} catch (NumberFormatException e) {
			isNumber = false;
		}
		if (!isRegister(token) && !isNumber && !isKeyword(token))
			return true;
		return false;
	}

	public void lexicalError() {
		error = true;
		errorString = currentInstruction;
		System.out.println("Lexical Error");
	}

	public void reset() {
		nodes.clear();
		dataCount = 0;
		line = 0;
	}

}
