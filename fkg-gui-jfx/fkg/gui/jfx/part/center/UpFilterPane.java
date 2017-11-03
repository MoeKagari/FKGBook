package fkg.gui.jfx.part.center;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import fkg.gui.jfx.other.CharacterData;
import fkg.gui.jfx.other.filter.AttackAttributeFilter;
import fkg.gui.jfx.other.filter.CountryFilter;
import fkg.gui.jfx.other.filter.Filter;
import fkg.gui.jfx.other.filter.OEBFilter;
import fkg.gui.jfx.other.filter.RarityFilter;
import fkg.gui.jfx.other.filter.SkilltypeFilter;
import fkg.other.StringInteger;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tool.FXUtils;
import tool.function.FunctionUtils;

public class UpFilterPane extends VBox {
	private final CenterShowListPane centerShowListPane;
	private IntSupplier favorIndex = () -> 0;
	private Predicate<CharacterData> filter = cd -> cd.isCharacter() && cd.getId() <= 100000000;

	public UpFilterPane(CenterShowListPane centerShowListPane) {
		this.centerShowListPane = centerShowListPane;

		ComboBox<String> rfcb = this.createComboBox(RarityFilter.SIS, RarityFilter::new),
				aafcb = this.createComboBox(AttackAttributeFilter.SIS, AttackAttributeFilter::new),
				cfcb = this.createComboBox(CountryFilter.SIS, CountryFilter::new),
				sfcb = this.createComboBox(SkilltypeFilter.SIS, SkilltypeFilter::new);
		HBox cbGroup = FXUtils.createHBox(15, Pos.CENTER_LEFT, false,
				FXUtils.createHBox(0, Pos.CENTER, false, new Label("稀有度 : "), rfcb),
				FXUtils.createHBox(0, Pos.CENTER, false, new Label("攻击属性 : "), aafcb),
				FXUtils.createHBox(0, Pos.CENTER, false, new Label("国家 : "), cfcb),
				FXUtils.createHBox(0, Pos.CENTER, false, new Label("技能 : "), sfcb),
				new Button("清除") {
					{
						this.setOnAction(ev -> {
							Stream.of(rfcb, aafcb, cfcb, sfcb).forEach(cb -> cb.getSelectionModel().select(0));
							centerShowListPane.updateTable();
						});
					}
				}
		/**/);

		this.setSpacing(4);
		this.getChildren().addAll(this.createFavorGroup(), this.createOebGroup(), cbGroup);
		BorderPane.setMargin(this, new Insets(4, 0, 4, 0));
	}

	private HBox createFavorGroup() {
		HBox favorGroup = new HBox(7);
		{
			ToggleGroup group = new ToggleGroup();
			FunctionUtils.forEach(new StringInteger[] {
					new StringInteger(0, "200%"),
					new StringInteger(1, "100%"),
					new StringInteger(2, "000%")
					/**/ }, si -> {
						RadioButton rb = new RadioButton(si.getString());

						if (si.getInteger() == 0) rb.setSelected(true);
						rb.setToggleGroup(group);
						favorGroup.getChildren().add(rb);

						this.favorIndex = FunctionUtils.addIntSupplier(this.favorIndex, () -> rb.isSelected() ? si.getInteger() : 0);
					});
			group.selectedToggleProperty().addListener((source, oldValue, newValue) -> this.centerShowListPane.updateTable());
		}
		return favorGroup;
	}

	private HBox createOebGroup() {
		HBox oebGroup = new HBox(7);
		{
			ToggleGroup group = new ToggleGroup();
			IntStream.range(0, OEBFilter.SIS.length).forEach(index -> {
				StringInteger si = OEBFilter.SIS[index];
				RadioButton rd = new RadioButton(si.getString());

				if (si.getInteger() == 12) rd.setSelected(true);
				rd.setToggleGroup(group);
				oebGroup.getChildren().add(rd);

				OEBFilter of = new OEBFilter(index);
				this.filter = this.filter.and(cd -> {
					return FunctionUtils.isFalse(rd.isSelected()) || of.filter(cd);
				});
			});
			group.selectedToggleProperty().addListener((source, oldValue, newValue) -> this.centerShowListPane.updateTable());
		}
		return oebGroup;
	}

	private ComboBox<String> createComboBox(StringInteger[] SIS, IntFunction<Filter> fun) {
		ComboBox<String> combo = new ComboBox<>();
		combo.setItems(FXCollections.observableArrayList(Arrays.stream(SIS).map(StringInteger::getString).toArray(String[]::new)));
		combo.getSelectionModel().select(0);
		combo.getSelectionModel().selectedItemProperty().addListener((source, oldValue, newValue) -> this.centerShowListPane.updateTable());
		this.filter = this.filter.and(cd -> {
			return fun.apply(combo.getSelectionModel().getSelectedIndex()).filter(cd);
		});
		return combo;
	}

	public Predicate<CharacterData> getFilter() {
		return this.filter;
	}

	public boolean showChineseName() {
		return false;
	}

	public int getFavorIndex() {
		return this.favorIndex.getAsInt();
	}
}
