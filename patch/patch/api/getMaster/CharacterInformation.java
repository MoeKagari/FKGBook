package patch.api.getMaster;

public class CharacterInformation implements GameData {
	public static final String key = "masterCharacter";

	int cid1, cid2;
	/**
	 * 科目,masterCharacterCategory
	 */
	int category;
	/**
	 * 国家<br>
	 * if 0，无国家所属<br>
	 * if 1,ウィンターローズ<br>
	 * if 2,バナナオーシャン<br>
	 * if 3,ブロッサムヒル<br>
	 * if 4,ベルガモットバレー<br>
	 * if 5,リリィウッド
	 */
	int country;
	int e;// ?????????
	/**
	 * 花名，不同于角色名
	 */
	String flower_name;
	String characterExplain;// ???????????
	/**
	 * 稀有度
	 */
	int rarity;
	/**
	 * 攻击属性<br>
	 * if 1,斩属性<br>
	 * if 2,打属性<br>
	 * if 3,突属性<br>
	 * if 4,魔属性<br>
	 * if 5,无属性
	 */
	int attackAttribute;
	String j;// ????????????
	/**
	 * 被动技能1，被动技能2，主动技能 CharacterLeaderSkill,CharacterLeaderSkill,CharacterSkill
	 */
	public int passiveSkill1, passiveSkill2, activeSkill;
	/**
	 * all 0,除开5种属性的鲸鱼飞艇
	 */
	String n;// 鲸鱼相关
	String o;//游戏中未用
	int minLevelHP, maxLevelHP;
	int minLevelAttack, maxLevelAttack;
	int minLevelDefense, maxLevelDefense;
	int minLevelMove, maxLevelMove;
	/**
	 * x,y,z<br>
	 * 三种灵兽分别增加的HP，攻击力，防御上限值4000，1000，400<br>
	 * 只对于角色而言，即isCharacter == 1<br>
	 */
	String x, y, z;
	/**
	 * 出售获得的金币值
	 */
	int worthGold;
	/**
	 * masterCharacterLevel,第二列<br>
	 * 升级经验，被合成经验<br>
	 * 1，3，5，7，9，分别为2，3，4，5，6星未进化<br>
	 * 2，4，6，8，10，分别为2，3，4，5，6星进化<br>
	 * 11，12，分别为5，6星开花<br>
	 * 101，5岁强化灵和3种灵兽、活动角色技花<br>
	 * 102，20岁强化灵、活动角色装花<br>
	 * 103，100岁强化灵<br>
	 * 201，全部进化龙<br>
	 * 202，无<br>
	 * 300，5星装花、6星装花、结晶交换的4种技花<br>
	 * 400，开花材料<br>
	 * 203，5种属性鲸鱼飞艇
	 */
	String ab;
	/**
	 * 仅对于isCharacter == 1 而言<br>
	 * 角色序号
	 */
	String ac;
	/**
	 * 是否为未进化角色<br>
	 * if 0，为未进化角色
	 */
	int beforeEvolution;
	/**
	 * if 0,特殊角色,如:进化龙、开花材料、三灵兽,etc<br>
	 * if 1,角色
	 */
	int isCharacter;
	int favorHP, favorAttack, favorDefense;
	/**
	 * book id
	 */
	private int bid;
	/**
	 * if 1,原始角色<br>
	 * if 2,进化角色<br>
	 * if 3,开花角色
	 */
	int oeb;
	/**
	 * 现和 isCharacter 一样<br>
	 * 现和 ap 一样
	 */
	String ak;
	/**
	 * 对于角色而言<br>
	 * 0，1，2，3，4分别为2，3，4，5，6星<br>
	 * 对于非角色而言, 只有一个值：0
	 */
	String al;
	int secondFavorHP, secondFavorAttack, secondFavorDefense;
	/**
	 * 现和 isCharacter 一样<br>
	 * 现和 ak 一样
	 */
	String ap;
	/**
	 * 开花序号，从1开始,仅对于进化角色，并且其 hasBloom == 1
	 */
	int bloomNumber;
	/**
	 * 是否为开花角色<br>
	 * if 1，为开花角色<br>
	 * if 0，不为开花角色
	 */
	String ar;
	/**
	 * 1为有开花<br>
	 * 0为无开花<br>
	 * 仅对于进化色色而言，即 oeb == 2
	 */
	int hasBloom;
	/**
	 * 角色名，不同于花名
	 */
	String character_name;
	String time1, time2;
	/**
	 * all 0, no use?
	 */
	String aw;
	String version;

