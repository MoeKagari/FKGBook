package fkg.gui.jfx.part.right;

import org.apache.commons.lang3.ArrayUtils;

import fkg.gui.jfx.other.CharacterData;
import fkg.masterdata.CharacterLeaderSkillDescription;
import fkg.masterdata.CharacterSkill;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import tool.FXUtils;
import tool.function.FunctionUtils;

class SkillBox extends VBox {
	private final TextArea mainSkill = this.createTextArea(53);
	private final TextArea[] skillTexts = java.util.stream.IntStream.range(0, 4).mapToObj(index -> this.createTextArea(68)).toArray(TextArea[]::new);

	SkillBox() {
		this.setSpacing(4);
		this.getChildren().addAll(
				new VBox(FXUtils.createHBox(0, Pos.CENTER, false, new Label("技能")), this.mainSkill),
				new VBox(ArrayUtils.insert(1, new Node[] { FXUtils.createHBox(0, Pos.CENTER, false, new Label("能力")) }, this.skillTexts))
		/**/);
	}

	protected void updateSkill(CharacterData chara) {
		//技能
		CharacterSkill cs = chara.getMainSkill();
		this.mainSkill.setText(cs == null ? "" : (cs.name + "\n" + cs.effect));

		//能力
		CharacterLeaderSkillDescription clsd = chara.getSkill();
		if (clsd != null) {
			FunctionUtils.forEach(this.skillTexts, clsd.skills, (skillText, skill) -> skillText.setText(skill.getString()));
		} else {
			FunctionUtils.forEach(this.skillTexts, skillText -> skillText.setText(""));
		}
	}

	private TextArea createTextArea(int height) {
		TextArea text = new TextArea();
		text.setEditable(false);
		text.setWrapText(true);
		text.setMinHeight(height);
		text.setMaxHeight(height);
		return text;
	}
}
