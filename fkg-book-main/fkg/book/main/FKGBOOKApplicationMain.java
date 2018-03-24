package fkg.book.main;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import fkg.book.gui.FKGBOOKWindow;
import fkg.book.patch.server.FKGBOOKServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class FKGBOOKApplicationMain extends Application {
	public final static String CONFIG_FOLDER = "config";
	private final static String MAINCONFIG_FILENAME = CONFIG_FOLDER + File.separator + "main.xml";
	private static Optional<Exception> lanchException = Optional.empty();
	private static FKGBOOKMainConfig mainConfig;
	private static FKGBOOKServer server;
	private static FKGBOOKWindow window;

	public static FKGBOOKServer getServer() {
		return server;
	}

	public static FKGBOOKWindow getWindow() {
		return window;
	}

	public static FKGBOOKMainConfig getMainConfig() {
		return mainConfig;
	}

	private static Optional<FKGBOOKMainConfig> readMainConfig() {
		try (XMLDecoder decoder = new XMLDecoder(new FileInputStream(MAINCONFIG_FILENAME))) {
			Object obj = decoder.readObject();
			if (obj instanceof FKGBOOKMainConfig) {
				return Optional.of((FKGBOOKMainConfig) obj);
			}
		} catch (FileNotFoundException e) {

		}
		return Optional.empty();
	}

	private static void storeMainConfig() {
		try {
			File mainConfigFile = new File(MAINCONFIG_FILENAME);
			Files.createDirectories(mainConfigFile.getParentFile().toPath());

			try (XMLEncoder encoder = new XMLEncoder(new FileOutputStream(mainConfigFile))) {
				encoder.writeObject(mainConfig);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		mainConfig = readMainConfig().orElseGet(FKGBOOKMainConfig::new);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			storeMainConfig();
		}));

		server = new FKGBOOKServer();
		try {
			server.initModular();
		} catch (Exception e) {
			lanchException = Optional.of(new Exception("需要重新启动服务器", e));
		}

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
