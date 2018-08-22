package fkg.book.temp;

import fkg.book.masterdata.CharacterLeaderSkillDescription;
import fkg.book.masterdata.CharacterSkill;
import javafx.scene.image.Image;

import java.util.Arrays;
import java.util.function.Predicate;

public abstract class AbstractCharaData {
	private final AbstractCharaDataImage abstractCharaDataImage = new AbstractCharaDataImage(this);

	public Image getStandS() {
		return this.abstractCharaDataImage.getStandS();
	}

	public Image getStand() {
		return this.abstractCharaDataImage.getStand();
	}

	public Image[] getIcons() {
		return this.abstractCharaDataImage.getIcons();
	}

	public Image getCutin() {
		return this.abstractCharaDataImage.getCutin();
	}

	/*-------------------------------------------------------------------------------------------------------------------------------*/

	private String countryString;
	private String attackAttributeString;
	private String rarityString;

	public abstract int getSkillNumber();

	public abstract String getVersion();

	public abstract boolean isCharacter();

	public abstract int getBloomNumber();

	public abstract int getBid();

	public abstract int getOeb();

	public abstract boolean isHasBloom();

	public abstract boolean isKariBloom();

	public abstract int getCountry();

	public String getCountryString() {
		if(this.countryString == null) {
			for(CountryFilter filter : CountryFilter.values()) {
				if(filter.country == this.getCountry()) {
					this.countryString = filter.toString();
					break;
				}
			}
		}
		return this.countryString;
	}

	public abstract int[] getPower();

	public int getPowerFinal() {
		return this.getPower()[0];
	}

	public abstract int[] getDefense();

	public int getDefenseFinal() {
		return this.getDefense()[0];
	}

	public abstract int[] getAttack();

	public int getAttackFinal() {
		return this.getAttack()[0];
	}

	public abstract int[] getHP();

	public int getHPFinal() {
		return this.getHP()[0];
	}

	public abstract int getMove();

	public abstract int getAttackAttribute();

	public String getAttackAttributeString() {
		if(this.attackAttributeString == null) {
			for(AttackAttributeFilter filter : AttackAttributeFilter.values()) {
				if(filter.attribute == this.getAttackAttribute()) {
					this.attackAttributeString = filter.toString();
					break;
				}
			}
		}
		return this.attackAttributeString;
	}

	public abstract int getRarity();

	public String getRarityString() {
		if(this.rarityString == null) {
			this.rarityString = "★★★★★★★★★★".substring(0, this.getRarity());
		}
		return this.rarityString;
	}

	public abstract int getId();

	/**
	 * 原版角色 与 换装角色 , 此值相同
	 */
	public abstract int getCharaGroupNumber();

	/**
	 * 当前角色名
	 */
	public abstract String getName();

	/**
	 * {@link AbstractCharaData#getName()} 去除 {@link AbstractCharaData#getVersion()}
	 */
	public abstract String getCustomName();

	/*-------------------------------------------------------------------------------------------------------------------------------*/
	private final AbstractCharaDataCharaType abstractCharaDataCharaType = new AbstractCharaDataCharaType(this);

	public abstract boolean isEventChara();

	public boolean isNotEventChara() {
		return this.abstractCharaDataCharaType.isNotEventChara();
	}

	public boolean isGachaChara() {
		return this.abstractCharaDataCharaType.isGachaChara();
	}

	public boolean isNotGachaChara() {
		return this.abstractCharaDataCharaType.isNotGachaChara();
	}

	public boolean isOtherChara() {
		return this.abstractCharaDataCharaType.isOtherChara();
	}

	public boolean isNotOtherChara() {
		return this.abstractCharaDataCharaType.isNotOtherChara();
	}

	/*-------------------------------------------------------------------------------------------------------------------------------*/

	public abstract int[] getSublimation();

	public abstract boolean isOriginalChara();

	public boolean isEvolutionChara() {
		return !this.isBloomChara() && !this.isOriginalChara();
	}

	public abstract boolean isBloomChara();

	/*-------------------------------------------------------------------------------------------------------------------------------*/
	private AbstractCharaDataExtension abstractCharaDataExtension = new AbstractCharaDataExtension(this);

	public AbstractCharaData[] getAllChara() {
		return this.abstractCharaDataExtension.getAllChara();
	}

	public CharacterSkill getSkill() {
		return this.abstractCharaDataExtension.getSkill();
	}

	public CharacterLeaderSkillDescription getAbility() {
		return this.abstractCharaDataExtension.getAbility();
	}

	public String getIntroduction() {
		return this.abstractCharaDataExtension.getIntroduction();
	}

	public String getFlowerLanguage() {
		return this.abstractCharaDataExtension.getFlowerLanguage();
	}

	/**
	 * fkgbook中 可否显示
	 */
	public boolean canShowInApplication() {
		return this.abstractCharaDataExtension.canShowInApplication();
	}

	/**
	 * 可以做 副团长
	 */
	public boolean canBeSetDeputyLeader() {
		return this.abstractCharaDataExtension.canBeSetDeputyLeader();
	}

