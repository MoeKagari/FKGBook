package patch.api;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.FileUtils;

import patch.Transfer;
import tool.HTTPUtils;
import tool.ZLibUtils;

public class GetTurningCardSheet implements ApiResponse {
	public final static String key = "flower-knight-girls.co.jp/api/v1/turningCard/getTurningCardSheet";
	public final static String filename = "GetTurningCardSheet.data";

	@Override
	public void deal(byte[] bytes) {
		bytes = HTTPUtils.getBody(bytes, true);
		bytes = ZLibUtils.decompress(bytes);
		bytes = Base64.getDecoder().decode(bytes);
		try {
			FileUtils.writeByteArrayToFile(new File(filename), bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void response(Transfer transfer) {
		transfer.response(transfer);
	}
}
