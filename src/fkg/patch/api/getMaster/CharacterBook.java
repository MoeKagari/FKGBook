package fkg.patch.api.getMaster;

public class CharacterBook implements GetMasterData {
	public static final String key = "masterCharacterBook";

	int id;
	String name;
	String c, d, e;
	String flowerLanguage;
	String g, h;
	int showInBook;
	int isBloom;
	String k, l, m;

	public CharacterBook(String source) {
		String[] info = source.trim().split(",");

		int index = 0;
		this.id = Integer.parseInt(info[index++]);
		this.name = info[index++];
		this.c = info[index++];
		this.d = info[index++];
		this.e = info[index++];
		this.flowerLanguage = info[index++];
		this.g = info[index++];
		this.h = info[index++];
		this.showInBook = Integer.parseInt(info[index++]);
		this.isBloom = Integer.parseInt(info[index++]);
		this.k = info[index++];
		this.l = info[index++];
		this.m = info[index++];
	}

	/*----------------------------------------------------------*/

	public int getId() {
		return this.id;
	}

	public boolean showInBook() {
		return this.showInBook == 1;
	}

	public boolean isBloom() {
		return this.isBloom == 1;
	}
}
