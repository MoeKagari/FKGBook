package fkg.masterdata;

public class CharacterSkill implements GetMasterData {
	public int id;
	public String name;
	int c, d, e, f;
	public String effect;
	String h, i, j;
	String time1, time2;
	String m;

	public CharacterSkill(String source) {
		String[] info = source.trim().split(",", -1);

		int index = 0;
		this.id = Integer.parseInt(info[index++]);
		this.name = info[index++];
		this.c = Integer.parseInt(info[index++]);
		this.d = Integer.parseInt(info[index++]);
		this.e = Integer.parseInt(info[index++]);
		this.f = Integer.parseInt(info[index++]);
		this.effect = info[index++];
		this.h = info[index++];
		this.i = info[index++];
		this.j = info[index++];
		this.time1 = info[index++];
		this.time2 = info[index++];
		this.m = info[index++];
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
