package high_level_parser;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * In this class syntax, semantic analysis and code generation is performed.
 * Syntax Analysis is the process of combining the tokens into well-formed
 * expressions, statements, and programs. The language I created has specific
 * rules about the structure of a program. The structure is similar to that off
 * Java�s. When the token collection is passed to the syntax analyser a tree
 * is produced which checks for syntactical errors. At this stage Semantic
 * analysis also takes place. Semantic analysis is the process of examining the
 * types and values of the statements used to make sure they make sense. During
 * the semantic analysis, the types, values, and other required information
 * about statements are recorded and checked. The corresponding target code is
 * generated as each token is checked. If there is a syntactical or a semantic
 * error, then the process stops. To evaluate sentences in the language and
 * define a representation for the grammar the interpreter design pattern is
 * used. To handle mathematical expressions Dijkstras Shunting Algorithm is
 * used.
 * 
 * @author Chris McCaw
 * @version 1.0
 * @uml.dependency supplier="high_level_parser.Token"
 */
public class Compiler {

	int index = 0;
	String[] tokens;
	Stack stack = new Stack();
	ArrayList queue = new ArrayList();
	private boolean error = false;
	ArrayList<String> vitualStack = new ArrayList<String>();
	ArrayList<AssemblyHolder> generatedAssembly = new ArrayList<AssemblyHolder>();
	private String errorString = "the beginning";
	private int stackCount = 0;
	private int parameters = 0;
	private int currentLine;

	/**
	 * Initialises the array with tokens generated from the lexical analysis
	 * 
	 * @param tokensFromLexical
	 *            The ArrayList collection which contains tokens
	 */
	public Compiler(ArrayList<String> tokensFromLexical) {

		tokens = new String[tokensFromLexical.size()];

		for (int i = 0; i < tokensFromLexical.size(); i++) {
			tokens[i] = tokensFromLexical.get(i);
		}

	}

	/**
	 * Retrieves the current token within the array
	 * 
	 * @return (string) the current token
	 */
	public String current() {
		String[] split = tokens[index].split(">");
		currentLine = Integer.parseInt(split[0].replace("<line", ""));
		return split[1];
	}

	/**
	 * Retrieves the generated assembly language
	 * 
	 * @return (ArrayList) the collection of intepreted assembly code
	 */
	public ArrayList generatedInstructions() {
		return generatedAssembly;
	}

	/**
	 * Increments the index to get the next token
	 * 
	 * @return (String) The next token
	 */
	public String next() {
		String[] split = tokens[++index].split(">");
		currentLine = Integer.parseInt(split[0].replace("<line", ""));
		return split[1];
	}

	/**
	 * Gets the previous token
	 * 
	 * @return (string) the previous token in the array
	 */
	public String previous() {
		try {
			String[] split = tokens[index - 1].split(">");
			currentLine = Integer.parseInt(split[0].replace("<line", ""));
			return split[1];
		} catch (ArrayIndexOutOfBoundsException a) {
			return tokens[index];
		}
	}

	/**
	 * Resets the compiler back to its original state
	 * 
	 */
	public void reset() {
		error = false;
		errorString = "";
		index = 0;
		stack.clear();
		queue.clear();
	}

	/**
	 * adds the assembly code an related high level code to the
	 * generatedAssembly collection
	 * 
	 * @param instruction
	 *            assembly code
	 * @param highLevel
	 *            The high level code
	 * 
	 */
	public void addToCollection(String instruction, String highLevel) {
		generatedAssembly.add(new AssemblyHolder(instruction, highLevel, currentLine));
	}

