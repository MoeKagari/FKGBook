package patch.api.getMaster;

public class CharacterCategory implements GameData {
	public static final String key = "masterCharacterCategory";
	
	int id;
	/**
	 * 花的科目
	 */
	String family;
	int isCharacter;
	String d,e,f;
	
	public CharacterCategory(String source) {
		String[] info = source.trim().split(",");

		int index = 0;
		id = Integer.parseInt(info[index++]);
		family = info[index++];
		isCharacter = Integer.parseInt(info[index++]);
		d = info[index++];
		e = info[index++];
		f = info[index++];
	}
	
	/*----------------------------------------------------------*/

	public static CharacterCategory getElement(GameData[] cis, int id) {
		for (GameData obj : cis) {
			if (obj instanceof CharacterCategory) {
				CharacterCategory ci = (CharacterCategory) obj;
				if (ci.getID() == id)
					return ci;
			}
		}

		return null;
	}

	public static GameData[] get() {
		return GameData.get(key, source -> new CharacterCategory(source));
	}

	/*----------------------------------------------------------*/
	
	public int getID(){
		return id;
	}
	
	public String getFamily(){
		return family;
	}
	
	public boolean isCharacter(){
		return isCharacter == 0;
	}

}
