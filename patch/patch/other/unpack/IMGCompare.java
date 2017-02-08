package patch.other.unpack;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * 比较两张图片的相似度
 */
class IMGCompare {
	final static double prec = 10 * 10 * 10;
	final static int bei = 1;
	final static double limit = 0.8;

	private static String[][] getPX(File file) {
		try {
			BufferedImage bi = ImageIO.read(file);

			int width = bi.getWidth();
			int height = bi.getHeight();
			int minx = bi.getMinX();
			int miny = bi.getMinY();

			String[][] list = new String[height / bei][width / bei];
			for (int i = miny; i < height / bei; i++) {
				for (int j = minx; j < width / bei; j++) {
					int pixel = bi.getRGB(j * bei, i * bei);
					list[i][j] = pixel + "";
				}
			}

			return list;
		} catch (Exception e) {
			return null;
		}
	}

	private static int[] pixelToRGB(String pixel) {
		int[] rgb = new int[3];

		int pi = Integer.parseInt(pixel);
		rgb[0] = (pi & 0xff0000) >> 16;
		rgb[1] = (pi & 0xff00) >> 8;
		rgb[2] = (pi & 0xff);

		return rgb;
	}

	private static double compareImage(String[][] list1, String[][] list2) {
		int xiangsi = 0;
		int busi = 0;

		for (int i = 0; i < list1.length; i++) {
			for (int j = 0; j < list1[i].length; j++) {

				int[] value1 = pixelToRGB(list1[i][j]);
				int[] value2 = pixelToRGB(list2[i][j]);

				int u = value1[0] - value2[0];
				int v = value1[1] - value2[1];
				int w = value1[2] - value2[2];

				double d = u * u + v * v + w * w;
				if (d <= prec)
					xiangsi++;
				else
					busi++;

			}
		}

		return (1.0 * xiangsi) / (xiangsi + busi);
	}

	public static void compare(File file, String str) {
		int count = 0;
		File[] f = file.listFiles();

		for (int i = 0; i < f.length; i++) {

			if (f[i] == null)
				continue;

			String[][] list = getPX(f[i]);
			if (list == null)
				continue;

			for (int j = 0; j < f.length; j++) {

				if (j == i || f[j] == null)
					continue;

				String[][] listtemp = getPX(f[j]);
				if (listtemp == null)
					continue;

				double d = compareImage(list, listtemp);

				if (d > limit) {
					String fi = file.getPath() + "\\#" + str + " " + count + ".1" + f[i].getName();
					String fj = file.getPath() + "\\#" + str + " " + count + ".2" + f[j].getName();

					f[i].renameTo(new File(fi));
					f[j].renameTo(new File(fj));

					f[i] = null;
					f[j] = null;

					count++;
					break;
				}

			}

		}
	}

}