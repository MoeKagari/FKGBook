package fkg.patch.server;

import server.CommunicationHandler;

public abstract class FKGApiHandler extends CommunicationHandler {
	public FKGApiHandler(String uri) {
		super(getServerName(), uri);
	}

	public static String getServerName() {
		return "web.flower-knight-girls.co.jp";
	}
}