	/**
	 * Set to true if the program is at its last token, otherwise false
	 * 
	 * @return (boolean) true, if there are no more tokens
	 */
	public boolean done() {
		return index >= tokens.length - 1;
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public boolean isKeyword(String token) {
		String[] keywords = { "if", "else", "while", "for", "return" };
		for (int i = 0; i < keywords.length; i++) {
			String word = keywords[i];
			if (token.equals(word)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public boolean isType(String token) {
		return token.equals("int") || token.equals("void");
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public boolean isBracket(String token) {
		return token.equals("(") || token.equals(")");
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public boolean isEqualsSign(String token) {
		return token.equals("=");
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public void compile() {
		generatedAssembly.clear();
		if (tokens.length != 0) {
			String[] split = tokens[index].split(">");
			currentLine = Integer.parseInt(split[0].replace("<line", ""));
			errorString = split[1];
		} else {
			error = true;
		}
		while (!done() && !error) {
			try {
				execute();
			} catch (Exception e) {
				System.out.println("Error");
				error = true;
				try {
					String[] split = tokens[index].split(">");
					currentLine = Integer.parseInt(split[0].replace("<line", ""));
					errorString = split[1];
				} catch (ArrayIndexOutOfBoundsException a) {

					String[] split = tokens[index-1].split(">");
					currentLine = Integer.parseInt(split[0].replace("<line", ""));
					errorString = split[1];
				}
				break;
			}
		}
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public void execute() {
		if (isKeyword(current())) {
			statement();
		} else if (isIdentifier(current())) {
			functionCall(current());
		} else if (isType(current())) {
			type(current());
		} else if (isReturn(current())) {
			type(current());
		} else {
			if (current().equals("}")) {
				if (vitualStack.size() > parameters)
					addToCollection("add sp, "
							+ (parameters * 2),
							current());
				addToCollection("pop bp", current());
				addToCollection("ret " + (parameters * 2), current());
				stackCount = 0;
				vitualStack.clear();
			}
			next();
		}
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public boolean isComma(String token) {
		return token.equals(",");
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	private void functionCall(String functionName) {
		String label = next();

		if (isBracket(label)) {
			next();
			while (!isBracket(current())) {
				if (!isComma(current())) {
					addToCollection("push " + current(), current());
					vitualStack.add(current());

				}
				next();
			}
			addToCollection("call " + functionName, "int " + functionName);
		} else if (label.equals("+")) {
			next();
			if (current().equals("+")) {
				addToCollection("mov dx, " + getStackObject(functionName),
						functionName + "++");
				addToCollection("inc dx", functionName + "++");
				addToCollection("mov " + getStackObject(functionName) + ", dx",
						functionName + "++");
			}
		} else if (isEqualsSign(label)) {
			assignment(functionName);
		} else {
			compileError();
		}
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public String getStackObject(String identifier) {
		for (int i = 0; i < vitualStack.size(); i++) {
			String object = vitualStack.get(i);
			if (object.equals(identifier)) {
				if (i < parameters) {
					return "[bp+" + (1 + parameters - i) * 2 + "]";
				}
				if (parameters == 0) {
					return "[bp-" + (1 + i) * 2 + "]";
				} else if (i >= parameters) {
					int index = ((i - parameters) * 2) + 1;
					return "[bp-" + (index + 1) + "]";
				}
			}
		}
		return "";
	}

	/*
	 * public String getStackObject(String identifier){ for(int i = 0; i <
	 * vitualStack.size(); i++){ String object = vitualStack.get(i);
	 * 
	 * if(object.equals(identifier)){ System.out.println(i); return "[bp+" +
	 * (i+2) + "]"; } }
	 * 
	 * for(int j = 0; j < vitualParameter.size(); j++){
	 * if(vitualParameter.get(j).equals(identifier)){ int index = (j+2) * 2;
	 * return "[bp-" + (index) + "]"; } } return ""; }
	 */

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public void type(String type) {

		next();
		if (isIdentifier(current())) {
			String label = current();
			next();
			if (isBracket(current())) {
				function(type, label);
			} else if (current().equals(",")) {
				addToCollection("push " + label, label);
				while (!next().equals(";")) {
					if (!current().equals(",")) {
						addToCollection("push " + current(), current());

					}
				}
			} else if (isEqualsSign(current())) {
				assignment(label);
			} else {
				compileError();
			}
		} else {
			compileError();
		}
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	private boolean isReturn(String token) {
		return token.equals("return");
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	private void function(String type, String label) {
		next();
		if (label.equals("main")) {
			generatedAssembly.add(0, new AssemblyHolder("jmp main", "main()",100));
		}
		addToCollection(label + ":", label);
		if (!label.equals("main")) {
			addToCollection("push bp", label);
			addToCollection("mov bp, sp", label);

		}
		while (!isBracket(current())) {
			if (isIdentifier(current())) {
				vitualStack.add(current());
				parameters++;
				stackCount++;
			}

			next();
		}
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public boolean isIdentifier(String token) {
		Pattern pattern = Pattern.compile("[a-zA-Z]*");
		Matcher matcher = pattern.matcher(token);
		if (matcher.matches() && !isKeyword(token) && !isType(current()))
			return true;
		return false;
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public void assignment(String label) {
		next();
		while (!current().equals(";")) {
			if (!assemblyOperator(current()).equals("")) {
				while (!stack.isEmpty()
						&& !isBracket(current())
						&& (getPrecedence(current()) <= getPrecedence((String) stack
								.peek()))) {
					queue.add(stack.pop());
				}
				stack.push(current());
			} else if (eoe(current())) {

								
				while (!stack.isEmpty() && !stack.peek().equals("(")) {
					queue.add(stack.pop());
				}
				if (stack.peek().equals("(")) {
					stack.pop();
				}

			} else if (!current().equals(")")) {
				queue.add(new Token(current()));
			}
			next();
			if(isKeyword(current()) || isType(current())){
				compileError();
			}
		}
		while (!stack.isEmpty() && !isBracket((String) stack.peek())) {
			queue.add(stack.pop());
		}

		for (int i = 0; i < queue.size(); i++) {
			if (queue.get(i) instanceof Token) {
				
				if (!isConstant(((Token) queue.get(i)).getName())) {
					if (generatedAssembly.get(generatedAssembly.size() - 1).assembly
							.contains("ax"))
						addToCollection(
								"mov dx, "
										+ getStackObject(((Token) queue.get(i))
												.getName()),
								((Token) queue.get(i)).getName());
					else
						addToCollection(
								"mov ax, "
										+ getStackObject(((Token) queue.get(i))
												.getName()),
								((Token) queue.get(i)).getName());
				} else {
					addToCollection(
							"mov ax, " + ((Token) queue.get(i)).getName(),
							((Token) queue.get(i)).getName());
					addToCollection("push ax", ((Token) queue.get(i)).getName());
				}
				// addToCollection("push ax"); moved up
			} else {
				if (!generatedAssembly.get(generatedAssembly.size() - 1).assembly
						.contains("bp"))
					addToCollection("pop dx",
							queue.get(i - 1).toString());

				if (!generatedAssembly.get(generatedAssembly.size() - 3).assembly
						.contains("bp"))
					addToCollection("pop ax",queue.get(i - 1).toString());


				addToCollection(assemblyOperator((String) queue.get(i))
						+ " ax, dx",(String) queue.get(i));
				addToCollection("push ax", label);

			}
		}
		queue.clear();

		if (!vitualStack.contains(label)) {
			vitualStack.add(label);
			stackCount++;
		} else {
			generatedAssembly.remove(generatedAssembly.size() - 1);
			addToCollection("mov " + getStackObject(label) + ", ax",
					getStackObject(label));
		}

	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public int getPrecedence(String operator) {
		if (operator.equals("+")) {
			return 2;
		}
		if (operator.equals("-")) {
			return 2;
		}
		if (operator.equals("*")) {
			return 3;
		}
		if (operator.equals("/")) {
			return 3;
		}
		return 0;
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public boolean eoe(String token) {
		return token.equals(";") || token.equals(")");
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public String assemblyOperator(String operator) {
		if (operator.equals("+")) {
			return "add";
		}
		if (operator.equals("-")) {
			return "sub";
		}
		if (operator.equals("*")) {
			return "mul";
		}
		if (operator.equals("/")) {
			return "div";
		}
		if (operator.equals("(")) {
			return "left";
		}
		return "";
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	private void statement() {

		if (isReturn(current())) {
			assignment(null);
		} else if (ifStatement(current())) {
			ifStatementInstructions();
		} else if (isWhile(current())) {
			whileStatementInstuctions();
		} else if (isFor(current())) {
			forLoopInstuctions();
		} else {
			compileError();
		}
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public void ifStatementInstructions() {
		addToCollection("IF:", "if");
		next();
		String first = next();
		String operator = next();
		String second = next();
		String condition = first + " " + operator + " " + second;
		if (isOperator(second)) {
			operator += second;
			second = next();
		}

		if (!isConstant(first)) {
			// addToCollection("pop dx");
			addToCollection("mov ax, " + getStackObject(first), first);
			first = "ax";
		}
		if (!isConstant(second.trim())) {
			// addToCollection("pop ax");
			addToCollection("mov dx, " + getStackObject(second), second);
			second = "dx";
		}

		addToCollection("cmp " + first + ", " + second, condition);
		addToCollection(getConditionInstruction(operator, "IF"), "if");
		while (!current().equals("}")) {
			execute();
		}
		addToCollection("ENDIF:", "}");
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public boolean isOperator(String token) {
		if (token.equals("<"))
			return true;
		if (token.equals(">"))
			return true;
		if (token.equals("="))
			return true;
		return false;
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public void whileStatementInstuctions() {
		addToCollection("WHILE:", "while");
		next();
		String first = next();
		String operator = next();
		String second = next();
		if (isOperator(second)) {
			operator += second;
			second = next();
		} else {
			compileError();
		}
		String condition = first + " " + operator.replace(" ", "") + " "
				+ second;

		if (!isConstant(first)) {
			addToCollection("mov dx, " + getStackObject(first), first);
			first = "dx";
		}
		if (!isConstant(second.trim())) {
			addToCollection("mov ax, " + getStackObject(second), second);
			second = "ax";
		}

		addToCollection("cmp " + first + ", " + second, condition);
		addToCollection(getConditionInstruction(operator, "WHILE"), "while");
		while (!current().equals("}")) {
			execute();
		}
		addToCollection("jmp WHILE", "while");
		addToCollection("ENDWHILE:", "}");
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public boolean isConstant(String token) {
		try {
			int checkNumberic = Integer.parseInt(token);
			return true;
		} catch (NumberFormatException n) {
			return false;
		}
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public void forLoopInstuctions() {
		next();
		next();
		execute();

		addToCollection("FOR:", "for");

		String first = next();
		String operator = next();
		String second = next();
		String condition = first + " " + operator + " " + second;
		System.out.println("condition " + condition);
		if (!isConstant(first)) {
			addToCollection("mov dx, " + getStackObject(first), first);
			first = "dx";
		}
		if (!isConstant(second.trim())) {
			addToCollection("mov ax, " + getStackObject(second), second);
			second = "ax";
		}
		addToCollection("cmp " + first + ", " + second, condition);
		next();
		execute();
		addToCollection(getConditionInstruction(operator, "FOR"), "for");
		System.out.println("After cpm " + current());
		while (!current().equals("}")) {
			execute();
		}
		addToCollection("jmp FOR", "for");
		addToCollection("ENDFOR:", "}");

	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	private String getConditionInstruction(String operator, String id) {
		if (operator.equals("<")) {
			return "jge END" + id;
		} else if (operator.equals("<=")) {
			return "jg END" + id;
		} else if (operator.equals(">")) {
			return "jl END" + id;
		} else if (operator.equals(">=")) {
			return "jle END" + id;
		} else if (operator.equals("==")) {
			return "jne END" + id;
		} else if (operator.equals("!=")) {
			return "je END" + id;
		}
		return "";
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	private boolean ifStatement(String token) {
		// TODO Auto-generated method stub
		return token.equals("if");
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	private boolean isWhile(String token) {
		// TODO Auto-generated method stub
		return token.equals("while");
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	private boolean isFor(String token) {
		// TODO Auto-generated method stub
		return token.equals("for");
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public void compileError() {
		error = true;
		String[] split = tokens[index-1].split(">");
		currentLine = Integer.parseInt(split[0].replace("<line", ""));
		errorString = split[1];
		System.out.println("Compile Error " + errorString);
	}

	/**
	 * @return
	 * @uml.property name="error"
	 */
	public boolean getError() {
		return error;
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public String errorPointStr() {
		return errorString;
	}

}
