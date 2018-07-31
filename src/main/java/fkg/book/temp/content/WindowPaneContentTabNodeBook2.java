package fkg.book.temp.content;

import com.moekagari.tool.acs.ArrayUtils;
import com.moekagari.tool.acs.CollectionUtils;
import com.moekagari.tool.acs.ExStreamUtils;
import com.moekagari.tool.lambda.LambdaUtils;
import com.moekagari.tool.other.FXUtils;
import fkg.book.gui.AbstractCharaData;
import fkg.book.masterdata.GetMasterData;
import fkg.book.temp.WindowPaneContent;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

/**
 * @author MoeKagari
 */
public class WindowPaneContentTabNodeBook2 extends TableView<AbstractCharaData> implements WindowPaneContentTabNode {
	private final WindowPaneContent windowPaneContent;

	public WindowPaneContentTabNodeBook2(WindowPaneContent windowPaneContent) {
		this.setId("book2");
		this.windowPaneContent = windowPaneContent;
		this.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		double normalWidth = 80;
		List<TableColumnSuper<?>> tableColumnList = ArrayUtils.asList(
				new TableColumnSuper<>(50, "", getIndexCellFactory(), LambdaUtils::returnSelf),
				new TableColumnSuper<>(80, "", getIconCellFactory(this), AbstractCharaData::getIcons, Pos.CENTER),
				new TableColumnInteger(100, "ID", AbstractCharaData::getId, Pos.CENTER),
				new TableColumnString(null, "花名", AbstractCharaData::getName),
				new TableColumnString(180, "国家", AbstractCharaData::getCountryString),
				new TableColumnString(120, "稀有度", AbstractCharaData::getRarityString),
				new TableColumnString(normalWidth, "属性", AbstractCharaData::getAttackAttributeString, Pos.CENTER),
				new TableColumnInteger(normalWidth, "移动力", AbstractCharaData::getMove, Pos.CENTER),
				new TableColumnInteger(normalWidth, "HP", AbstractCharaData::getHPFinal, Pos.CENTER),
				new TableColumnInteger(normalWidth, "攻击力", AbstractCharaData::getAttackFinal, Pos.CENTER),
				new TableColumnInteger(normalWidth, "防御力", AbstractCharaData::getDefenseFinal, Pos.CENTER),
				new TableColumnInteger(normalWidth, "总合力", AbstractCharaData::getPowerFinal, Pos.CENTER),
				new TableColumnString(100, "状态", AbstractCharaData::getStateString),
				new TableColumnString(160, "衣装", AbstractCharaData::getVersion),
				new TableColumnSuper<>(100, "", getWikiCellFactory(), AbstractCharaData::getName, Pos.CENTER)
		);
		this.getColumns().addAll(tableColumnList);
		tableColumnList.forEach(TableColumnSuper::setPrefWidth);
		long freeWidthTableColumnNumber = tableColumnList.stream().filter(TableColumnSuper::isWidthFree).count();
		double usedWidth = ExStreamUtils.stream(tableColumnList)
		                                .map(tableColumnSuper -> tableColumnSuper.width)
		                                .notNull()
		                                .sumDouble(Number::doubleValue);
		this.widthProperty().addListener(FXUtils.makeChangeListener((oldValue, newValue) -> CollectionUtils.forEach(
				tableColumnList,
				TableColumnSuper::isWidthFree,
				tableColumn -> tableColumn.setPrefWidth(Double.max(
						270,
						(newValue.doubleValue() - usedWidth - 13) / freeWidthTableColumnNumber
				))
		)));
	}

	@Override public void activeBefore() {
		this.getItems().setAll(
				ExStreamUtils.mapValueStream(GetMasterData.ALL_CHARA).filter(AbstractCharaData::canShowInApplication)
				             .filter(AbstractCharaData::isMostLevel)
				             .filter(this.windowPaneContent.filter.windowPaneContentTabNode.getFilter())
				             .sorted(this.windowPaneContent.sorter.windowPaneContentTabNode.getSorter())
				             //.sortedInt(value -> -value.getPower()[0])
				             //.limit(30)
				             .toList()
		);
	}

	private static class TableColumnSuper<T> extends TableColumn<AbstractCharaData, T> {
		private final Number width;

		public TableColumnSuper(
				Number width, String text,
				Callback<TableColumn<AbstractCharaData, T>, TableCell<AbstractCharaData, T>> cellFactory,
				Function<AbstractCharaData, T> cellValue
		) {
			this(width, text, cellFactory, cellValue, Pos.CENTER_LEFT);
		}

