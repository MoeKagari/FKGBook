package patch.api.getMaster;

import java.io.ByteArrayInputStream;

import javax.json.Json;
import javax.json.JsonString;

import patch.Transfer;
import patch.api.ApiResponse;
import tool.FileUtil;
import tool.GameUtils;
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
		bytes = GameUtils.getBody(bytes, true);
		if (bytes == null) {
			System.out.println("getBody()´íÎó in GetMaster.");
			return;
		}

		bytes = ZLibUtils.decompress(bytes);
		if (bytes == null) {
			System.out.println("½âÑ¹´íÎó in GetMaster.");
			return;
		}

		Json.createReader(new ByteArrayInputStream(bytes)).readObject().forEach((key, value) -> {
			if (value instanceof JsonString) {
				if (CharacterBook.key.equals(key) || CharacterInformation.key.equals(key) || CharacterLeaderSkill.key.equals(key) || CharacterSkill.key.equals(key)) {
					JsonString json = (JsonString) value;
					try {
						byte[] data = GameUtils.decompress(json.toString().getBytes("utf-8"));
						FileUtil.save(dir + "\\" + key + ".csv", data);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		});
	}

}
