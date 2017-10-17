package fkg.gui.jfx;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.stream.IntStream;

import javax.swing.JPanel;
import javax.swing.JWindow;

import org.apache.commons.lang3.ArrayUtils;

import fkg.gui.jfx.CenterShowListPane.CharacterData;
import fkg.gui.jfx.RightShowCharaPane.ButtonBox.IndexRadioButton;
import fkg.masterdata.CharacterLeaderSkillDescription;
import fkg.masterdata.CharacterSkill;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tool.function.FunctionUtils;

public class RightShowCharaPane extends VBox {
	private int showingChara = -1;
	public final CharacterData[] charas = new CharacterData[3];
	public final ShowStandWindow showStandWindow = new ShowStandWindow(960, 640);

	public final ButtonBox buttonBox = new ButtonBox();
	public final ImageView imageView = new ImageView(new WritableImage(329, 467));
	public final InformationBox infoBox = new InformationBox();

	public RightShowCharaPane(ApplicationMain main) {
		this.setSpacing(4);
		this.setMinWidth(this.imageView.getImage().getWidth());
		this.setMaxWidth(this.imageView.getImage().getWidth());

		this.getChildren().add(this.buttonBox);
		this.getChildren().add(this.imageView);
		this.getChildren().add(this.infoBox);

		FunctionUtils.forEach(this.buttonBox.buttons, button -> {
			button.setDisable(true);
			button.setSelected(false);
			button.setToggleGroup(this.buttonBox.group);
		});
		this.buttonBox.group.selectedToggleProperty().addListener((source, oldValue, newValue) -> {
			if (newValue != oldValue) {
				this.showCharacter(((IndexRadioButton) newValue).index);
			}
		});
		this.buttonBox.lihui.setOnAction(ev -> {
			this.showStandWindow.display(this.charas[this.showingChara].getStand());
		});
	}

	public void showCharacter(CharacterData chara) {
		if (chara == null) {
			return;
		}
		int oeb = chara.getOeb();

		if (Arrays.stream(this.charas).noneMatch(obj -> obj == chara)) {
			this.showingChara = -1;
			int id = chara.getId() - (oeb == 2 ? 1 : (oeb == 3 ? 300000 : 0));

			this.charas[0] = CharacterData.ALL_CHARA.get(id);
			this.charas[1] = CharacterData.ALL_CHARA.get(id + 1);
			this.charas[2] = CharacterData.ALL_CHARA.get(id + 300000);
		}

		this.showCharacter(oeb - 1);
	}

	public void showCharacter(int index) {
		if (this.showingChara == index) {
			return;
		} else {
			this.showingChara = index;
		}

		CharacterData chara = this.charas[this.showingChara];
		//刷新button状态
		this.buttonBox.buttons[this.showingChara].setSelected(true);
		FunctionUtils.forEach(this.buttonBox.buttons, button -> {
			button.setDisable(this.charas[button.index] == null);
		});
		//显示花骑士
		this.imageView.setImage(this.charas[this.showingChara].getStandS());
		//技能刷新
		CharacterSkill cs = chara.getMainSkill();
		this.infoBox.mainSkill.setText(cs == null ? "" : (cs.name + "\n" + cs.effect));
		CharacterLeaderSkillDescription clsd = chara.getSkill();
		if (clsd != null) {
			FunctionUtils.forEach(this.infoBox.skillTexts, clsd.skills, (skillText, skill) -> {
				skillText.setText(skill.getString());
			});
		} else {
			FunctionUtils.forEach(this.infoBox.skillTexts, skillText -> {
				skillText.setText("");
			});
		}
	}

	public class ButtonBox extends HBox {
		public final ToggleGroup group = new ToggleGroup();
		public final IndexRadioButton[] buttons = new IndexRadioButton[] {
				new IndexRadioButton(0, "原始"),
				new IndexRadioButton(1, "进化"),
				new IndexRadioButton(2, "开花")
				/**/ };
		public final Button lihui = new Button("立绘");

		public ButtonBox() {
			this.setSpacing(5);
			this.setAlignment(Pos.CENTER);
			this.getChildren().addAll(this.buttons);
			this.getChildren().addAll(this.lihui);
		}

		public class IndexRadioButton extends RadioButton {
			public final int index;

			public IndexRadioButton(int index, String text) {
				this.index = index;
				this.setText(text);
			}
		}
	}

	public class InformationBox extends VBox {
		public final TextArea mainSkill = this.createTextArea(53);
		public final TextArea[] skillTexts;

		public InformationBox() {
			this.skillTexts = IntStream.range(0, 4).mapToObj(index -> {
				return this.createTextArea(68);
			}).toArray(TextArea[]::new);

			this.setSpacing(4);
			this.setAlignment(Pos.TOP_LEFT);
			this.getChildren().addAll(new VBox(new Label("    技能 : "), this.mainSkill));
			this.getChildren().addAll(new VBox(ArrayUtils.insert(1, new Node[] { new Label("    能力 : ") }, this.skillTexts)));
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

	public class ShowStandWindow {
		public final JWindow window;
		public final PicturePanel picturePanel = new PicturePanel();;

		public ShowStandWindow(int width, int height) {
			this.window = new JWindow();
			this.window.setSize(width, height);
			this.window.setLocationRelativeTo(null);
			this.window.setAlwaysOnTop(true);
			this.window.setContentPane(this.picturePanel);
			this.window.setBackground(new Color(0, 0, 0, 0f));
			DragWindowMoveListener ma = new DragWindowMoveListener(this.window);
			this.window.addMouseListener(ma);
			this.window.addMouseMotionListener(ma);
		}

		public void display(javafx.scene.image.Image image) {
			this.picturePanel.reflash(SwingFXUtils.fromFXImage(image, new BufferedImage((int) image.getWidth(), (int) image.getHeight(), BufferedImage.TYPE_INT_ARGB)));
			this.window.setVisible(true);
		}

		@SuppressWarnings("serial")
		private class PicturePanel extends JPanel {
			private Image image;

			public void reflash(Image image) {
				this.image = image;
				this.repaint();
			}

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				if (this.image != null) {
					g.drawImage(this.image, 0, 0, this);
				}
			}
		}

		public class DragWindowMoveListener extends MouseAdapter {
			private JWindow window;
			private java.awt.Point oldLocation;
			private java.awt.Point newLocation;
			private boolean closeWindow = false;
			private boolean allowDrag = false;

			public DragWindowMoveListener(JWindow window) {
				this.window = window;
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					this.allowDrag = true;
					this.closeWindow = true;
					this.oldLocation = e.getPoint();
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (this.allowDrag) {
					this.closeWindow = false;
					this.newLocation = e.getPoint();
					this.window.setLocation(//
							(int) (this.window.getLocation().getX() + this.newLocation.getX() - this.oldLocation.getX()), //
							(int) (this.window.getLocation().getY() + this.newLocation.getY() - this.oldLocation.getY())//
					);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && this.closeWindow) {
					this.window.setVisible(false);
				}

				this.closeWindow = false;
				this.allowDrag = false;
			}
		}
	}
}
