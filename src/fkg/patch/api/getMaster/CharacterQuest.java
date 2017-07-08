package fkg.patch.api.getMaster;

public class CharacterQuest implements GetMasterData {
	public static String key = "masterCharacterQuest";

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
		String[] info = source.trim().split(",");

		int index = 0;
		this.id = Integer.parseInt(info[index++]);
		this.name = info[index++];
		this.c = info[index++];
		this.d = info[index++];
		this.index = Integer.parseInt(info[index++]);
		this.nextQuestID = Integer.parseInt(info[index++]);
		this.g = Integer.parseInt(info[index++]);
		this.cid = Integer.parseInt(info[index++]);
		this.bid = Integer.parseInt(info[index++]);
		this.j = Integer.parseInt(info[index++]);
		this.needCharacterLevel = Integer.parseInt(info[index++]);
		this.l = Integer.parseInt(info[index++]);
		this.m = Integer.parseInt(info[index++]);
		this.n = info[index++];
		this.o = info[index++];
		this.p = Integer.parseInt(info[index++]);
	}

	/*----------------------------------------------------------*/

	public int getID() {
		return this.id;
	}
}
