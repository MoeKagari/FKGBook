package show.config;

import tool.MD5;

public class AppConfig {
	public static final String ID_CHINESENAME = "resources\\美少女花骑士角色中文名.csv";
	public static final String CHARACTER_INFORMATION = "resources\\美少女花骑士角色属性.csv";
	public static final String CHARACTER_SKILLTYPE = "resources\\美少女花骑士角色技能类型.csv";
	public static final String SKILL_PANEL_BACKGROUND = "resources\\skill_background";

	private static final String profix = "http://dugrqaqinbtcq.cloudfront.net/product/";
	public static final String CHARACTER_ICON = "resources\\character_image\\icon";
	private static final String CHARACTER_ICON_NETPATH = profix + "images/character/i/";
	public static final String CHARACTER_STAND = "resources\\character_image\\stand";
	private static final String CHARACTER_STAND_NETPATH = profix + "images/character/s/";
	public static final String CHARACTER_STAND_S = "resources\\character_image\\stand_s";
	private static final String CHARACTER_STAND_S_NETPATH = profix + "images/character/s/";

	public static String getCharacterIconNetpath(int id) {
		return CHARACTER_ICON_NETPATH + MD5.getMD5("icon_s_" + id) + ".bin";
	}

	public static String getCharacterStandNetpath(int id) {
		return CHARACTER_STAND_NETPATH + MD5.getMD5("stand_" + id) + ".bin";
	}

	public static String getCharacterStandSNetpath(int id) {
		return CHARACTER_STAND_S_NETPATH + MD5.getMD5("stand_s_" + id) + ".bin";
	}

	/*------------------------------------------------------------------------------------------------------------------*/

	public static final String[] STRING_FAVOR = { "200%", "100%", "000%" };
	private static int favorIndex = 0;

	private static boolean useChineseName = false;
	private static boolean sortByBloomNumber = false;
	private static boolean sortByBid = true;

	private static int oEBIndex = 0;
	private static int rarityIndex = 0;
	private static int aaIndex = 0;
	private static int countryIndex = 0;
	private static int stIndex = 0;

	public static int getFavorIndex() {
		return favorIndex;
	}

	public static void setFavorIndex(int favorIndex) {
		AppConfig.favorIndex = favorIndex;
	}

	public static boolean isUseChineseName() {
		return useChineseName;
	}

	public static void setUseChineseName(boolean useChineseName) {
		AppConfig.useChineseName = useChineseName;
	}

	public static boolean isSortByBloomNumber() {
		return sortByBloomNumber;
	}

	public static void setSortByBloomNumber(boolean sortByBloomNumber) {
		AppConfig.sortByBloomNumber = sortByBloomNumber;
	}

	public static boolean isSortByBid() {
		return sortByBid;
	}

	public static void setSortByBid(boolean sortByBid) {
		AppConfig.sortByBid = sortByBid;
	}

	public static int getOEBIndex() {
		return oEBIndex;
	}

	public static void setOEBIndex(int oEBIndex) {
		AppConfig.oEBIndex = oEBIndex;
	}

	public static int getRarityIndex() {
		return rarityIndex;
	}

	public static void setRarityIndex(int rarityIndex) {
		AppConfig.rarityIndex = rarityIndex;
	}

	public static int getAAIndex() {
		return aaIndex;
	}

	public static void setAAIndex(int aaIndex) {
		AppConfig.aaIndex = aaIndex;
	}

	public static int getCountryIndex() {
		return countryIndex;
	}

	public static void setCountryIndex(int countryIndex) {
		AppConfig.countryIndex = countryIndex;
	}

	public static int getSTIndex() {
		return stIndex;
	}

	public static void setSTIndex(int stIndex) {
		AppConfig.stIndex = stIndex;
	}

}
