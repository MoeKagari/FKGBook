package patch.api;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import patch.Transfer;
import tool.FileUtil;
import tool.HTTPUtil;
import tool.ZLibUtils;

public class GetTurningCardSheet implements ApiResponse {
	public final static String key = "flower-knight-girls.co.jp/api/v1/turningCard/getTurningCardSheet";

	@Override
	public void deal(byte[] bytes) {
		bytes = HTTPUtil.getBody(bytes, true);
		bytes = ZLibUtils.decompress(bytes);
		bytes = Base64.getDecoder().decode(bytes);
		FileUtil.save("GetTurningCardSheet_" + new SimpleDateFormat("HH_mm_ss").format(new Date()), bytes);
	}

	@Override
	public void response(Transfer transfer) {
		transfer.handle();
	}

}
