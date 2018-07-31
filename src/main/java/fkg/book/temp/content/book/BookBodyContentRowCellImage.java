package fkg.book.temp.content.book;

import com.moekagari.tool.acs.ExStreamUtils;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * @author MoeKagari
 */
public class BookBodyContentRowCellImage extends StackPane implements BookBodyContentRowCell {
	public BookBodyContentRowCellImage(Image[] images) {
		this.getChildren().addAll(ExStreamUtils.of(images).map(image -> {
			ImageView imageView = new ImageView(image);
			StackPane.setAlignment(imageView, Pos.CENTER);
			return imageView;
		}).toList());
	}
}
