package fkg.gui.jfx.part.center;

import java.util.Comparator;

import fkg.gui.jfx.ApplicationMain;
import fkg.gui.jfx.other.CharacterData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;

public class CenterShowListPane extends BorderPane {
	public final UpFilterPane filterPane = new UpFilterPane(this);
	public final DownDataTable dataTable = new DownDataTable(this);

	public CenterShowListPane(ApplicationMain main) {
		this.setTop(this.filterPane);
		this.setCenter(this.dataTable);
		this.updateTable();
		this.dataTable.getSelectionModel().selectedItemProperty().addListener((source, oldValue, newValue) -> {
			main.rightShow.showCharacter(newValue);
		});
	}

	protected void updateTable() {
		ObservableList<CharacterData> datas = FXCollections.observableArrayList(CharacterData.ALL_CHARA.values());

		datas.removeIf(data -> {
			return data.isCharacter() == false || data.getId() > 100000000 || this.filterPane.getFilter().negate().test(data);
		});
		datas.sort(Comparator.comparingInt(CharacterData::getBid).reversed().thenComparing(Comparator.comparingInt(CharacterData::getOeb)));

		this.dataTable.getItems().clear();
		this.dataTable.setItems(datas);
	}
}
