package fkg.book.other.sd;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class AnimatedSDStage {
	private final Stage stage;
	private final List<AnimatedSDStageStateChangeListener> animatedSDStageStateChangeListeners = new ArrayList<>();

	public AnimatedSDStage(Window primaryStage, String name, Image[] images) {
		this.stage = new Stage(StageStyle.TRANSPARENT);
		this.stage.setAlwaysOnTop(true);
		this.stage.setTitle(name);
		this.stage.initOwner(primaryStage);//设置父级,使 stage 无任务栏

		ImageView imageView = new ImageView(images[0]);
		{
			class ImageViewMouseEvent {
				boolean showMenu = false;
				boolean allowDrag = false;
				final double[] xys = {0, 0, 0, 0};
				final ContextMenu menu = new ContextMenu();

				ImageViewMouseEvent() {
					MenuItem menuItem = new MenuItem("关闭");
					menuItem.setOnAction(ev -> AnimatedSDStage.this.hide());
					this.menu.getItems().add(menuItem);
				}

				void mousePressed(MouseEvent ev) {
					this.xys[0] = AnimatedSDStage.this.stage.getX();
					this.xys[1] = AnimatedSDStage.this.stage.getY();
					this.xys[2] = ev.getScreenX();
					this.xys[3] = ev.getScreenY();

					switch(ev.getButton()) {
						case PRIMARY:
							this.allowDrag = true;
							this.showMenu = false;
							break;
						case SECONDARY:
							this.allowDrag = false;
							this.showMenu = true;
							break;
						case MIDDLE:
							break;
						case NONE:
							break;
					}
				}

				void mouseDragged(MouseEvent ev) {
					this.showMenu = false;
					if(this.allowDrag) {
						AnimatedSDStage.this.stage.setX(this.xys[0] + ev.getScreenX() - this.xys[2]);
						AnimatedSDStage.this.stage.setY(this.xys[1] + ev.getScreenY() - this.xys[3]);
					}
				}

				void mouseReleased(MouseEvent ev) {
					if(this.showMenu) {
						this.menu.show(AnimatedSDStage.this.stage, ev.getScreenX(), ev.getScreenY());
					}
				}
			}
			//鼠标拖动
			ImageViewMouseEvent ivme = new ImageViewMouseEvent();
			imageView.setOnMousePressed(ivme::mousePressed);
			imageView.setOnMouseDragged(ivme::mouseDragged);
			imageView.setOnMouseReleased(ivme::mouseReleased);
		}

		Pane pane = new Pane(imageView);
		pane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(0), new Insets(0))));
		Scene scene = new Scene(pane);
		scene.setFill(null);
		this.stage.setScene(scene);

		SimpleIntegerProperty target = new SimpleIntegerProperty(0);
		target.addListener((source, oldValue, newValue) -> {
			imageView.setImage(images[newValue.intValue()]);
		});

		Timeline anime = new Timeline(new KeyFrame(new Duration(33 * images.length), new KeyValue(target, images.length - 1)));
		anime.setCycleCount(Timeline.INDEFINITE);
		anime.play();
	}

	public void addAnimatedSDStageStateChangeListener(AnimatedSDStageStateChangeListener animatedSDStageStateChangeListener) {
		this.animatedSDStageStateChangeListeners.add(animatedSDStageStateChangeListener);
	}

	public void show() {
		this.animatedSDStageStateChangeListeners.forEach(AnimatedSDStageStateChangeListener::showBefore);
		this.stage.show();
		this.animatedSDStageStateChangeListeners.forEach(AnimatedSDStageStateChangeListener::showAfter);
	}

	public void hide() {
		this.animatedSDStageStateChangeListeners.forEach(AnimatedSDStageStateChangeListener::hideBefore);
		this.stage.hide();
		this.animatedSDStageStateChangeListeners.forEach(AnimatedSDStageStateChangeListener::hideAfter);
	}

	public boolean isShowing() {
		return this.stage.isShowing();
	}

	public static class AnimatedSDStageStateChangeListener {
		public void hideBefore() {}

		public void hideAfter() {}

		public void showBefore() {}

		public void showAfter() {}
	}
}
