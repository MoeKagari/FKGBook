package fkg.book.gui;

import com.moekagari.tool.other.FXUtils;
import fkg.book.main.FKGBOOKModular;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Optional;

public final class FKGBOOKWindow extends FKGBOOKModular {
	private Stage stage;
	private ShowCharaNode showCharaNode;
	private ShowDataNode showDataNode;

	@Override
	public void initModular() throws Exception {
	}

	@Override
	public void disposeModular() throws Exception {
		this.showCharaNode.showStandWindow.window.dispose();
	}

	Stage getStage() {
		return this.stage;
	}

	public void setStage(Stage primaryStage) {
		this.stage = primaryStage;

		this.showCharaNode = new ShowCharaNode();
		BorderPane.setMargin(this.showCharaNode, new Insets(5, 3, 0, 3));

		this.showDataNode = new ShowDataNode(this);

		this.stage.setScene(new Scene(new BorderPane(this.showDataNode, null, null, null, this.showCharaNode), 1700, 900));
		this.stage.getIcons().setAll(new Image(this.getClass().getResourceAsStream("/icon.png")));
		this.stage.setTitle("美少女花骑士BOOK");
		this.stage.setOnCloseRequest(ev -> {
			if(FXUtils.showCloseConfirmationWindow(this.stage, "退出", "是否退出?")) {

			} else {
				ev.consume();
			}
		});
	}

	public void display(Optional<Exception> lanchException) {
		this.stage.show();

		lanchException.ifPresent(ex -> {
			Platform.runLater(() -> {
				FXUtils.showExceptionDialog(this.stage, ex);
			});
		});
	}

	public void selectChara(int target) {
		this.showDataNode.selectChara(target);
	}

	void showCharacter(AbstractCharaData newValue) {
		this.showCharaNode.showCharacter(newValue);
	}
}
