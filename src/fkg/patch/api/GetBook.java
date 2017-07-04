package fkg.patch.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.api.Response;

import fkg.patch.CommunicateHandler.FKGApiHandler;
import fkg.patch.api.getMaster.CharacterBook;
import fkg.patch.api.getMaster.CharacterInformation;
import fkg.patch.api.getMaster.GetMasterData;
import tool.ZLibUtils;

public class GetBook extends FKGApiHandler {
	public GetBook() {
		super(getUri());
	}

	public static String getUri() {
		return "/api/v1/character/getBook";
	}

	@Override
	public void onHeaders(Response proxyResponse, HttpServletResponse httpResponse, BiConsumer<Response, HttpServletResponse> defaultOnHeaders) {}

	@Override
	public void onContent(HttpServletResponse httpResponse, byte[] buffer, int offset, int length) throws IOException {}

	@Override
	public void onSuccess(HttpServletRequest httpRequest, HttpServletResponse httpResponse, ByteArrayOutputStream requestBody, ByteArrayOutputStream responseBody) throws IOException {
		byte[] bytes = responseBody.toByteArray();
		bytes = ZLibUtils.decompress(bytes);
		bytes = Base64.getDecoder().decode(bytes);
		String version = Json.createReader(new ByteArrayInputStream(bytes)).readObject().getString("version");
		bytes = getAllcgString(version).getBytes();
		bytes = Base64.getEncoder().encode(bytes);
		bytes = ZLibUtils.compress(bytes);

		httpResponse.addHeader("Content-Type", "text/plain");
		httpResponse.addHeader("Content-Length", String.valueOf(bytes.length));
		httpResponse.getOutputStream().write(bytes, 0, bytes.length);
	}

	@Override
	public boolean storeResponseBody() {
		return true;
	}

	private static ArrayList<ArrayList<Integer>> getIDS() {
		ArrayList<ArrayList<Integer>> result = new ArrayList<>();
		ArrayList<Integer> cid = new ArrayList<>();
		ArrayList<Integer> bid = new ArrayList<>();
		ArrayList<Integer> sbid = new ArrayList<>();

		for (int id : new int[] {
				// 三灵兽
				5000, 5010, 5020,
				// 强化灵
				109913, 109914, 109915, 109916, 109917, 109918, 109919, 109920, 109921, 109922, 109923, 109924, 109925,
				// 进化龙
				109926, 109927, 109928, 109929, 109930, 109931, 109932, 109933, 109934, 109935, 109936,
				// 开花材料
				109949, 109950, 109951, 109952, 109953, 109954, 109955, 109956, 109957, 109958, 109959, 109960 //
		}) {
			cid.add(id);
		}

		List<GetMasterData> cis = GetMasterData.get(CharacterInformation.key, CharacterInformation::new);
		for (GetMasterData obj : cis) {
			if (obj instanceof CharacterInformation) {
				CharacterInformation ci = (CharacterInformation) obj;
				if (ci.isCharacter()) {
					cid.add(ci.getID());
				}
			}
		}

		List<GetMasterData> cbs = GetMasterData.get(CharacterBook.key, CharacterBook::new);
		for (GetMasterData obj : cbs) {
			if (obj instanceof CharacterBook) {
				CharacterBook cb = (CharacterBook) obj;
				int id = cb.getId();
				if (cb.showInBook()) bid.add(id);
				if (cb.isBloom()) sbid.add(id);
			}
		}

		result.add(cid);
		result.add(bid);
		result.add(sbid);
		return result;
	}

	private static String getAllcgString(String version) {
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		ArrayList<ArrayList<Integer>> IDS = getIDS();
		ArrayList<Integer> cid = IDS.get(0);
		ArrayList<Integer> bid = IDS.get(1);
		ArrayList<Integer> sbid = IDS.get(2);

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
		builder.add("serverTime", time);
		builder.add("version", version);

		return builder.build().toString();
	}
}
