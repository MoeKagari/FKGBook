package gui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class GuiConfig {

	private static final String FILEPATH = "config";
	private static final Properties config = new Properties();

	public static String load() {
		try (FileInputStream fis = new FileInputStream(FILEPATH)) {
			config.load(fis);
			return null;
		} catch (IOException e) {
			return "Œﬁ≈‰÷√Œƒº˛";
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

	public static void setServerport(int serverPort) {
		config.setProperty("serverPort", String.valueOf(serverPort));
	}

	public static int getAgentPort() {
		return Integer.parseInt(config.getProperty("agentPort", String.valueOf(1080)));
	}

	public static void setAgentport(int agentPort) {
		config.setProperty("agentPort", String.valueOf(agentPort));
	}

	public static boolean isUseProxy() {
		return Boolean.parseBoolean(config.getProperty("useProxy", Boolean.toString(false)));
	}

	public static void setUseProxy(boolean useProxy) {
		config.setProperty("useProxy", Boolean.toString(useProxy));
	}

	/*---------------------------------------------------------------------------------------------*/

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

	public static Integer getFutuanzhangID() {
		return Integer.parseInt(config.getProperty("futuanzhangID", String.valueOf(169702)));
	}

	public static void setFutuanzhangID(Integer futuanzhangID) {
		config.setProperty("futuanzhangID", futuanzhangID == null ? String.valueOf(169702) : futuanzhangID.toString());
	}

}
