package patch.api.getMaster;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Base64;

import javax.json.Json;
import javax.json.JsonString;

import org.apache.commons.io.FileUtils;

import patch.Transfer;
import patch.api.ApiResponse;
import tool.HTTPUtils;
import tool.ZLibUtils;

public class GetMaster implements ApiResponse {
	public final static String dir = "resources\\getMaster";
	public final static String key = "flower-knight-girls.co.jp/api/v1/master/getMaster";

	@Override
	public void response(Transfer transfer) {
		transfer.response(transfer);
	}

	@Override
	public void deal(byte[] bytes) {
		byte[] body = HTTPUtils.getBody(bytes, new String(HTTPUtils.getHeader(bytes)).contains("chunked"));
		if (body == null) {
			System.out.println("getBody()错误 in GetMaster.");
			return;
		}

		body = ZLibUtils.decompress(body);
		if (body == null) {
			System.out.println("解压错误 in GetMaster.");
			return;
		}

		Json.createReader(new ByteArrayInputStream(body)).readObject().forEach((key, value) -> {
			try {
				if (value instanceof JsonString) {
					//if (CharacterInformation.key.equals(key) || CharacterSkill.key.equals(key) || CharacterLeaderSkill.key.equals(key) || CharacterBook.key.equals(key))//
					{
						byte[] data = ((JsonString) value).getString().getBytes("utf-8");
						if ("version".equals(key) == false) {
							data = Base64.getDecoder().decode(data);
						}
						FileUtils.writeByteArrayToFile(new File(dir + "\\" + key + ".csv"), data);
					}
				}
			} catch (Exception e) {

			}
		});
	}

}
