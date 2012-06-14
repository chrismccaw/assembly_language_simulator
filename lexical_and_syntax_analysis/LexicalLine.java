package lexical_and_syntax_analysis;

import java.util.ArrayList;

public class LexicalLine extends ArrayList<TokenNode> {

	public LexicalLine() {
		super();
	}

	public boolean possiblyRIType() {

		return size() == 3;
	}

	@Override
	public String toString() {
		String temp = "";
		for (int i = 0; i < size(); i++) {
			temp += " " + get(i).getTokenName();
		}
		return temp;
	}

	public boolean possiblyJNType() {
		return size() == 2;
	}
}
