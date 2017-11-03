package fkg.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {
	private static final String FILEPATH = "config";
	private static final Properties config = new Properties();

	public static void load() {
		try (FileInputStream fis = new FileInputStream(FILEPATH)) {
			config.load(fis);
		} catch (IOException e) {

		}
	}

	public static void store() {
		try (FileOutputStream fos = new FileOutputStream(FILEPATH)) {
			config.store(fos, "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*---------------------------------------------------------------------------------------------*/

	public static int getServerPort() {
		return Integer.parseInt(config.getProperty("serverPort", String.valueOf(2222)));
	}

	public static void setServerPort(int serverPort) {
		config.setProperty("serverPort", String.valueOf(serverPort));
	}

	public static boolean isUseProxy() {
		return Boolean.parseBoolean(config.getProperty("useProxy", Boolean.toString(false)));
	}

	public static void setUseProxy(boolean useProxy) {
		config.setProperty("useProxy", Boolean.toString(useProxy));
	}

	public static int getAgentPort() {
		return Integer.parseInt(config.getProperty("agentPort", String.valueOf(1080)));
	}

	public static void setAgentPort(int agentPort) {
		config.setProperty("agentPort", String.valueOf(agentPort));
	}

	/*---------------------------------------------------------------------------------------------*/

	public static boolean isLazyLoad() {
		return Boolean.parseBoolean(config.getProperty("lazyLoad", Boolean.toString(true)));
	}

	public static void setLazyLoad(boolean lazyLoad) {
		config.setProperty("lazyLoad", Boolean.toString(lazyLoad));
	}

	public static boolean isAllCG() {
		return Boolean.parseBoolean(config.getProperty("allCG", Boolean.toString(false)));
	}

	public static void setAllCG(boolean allCG) {
		config.setProperty("allCG", Boolean.toString(allCG));
	}

	public static boolean isTihuan() {
		return Boolean.parseBoolean(config.getProperty("tihuan", Boolean.toString(false)));
	}

	public static void setTihuan(boolean tihuan) {
		config.setProperty("tihuan", Boolean.toString(tihuan));
	}

	public static int getFutuanzhangID() {
		return Integer.parseInt(config.getProperty("futuanzhangID", String.valueOf(169702)));
	}

	public static void setFutuanzhangID(int futuanzhangID) {
		config.setProperty("futuanzhangID", String.valueOf(futuanzhangID));
	}
}
