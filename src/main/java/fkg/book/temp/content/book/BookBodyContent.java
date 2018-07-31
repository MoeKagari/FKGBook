package fkg.book.temp.content.book;

import com.moekagari.tool.acs.CollectionUtils;
import javafx.collections.ListChangeListener.Change;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * @author MoeKagari
 */
public class BookBodyContent extends VBox {
	public BookBodyContent() {
		this.getStyleClass().add("book-body-content");
		this.getChildren().addListener((Change<? extends Node> change) -> CollectionUtils.forEachUseIndex(
				this.getChildren(), (index, node) -> ((BookBodyContentRow) node).refreshIndex(index)
		));
	}
}
