package high_level_parser;

/**
 * This Dialog class contains information about the creator and the project
 * 
 * @author Chris McCaw
 * @version 1.0
 */
public class VitualStack {
	public String identifier;
	public int value;

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 */
	public VitualStack(String identifier, int value) {
		this.identifier = identifier;
		this.value = value;

	}
}
