package fkg.masterdata;

public class CharacterLeaderSkill implements GetMasterData {
	int id;
	String name;
	int c, d, e, f, g;
	int h, i, j, k, l;
	int m, n, o, p, q;
	String r;
	String time1, time2;
	/** all 0 */
	String u;

	public CharacterLeaderSkill(String source) {
		String[] info = source.trim().split(",", -1);
		int index = 0;

		this.id = Integer.parseInt(info[index++]);
		this.name = info[index++];

		this.c = Integer.parseInt(info[index++]);
		this.d = Integer.parseInt(info[index++]);
		this.e = Integer.parseInt(info[index++]);
		this.f = Integer.parseInt(info[index++]);
		this.g = Integer.parseInt(info[index++]);

		this.h = Integer.parseInt(info[index++]);
		this.i = Integer.parseInt(info[index++]);
		this.j = Integer.parseInt(info[index++]);
		this.k = Integer.parseInt(info[index++]);
		this.l = Integer.parseInt(info[index++]);

		this.m = Integer.parseInt(info[index++]);
		this.n = Integer.parseInt(info[index++]);
		this.o = Integer.parseInt(info[index++]);
		this.p = Integer.parseInt(info[index++]);
		this.q = Integer.parseInt(info[index++]);

		this.r = info[index++];
		this.time1 = info[index++];
		this.time2 = info[index++];
		this.u = info[index++];
	}
}
