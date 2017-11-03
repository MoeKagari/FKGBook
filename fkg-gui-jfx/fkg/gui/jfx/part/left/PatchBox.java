package fkg.gui.jfx.part.left;

import fkg.config.AppConfig;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import tool.FXUtils;

public class PatchBox extends HBox {
	private final CheckBox allCG = new CheckBox("全CG");
	private final CheckBox replace = new CheckBox("替换副团长");
	private final TextField futuanzhangID = new TextField();

	public PatchBox() {
		this.allCG.setSelected(AppConfig.isAllCG());
		this.allCG.selectedProperty().addListener((source, oldValue, newValue) -> AppConfig.setAllCG(newValue));

		this.replace.setSelected(AppConfig.isTihuan());
		this.replace.selectedProperty().addListener((source, oldValue, newValue) -> AppConfig.setTihuan(newValue));

		this.futuanzhangID.setMinWidth(80);
		this.futuanzhangID.setMaxWidth(80);
		this.futuanzhangID.setText(String.valueOf(AppConfig.getFutuanzhangID()));
		this.futuanzhangID.textProperty().addListener((source, oldValue, newValue) -> {
			try {
				AppConfig.setFutuanzhangID(Integer.parseInt(newValue));
			} catch (Exception ex) {
				this.futuanzhangID.setText(oldValue);
			}
		});

		this.setSpacing(15);
		this.setAlignment(Pos.CENTER);
		this.setBorder(FXUtils.createNewBorder());
		this.getChildren().addAll(
				FXUtils.createHBox(0, Pos.CENTER, false, this.allCG),
				FXUtils.createVBox(2, Pos.CENTER, false, this.replace, this.futuanzhangID)//
		);
	}
}
