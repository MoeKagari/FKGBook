package fkg.gui.jfx;

import fkg.config.AppConfig;
import fkg.gui.jfx.part.center.CenterShowListPane;
import fkg.gui.jfx.part.left.LeftOptionPane;
import fkg.gui.jfx.part.right.RightShowCharaPane;
import fkg.patch.server.FKGServerServlet;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import tool.FXUtils;

public class ApplicationMain {
	public static FKGServerServlet server;
	public final Stage primaryStage;
	public final LeftOptionPane leftOption = new LeftOptionPane(this);
	public final CenterShowListPane centerShow = new CenterShowListPane(this);
	public final RightShowCharaPane rightShow = new RightShowCharaPane(this);

	public ApplicationMain(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setScene(new Scene(new BorderPane(this.centerShow, null, this.rightShow, null, this.leftOption), 1700, 900));
		this.primaryStage.getIcons().setAll(new Image(this.getClass().getResourceAsStream("/icon.png")));
		this.primaryStage.setTitle("美少女花骑士BOOK by MoeKagari");
		FXUtils.addCloseConfirmationWindow(this.primaryStage, "退出", "是否退出?", ev -> {
			this.rightShow.showStandWindow.window.dispose();
		});
	}

	public static class MainStart extends Application {
		public static void main(String[] args) {
			AppConfig.load();
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				AppConfig.store();
			}));

			server = new FKGServerServlet(AppConfig::getServerPort, AppConfig::isUseProxy, null, AppConfig::getAgentPort);
			try {
				server.start();
			} catch (Exception e) {
				try {
					server.end();
				} catch (Exception ex) {

				}
				Platform.exit();
				return;
			}

			MainStart.launch(args);

			try {
				server.end();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void start(Stage primaryStage) {
			ApplicationMain main = new ApplicationMain(primaryStage);
			main.primaryStage.show();
			main.centerShow.dataTable.requestFocus();
			main.centerShow.dataTable.getItems().stream()
					.filter(item -> item.getId() == AppConfig.getFutuanzhangID())
					.findFirst().ifPresent(futuanzhang -> {
						main.centerShow.dataTable.getSelectionModel().select(futuanzhang);
						main.centerShow.dataTable.scrollTo(futuanzhang);
					});
		}
	}
}
