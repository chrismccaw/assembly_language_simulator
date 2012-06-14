package high_level_parser;

import java.util.ArrayList;

public class HighLevelCompiler {

	private HighLevelLexical lexical;
	private Compiler c;

	public HighLevelCompiler(StringBuffer program) {

		lexical = new HighLevelLexical();

		lexical.processProgram(program);
		c = new Compiler(lexical.lexicalStructure());
		c.compile();
	}

	public ArrayList<AssemblyHolder> compiledInstructions() {
		return c.generatedAssembly;
	}

	public Compiler getCompiler() {
		return c;
	}
}
