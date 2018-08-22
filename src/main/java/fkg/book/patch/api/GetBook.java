package fkg.book.patch.api;

import com.moekagari.tool.json.JsonUtils;
import fkg.book.temp.AbstractCharaData;
import fkg.book.masterdata.CharacterBook;
import fkg.book.masterdata.GetMasterData;
import fkg.book.patch.server.FKGBOOKApiHandler;
import org.eclipse.jetty.client.api.Response;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

public class GetBook extends FKGBOOKApiHandler {
	public final static String URI = "/api/v1/character/getBook";

	public GetBook() {
		super(URI);
	}

	@Override
	public void onHeaders(Response proxyResponse, HttpServletResponse httpResponse, BiConsumer<Response, HttpServletResponse> defaultOnHeaders) {
	}

	@Override
	public void onContent(HttpServletResponse httpResponse, byte[] buffer, int offset, int length) throws IOException {
	}

	@Override
	public void onSuccess(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Map<String, String> headers, ByteArrayOutputStream requestBody, ByteArrayOutputStream responseBody) throws IOException {
		byte[] bytes;
		bytes = responseBody.toByteArray();
		//FileUtils.write(new File("GetBook.data"), bytes);
		bytes = Optional.of(bytes)
		                //.flatMap(ZLib::decompressOptional)
		                //.flatMap(Base64::decompress)
		                .map(GetBook::getAllcgString)
		                //.flatMap(Base64::compress)
		                //.flatMap(ZLib::compressOptional)
		                .get();

		int length = bytes.length;
		headers.forEach((name, value) -> {
			if("Content-Length".equals(name)) {
				httpResponse.addHeader("Content-Length", String.valueOf(length));
			} else {
				httpResponse.addHeader(name, value);
			}
		});
		httpResponse.getOutputStream().write(bytes);
	}

	@Override
	public boolean storeResponseBody() {
		return true;
	}

	@Override
	public boolean storeResponseHeaders() {
		return true;
	}

	private static byte[] getAllcgString(byte[] bytes) {
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		JsonObject json = Json.createReader(new ByteArrayInputStream(bytes)).readObject();

		json = JsonUtils.replace(json, "userCharacterBookList", userCharacterBookListValue -> {
			return JsonUtils.toJsonArray(
					IntStream.concat(
							IntStream.of(
									// 三灵兽
									5000, 5010, 5020,
									// 三灵兽(大)
									5030, 5040, 5050,
									// 强化灵
									109913, 109914, 109915, 109916, 109917, 109918, 109919, 109920, 109921, 109922, 109923, 109924, 109925,
									// 进化龙
									109926, 109927, 109928, 109929, 109930, 109931, 109932, 109933, 109934, 109935, 109936,
									// 开花材料
									109949, 109950, 109951, 109952, 109953, 109954, 109955, 109956, 109957, 109958, 109959, 109960//
							),
							GetMasterData.ALL_CHARA.values().stream()
							                       .filter(AbstractCharaData::isCharacter)
							                       .mapToInt(AbstractCharaData::getId)//
					)
					         .mapToObj(id -> {
						         return Json.createObjectBuilder()
						                    .add("characterId", id)
						                    .add("status", 1)
						                    .add("created", time);
					         }),
					JsonObjectBuilder::build
					/**/
			);
		});

		json = JsonUtils.replace(json, "userCharacterBookGroupList", userCharacterBookGroupListValue -> {
			return JsonUtils.toJsonArray(
					GetMasterData.MASTERBOOK.stream()
					                        .filter(CharacterBook::showInBook)
					                        .mapToInt(CharacterBook::getId)
					                        .mapToObj(bid -> {
						                        return Json.createObjectBuilder()
						                                   .add("id", 10000000)
						                                   .add("characterBookId", bid)
						                                   .add("status", 4)
						                                   .add("favorabilityValue", 100)
						                                   .add("created", time);
					                        }),
					JsonObjectBuilder::build
					/**/
			);
		});

		json = JsonUtils.replace(json, "userCharacterBookMaxFavorabilitySceneDtoList", userCharacterBookMaxFavorabilitySceneDtoListValue -> {
			return JsonUtils.toJsonArray(
					GetMasterData.MASTERBOOK.stream()
					                        .filter(CharacterBook::hadBloomed)
					                        .mapToInt(CharacterBook::getId)
					                        .mapToObj(bid -> {
						                        return Json.createObjectBuilder()
						                                   .add("characterBookId", bid)
						                                   .add("secondFavorabilityStatus", 1);
					                        }),
					JsonObjectBuilder::build
					/**/
			);
		});

		return json.toString().getBytes();
	}
}
