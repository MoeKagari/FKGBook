package fkg.gui.book.filter;

import fkg.gui.book.data.CharacterData;

public class AttackAttributeFilter implements Filter {
	public static final StringInteger[] SIS = {//
			new StringInteger("无", 0),//
			new StringInteger("斩", 1),//
			new StringInteger("打", 2),//
			new StringInteger("突", 3),//
			new StringInteger("魔", 4),//
	};

	private final int attribute;

	public AttackAttributeFilter(int index) {
		this.attribute = SIS[index].getInteger();
	}

	@Override
	public boolean filter(CharacterData cd) {
		return this.attribute == 0 || this.attribute == cd.ci.getAttackAttributeNumber();
	}
}
