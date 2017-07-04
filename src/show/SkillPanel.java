package show;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import show.config.ShowConfig;
import show.data.CharacterData;
import tool.IndexFinder;

/**
 * 右侧显示skill
 */
@SuppressWarnings("serial")
public class SkillPanel extends JPanel {
	private final CharacterData[] cds = new CharacterData[3];
	private final JRadioButton[] jrb_oeb = new JRadioButton[3];
	private final String[] string_oeb = { "原始", "进化", "开花" };

	private BufferedImage image;
	private JButton lihui;
	private ShowPanel sp;
	private ShowOther showStand;

	public SkillPanel() {
		this.sp = new ShowPanel();
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(451, 707));
		/*-----------------------------------------------------------*/
		JPanel north = new JPanel();
		ButtonGroup bg = new ButtonGroup();

		for (int i = 0; i < this.jrb_oeb.length; i++) {
			JRadioButton jrb = this.jrb_oeb[i] = new JRadioButton(this.string_oeb[i]);
			jrb.setFocusPainted(false);
			jrb.setEnabled(false);
			int index = i;
			jrb.addActionListener(ev -> this.reflash(index + 1));
			bg.add(jrb);
			north.add(jrb);
		}

		this.showStand = new ShowOther(false, ShowConfig.CHARACTER_STAND, 960, 640);
		this.lihui = new JButton("立绘");
		this.lihui.setFocusPainted(false);
		this.lihui.addActionListener(ev -> {
			int index = IndexFinder.find(this.jrb_oeb, JRadioButton::isSelected);
			if (index != -1) {
				CharacterData cd = this.cds[index];
				if (cd != null) {
					this.showStand.display(cd.ci.getID(), cd.getStand());
				}
			}
		});
		north.add(this.lihui);

		this.add(north, BorderLayout.NORTH);
		this.add(this.sp, BorderLayout.CENTER);
	}

	private void reflash(int oeb) {
		this.jrb_oeb[oeb - 1].setSelected(true);
		this.image = this.cds[oeb - 1].getSkillImage();
		this.sp.repaint();
	}

	public void dispose() {
		this.showStand.getWindow().dispose();
	}

	public void showCharacter(CharacterData cd) {
		if (cd == null) return;

		int oeb = cd.ci.getOeb();
		int id = cd.ci.getID() - (oeb == 2 ? 1 : (oeb == 3 ? 300000 : 0));
		this.cds[0] = CharacterData.get().get(id);
		this.cds[1] = CharacterData.get().get(id + 1);
		this.cds[2] = CharacterData.get().get(id + 300000);

		for (int i = 0; i < this.cds.length; i++) {
			this.jrb_oeb[i].setEnabled(this.cds[i] != null);
			this.jrb_oeb[i].setSelected(false);
		}

		this.reflash(oeb);
	}

	private class ShowPanel extends JPanel {
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			if (SkillPanel.this.image != null) {
				g.drawImage(SkillPanel.this.image, 0, 0, this);
			}
		}
	}
}
