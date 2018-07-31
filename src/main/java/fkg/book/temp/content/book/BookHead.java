package fkg.book.temp.content.book;

import com.moekagari.tool.lambda.LambdaUtils;
import com.moekagari.tool.other.FXUtils;
import fkg.book.temp.content.WindowPaneContentTabNodeBook.TabNodeBookRow;

/**
 * @author MoeKagari
 */
public class BookHead extends TabNodeBookRow {
	public BookHead(BookHeadCell[] bookHeadCellArray) {
		super(bookHeadCellArray, LambdaUtils::returnSelf);
		FXUtils.addStyleClass(this, "book-head");
	}
}
