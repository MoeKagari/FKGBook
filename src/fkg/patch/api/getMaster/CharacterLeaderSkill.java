package fkg.patch.api.getMaster;

import java.util.List;

public class CharacterLeaderSkill implements GetMasterData {
	public static final String key = "masterCharacterLeaderSkill";

	int id;
	String name;
	/** 组合技能1类型 */
	int c;
	/** 组合技能1参数1 */
	int d;
	/** 组合技能1作用人数 */
	int e;
	/** 组合技能1参数2 */
	int f;
	/** 组合技能2 */
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

	public static CharacterLeaderSkill getElement(List<GetMasterData> clss, int id) {
		for (GetMasterData obj : clss) {
			if (obj instanceof CharacterLeaderSkill) {
				CharacterLeaderSkill cls = (CharacterLeaderSkill) obj;
				if (cls.getID() == id) return cls;
			}
		}
		return null;
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
