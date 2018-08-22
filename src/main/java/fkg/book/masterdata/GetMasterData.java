package fkg.book.masterdata;

import com.moekagari.tool.lambda.LambdaUtils;
import com.moekagari.tool.acs.ExStreamUtils;
import fkg.book.temp.AbstractCharaData;
import fkg.book.patch.api.GetMaster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GetMasterData {
	public static final List<CharacterSkill> MASTERSKILL = GetMasterData.get("masterCharacterSkill", CharacterSkill::new);
	public static final List<CharacterLeaderSkill> MASTERLEADERSKILL = GetMasterData.get("masterCharacterLeaderSkill", CharacterLeaderSkill::new);
	public static final List<MasterStory> MASTERSTORY = GetMasterData.get("masterStory", MasterStory::new);
	public static final List<CharacterQuest> MASTERCHARACTERSTORY = GetMasterData.get("masterCharacterQuest", CharacterQuest::new);
	public static final List<CharacterBook> MASTERBOOK = GetMasterData.get("masterCharacterBook", CharacterBook::new);
	public static final List<CharacterLeaderSkillDescription> MASTERLEADERSKILLDESCRIPTION = GetMasterData.get("masterCharacterLeaderSkillDescription", CharacterLeaderSkillDescription::new);
	public static final List<CharacterMypageVoiceResourceGroup> MASTERCHARACTERMYPAGEVOICERESOURCEGROUP = GetMasterData
			.get("masterCharacterMypageVoiceResourceGroup", CharacterMypageVoiceResourceGroup::new);
	public static final List<CharacterTextResource> MASTERCHARACTERTEXTRESOURCE = GetMasterData.get("masterCharacterTextResource", CharacterTextResource::new);
	public static final List<CharacterMypageVoiceResource> MASTERCHARACTERMYPAGEVOICERESOURCE = GetMasterData.get("masterCharacterMypageVoiceResource", CharacterMypageVoiceResource::new);
	//在最后
	public static final Map<Integer, AbstractCharaData> ALL_CHARA = Collections.unmodifiableMap(
			ExStreamUtils.stream(GetMasterData.get("masterCharacter", CharacterInformation::new))
			             .toMap(CharacterInformation::getId, LambdaUtils::returnSelf)
			/**/);

	@SafeVarargs
	private static <E> List<E> get(String key, Function<String, E> gdd, Predicate<E>... filters) {
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(GetMaster.dir + "\\" + key + ".csv")), "utf-8"))) {
			Stream<E> stream = reader.lines().map(gdd);
			for(Predicate<E> filter : filters) {
				stream = stream.filter(filter);
			}
			ArrayList<E> list = stream.collect(Collectors.toCollection(ArrayList::new));
			return Collections.unmodifiableList(list);
		} catch(IOException e) {
			throw new RuntimeException(key + " master data init exception", e);
		}
	}

	static class GetMasterDataLineSpliter {
		private final Iterator<String> iter;

		GetMasterDataLineSpliter(String line) {
			this.iter = ExStreamUtils.of(line.split(",", -1)).iterator();
		}

		int nextInt() {
			return Integer.parseInt(this.next());
		}

		String next() {
			return this.iter.next();
		}
	}
}
