package fkg.gui.jfx;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import fkg.config.ShowConfig;
import fkg.gui.jfx.filter.AttackAttributeFilter;
import fkg.gui.jfx.filter.CountryFilter;
import fkg.gui.jfx.filter.Filter;
import fkg.gui.jfx.filter.OEBFilter;
import fkg.gui.jfx.filter.RarityFilter;
import fkg.gui.jfx.filter.SkilltypeFilter;
import fkg.masterdata.CharacterLeaderSkillDescription;
import fkg.masterdata.CharacterSkill;
import fkg.masterdata.GetMasterData;
import fkg.other.StringInteger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import tool.Downloader;
import tool.FXUtils;
import tool.compress.ZLib;
import tool.function.FunctionUtils;

public class CenterShowListPane extends BorderPane {
	public final ApplicationMain main;
	public final UpFilterPane filterPane = new UpFilterPane();
	public final DownDataTable table = new DownDataTable();

	public CenterShowListPane(ApplicationMain main) {
		this.main = main;
		this.setTop(this.filterPane);
		this.setCenter(this.table);
		BorderPane.setMargin(this.filterPane, new Insets(4, 0, 4, 4));
		this.updateTable();
		this.table.getSelectionModel().selectedItemProperty().addListener((source, oldValue, newValue) -> {
			main.rightShow.showCharacter(newValue);
		});
	}

	public void showCharacter(CharacterData chara) {
		TableViewSelectionModel<CharacterData> selectionModel = this.table.getSelectionModel();
		if (selectionModel.getSelectedItem() == chara) {
			return;
		}
		selectionModel.select(chara);
	}

	private void updateTable() {
		ObservableList<CharacterData> datas = FXCollections.observableArrayList(CharacterData.ALL_CHARA.values());
		{
			datas.removeIf(data -> {
				return data.isCharacter() == false || data.getId() > 100000000 || this.filterPane.filter.negate().test(data);
			});
			datas.sort(Comparator.comparingInt(CharacterData::getBid).reversed().thenComparing(Comparator.comparingInt(CharacterData::getOeb)));
		}

		this.table.getItems().clear();
		this.table.setItems(datas);
	}

	public class UpFilterPane extends VBox {
		private Predicate<CharacterData> filter;
		private int favorIndex = 0;

		public UpFilterPane() {
			HBox favorGroup = new HBox(7);
			VBox.setMargin(favorGroup, new Insets(2, 0, 2, 0));
			{
				ToggleGroup group = new ToggleGroup();
				for (int index = 0; index < ShowConfig.STRING_FAVOR.length; index++) {
					RadioButton rb = new RadioButton(ShowConfig.STRING_FAVOR[index]);
					if (index == this.favorIndex) {
						rb.setSelected(true);
					}
					rb.setToggleGroup(group);
					favorGroup.getChildren().add(rb);

					final int temp = index;
					rb.selectedProperty().addListener((source, oldValue, newValue) -> {
						if (newValue) {
							this.favorIndex = temp;
							CenterShowListPane.this.updateTable();
						}
					});
				}
			}

			Predicate<CharacterData> oebFilter = null;
			HBox oebGroup = new HBox(7);
			VBox.setMargin(oebGroup, new Insets(2, 0, 2, 0));
			{
				ToggleGroup group = new ToggleGroup();
				for (int index = 0; index < OEBFilter.SIS.length; index++) {
					StringInteger si = OEBFilter.SIS[index];
					RadioButton rd = new RadioButton(si.getString());
					if (si.getInteger() == 12) {
						rd.setSelected(true);
					}
					rd.setToggleGroup(group);
					oebGroup.getChildren().add(rd);

					OEBFilter of = new OEBFilter(index);
					Predicate<CharacterData> fl = cd -> FunctionUtils.isFalse(rd.isSelected()) || of.filter(cd);
					if (oebFilter == null) {
						oebFilter = fl;
					} else {
						oebFilter = oebFilter.and(fl);
					}
				}
				group.selectedToggleProperty().addListener((source, oldValue, newValue) -> CenterShowListPane.this.updateTable());
			}
			this.filter = oebFilter;

			this.getChildren().addAll(
					favorGroup, oebGroup,
					FXUtils.createHBox(10, Pos.CENTER_LEFT, false//
							, new Label("稀有度 : "), this.createComboBox(RarityFilter.SIS, RarityFilter::new)//
							, new Label("攻击属性 : "), this.createComboBox(AttackAttributeFilter.SIS, AttackAttributeFilter::new)//
							, new Label("国家 : "), this.createComboBox(CountryFilter.SIS, CountryFilter::new)//
							, new Label("技能 : "), this.createComboBox(SkilltypeFilter.SIS, SkilltypeFilter::new)//
					/**/)
			/**/);
		}

