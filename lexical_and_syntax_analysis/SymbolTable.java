package lexical_and_syntax_analysis;

import java.util.HashMap;

public class SymbolTable {

	static HashMap<String, TokenLabel> table = new HashMap<String, TokenLabel>();
	private static SymbolTable instance = null;

	public static void addSymbol(String label, TokenLabel token) {
		table.put(label, token);
	}

	public static int getLocation(String label) {
		return table.get(label).getLocation();
	}

	public static String getType(String label) {
		return table.get(label).getType();
	}

	public static int getValue(String label) {
		return table.get(label).value;
	}

	public static HashMap<String, TokenLabel> getSymbolTable() {
		return table;
	}

	public static boolean isSymbol(String label) {
		return table.containsKey(label);
	}

	public static SymbolTable getInstance() {
		instance = new SymbolTable();
		return instance;
	}

}
