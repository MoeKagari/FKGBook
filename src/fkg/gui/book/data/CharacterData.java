package fkg.gui.book.data;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.io.FileUtils;

import fkg.gui.book.ShowConfig;
import fkg.patch.api.getMaster.CharacterInformation;
import fkg.patch.api.getMaster.CharacterLeaderSkill;
import fkg.patch.api.getMaster.CharacterQuest;
import fkg.patch.api.getMaster.CharacterSkill;
import fkg.patch.api.getMaster.GetMasterData;
import fkg.patch.api.getMaster.MasterStory;
import tool.Downloader;
import tool.ZLib;

public class CharacterData {
	public static class Skill {
		public final String mainSkillName;
		public final String mainSkillEffect;
		public final int mainSkillType;

		public final String skill1Effect;
		public final int[] skill1Type;
		public final String skill2Effect;
		public final int[] skill2Type;

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

	public final CharacterInformation ci;

	private int[] hp = new int[3];
	private int[] attack = new int[3];
	private int[] defense = new int[3];

	public final int bloomNumber;
	public final Skill skill;
	public final String chineseName;

	private final int image_id;
	public final ImageIcon icon;

	public CharacterData(CharacterInformation ci) {
		this.ci = ci;

		this.hp = ci.getHP();
		this.attack = ci.getAttack();
		this.defense = ci.getDefense();

		this.chineseName = CharacterChineseName.get().get(ci.getID());

		CharacterInformation evo = null;
		switch (ci.getOeb()) {
			case 1:
				evo = CharacterInformation.getElement(GetMasterData.MASTERCHARACTER, ci.getID() + 1);
				break;
			case 2:
				evo = ci;
				break;
			case 3:
				evo = CharacterInformation.getElement(GetMasterData.MASTERCHARACTER, ci.getID() + 1 - 300000);
				break;
		}
		if (evo == null) evo = ci;
		this.bloomNumber = evo.getBloomNumber();

		{
			String mainSkillName;
			String mainSkillEffect;
			int mainSkillType;

			String skill1Effect;
			int[] skill1Type;
			String skill2Effect;
			int[] skill2Type;

			GetMasterData[] skills = ci.getSkill(GetMasterData.MASTERSKILL, GetMasterData.MASTERLEADERSKILL);
			GetMasterData skill;

			skill = skills[2];
			if (skill != null && skill instanceof CharacterSkill) {
				CharacterSkill cs = (CharacterSkill) skill;
				mainSkillName = cs.getName().replace("?", "·");
				mainSkillEffect = cs.getEffect();
				mainSkillType = cs.getSkillType();
			} else {
				mainSkillName = null;
				mainSkillEffect = null;
				mainSkillType = 0;
			}

			skill = skills[0];
			if (skill != null && skill instanceof CharacterLeaderSkill) {
				CharacterLeaderSkill cls = (CharacterLeaderSkill) skill;
				skill1Effect = cls.getEffect();
				skill1Type = new int[] { cls.getSkillType()[0], cls.getSkillType()[1] };
			} else {
				skill1Effect = null;
				skill1Type = new int[] { 0, 0 };
			}

			skill = skills[1];
			if (skill != null && skill instanceof CharacterLeaderSkill) {
				CharacterLeaderSkill cls = (CharacterLeaderSkill) skill;
				skill2Effect = cls.getEffect();
				skill2Type = new int[] { cls.getSkillType()[0], cls.getSkillType()[1] };
			} else {
				skill2Effect = null;
				skill2Type = new int[] { 0, 0 };
			}

			this.skill = new Skill(mainSkillName, mainSkillEffect, mainSkillType, skill1Effect, skill1Type, skill2Effect, skill2Type);
		}

		int image_id = this.ci.getID();
		if (this.ci.getOeb() == 3 && this.ci.getKariBloom()) {
			image_id = image_id - 300000 + 1;
		}
		this.image_id = image_id;
		this.icon = new ImageIcon(getImage(image_id, ShowConfig.CHARACTER_ICON, ShowConfig.getCharacterIconNetpath(image_id), 50, 50));

	}

	public BufferedImage getStand() {
		return getImage(this.image_id, ShowConfig.CHARACTER_STAND, ShowConfig.getCharacterStandNetpath(this.image_id), 960, 640);
	}

	public BufferedImage getSkillImage() {
		BufferedImage buffer = getSkillImage(this);
		BufferedImage stand_s = getImage(this.image_id, ShowConfig.CHARACTER_STAND_S, ShowConfig.getCharacterStandSNetpath(this.image_id), 329, 467);
		BufferedImage skillImage = new BufferedImage(buffer.getWidth(), buffer.getHeight() + stand_s.getHeight(), BufferedImage.TYPE_INT_ARGB);

		Graphics g = skillImage.createGraphics();
		g.drawImage(stand_s, (buffer.getWidth() - stand_s.getWidth()) / 2, 0, null);
		g.drawImage(buffer, 0, stand_s.getHeight(), null);

		return skillImage;
	}

