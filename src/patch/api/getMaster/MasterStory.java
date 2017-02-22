package patch.api.getMaster;

public class MasterStory implements GameData {
	public static final String key = "masterStory";

	private final int id;
	/**
	 * 目前只有2,4,6 
	 * 2017-1-13 5:12:41
	 *  2:masterStage
	 *  4:masterCharacterQuest
	 *  6:通关某一group后出现的后续剧情
	 */
	private final int type;
	/**
	 * 相应type里面的groupNumber
	 */
	private final int groupNumber;

	public MasterStory(String source) {
		String[] info = source.trim().split(",");

		int index = 0;
		this.id = Integer.parseInt(info[index++]);
		this.type = Integer.parseInt(info[index++]);
		this.groupNumber = Integer.parseInt(info[index++]);
	}

	/*----------------------------------------------------------*/

	public static GameData[] get() {
		return GameData.get(key, MasterStory::new);
	}

	/*----------------------------------------------------------*/

	public int getId() {
		return this.id;
	}

	public int getType() {
		return this.type;
	}

	public String getTypeString() {
		int type = this.getType();
		switch (type) {
			case 2:
				return "EventStory";
			case 4:
				return "CharacterStory";
			case 6:
				return "";
			default:
				return "";
		}
	}

	public int getGroupNumber() {
		return this.groupNumber;
	}

}
