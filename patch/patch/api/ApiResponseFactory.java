package patch.api;

import patch.api.getMaster.GetMaster;

public class ApiResponseFactory {

	public static ApiResponse get(String url) {
		if (url == null) return null;

		if (url.contains(GetMaster.key)) return new GetMaster();

		return null;
	}

}
