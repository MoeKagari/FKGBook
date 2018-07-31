package fkg.book.temp.content;

import com.moekagari.tool.acs.CollectionUtils;
import com.moekagari.tool.acs.ExStreamUtils;
import fkg.book.gui.AbstractCharaData;
import fkg.book.masterdata.GetMasterData;
import fkg.book.temp.WindowPaneContent;
import fkg.book.temp.content.book.BookBody;
import fkg.book.temp.content.book.BookBodyContent;
import fkg.book.temp.content.book.BookBodyContentRow;
import fkg.book.temp.content.book.BookBodyContentRowCellImage;
import fkg.book.temp.content.book.BookBodyContentRowCellIndex;
import fkg.book.temp.content.book.BookBodyContentRowCellInteger;
import fkg.book.temp.content.book.BookBodyContentRowCellString;
import fkg.book.temp.content.book.BookBodyContentRowCellWiki;
import fkg.book.temp.content.book.BookHead;
import fkg.book.temp.content.book.BookHeadCell;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.function.Function;

/**
 * @author MoeKagari
 */
public class WindowPaneContentTabNodeBook extends BorderPane implements WindowPaneContentTabNode {
	private final WindowPaneContent windowPaneContent;
	private final static double NORMAL_WIDTH = 80;
	private final BookHeadCell[] bookHeadCellArray = new BookHeadCell[]{
			new BookHeadCell("", chara -> new BookBodyContentRowCellIndex(), 50),
			new BookHeadCell("", chara -> new BookBodyContentRowCellImage(chara.getIcons()), 80),
			new BookHeadCell("ID", chara -> new BookBodyContentRowCellInteger(chara.getId()), 100, HPos.CENTER),
			new BookHeadCell("花名", chara -> new BookBodyContentRowCellString(chara.getName())),
			new BookHeadCell("国家", chara -> new BookBodyContentRowCellString(chara.getCountryString()), 180),
			new BookHeadCell("稀有度", chara -> new BookBodyContentRowCellString(chara.getRarityString()), 120),
			new BookHeadCell("属性", chara -> new BookBodyContentRowCellString(chara.getAttackAttributeString()), NORMAL_WIDTH, HPos.CENTER),
			new BookHeadCell("移动力", chara -> new BookBodyContentRowCellInteger(chara.getMove()), NORMAL_WIDTH, HPos.CENTER),
			new BookHeadCell("HP", chara -> new BookBodyContentRowCellInteger(chara.getHP()[0]), NORMAL_WIDTH, HPos.CENTER),
			new BookHeadCell("攻击力", chara -> new BookBodyContentRowCellInteger(chara.getAttack()[0]), NORMAL_WIDTH, HPos.CENTER),
			new BookHeadCell("防御力", chara -> new BookBodyContentRowCellInteger(chara.getDefense()[0]), NORMAL_WIDTH, HPos.CENTER),
			new BookHeadCell("总合力", chara -> new BookBodyContentRowCellInteger(chara.getPower()[0]), NORMAL_WIDTH, HPos.CENTER),
			new BookHeadCell("状态", chara -> new BookBodyContentRowCellString(chara.getStateString()), 100),
			new BookHeadCell("版本", chara -> new BookBodyContentRowCellString(chara.getVersion()), 150),
			new BookHeadCell("", chara -> new BookBodyContentRowCellWiki(chara.getName()), 100, HPos.CENTER)
	};

	public WindowPaneContentTabNodeBook(WindowPaneContent windowPaneContent) {
		this.setId("book");
		this.windowPaneContent = windowPaneContent;
		this.setTop(new BookHead(this.bookHeadCellArray));
		this.setCenter(new BookBody());
	}

	@Override public void activeBefore() {
		((BookBodyContent)
				((BookBody) this.getCenter()).scrollPane.getContent()
		).getChildren().setAll(
				ExStreamUtils.mapValueStream(GetMasterData.ALL_CHARA).filter(AbstractCharaData::canShowInApplication)
				             .filter(this.windowPaneContent.filter.windowPaneContentTabNode.getFilter())
				             .sorted(this.windowPaneContent.sorter.windowPaneContentTabNode.getSorter())
				             .limit(20)
				             .map(chara -> new BookBodyContentRow(this.bookHeadCellArray, chara))
				             .toList()
		);
	}

	public static class TabNodeBookRow extends GridPane {
		public TabNodeBookRow(BookHeadCell[] bookHeadCellArray, Function<BookHeadCell, Node> rowCellMapper) {
			this.getStyleClass().add("book-row");
			this.getColumnConstraints().addAll(
					ExStreamUtils.of(bookHeadCellArray)
					             .map(bookHeadCell -> bookHeadCell.columnConstraints)
					             .toList()
			);
			CollectionUtils.forEachUseIndex(
					ExStreamUtils.of(bookHeadCellArray).map(rowCellMapper).toList(),
					this::addColumn
			);
		}
	}
}
