package fkg.book.masterdata;

import fkg.book.masterdata.GetMasterData.GetMasterDataLineSpliter;

public class CharacterLeaderSkill {
	int id;
	String name;
	int c, d, e, f, g;
	int h, i, j, k, l;
	int m, n, o, p, q;
	String r;
	String time1, time2;
	String u;

	public CharacterLeaderSkill(String source) {
		GetMasterDataLineSpliter gmdls = new GetMasterDataLineSpliter(source);

		this.id = gmdls.nextInt();
		this.name = gmdls.next();

		this.c = gmdls.nextInt();
		this.d = gmdls.nextInt();
		this.e = gmdls.nextInt();
		this.f = gmdls.nextInt();
		this.g = gmdls.nextInt();

		this.h = gmdls.nextInt();
		this.i = gmdls.nextInt();
		this.j = gmdls.nextInt();
		this.k = gmdls.nextInt();
		this.l = gmdls.nextInt();

		this.m = gmdls.nextInt();
		this.n = gmdls.nextInt();
		this.o = gmdls.nextInt();
		this.p = gmdls.nextInt();
		this.q = gmdls.nextInt();

		this.r = gmdls.next();
		this.time1 = gmdls.next();
		this.time2 = gmdls.next();
		this.u = gmdls.next();
	}
}
