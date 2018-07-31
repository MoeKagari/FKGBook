package fkg.book.temp.content.book;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.StackPane;

/**
 * @author MoeKagari
 */
public class BookBody extends StackPane {
	public final ScrollPane scrollPane;
	public final ScrollBar scrollBar;

	public BookBody() {
		this.scrollPane = new ScrollPane(new BookBodyContent());
		StackPane.setAlignment(this.scrollPane, Pos.CENTER);
		this.scrollPane.setFitToWidth(true);
		this.scrollPane.setFitToHeight(true);
		//隐藏 scrollPane 的 scrollBar
		this.scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		this.scrollPane.getStyleClass().add("book-scroll-pane");

		this.scrollBar = new ScrollBar();
		StackPane.setAlignment(this.scrollBar, Pos.CENTER_RIGHT);
		this.scrollBar.setOrientation(Orientation.VERTICAL);
		//将 scrollPane 的 v scrollBar 的 value,min,max 与 悬浮的scrollBar 双向绑定
		this.scrollBar.valueProperty().bindBidirectional(this.scrollPane.vvalueProperty());
		this.scrollBar.maxProperty().bindBidirectional(this.scrollPane.vmaxProperty());
		this.scrollBar.minProperty().bindBidirectional(this.scrollPane.vminProperty());
		this.scrollBar.getStyleClass().add("book-scroll-bar");

		this.getChildren().addAll(this.scrollPane, this.scrollBar);
	}
}
