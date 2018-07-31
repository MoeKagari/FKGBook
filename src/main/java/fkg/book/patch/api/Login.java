package fkg.book.patch.api;

import com.moekagari.tool.io.FileUtils;
import com.moekagari.tool.json.JsonUtils;
import fkg.book.patch.server.FKGBOOKApiHandler;
import org.eclipse.jetty.client.api.Response;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class Login extends FKGBOOKApiHandler {
	public final static String URI = "/api/v1/user/login";
	private final int futuanzhangID;

	public Login(int futuanzhangID) {
		super(URI);
		this.futuanzhangID = futuanzhangID;
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
		FileUtils.write(new File("Login.data"), bytes);
		bytes = Optional.of(bytes)
		                //.flatMap(ZLib::decompressOptional)
		                //.flatMap(Base64::decompress)
		                .map(this::patch)
		                //.flatMap(Base64::compress)
		                //.flatMap(ZLib::compressOptional)
		                .get();

		int contentLength = bytes.length;
		headers.forEach((name, value) -> {
			if("Content-Length".equals(name)) {
				httpResponse.addHeader("Content-Length", String.valueOf(contentLength));
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

	private byte[] patch(byte[] bytes) {
		JsonObject json = Json.createReader(new ByteArrayInputStream(this.replace(bytes, '\\', '#'))).readObject();

		json = JsonUtils.replace(json, "user", userValue -> {
			return JsonUtils.replace((JsonObject) userValue, "deputyLeaderCharacterId", deputyLeaderCharacterIdValue -> {
				return JsonUtils.createValue(this.futuanzhangID);
			});
		});

		return this.replace(json.toString().getBytes(), '#', '\\');
	}

	private byte[] replace(byte[] bytes, char c, char d) {
		for(int i = 0; i < bytes.length; i++) {
			if(bytes[i] == c) {
				bytes[i] = (byte) d;
			}
		}
		return bytes;
	}
}
