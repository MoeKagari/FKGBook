package fkg.book.masterdata;

import fkg.book.gui.AbstractCharaData;
import fkg.book.masterdata.GetMasterData.GetMasterDataLineSpliter;

public class CharacterInformation extends AbstractCharaData {
	int cid, cid1;
	/**
	 * 科目,masterCharacterCategory
	 */
	int category;
	/**
	 * 国家
	 */
	int country;
	int e;// 实装顺序?
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
	int j;// ???????????
	/**
	 * 被动技能1，被动技能2，主动技能
	 * CharacterLeaderSkill,CharacterLeaderSkill,CharacterSkill
	 */
	int passiveSkill1, passiveSkill2, skillNumber;
	/**
	 * all 0,除开5种属性的鲸鱼飞艇
	 */
	String n;
	String o;// ???????
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
	String aa, ab, ac;//同x，y，z
	/**
	 * 出售获得的金币值
	 */
	int worthGold;
	/**
	 * masterCharacterLevel,第二列<br>
	 * 升级经验，被合成经验
	 */
	String ae;

	/**
	 * 角色序号,原始角色限定
	 */
	String af;
	/**
	 * 0=原始角色
	 */
	int isOriginalChara;

	/**
	 * 1=角色
	 */
	int isCharacter;

	int favorHP, favorAttack, favorDefense;

	/**
	 * book id
	 */
	int bid;

	/**
	 * if 1,原始状态<br>
	 * if 2,进化状态<br>
	 * if 3,开花状态<br>
	 * if 99,升华状态<br>
	 */
	int oeb;

	String an;// 现和 isCharacter 一样

	int secondFavorHP, secondFavorAttack, secondFavorDefense;

	String ar;// 现和 isCharacter 一样

	/**
	 * 开花序号，从1开始,仅对于进化角色，并且其 isHasBloom == 1
	 */
	int bloomNumber;
	/**
	 * 1=开花角色
	 */
	int isBloomChara;
	/**
	 * 1=有开花,进化角色限定
	 */
	int hasBloom;

	/**
	 * 角色名，不同于花名
	 */
	String customName;

	int kariBloom;
	String version;//角色版本代号
	String kanaName;//角色名的假名
	int charaGroupNumber;//不同state,不同version,此字段相同
	String ba;
	int eventChara;
	String time1, time2;
	String be;
	String bf;
	int sublimation1, sublimation2, sublimation3;

	/**
	 * 升华有关的信息<br>
	 * 1 : 升华版本的ID,低星角色为(id+300000),5星角色为(id+300000+1)
	 * 2 : 1=升华角色
	 * 3 : 1=可升华 , 低星进化及五星开花限定
	 */
	int[] sublimation;
	int[] hpFavor, attackFavor, defenseFavor, powerFavor;

