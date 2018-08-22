package fkg.book.temp;

import com.moekagari.tool.other.FXUtils;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * @author MoeKagari
 */
public class WindowPaneStand extends StackPane implements WindowPane {
	private final ImageView stand, stand_s;

	public WindowPaneStand() {
		this.setAlignment(Pos.CENTER);
		this.setBackground(FXUtils.createBackground(Color.gray(0.1)));
		this.setVisible(false);
		this.setOnMouseClicked(ev -> this.setVisible(false));

		this.stand = new ImageView();
		this.stand_s = new ImageView();

		HBox imageViewContainer = new HBox(5, this.stand_s, this.stand);
		imageViewContainer.setAlignment(Pos.CENTER);
		this.getChildren().add(imageViewContainer);

		//通过鼠标滚动 , 缩放
		imageViewContainer.setOnScroll(ev -> {
			/*
			//鼠标未处于图片之上时 , 不处理
			EventTarget eventTarget = ev.getTarget();
			if(eventTarget != this.stand && eventTarget != this.stand_s) {
				return;
			}

			if(ev.getDeltaY() == 0) {
				return;
			}

			int sign = ev.getDeltaY() > 0 ? 1 : -1;
			double delta = 0.1 * sign;

			/*
			double
					newTranslateX = imageViewContainer.getTranslateX() +
					(imageViewContainer.getWidth() / 2 - ev.getX()) * delta * imageViewContainer.getScaleX(),
					newTranslateY = imageViewContainer.getTranslateY() +
							(imageViewContainer.getHeight() / 2 - ev.getY()) * delta * imageViewContainer.getScaleY();
			imageViewContainer.setTranslateX(newTranslateX);
			imageViewContainer.setTranslateY(newTranslateY);
			//*/

			/*
			double newScaleX = imageViewContainer.getScaleX() + delta,
					newScaleY = imageViewContainer.getScaleY() + delta;
			imageViewContainer.setScaleX(newScaleX);
			imageViewContainer.setScaleY(newScaleY);
			//*/
		});

		for(ImageView imageView : new ImageView[]{this.stand_s, this.stand}) {
			//鼠标点击此元素之后 , 不会触发父级元素的鼠标点击事件
			imageView.setOnMouseClicked(Event::consume);

			/*
			//鼠标拖动
			double[] xys = {0, 0, 0, 0};
			imageView.setOnMousePressed(ev -> {
				xys[0] = imageViewContainer.getTranslateX();
				xys[1] = imageViewContainer.getTranslateY();
				xys[2] = ev.getSceneX();
				xys[3] = ev.getSceneY();
			});
			imageView.setOnMouseDragged(ev -> {
				imageViewContainer.setTranslateX(xys[0] + ev.getSceneX() - xys[2]);
				imageViewContainer.setTranslateY(xys[1] + ev.getSceneY() - xys[3]);
			});
			//*/
		}
	}

	@Override public void showCharaStand(AbstractCharaData chara) {
		this.stand.setImage(chara.getStand());
		this.stand_s.setImage(chara.getStandS());
		this.setVisible(true);
	}
}
