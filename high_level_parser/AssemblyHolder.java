package high_level_parser;

/**
 * The class holds the high level token and the assembly code it corresponds to.
 * 
 * @author Chris McCaw
 * @version 1.0
 */
public class AssemblyHolder {

	public String assembly;
	public String highLevel;
	public int LineNum;

	public AssemblyHolder(String assembly, String highLevel, int lineNum) {
		this.assembly = assembly.trim();
		this.highLevel = highLevel.replace(";", "").trim();
		this.LineNum = lineNum;
	}

	@Override
	public String toString() {
		return assembly + " " + highLevel;
	}
}
