package patch.other;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import patch.api.getMaster.CharacterInformation;
import patch.api.getMaster.CharacterLeaderSkill;
import patch.api.getMaster.CharacterSkill;
import patch.api.getMaster.GameData;
import show.config.ShowConfig;
import tool.Downloader;
import tool.FileUtil;
import tool.ZLibUtils;

public class ShowFlower {
	private final static GameData[] cis = CharacterInformation.get();

	public static void main(String[] args) {
		for (GameData gd : cis) {
			if (gd instanceof CharacterInformation) {
				CharacterInformation ci = (CharacterInformation) gd;
				if (ci.isCharacter()) {
					showNewEvolution(ci);
					showNewBloom(ci);
				}
			}
		}
	}

	public static void showNewBloom(CharacterInformation ci) {
		int n = 17;
		int bn = ci.getBloomNumber();
		boolean flag = bn == 4 * n + 1 || bn == 4 * n + 2 || bn == 4 * n + 3 || bn == 4 * n + 4;

		if (flag) show(CharacterInformation.getElement(cis, ci.getID() - 1 + 300000));
	}

	public static void showNewEvolution(CharacterInformation ci) {
		int limit = 324;
		int bid = ci.getBid();
		if (bid > limit) show(ci);
	}

	/*-------------------------------------------------------*/

	public static BufferedImage show(CharacterInformation ci) {
		if (ci == null) return null;

		int orb = ci.getOeb();
		int id = ci.getID();

		if (orb == 1) {
			System.out.println("原始角色：" + id);
			return null;
		}

		ArrayList<BufferedImage> ims = new ArrayList<>();
		if (orb == 2) {
			ims.add(getImage(id - 1));
			ims.add(getImage(getDescription(ci)));
			ims.add(getImage(id));
		} else if (orb == 3) {
			ims.add(getImage(id));
			ims.add(getImage(getDescription(ci)));
		}

		int width = 0;
		int height = 0;
		for (BufferedImage im : ims) {
			width += im.getWidth();
			if (height < im.getHeight()) height = im.getHeight();
		}

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.createGraphics();
		int x = 0;
		int y;
		for (BufferedImage im : ims) {
			y = (height - im.getHeight()) / 2;
			g.drawImage(im, x, y, null);
			x += im.getWidth();
		}

		try {
			ImageIO.write(image, "png", new File(id + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	private static BufferedImage getImage(int id) {
		File file = new File(ShowConfig.CHARACTER_STAND_S + "\\" + id + ".png");
		if (file.exists()) {
			try {
				return ImageIO.read(file);
			} catch (IOException e) {

			}
		}

		String urlStr = ShowConfig.getCharacterStandSNetpath(id);

		byte[] bytes = Downloader.download(urlStr);
		if (bytes == null) {
			System.out.println("bytes == null 1");
			return null;
		}

		bytes = ZLibUtils.decompress(bytes);
		if (bytes == null) {
			System.out.println("bytes == null 2");
			return null;
		}

		try {
			FileUtil.save(file, bytes);
			return ImageIO.read(new ByteArrayInputStream(bytes));
		} catch (IOException e) {
			System.out.println("ImageIO.read() 错误.");
			return null;
		}
	}

	private static BufferedImage getImage(String info) {
		int width = 500;
		int height = 600;
		int x, y;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(new Font(null, Font.BOLD, 18));

		FontMetrics fm = g.getFontMetrics();
		int charWidth;
		int charHeight = fm.getHeight();
		x = 0;
		y = 20 + fm.getAscent();
		for (int index = 0; index < info.length(); index++) {
			char ch = info.charAt(index);
			charWidth = fm.charWidth(ch);

			if (ch == '\n') {
				x = 0;
				y += charHeight;
				if (index + 1 < info.length() && info.charAt(index + 1) == '\n') {
					x = 0;
					y += charHeight / 2;
					index++;
				}
			}

			if (x + charWidth > width) {
				x = 0;
				y += charHeight;
			}

			g.drawString("" + ch, x, y);
			x += charWidth;
		}

		return image;
	}

	private static String getDescription(CharacterInformation ci) {
		GameData[] clss = CharacterLeaderSkill.get();
		GameData[] css = CharacterSkill.get();

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < ci.getRarity(); i++)
			sb.append("★");
		sb.append("\n");
		sb.append("花名：" + ci.getName() + "\n\n");
		sb.append("属性：" + ci.getAttackAttribute() + "\n");
		sb.append("移动力：" + ci.getMove() + "\n");

		int[] hp = ci.getHP();
		int[] attack = ci.getAttack();
		int[] defense = ci.getDefense();
		int h = hp[0];
		int a = attack[0];
		int d = defense[0];
		sb.append(String.format("000%%满级：%d  %d  %d %d\n", h, a, d, h + a + d));
		h += hp[1] * 1.2;
		a += attack[1] * 1.2;
		d += defense[1] * 1.2;
		sb.append(String.format("100%%满级：%d  %d  %d %d\n", h, a, d, h + a + d));
		h += hp[2] * 1.2;
		a += attack[2] * 1.2;
		d += defense[2] * 1.2;
		sb.append(String.format("200%%满级：%d  %d  %d %d\n", h, a, d, h + a + d));

		sb.append("\n");

		GameData[] skill = ci.getSkill(css, clss);

		if (skill[2] != null && skill[2] instanceof CharacterSkill) {
			CharacterSkill cs = (CharacterSkill) skill[2];
			sb.append("主动技能名：\n" + cs.getName() + "\n");
			sb.append("主动技能：\n" + cs.getEffect() + "\n");
		}
		if (skill[0] != null && skill[0] instanceof CharacterLeaderSkill) {
			CharacterLeaderSkill cls = (CharacterLeaderSkill) skill[0];
			sb.append("被动1：\n" + cls.getEffect() + "\n");
		}
		if (skill[1] != null && skill[1] instanceof CharacterLeaderSkill) {
			CharacterLeaderSkill cls = (CharacterLeaderSkill) skill[1];
			sb.append("被动2：\n" + cls.getEffect() + "\n");
		}

		return sb.toString();
	}

}
