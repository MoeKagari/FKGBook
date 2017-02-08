package desktop;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JWindow;

public class ShowDesktopCharacter {

	public static void main(String[] args) {
		new ShowDesktopCharacter(true, "resources\\desktop_images");
	}

	private JWindow window;
	private JPopupMenu menu = new JPopupMenu();
	private CardLayout card;
	private boolean exit = false;
	private final String dir;

	public ShowDesktopCharacter(boolean allLoad, String dirpath) {
		this.window = new JWindow();
		this.dir = dirpath;

		if (allLoad) {
			ArrayList<BufferedImage> images = new ArrayList<>();
			for (int index = 0; index <= 1000; index++) {
				BufferedImage image = getNewImage(this.dir + "\\" + index + ".png");
				if (image != null) images.add(image);
			}
			if (images.size() == 0) {
				JOptionPane.showMessageDialog(this.window, "文件夹内无png图像(文件名为 数字.png ,数字为1-1000,按顺序)", "", JOptionPane.WARNING_MESSAGE);
				return;
			}

			this.card = new CardLayout();
			this.window.getContentPane().setLayout(this.card);

			for (BufferedImage image : images) {
				JPanel panel = getNewPanel(image);
				if (panel != null) this.window.getContentPane().add(panel);
			}

			int width = images.stream().mapToInt(image -> image == null ? 0 : image.getWidth()).max().orElse(0);
			int height = images.stream().mapToInt(image -> image == null ? 0 : image.getHeight()).max().orElse(0);
			images.clear();
			this.window.setSize(width, height);
		} else {
			this.window.getContentPane().setLayout(new BorderLayout());
		}

		this.window.setLocationRelativeTo(null);
		this.window.setAlwaysOnTop(true);
		this.window.setBackground(new Color(0, 0, 0, 0));
		this.window.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					ShowDesktopCharacter.this.menu.setVisible(false);
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					ShowDesktopCharacter.this.menu.show(ShowDesktopCharacter.this.window, e.getX(), e.getY());
				}
			}
		});
		MouseAdapter ma = new MouseAdapter() {
			private Point oldLocation;
			private Point newLocation;
			private boolean allowDrag = false;

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					this.allowDrag = true;
					this.oldLocation = e.getPoint();
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (this.allowDrag) {
					this.newLocation = e.getPoint();
					JWindow win = ShowDesktopCharacter.this.window;
					ShowDesktopCharacter.this.window.setLocation(//
							(int) (win.getLocation().getX() + this.newLocation.getX() - this.oldLocation.getX()),//
							(int) (win.getLocation().getY() + this.newLocation.getY() - this.oldLocation.getY())//
					);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				this.allowDrag = false;
			}
		};
		this.window.addMouseListener(ma);
		this.window.addMouseMotionListener(ma);

		this.initPopmenu();
		this.window.setVisible(true);

		try {
			int index = 0;
			int total = new File(this.dir).list().length;
			while (this.exit == false) {
				if (allLoad) {
					this.card.next(this.window.getContentPane());
				} else {
					this.window.getContentPane().removeAll();
					JPanel panel = getNewPanel(getNewImage(this.dir + "\\" + index + ".png"));
					index = index == total ? 0 : (index + 1);
					if (panel == null) continue;
					this.window.getContentPane().add(panel, BorderLayout.CENTER);
					this.window.setSize(panel.getWidth(), panel.getHeight());
					this.window.repaint();
				}
				Thread.sleep(33);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initPopmenu() {
		JMenuItem ontop = new JMenuItem("总在前");
		ontop.addActionListener(ev -> this.window.setAlwaysOnTop(!this.window.isAlwaysOnTop()));
		this.menu.add(ontop);

		JMenuItem exit = new JMenuItem("退出");
		exit.addActionListener(ev -> {
			this.window.dispose();
			this.exit = true;
		});
		this.menu.add(exit);
	}

	private static BufferedImage getNewImage(String filepath) {
		File file = new File(filepath);
		if (file.exists() == false) return null;

		try {
			return ImageIO.read(file);
		} catch (IOException e) {
			return null;
		}
	}

	@SuppressWarnings("serial")
	private static JPanel getNewPanel(BufferedImage image) {
		if (image == null) return null;

		JPanel panel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.drawImage(image, 0, 0, this);
			}
		};
		panel.setSize(image.getWidth(), image.getHeight());
		panel.setBackground(new Color(0, 0, 0, 0));
		return panel;
	}

}
