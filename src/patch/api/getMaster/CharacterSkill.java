package patch.api.getMaster;

public class CharacterSkill implements GameData {
	public static final String key = "masterCharacterSkill";

	int id;
	String name;
	/** 技能类型 技能参数1 技能作用人数 技能参数2 */
	int c, d, e, f;
	String effect;
	String h, i, j;
	String time1, time2;
	String m;

	public CharacterSkill(String source) {
		String[] info = source.trim().split(",");

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

	/*----------------------------------------------------------*/

	public static CharacterSkill getElement(GameData[] css, int id) {
		for (GameData obj : css) {
			if (obj instanceof CharacterSkill) {
				CharacterSkill cs = (CharacterSkill) obj;
				if (cs.getID() == id) return cs;
			}
		}

		return null;
	}

	public static GameData[] get() {
		return GameData.get(key, source -> new CharacterSkill(source));
	}

	/*----------------------------------------------------------*/

	public int getID() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getEffect() {
		return this.effect;
	}

	public int getSkillType() {
		return this.c;
	}

}
