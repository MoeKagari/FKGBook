package show.filter;

import show.CharacterData;

public class CountryFilter implements Filter {
	public static final String[] STRING_COUNTRY = { "��", "�����󥿩`��`��", "�Хʥʥ��`�����", "�֥�å���ҥ�", "�٥륬��åȥХ�`", "��ꥣ���å�", "", "��`�����쥤��" };
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
