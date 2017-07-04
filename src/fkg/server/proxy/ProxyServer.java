package fkg.server.proxy;

import java.net.BindException;

import org.eclipse.jetty.proxy.ConnectHandler;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import fkg.gui.AppConfig;
import fkg.gui.FKGGui;

public final class ProxyServer {
	private static Server server;

	private static int port = 0;
	private static int proxyPort = 0;

	public static void start() {
		try {
			server = new Server();
			updateSetting();
			setConnector();

			// httpsをプロキシできるようにConnectHandlerを設定
			ConnectHandler proxy = new ConnectHandler();
			server.setHandler(proxy);

			// httpはこっちのハンドラでプロキシ
			ServletContextHandler context = new ServletContextHandler(proxy, "/", ServletContextHandler.SESSIONS);
			ServletHolder holder = new ServletHolder(new CacheProxyServlet());
			holder.setInitParameter("maxThreads", "256");
			holder.setInitParameter("timeout", "600000");
			context.addServlet(holder, "/*");

			try {
				server.start();
			} catch (Exception e) {
				StringBuilder sb = new StringBuilder();
				sb.append("代理终了").append("\n");
				sb.append("例外 : " + e.getClass().getName()).append("\n");
				sb.append("原因 : " + e.getMessage()).append("\n");
				if (e instanceof BindException) {
					sb.append("很有可能是由于其他软件使用同一端口导致的");
				}

				Display.getDefault().asyncExec(() -> {
					Shell shell = new Shell(Display.getDefault());
					{
						MessageBox box = new MessageBox(shell, SWT.YES | SWT.ICON_ERROR);
						box.setText("代理启动出现错误");
						box.setMessage(sb.toString());
						box.open();
					}
					shell.dispose();
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void restart() {
		if (server == null) {
			return;
		}
		if (updateSetting()) {
			try {
				server.stop();
				setConnector();
				server.start();
				FKGGui.gui.printMessage(String.format("%s%d→%s", "更换配置 ", AppConfig.getServerPort(), AppConfig.isUseProxy() ? String.valueOf(AppConfig.getAgentPort()) : "直连"));
			} catch (Exception e) {
				FKGGui.gui.printMessage("更换配置失败");
				e.printStackTrace();
			}
		}
	}

	public static void end() throws Exception {
		if (server != null) {
			server.stop();
			server.join();
			server = null;
		}
	}

	private static boolean updateSetting() {
		int newPort = AppConfig.getServerPort();
		int newProxyPort = AppConfig.isUseProxy() ? AppConfig.getAgentPort() : 0;

		if ((newPort == port) && (newProxyPort == proxyPort)) {
			return false;
		}

		port = newPort;
		proxyPort = newProxyPort;
		return true;
	}

	private static void setConnector() {
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(port);
		connector.setHost("127.0.0.1");
		server.setConnectors(new Connector[] { connector });
	}
}
