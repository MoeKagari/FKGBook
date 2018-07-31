package fkg.book.patch.server;

import fkg.book.main.FKGBOOKApplicationMain;
import fkg.book.main.FKGBOOKModular;

public class FKGBOOKServer extends FKGBOOKModular {
	private FKGBOOKServerServlet server;

	@Override
	public void initModular() throws Exception {
		this.server = new FKGBOOKServerServlet(
				FKGBOOKApplicationMain.getServerConfig()::getListenPort,
				FKGBOOKApplicationMain.getServerConfig()::isUseProxy,
				FKGBOOKApplicationMain.getServerConfig()::getProxyPort
		/**/);

		try {
			this.server.start();
		} catch (Exception e) {
			try {
				this.server.end();
			} catch (Exception ex) {}
			throw new Exception("server启动失败", e);
		}
	}

	public void restart() throws Exception {
		this.server.restart();
	}

	@Override
	public void disposeModular() throws Exception {
		try {
			this.server.end();
		} catch (Exception e) {
			throw new Exception("server关闭失败", e);
		}
	}
}
