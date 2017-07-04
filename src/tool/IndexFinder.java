package tool;

import java.util.function.Predicate;

public class IndexFinder {
	public static <T> int find(T[] ts, Predicate<T> infi) {
		for (int i = 0; i < ts.length; i++) {
			if (infi.test(ts[i])) {
				return i;
			}
		}
		return -1;
	}
}
