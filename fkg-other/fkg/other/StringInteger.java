package fkg.other;

public class StringInteger {
	private final String string;
	private final int integer;

	public StringInteger(int integer, String string) {
		this.string = string;
		this.integer = integer;
	}

	public StringInteger(String string, int integer) {
		this.string = string;
		this.integer = integer;
	}

	public String getString() {
		return this.string;
	}

	public int getInteger() {
		return this.integer;
	}
}
