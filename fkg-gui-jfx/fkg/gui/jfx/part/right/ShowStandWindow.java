package fkg.gui.jfx.part.right;

import javax.swing.JPanel;
import javax.swing.JWindow;

import fkg.gui.jfx.other.CharacterData;
import javafx.embed.swing.SwingFXUtils;
import tool.function.FunctionUtils;

public class ShowStandWindow {
	private java.awt.Image image;
	private CharacterData showingChara = null;

	public final JWindow window = new JWindow();
	@SuppressWarnings("serial")
	private final JPanel showStandPanel = new JPanel() {
		@Override
		public void paint(java.awt.Graphics g) {
			super.paint(g);
			if (ShowStandWindow.this.image != null) {
				g.drawImage(ShowStandWindow.this.image, 0, 0, this);
			}
		}
	};

	protected void update(CharacterData chara, boolean show) {
		if (this.showingChara != chara) {
			this.image = SwingFXUtils.fromFXImage(chara.getStand(), null);
			this.showStandPanel.repaint();
			this.showingChara = chara;
		}

		if (show) {
			this.window.setVisible(true);
		}
	}

	ShowStandWindow(int width, int height) {
		this.window.setSize(width, height);
		this.window.setLocationRelativeTo(null);
		this.window.setAlwaysOnTop(true);
		this.window.setContentPane(this.showStandPanel);
		this.window.setBackground(new java.awt.Color(0, 0, 0, 0));
		FunctionUtils.use(new DragWindowMoveListener(), this.window::addMouseListener, this.window::addMouseMotionListener);
	}

	private class DragWindowMoveListener extends java.awt.event.MouseAdapter {
		private boolean closeWindow = false;
		private boolean allowDrag = false;
		private java.awt.Point oldLocation;

		@Override
		public void mousePressed(java.awt.event.MouseEvent e) {
			if (e.getButton() == java.awt.event.MouseEvent.BUTTON1) {
				this.allowDrag = true;
				this.closeWindow = true;
				this.oldLocation = e.getPoint();
			}
		}

		@Override
		public void mouseDragged(java.awt.event.MouseEvent e) {
			if (this.allowDrag) {
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
			if (e.getButton() == java.awt.event.MouseEvent.BUTTON1 && this.closeWindow) {
				ShowStandWindow.this.window.setVisible(false);
			}

			this.oldLocation = null;
			this.closeWindow = false;
			this.allowDrag = false;
		}
	}
}
