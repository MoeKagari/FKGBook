package patch.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import gui.GuiConfig;
import patch.Transfer;
import tool.HTTPUtil;
import tool.ZLibUtils;

public class Login implements ApiResponse {
	public final static String key = "flower-knight-girls.co.jp/api/v1/user/login";

	@Override
	public void deal(byte[] bytes) {}

	@Override
	public void response(Transfer transfer) {
		if (GuiConfig.patch() == false) {
			transfer.handle();
			return;
		}

		Integer target = GuiConfig.getFutuanzhangID();
		if (GuiConfig.isTihuan() == false || target == null) {
			transfer.handle();
			return;
		}

		new Thread(() -> {
			try {
				byte[] buffer = new byte[1024];
				int len;
				while ((len = transfer.readCTA(buffer)) != -1) {
					transfer.writeCTA(buffer, 0, len);
				}
			} catch (IOException e) {
				System.out.println("c to a ´íÎó:\n" + transfer.getHeader().trim());
				e.printStackTrace();
			} finally {
				transfer.countDown();
			}
		}).start();

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			byte[] buffer = new byte[1024];
			int len;
			while ((len = transfer.readATC(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}

			byte[] bytes = baos.toByteArray();
			byte[] header = HTTPUtil.getHeader(bytes);
			byte[] body = HTTPUtil.getBody(bytes, true);
			body = ZLibUtils.decompress(body);
			body = Base64.getDecoder().decode(body);
			body = patch(body, target);
			body = Base64.getEncoder().encode(body);
			body = ZLibUtils.compress(body);

			for (byte[] by : new byte[][] {//
					header,//
					Integer.toHexString(body.length).getBytes(),//
					"\r\n".getBytes(),//
					body,//
					"\r\n".getBytes(),//
					"0\r\n\r\n".getBytes(),//
			}) {
				transfer.writeATC(by, 0, by.length);
			}
		} catch (IOException e) {
			System.out.println("a to c ´íÎó:\n" + transfer.getHeader().trim());
		} finally {
			transfer.countDown();
		}
	}

	private static byte[] patch(byte[] body, int target) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		JsonObject json = Json.createReader(new ByteArrayInputStream(replace(body, '\\', '#'))).readObject();

		json.forEach((key, value) -> {
			if ("user".equals(key) == false) {
				builder.add(key, value);
			}
		});
		if (json.containsKey("user")) {
			JsonObject user = json.getJsonObject("user");
			JsonObjectBuilder userBuilder = Json.createObjectBuilder();
			user.forEach((key, value) -> {
				if ("deputyLeaderCharacterId".equals(key)) {
					userBuilder.add(key, target);
				} else if ("deputyLeaderUserCharacterId".equals(key)) {
					userBuilder.add(key, 22000000000L);
				} else {
					userBuilder.add(key, value);
				}
			});
			builder.add("user", userBuilder);
		}

		return replace(builder.build().toString().getBytes(), '#', '\\');
	}

	private static byte[] replace(byte[] bytes, char c, char d) {
		for (int i = 0; i < bytes.length; i++) {
			if (bytes[i] == c) {
				bytes[i] = (byte) d;
			}
		}
		return bytes;
	}

}
