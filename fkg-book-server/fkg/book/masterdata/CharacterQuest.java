package fkg.book.masterdata;

import fkg.book.masterdata.GetMasterData.GetMasterDataLineSpliter;

public class CharacterQuest {
	public int id;
	public String name;
	String c, d;
	/**
	 * 1,2,3,4顺序
	 */
	public int index;
	/**
	 * 0 if not have
	 */
	int nextQuestID;
	int g;
	public int cid;
	int bid;

	int j;
	int needCharacterLevel;
	int l;
	int m;
	String n, o;
	int p;

	public CharacterQuest(String source) {
		GetMasterDataLineSpliter gmdls = new GetMasterDataLineSpliter(source);

		this.id = gmdls.nextInt();
		this.name = gmdls.next();
		this.c = gmdls.next();
		this.d = gmdls.next();
		this.index = gmdls.nextInt();
		this.nextQuestID = gmdls.nextInt();
		this.g = gmdls.nextInt();
		this.cid = gmdls.nextInt();
		this.bid = gmdls.nextInt();
		this.j = gmdls.nextInt();
		this.needCharacterLevel = gmdls.nextInt();
		this.l = gmdls.nextInt();
		this.m = gmdls.nextInt();
		this.n = gmdls.next();
		this.o = gmdls.next();
		this.p = gmdls.nextInt();
	}
}
