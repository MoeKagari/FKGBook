package fkg.book.temp.content.book;

/**
 * @author MoeKagari
 */
public class BookBodyContentRowCellIndex extends BookBodyContentRowCellString {
	public BookBodyContentRowCellIndex() {
		super("");
	}

	@Override public void followIndexChange(int index) {
		this.setText(Integer.toString(index + 1));
	}
}
