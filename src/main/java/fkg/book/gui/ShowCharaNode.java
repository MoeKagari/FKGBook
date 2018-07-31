package fkg.book.gui;

import com.moekagari.tool.acs.ArrayUtils;
import com.moekagari.tool.lambda.LambdaUtils;
import com.moekagari.tool.other.FXUtils;
import fkg.book.masterdata.CharacterSkill;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.util.Optional;
import java.util.function.IntFunction;

public class ShowCharaNode extends VBox {
	final ShowStandWindow showStandWindow = new ShowStandWindow(960, 640);

	final ButtonBox buttonBox;
	final ImageView imageView;
	final SkillBox skillBox;

	ShowCharaNode() {
		this.buttonBox = new ButtonBox();
		this.imageView = new ImageView(new WritableImage(329, 467));
		this.skillBox = new SkillBox();

		this.setSpacing(4);
		this.setMinWidth(this.imageView.getImage().getWidth());
		this.setMaxWidth(this.imageView.getImage().getWidth());
		this.getChildren().addAll(this.buttonBox, this.imageView, this.skillBox);
	}

	void showCharacter(AbstractCharaData chara) {
		if(chara == null||chara == this.buttonBox.getSelectedChara()) {
			return;
		}
		this.buttonBox.updateButtonState(chara);
		this.showStandWindow.update(this.buttonBox.getSelectedChara());
		this.imageView.setImage(chara.getStandS());
		this.skillBox.updateSkill(chara);
	}

	class ButtonBox extends HBox {
		final IndexRadioButton[] buttons = new IndexRadioButton[]{
				new IndexRadioButton(0, "原始"),
				new IndexRadioButton(1, "进化"),
				new IndexRadioButton(2, "开花"),
				new IndexRadioButton(3, "升华")
		};
		final Button showStand = new Button("立绘");

		ButtonBox() {
			this.setSpacing(5);
			this.setAlignment(Pos.CENTER);
			this.getChildren().addAll(this.buttons);
			this.getChildren().addAll(this.showStand);

			this.showStand.setOnAction(ev -> {
				ShowCharaNode.this.showStandWindow.window.setVisible(true);
			});

			ToggleGroup group = new ToggleGroup();
			ArrayUtils.forEach(this.buttons, button -> {
				button.setDisable(true);
				button.setSelected(false);
				button.setToggleGroup(group);
				button.setOnAction(ev -> {
					ShowCharaNode.this.imageView.setImage(button.chara.getStandS());
					ShowCharaNode.this.skillBox.updateSkill(button.chara);
					ShowCharaNode.this.showStandWindow.update(this.getSelectedChara());
				});
			});
		}

		AbstractCharaData getSelectedChara() {
			return java.util.Arrays.stream(this.buttons).filter(RadioButton::isSelected).map(irb -> irb.chara).findFirst().orElse(null);
		}

		void updateButtonState(AbstractCharaData chara) {
			if(java.util.Arrays.stream(this.buttons).noneMatch(button -> button.chara == chara)) {
				int rarity = chara.getRarity();
				this.buttons[0].chara = chara.getAllChara()[0];
				this.buttons[1].chara = chara.getAllChara()[1];
				this.buttons[2].chara = chara.getAllChara()[2];
				this.buttons[3].chara = chara.getAllChara()[3];
				ArrayUtils.forEach(this.buttons, button -> {
					button.setDisable(button.chara == null);
					button.setText(Optional.ofNullable(button.chara).map(AbstractCharaData::getStateString).orElseGet(() -> {
						switch(button.index) {
							case 0:
								return "原始";
							case 1:
								return "进化";
							case 2:
								return rarity <= 4 ? "升华(进化)" : "开花";
							case 3:
								return "升华(开花)";
						}
						throw new RuntimeException("不可能的index");
					}));
				});
			}
			ArrayUtils.forEach(this.buttons, button -> {
				button.setSelected(button.chara == chara);
			});
		}