	public String[][] getCharacterStory() {
		int oeb = this.ci.getOeb();
		int o_id = this.ci.getID() - (oeb == 2 ? 1 : (oeb == 3 ? 300000 : 0));

		return new String[][] { getStoryInformation(o_id, 1), getStoryInformation(o_id, 2), getStoryInformation(o_id + 1, 3), getStoryInformation(o_id + 300000, 4) };
	}

	/*------------------------------------------------------*/

	private static String[] getStoryInformation(int id, int index) {
		String[] story = null;
		for (GetMasterData obj : GetMasterData.MASTERCHARACTERSTORY) {
			CharacterQuest cq = (CharacterQuest) obj;
			if (cq.cid == id && cq.index == index) {
				story = new String[] { null, null };
				story[0] = cq.name;
				for (GetMasterData gmd : GetMasterData.MASTERSTORY) {
					MasterStory ms = (MasterStory) gmd;
					if (ms.getIdInType() == cq.id) {
						story[1] = resolveStory(getStoryContent(id, index, ms.getId()));
						break;
					}
				}
			}
		}
		return story;
	}

	private static byte[] getStoryContent(int id, int index, int ms_id) {
		String filepath = String.format("%s\\%d" + (index <= 2 ? ("_" + index) : "") + ".txt", ShowConfig.CHARACTER_STORY, id);
		try {
			return FileUtils.readFileToByteArray(new File(filepath));
		} catch (IOException e) {
			byte[] content = Downloader.download(ShowConfig.getCharacterStoryNetpath(ms_id));
			if (content != null) {
				content = ZLib.decompress(content);
				if (content != null) {
					try {
						FileUtils.writeByteArrayToFile(new File(filepath), content);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
			return content;
		}
	}

	private static String resolveStory(byte[] content) {
		if (content == null) return null;
		StringBuilder sb = new StringBuilder();
		new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content), Charset.forName("utf-8")))//
				.lines().filter(line -> line.startsWith("mess,")).forEach(line -> {
					String[] eles = line.split(",");
					if ("".equals(eles[1]) == false) {
						sb.append(eles[1]).append("\n");
					}
					sb.append(eles[2].replace("\\n", "\n")).append("\n").append("\n");
				});
		return sb.toString();
	}

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

		String str = cd.skill.mainSkillName;
		str = str.replace("?", "·");
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

		str = cd.skill.mainSkillEffect;
		x = 15;
		y = 35 + fm.getAscent();
		for (int index = 0; index < str.length(); index++) {
			char ch = str.charAt(index);
			charWidth = fm.charWidth(ch);
			g.drawString("" + ch, x, y);
			x += charWidth;
		}

		str = cd.skill.skill1Effect;
		if (str != null) {
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

		str = cd.skill.skill2Effect;
		if (str != null) {
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
		frameImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = frameImage.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int[][] xy = { { 0, 110 }, { 1, 95 }, { 0, 15 }, { 1, 0 } };
		for (int i = 0; i < 4; i++) {
			try {
				BufferedImage temp = ImageIO.read(new File(String.format("%s\\%d.png", ShowConfig.SKILL_PANEL_BACKGROUND, i + 1)));
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
		String info = "戦闘スキル";
		for (int index = 0; index < info.length(); index++) {
			char ch = info.charAt(index);
			charWidth = fm.charWidth(ch);
			g.drawString("" + ch, x, y);
			x += charWidth;
		}

		x = 34;
		y = 99 + fm.getAscent();
		info = "アビリティ";
		for (int index = 0; index < info.length(); index++) {
			char ch = info.charAt(index);
			charWidth = fm.charWidth(ch);
			g.drawString("" + ch, x, y);
			x += charWidth;
		}
	}

	private static BufferedImage getImage(int id, String filedir, String urlStr, int width, int height) {
		BufferedImage image = null;
		String filepath = String.format("%s\\%d.png", filedir, id);

		try {
			image = ImageIO.read(new File(filepath));
		} catch (IOException e) {
			byte[] bytes = Downloader.download(urlStr);
			if (bytes != null) {
				bytes = ZLib.decompress(bytes);
				if (bytes != null) {
					try {
						FileUtils.writeByteArrayToFile(new File(filepath), bytes);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					try {
						image = ImageIO.read(new ByteArrayInputStream(bytes));
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		}

		return (image != null) ? image : new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	/*------------------------------------------------------*/

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

	/*------------------------------------------------------*/
	private static Map<Integer, CharacterData> cdMap;

	public static Map<Integer, CharacterData> get() {
		if (cdMap == null) {
			cdMap = new TreeMap<>();

			for (GetMasterData obj : GetMasterData.MASTERCHARACTER) {
				if (obj instanceof CharacterInformation) {
					CharacterInformation ci = (CharacterInformation) obj;
					if (ci.isCharacter()) {
						CharacterData cd = new CharacterData(ci);
						cdMap.put(cd.ci.getID(), cd);
					}
				}
			}
		}

		return cdMap;
	}

}
