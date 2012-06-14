package lexical_and_syntax_analysis;

public class TokenNode {

	private String name;
	private String type;

	TokenNode(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public String getTokenName() {
		return name;
	}

	public String getTokenType() {
		return type;
	}

	@Override
	public String toString() {
		return name;
	}

}
