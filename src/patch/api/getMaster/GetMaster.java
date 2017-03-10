package patch.api.getMaster;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import javax.json.Json;
import javax.json.JsonString;

import patch.Transfer;
import patch.api.ApiResponse;
import tool.FileUtil;
import tool.HTTPUtil;
import tool.ZLibUtils;

public class GetMaster implements ApiResponse {
	public final static String dir = "resources\\getMaster";
	public final static String key = "flower-knight-girls.co.jp/api/v1/master/getMaster";

	@Override
	public void response(Transfer transfer) {
		transfer.handle();
	}

	@Override
	public void deal(byte[] bytes) {
		byte[] body = HTTPUtil.getBody(bytes, new String(HTTPUtil.getHeader(bytes)).contains("chunked"));
		if (body == null) {
			System.out.println("getBody()´íÎó in GetMaster.");
			return;
		}

		body = ZLibUtils.decompress(body);
		if (body == null) {
			System.out.println("½âÑ¹´íÎó in GetMaster.");
			return;
		}

		Json.createReader(new ByteArrayInputStream(body)).readObject().forEach((key, value) -> {
			try {
				if (value instanceof JsonString) {
					if (CharacterInformation.key.equals(key) || CharacterSkill.key.equals(key) || CharacterLeaderSkill.key.equals(key) || CharacterBook.key.equals(key))//
					{
						byte[] data = Base64.getDecoder().decode(((JsonString) value).getString().getBytes("utf-8"));
						FileUtil.save(dir + "\\" + key + ".csv", data);
					}
				}
			} catch (Exception e) {

			}
		});
	}

}
