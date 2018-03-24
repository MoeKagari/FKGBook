package fkg.book.gui;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import fkg.book.masterdata.CharacterLeaderSkillDescription;
import fkg.book.masterdata.CharacterSkill;
import fkg.book.masterdata.GetMasterData;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import tool.Downloader;
import tool.compress.MD5;
import tool.compress.ZLib;

public abstract class AbstractCharaData {
	private static final int ICON_SIZE = 50;
	private static final Image[][] ICON_DECORATION = {
			IntStream.rangeClosed(1, 6)
					.mapToObj(index -> String.format("/fkg/book/other/icon/decoration/background_%d.png", index))
					.map(filepath -> new Image(AbstractCharaData.class.getResourceAsStream(filepath), ICON_SIZE, ICON_SIZE, true, true))
					.toArray(Image[]::new),
			IntStream.rangeClosed(1, 6)
					.mapToObj(index -> String.format("/fkg/book/other/icon/decoration/frame_%d.png", index))
					.map(filepath -> new Image(AbstractCharaData.class.getResourceAsStream(filepath), ICON_SIZE, ICON_SIZE, true, true))
					.toArray(Image[]::new),
			IntStream.rangeClosed(1, 4)
					.mapToObj(index -> String.format("/fkg/book/other/icon/decoration/attribute_%d.png", index))
					.map(filepath -> new Image(AbstractCharaData.class.getResourceAsStream(filepath), ICON_SIZE, ICON_SIZE, true, true))
					.toArray(Image[]::new)
			/**/ };

	//@formatter:off
	private static final String NETPATH_profix = "http://dugrqaqinbtcq.cloudfront.net/product/";

	public static final String CHARACTER_ICON = "resources\\character_image\\icon";
	public static String getCharacterIconNetpath(int id) {
		return NETPATH_profix + "images/character/i/" + MD5.getMD5("icon_s_" + id) + ".bin";
	}
	
	public static final String CHARACTER_STAND = "resources\\character_image\\stand";
	public static String getCharacterStandNetpath(int id) {
		return NETPATH_profix + "images/character/s/" + MD5.getMD5("stand_" + id) + ".bin";
	}
	
	public static final String CHARACTER_STAND_S = "resources\\character_image\\stand_s";
	public static String getCharacterStandSNetpath(int id) {
		return NETPATH_profix + "images/character/s/" + MD5.getMD5("stand_s_" + id) + ".bin";
	}
	
	public static final String CHARACTER_STORY = "resources\\character_story";
	/** 非角色ID,而是角色的个人剧情对应的MasterStory里面的ID */
	public static String getCharacterStoryNetpath(int id) {
		return NETPATH_profix + "event/story/" + MD5.getMD5(String.format("story_%06d", id)) + ".bin";
	}
	//@formatter:on

	/*-------------------------------------------------------------------------------------------------------------------------------*/

	private Image getImage(String filedir, IntFunction<String> urlStr, int width, int height) {
		int image_id;
		{
			int id = this.getId();
			if (this.getOeb() == 99) {
				if (this.isBloomChara()) {
					image_id = id - 300000;
				} else {
					image_id = id - 300000 + 1;
				}
			} else if (this.getOeb() == 3 && this.getKariBloom()) {
				image_id = id - 300000 + 1;
			} else {
				image_id = id;
			}
		}

		File file = new File(String.format("%s\\%d.png", filedir, image_id));

		Image image = null;
		image = Optional.ofNullable(image)
				.orElseGet(() -> {
					try (FileInputStream fis = new FileInputStream(file)) {
						return new Image(fis);
					} catch (Exception e) {}
					return null;
				});
		image = Optional.ofNullable(image)
				.orElseGet(() -> {
					byte[] bytes = ZLib.decompress(Downloader.download(urlStr.apply(image_id)));
					if (bytes != null) {
						try {
							Files.createDirectories(file.getParentFile().toPath());
							Files.write(file.toPath(), bytes);
							return new Image(new ByteArrayInputStream(bytes));
						} catch (Exception e) {}
					}
					return null;
				});
		return Optional.ofNullable(image)
				.orElseGet(() -> {
					return new WritableImage(width, height);
				});
	}

