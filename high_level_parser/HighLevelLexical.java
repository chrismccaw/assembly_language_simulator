package high_level_parser;

/**
 *  This Dialog class contains information about the creator and the project
 *
 * @author Chris McCaw
 * @version 1.0
 */
import java.util.ArrayList;
import java.util.StringTokenizer;

public class HighLevelLexical {

	private ArrayList<String> tokensContainer;
	private int lineNum;

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public void processProgram(StringBuffer program) {
		tokensContainer = new ArrayList<String>();
		String lineSep = System.getProperty("line.separator");
		StringTokenizer tokens = new StringTokenizer(program.toString(),
				lineSep, true);

		lineNum = 1;
		while (tokens.hasMoreTokens()) {
			
			String token = tokens.nextToken();			
				
			TokeniserLine(token);
			
			if(token.equals("\n"))
				++lineNum;
		}
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public ArrayList<String> lexicalStructure() {
		return tokensContainer;
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public void TokeniserLine(String line) {
		StringTokenizer tokens = new StringTokenizer(line, " {}(),;:+-*%/=",
				true);
		while (tokens.hasMoreTokens()) {

			String token = tokens.nextToken();
			if (token.trim().length() > 0)
				// tokenIdentifier(token.trim());
				tokensContainer.add("<line" + lineNum + ">" +token);
		}
	}

}
