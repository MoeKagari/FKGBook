package patch.api.getBook;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.FileUtils;

import fkg.gui.AppConfig;
import patch.Transfer;
import patch.api.ApiResponse;
import tool.HTTPUtils;
import tool.ZLibUtils;

public class GetBook implements ApiResponse {
	public final static String key = "flower-knight-girls.co.jp/api/v1/character/getBook";

	@Override
	public void response(Transfer transfer) {
		if (AppConfig.patch() == false) {
			transfer.response(transfer);
			return;
		}

		if (AppConfig.isAllCG() == false) {
			transfer.response(transfer);
			return;
		}

		new Thread(() -> {
			try {
				byte[] body = AllCGJSON.get().getBytes();
				body = Base64.getEncoder().encode(body);
				body = ZLibUtils.compress(body);
				transfer.writeATC(body, 0, body.length);
			} catch (IOException e) {
				System.out.println("getBook() 错误");
			} finally {
				transfer.countDown();
				transfer.countDown();
			}
		}).start();
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

		try {
			FileUtils.writeByteArrayToFile(new File("getBook.data"), Base64.getDecoder().decode(body));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
