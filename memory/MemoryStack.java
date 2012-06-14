package memory;

import java.util.ArrayList;
import java.util.Observable;

public class MemoryStack extends Observable {

	/**
	 * @uml.property name="stack"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 *                     elementType="memory.StackObject"
	 */
	private ArrayList<StackObject> stack;
	/**
	 * @uml.property name="oldbp"
	 */
	public int oldbp = 0;

	public MemoryStack() {
		stack = new ArrayList<StackObject>();
	}

	public ArrayList<StackObject> stack() {
		return stack;
	}

	public void setValue(String bp, int value) {
		boolean minus = false;
		if (bp.contains("+"))
			bp = bp.replace("+", "&").trim();
		if (bp.contains("-")) {
			bp = bp.replace("-", "&").trim();
			minus = true;
		}

		String[] temp = bp.split("&");
		int index = Integer.parseInt(temp[1].trim());

		int fetch = 0;

		if (minus)
			fetch = oldbp + (index / 2) - 1;
		else
			fetch = (oldbp + 1) / index;

		stack.get(fetch).value = value;
		this.setChanged();
		this.notifyObservers();
	}

	public void updateBP(int newbp) {

		boolean containsBP = false;
		for (int i = 0; i < stack.size(); i++) {
			if (stack.get(i).basepointer) {
				stack.get(i).value = newbp;
				containsBP = true;
				break;
			}
		}
		if (!containsBP) {
			oldbp = newbp;
		}
	}

	public void push(String label, int value) {
		StackObject s = new StackObject(label, value);
		if (label.equals("bp")) {
			oldbp = stack.size() + 1;
			s.basepointer = true;
			push("ra", 0);
		}
		if (label.equals("ra")) {
			s.returnaddress = true;
		}
		stack.add(s);
		this.setChanged();
		this.notifyObservers();
	}

	public StackObject pop() {
		if (!stack.isEmpty()) {
			StackObject temp = stack.get(stack.size() - 1);
			stack.remove(stack.size() - 1);
			this.setChanged();
			this.notifyObservers();
			return temp;
		}
		return null;
	}

	public int getValue(String bp) {
		boolean minus = false;
		if (bp.contains("+"))
			bp = bp.replace("+", "&").trim();
		if (bp.contains("-")) {
			bp = bp.replace("-", "&").trim();
			minus = true;
		}

		String[] temp = bp.split("&");
		int index = Integer.parseInt(temp[1].trim());

		int fetch = 0;

		if (minus)
			fetch = oldbp + (index / 2) - 1;
		else
			fetch = (oldbp + 1) / index;
		return stack.get(fetch).value;
	}

	public void removeStackObjects(int amount) {
		for (int i = 0; i < amount; i++) {
			pop();
		}
	}

	public static void main(String[] args) {
		MemoryStack s = new MemoryStack();
		s.push("x", 10);
		s.push("y", 20);
		s.push("z", 30);
		s.push("bp", 5);
		s.push("i", 1);
		s.push("j", 2);
	}
}
