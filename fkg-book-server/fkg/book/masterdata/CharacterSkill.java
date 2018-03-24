package fkg.book.masterdata;

import fkg.book.masterdata.GetMasterData.GetMasterDataLineSpliter;

public class CharacterSkill {
	public int id;
	public String name;
	int c, d, e, f;
	public String effect;
	String h, i, j;
	String time1, time2;
	String m;

	public CharacterSkill(String source) {
		GetMasterDataLineSpliter gmdls = new GetMasterDataLineSpliter(source);

		this.id = gmdls.nextInt();
		this.name = gmdls.next();
		this.c = gmdls.nextInt();
		this.d = gmdls.nextInt();
		this.e = gmdls.nextInt();
		this.f = gmdls.nextInt();
		this.effect = gmdls.next();
		this.h = gmdls.next();
		this.i = gmdls.next();
		this.j = gmdls.next();
		this.time1 = gmdls.next();
		this.time2 = gmdls.next();
		this.m = gmdls.next();
	}

	public int getID() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getEffect() {
		return this.effect;
	}
}
