package fkg.book.other;

import com.moekagari.tool.acs.ExStreamUtils;
import com.moekagari.tool.compress.Base64;
import com.moekagari.tool.io.FileUtils;
import com.moekagari.tool.json.JsonUtils;
import com.moekagari.tool.lambda.LambdaUtils;
import fkg.book.gui.AbstractCharaData;
import fkg.book.gui.AbstractCharaData.SkillTypeFilter;
import fkg.book.masterdata.CharacterLeaderSkillDescription.AbilityInfo;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fkg.book.masterdata.GetMasterData.ALL_CHARA;

/**
 * 导出用于 fkgbook网页版 的数据
 */
public class FKGBOOK_WEB_MASTER_DATA {
	public static void main(String[] args) {
		String output_root = "F:\\tomcat\\webapps\\ROOT";
		BiConsumer<String, Object> saveData = (filename, data) -> FileUtils.write(
				new File(String.format(output_root + "\\script\\data-%s.js", filename)),
				String.format("FKGBook.data.%s = %s;", filename, data),
				Charset.defaultCharset()
		);

		ExStreamUtils.of(SkillTypeFilter.values())
		             .sortedInt(ss -> ss.skillType)
		             .map(value -> String.format(
				             "{'text':'%s','value':'%d'},",
				             value.skillTypeString, value.skillType
		             ))
		             .println();
		ExStreamUtils.mapValueStream(ALL_CHARA).filter(AbstractCharaData::canShowInApplication)
		             .map(AbstractCharaData::getAbility)
		             .flatMap(ability -> ExStreamUtils.of(ability.abilityArray))
		             .mapToInt(AbilityInfo::getType)
		             .distinct().sorted().println();

		boolean limit = true;
		limit = false;
		int chara_limit = limit ? 50 : Integer.MAX_VALUE;

		/** 同一角色(bid相同),不同进化程度之间相同的值 */
		class DifferentLevelSameValue implements Comparable<DifferentLevelSameValue> {
			final AbstractCharaData chara;

			public DifferentLevelSameValue(AbstractCharaData chara) {
				this.chara = chara;
			}

			@Override
			public int compareTo(DifferentLevelSameValue other) {
				return Integer.compare(this.chara.getBid(), other.chara.getBid());
			}

			@Override
			public boolean equals(Object obj) {
				if(this == obj) {
					return true;
				}
				if(obj instanceof DifferentLevelSameValue) {
					return this.chara.getBid() == ((DifferentLevelSameValue) obj).chara.getBid();
				}
				return false;
			}

			@Override
			public int hashCode() {
				return this.chara.getBid();
			}

			public JsonObjectBuilder toJson() {
				return Json.createObjectBuilder()
				           .add("version", this.chara.getVersion())
				           .add("name", this.chara.getName())
				           .add("bid", this.chara.getBid());
			}
		}
		class CharaJsonObject implements Comparable<CharaJsonObject> {
			final AbstractCharaData chara;

			public CharaJsonObject(AbstractCharaData chara) {
				this.chara = chara;
			}

			@Override
			public int compareTo(CharaJsonObject other) {
				return Integer.compare(this.chara.getId(), other.chara.getId());
			}

			public JsonObject toJson() {
				return Json.createObjectBuilder()
				           .add("id", this.chara.getId())
				           .add("name", this.chara.getName())
				           .add("rarity", this.chara.getRarity())
				           .add("oeb", this.chara.getOeb())
				           .add("attackAttribute", this.chara.getAttackAttributeString())
				           .add("move", this.chara.getMove())
				           .add("hp", JsonUtils.toJsonArray(this.chara.getHP()))
				           .add("attack", JsonUtils.toJsonArray(this.chara.getAttack()))
				           .add("defense", JsonUtils.toJsonArray(this.chara.getDefense()))
				           .add("power", JsonUtils.toJsonArray(this.chara.getPower()))
				           .add("country", this.chara.getCountryString())
				           .add("version", this.chara.getVersion())
				           .add("bloomNumber", this.chara.getBloomNumber())
				           .add("kariBloom", this.chara.isKariBloom())
				           .add("isHasBloom", this.chara.isHasBloom())
				           .add("bid", this.chara.getBid())
				           .add("state", this.chara.getStateString())
				           .add("isEventChara", this.chara.isEventChara())
				           .add("charaGroupNumber", this.chara.getCharaGroupNumber())
				           .add("image_id", this.chara.getImageId())
				           .add("mostLevel", this.chara.isMostLevel())
				           .add("isNotHaveBloom", this.chara.isNotHaveBloom())
				           .add("bloomChara", this.chara.isBloomChara())
				           .add("customName", this.chara.getCustomName())
				           .add("introduction", this.chara.getIntroduction())
				           .add("abouts", Json.createArrayBuilder().add("待整理"))
				           .add("flowerLanguage", this.chara.getAllChara()[0].getFlowerLanguage())

				           //技能
				           .add("skill", Json.createObjectBuilder().add("name", this.chara.getSkill().getName())
				                             .add("effect", this.chara.getSkill().getEffect()))

				           //能力
				           .add("ability", JsonUtils.toJsonArray(
						           this.chara.getAbility().abilityArray,
						           ability -> {
							           int type = ability.getType();
							           String description = ability.getDescription();
							           String type_icon = String.valueOf(type);
							           if(type == 12) {
								           if(description.contains("斬")) {
									           type_icon += 1;
								           } else {
									           if(description.contains("打")) {
										           type_icon += 2;
									           } else {
										           if(description.contains("突")) {
											           type_icon += 3;
										           } else {
											           if(description.contains("魔")) {
												           type_icon += 4;
											           } else {
												           throw new RuntimeException("不可能的 属性赋予(单) : " + description);
											           }
										           }
									           }
								           }
							           }
							           return Json.createObjectBuilder()
							                      .add("type_icon", type_icon)
							                      .add("type", type)
							                      .add("description", description)
							                      .build();
						           }
				           ))

				           .build();
			}
		}
		Stream<JsonValue> charaData =
				ExStreamUtils.mapValueStream(ALL_CHARA)
				             .filter(AbstractCharaData::canShowInApplication)
				             .limit(chara_limit).parallel()
				             .collect(Collectors.groupingBy(
						             AbstractCharaData::getCharaGroupNumber,
						             Collectors.groupingBy(
								             DifferentLevelSameValue::new,
								             Collectors.mapping(CharaJsonObject::new, Collectors.toList())
						             )
				             ))
				             .entrySet().parallelStream()
				             .map(layer1 -> JsonUtils.toJsonArray(
						             ExStreamUtils.mapEntryStream(layer1.getValue())
						                          .sortedObj(Map.Entry::getKey)
						                          .map(layer2 -> Json.createObjectBuilder()
						                                             .add("info", layer2.getKey().toJson())
						                                             .add("charas", JsonUtils.toJsonArray(
								                                             layer2.getValue().stream().sorted()
								                                                   .map(CharaJsonObject::toJson)
						                                             ))
						                                             .build()
						                          )
				             ));
		saveData.accept("chara", JsonUtils.toJsonArray(charaData));

		int width = 50, height = 50;
		ExStreamUtils.mapValueStream(ALL_CHARA).filter(AbstractCharaData::canShowInApplication).parallel()
		             .forEach(chara -> {
			             BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			             Graphics2D g = image.createGraphics();
			             for(Image icon : chara.getIcons()) {
				             g.drawImage(SwingFXUtils.fromFXImage(icon, null), 0, 0, width, height, null);
			             }

			             try {
				             ImageIO.write(image, "png", new File(output_root + "\\icon\\" + chara.getId() + ".png"));
			             } catch(IOException e) {
				             e.printStackTrace();
			             }
		             });

		JsonObjectBuilder abilityData = Json.createObjectBuilder();
		ExStreamUtils.of(new File("resources\\ability_icon").listFiles()).parallel()
		             .forEach(
				             file -> {
					             String filename = file.getName();
					             String type = filename.substring(0, filename.indexOf('.'));
					             return "type" + type;
				             },
				             file -> {
					             try {
						             return Base64.compress(Files.readAllBytes(file.toPath())).map(String::new).orElse("");
					             } catch(IOException e) {
						             return null;
					             }
				             },
				             abilityData::add
		             );
		saveData.accept("ability", abilityData.build());

		String version = DateTimeFormatter.ofPattern("OOOO yyyy-MM-dd HH:mm:ss").format(ZonedDateTime.now());
		saveData.accept("version", "\"" + version + "\"");

		class CharaUpdateWithTime {
			final LocalDate date;
			final CharaUpdate[] detailUpdates;

			public CharaUpdateWithTime(int date, CharaUpdate... detailUpdates) {
				this.date = LocalDate.parse("20" + date, DateTimeFormatter.BASIC_ISO_DATE);
				this.detailUpdates = detailUpdates;
			}

			LocalDate getDate() {
				return this.date;
			}

			JsonObjectBuilder toJson() {
				return Json.createObjectBuilder()
				           .add("date", JsonUtils.toJsonArray(
						           this.date.getYear(), this.date.getMonthValue(), this.date.getDayOfMonth(),
						           this.date.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, Locale.CHINA)
				           ))
				           .add("detail", JsonUtils.toJsonObject(
						           ExStreamUtils.of(this.detailUpdates).collect(Collectors.groupingBy(CharaUpdate::getKey)),
						           Map.Entry::getKey,
						           cus -> JsonUtils.toJsonArray(cus.getValue(), LambdaUtils.andThen(CharaUpdate::toJson, JsonObjectBuilder::build))
				           ));
			}
		}
		saveData.accept("update", JsonUtils.toJsonArray(
				ExStreamUtils.of(
						new CharaUpdateWithTime(
								180514,
								new CharaUpdateSublimation(111111),
								new CharaUpdateSublimation(111111),
								new CharaUpdateSublimation(111111),
								new CharaUpdateSublimation(111111)
						)
				)
				             .sortedObj(CharaUpdateWithTime::getDate)
				             .mapChain(CharaUpdateWithTime::toJson, JsonObjectBuilder::build)
		                )
		);
	}

	static abstract class CharaUpdate {
		final int[] charaIds;

		public CharaUpdate(int... charaIds) {
			this.charaIds = charaIds;
		}

		JsonObjectBuilder toJson() {
			return Json.createObjectBuilder().add("charas", JsonUtils.toJsonArray(this.charaIds));
		}

		abstract String getKey();
	}

	//实装 , 三种
	static abstract class CharaUpdateRelease extends CharaUpdate {
		final String howToGet;

		public CharaUpdateRelease(String howToGet, int... charaIds) {
			super(charaIds);
			this.howToGet = howToGet;
		}

		@Override
		JsonObjectBuilder toJson() {
			return super.toJson().add("howToGet", this.howToGet);
		}

		@Override
		String getKey() {
			return "release";
		}

		//卡池
		static class CharaUpdateReleaseGacha extends CharaUpdateRelease {
			public CharaUpdateReleaseGacha(int... charaIds) {
				super("卡池", charaIds);
			}
		}

		//活动
		static class CharaUpdateReleaseEvent extends CharaUpdateRelease {
			public CharaUpdateReleaseEvent(int... charaIds) {
				super("活动", charaIds);
			}
		}

		//其它[周边特典,联动,etc]
		static class CharaUpdateReleaseOther extends CharaUpdateRelease {
			public CharaUpdateReleaseOther(String howToGet, int... charaIds) {
				super(howToGet, charaIds);
			}
		}
	}

	//开花
	static class CharaUpdateBloom extends CharaUpdate {
		public CharaUpdateBloom(int... charaIds) {
			super(charaIds);
		}

		@Override
		String getKey() {
			return "bloom";
		}

		//开花(能力)
		static class CharaUpdateBloomAbility extends CharaUpdateBloom {
			public CharaUpdateBloomAbility(int... charaIds) {
				super(charaIds);
			}
		}

		//开花(立绘)
		static class CharaUpdateBloomImage extends CharaUpdateBloom {
			public CharaUpdateBloomImage(int... charaIds) {
				super(charaIds);
			}
		}
	}

	//升华
	static class CharaUpdateSublimation extends CharaUpdate {
		public CharaUpdateSublimation(int... charaIds) {
			super(charaIds);
		}

		@Override
		String getKey() {
			return "sublimation";
		}
	}

	//其它 , 比如 技能能力的调整 , 画像的调整 etc
	static class CharaUpdateOther extends CharaUpdate {
		public CharaUpdateOther(int... charaIds) {
			super(charaIds);
		}

		@Override
		String getKey() {
			return "other";
		}
	}
}
