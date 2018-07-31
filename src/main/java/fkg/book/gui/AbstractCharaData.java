package fkg.book.gui;

import com.moekagari.tool.acs.ArrayUtils;
import fkg.book.masterdata.CharacterBook;
import fkg.book.masterdata.CharacterLeaderSkillDescription;
import fkg.book.masterdata.CharacterSkill;
import fkg.book.masterdata.CharacterTextResource;
import fkg.book.masterdata.GetMasterData;
import javafx.scene.image.Image;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class AbstractCharaData {
	private static final List<Integer> OTHER_CHARA_LIST = ArrayUtils.asList(
			136701, 117101, 112901, 114901, 148301,
			128801, 131923, 124705, 139201, 156501,
			130805, 150203, 150811, 117603, 132913,
			164403, 110809, 157603, 125607, 154405,
			110001, 122801, 162701, 143003, 120003,
			120819
	);

	/*-------------------------------------------------------------------------------------------------------------------------------*/

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

	/*-------------------------------------------------------------------------------------------------------------------------------*/

	private AbstractCharaData[] allChara;
	private CharacterSkill skill;//技能
	private CharacterLeaderSkillDescription ability;//能力
	private String introduction;
	private String flowerLanguage;

	public AbstractCharaData[] getAllChara() {
		return this.allChara = Optional.ofNullable(this.allChara)
		                               .orElseGet(() -> {
			                               int o_id = this.getId();
			                               switch(this.getOeb()) {
				                               case 1:
					                               o_id -= 0;
					                               break;
				                               case 2:
					                               o_id -= 1;
					                               break;
				                               case 3:
					                               o_id -= 300000;
					                               break;
				                               case 99:
					                               if(this.isBloomChara()) {
						                               o_id = o_id - 300000 - 1;
					                               } else {
						                               o_id = o_id - 300000;
					                               }
					                               break;
				                               default:
					                               throw new RuntimeException("不可能的oeb : " + this.getOeb());
			                               }
			                               return new AbstractCharaData[]{
					                               GetMasterData.ALL_CHARA.get(o_id),
					                               GetMasterData.ALL_CHARA.get(o_id + 1),
					                               GetMasterData.ALL_CHARA.get(o_id + 300000),
					                               GetMasterData.ALL_CHARA.get(o_id + 300000 + 1)
			                               };
		                               });
	}

	public CharacterSkill getSkill() {
		if(this.skill == null) {
			Optional<CharacterSkill> skillOptional = GetMasterData.MASTERSKILL
					.stream().filter(ms -> ms.id == this.getSkillNumber()).findFirst();
			assert skillOptional.isPresent();
			this.skill = skillOptional.get();
		}
		return this.skill;
	}

	public CharacterLeaderSkillDescription getAbility() {
		if(this.ability == null) {
			Optional<CharacterLeaderSkillDescription> abilityOptional = GetMasterData.MASTERLEADERSKILLDESCRIPTION
					.stream().filter(characterLeaderSkillDescription -> characterLeaderSkillDescription.cid == this.getId()).findFirst();
			assert abilityOptional.isPresent();
			this.ability = abilityOptional.get();
		}
		return this.ability;
	}

	public String getIntroduction() {
		if(this.introduction == null) {
			Optional<CharacterTextResource> introductionOptional = GetMasterData.MASTERCHARACTERTEXTRESOURCE
					.stream()
					.filter(CharacterTextResource::isIntroduction)
					.filter(ctr -> ctr.getCharacter() == this.getAllChara()[0].getId())
					.findFirst();
			assert introductionOptional.isPresent();
			this.introduction = introductionOptional.get().getText();
		}
		return this.introduction;
	}

	public String getFlowerLanguage() {
		if(this.flowerLanguage == null) {
			Optional<String> flowerLanguageOptional = GetMasterData.MASTERBOOK
					.stream().filter(cb -> cb.getId() == this.getBid()).findFirst().map(CharacterBook::getFlowerLanguage);
			assert flowerLanguageOptional.isPresent();
			this.flowerLanguage = flowerLanguageOptional.get();
		}
		return this.flowerLanguage;
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

	public abstract int[] getDefense();

	public abstract int[] getAttack();

	public abstract int[] getHP();

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

	public abstract boolean isEventChara();

	public boolean isNotEventChara() {
		return !this.isEventChara();
	}

	public boolean isGachaChara() {
		return this.isNotEventChara() && this.isNotOtherChara();
	}

	public boolean isNotGachaChara() {
		return !this.isGachaChara();
	}

	public boolean isOtherChara() {
		return OTHER_CHARA_LIST.contains(this.getAllChara()[0].getId());
	}

	public boolean isNotOtherChara() {
		return !this.isOtherChara();
	}

	/*-------------------------------------------------------------------------------------------------------------------------------*/

	public abstract int[] getSublimation();

	public abstract boolean isOriginalChara();

	public boolean isEvolutionChara() {
		return !this.isBloomChara() && !this.isOriginalChara();
	}

	public abstract boolean isBloomChara();

	/*-------------------------------------------------------------------------------------------------------------------------------*/
	private String stateString;

	public int getHPFinal() {return this.getHP()[0];}

	public int getAttackFinal() {return this.getAttack()[0];}

	public int getDefenseFinal() {return this.getDefense()[0];}

	public int getPowerFinal() {return this.getPower()[0];}

	/**
	 * fkgbook中 可否显示
	 */
	public boolean canShowInApplication() {
		return this.isCharacter() &&
				this.getId() < 1_0000_0000//仅有三个角色的特殊换装
				;
	}

	/**
	 * 可以做 副团长
	 */
	public boolean canBeSetDeputyLeader() {
		switch(this.getOeb()) {
			case 1:
			case 2:
				return true;
			case 3:
				return this.isKariBloom();
			case 99:
				return false;
		}
		throw new RuntimeException("不可能的OEB : " + this.getOeb());
	}

	public String getStateString() {
		return this.stateString = Optional.ofNullable(this.stateString).orElseGet(() -> {
			switch(this.getOeb()) {
				case 1:
					return "原始";
				case 2:
					return "进化";
				case 3:
					return "开花" + (this.isKariBloom() ? "(假)" : "");
				case 99:
					return String.format("升华(%s)", this.isBloomChara() ? "开花" : "进化");
			}
			throw new RuntimeException("不可能的OEB : " + this.getOeb());
		});
	}

	/**
	 * 最高进化
	 */
	public boolean isMostLevel() {
		AbstractCharaData[] allChara = this.getAllChara();
		for(int index = allChara.length - 1; index >= 0; index--) {
			if(allChara[index] != null) {
				return allChara[index].getId() == this.getId();
			}
		}
		throw new RuntimeException("不可能");
	}

	/**
	 * 仅对于进化角色,高星无开花,低星无升华
	 */
	public boolean isNotHaveBloom() {
		return this.getOeb() == 2 && !this.isHasBloom() && this.getSublimation()[2] != 1;
	}

	public int getImageId() {
		int id = this.getId();
		switch(this.getOeb()) {
			case 1:
			case 2:
				return id;
			case 3:
				return this.isKariBloom() ? (id - 300000 + 1) : id;
			case 99:
				try {
					switch(this.getAllChara()[0].getRarity()) {
						case 1:
						case 2:
						case 3:
						case 4:
							return this.getAllChara()[1].getId();
						case 5:
							if(this.isBloomChara()) {
								return this.isKariBloom() ? (id - 300000) : (id - 1);
							} else {
								throw new RuntimeException("不可能不为 bloomChara");
							}
						case 6:
						default:
							throw new RuntimeException("不可能的rarity : " + this.getRarity());
					}
				} catch(Exception e) {
					throw new RuntimeException(this.getId() + "", e);
				}
			default:
				throw new RuntimeException("不可能的oeb : " + this.getOeb());
		}
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