		public TableColumnSuper(
				Number width, String text,
				Callback<TableColumn<AbstractCharaData, T>, TableCell<AbstractCharaData, T>> cellFactory,
				Function<AbstractCharaData, T> cellValue,
				Pos pos
		) {
			super(text);
			this.width = width;
			this.getStyleClass().add(pos.toString().toLowerCase());
			this.setCellFactory(param -> {
				TableCell<AbstractCharaData, T> tableCell = cellFactory.call(param);
				tableCell.setAlignment(pos);
				tableCell.setBorder(FXUtils.createNewBorder(0, 0, 0, 0));
				return tableCell;
			});
			this.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(cellValue.apply(param.getValue())));
			this.setEditable(false);
			this.setResizable(false);
			this.setSortable(false);
		}

		boolean isWidthFree() {
			return this.width == null;
		}

		void setPrefWidth() {
			if(!this.isWidthFree()) {
				double width = this.width.doubleValue();
				this.setMinWidth(width);
				this.setPrefWidth(width);
				this.setMaxWidth(width);
			}
		}
	}

	private static class TableColumnString extends TableColumnSuper<String> {
		public TableColumnString(Number width, String text, Function<AbstractCharaData, String> cellValue) {
			super(width, text, TextFieldTableCell.forTableColumn(), cellValue, Pos.CENTER_LEFT);
		}

		public TableColumnString(Number width, String text, Function<AbstractCharaData, String> cellValue, Pos pos) {
			super(width, text, TextFieldTableCell.forTableColumn(), cellValue, pos);
		}
	}

	private static class TableColumnInteger extends TableColumnSuper<Integer> {
		public TableColumnInteger(Number width, String text, Function<AbstractCharaData, Integer> cellValue, Pos pos) {
			super(width, text, TextFieldTableCell.forTableColumn(new IntegerStringConverter()), cellValue, pos);
		}
	}

	private static Callback<TableColumn<AbstractCharaData, AbstractCharaData>, TableCell<AbstractCharaData, AbstractCharaData>> getIndexCellFactory() {
		return param -> new TableCell<AbstractCharaData, AbstractCharaData>() {
			@Override protected void updateItem(AbstractCharaData item, boolean empty) {
				super.updateItem(item, empty);
				TableRow<?> tableRow = this.getTableRow();
				this.setText((tableRow == null || item == null) ? "" : String.valueOf(tableRow.getIndex() + 1));
			}
		};
	}

	private static Callback<TableColumn<AbstractCharaData, Image[]>, TableCell<AbstractCharaData, Image[]>> getIconCellFactory(WindowPaneContentTabNodeBook2 book) {
		return param -> new TableCell<AbstractCharaData, Image[]>() {
			final ImageView[] imageViews = new ImageView[]{new ImageView(), new ImageView(), new ImageView(), new ImageView(), new ImageView()};

			{
				this.setGraphic(LambdaUtils.init(new StackPane(), stackPane -> {
					int iconSize = 50;
					stackPane.getChildren().addAll(this.imageViews);
					stackPane.setScaleX(0.5);
					stackPane.setScaleY(0.5);
					stackPane.setMinSize(iconSize, iconSize);
					stackPane.setMaxSize(iconSize, iconSize);
					stackPane.setPrefSize(iconSize, iconSize);
					stackPane.setCursor(Cursor.HAND);
					stackPane.setOnMouseClicked(ev -> {
						AbstractCharaData chara = (AbstractCharaData) this.getTableRow().getItem();
						book.windowPaneContent.window.windowPaneList.forEach(windowPane -> windowPane.showCharaDetail(chara));
					});
				}));
			}

			@Override protected void updateItem(Image[] item, boolean empty) {
				super.updateItem(item, empty);
				for(int index = 0; index < 5; index++) {
					this.imageViews[index].setImage((empty || item == null) ? null : item[index]);
				}
			}
		};
	}

	private static Callback<TableColumn<AbstractCharaData, String>, TableCell<AbstractCharaData, String>> getWikiCellFactory() {
		return param -> new TableCell<AbstractCharaData, String>() {
			final Button wikiButton = new Button("WIKI");

			{
				this.wikiButton.setUnderline(true);
				this.wikiButton.setPadding(new Insets(0));
				this.wikiButton.setCursor(Cursor.HAND);
				this.wikiButton.setBackground(FXUtils.createBackground(Color.TRANSPARENT));
				this.setGraphic(this.wikiButton);
			}

			@Override protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if(!empty && item != null) {
					this.wikiButton.setOnAction(ev -> {
						try {
							String url = "https://xn--eckq7fg8cygsa1a1je.xn--wiki-4i9hs14f.com/index.php?" + item;
							Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
						} catch(IOException e) {
							e.printStackTrace();
						}
					});
				}
			}
		};
	}
}