		class IndexRadioButton extends RadioButton {
			AbstractCharaData chara = null;
			final int index;

			IndexRadioButton(int index, String text) {
				super(text);
				this.index = index;
			}
		}
	}

	class ShowStandWindow {
		java.awt.Image image;
		final JWindow window = new JWindow();

		void update(AbstractCharaData chara) {
			if(chara == null) {
				return;
			}
			this.image = SwingFXUtils.fromFXImage(chara.getStand(), null);
			this.window.getContentPane().repaint();
		}

		ShowStandWindow(int width, int height) {
			this.window.setSize(width, height);
			this.window.setLocationRelativeTo(null);
			this.window.setAlwaysOnTop(true);
			this.window.setContentPane(new JPanel() {
				@Override
				public void paint(java.awt.Graphics g) {
					super.paint(g);
					if(ShowStandWindow.this.image != null) {
						g.drawImage(ShowStandWindow.this.image, 0, 0, this);
					}
				}
			});
			this.window.setBackground(new java.awt.Color(0, 0, 0, 0));
			LambdaUtils.init(new java.awt.event.MouseAdapter() {
				boolean closeWindow = false;
				boolean allowDrag = false;
				java.awt.Point oldLocation;

				@Override
				public void mousePressed(java.awt.event.MouseEvent e) {
					if(e.getButton() == java.awt.event.MouseEvent.BUTTON1) {
						this.allowDrag = true;
						this.closeWindow = true;
						this.oldLocation = e.getPoint();
					}
				}

				@Override
				public void mouseDragged(java.awt.event.MouseEvent e) {
					if(this.allowDrag) {
						java.awt.Point newLocation = e.getPoint();
						ShowStandWindow.this.window.setLocation(//
						                                        (int) (ShowStandWindow.this.window.getLocation().getX() + newLocation.getX() - this.oldLocation.getX()), //
						                                        (int) (ShowStandWindow.this.window.getLocation().getY() + newLocation.getY() - this.oldLocation.getY())//
						);

						this.closeWindow = false;
					}
				}

				@Override
				public void mouseReleased(java.awt.event.MouseEvent e) {
					if(e.getButton() == java.awt.event.MouseEvent.BUTTON1&&this.closeWindow) {
						ShowStandWindow.this.window.setVisible(false);
					}

					this.oldLocation = null;
					this.closeWindow = false;
					this.allowDrag = false;
				}
			}, this.window::addMouseListener, this.window::addMouseMotionListener);
		}
	}

	class SkillBox extends VBox {
		final TextArea skillText;
		final TextArea[] abilityTexts;

		SkillBox() {
			IntFunction<TextArea> createTextArea = height -> {
				TextArea text = new TextArea();
				text.setEditable(false);
				text.setWrapText(true);
				text.setMinHeight(height);
				text.setMaxHeight(height);
				return text;
			};
			this.skillText = createTextArea.apply(53);
			this.abilityTexts = new TextArea[]{createTextArea.apply(68), createTextArea.apply(68), createTextArea.apply(68), createTextArea.apply(68)};

			this.setSpacing(4);
			this.getChildren().addAll(
					FXUtils.createVBox(0, null, false, box -> {
						box.getChildren().add(FXUtils.createHBox(0, Pos.CENTER, false, new Label("技能")));
						box.getChildren().addAll(this.skillText);
					}),
					FXUtils.createVBox(0, null, false, box -> {
						box.getChildren().add(FXUtils.createHBox(0, Pos.CENTER, false, new Label("能力")));
						box.getChildren().addAll(this.abilityTexts);
					})
					/**/
			);
		}

		void updateSkill(AbstractCharaData chara) {
			//技能
			CharacterSkill skill = chara.getSkill();
			this.skillText.setText(skill.name + "\n" + skill.effect);

			//能力
			for(int index = 0; index < 4; index++) {
				this.abilityTexts[index].setText(chara.getAbility().abilityArray[index].getDescription());
			}
		}
	}
}
