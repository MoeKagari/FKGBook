package show.filter;

import show.CharacterData;

public class CountryFilter implements Filter {
	public static final String[] STRING_COUNTRY = { "ÎÞ", "¥¦¥£¥ó¥¿©`¥í©`¥º", "¥Ð¥Ê¥Ê¥ª©`¥·¥ã¥ó", "¥Ö¥í¥Ã¥µ¥à¥Ò¥ë", "¥Ù¥ë¥¬¥â¥Ã¥È¥Ð¥ì©`", "¥ê¥ê¥£¥¦¥Ã¥É" };
	private static final int[] COUNTRY = { 0, 1, 2, 3, 4, 5 };

	private final int country;

	public CountryFilter(int index) {
		country = COUNTRY[index];
	}

	@Override
	public boolean filter(CharacterData cd) {
		return country == 0 || country == cd.getCountryNumber();
	}

}
