package patch.api.getBook;

import java.io.IOException;
import java.util.Base64;

import gui.GuiConfig;
import patch.Transfer;
import patch.api.ApiResponse;
import tool.ZLibUtils;

public class GetBook implements ApiResponse {
	public final static String key = "flower-knight-girls.co.jp/api/v1/character/getBook";

	@Override
	public void response(Transfer transfer) {
		if (GuiConfig.patch() == false) {
			transfer.handle();
			return;
		}

		if (GuiConfig.isAllCG() == false) {
			transfer.handle();
			return;
		}

		new Thread(() -> {
			try {
				byte[] body = AllCGJSON.get().getBytes();
				body = Base64.getEncoder().encode(body);
				body = ZLibUtils.compress(body);
				transfer.writeATC(body, 0, body.length);
			} catch (IOException e) {
				System.out.println("getBook() ´íÎó");
			} finally {
				transfer.countDown();
				transfer.countDown();
			}
		}).start();
	}

	@Override
	public void deal(byte[] bytes) {}

}