	public String getStateString() {
		return this.abstractCharaDataExtension.getStateString();
	}

	/**
	 * 最高进化
	 */
	public boolean isMostLevel() {
		return this.abstractCharaDataExtension.isMostLevel();
	}

	public int getImageId() {
		return this.abstractCharaDataExtension.getImageId();
	}

	//@formatter:off
	public interface CharaFilter extends Predicate<AbstractCharaData> {}
	public enum SkillTypeFilter implements CharaFilter {
		NONE(0,"无"),
		SKILL_TYPE_32(32,"降攻"),
		SKILL_TYPE_20(20,"敌攻击无效"),
		SKILL_TYPE_15(15,"暴击率上升or暴击伤害增加"),
		SKILL_TYPE_18(18,"防御上升-受到伤害下降-3扛"),
		SKILL_TYPE_12(12,"属性赋予(单)"),
		SKILL_TYPE_37(37,"属性赋予(多)"),
		SKILL_TYPE_13(13,"技能发动率(1)"),
		SKILL_TYPE_14(14,"技能发动率(2)"),
		SKILL_TYPE_10(10,"伤害增加"),
		SKILL_TYPE_11(11,"伤害增加(BOSS)"),
		SKILL_TYPE_22(22,"回复(一定条件)"),
		SKILL_TYPE_16(16,"再动"),
		SKILL_TYPE_17(17,"再动(第一回合)"),
		SKILL_TYPE_21(21,"几率反击"),
		SKILL_TYPE_19(19,"几率回避"),
		SKILL_TYPE_1(1,"攻击力上升"),
		SKILL_TYPE_38(38,"攻击力上升(回合数)"),
		SKILL_TYPE_9(9,"攻击力上升(BOSS战)"),
		SKILL_TYPE_7(7,"攻击力上升(战斗开始时的敌数)"),
		SKILL_TYPE_6(6,"攻击力上升(技能等级)"),
		SKILL_TYPE_4(4,"攻击力上升(移动力)"),
		SKILL_TYPE_2(2,"攻击力上升(第一回合)"),
		SKILL_TYPE_8(8,"攻击力上升(队友技能发动)"),
		SKILL_TYPE_3(3,"攻击力上升(阳光炮)"),
		SKILL_TYPE_29(29,"放置[1.1技能发动率-HP回复]点"),
		SKILL_TYPE_27(27,"放置回复点"),
		SKILL_TYPE_28(28,"放置阳光点"),
		SKILL_TYPE_34(34,"敌技能发动率降低"),
		SKILL_TYPE_33(33,"敌攻击几率MISS"),
		SKILL_TYPE_24(24,"移动力增加"),
		SKILL_TYPE_26(26,"移动力不受增减"),
		SKILL_TYPE_23(23,"阳光炮能量上升"),
		SKILL_TYPE_35(35,"加农炮的攻击数量加一"),
		SKILL_TYPE_30(30,"回复点效果倍增"),
		SKILL_TYPE_31(31,"地图上的害虫相关");
		public final int skillType;
		public final String skillTypeString;
		SkillTypeFilter(int skillType, String skillTypeString) {this.skillType = skillType;this.skillTypeString = skillTypeString;}
		@Override public String toString() {return this.skillTypeString;}
		@Override public boolean test(AbstractCharaData cd) {
			if (this.skillType == 0) {
				return true;
			}
			if (cd.getAbility() == null) {
				return false;
			}
			return Arrays.stream(cd.getAbility().abilityArray).anyMatch(si -> si.getType()== this.skillType);
		}
	}
	@SuppressWarnings("NonAsciiCharacters")
	public enum RarityFilter implements CharaFilter {
		无(0,"无"),星2(2,"★2"),星3(3,"★3"),星4(4,"★4"),星5(5,"★5"),星6(6,"★6");
		public final int rarity;
		public final String rarityString;
		RarityFilter(int rarity,String rarityString) {this.rarity = rarity;this.rarityString=rarityString;}
		@Override public String toString() {return this.rarityString;}
		@Override public boolean test(AbstractCharaData cd) {
			return this.rarity == 0 || this.rarity == cd.getRarity();
		}
	}
	@SuppressWarnings("NonAsciiCharacters")
	public enum CountryFilter implements CharaFilter {
		无(0),ウィンターローズ(1),バナナオーシャン(2),ブロッサムヒル(3),ベルガモットバレー(4),リリィウッド(5),ロータスレイク(7);
		public final int country;
		CountryFilter(int country) {this.country = country;}
		@Override public boolean test(AbstractCharaData cd) {
			return this.country == 0 || this.country == cd.getCountry();
		}
	}
	@SuppressWarnings("NonAsciiCharacters")
	public enum AttackAttributeFilter implements CharaFilter {
		无(0),斩(1),打(2),突(3),魔(4);
		public final int attribute;
		AttackAttributeFilter(int attribute) {this.attribute = attribute;}
		@Override public boolean test(AbstractCharaData cd) {
			return this.attribute == 0 || this.attribute == cd.getAttackAttribute();
		}
	}
	//@formatter:on
}
