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

	public static String[] toStringArray(StringInteger[] sis) {
		int len = sis.length;
		String[] ss = new String[len];
		for (int i = 0; i < len; i++) {
			ss[i] = sis[i].string;
		}
		return ss;
	}
}
