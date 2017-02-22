package patch.api.getMaster;

public class MasterStage implements GameData {
	public static final String key = "masterStage";

	private final int id;
	private final String name;
	private final int groupNumber;
	private final int staminaCost;
	private final int numberInGroup;
	private final int nextStageID;
	private final String mapBackgroundPictureName;

	public MasterStage(String source) {
		String[] info = source.trim().split(",");

		int index = 0;
		this.id = Integer.parseInt(info[index++]);
		this.name = info[index++];
		this.groupNumber = Integer.parseInt(info[index++]);
		this.staminaCost = Integer.parseInt(info[index++]);
		this.numberInGroup = Integer.parseInt(info[index++]);
		this.nextStageID = Integer.parseInt(info[index++]);
		this.mapBackgroundPictureName = info[index++];
	}

	/*----------------------------------------------------------*/

	public static MasterStage getElement(GameData[] mss, int id) {
		for (GameData obj : mss) {
			if (obj instanceof MasterStage) {
				MasterStage ms = (MasterStage) obj;
				if (ms.getId() == id) return ms;
			}
		}

		return null;
	}

	public static GameData[] get() {
		return GameData.get(key, source -> new MasterStage(source));
	}

	/*----------------------------------------------------------*/

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public int getGroupNumber() {
		return this.groupNumber;
	}

	public String getMapBackgroundPictureName() {
		return this.mapBackgroundPictureName;
	}

	public int getStaminaCost() {
		return this.staminaCost;
	}

	public int getNextStageID() {
		return this.nextStageID;
	}

	public int getNumberInGroup() {
		return numberInGroup;
	}

}