	public CharacterInformation(String source) {
		GetMasterDataLineSpliter gmdls = new GetMasterDataLineSpliter(source);

		this.cid = gmdls.nextInt();
		this.cid1 = gmdls.nextInt();
		this.category = gmdls.nextInt();
		this.country = gmdls.nextInt();
		this.e = gmdls.nextInt();
		this.flower_name = gmdls.next();
		this.characterExplain = gmdls.next();
		this.rarity = gmdls.nextInt();
		this.attackAttribute = gmdls.nextInt();
		this.j = gmdls.nextInt();
		this.passiveSkill1 = gmdls.nextInt();
		this.passiveSkill2 = gmdls.nextInt();
		this.skillNumber = gmdls.nextInt();
		this.n = gmdls.next();
		this.o = gmdls.next();
		this.minLevelHP = gmdls.nextInt();
		this.maxLevelHP = gmdls.nextInt();
		this.minLevelAttack = gmdls.nextInt();
		this.maxLevelAttack = gmdls.nextInt();
		this.minLevelDefense = gmdls.nextInt();
		this.maxLevelDefense = gmdls.nextInt();
		this.minLevelMove = gmdls.nextInt();
		this.maxLevelMove = gmdls.nextInt();
		this.x = gmdls.next();
		this.y = gmdls.next();
		this.z = gmdls.next();
		this.aa = gmdls.next();
		this.ab = gmdls.next();
		this.ac = gmdls.next();
		this.worthGold = gmdls.nextInt();
		this.ae = gmdls.next();
		this.af = gmdls.next();
		this.isOriginalChara = gmdls.nextInt();
		this.isCharacter = gmdls.nextInt();
		this.favorHP = gmdls.nextInt();
		this.favorAttack = gmdls.nextInt();
		this.favorDefense = gmdls.nextInt();
		this.bid = gmdls.nextInt();
		this.oeb = gmdls.nextInt();
		this.an = gmdls.next();
		this.secondFavorHP = gmdls.nextInt();
		this.secondFavorAttack = gmdls.nextInt();
		this.secondFavorDefense = gmdls.nextInt();
		this.ar = gmdls.next();
		this.bloomNumber = gmdls.nextInt();
		this.isBloomChara = gmdls.nextInt();
		this.hasBloom = gmdls.nextInt();
		this.customName = gmdls.next();
		this.kariBloom = gmdls.nextInt();
		this.version = gmdls.next();
		this.kanaName = gmdls.next();
		this.charaGroupNumber = gmdls.nextInt();
		this.ba = gmdls.next();
		this.eventChara = gmdls.nextInt();
		this.time1 = gmdls.next();
		this.time2 = gmdls.next();
		this.be = gmdls.next();
		this.bf = gmdls.next();
		this.sublimation1 = gmdls.nextInt();
		this.sublimation2 = gmdls.nextInt();
		this.sublimation3 = gmdls.nextInt();

		this.sublimation = new int[]{this.sublimation1, this.sublimation2, this.sublimation3};
		this.hpFavor = new int[]{
				(int) (this.maxLevelHP + 1.2 * this.favorHP + 1.2 * this.secondFavorHP),
				(int) (this.maxLevelHP + 1.2 * this.favorHP),
				this.maxLevelHP
		};
		this.attackFavor = new int[]{
				(int) (this.maxLevelAttack + 1.2 * this.favorAttack + 1.2 * this.secondFavorAttack),
				(int) (this.maxLevelAttack + 1.2 * this.favorAttack),
				this.maxLevelAttack
		};
		this.defenseFavor = new int[]{
				(int) (this.maxLevelDefense + 1.2 * this.favorDefense + 1.2 * this.secondFavorDefense),
				(int) (this.maxLevelDefense + 1.2 * this.favorDefense),
				this.maxLevelDefense
		};
		this.powerFavor = new int[]{
				this.hpFavor[0] + this.attackFavor[0] + this.defenseFavor[0],
				this.hpFavor[1] + this.attackFavor[1] + this.defenseFavor[1],
				this.hpFavor[2] + this.attackFavor[2] + this.defenseFavor[2]
		};
	}

	@Override public int getCharaGroupNumber() {
		return this.charaGroupNumber;
	}

	@Override public boolean isBloomChara() {
		return this.isBloomChara == 1;
	}

	@Override public boolean isOriginalChara() {
		return this.isOriginalChara == 0;
	}

	@Override public boolean isEventChara() {
		return this.eventChara == 1;
	}

	@Override public int[] getSublimation() {
		return this.sublimation;
	}

	@Override public String getVersion() {
		return this.version;
	}

	@Override public int getSkillNumber() {
		return this.skillNumber;
	}

	@Override public int getBid() {
		return this.bid;
	}

	@Override public int getBloomNumber() {
		return this.bloomNumber;
	}

	@Override public int getCountry() {
		return this.country;
	}

	@Override public int getAttackAttribute() {
		return this.attackAttribute;
	}

	@Override public String getName() {
		return this.customName;
	}

	@Override public String getCustomName() {
		return this.customName;
	}

	@Override public int getId() {
		return this.cid;
	}

	@Override public int getOeb() {
		return this.oeb;
	}

	@Override public boolean isCharacter() {
		return this.isCharacter == 1;
	}

	@Override public boolean isKariBloom() {
		return this.kariBloom == 1;
	}

	@Override public boolean isHasBloom() {
		return this.hasBloom == 1;
	}

	@Override public int[] getHP() {
		return this.hpFavor;
	}

	@Override public int[] getAttack() {
		return this.attackFavor;
	}

	@Override public int[] getDefense() {
		return this.defenseFavor;
	}

	@Override public int[] getPower() {
		return this.powerFavor;
	}

	@Override public int getMove() {
		return this.minLevelMove;
	}

	@Override public int getRarity() {
		return this.rarity;
	}
}
