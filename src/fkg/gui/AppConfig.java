package fkg.gui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.swt.graphics.Point;

public class AppConfig {
	private static final String FILEPATH = "config";
	private static final Properties config = new Properties();

	public static String load() {
		try (FileInputStream fis = new FileInputStream(FILEPATH)) {
			config.load(fis);
			return null;
		} catch (IOException e) {
			return "无配置文件";
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

	/*---------------------------------------------------------------------------------------------*/

	public static boolean isUseProxyForPuzzle() {
		return Boolean.parseBoolean(config.getProperty("useProxyForPuzzle", Boolean.toString(false)));
	}

	public static void setUseProxyForPuzzle(boolean useProxyForPuzzle) {
		config.setProperty("useProxyForPuzzle", Boolean.toString(useProxyForPuzzle));
	}

	public static boolean patch() {
		return Boolean.parseBoolean(config.getProperty("patch", Boolean.toString(false)));
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

	public static boolean isUseProxy() {
		return Boolean.parseBoolean(config.getProperty("useProxy", Boolean.toString(false)));
	}

	public static void setUseProxy(boolean useProxy) {
		config.setProperty("useProxy", Boolean.toString(useProxy));
	}

	public static String cachePath() {
		return config.getProperty("cachePath", null);
	}

	public static Point getStoryViewerSize() {
		int width = Integer.parseInt(config.getProperty("StoryViewerWidth", String.valueOf(400)));
		int height = Integer.parseInt(config.getProperty("StoryViewerHeight", String.valueOf(600)));
		return new Point(width, height);
	}

	public static void setStoryViewerSize(Point size) {
		config.setProperty("StoryViewerWidth", String.valueOf(size.x));
		config.setProperty("StoryViewerHeight", String.valueOf(size.y));
	}

	public static Point getStoryViewerLocation() {
		int x = Integer.parseInt(config.getProperty("StoryViewerLocationX", String.valueOf(400)));
		int y = Integer.parseInt(config.getProperty("StoryViewerLocationY", String.valueOf(600)));
		return new Point(x, y);
	}

	public static void setStoryViewerLocation(Point size) {
		config.setProperty("StoryViewerLocationX", String.valueOf(size.x));
		config.setProperty("StoryViewerLocationY", String.valueOf(size.y));
	}

}
