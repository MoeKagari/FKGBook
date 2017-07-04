package patch.other;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import org.apache.commons.io.FileUtils;

import show.data.CharacterData;

public class ValentineCardLetter {
	public static void create() {
		String name;
		int flag;
		do {
			name = JOptionPane.showInputDialog("请输入团长名");
			if (name == null || "".equals(name)) return;
			flag = JOptionPane.showConfirmDialog(null, "确认团长名为:\n" + name, "确认团长名", JOptionPane.YES_NO_OPTION);
			if (flag == JOptionPane.CLOSED_OPTION) return;
		} while (flag != JOptionPane.OK_OPTION);

		JFrame frame = new JFrame("ValentineCardMaker");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setAlwaysOnTop(true);
		frame.setSize(400, 100);
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				over = true;
			}
		});
		JProgressBar bar = new JProgressBar(0, 640);
		frame.add(bar, BorderLayout.CENTER);
		bar.setStringPainted(true);
		frame.setVisible(true);

		Thread thread = new Thread(() -> {
			while (true) {
				bar.setValue(count);
				bar.setString(count + "/" + bar.getMaximum());
				if (over) break;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("完毕");
			bar.setString("完毕");
		});
		thread.setDaemon(true);
		thread.start();

		try {
			combineImage(name);
		} catch (Exception e) {
			over = true;
			JOptionPane.showMessageDialog(frame, e, "发生错误", JOptionPane.ERROR_MESSAGE);
		}
	}

	private static int count = 0;
	private static boolean over = false;

	private static void combineImage(String name) throws Exception {
		BufferedImage textWindow = ImageIO.read(new File("resources\\character_image\\valentine\\other\\text_window.png"));
		//http://dugrqaqinbtcq.cloudfront.net/product/images/mypage/letter/background/valentinecard_2.png
		//http://dugrqaqinbtcq.cloudfront.net/product/images/mypage/letter/accent/valentineaccent_1.png
		//http://dugrqaqinbtcq.cloudfront.net/product/images/mypage/letter/text_window.png
		//System.out.println(MD5.getMD5("bustup_131926_02"));

		Map<Integer, MasterLetter> mls = new HashMap<>();
		String[] sources = new String(FileUtils.readFileToByteArray(new File("resources\\character_image\\valentine\\other\\masterLetter.csv")), "utf-8").trim().split("\n");
		for (String source : sources) {
			MasterLetter ml = new MasterLetter(source);
			mls.put(ml.id, ml);
		}

		for (CharacterData cd : CharacterData.get().values()) {
			if (over) break;

			int id = cd.id;
			MasterLetter ml = null;
			switch (cd.oeb) {
				case 1:
					ml = mls.get(id);
					break;
				case 2:
					ml = mls.get(id - 1);
					break;
				case 3:
					ml = mls.get(id - 300000);
					break;
			}
			if (ml != null) {
				combineImage(id, ml, textWindow, name);
			}
		}
		over = true;
	}

	private static void combineImage(int id, MasterLetter ml, BufferedImage textWindow, String name) {
		BufferedImage bustup = getImage("resources\\character_image\\valentine\\bustup\\" + id + ".png");
		if (bustup == null) {
			return;
		}
		BufferedImage background = getImage("resources\\character_image\\valentine\\" + ml.background.replace('/', '\\'));
		if (background == null) {
			background = new BufferedImage(960, 640, BufferedImage.TYPE_INT_ARGB);
		}
		BufferedImage accent = getImage("resources\\character_image\\valentine\\" + ml.accent.replace('/', '\\'));
		if (accent == null) {
			accent = new BufferedImage(960, 640, BufferedImage.TYPE_INT_ARGB);
		}

		BufferedImage image = combineImage(getTextWindow(ml.text, textWindow, name), background, accent, bustup);
		try {
			File file = new File("resources\\character_image\\valentine\\card\\" + id + ".png");
			file.getParentFile().mkdirs();
			ImageIO.write(image, "png", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		count++;
	}

	private static BufferedImage getTextWindow(String text, BufferedImage textWindow, String name) {
		BufferedImage image = new BufferedImage(textWindow.getWidth(), textWindow.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setFont(new Font("MigMix 2M", Font.BOLD, 19));
		g2d.setColor(new Color(103, 52, 1));

		g2d.drawImage(textWindow, 0, 0, textWindow.getWidth(), textWindow.getHeight(), null);
		FontMetrics fm = g2d.getFontMetrics();
		int x = 165, y = 0 + fm.getHeight();
		if (text.startsWith("\"") && text.endsWith("\"")) {
			text = text.substring(1, 1 + text.length() - 2);
		}
		text = text.replaceAll("%user%", name);
		String[] words = text.split("<br>");
		{
			g2d.drawString(words[0], x, y);
			y += 27;
		}
		for (int i = 1; i < words.length - 1; i++) {
			g2d.drawString(words[i], x, y);
			y += 25;
		}
		{
			String word = words[words.length - 1];
			x = 795 - fm.stringWidth(word);
			g2d.drawString(word, x, y);
		}

		return image;
	}

	private static BufferedImage combineImage(BufferedImage text, BufferedImage background, BufferedImage accent, BufferedImage bustup) {
		BufferedImage image = new BufferedImage(960, 640, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();

		g2d.drawImage(background, 0, 0, background.getWidth(), background.getHeight(), null);
		g2d.drawImage(bustup, 0, 0, bustup.getWidth(), bustup.getHeight(), null);
		g2d.drawImage(text, 0, 460, text.getWidth(), text.getHeight(), null);
		g2d.drawImage(accent, 0, 0, accent.getWidth(), accent.getHeight(), null);

		return image;
	}

	private static BufferedImage getImage(String filepath) {
		try {
			return ImageIO.read(new File(filepath));
		} catch (IOException e) {
			return null;
		}
	}

	static class MasterLetter {
		String a, b;
		private int id;
		private String text;
		int bustup_no;
		private String background;
		private String accent;

		public MasterLetter(String source) {
			String[] datas = source.trim().split(",");

			int index = 0;
			this.a = datas[index++];
			this.b = datas[index++];
			this.id = Integer.parseInt(datas[index++]);
			this.text = datas[index++];
			this.bustup_no = Integer.parseInt(datas[index++]);
			this.background = datas[index++];
			this.accent = datas[index++];
		}

		public String getBackground() {
			return "http://dugrqaqinbtcq.cloudfront.net/product/images/mypage/letter/" + this.background;
		}

		public String getAccent() {
			return "http://dugrqaqinbtcq.cloudfront.net/product/images/mypage/letter/" + this.accent;
		}
	}
}
