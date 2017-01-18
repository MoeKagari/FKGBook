package show;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import patch.api.getMaster.CharacterInformation;
import patch.api.getMaster.CharacterLeaderSkill;
import patch.api.getMaster.CharacterSkill;
import patch.api.getMaster.GameData;
import show.config.AppConfig;
import tool.Downloader;
import tool.FileUtil;
import tool.ZLibUtils;

public class CharacterData {

	public static class Skill {
		private final String mainSkillName;
		private final String mainSkillEffect;
		private final int mainSkillType;

		private final String skill1Effect;
		private final int[] skill1Type;
		private final String skill2Effect;
		private final int[] skill2Type;

		public String getMainSkillName() {
			return this.mainSkillName;
		}

		public String getMainSkillEffect() {
			return this.mainSkillEffect;
		}

		public int getMainSkillType() {
			return this.mainSkillType;
		}

		public String getSkill1Effect() {
			return this.skill1Effect;
		}

		public int[] getSkill1Type() {
			return this.skill1Type;
		}

		public String getSkill2Effect() {
			return this.skill2Effect;
		}

		public int[] getSkill2Type() {
			return this.skill2Type;
		}

		public Skill(String mainSkillName, String mainSkillEffect, int mainSkillType, String skill1Effect, int[] skill1Type, String skill2Effect, int[] skill2Type) {
			super();
			this.mainSkillName = mainSkillName;
			this.mainSkillEffect = mainSkillEffect;
			this.mainSkillType = mainSkillType;
			this.skill1Effect = skill1Effect;
			this.skill1Type = skill1Type;
			this.skill2Effect = skill2Effect;
			this.skill2Type = skill2Type;
		}

	}

	private int id;
	private int bid;
	private String name;
	private int rarity;
	private int attackAttribute;
	private int move;
	private int[] hp = new int[3];
	private int[] attack = new int[3];
	private int[] defense = new int[3];
	private int country;
	private int oeb;
	private int hasBloom;
	private int bloomNumber;
	private Skill skill;
	private String chineseName;

	public CharacterData(String source) {
		String[] info = source.trim().split(",");

		int index = 0;

		this.id = Integer.parseInt(info[index++]);
		this.bid = Integer.parseInt(info[index++]);
		this.name = info[index++];
		this.rarity = Integer.parseInt(info[index++]);
		this.attackAttribute = Integer.parseInt(info[index++]);
		this.move = Integer.parseInt(info[index++]);
		this.hp[0] = Integer.parseInt(info[index++]);
		this.hp[1] = Integer.parseInt(info[index++]);
		this.hp[2] = Integer.parseInt(info[index++]);
		this.attack[0] = Integer.parseInt(info[index++]);
		this.attack[1] = Integer.parseInt(info[index++]);
		this.attack[2] = Integer.parseInt(info[index++]);
		this.defense[0] = Integer.parseInt(info[index++]);
		this.defense[1] = Integer.parseInt(info[index++]);
		this.defense[2] = Integer.parseInt(info[index++]);
		this.country = Integer.parseInt(info[index++]);
		this.oeb = Integer.parseInt(info[index++]);
		this.hasBloom = Integer.parseInt(info[index++]);
		this.bloomNumber = Integer.parseInt(info[index++]);
		this.skill = new Skill(info[index++], info[index++], Integer.parseInt(info[index++]), info[index++], new int[] { Integer.parseInt(info[index++]), Integer.parseInt(info[index++]) }, info[index++],
				new int[] { Integer.parseInt(info[index++]), Integer.parseInt(info[index++]) });

		this.chineseName = CharacterChineseName.get().get(this.id);
	}

	public ImageIcon getIcon() {
		return new ImageIcon(getImage(this.id, AppConfig.CHARACTER_ICON, AppConfig.getCharacterIconNetpath(this.id), 50, 50));
	}

	public BufferedImage getStand() {
		return getImage(this.id, AppConfig.CHARACTER_STAND, AppConfig.getCharacterStandNetpath(this.id), 960, 640);
	}

	public BufferedImage getSkillImage() {
		BufferedImage buffer = getSkillImage(this);
		BufferedImage stand_s = getImage(this.id, AppConfig.CHARACTER_STAND_S, AppConfig.getCharacterStandSNetpath(this.id), 329, 467);
		BufferedImage skillImage = new BufferedImage(buffer.getWidth(), buffer.getHeight() + stand_s.getHeight(), BufferedImage.TYPE_INT_ARGB);

		Graphics g = skillImage.createGraphics();
		g.drawImage(stand_s, (buffer.getWidth() - stand_s.getWidth()) / 2, 0, null);
		g.drawImage(buffer, 0, stand_s.getHeight(), null);

		return skillImage;
	}

	/*------------------------------------------------------*/

