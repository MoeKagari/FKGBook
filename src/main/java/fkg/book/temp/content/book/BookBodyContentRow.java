package fkg.book.temp.content.book;

import fkg.book.gui.AbstractCharaData;
import fkg.book.temp.content.WindowPaneContentTabNodeBook.TabNodeBookRow;
import javafx.scene.Node;

/**
 * @author MoeKagari
 */
public class BookBodyContentRow extends TabNodeBookRow {
	public BookBodyContentRow(BookHeadCell[] bookHeadCellArray, AbstractCharaData chara) {
		super(bookHeadCellArray, bookHeadCell -> (Node) bookHeadCell.value.apply(chara));
		/*
		this.hoverProperty().addListener(FXUtils.makeChangeListener((oldValue, newValue) -> {
			String hover = "book-body-row-hover";
			if(newValue) {
				this.getStyleClass().add(hover);
			} else {
				this.getStyleClass().remove(hover);
			}
		}));
		*/
	}

	public void refreshIndex(int index) {
		this.getChildren().forEach(node -> ((BookBodyContentRowCell) node).followIndexChange(index));
		/*
		String odd = "book-body-row-odd", even = "book-body-row-even";
		this.getStyleClass().remove(odd);
		this.getStyleClass().remove(even);
		this.getStyleClass().add(index % 2 == 0 ? even : odd);
		*/
	}
}
