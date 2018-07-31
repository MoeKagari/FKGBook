package fkg.book.masterdata;

import java.util.Arrays;

import fkg.book.masterdata.GetMasterData.GetMasterDataLineSpliter;

public class CharacterSkill {
	public int id;
	public String name;
	String c, d, e, f;
	public String effect;
	String h, i, j;
	String k;
	String time1, time2;
	String n;

	public CharacterSkill(String source) {
		GetMasterDataLineSpliter gmdls = new GetMasterDataLineSpliter(source);

		this.id = gmdls.nextInt();
		this.name = gmdls.next();
		this.c = gmdls.next();
		this.d = gmdls.next();
		this.e = gmdls.next();
		this.f = gmdls.next();
		this.effect = gmdls.next();
		this.h = gmdls.next();
		this.i = gmdls.next();
		this.j = gmdls.next();
		this.k = gmdls.next();
		this.time1 = gmdls.next();
		this.time2 = gmdls.next();
		this.n = gmdls.next();
	}

	@Override
	public String toString() {
		return Arrays.toString(new String[] { this.c, this.d, this.e, this.f }) + "," + Arrays.toString(new String[] { this.h, this.i, this.j });
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
