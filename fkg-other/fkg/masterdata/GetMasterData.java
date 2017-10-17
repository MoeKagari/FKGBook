package fkg.masterdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import fkg.patch.api.GetMaster;

public interface GetMasterData {
	public static final List<CharacterSkill> MASTERSKILL = GetMasterData.get("masterCharacterSkill", CharacterSkill::new);
	public static final List<CharacterLeaderSkill> MASTERLEADERSKILL = GetMasterData.get("masterCharacterLeaderSkill", CharacterLeaderSkill::new);
	public static final List<MasterStory> MASTERSTORY = GetMasterData.get("masterStory", MasterStory::new);
	public static final List<CharacterQuest> MASTERCHARACTERSTORY = GetMasterData.get("masterCharacterQuest", CharacterQuest::new);
	public static final List<CharacterBook> MASTERBOOK = GetMasterData.get("masterCharacterBook", CharacterBook::new);
	public static final List<CharacterLeaderSkillDescription> MASTERLEADERSKILLDESCRIPTION = GetMasterData.get("masterCharacterLeaderSkillDescription", CharacterLeaderSkillDescription::new);
	public static final List<CharacterInformation> MASTERCHARACTER = GetMasterData.get("masterCharacter", CharacterInformation::new);

	static <T> List<T> get(String key, Function<String, T> gdd) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(GetMaster.dir + "\\" + key + ".csv")), "utf-8"))) {
			return reader.lines().map(gdd).collect(Collectors.toCollection(ArrayList::new));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
