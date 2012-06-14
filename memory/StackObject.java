package memory;

public class StackObject {

	public String label;
	public int value;
	public boolean basepointer = false;
	public boolean returnaddress = false;

	public StackObject(String label, int value) {
		this.label = label;
		this.value = value;
	}

	@Override
	public String toString() {
		if (basepointer) {
			return "Old BP - " + (1000-value);
		}
		if (returnaddress) {
			return "RET";
		}
		if (label.equals(""))
			return value + "";

		return "" + value;
	}

	public boolean isBasePointer() {
		return basepointer;
	}

	public boolean isReturnAddress() {
		return returnaddress;
	}

}
