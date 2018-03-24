package fkg.book.patch.server;

public class FKGBOOKServerConfig {
	private int listenPort = 22223;
	private boolean useProxy = false;
	private int proxyPort = 1080;

	public int getListenPort() {
		return this.listenPort;
	}

	public void setListenPort(int listenPort) {
		this.listenPort = listenPort;
	}

	public boolean isUseProxy() {
		return this.useProxy;
	}

	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}

	public int getProxyPort() {
		return this.proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}
}
