package fkg.gui.jfx.part.center;

import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import fkg.gui.jfx.other.CharacterData;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;
import tool.FXUtils;
import tool.function.FunctionUtils;

public class DownDataTable extends TableView<CharacterData> {
	public DownDataTable(CenterShowListPane centerShowListPane) {
		this.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		FXUtils.addNumberColumn(this);
		this.addNewColumn(false, "", FXUtils.getImageTableCellFactory(50), CharacterData::getIcon);
		this.addNewColumn("ID", FXUtils.getIntegerTableCellFactory(), CharacterData::getId);
		this.addNewColumn("花名", FXUtils.getStringTableCellFactory(), cd -> {
			return centerShowListPane.filterPane.showChineseName() ? cd.getChineseName() : cd.getName();
		});
		this.addNewColumn("稀有度", FXUtils.getStringTableCellFactory(), CharacterData::getRarityString);
		this.addNewColumn("属性", FXUtils.getStringTableCellFactory(), CharacterData::getAttackAttributeString);
		this.addNewColumn("移动力", FXUtils.getIntegerTableCellFactory(), CharacterData::getMove);
		this.addNewColumn("HP", FXUtils.getIntegerTableCellFactory(), cd -> cd.getHP()[centerShowListPane.filterPane.getFavorIndex()]);
		this.addNewColumn("攻击力", FXUtils.getIntegerTableCellFactory(), cd -> cd.getAttack()[centerShowListPane.filterPane.getFavorIndex()]);
		this.addNewColumn("防御力", FXUtils.getIntegerTableCellFactory(), cd -> cd.getDefense()[centerShowListPane.filterPane.getFavorIndex()]);
		this.addNewColumn("综合力", FXUtils.getIntegerTableCellFactory(), cd -> cd.getPower()[centerShowListPane.filterPane.getFavorIndex()]);
		this.addNewColumn("国家", FXUtils.getStringTableCellFactory(), CharacterData::getCountryString);
		this.addNewColumn("版本", FXUtils.getStringTableCellFactory(), CharacterData::getVersion);
		this.addNewColumn("图鉴编号", FXUtils.getIntegerTableCellFactory(), CharacterData::getBid);
		this.addNewColumn("开花编号", TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
			@Override
			public String toString(Integer object) {
				return object == 0 ? "" : object.toString();
			}

			@Override
			public Integer fromString(String string) {
				return StringUtils.isBlank(string) ? 0 : Integer.parseInt(string.trim());
			}
		}), cd -> FunctionUtils.notNull(cd.getEvolutionChara(), CharacterData::getBloomNumber, 0));
		this.addNewColumn("状态", FXUtils.getStringTableCellFactory(), cd -> {
			switch (cd.getOeb()) {
				case 1:
					return "原始";
				case 2:
					return "进化";
				case 3:
					return "开花";
				default:
					return "";
			}
		});
	}

	private <T> void addNewColumn(String name, Callback<TableColumn<CharacterData, T>, TableCell<CharacterData, T>> cell, Function<CharacterData, T> cellValue) {
		this.addNewColumn(true, name, cell, cellValue);
	}

	private <T> void addNewColumn(boolean sortable, String name, Callback<TableColumn<CharacterData, T>, TableCell<CharacterData, T>> cell, Function<CharacterData, T> cellValue) {
		TableColumn<CharacterData, T> tableColumn = new TableColumn<>();
		tableColumn.setSortable(sortable);
		tableColumn.setText(name);
		tableColumn.setCellFactory(cell);
		tableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(cellValue.apply(param.getValue())));
		this.getColumns().add(tableColumn);
	}
}
