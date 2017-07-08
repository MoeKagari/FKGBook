package fkg.patch.api.other;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.api.Response;

import server.CommunicationHandler;
import tool.GZIP;

public class ALLCGPatchForRedCorreption extends CommunicationHandler {
	public ALLCGPatchForRedCorreption() {
		super(getServerName(), getUri());
	}

	public static String getServerName() {
		return "www.rc.dmmgames.com";
	}

	public static String getUri() {
		return "/datastore/mget";
	}

	@Override
	public void onHeaders(Response proxyResponse, HttpServletResponse httpResponse, BiConsumer<Response, HttpServletResponse> defaultOnHeaders) {}

	@Override
	public void onContent(HttpServletResponse httpResponse, byte[] buffer, int offset, int length) throws IOException {}

	@Override
	public void onSuccess(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Map<String, String> headers, ByteArrayOutputStream requestBody, ByteArrayOutputStream responseBody) throws IOException {
		byte[] bytes = responseBody.toByteArray();
		if (httpRequest.getMethod().equalsIgnoreCase("post")) {
			bytes = GZIP.decompress(bytes);
			bytes = patch(bytes);
			bytes = GZIP.compress(bytes);
		}
		int length = bytes.length;
		headers.forEach((name, value) -> {
			if ("Content-Length".equals(name)) {
				httpResponse.addHeader("Content-Length", String.valueOf(length));
			} else {
				httpResponse.addHeader(name, value);
			}
		});
		httpResponse.getOutputStream().write(bytes, 0, bytes.length);
	}

	@Override
	public boolean storeResponseBody() {
		return true;
	}

	@Override
	public boolean storeHeaders() {
		return true;
	}

	private static final int[] ids = { 10754, 50181, 20485, 40454, 60934, 50694, 10764, 20494, 40464, 50704, 60944, 10773, 20504, 30233, 40473, 60954, 50714, 10784, 10273, 20514, 40484, 50724, 60964, 10794, 20524, 30253, 40494, 50734,
			60974, 50223, 10804, 40502, 20534, 50744, 60984, 60474, 10814, 10302, 20543, 50753, 50242, 40514, 30274, 60994, 60484, 10824, 20041, 20553, 30284, 40524, 61004, 50764, 50253, 60494, 10834, 20563, 40534, 50774, 61014,
			60504, 10844, 10333, 20574, 40544, 50784, 61024, 10854, 20584, 40554, 30314, 61034, 50794, 10864, 20081, 20594, 40564, 50804, 61044, 60023, 10874, 50810, 20092, 20604, 40574, 61054, 50304, 10884, 10374, 20614, 40583,
			30343, 40071, 61064, 60041, 10894, 20624, 20112, 40082, 30354, 40594, 61074, 20634, 30363, 40604, 61084, 20644, 40614, 30374, 61094, 60072, 20654, 61103, 30384, 40624, 20664, 20152, 61113, 30394, 40634, 60092, 60604,
			20674, 20162, 40644, 30404, 61124, 20683, 10443, 30414, 40654, 61134, 50894, 50383, 60113, 20181, 20694, 30423, 40152, 40664, 61144, 20704, 40673, 40162, 30434, 61154, 50403, 20714, 40171, 40683, 30444, 61164, 20724,
			40693, 40181, 20214, 30454, 61174, 20222, 20734, 30464, 61184, 10502, 20744, 20233, 61193, 30474, 60173, 10514, 20754, 30484, 40213, 10012, 10524, 30494, 60702, 60191, 10532, 10021, 30504, 40233, 50473, 10542, 30513,
			60212, 50484, 10554, 20283, 30524, 40254, 50494, 10051, 10564, 30022, 30534, 50504, 60233, 10573, 30544, 50513, 10584, 30041, 30554, 50524, 10594, 20324, 30564, 50533, 50023, 10604, 30061, 30574, 50544, 60273, 10614,
			10102, 30584, 40313, 50041, 50554, 60283, 10622, 50560, 30081, 20353, 30594, 60804, 10634, 20363, 30604, 50573, 10644, 30613, 20374, 50583, 40344, 60314, 10654, 10142, 20383, 30624, 40353, 50082, 50594, 10153, 20394,
			30634, 40364, 50604, 10674, 30644, 10164, 40373, 50614, 10684, 30653, 30143, 40384, 50112, 50624, 60353, 10181, 10694, 30664, 40394, 50634, 10704, 30674, 30162, 40404, 50644, 10714, 30684, 40414, 50654, 10723, 10212,
			40420, 30694, 60904, 50664, 10732, 10222, 30704, 60912, 50672, 40434, 50164, 10744, 60923, 50684 };

	private static byte[] patch(byte[] bytes) {
		JsonObject source = Json.createReader(new ByteArrayInputStream(bytes)).readObject();
		if (source.getJsonObject("contents").containsKey("playerCharacter") == false) {
			return bytes;
		}

		JsonObjectBuilder builder = Json.createObjectBuilder();
		source.forEach(builder::add);
		{
			JsonObject contents = source.getJsonObject("contents");

			JsonObjectBuilder contentsBuilder = Json.createObjectBuilder();
			contents.forEach(contentsBuilder::add);
			{
				JsonObject playerCharacter = contents.getJsonObject("playerCharacter");

				JsonObjectBuilder playerCharacterBuilder = Json.createObjectBuilder();
				playerCharacter.forEach(playerCharacterBuilder::add);
				{
					JsonArray after = playerCharacter.getJsonArray("after");
					JsonArrayBuilder afterBuilder = Json.createArrayBuilder();
					{
						after.forEach(chara -> {
							afterBuilder.add(newCharacterJsonObject((JsonObject) chara));
						});
						//添加游戏中自己还没有图鉴的角色
						for (int id : ids) {
							afterBuilder.add(newCharacterJsonObject(String.valueOf(id)));
						}
					}
					playerCharacterBuilder.add("after", afterBuilder);
				}
				contentsBuilder.add("playerCharacter", playerCharacterBuilder);
			}
			builder.add("contents", contentsBuilder);
		}

		return builder.build().toString().getBytes();
	}

	private static JsonObject newCharacterJsonObject(JsonObject json) {
		/**
		 {
						"characterId": "10012",
						"love": "2000",
						"flagAbility": "0",
						"flagEvent": "0"
					}
		 */

		JsonObjectBuilder builder = Json.createObjectBuilder();
		json.forEach(builder::add);
		builder.add("love", "500");
		return builder.build();
	}

	private static JsonObject newCharacterJsonObject(String id) {
		JsonObjectBuilder builder = Json.createObjectBuilder();

		builder.add("characterId", id);
		builder.add("love", "500");
		builder.add("flagAbility", "0");
		builder.add("flagEvent", "0");

		return builder.build();
	}

}