	public CharacterInformation(String source) {
		String[] info = source.trim().split(",");

		int index = 0;
		this.cid1 = Integer.parseInt(info[index++]);
		this.cid2 = Integer.parseInt(info[index++]);
		this.category = Integer.parseInt(info[index++]);
		this.country = Integer.parseInt(info[index++]);
		this.e = Integer.parseInt(info[index++]);
		this.flower_name = info[index++];
		this.characterExplain = info[index++];
		this.rarity = Integer.parseInt(info[index++]);
		this.attackAttribute = Integer.parseInt(info[index++]);
		this.j = info[index++];
		this.passiveSkill1 = Integer.parseInt(info[index++]);
		this.passiveSkill2 = Integer.parseInt(info[index++]);
		this.activeSkill = Integer.parseInt(info[index++]);
		this.n = info[index++];
		this.o = info[index++];
		this.minLevelHP = Integer.parseInt(info[index++]);
		this.maxLevelHP = Integer.parseInt(info[index++]);
		this.minLevelAttack = Integer.parseInt(info[index++]);
		this.maxLevelAttack = Integer.parseInt(info[index++]);
		this.minLevelDefense = Integer.parseInt(info[index++]);
		this.maxLevelDefense = Integer.parseInt(info[index++]);
		this.minLevelMove = Integer.parseInt(info[index++]);
		this.maxLevelMove = Integer.parseInt(info[index++]);
		this.x = info[index++];
		this.y = info[index++];
		this.z = info[index++];
		this.worthGold = Integer.parseInt(info[index++]);
		this.ab = info[index++];
		this.ac = info[index++];
		this.beforeEvolution = Integer.parseInt(info[index++]);
		this.isCharacter = Integer.parseInt(info[index++]);
		this.favorHP = Integer.parseInt(info[index++]);
		this.favorAttack = Integer.parseInt(info[index++]);
		this.favorDefense = Integer.parseInt(info[index++]);
		this.bid = (Integer.parseInt(info[index++]));
		this.oeb = Integer.parseInt(info[index++]);
		this.ak = info[index++];
		this.al = info[index++];
		this.secondFavorHP = Integer.parseInt(info[index++]);
		this.secondFavorAttack = Integer.parseInt(info[index++]);
		this.secondFavorDefense = Integer.parseInt(info[index++]);
		this.ap = info[index++];
		this.bloomNumber = Integer.parseInt(info[index++]);
		this.ar = info[index++];
		this.hasBloom = Integer.parseInt(info[index++]);
		this.character_name = info[index++];
		this.time1 = info[index++];
		this.time2 = info[index++];
		this.aw = info[index++];
		this.version = info[index++];
	}

	/*----------------------------------------------------------*/

	public static CharacterInformation getElement(GameData[] cis, int id) {
		for (GameData obj : cis) {
			if (obj instanceof CharacterInformation) {
				CharacterInformation ci = (CharacterInformation) obj;
				if (ci.getID() == id) return ci;
			}
		}

		return null;
	}

	public static GameData[] get() {
		return GameData.get(key, CharacterInformation::new);
	}

	/*----------------------------------------------------------*/

	/**
	 * 国家<br>
	 * if 0，无国家所属<br>
	 * if 1,ウィンターローズ<br>
	 * if 2,バナナオーシャン<br>
	 * if 3,ブロッサムヒル<br>
	 * if 4,ベルガモットバレー<br>
	 * if 5,リリィウッド
	 */
	public String getCountry() {
		String[] c = { "无", "ウィンターローズ", "バナナオーシャン", "ブロッサムヒル", "ベルガモットバレー", "リリィウッド" };

		return c[this.country];
	}

	public int getCountryNumber() {
		return this.country;
	}

	public int getAttackAttributeNumber() {
		return this.attackAttribute;
	}

	public String getAttackAttribute() {
		final String[] aa = new String[] { "斩", "打", "突", "魔", "无" };
		return aa[this.attackAttribute - 1];
	}

	public String getName() {
		return this.character_name;
	}

	public int getID() {
		return this.cid1;
	}

	public int getOeb() {
		return this.oeb;
	}

	public boolean isCharacter() {
		return this.isCharacter == 1;
	}

	public GameData[] getSkill(GameData[] css, GameData[] clss) {
		return new GameData[] { CharacterLeaderSkill.getElement(clss, this.passiveSkill1), CharacterLeaderSkill.getElement(clss, this.passiveSkill2), CharacterSkill.getElement(css, this.activeSkill) };
	}

	public int[] getHP() {
		return new int[] { this.maxLevelHP, this.favorHP, this.secondFavorHP };
	}

	public int[] getAttack() {
		return new int[] { this.maxLevelAttack, this.favorAttack, this.secondFavorAttack };
	}

	public int[] getDefense() {
		return new int[] { this.maxLevelDefense, this.favorDefense, this.secondFavorDefense };
	}

	public int[] getPower() {
		return new int[] { this.maxLevelHP + this.maxLevelAttack + this.maxLevelDefense, this.favorHP + this.favorAttack + this.favorDefense, this.secondFavorHP + this.secondFavorAttack + this.secondFavorDefense };
	}

	public int getMove() {
		return this.minLevelMove;
	}

	public boolean hasBloom() {
		return this.hasBloom == 1;
	}

	public int getRarity() {
		return this.rarity;
	}

	public int getBloomNumber() {
		return this.bloomNumber;
	}

	public int getBid() {
		return this.bid;
	}

}
