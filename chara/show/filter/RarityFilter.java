package show.filter;

import show.CharacterData;

public class RarityFilter implements Filter {
	public static final String[] STRING_RARITY = { "╬▐", "бя2", "бя3", "бя4", "бя5", "бя6" };
	private static final int[] RARITY = { 0, 2, 3, 4, 5, 6 };

	private final int rarity;

	public RarityFilter(int index) {
		this.rarity = RARITY[index];
	}

	@Override
	public boolean filter(CharacterData cd) {
		return rarity == 0 || rarity == cd.getRarity();
	}

}
