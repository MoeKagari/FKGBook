package patch.api;

import patch.Transfer;

public interface ApiResponse {

	public void deal(byte[] bytes);
	
	public void response(Transfer transfer);

}