	private CharacterLeaderSkillDescription ability;//能力
	private CharacterSkill skill;//技能
	private Image[] icons;
	private AbstractCharaData[] allChara;

	public AbstractCharaData[] getAllChara() {
		return Optional.ofNullable(this.allChara)
				.orElseGet(() -> {
					int o_id = this.getId();
					switch (this.getOeb()) {
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
							o_id -= 1;
							o_id -= 300000;
							break;
						default:
							throw new RuntimeException("不可能的oeb : " + this.getOeb());
					}
					return new AbstractCharaData[] {
							GetMasterData.ALL_CHARA.get(o_id),
							GetMasterData.ALL_CHARA.get(o_id + 1),
							GetMasterData.ALL_CHARA.get(o_id + 300000),
							GetMasterData.ALL_CHARA.get(o_id + 300000 + 1)
					};
				});
	}

	public Image getStandS() {
		return this.getImage(AbstractCharaData.CHARACTER_STAND_S, AbstractCharaData::getCharacterStandSNetpath, 329, 467);
	}

	public Image getStand() {
		return this.getImage(AbstractCharaData.CHARACTER_STAND, AbstractCharaData::getCharacterStandNetpath, 960, 640);
	}

	public Image[] getIcons() {
		return this.icons = Optional.ofNullable(this.icons)
				.orElseGet(() -> {
					return new Image[] {
							ICON_DECORATION[0][this.getRarity() - 1],
							this.getImage(AbstractCharaData.CHARACTER_ICON, AbstractCharaData::getCharacterIconNetpath, 50, 50),
							ICON_DECORATION[1][this.getRarity() - 1],
							ICON_DECORATION[2][this.getAttackAttribute() - 1]
					};
				});
	}

	public abstract int getSkillNumber();

	public CharacterSkill getSkill() {
		return this.skill = Optional.ofNullable(this.skill)
				.orElseGet(() -> {
					return GetMasterData.MASTERSKILL.stream().filter(ms -> ms.id == this.getSkillNumber()).findFirst().get();
				});
	}

	public CharacterLeaderSkillDescription getAbility() {
		return this.ability = Optional.ofNullable(this.ability)
				.orElseGet(() -> {
					return GetMasterData.MASTERLEADERSKILLDESCRIPTION.stream().filter(mlsd -> mlsd.cid == this.getId()).findFirst().get();
				});
	}

	public abstract String getVersion();

	public abstract boolean isCharacter();

	public abstract int getBloomNumber();

	public abstract int getBid();

	public abstract int getOeb();

	public abstract boolean hasBloom();

	public abstract boolean getKariBloom();

	public abstract int getCountry();

	public String getCountryString() {
		for (CountryFilter filter : CountryFilter.values()) {
			if (filter.country == this.getCountry()) {
				return filter.toString();
			}
		}
		throw new RuntimeException("无对应的Country");
	}

	public abstract int[] getPower();

	public abstract int[] getDefense();

	public abstract int[] getAttack();

	public abstract int[] getHP();

	public abstract int getMove();

	public abstract int getAttackAttribute();

	public String getAttackAttributeString() {
		for (AttackAttributeFilter filter : AttackAttributeFilter.values()) {
			if (filter.attribute == this.getAttackAttribute()) {
				return filter.toString();
			}
		}
		throw new RuntimeException("无对应的AttackAttribute");
	}

	public abstract int getRarity();

	public String getRarityString() {
		return "★★★★★★★★★★".substring(0, this.getRarity());
	}

	public abstract int getId();

	public abstract String getName();

	/** fkgbook中 可否显示 */
	public boolean canShowInApplication() {
		return this.isCharacter() &&
				this.getId() < 1_0000_0000//仅有三个角色的特殊换装
		;
	}

	/** 可以做 副团长 */
	public boolean canBeSetDeputyLeader() {
		switch (this.getOeb()) {
			case 1:
			case 2:
				return true;
			case 3:
				return this.getKariBloom();
			case 99:
				return false;
		}
		throw new RuntimeException("不可能的OEB : " + this.getOeb());
	}

	/** 活动角色 */
	public abstract boolean isEventChara();

	public abstract int[] getSublimation();

	public abstract boolean isOriginalChara();

	public abstract boolean isEvolutionChara();

	public abstract boolean isBloomChara();

