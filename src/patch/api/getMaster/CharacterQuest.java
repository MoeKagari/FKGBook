package patch.api.getMaster;

public class CharacterQuest implements GameData {
	private static String key = "masterCharacterQuest";

	int id;
	String questName;
	String c, d, e;
	/**
	 * 0 if not have
	 */
	int nextQuestID;
	int g;
	int h;
	int bid;
	/**
	 * all 2 , the kind number of story?
	 */
	int j;
	int k;
	int l;
	int m;
	String n, o;
	int p;

	public CharacterQuest(String source) {
		String[] info = source.trim().split(",");

		int index = 0;
		this.id = Integer.parseInt(info[index++]);
		this.questName = info[index++];
		this.c = info[index++];
		this.d = info[index++];
		this.e = info[index++];
		this.nextQuestID = Integer.parseInt(info[index++]);
		this.g = Integer.parseInt(info[index++]);
		this.h = Integer.parseInt(info[index++]);
		this.bid = Integer.parseInt(info[index++]);
		this.j = Integer.parseInt(info[index++]);
		this.k = Integer.parseInt(info[index++]);
		this.l = Integer.parseInt(info[index++]);
		this.m = Integer.parseInt(info[index++]);
		this.n = info[index++];
		this.o = info[index++];
		this.p = Integer.parseInt(info[index++]);
	}

	/*----------------------------------------------------------*/

	public static CharacterQuest getElement(GameData[] cis, int id) {
		for (GameData obj : cis) {
			if (obj instanceof CharacterQuest) {
				CharacterQuest ci = (CharacterQuest) obj;
				if (ci.getID() == id) return ci;
			}
		}

		return null;
	}

	public static GameData[] get() {
		return GameData.get(key, source -> new CharacterQuest(source));
	}

	/*----------------------------------------------------------*/

	private int getID() {
		return this.id;
	}
}
