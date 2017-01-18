package show.filter;

import show.CharacterData;

public class CountryFilter implements Filter {
	public static final String[] STRING_COUNTRY = { "��", "�����󥿩`��`��", "�Хʥʥ��`�����", "�֥�å���ҥ�", "�٥륬��åȥХ�`", "��ꥣ���å�" };
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
