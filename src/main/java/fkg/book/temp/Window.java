package fkg.book.temp;

import com.moekagari.tool.acs.ExStreamUtils;
import com.moekagari.tool.io.FileUtils;
import com.moekagari.tool.other.FXUtils;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Window {
	private final Stage stage;
	public final List<WindowPane> windowPaneList;

	public Window(Stage primaryStage) {
		this.stage = primaryStage;

		WindowPaneBackground background = new WindowPaneBackground();
		WindowPaneContent content = new WindowPaneContent(this);
		WindowPaneDetail detail = new WindowPaneDetail(this);
		WindowPaneStand stand = new WindowPaneStand();
		this.windowPaneList = Arrays.asList(background, content, detail, stand);

		StackPane stackPane = new StackPane();
		stackPane.setAlignment(Pos.CENTER);
		stackPane.setBackground(FXUtils.createBackground(Color.TRANSPARENT));
		stackPane.getChildren().addAll(background, content, detail, stand);
		stackPane.getStylesheets().addAll(
				ExStreamUtils.of("main", "book2", "filter", "sorter", "hensei", "config", "api")
				             .map(name -> "css\\" + name + ".css")
				             .map(FileUtils::getFileUrlString)
				             .toList()
		);

		Consumer<WindowPane> focusParentSizeChange = windowPane -> windowPane.focusParentSizeChange(stackPane.getWidth(), stackPane.getHeight());
		stackPane.widthProperty().addListener(FXUtils.makeChangeListener(() -> this.windowPaneList.forEach(focusParentSizeChange)));
		stackPane.heightProperty().addListener(FXUtils.makeChangeListener(() -> this.windowPaneList.forEach(focusParentSizeChange)));

		this.stage.setScene(new Scene(stackPane, 1700, 900));
		this.stage.setMaximized(true);
		this.stage.getIcons().setAll(new Image(this.getClass().getResourceAsStream("/icon.png")));
		this.stage.setTitle("美少女花骑士BOOK");
	}

	public static class MainStart extends Application {
		public static void main(String[] args) {
			launch(args);
		}

		@Override public void start(Stage primaryStage) {
			Window window = new Window(primaryStage);
			window.windowPaneList.forEach(WindowPane::windowShowBefore);
			window.stage.show();
			window.windowPaneList.forEach(WindowPane::windowShowAfter);
		}
	}
}
