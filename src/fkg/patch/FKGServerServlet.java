package fkg.patch;

import fkg.gui.AppConfig;
import fkg.patch.api.GetBook;
import fkg.patch.api.GetMaster;
import fkg.patch.api.GetTurningCardSheet;
import fkg.patch.api.Login;
import fkg.patch.api.other.ALLCGPatchForRedCorreption;
import server.CommunicationHandler;
import server.ProxyServerServlet;
import server.ServerConfig;

@SuppressWarnings("serial")
public class FKGServerServlet extends ProxyServerServlet {
	public FKGServerServlet(ServerConfig config) {
		super(config);
	}

	@Override
	public CommunicationHandler getHandler(String serverName, String uri) {
		if (FKGApiHandler.getServerName().equals(serverName)) {
			if (Login.getUri().equals(uri)) {
				if (AppConfig.patch() && AppConfig.isTihuan()) {
					return new Login();
				}
			} else if (GetBook.getUri().equals(uri)) {
				if (AppConfig.patch() && AppConfig.isAllCG()) {
					return new GetBook();
				}
			} else if (GetTurningCardSheet.getUri().equals(uri)) {
				return new GetTurningCardSheet();
			} else if (GetMaster.getUri().equals(uri)) {
				return new GetMaster();
			}
		}

		if (ALLCGPatchForRedCorreption.getServerName().equals(serverName)) {
			if (ALLCGPatchForRedCorreption.getUri().equals(uri)) {
				return new ALLCGPatchForRedCorreption();
			}
		}

		return super.getHandler(serverName, uri);
	}
}
