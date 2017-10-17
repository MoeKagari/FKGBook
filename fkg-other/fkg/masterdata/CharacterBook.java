package fkg.masterdata;

public class CharacterBook implements GetMasterData {
	int id;
	String name;
	String c, d, e;
	String flowerLanguage;
	int showInBook;
	int hadBloomed;
	String i, j, k;

	public CharacterBook(String source) {
		String[] info = source.trim().split(",", -1);

		int index = 0;
		this.id = Integer.parseInt(info[index++]);
		this.name = info[index++];
		this.c = info[index++];
		this.d = info[index++];
		this.e = info[index++];
		this.flowerLanguage = info[index++];
		this.showInBook = Integer.parseInt(info[index++]);
		this.hadBloomed = Integer.parseInt(info[index++]);
		this.i = info[index++];
		this.j = info[index++];
		this.k = info[index++];
	}

	/*----------------------------------------------------------*/

	public int getId() {
		return this.id;
	}

	public boolean showInBook() {
		return this.showInBook == 1;
	}

	public boolean hadBloomed() {
		return this.hadBloomed == 1;
	}
}
