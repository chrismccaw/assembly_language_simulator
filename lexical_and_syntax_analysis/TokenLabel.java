package lexical_and_syntax_analysis;

public class TokenLabel {

	private String type;
	private int location;
	public int value;
	public String labelName;

	public TokenLabel(String type, int location, String labelName) {
		this.type = type;
		this.location = location;
		this.labelName = labelName;
	}

	public String getType() {
		return type;
	}

	public int getLocation() {
		return location;
	}

	@Override
	public String toString() {
		return type + " " + location + " " + value;
	}
}
