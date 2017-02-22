package show.filter;

import show.CharacterData;

public class AttackAttributeFilter implements Filter {
	public static final String[] STRING_ATTRIBUTE = { "ÎÞ", "Õ¶", "´ò", "Í»", "Ä§" };
	private static final int[] ATTRIBUTE = { 0, 1, 2, 3, 4 };

	private final int attribute;

	public AttackAttributeFilter(int index) {
		this.attribute = ATTRIBUTE[index];
	}

	@Override
	public boolean filter(CharacterData cd) {
		return attribute == 0 || attribute == cd.getAttackAttributeNumber();
	}

}
