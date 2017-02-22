package patch.other;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import tool.Downloader;
import tool.FileUtil;

public class DownloadComic {

	public static void main(String[] args) {
		// http://ct.webcomic-eb.com/viewer/EB/sasayuki_001/flowerknight_001/0001-0_jVE5/page3/x2/1.jpg
		String[] huashu = new String[] {
				//"0001-0_jVE5", "0002-0_QyH8", "0003-0_mY89", "0004-0_Wr8w", "0005-0_6Mch", "0006-0_9Vj3","0007-0_3NhB",
		};

		for (int shu = 0; shu < huashu.length; shu++) {
			for (int page = 1; page <= 25; page++) {
				BufferedImage[] images = new BufferedImage[6];
				for (int part = 1; part <= images.length; part++) {
					String urlStr = "http://ct.webcomic-eb.com/viewer/EB/sasayuki_001/flowerknight_001/" + huashu[shu] + "/page" + page + "/x2/" + part + ".jpg";
					images[part - 1] = getImage(urlStr);
				}
				compose("comic\\" + String.format("%02d", shu) + "\\" + String.format("%02d", page) + ".jpg", images);
			}
		}
	}

	private static void compose(String filename, BufferedImage[] images) {
		for (BufferedImage image : images) {
			if (image == null) return;
		}

		int width = images[0].getWidth() + images[1].getWidth();
		int height = images[0].getHeight() + images[2].getHeight() + images[4].getHeight();

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.createGraphics();

		int x = 0;
		int y = 0;
		g.drawImage(images[0], x, y, null);

		x = images[0].getWidth();
		y = 0;
		g.drawImage(images[1], x, y, null);

		x = 0;
		y = images[0].getHeight();
		g.drawImage(images[2], x, y, null);

		x = images[0].getWidth();
		y = images[0].getHeight();
		g.drawImage(images[3], x, y, null);

		x = 0;
		y = images[0].getHeight() + images[2].getHeight();
		g.drawImage(images[4], x, y, null);

		x = images[0].getWidth();
		y = images[0].getHeight() + images[2].getHeight();
		g.drawImage(images[5], x, y, null);

		try {
			ImageIO.write(image, "jpg", FileUtil.create(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static BufferedImage getImage(String urlStr) {
		byte[] bytes = Downloader.download(urlStr);
		if (bytes == null) {
			System.out.println("bytes == null");
			return null;
		}

		try {
			return ImageIO.read(new ByteArrayInputStream(bytes));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
