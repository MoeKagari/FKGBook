package fkg.gui.jfx.part.right;

import fkg.gui.jfx.ApplicationMain;
import fkg.gui.jfx.other.CharacterData;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;

public class RightShowCharaPane extends VBox {
	public final ShowStandWindow showStandWindow = new ShowStandWindow(960, 640);

	public final ButtonBox buttonBox = new ButtonBox(this);
	public final ImageView imageView = new ImageView(new WritableImage(329, 467));
	public final SkillBox skillBox = new SkillBox();

	public RightShowCharaPane(ApplicationMain main) {
		this.setSpacing(4);
		this.setMinWidth(this.imageView.getImage().getWidth());
		this.setMaxWidth(this.imageView.getImage().getWidth());
		this.getChildren().addAll(this.buttonBox, this.imageView, this.skillBox);
	}

	/* 仅 center pane 用 */
	public void showCharacter(CharacterData chara) {
		if (chara == null || chara == this.buttonBox.getSelectedChara()) return;
		this.buttonBox.updateButtonState(chara);
		if (this.buttonBox.autoUpdateStand.isSelected()) {
			this.showStandWindow.update(this.buttonBox.getSelectedChara(), false);
		}
		this.imageView.setImage(chara.getStandS());
		this.skillBox.updateSkill(chara);
	}
}
