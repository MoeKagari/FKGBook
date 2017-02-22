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
		id = Integer.parseInt(info[index++]);
		questName = info[index++];
		c = info[index++];
		d = info[index++];
		e = info[index++];
		nextQuestID = Integer.parseInt(info[index++]);
		g = Integer.parseInt(info[index++]);
		h = Integer.parseInt(info[index++]);
		bid = Integer.parseInt(info[index++]);
		j = Integer.parseInt(info[index++]);
		k = Integer.parseInt(info[index++]);
		l = Integer.parseInt(info[index++]);
		m = Integer.parseInt(info[index++]);
		n = info[index++];
		o = info[index++];
		p = Integer.parseInt(info[index++]);
	}

	/*----------------------------------------------------------*/

	public static CharacterQuest getElement(GameData[] cis, int id) {
		for (GameData obj : cis) {
			if (obj instanceof CharacterQuest) {
				CharacterQuest ci = (CharacterQuest) obj;
				if (ci.getID() == id)
					return ci;
			}
		}

		return null;
	}

	public static GameData[] get() {
		return GameData.get(key, source -> new CharacterQuest(source));
	}

	/*----------------------------------------------------------*/
	
	private int getID() {
		return id;
	}
}