		private ComboBox<String> createComboBox(StringInteger[] SIS, IntFunction<Filter> fun) {
			ComboBox<String> combo = new ComboBox<>();
			VBox.setMargin(combo, new Insets(2, 0, 2, 0));
			combo.setItems(FXCollections.observableArrayList(Arrays.stream(SIS).map(StringInteger::getString).toArray(String[]::new)));
			combo.getSelectionModel().select(0);
			combo.getSelectionModel().selectedItemProperty().addListener((source, oldValue, newValue) -> CenterShowListPane.this.updateTable());
			this.filter = this.filter.and(cd -> {
				return fun.apply(combo.getSelectionModel().getSelectedIndex()).filter(cd);
			});
			return combo;
		}

		public boolean showChineseName() {
			return false;
		}
	}

	public class DownDataTable extends TableView<CharacterData> {
		public DownDataTable() {
			this.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

			FXUtils.addNumberColumn(this);
			this.addNewColumn(false, "", ImageViewTableCell::new, CharacterData::getIcon);
			this.addNewColumn("ID", this.getIntegerTableCellFactory(), CharacterData::getId);
			this.addNewColumn("花名", this.getStringTableCellFactory(), cd -> {
				return CenterShowListPane.this.filterPane.showChineseName() ? cd.getChineseName() : cd.getName();
			});
			this.addNewColumn("稀有度", this.getStringTableCellFactory(), CharacterData::getRarityString);
			this.addNewColumn("属性", this.getStringTableCellFactory(), CharacterData::getAttackAttributeString);
			this.addNewColumn("移动力", this.getIntegerTableCellFactory(), CharacterData::getMove);
			this.addNewColumn("HP", this.getIntegerTableCellFactory(), cd -> cd.getHP()[CenterShowListPane.this.filterPane.favorIndex]);
			this.addNewColumn("攻击力", this.getIntegerTableCellFactory(), cd -> cd.getAttack()[CenterShowListPane.this.filterPane.favorIndex]);
			this.addNewColumn("防御力", this.getIntegerTableCellFactory(), cd -> cd.getDefense()[CenterShowListPane.this.filterPane.favorIndex]);
			this.addNewColumn("综合力", this.getIntegerTableCellFactory(), cd -> cd.getPower()[CenterShowListPane.this.filterPane.favorIndex]);
			this.addNewColumn("国家", this.getStringTableCellFactory(), CharacterData::getCountryString);
			this.addNewColumn("版本", this.getStringTableCellFactory(), CharacterData::getVersion);
			this.addNewColumn("图鉴编号", this.getIntegerTableCellFactory(), CharacterData::getBid);
			this.addNewColumn("开花编号", this.getStringTableCellFactory(), cd -> {
				return FunctionUtils.ifFunction(cd.getBloomNumber(), bn -> bn != 0, String::valueOf, "");
			});
			this.addNewColumn("状态", this.getStringTableCellFactory(), cd -> {
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

		private Callback<TableColumn<CharacterData, String>, TableCell<CharacterData, String>> getStringTableCellFactory() {
			return TextFieldTableCell.forTableColumn();
		}

		private Callback<TableColumn<CharacterData, Integer>, TableCell<CharacterData, Integer>> getIntegerTableCellFactory() {
			return TextFieldTableCell.forTableColumn(new IntegerStringConverter());
		}

		private class ImageViewTableCell extends TableCell<CharacterData, Image> {
			private int size = 50;
			private final ImageView imageView = new ImageView();

			public ImageViewTableCell(TableColumn<CharacterData, Image> param) {
				this.setMinSize(this.size, this.size);
				this.setMaxSize(this.size, this.size);
				this.setAlignment(Pos.CENTER);
				this.setGraphic(this.imageView);
			}

			@Override
			protected void updateItem(Image item, boolean empty) {
				super.updateItem(item, empty);
				this.imageView.setImage((empty || item == null) ? null : item);
			}
		}
	}

	public static abstract class CharacterData {
		public static final Map<Integer, CharacterData> ALL_CHARA = GetMasterData.MASTERCHARACTER
				.stream()
				.collect(Collectors.toMap(CharacterData::getId, FunctionUtils::returnSelf));
		private Image icon, stand, standS;
		private CharacterLeaderSkillDescription skill;
		private CharacterSkill mainSkill;

		private Image getImage(String filedir, IntFunction<String> urlStr, int width, int height) {
			int image_id = this.getId();
			if (this.getOeb() == 3 && this.getKariBloom()) {
				image_id = image_id - 300000 + 1;
			}

			String filepath = String.format("%s\\%d.png", filedir, image_id);
			Image image = null;

			try (FileInputStream fis = new FileInputStream(filepath)) {
				image = new Image(new File(filepath).toURI().toString());
			} catch (Exception e) {
				byte[] bytes = ZLib.decompress(Downloader.download(urlStr.apply(image_id)));
				if (bytes != null) {
					try {
						FileUtils.writeByteArrayToFile(new File(filepath), bytes);
						try (FileInputStream fis = new FileInputStream(filepath)) {
							image = new Image(new File(filepath).toURI().toString());
						} catch (Exception e1) {

						}
					} catch (Exception e2) {

					}
				}
			}

			return (image != null) ? image : new WritableImage(width, height);
		}

		public abstract String getVersion();

		public CharacterSkill getMainSkill() {
			if (this.mainSkill == null) {
				this.mainSkill = GetMasterData.MASTERSKILL.stream().filter(ms -> ms.id == this.getMainSkillNumber()).findFirst().orElse(null);
			}
			return this.mainSkill;
		}

		public abstract int getMainSkillNumber();

		public CharacterLeaderSkillDescription getSkill() {
			if (this.skill == null) {
				this.skill = GetMasterData.MASTERLEADERSKILLDESCRIPTION.stream().filter(mlsd -> mlsd.cid == this.getId()).findFirst().orElse(null);
			}
			return this.skill;
		}

		public abstract boolean isCharacter();

		public abstract int getBloomNumber();

		public abstract int getBid();

		public abstract int getOeb();

		public abstract boolean hasBloom();

		public abstract boolean getKariBloom();

		public String getChineseName() {
			return "";
		}

		public Image getStandS() {
			if (this.standS == null) {
				this.standS = this.getImage(ShowConfig.CHARACTER_STAND_S, ShowConfig::getCharacterStandSNetpath, 329, 467);
			}
			return this.standS;
		}

		public Image getStand() {
			if (this.stand == null) {
				this.stand = this.getImage(ShowConfig.CHARACTER_STAND, ShowConfig::getCharacterStandNetpath, 960, 640);
			}
			return this.stand;
		}

		public Image getIcon() {
			if (this.icon == null) {
				this.icon = this.getImage(ShowConfig.CHARACTER_ICON, ShowConfig::getCharacterIconNetpath, 50, 50);
			}
			return this.icon;
		}

		public abstract int getCountry();

		public String getCountryString() {
			return CountryFilter.SIS[this.getCountry()].getString();
		}

		public abstract int[] getPower();

		public abstract int[] getDefense();

		public abstract int[] getAttack();

		public abstract int[] getHP();

		public abstract int getMove();

		public abstract int getAttackAttribute();

		public String getAttackAttributeString() {
			return AttackAttributeFilter.SIS[this.getAttackAttribute()].getString();
		}

		public abstract int getRarity();

		public String getRarityString() {
			return "★★★★★★★★★★".substring(0, this.getRarity());
		}

		public abstract int getId();

		public abstract String getName();
	}
}
