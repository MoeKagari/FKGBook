package fkg.gui.jfx.part.right;

import fkg.gui.jfx.other.CharacterData;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import tool.FXUtils;
import tool.function.FunctionUtils;

public class ButtonBox extends HBox {
	public final IndexRadioButton[] buttons = new IndexRadioButton[] {
			new IndexRadioButton(0, "原始"),
			new IndexRadioButton(1, "进化"),
			new IndexRadioButton(2, "开花")
			/**/ };
	public final CheckBox autoUpdateStand = new CheckBox();
	public final Button showStand = new Button("立绘");

	protected CharacterData getSelectedChara() {
		return java.util.Arrays.stream(this.buttons).filter(RadioButton::isSelected).map(irb -> irb.chara).findFirst().orElse(null);
	}

	protected void updateButtonState(CharacterData chara) {
		if (java.util.Arrays.stream(this.buttons).noneMatch(button -> button.chara == chara)) {
			this.buttons[0].chara = CharacterData.ALL_CHARA.get(chara.getIDS()[0]);
			this.buttons[1].chara = CharacterData.ALL_CHARA.get(chara.getIDS()[1]);
			this.buttons[2].chara = CharacterData.ALL_CHARA.get(chara.getIDS()[2]);
			FunctionUtils.forEach(this.buttons, button -> {
				button.setDisable(button.chara == null);
			});
		}
		FunctionUtils.forEach(this.buttons, button -> {
			button.setSelected(button.chara == chara);
		});
	}

	ButtonBox(RightShowCharaPane rightShowCharaPane) {
		this.setSpacing(5);
		this.setAlignment(Pos.CENTER);
		this.getChildren().addAll(this.buttons);
		this.getChildren().addAll(FXUtils.createHBox(0, Pos.CENTER_LEFT, true, this.autoUpdateStand, this.showStand));

		this.showStand.setOnAction(ev -> {
			rightShowCharaPane.showStandWindow.update(this.getSelectedChara(), true);
		});
		this.autoUpdateStand.setSelected(true);
		this.autoUpdateStand.setOnAction(ev -> {
			if (this.autoUpdateStand.isSelected()) {
				rightShowCharaPane.showStandWindow.update(this.getSelectedChara(), false);
			}
		});

		ToggleGroup group = new ToggleGroup();
		FunctionUtils.forEach(this.buttons, button -> {
			button.setDisable(true);
			button.setSelected(false);
			button.setToggleGroup(group);
			button.setOnAction(ev -> {
				rightShowCharaPane.imageView.setImage(button.chara.getStandS());
				rightShowCharaPane.skillBox.updateSkill(button.chara);
				if (this.autoUpdateStand.isSelected()) {
					rightShowCharaPane.showStandWindow.update(this.getSelectedChara(), false);
				}
			});
		});
	}

	public class IndexRadioButton extends RadioButton {
		public CharacterData chara = null;
		public final int index;

		public IndexRadioButton(int index, String text) {
			this.index = index;
			this.setText(text);
		}
	}
}
