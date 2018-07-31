package fkg.book.temp;

import com.moekagari.tool.io.FileUtils;
import com.moekagari.tool.other.FXUtils;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * @author MoeKagari
 */
public class WindowPaneBackground extends ImageView implements WindowPane {
	public WindowPaneBackground() {
		FXUtils.addStyleClass(this, "background");
		this.setEffect(new Lighting(new Light.Distant(45.0, 45.0, Color.gray(0.2))));
		this.setImage(new Image(FileUtils.getFileUrlString("resources\\background.jpg")));
	}

	@Override public void focusParentSizeChange(double width, double height) {
		Image image = this.getImage();
		double scale = Double.max(width / image.getWidth(), height / image.getHeight());
		this.setScaleX(scale);
		this.setScaleY(scale);
	}
}
