package high_level_parser;

/**
 * This Dialog class contains information about the creator and the project
 * 
 * @author Chris McCaw
 * @version 1.0
 */
public class Token {

	private String name;

	public Token(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

}
