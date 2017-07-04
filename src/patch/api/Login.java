package patch.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import fkg.gui.AppConfig;
import patch.Transfer;
import tool.HTTPUtils;
import tool.ZLibUtils;

public class Login implements ApiResponse {
	public final static String key = "flower-knight-girls.co.jp/api/v1/user/login";

	@Override
	public void deal(byte[] bytes) {}

	@Override
	public void response(Transfer transfer) {
		if (AppConfig.patch() == false) {
			transfer.response(transfer);
			return;
		}

		if (AppConfig.isTihuan() == false) {
			transfer.response(transfer);
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
				System.out.println("c to a 错误:\n" + transfer.getHeader().trim());
				e.printStackTrace();
			} finally {
				transfer.countDown();
			}
		}).start();

		new Thread(() -> {
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				byte[] buffer = new byte[1024];
				int len;
				while ((len = transfer.readATC(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}

				byte[] bytes = baos.toByteArray();
				byte[] header = HTTPUtils.getHeader(bytes);
				boolean chunked = new String(header).contains("chunked");
				byte[] body = HTTPUtils.getBody(bytes, chunked);
				body = ZLibUtils.decompress(body);
				body = Base64.getDecoder().decode(body);
				body = patch(body, AppConfig.getFutuanzhangID());
				body = Base64.getEncoder().encode(body);
				body = ZLibUtils.compress(body);

				if (chunked) {
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
				} else {
					byte[] newHeader = new String(header).replaceAll("\r\nContent-Length.+?\r\n", "\r\nContent-Length:" + body.length + "\r\n").getBytes();
					transfer.writeATC(newHeader, 0, newHeader.length);
					transfer.writeATC(body, 0, body.length);
				}
			} catch (IOException e) {
				System.out.println("a to c 错误:\n" + transfer.getHeader().trim());
			} finally {
				transfer.countDown();
			}
		}).start();
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