	public String getStateString() {
		switch (this.getOeb()) {
			case 1:
				return "原始";
			case 2:
				return "进化";
			case 3:
				return this.getKariBloom() ? "开花(假)" : "开花(真)";
			case 99:
				return this.isBloomChara() ? "升华(开花)" : "升华(进化)";
		}
		throw new RuntimeException("不可能的OEB : " + this.getOeb());
	}

	//@formatter:off
	public interface CharaFilter extends Predicate<AbstractCharaData> {
		boolean filter(AbstractCharaData cd);
		@Override String toString();
		@Override default boolean test(AbstractCharaData cd) {return filter(cd);}
	}
	public static enum SkilltypeFilter implements CharaFilter {
		NONE(0,"无"),
		SKILL_TYPE_10(10,"伤害上升(回合)"),
		SKILL_TYPE_11(11,"伤害增加(BOSS)"),
		SKILL_TYPE_16(16,"再动"),
		SKILL_TYPE_17(17,"再动(第一回合)"),
		SKILL_TYPE_21(21,"几率反击"),
		SKILL_TYPE_19(19,"几率回避"),
		SKILL_TYPE_35(35,"加农炮的攻击数量加一"),
		SKILL_TYPE_30(30,"回复点效果倍增"),
		SKILL_TYPE_31(31,"地图上的害虫相关"),
		SKILL_TYPE_22(22,"复生"),
		SKILL_TYPE_12(12,"属性赋予"),
		SKILL_TYPE_37(37,"属性赋予-三种属性-每种属性2人"),
		SKILL_TYPE_13(13,"技能发动率(1)"),
		SKILL_TYPE_14(14,"技能发动率(2)"),
		SKILL_TYPE_1(1,"攻击力上升"),
		SKILL_TYPE_38(38,"攻击力上升(30%*回合数)"),
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
		SKILL_TYPE_20(20,"敌攻击无效"),
		SKILL_TYPE_15(15,"暴击率上升or暴击伤害增加"),
		SKILL_TYPE_26(26,"移动力不受增减"),
		SKILL_TYPE_24(24,"移动力增加"),
		SKILL_TYPE_18(18,"防御上升-受到伤害下降-3扛"),
		SKILL_TYPE_23(23,"阳光炮能量上升"),
		SKILL_TYPE_32(32,"降攻");
		public final int skilltype;
		public final String skilltypeString;
		private SkilltypeFilter(int skilltype, String skilltypeString) {this.skilltype = skilltype;this.skilltypeString = skilltypeString;}
		@Override public String toString() {return this.skilltypeString;}
		@Override public boolean filter(AbstractCharaData cd) {
			if (this.skilltype == 0)return true;
			if (cd.getAbility() == null)return false;
			return Arrays.stream(cd.getAbility().abilityArray).anyMatch(si -> si.getType()== this.skilltype);
		}
	}
	public static enum RarityFilter implements CharaFilter {
		无(0,"无"),星2(2,"★2"),星3(3,"★3"),星4(4,"★4"),星5(5,"★5"),星6(6,"★6");
		public final int rarity;
		public final String rarityString;
		private RarityFilter(int rarity,String rarityString) {this.rarity = rarity;this.rarityString=rarityString;}
		@Override public String toString() {return this.rarityString;}
		@Override public boolean filter(AbstractCharaData cd) {
			return this.rarity == 0 || this.rarity == cd.getRarity();
		}
	}
	public static enum CountryFilter implements CharaFilter {
		无(0),ウィンターローズ(1),バナナオーシャン(2),ブロッサムヒル(3),ベルガモットバレー(4),リリィウッド(5),ロータスレイク(7);
		public final int country;
		private CountryFilter(int country) {this.country = country;}
		@Override public boolean filter(AbstractCharaData cd) {
			return this.country == 0 || this.country == cd.getCountry();
		}
	}
	public static enum AttackAttributeFilter implements CharaFilter {
		无(0),斩(1),打(2),突(3),魔(4);
		public final int attribute;
		private AttackAttributeFilter(int attribute) {this.attribute = attribute;}
		@Override public boolean filter(AbstractCharaData cd) {
			return this.attribute == 0 || this.attribute == cd.getAttackAttribute();
		}
	}
	//@formatter:on
}
