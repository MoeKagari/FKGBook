package fkg.book.patch.server;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import fkg.book.main.FKGBOOKApplicationMain;
import fkg.book.main.FKGBOOKModular;

public class FKGBOOKServer extends FKGBOOKModular {
	private final static String SERVERCONFIG_FILENAME = FKGBOOKApplicationMain.CONFIG_FOLDER + File.separator + "server.xml";

	private FKGBOOKServerServlet server;
	private FKGBOOKServerConfig serverConfig;

	public FKGBOOKServerConfig getServerConfig() {
		return this.serverConfig;
	}

	@Override
	public void initModular() throws Exception {
		this.serverConfig = this.readConfig().orElseGet(FKGBOOKServerConfig::new);
		this.server = new FKGBOOKServerServlet(() -> this.serverConfig.getListenPort(), () -> this.serverConfig.isUseProxy(), () -> this.serverConfig.getProxyPort());

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
		this.storeConfig();

		try {
			this.server.end();
		} catch (Exception e) {
			throw new Exception("server关闭失败", e);
		}
	}

	private Optional<FKGBOOKServerConfig> readConfig() {
		try (XMLDecoder decoder = new XMLDecoder(new FileInputStream(SERVERCONFIG_FILENAME))) {
			Object obj = decoder.readObject();
			if (obj instanceof FKGBOOKServerConfig) {
				return Optional.of((FKGBOOKServerConfig) obj);
			}
		} catch (FileNotFoundException e) {

		}
		return Optional.empty();
	}

	private void storeConfig() {
		try {
			File serverConfigFile = new File(SERVERCONFIG_FILENAME);
			Files.createDirectories(serverConfigFile.getParentFile().toPath());

			try (XMLEncoder encoder = new XMLEncoder(new FileOutputStream(serverConfigFile))) {
				encoder.writeObject(this.serverConfig);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
