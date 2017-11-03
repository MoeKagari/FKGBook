package fkg.other.home;

import java.io.File;

import fkg.other.home.chara.PartCharacter;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import tool.FXUtils;

public final class Home {
	private final Stage primaryStage;

	public Home(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setScene(new Scene(this.createRoot()));

		primaryStage.setMinWidth(960);
		primaryStage.setMinHeight(640);
		primaryStage.setMaxWidth(960);
		primaryStage.setMaxHeight(640);

		FXUtils.setStageLocationToCenter(primaryStage);
		FXUtils.addCloseConfirmationWindow(primaryStage, "退出", "");
	}

	private Parent createRoot() {
		//http://dugrqaqinbtcq.cloudfront.net/product/swfs/mypageground/background.swf
		//http://dugrqaqinbtcq.cloudfront.net/product/swfs/mypage.swf
		StackPane stackPane = new StackPane(new Group(
				new PartBackgroundImage("file:resources\\home\\background.png", 0, 0),
				new PartBackgroundImage("file:resources\\home\\rope.png", 21 / 20, 0),
				new PartBackgroundImage("file:resources\\home\\umbrella.png", 0, 3169 / 20),
				new PartBackgroundImage("file:resources\\home\\desk.png", 0, 9440 / 20)
		/**/), new PartCharacter(165601));
		stackPane.setAlignment(Pos.BOTTOM_RIGHT);
		return stackPane;
	}

	public static class MainStart extends Application {
		public static void main(String[] args) {
			MainStart.launch(args);
		}

		@Override
		public void start(Stage primaryStage) throws Exception {
			Home home = new Home(primaryStage);
			home.primaryStage.show();

			//http://dugrqaqinbtcq.cloudfront.net/product/bgm/fkg_bgm_stage054.mp3
			MediaPlayer bgm = new MediaPlayer(new Media(new File("resources\\home\\bgm.mp3").toURI().toString()));
			bgm.setCycleCount(Integer.MAX_VALUE);
			bgm.setVolume(0.1);
			bgm.setAutoPlay(true);
		}
	}
}
