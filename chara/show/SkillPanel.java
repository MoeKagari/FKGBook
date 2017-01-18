package show;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JWindow;

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
	private ShowStand ss;
	private ShowPanel sp;

	public SkillPanel() {
		this.ss = new ShowStand();
		this.sp = new ShowPanel();
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(451, 707));
		/*-----------------------------------------------------------*/
		JPanel north = new JPanel();
		ButtonGroup bg = new ButtonGroup();

		for (int i = 0; i < this.jrb_oeb.length; i++) {
			this.jrb_oeb[i] = new JRadioButton(this.string_oeb[i]);
			this.jrb_oeb[i].setFocusPainted(false);
			this.jrb_oeb[i].setEnabled(false);
			int mode = i;
			this.jrb_oeb[i].addActionListener(ev -> this.reflash(mode + 1));
			bg.add(this.jrb_oeb[i]);
			north.add(this.jrb_oeb[i]);
		}

		JButton lihui = new JButton("立绘");
		lihui.setFocusPainted(false);
		lihui.addActionListener((ev) -> this.ss.display(this.cds[IndexFinder.find(this.jrb_oeb, jrb -> jrb.isSelected())].getStand()));
		north.add(lihui);

		this.add(north, BorderLayout.NORTH);
		this.add(this.sp, BorderLayout.CENTER);
	}

	private void reflash(int mode) {
		this.jrb_oeb[mode - 1].setSelected(true);
		this.image = this.cds[mode - 1].getSkillImage();
		this.sp.repaint();
	}

	public void showCharacter(final CharacterData cd) {
		final int id = cd.getId();
		final int oeb = cd.getOEB();

		switch (oeb) {
			case 1:
				this.cds[0] = cd;
				this.cds[1] = CharacterData.get().get(id + 1);
				this.cds[2] = CharacterData.get().get(id + 300000);
				break;
			case 2:
				this.cds[0] = CharacterData.get().get(id - 1);
				this.cds[1] = cd;
				this.cds[2] = CharacterData.get().get(id - 1 + 300000);
				break;
			case 3:
				this.cds[0] = CharacterData.get().get(id - 300000);
				this.cds[1] = CharacterData.get().get(id + 1 - 300000);
				this.cds[2] = cd;
				break;
			default:
				return;
		}

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

	private class ShowStand extends JPanel {
		private JWindow window;
		private BufferedImage image;

		public ShowStand() {
			this.window = new JWindow();
			this.window.setSize(960, 640);
			this.window.setLocationRelativeTo(null);
			this.window.setAlwaysOnTop(true);
			this.window.setContentPane(this);
			this.window.setBackground(new Color(0, 0, 0, 0f));
			MouseAdapter ma = new MouseAdapter() {
				private Point oldLocation;
				private Point newLocation;
				private boolean closeWindow = false;
				private boolean allowDrag = false;

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
						ShowStand.this.window.setLocation((int) (ShowStand.this.window.getLocation().getX() + this.newLocation.getX() - this.oldLocation.getX()),
								(int) (ShowStand.this.window.getLocation().getY() + this.newLocation.getY() - this.oldLocation.getY()));
					}
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1 && this.closeWindow) ShowStand.this.window.setVisible(false);

					this.closeWindow = false;
					this.allowDrag = false;
				}

			};
			this.window.addMouseListener(ma);
			this.window.addMouseMotionListener(ma);
		}

		public void display(BufferedImage image) {
			this.image = image;
			this.repaint();
			this.window.setVisible(true);
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			if (this.image != null) {
				g.drawImage(this.image, 0, 0, this);
			}
		}
	}

}
