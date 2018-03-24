package fkg.book.masterdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.apache.commons.io.FileUtils;

import fkg.book.gui.AbstractCharaData;
import fkg.book.gui.AbstractCharaData.SkilltypeFilter;
import fkg.book.patch.api.GetMaster;
import tool.JsonUtils;
import tool.function.FunctionUtils;

public class GetMasterData {
	public static final List<CharacterSkill> MASTERSKILL = GetMasterData.get("masterCharacterSkill", CharacterSkill::new);
	public static final List<CharacterLeaderSkill> MASTERLEADERSKILL = GetMasterData.get("masterCharacterLeaderSkill", CharacterLeaderSkill::new);
	public static final List<MasterStory> MASTERSTORY = GetMasterData.get("masterStory", MasterStory::new);
	public static final List<CharacterQuest> MASTERCHARACTERSTORY = GetMasterData.get("masterCharacterQuest", CharacterQuest::new);
	public static final List<CharacterBook> MASTERBOOK = GetMasterData.get("masterCharacterBook", CharacterBook::new);
	public static final List<CharacterLeaderSkillDescription> MASTERLEADERSKILLDESCRIPTION = GetMasterData.get("masterCharacterLeaderSkillDescription", CharacterLeaderSkillDescription::new);
	public static final List<CharacterMypageVoiceResourceGroup> MASTERCHARACTERMYPAGEVOICERESOURCEGROUP = GetMasterData.get("masterCharacterMypageVoiceResourceGroup", CharacterMypageVoiceResourceGroup::new);
	public static final List<CharacterMypageVoiceResource> MASTERCHARACTERMYPAGEVOICERESOURCE = GetMasterData.get("masterCharacterMypageVoiceResource", CharacterMypageVoiceResource::new);
	//在最后
	public static final Map<Integer, CharacterInformation> ALL_CHARA = GetMasterData.get("masterCharacter", CharacterInformation::new).stream().collect(Collectors.toMap(CharacterInformation::getId, FunctionUtils::returnSelf));

	static <T> List<T> get(String key, Function<String, T> gdd) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(GetMaster.dir + "\\" + key + ".csv")), "utf-8"))) {
			return reader.lines().map(gdd).collect(Collectors.toCollection(ArrayList::new));
		} catch (IOException e) {
			throw new RuntimeException(key + " master data init exception", e);
		}
	}

	public static void main(String[] args) {
		for (SkilltypeFilter value : SkilltypeFilter.values()) {
			System.out.println(String.format("\"%s\":%d,", value.skilltypeString, value.skilltype));
		}

		JsonObjectBuilder builder = Json.createObjectBuilder()
				//版本,即 游戏的更新日期
				.add("version", "2018.1.22")
				.add("masterdata", JsonUtils.arrayToJsonArray(
						GetMasterData.ALL_CHARA.values()
								.stream()
								.filter(AbstractCharaData::canShowInApplication)
								.map(chara -> {
									return Json.createObjectBuilder()

											.add("id", chara.getId())
											.add("name", chara.getName())
											.add("rarity", chara.getRarity())
											.add("oeb", chara.getOeb())
											.add("attackAttribute", chara.getAttackAttribute())
											.add("move", chara.getMove())
											.add("hp", JsonUtils.arrayToJsonArray(chara.getHP()))
											.add("attack", JsonUtils.arrayToJsonArray(chara.getAttack()))
											.add("defense", JsonUtils.arrayToJsonArray(chara.getDefense()))
											.add("country", chara.getCountry())
											.add("version", chara.getVersion())
											.add("bloomNumber", 0)
											.add("kariBloom", chara.getKariBloom())
											.add("hasBloom", chara.hasBloom())
											.add("bid", chara.getBid())

											//技能
											.add("skill", JsonUtils.arrayToJsonArray(chara.getSkill().getName(), chara.getSkill().getEffect()))

											//能力
											.add("ability", JsonUtils.arrayToJsonArray(chara.getAbility().abilityArray,
													ability -> Json.createArrayBuilder().add(ability.getType()).add(ability.getDescription()).build()))
											.build();
								})
								.toArray(JsonObject[]::new)
		/**/));
		try {
			FileUtils.write(new File("masterdata.json"), builder.build().toString(), Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static class GetMasterDataLineSpliter {
		private final Iterator<String> iter;

		GetMasterDataLineSpliter(String line) {
			this.iter = FunctionUtils.stream(line.split(",", -1)).iterator();
		}

		int nextInt() {
			return Integer.parseInt(this.next());
		}

		String next() {
			return this.iter.next();
		}
	}
}
