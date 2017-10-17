package fkg.gui.jfx;

import fkg.config.AppConfig;
import fkg.patch.server.FKGServerServlet;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import server.ServerConfig;
import tool.FXUtils;

public class ApplicationMain {
	public final Stage primaryStage;
	public final LeftOptionPane leftOption;
	public final CenterShowListPane centerShow;
	public final RightShowCharaPane rightShow;

	public ApplicationMain(Stage primaryStage) {
		BorderPane borderPane = new BorderPane();
		{
			this.leftOption = new LeftOptionPane(this);

			this.centerShow = new CenterShowListPane(this);

			this.rightShow = new RightShowCharaPane(this);
		}
		borderPane.setLeft(this.leftOption);
		borderPane.setCenter(this.centerShow);
		borderPane.setRight(this.rightShow);

		this.primaryStage = primaryStage;
		this.primaryStage.setScene(new Scene(borderPane));
		FXUtils.setStageSize(primaryStage, 1700, 920);
		FXUtils.setStageLocationToCenter(primaryStage);
		FXUtils.addCloseConfirmationWindow(primaryStage, "退出", "是否退出?", ev -> {
			this.rightShow.showStandWindow.window.dispose();
		});
	}

	public static class MainStart extends Application {
		public static FKGServerServlet server;

		public static void main(String[] args) {
			AppConfig.load();

			server = new FKGServerServlet(new ServerConfig(AppConfig::getServerPort, AppConfig::isUseProxy, () -> "127.0.0.1", AppConfig::getAgentPort));
			try {
				server.start();
			} catch (Exception e) {
				try {
					server.end();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
				return;
			}

			MainStart.launch(args);

			try {
				server.end();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			AppConfig.store();
		}

		@Override
		public void start(Stage primaryStage) throws Exception {
			ApplicationMain main = new ApplicationMain(primaryStage);
			main.primaryStage.show();
			main.centerShow.requestFocus();
			main.centerShow.table.getItems().stream()
					.filter(item -> item.getId() == AppConfig.getFutuanzhangID())
					.findFirst().ifPresent(futuanzhang -> {
						main.centerShow.table.getSelectionModel().select(futuanzhang);
						main.centerShow.table.scrollTo(futuanzhang);
					});
		}
	}
}
