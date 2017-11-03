package fkg.gui.jfx.other.filter;

import fkg.gui.jfx.other.CharacterData;
import fkg.other.StringInteger;

public class RarityFilter implements Filter {
	public static final StringInteger[] SIS = {//
			new StringInteger("无", 0),//
			new StringInteger("★2", 2),//
			new StringInteger("★3", 3),//
			new StringInteger("★4", 4),//
			new StringInteger("★5", 5),//
			new StringInteger("★6", 6),//
	};

	private final int rarity;

	public RarityFilter(int index) {
		this.rarity = SIS[index].getInteger();
	}

	@Override
	public boolean filter(CharacterData cd) {
		return this.rarity == 0 || this.rarity == cd.getRarity();
	}
}
