package show;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;

@SuppressWarnings("serial")
public class ShowOther extends JPanel {
	private JWindow window;
	private BufferedImage image;
	private int id;
	private int zoom = 10;

	public JWindow getWindow() {
		return this.window;
	}

	public ShowOther(boolean canZoom, String filedir, int width, int height) {
		this.window = new JWindow();
		this.window.setSize(width, height);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.window.setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2);

		this.window.setAlwaysOnTop(true);
		this.window.setContentPane(this);
		this.window.setBackground(new Color(0, 0, 0, 0f));
		DragWindowMoveListener ma = new DragWindowMoveListener(this.window);
		this.window.addMouseListener(ma);
		this.window.addMouseMotionListener(ma);

		if (canZoom) this.window.addMouseWheelListener(ev -> {
			this.zoom += ev.getWheelRotation() < 0 ? 1 : (-1);
			if (this.zoom <= 5) this.zoom = 5;
			this.repaint();
		});

		if (filedir != null) this.window.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				if (ev.getButton() == MouseEvent.BUTTON3) {
					ShowOther.this.window.setAlwaysOnTop(false);
					int flag = JOptionPane.showConfirmDialog(ShowOther.this.window, "是否打开图片所在位置？", "打开图片所在位置", JOptionPane.YES_NO_OPTION);
					ShowOther.this.window.setAlwaysOnTop(true);
					if (flag == JOptionPane.OK_OPTION) {
						try {
							Runtime.getRuntime().exec("explorer /e,/select ," + filedir + "\\" + ShowOther.this.id + ".png");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}

	public void display(int id, BufferedImage image) {
		this.id = id;
		this.image = image;
		this.repaint();
		this.window.setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		if (this.zoom == 10) {
			if (this.image != null) {
				g.drawImage(this.image, 0, 0, this);
			}
			return;
		}

		int width = this.zoom * this.image.getWidth() / 10;
		int height = this.zoom * this.image.getHeight() / 10;
		BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		temp.createGraphics().drawImage(this.image, 0, 0, null);
		if (this.image != null) {
			int x = (this.window.getWidth() - temp.getWidth()) / 2;
			int y = (this.window.getHeight() - temp.getHeight()) / 2;
			g.drawImage(temp, x, y, this);
		}
	}
}
