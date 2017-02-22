package show.filter;

import show.CharacterData;

public class CountryFilter implements Filter {
	public static final String[] STRING_COUNTRY = { "ÎÞ", "¥¦¥£¥ó¥¿©`¥í©`¥º", "¥Ð¥Ê¥Ê¥ª©`¥·¥ã¥ó", "¥Ö¥í¥Ã¥µ¥à¥Ò¥ë", "¥Ù¥ë¥¬¥â¥Ã¥È¥Ð¥ì©`", "¥ê¥ê¥£¥¦¥Ã¥É", "", "¥í©`¥¿¥¹¥ì¥¤¥¯" };
	private static final int[] COUNTRY = { 0, 1, 2, 3, 4, 5, 6, 7 };

	private final int country;

	public CountryFilter(int index) {
		this.country = COUNTRY[index];
	}

	@Override
	public boolean filter(CharacterData cd) {
		return this.country == 0 || this.country == cd.getCountryNumber();
	}

}
