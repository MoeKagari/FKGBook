package fkg.config;

import tool.compress.MD5;

public class ShowConfig {
	public static final String ID_CHINESENAME = "resources\\美少女花骑士角色中文名.csv";
	public static final String CHARACTER_SKILLTYPE = "resources\\美少女花骑士角色技能类型.csv";

	private static final String NETPATH_profix = "http://dugrqaqinbtcq.cloudfront.net/product/";
	public static final String CHARACTER_VOICE = "resources\\home\\voice";
	public static final String CHARACTER_HOME = "resources\\character_image\\home";
	public static final String CHARACTER_ICON = "resources\\character_image\\icon";
	public static final String CHARACTER_STAND = "resources\\character_image\\stand";
	public static final String CHARACTER_STAND_S = "resources\\character_image\\stand_s";
	public static final String CHARACTER_STORY = "resources\\character_story";

	public static String getCharacterVoiceNetpath(int id, String voice_file) {
		voice_file = voice_file.split("\\.")[0];
		return NETPATH_profix + "voice/c/" + id + "/" + MD5.getMD5(voice_file) + ".bin";
	}

	public static String getCharacterHomeNetpath(int id, int expressionType) {
		return NETPATH_profix + "images/character/s/" + MD5.getMD5("home_" + id + (expressionType == 0 ? "" : ("_0" + expressionType))) + ".bin";
	}

	public static String getCharacterIconNetpath(int id) {
		return NETPATH_profix + "images/character/i/" + MD5.getMD5("icon_s_" + id) + ".bin";
	}

	public static String getCharacterStandNetpath(int id) {
		return NETPATH_profix + "images/character/s/" + MD5.getMD5("stand_" + id) + ".bin";
	}

	public static String getCharacterStandSNetpath(int id) {
		return NETPATH_profix + "images/character/s/" + MD5.getMD5("stand_s_" + id) + ".bin";
	}

	/** 非角色ID,而是角色的个人剧情对应的MasterStory里面的ID */
	public static String getCharacterStoryNetpath(int id) {
		return NETPATH_profix + "event/story/" + MD5.getMD5(String.format("story_%06d", id)) + ".bin";
	}
}
