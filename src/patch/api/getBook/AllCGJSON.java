package patch.api.getBook;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Function;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import patch.api.getMaster.CharacterBook;
import patch.api.getMaster.CharacterInformation;
import patch.api.getMaster.GameData;

public class AllCGJSON {

	private static ArrayList<ArrayList<Integer>> getIDS() {
		ArrayList<ArrayList<Integer>> result = new ArrayList<>();
		ArrayList<Integer> cid = new ArrayList<>();
		ArrayList<Integer> bid = new ArrayList<>();
		ArrayList<Integer> sbid = new ArrayList<>();
		/*---------------------------------------------*/
		for (int id : new int[] {
				// 三灵兽
				5000, 5010, 5020,
				// 强化灵
				109913, 109914, 109915, 109916, 109917, 109918, 109919, 109920, 109921, 109922, 109923, 109924, 109925,
				// 进化龙
				109926, 109927, 109928, 109929, 109930, 109931, 109932, 109933, 109934, 109935, 109936,
				// 开花材料
				109949, 109950, 109951, 109952, 109953, 109954, 109955, 109956, 109957, 109958, 109959, 109960 })
			cid.add(id);

		GameData[] cis = CharacterInformation.get();
		if (cis != null) {
			for (GameData obj : cis) {
				if (obj instanceof CharacterInformation) {
					CharacterInformation ci = (CharacterInformation) obj;
					if (ci.isCharacter()) cid.add(ci.getID());
				}
			}
		}
		/*---------------------------------------------*/
		GameData[] cbs = CharacterBook.get();
		if (cbs != null) {
			for (GameData obj : cbs) {
				if (obj instanceof CharacterBook) {
					CharacterBook cb = (CharacterBook) obj;
					int id = cb.getId();
					if (cb.showInBook()) bid.add(id);
					if (cb.isBloom()) sbid.add(id);
				}
			}
		}
		/*---------------------------------------------*/
		result.add(cid);
		result.add(bid);
		result.add(sbid);
		return result;
	}

	private static String getTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	private static String ALLCGJSON = null;

	public static String get() {
		if (ALLCGJSON != null) return ALLCGJSON;

		String time = getTime();
		ArrayList<ArrayList<Integer>> IDS = getIDS();
		if (IDS == null) return null;

		ArrayList<Integer> cid, bid, sbid;
		cid = IDS.get(0);
		bid = IDS.get(1);
		sbid = IDS.get(2);

		Function<Integer, JsonObjectBuilder> userCharacterBookListGetter = id -> {
			JsonObjectBuilder builder = Json.createObjectBuilder();
			builder.add("characterId", id);
			builder.add("status", 1);
			builder.add("created", time);
			return builder;
		};
		Function<Integer, JsonObjectBuilder> userCharacterBookGroupListGetter = id -> {
			JsonObjectBuilder builder = Json.createObjectBuilder();
			builder.add("id", 10000000);
			builder.add("characterBookId", id);
			builder.add("status", 4);
			builder.add("favorabilityValue", 100);
			builder.add("created", time);
			return builder;
		};
		Function<Integer, JsonObjectBuilder> userCharacterBookMaxFavorabilitySceneDtoListGetter = id -> {
			JsonObjectBuilder builder = Json.createObjectBuilder();
			builder.add("characterBookId", id);
			builder.add("secondFavorabilityStatus", 1);
			return builder;
		};

		JsonObjectBuilder builder = Json.createObjectBuilder();
		{
			JsonArrayBuilder userCharacterBookListBuilder = Json.createArrayBuilder();
			cid.forEach(id -> userCharacterBookListBuilder.add(userCharacterBookListGetter.apply(id)));
			builder.add("userCharacterBookList", userCharacterBookListBuilder);
		}
		{
			JsonArrayBuilder userCharacterBookGroupListBuilder = Json.createArrayBuilder();
			bid.forEach(id -> userCharacterBookGroupListBuilder.add(userCharacterBookGroupListGetter.apply(id)));
			builder.add("userCharacterBookGroupList", userCharacterBookGroupListBuilder);
		}
		{
			JsonArrayBuilder userCharacterBookMaxFavorabilitySceneDtoListBuilder = Json.createArrayBuilder();
			sbid.forEach(id -> userCharacterBookMaxFavorabilitySceneDtoListBuilder.add(userCharacterBookMaxFavorabilitySceneDtoListGetter.apply(id)));
			builder.add("userCharacterBookMaxFavorabilitySceneDtoList", userCharacterBookMaxFavorabilitySceneDtoListBuilder);
		}
		builder.addNull("errorMessage");
		builder.add("resultCode", "0");
		builder.add("buildVersion", "1.0.0");
		builder.add("serverTime", getTime());
		builder.add("version", "v1.76.0");
		return ALLCGJSON = builder.build().toString();
	}

}
