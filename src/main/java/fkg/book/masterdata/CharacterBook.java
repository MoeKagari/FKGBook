package fkg.book.masterdata;

import fkg.book.masterdata.GetMasterData.GetMasterDataLineSpliter;

public class CharacterBook {
	int id;
	String name;
	String c, d, e;
	String flowerLanguage;
	int showInBook;
	int hadBloomed;
	String i, j, k;

	public CharacterBook(String source) {
		GetMasterDataLineSpliter gmdls = new GetMasterDataLineSpliter(source);

		this.id = gmdls.nextInt();
		this.name = gmdls.next();
		this.c = gmdls.next();
		this.d = gmdls.next();
		this.e = gmdls.next();
		this.flowerLanguage = gmdls.next();
		this.showInBook = gmdls.nextInt();
		this.hadBloomed = gmdls.nextInt();
		this.i = gmdls.next();
		this.j = gmdls.next();
		this.k = gmdls.next();
	}

	public int getId() {
		return this.id;
	}

	public String getFlowerLanguage() {
		return this.flowerLanguage;
	}

	public boolean showInBook() {
		return this.showInBook == 1;
	}

	public boolean hadBloomed() {
		return this.hadBloomed == 1;
	}
}
