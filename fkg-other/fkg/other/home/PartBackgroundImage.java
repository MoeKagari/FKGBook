package fkg.other.home;

import javafx.scene.image.Image;

/**
 * 主页背景静图
 * 
 * @author MoeKagari
 */
public class PartBackgroundImage extends PartBackground {
	public PartBackgroundImage(String image_path, double translateX, double translateY) {
		this(new Image(image_path), translateX, translateY);
	}

	public PartBackgroundImage(Image image, double translateX, double translateY) {
		this.setTranslateX(translateX);
		this.setTranslateY(translateY);
		this.setImage(image);
	}
}
