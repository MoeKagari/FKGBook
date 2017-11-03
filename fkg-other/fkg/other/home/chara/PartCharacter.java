package fkg.other.home.chara;

import fkg.other.home.PartHome;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import tool.function.FunctionUtils;

public class PartCharacter extends Group implements PartHome {
	public CharacterResource charaRes;
	private CharacterResource.RandomHomeCharacter showingHome;
	private final Timeline big_small;
	private final IdleTimer idle;

	public PartCharacter(int chara_id) {
		this.charaRes = new CharacterResource(chara_id);
		this.getChildren().add(new ImageView(this.charaRes.getDefaultHomeImage()));

		//渐大渐小
		this.big_small = new Timeline(
				new KeyFrame(new Duration(5000), new KeyValue(this.scaleXProperty(), 1.05)),
				new KeyFrame(new Duration(5000), new KeyValue(this.scaleYProperty(), 1.05))
		/**/);
		this.big_small.setAutoReverse(true);
		this.big_small.setCycleCount(Animation.INDEFINITE);
		this.big_small.play();

		//点击变化角色 表情 和 语音
		PartCharacterMouseEvent mouseEvent = new PartCharacterMouseEvent();
		this.setOnMousePressed(mouseEvent::mousePressed);
		this.setOnMouseReleased(mouseEvent::mouseReleased);
		this.setOnMouseEntered(mouseEvent::mouseEntered);
		this.setOnMouseExited(mouseEvent::mouseExited);

		//放置
		this.idle = new IdleTimer(this);
		this.idle.reset();
	}

	private void handleEvent() {
		CharacterResource.RandomHomeCharacter rhc = this.charaRes.getRandomHomeCharacter(this.showingHome == null ? null : this.showingHome.cmvrg);
		if (rhc.audio == null) {
			return;
		}

		CharacterResource.RandomHomeCharacter old = this.showingHome;
		this.showingHome = rhc;

		boolean[] clearOver = { false };
		PartCharacter.this.big_small.pause();
		Platform.runLater(() -> {
			this.getChildren().add(rhc.imageView);
			if (old != null) {
				this.getChildren().remove(old.imageView);
				old.audio.stop();
			}
			clearOver[0] = true;
		});
		while (FunctionUtils.isFalse(clearOver[0])) {}

		rhc.audio.play();
		while (rhc.audio.isPlaying()) {}
		Platform.runLater(() -> {
			this.getChildren().remove(rhc.imageView);
		});

		if (this.showingHome == rhc) {
			this.big_small.play();
			this.showingHome = null;
		}
	}

	private class PartCharacterMouseEvent {
		private boolean haveClickEvent = false;

		private void mouseEntered(MouseEvent ev) {
			this.haveClickEvent = true;
		}

		private void mouseExited(MouseEvent ev) {
			this.haveClickEvent = false;
		}

		private void mousePressed(MouseEvent ev) {
			this.haveClickEvent = true;
		}

		private void mouseReleased(MouseEvent ev) {
			if (this.haveClickEvent) {
				//重置放置语音
				PartCharacter.this.idle.reset();

				Thread thread = new Thread(PartCharacter.this::handleEvent);
				thread.setDaemon(true);
				thread.start();
			}
		}
	}
}
