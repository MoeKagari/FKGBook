package fkg.gui.jfx.other.filter;

import fkg.gui.jfx.other.CharacterData;
import fkg.other.StringInteger;

public class CountryFilter implements Filter {
	public static final StringInteger[] SIS = {//
			new StringInteger("无", 0),//
			new StringInteger("ウィンターローズ", 1),//
			new StringInteger("バナナオーシャン", 2),//
			new StringInteger("ブロッサムヒル", 3),//
			new StringInteger("ベルガモットバレー", 4),//
			new StringInteger("リリィウッド", 5),//
			new StringInteger("", 6),//
			new StringInteger("ロータスレイク", 7),//
	};

	private final int country;

	public CountryFilter(int index) {
		this.country = SIS[index].getInteger();
	}

	@Override
	public boolean filter(CharacterData cd) {
		return this.country == 0 || this.country == cd.getCountry();
	}
}
