package show.filter;

import show.data.CharacterData;

public interface Filter {
	public boolean filter(CharacterData cd);

	public class StringInteger {
		private final String string;
		private final int integer;

		public StringInteger(String string, int integer) {
			this.string = string;
			this.integer = integer;
		}

		public String getString() {
			return string;
		}

		public int getInteger() {
			return integer;
		}
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
