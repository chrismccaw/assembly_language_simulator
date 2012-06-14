package assignment;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class AssignmentFillIn {

	private ArrayList<AssignmentToken> answers;

	public AssignmentFillIn() {
		answers = new ArrayList<AssignmentToken>();
	}

	public void addAnswer(String answer, int line) {
		answers.add(new AssignmentToken(answer, line));
	}

	public String formatInstruction(String instruction) {
		try {
			int start = instruction.trim().indexOf(" ");
			String opcodeString = instruction.substring(0, start);
			String restOfString = instruction.substring(start,
					instruction.length()).replaceAll(" ", "");
			return opcodeString + " " + restOfString;
		} catch (StringIndexOutOfBoundsException s) {
			return instruction;
		}
	}

	public String WronglineNumbers(StringBuffer program) {
		String lineSep = System.getProperty("line.separator");
		StringTokenizer tokens = new StringTokenizer(program.toString(),
				lineSep);
		int lineNo = 0;
		String wrongLines = "";
		while (tokens.hasMoreTokens()) {
			String lineInstruction = formatInstruction(tokens.nextToken()
					.trim());
			if (isAssessedLine(lineNo)) {
				String correctAnswer = formatInstruction(getAnswer(lineNo)
						.trim());
				if (!lineInstruction.equals(correctAnswer)) {
					wrongLines += (lineNo + 1) + ",";
				}
			}

			lineNo++;
		}
		return wrongLines.substring(0, wrongLines.length() - 1);
	}

	public boolean checkAnswers(StringBuffer program) {
		String lineSep = System.getProperty("line.separator");
		StringTokenizer tokens = new StringTokenizer(program.toString(),
				lineSep);
		int lineNo = 0;
		boolean allCorrect = true;
		while (tokens.hasMoreTokens()) {
			String lineInstruction = tokens.nextToken().trim();
			if (isAssessedLine(lineNo)) {
				String correctAnswer = getAnswer(lineNo);
				if (lineInstruction.equals(correctAnswer)) {
				} else {
					allCorrect = false;
					System.out.println("Line " + lineNo
							+ " is WRONG - Should be " + correctAnswer);
				}
			}
			lineNo++;
		}

		if (allCorrect)
			return true;
		return false;
	}

	private boolean isAssessedLine(int lineNo) {
		for (int i = 0; i < answers.size(); i++) {

			if (lineNo == answers.get(i).line) {
				return true;
			}
		}
		return false;
	}

	public String getAnswer(int line) {
		for (int i = 0; i < answers.size(); i++) {
			AssignmentToken temp = answers.get(i);
			if (temp.line == line) {
				return temp.answer;
			}
		}
		return "";
	}

	public void reset() {
		answers.clear();

	}
}
