package patch.api.getMaster;

public class CharacterLeaderSkill implements GameData {
	public static final String key = "masterCharacterLeaderSkill";

	int id;
	String name;
	/** ��ϼ���1���� */
	int c;
	/** ��ϼ���1����1 */
	int d;
	/** ��ϼ���1�������� */
	int e;
	/** ��ϼ���1����2 */
	int f;
	/** ��ϼ���2 */
	int g, h, i, j;
	String effect;
	String time1, time2;
	/** all 0 */
	String n;

	public CharacterLeaderSkill(String source) {
		String[] info = source.trim().split(",");

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
		this.effect = info[index++];
		this.time1 = info[index++];
		this.time2 = info[index++];
		this.n = info[index++];
	}

	/*----------------------------------------------------------*/

	public static CharacterLeaderSkill getElement(GameData[] clss, int id) {
		for (GameData obj : clss) {
			if (obj instanceof CharacterLeaderSkill) {
				CharacterLeaderSkill cls = (CharacterLeaderSkill) obj;
				if (cls.getID() == id) return cls;
			}
		}

		return null;
	}

	public static GameData[] get() {
		return GameData.get(key, source -> new CharacterLeaderSkill(source));
	}

	/*----------------------------------------------------------*/

	public int getID() {
		return this.id;
	}

	public String getEffect() {
		return this.effect;
	}

	public int[] getSkillType() {
		return new int[] { this.c, this.g };
	}

}
