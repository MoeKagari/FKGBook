package tool;

@FunctionalInterface
public interface IndexFinder<S> {

	public boolean match(S s);

	public static <T> int find(T[] ts, IndexFinder<T> infi) {
		for (int i = 0; i < ts.length; i++) {
			if (infi.match(ts[i])) {
				return i;
			}
		}
		return -1;
	}

}