	private static BufferedImage getSkillImage(CharacterData cd) {
		BufferedImage image = new BufferedImage(frameImage.getWidth(), frameImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(frameImage, 0, 0, null);

		int x, y;
		g.setColor(Color.black);
		g.setFont(new Font(null, Font.BOLD, 14));

		FontMetrics fm = g.getFontMetrics();
		int charWidth;
		int charHeight = fm.getHeight();

		String str = cd.getSkill().getMainSkillName();
		str = str.replace("?", "¡¤");
		x = 150;
		y = 4 + fm.getAscent();
		for (int index = 0; index < str.length(); index++) {
			char ch = str.charAt(index);
			charWidth = fm.charWidth(ch);
			g.drawString("" + ch, x, y);
			x += charWidth;
		}

		g.setColor(Color.white);
		g.setFont(new Font(null, Font.BOLD, 12));
		fm = g.getFontMetrics();
		charHeight = fm.getHeight();

		str = cd.getSkill().getMainSkillEffect();
		x = 15;
		y = 35 + fm.getAscent();
		for (int index = 0; index < str.length(); index++) {
			char ch = str.charAt(index);
			charWidth = fm.charWidth(ch);
			g.drawString("" + ch, x, y);
			x += charWidth;
		}

		str = cd.getSkill().getSkill1Effect();
		if (!"null".equals(str)) {
			x = 15;
			y = 125 + fm.getAscent();
			for (int index = 0; index < str.length(); index++) {
				char ch = str.charAt(index);
				charWidth = fm.charWidth(ch);

				if (x > image.getWidth() - 25) {
					x = 15;
					y += charHeight;
				}

				g.drawString("" + ch, x, y);
				x += charWidth;
			}
		}

		str = cd.getSkill().getSkill2Effect();
		if (!"null".equals(str)) {
			x = 15;
			y = 182 + fm.getAscent();
			for (int index = 0; index < str.length(); index++) {
				char ch = str.charAt(index);
				charWidth = fm.charWidth(ch);

				if (x > image.getWidth() - 25) {
					x = 15;
					y += charHeight;
				}

				g.drawString("" + ch, x, y);
				x += charWidth;
			}
		}

		return image;
	}

	private final static BufferedImage frameImage;

	static {
		final int width = 451;
		final int height = 240;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int[][] xy = { { 0, 110 }, { 1, 95 }, { 0, 15 }, { 1, 0 } };
		for (int i = 0; i < 4; i++) {
			try {
				BufferedImage temp = ImageIO.read(new File(AppConfig.SKILL_PANEL_BACKGROUND + "\\" + (i + 1) + ".png"));
				g.drawImage(temp, xy[i][0], xy[i][1], null);
			} catch (IOException e) {

			}
		}

		int x, y;
		g.setColor(Color.black);
		g.setFont(new Font(null, Font.BOLD, 14));

		FontMetrics fm = g.getFontMetrics();
		int charWidth;
		// int charHeight = fm.getHeight();
		x = 34;
		y = 4 + fm.getAscent();
		String info = "‘éêL¥¹¥­¥ë";
		for (int index = 0; index < info.length(); index++) {
			char ch = info.charAt(index);
			charWidth = fm.charWidth(ch);
			g.drawString("" + ch, x, y);
			x += charWidth;
		}

		x = 34;
		y = 99 + fm.getAscent();
		info = "¥¢¥Ó¥ê¥Æ¥£";
		for (int index = 0; index < info.length(); index++) {
			char ch = info.charAt(index);
			charWidth = fm.charWidth(ch);
			g.drawString("" + ch, x, y);
			x += charWidth;
		}

		frameImage = image;
	}

	private static BufferedImage getImage(int id, String filedir, String urlStr, int width, int height) {
		BufferedImage image = null;
		String filepath = filedir + "\\" + id + ".png";

		try {
			image = ImageIO.read(new File(filepath));
		} catch (IOException e) {

		}

		if (image == null) {
			byte[] bytes = Downloader.download(urlStr);
			if (bytes != null) bytes = ZLibUtils.decompress(bytes);
			if (bytes != null) FileUtil.save(filepath, bytes);

			try {
				if (bytes != null) image = ImageIO.read(new ByteArrayInputStream(bytes));
			} catch (IOException e) {

			}
		}

		return (image != null) ? image : new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	/*------------------------------------------------------*/

	public int getId() {
		return this.id;
	}

	public int getBid() {
		return this.bid;
	}

	public String getName() {
		return this.name;
	}

	public int getRarity() {
		return this.rarity;
	}

	public int getAttackAttributeNumber() {
		return this.attackAttribute;
	}

	public int getMove() {
		return this.move;
	}

	public int getOEB() {
		return this.oeb;
	}

	public int[] getHp() {
		return new int[] { (int) (this.hp[0] + 1.2 * this.hp[1] + 1.2 * this.hp[2]), (int) (this.hp[0] + 1.2 * this.hp[1]), this.hp[0] };
	}

	public int[] getAttack() {
		return new int[] { (int) (this.attack[0] + 1.2 * this.attack[1] + 1.2 * this.attack[2]), (int) (this.attack[0] + 1.2 * this.attack[1]), this.attack[0] };
	}

	public int[] getDefense() {
		return new int[] { (int) (this.defense[0] + 1.2 * this.defense[1] + 1.2 * this.defense[2]), (int) (this.defense[0] + 1.2 * this.defense[1]), this.defense[0] };
	}

	public int[] getPower() {
		int[] hp = this.getHp();
		int[] attack = this.getAttack();
		int[] defense = this.getDefense();
		return new int[] { hp[0] + attack[0] + defense[0], hp[1] + attack[1] + defense[1], hp[2] + attack[2] + defense[2] };
	}

	public int getCountryNumber() {
		return this.country;
	}

	public Skill getSkill() {
		return this.skill;
	}

	public boolean hasBloom() {
		return this.hasBloom == 1;
	}

	public String getChineseName() {
		return this.chineseName;
	}

	public int getBloomNumber() {
		return this.bloomNumber;
	}

	/*------------------------------------------------------*/
	private static TreeMap<Integer, CharacterData> cdMap;

	public static TreeMap<Integer, CharacterData> get() {
		if (cdMap == null) {
			cdMap = new TreeMap<>();
			String data = getData();
			if (data == null) return null;
			String[] sources = data.split("\r\n");

			for (String source : sources) {
				CharacterData cd = new CharacterData(source);
				cdMap.put(cd.getId(), cd);
			}
		}

		return cdMap;
	}

	private static String getData() {
		StringBuilder sb = new StringBuilder();
		GameData[] css = CharacterSkill.get();
		GameData[] clss = CharacterLeaderSkill.get();
		GameData[] cis = CharacterInformation.get();
		if (css == null || clss == null || cis == null) {
			System.out.println("getData() == null");
			return null;
		}

		for (GameData obj : cis) {
			if (obj instanceof CharacterInformation) {
				CharacterInformation ci = (CharacterInformation) obj;
				if (!ci.isCharacter()) continue;

				sb.append(ci.getID()).append(",");
				sb.append(ci.getBid()).append(",");
				sb.append(ci.getName()).append(",");
				sb.append(ci.getRarity()).append(",");
				sb.append(ci.getAttackAttributeNumber()).append(",");
				sb.append(ci.getMove()).append(",");

				for (int n : ci.getHP())
					sb.append(n).append(",");
				for (int n : ci.getAttack())
					sb.append(n).append(",");
				for (int n : ci.getDefense())
					sb.append(n).append(",");

				sb.append(ci.getCountryNumber()).append(",");
				sb.append(ci.getOeb()).append(",");
				sb.append(ci.hasBloom() ? "1" : "0").append(",");

				CharacterInformation evo = null;
				switch (ci.getOeb()) {
					case 1:
						evo = CharacterInformation.getElement(cis, ci.getID() + 1);
						break;
					case 2:
						evo = ci;
						break;
					case 3:
						evo = CharacterInformation.getElement(cis, ci.getID() + 1 - 300000);
						break;
				}
				if (evo == null) evo = ci;
				sb.append(evo.getBloomNumber()).append(",");

				GameData[] skills = ci.getSkill(css, clss);
				GameData skill;
				String name, effect;
				int c, g;

				skill = skills[2];
				if (skill != null && skill instanceof CharacterSkill) {
					CharacterSkill cs = (CharacterSkill) skill;
					name = cs.getName().replace("?", "¡¤");
					effect = cs.getEffect();
					c = cs.getSkillType();
				} else {
					name = "null";
					effect = "null";
					c = 0;
				}
				sb.append(name).append(",").append(effect).append(",").append(c).append(",");

				skill = skills[0];
				if (skill != null && skill instanceof CharacterLeaderSkill) {
					CharacterLeaderSkill cls = (CharacterLeaderSkill) skill;
					effect = cls.getEffect();
					c = cls.getSkillType()[0];
					g = cls.getSkillType()[1];
				} else {
					effect = "null";
					c = 0;
					g = 0;
				}
				sb.append(effect).append(",").append(c).append(",").append(g).append(",");

				skill = skills[1];
				if (skill != null && skill instanceof CharacterLeaderSkill) {
					CharacterLeaderSkill cls = (CharacterLeaderSkill) skill;
					effect = cls.getEffect();
					c = cls.getSkillType()[0];
					g = cls.getSkillType()[1];
				} else {
					effect = "null";
					c = 0;
					g = 0;
				}
				sb.append(effect).append(",").append(c).append(",").append(g).append(",");

				sb.append("\r\n");
			}
		}

		return sb.toString();
	}

}
