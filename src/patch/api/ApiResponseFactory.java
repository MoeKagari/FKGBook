package patch.api;

import patch.Transfer;
import patch.api.getBook.GetBook;
import patch.api.getMaster.GetMaster;

public class ApiResponseFactory {

	public static ApiResponse get(String url) {
		if (url == null) return null;

		if (url.contains(GetMaster.key)) return new GetMaster();
		if (url.contains(GetBook.key)) return new GetBook();
		if (url.contains(Login.key)) return new Login();
		//	if (url.contains(GetTurningCardSheet.key)) return new GetTurningCardSheet();

		return new ApiResponse() {

			@Override
			public void response(Transfer transfer) {
				transfer.handle();
			}

			@Override
			public void deal(byte[] bytes) {}
		};
	}

}
