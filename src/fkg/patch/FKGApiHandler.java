package fkg.patch;

import server.CommunicationHandler;

public  class FKGApiHandler extends CommunicationHandler {
	public FKGApiHandler(String uri) {
		super(getServerName(), uri);
	}

	public static String getServerName() {
		return "web.flower-knight-girls.co.jp";
	}
}