package fkg.book.temp;

import com.moekagari.tool.other.FXUtils;
import fkg.book.gui.AbstractCharaData;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * @author MoeKagari
 */
public class WindowPaneDetail extends GridPane implements WindowPane {
	private final Window window;
	private final Container container;
	private AbstractCharaData showingChara;

	public WindowPaneDetail(Window window) {
		this.window = window;
		FXUtils.addStyleClass(this, "detail");
		this.setVisible(false);
		this.setOnMouseClicked(ev -> this.setVisible(false));
		this.setAlignment(Pos.CENTER);
		this.add(this.container = new Container(), 0, 0);
	}

	@Override public void showCharaDetail(AbstractCharaData chara) {
		this.showingChara = chara;
		this.container.left.update();
		this.container.center.update();
		this.container.right.update();
		this.setVisible(true);
	}

	private class Container extends GridPane {
		private final NodeLeft left;
		private final NodeCenter center;
		private final NodeRight right;

		public Container() {
			FXUtils.addStyleClass(this, "container");
			this.setHgap(2);
			this.setOnMouseClicked(Event::consume);
			this.getColumnConstraints().addAll(
					FXUtils.createColumnConstraints(400),
					FXUtils.createColumnConstraints(500),
					FXUtils.createColumnConstraints(400)
			);
			this.getRowConstraints().addAll(FXUtils.createRowConstraints(660));
			this.addRow(
					0,
					this.left = new NodeLeft(),
					this.center = new NodeCenter(),
					this.right = new NodeRight()
			);
		}
	}

	private class NodeChild extends VBox {
		public NodeChild(String styleClass) {
			FXUtils.addStyleClass(this, "child", styleClass);
			this.setAlignment(Pos.CENTER);
		}

		public void update() {

		}
	}

	private class NodeLeft extends NodeChild {
		final ImageView standS = new ImageView();
		final Label flowerLanguage = new Label();
		final TextArea introduction = new TextArea();

		public NodeLeft() {
			super("left");

			VBox.setMargin(this.standS, new Insets(10, 0, 10, 0));
			FXUtils.addStyleClass(this.standS, "stand_s");
			this.standS.setOnMouseClicked(
					ev -> WindowPaneDetail.this.window.windowPaneList
							.forEach(windowPane -> windowPane.showCharaStand(WindowPaneDetail.this.showingChara))
			);

			FXUtils.setMaxWidth(this.flowerLanguage, Double.MAX_VALUE);
			FXUtils.addStyleClass(this.flowerLanguage, "flower_language");

			this.introduction.setEditable(false);
			FXUtils.addStyleClass(this.introduction, "introduction");

			this.getChildren().addAll(this.standS, this.flowerLanguage, this.introduction);
		}

		@Override public void update() {
			this.standS.setImage(WindowPaneDetail.this.showingChara.getStandS());
			this.flowerLanguage.setText("花语：" + WindowPaneDetail.this.showingChara.getFlowerLanguage());
			this.introduction.setText(WindowPaneDetail.this.showingChara.getIntroduction());
		}
	}

	private class NodeCenter extends NodeChild {
		public NodeCenter() {
			super("center");
		}
	}

	private class NodeRight extends NodeChild {
		public NodeRight() {
			super("right");
		}
	}
}
