package show;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;

@SuppressWarnings("serial")
public class ShowOther extends JPanel {
	private JWindow window;
	private BufferedImage image;
	private int id;
	private double zoom = 1.0;

	public JWindow getWindow() {
		return this.window;
	}

	public ShowOther(String filedir, int width, int height) {
		this.window = new JWindow();
		this.window.setSize(width, height);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width - width) / 2;
		int y = (screenSize.height - height) / 2;
		this.window.setLocation(x, y);

		this.window.setAlwaysOnTop(true);
		this.window.setContentPane(this);
		this.window.setBackground(new Color(0, 0, 0, 0f));
		DragWindowMoveListener ma = new DragWindowMoveListener(this.window);
		this.window.addMouseListener(ma);
		this.window.addMouseMotionListener(ma);

		this.window.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				ShowOther.this.zoom += e.getWheelRotation() < 0 ? 0.1 : (-0.1);
				if (ShowOther.this.zoom <= 0.5) ShowOther.this.zoom = 0.5;
				ShowOther.this.repaint();
			}
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
						} catch (IOException e) {
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
		int width = (int) (this.zoom * this.image.getWidth());
		int height = (int) (this.zoom * this.image.getHeight());
		BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		temp.createGraphics().drawImage(this.image, 0, 0, width, height, null);
		if (this.image != null) {
			int x = (this.window.getWidth() - temp.getWidth()) / 2;
			int y = (this.window.getHeight() - temp.getHeight()) / 2;
			System.out.println(this.window.getLocation());
			System.out.println(new Point(x, y));
			g.drawImage(temp, x, y, this);
		}
	}
}
