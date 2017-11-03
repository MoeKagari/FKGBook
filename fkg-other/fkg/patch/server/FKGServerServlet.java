package fkg.patch.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.client.api.Response;

import fkg.config.AppConfig;
import fkg.patch.api.GetBook;
import fkg.patch.api.GetMaster;
import fkg.patch.api.GetTurningCardSheet;
import fkg.patch.api.Login;
import server.CommunicationHandler;
import server.ProxyServerServlet;
import tool.compress.GZIP;
import tool.function.FunctionUtils;

public class FKGServerServlet extends ProxyServerServlet {
	public FKGServerServlet(IntSupplier listenPort, BooleanSupplier useProxy, Supplier<String> proxyHost, IntSupplier proxyPort) {
		super(listenPort, useProxy, proxyHost, proxyPort);
	}

	Set<String> ids = new TreeSet<>(Comparator.comparing(FunctionUtils::returnSelf));

	@Override
	protected void service(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
		String serverName = httpRequest.getServerName();
		String uri = httpRequest.getRequestURI();

		byte[] bytes = null;
		if ("www.rc.dmmgames.com".equals(serverName)) {
			String filename = null;
			if ("/files/webgl/production/20170928163115/MB.datagz".equals(uri)) {
				filename = "MB.datagz";
			} else if ("/files/webgl/production/20170928163115/MB.jsgz".equals(uri)) {
				filename = "MB.jsgz";
			}

			if (filename != null) try {
				bytes = FileUtils.readFileToByteArray(new File(filename).getAbsoluteFile());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		if (bytes != null) {
			AsyncContext asyncContext = httpRequest.startAsync();
			asyncContext.setTimeout(0);
			try {
				httpResponse.getOutputStream().write(bytes);
			} catch (Exception ex) {
				ex.printStackTrace();
				httpResponse.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
			} finally {
				asyncContext.complete();
			}
			return;
		}

		String pa = "/files/AssetBundles/5.3/WebGL/omake/omake_thumb_";
		if (uri.startsWith(pa)) {
			this.ids.add(uri.substring(pa.length(), pa.length() + 5));
			//System.out.println(String.join(",", this.ids));
		}

		super.service(httpRequest, httpResponse);
	}

	@Override
	public CommunicationHandler getHandler(String serverName, String uri) {
		if (FKGApiHandler.getServerName().equals(serverName)) {
			if (Login.getUri().equals(uri)) {
				if (AppConfig.isTihuan()) {
					return new Login();
				}
			} else

			if (GetBook.getUri().equals(uri)) {
				if (AppConfig.isAllCG()) {
					return new GetBook();
				}
			} else

			if (GetTurningCardSheet.getUri().equals(uri)) {
				return new GetTurningCardSheet();
			} else

			if (GetMaster.getUri().equals(uri)) {
				return new GetMaster();
			}
		} else

		if (ALLCGPatchForRedCorreption.getServerName().equals(serverName)) {
			if (ALLCGPatchForRedCorreption.getUri().equals(uri)) {
				return new ALLCGPatchForRedCorreption();
			}
		}

		return super.getHandler(serverName, uri);
	}

	public static void main(String[] args) {
		Set<Integer> ids = new TreeSet<>();
		ids.addAll(Arrays.asList(10021, 10051, 10102, 10142, 10164, 10181, 10222, 10273, 10302, 10333, 10374, 10443, 10502, 10532, 10554, 10564, 10573, 10584, 10594, 10634, 10644, 10654, 10684, 10694, 10704, 10714, 10732, 10744,
				10754, 10764, 10773, 10784, 10794, 10824, 10834, 10864, 10874, 10884, 10904, 10914, 10924, 20041, 20081, 20092, 20112, 20152, 20162, 20214, 20283, 20353, 20363, 20374, 20383, 20485, 20494, 20504, 20514,
				20524, 20534, 20543, 20553, 20584, 20604, 20614, 20644, 20654, 20664, 20674, 20694, 20704, 20714, 20724, 20734, 20754, 20764, 20774, 20783, 20794, 30061, 30143, 30162, 30233, 30253, 30274, 30314, 30343,
				30354, 30363, 30374, 30384, 30404, 30414, 30423, 30434, 30454, 30464, 30474, 30484, 30494, 30504, 30513, 30524, 30534, 30554, 30564, 30574, 30584, 30594, 30604, 30613, 30624, 30634, 30653, 30674, 30694,
				30704, 30713, 30724, 30734, 30744, 30754, 40071, 40082, 40152, 40171, 40181, 40213, 40233, 40254, 40344, 40353, 40364, 40394, 40404, 40414, 40434, 40464, 40473, 40484, 40524, 40544, 40564, 40574, 40594,
				40624, 40644, 40654, 40664, 40673, 40683, 40704, 40714, 40724, 40734,
				10754, 50181, 20485, 40454, 60934, 50694, 10764, 20494, 40464, 50704, 60944, 10773, 20504, 30233, 40473, 60954, 50714, 10784, 10273, 20514, 40484, 50724, 60964, 10794, 20524, 30253, 40494,
				50734, 60974, 50223, 10804, 40502, 20534, 50744, 60984, 60474, 10814, 10302, 20543, 50753, 50242, 40514, 30274, 60994, 60484, 10824, 20041, 20553, 30284, 40524, 61004, 50764, 50253, 60494, 10834, 20563,
				40534, 50774, 61014, 60504, 10844, 10333, 20574, 40544, 50784, 61024, 10854, 20584, 40554, 30314, 61034, 50794, 10864, 20081, 20594, 40564, 50804, 61044, 60023, 10874, 50810, 20092, 20604, 40574, 61054,
				50304, 10884, 10374, 20614, 40583, 30343, 40071, 61064, 60041, 10894, 20624, 20112, 40082, 30354, 40594, 61074, 20634, 30363, 40604, 61084, 20644, 40614, 30374, 61094, 60072, 20654, 61103, 30384, 40624,
				20664, 20152, 61113, 30394, 40634, 60092, 60604, 20674, 20162, 40644, 30404, 61124, 20683, 10443, 30414, 40654, 61134, 50894, 50383, 60113, 20181, 20694, 30423, 40152, 40664, 61144, 20704, 40673, 40162,
				30434, 61154, 50403, 20714, 40171, 40683, 30444, 61164, 20724, 40693, 40181, 20214, 30454, 61174, 20222, 20734, 30464, 61184, 10502, 20744, 20233, 61193, 30474, 60173, 10514, 20754, 30484, 40213, 10012,
				10524, 30494, 60702, 60191, 10532, 10021, 30504, 40233, 50473, 10542, 30513, 60212, 50484, 10554, 20283, 30524, 40254, 50494, 10051, 10564, 30022, 30534, 50504, 60233, 10573, 30544, 50513, 10584, 30041,
				30554, 50524, 10594, 20324, 30564, 50533, 50023, 10604, 30061, 30574, 50544, 60273, 10614, 10102, 30584, 40313, 50041, 50554, 60283, 10622, 50560, 30081, 20353, 30594, 60804, 10634, 20363, 30604, 50573,
				10644, 30613, 20374, 50583, 40344, 60314, 10654, 10142, 20383, 30624, 40353, 50082, 50594, 10153, 20394, 30634, 40364, 50604, 10674, 30644, 10164, 40373, 50614, 10684, 30653, 30143, 40384, 50112, 50624,
				60353, 10181, 10694, 30664, 40394, 50634, 10704, 30674, 30162, 40404, 50644, 10714, 30684, 40414, 50654, 10723, 10212, 40420, 30694, 60904, 50664, 10732, 10222, 30704, 60912, 50672, 40434, 50164, 10744,
				60923, 50684,
				50023, 50041, 50082, 50112, 50223, 50304, 50383, 50403, 50473, 50484, 50494, 50504, 50513, 50524, 50533, 50573, 50594, 50604, 50624, 50654, 50664, 50672, 50694, 50704, 50714, 50724, 50734, 50744, 50753,
				50764, 50794, 50804, 50824, 50834, 50844, 50854, 50864, 50874, 50884, 50894, 60041, 60092, 60173, 60191, 60212, 60233, 60273, 60283, 60314, 60353, 60484, 60494, 60504, 60604, 60702, 60804, 60904, 60923,
				60934, 60944, 60954, 60964, 60974, 60984, 60994, 61004, 61014, 61024, 61044, 61054, 61064, 61074, 61084, 61094, 61113, 61124, 61134, 61144, 61154, 61174, 61193, 61204, 61214, 61223, 61234, 61244, 61254));
		System.out.println(String.join(",", ids.stream().map(Object::toString).toArray(String[]::new)));
	}

	/** redcorruption的全CG */
	public static class ALLCGPatchForRedCorreption extends CommunicationHandler {
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
				if ("gzip".equalsIgnoreCase(headers.get("Content-Encoding"))) {
					bytes = GZIP.decompress(bytes);
					bytes = patch(bytes);
					bytes = GZIP.compress(bytes);
				} else {
					bytes = patch(bytes);
				}
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
		public boolean storeResponseHeaders() {
			return true;
		}

		private static final int[] ids = { 10012, 10021, 10051, 10102, 10142, 10153, 10164, 10181, 10212, 10222, 10273, 10302, 10333,
				10374, 10443, 10502, 10514, 10524, 10532, 10542, 10554, 10564, 10573, 10584, 10594, 10604, 10614, 10622, 10634,
				10644, 10654, 10674, 10684, 10694, 10704, 10714, 10723, 10732, 10744, 10754, 10764, 10773, 10784, 10794, 10804,
				10814, 10824, 10834, 10844, 10854, 10864, 10874, 10884, 10894, 10904, 10914, 10924, 20041, 20081, 20092, 20112,
				20152, 20162, 20181, 20214, 20222, 20233, 20283, 20324, 20353, 20363, 20374, 20383, 20394, 20485, 20494, 20504,
				20514, 20524, 20534, 20543, 20553, 20563, 20574, 20584, 20594, 20604, 20614, 20624, 20634, 20644, 20654, 20664,
				20674, 20683, 20694, 20704, 20714, 20724, 20734, 20744, 20754, 20764, 20774, 20783, 20794, 30022, 30041, 30061,
				30081, 30143, 30162, 30233, 30253, 30274, 30284, 30314, 30343, 30354, 30363, 30374, 30384, 30394, 30404, 30414,
				30423, 30434, 30444, 30454, 30464, 30474, 30484, 30494, 30504, 30513, 30524, 30534, 30544, 30554, 30564, 30574,
				30584, 30594, 30604, 30613, 30624, 30634, 30644, 30653, 30664, 30674, 30684, 30694, 30704, 30713, 30724, 30734,
				30744, 30754, 40071, 40082, 40152, 40162, 40171, 40181, 40213, 40233, 40254, 40313, 40344, 40353, 40364, 40373,
				40384, 40394, 40404, 40414, 40420, 40434, 40454, 40464, 40473, 40484, 40494, 40502, 40514, 40524, 40534, 40544,
				40554, 40564, 40574, 40583, 40594, 40604, 40614, 40624, 40634, 40644, 40654, 40664, 40673, 40683, 40693, 40704,
				40714, 40724, 40734, 50023, 50041, 50082, 50112, 50164, 50181, 50223, 50242, 50253, 50304, 50383, 50403, 50473,
				50484, 50494, 50504, 50513, 50524, 50533, 50544, 50554, 50560, 50573, 50583, 50594, 50604, 50614, 50624, 50634,
				50644, 50654, 50664, 50672, 50684, 50694, 50704, 50714, 50724, 50734, 50744, 50753, 50764, 50774, 50784, 50794,
				50804, 50810, 50824, 50834, 50844, 50854, 50864, 50874, 50884, 50894, 60023, 60041, 60072, 60092, 60113, 60173,
				60191, 60212, 60233, 60273, 60283, 60314, 60353, 60474, 60484, 60494, 60504, 60604, 60702, 60804, 60904, 60912,
				60923, 60934, 60944, 60954, 60964, 60974, 60984, 60994, 61004, 61014, 61024, 61034, 61044, 61054, 61064, 61074,
				61084, 61094, 61103, 61113, 61124, 61134, 61144, 61154, 61164, 61174, 61184, 61193, 61204, 61214, 61223, 61234,
				61244, 61254
		};

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
						JsonArrayBuilder afterBuilder = Json.createArrayBuilder();

						for (int id : ids) {
							afterBuilder.add(newCharacterJsonObject(String.valueOf(id)));
						}
						for (int id = 10000; id < 50000; id++) {
							//afterBuilder.add(newCharacterJsonObject(String.valueOf(id)));
						}
						for (int id = 50000; id < 100000; id++) {
							//afterBuilder.add(newCharacterJsonObject(String.valueOf(id)));
						}

						playerCharacterBuilder.add("after", afterBuilder);
					}
					contentsBuilder.add("playerCharacter", playerCharacterBuilder);
				}
				builder.add("contents", contentsBuilder);
			}

			return builder.build().toString().getBytes();
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

}
