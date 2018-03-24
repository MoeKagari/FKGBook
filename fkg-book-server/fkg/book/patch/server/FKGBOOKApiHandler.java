package fkg.book.patch.server;

import server.CommunicationHandler;

public abstract class FKGBOOKApiHandler extends CommunicationHandler {
	public final static String SERVERNAME = "web.flower-knight-girls.co.jp";

	public FKGBOOKApiHandler(String uri) {
		super(SERVERNAME, uri);
	}
}
