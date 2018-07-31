package fkg.book.main;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.function.Supplier;

import fkg.book.gui.FKGBOOKWindow;
import fkg.book.patch.server.FKGBOOKServer;
import fkg.book.patch.server.FKGBOOKServerConfig;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class FKGBOOKApplicationMain extends Application {
	public final static String CONFIG_FOLDER = "config";
	private final static String MAINCONFIG_FILENAME = CONFIG_FOLDER + File.separator + "main.xml";
	private final static String SERVERCONFIG_FILENAME = CONFIG_FOLDER + File.separator + "server.xml";

	//@formatter:off
	private static Optional<Exception> lanchException = Optional.empty();
	private static FKGBOOKMainConfig mainConfig;
	private static FKGBOOKServerConfig serverConfig;
	private static FKGBOOKServer server;
	private static FKGBOOKWindow window;
	public static FKGBOOKServer getServer() {return server;}
	public static FKGBOOKMainConfig getMainConfig() {return mainConfig;}
	public static FKGBOOKServerConfig getServerConfig() {return serverConfig;}
	private @SuppressWarnings("unchecked") static <T> T readConfig(String filePath,Supplier<T> other){
		try (XMLDecoder decoder = new XMLDecoder(new FileInputStream(filePath))) {
			return (T) decoder.readObject();
		} catch (Exception e) {}
		return other.get();
	}
	private static void storeConfig(String filePath,Object config) {
		try {
			Files.createDirectories(new File(filePath).getParentFile().toPath());
			try (XMLEncoder encoder = new XMLEncoder(new FileOutputStream(filePath))) {
				encoder.writeObject(config);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//@formatter:on

	public static void main(String[] args) {
		mainConfig = readConfig(MAINCONFIG_FILENAME, FKGBOOKMainConfig::new);
		serverConfig = readConfig(SERVERCONFIG_FILENAME, FKGBOOKServerConfig::new);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			storeConfig(MAINCONFIG_FILENAME, mainConfig);
			storeConfig(SERVERCONFIG_FILENAME, serverConfig);
		}));

		server = new FKGBOOKServer();
		try {
			server.initModular();
		} catch (Exception e) {
			lanchException = Optional.of(new Exception("需要重新启动服务器", e));
		}

		mainConfig.setSaveDataForMasterData(true);

		//旧版本 使用单个config文件 , 需要删除
		File oldConfigFile = new File(CONFIG_FOLDER);
		if (oldConfigFile.exists() && oldConfigFile.isFile()) {
			try {
				Files.delete(oldConfigFile.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		window = new FKGBOOKWindow();
		FKGBOOKApplicationMain.launch(args);
		//launch 内部执行 start
		//并堵塞

		for (FKGBOOKModular modular : new FKGBOOKModular[] { window, server }) {
			try {
				modular.disposeModular();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window.setStage(primaryStage);
		window.display(lanchException);
		Platform.runLater(() -> {
			window.selectChara(mainConfig.getTargetDeputyLeaderId());
		});
	}
}
