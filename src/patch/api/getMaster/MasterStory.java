package patch.api.getMaster;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tool.Downloader;
import tool.FileUtil;
import tool.MD5;
import tool.ZLibUtils;

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
	 * 相应type里面的id
	 */
	private final int idInType;

	public MasterStory(String source) {
		String[] info = source.trim().split(",");

		int index = 0;
		this.id = Integer.parseInt(info[index++]);
		this.type = Integer.parseInt(info[index++]);
		this.idInType = Integer.parseInt(info[index++]);
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

	public int getIdInType() {
		return this.idInType;
	}

	public static void main(String[] args) {
		GameData[] masterStages = MasterStage.get();
		GameData[] characterQuests = CharacterQuest.get();
		int unknown = 1;
		for (GameData gd : get()) {
			MasterStory msterStory = (MasterStory) gd;
			int id = msterStory.id;

			String filepath = "story";
			if (msterStory.type == 2 || msterStory.type == 6) {
				MasterStage masterStage = MasterStage.getElement(masterStages, msterStory.idInType);
				if (masterStage == null) {
					System.out.println("masterStage==null  " + id);
					filepath += "\\" + unknown++;
				} else {
					filepath += "\\stage\\" + masterStage.getGroupNumber() + "\\" + masterStage.getName();
				}
			} else if (msterStory.type == 4) {
				CharacterQuest characterQuest = CharacterQuest.getElement(characterQuests, msterStory.idInType);
				if (characterQuest == null) {
					System.out.println("characterQuest == null  " + id);
					filepath += "\\" + unknown++;
				} else {
					int cid = characterQuest.cid;
					cid -= cid >= 400000 ? 300000 : 0;
					cid -= cid % 2 == 0 ? 1 : 0;
					filepath += "\\characterQuest\\" + cid + "\\" + characterQuest.e + "#" + characterQuest.name;
				}
			} else {
				System.out.println("msterStory.type ==" + msterStory.type);
				continue;
			}

			File file = new File(filepath);
			String dir = file.getParent();
			String filename = file.getName();
			{
				Pattern pattern = Pattern.compile("[\\s\\\\/:\\*\\?\\\"<>\\|]");
				Matcher matcher = pattern.matcher(filename);
				filename = matcher.replaceAll(""); // 将匹配到的非法字符以空替换
			}
			file = new File(dir + "\\" + filename);
			if (file.exists()) continue;

			byte[] bytes = Downloader.download("http://dugrqaqinbtcq.cloudfront.net/product/event/story/" + MD5.getMD5(String.format("story_%06d", id)) + ".bin");
			if (bytes == null) {
				System.out.println(id + " " + 1);
				continue;
			}
			bytes = ZLibUtils.decompress(bytes);
			if (bytes == null) {
				System.out.println(id + " " + 2);
				continue;
			}

			FileUtil.save(file, bytes);
		}
	}

}
