package fkg.book.other.sd;

import javafx.application.Application;
import javafx.stage.Stage;

public class AnimatedSD extends Application {
	public static void main(String[] args) {
		AnimatedSD.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		AnimatedSDStageManager asm = new AnimatedSDStageManager(primaryStage);
		asm.show();
	}
}
