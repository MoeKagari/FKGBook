package fkg.gui.jfx.other;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import fkg.config.AppConfig;
import fkg.config.ShowConfig;
import fkg.gui.jfx.other.filter.AttackAttributeFilter;
import fkg.gui.jfx.other.filter.CountryFilter;
import fkg.masterdata.CharacterLeaderSkillDescription;
import fkg.masterdata.CharacterSkill;
import fkg.masterdata.GetMasterData;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.media.AudioClip;
import tool.Downloader;
import tool.compress.ZLib;
import tool.function.FunctionUtils;

public abstract class CharacterData {
	private static final boolean lazyLoad = AppConfig.isLazyLoad();
	private static final int ICON_SIZE = 50;
	private static final BufferedImage[][] ICON_DECORATION = {
			IntStream.rangeClosed(1, 6)
					.mapToObj(index -> String.format("/icon_decoration/background_%d.png", index))
					.map(CharacterData::readImage)
					.toArray(BufferedImage[]::new),
			IntStream.rangeClosed(1, 6)
					.mapToObj(index -> String.format("/icon_decoration/frame_%d.png", index))
					.map(CharacterData::readImage)
					.toArray(BufferedImage[]::new),
			IntStream.rangeClosed(1, 4)
					.mapToObj(index -> String.format("/icon_decoration/attribute_%d.png", index))
					.map(CharacterData::readImage)
					.toArray(BufferedImage[]::new)
			/**/ };
	public static final Map<Integer, CharacterData> ALL_CHARA = GetMasterData.MASTERCHARACTER
			.stream()
			.collect(Collectors.toMap(CharacterData::getId, FunctionUtils::returnSelf));

	private static BufferedImage readImage(String filepath) {
		try {
			return ImageIO.read(CharacterData.class.getResourceAsStream(filepath));
		} catch (IOException e) {
			return new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_ARGB);
		}
	}

	private Image icon, stand, standS;
	private CharacterLeaderSkillDescription skill;//能力
	private CharacterSkill mainSkill;//技能
	private CharacterData evolutionChara;
	private AudioClip idleVoice;
	private int[] ids;

	/* 进化角色 */
	public CharacterData getEvolutionChara() {
		if (this.evolutionChara == null) {
			this.evolutionChara = ALL_CHARA.get(this.getIDS()[1]);
		}
		return this.evolutionChara;
	}

	public int[] getIDS() {
		if (this.ids == null) {
			int id = this.getId();
			switch (this.getOeb()) {
				case 1:
					this.ids = new int[] { id, id + 1, id + 300000 };
					break;
				case 2:
					this.ids = new int[] { id - 1, id, id - 1 + 300000 };
					break;
				case 3:
					this.ids = new int[] { id - 300000, id - 300000 + 1, id };
					break;
			}
		}
		return this.ids;
	}

	public AudioClip getIdleVoice() {
		if (this.idleVoice == null) {
			this.idleVoice = this.getVoice("fkg_mypageignore.mp3");
		}
		return this.idleVoice;
	}

	public AudioClip getVoice(String voice_file) {
		AudioClip audio;
		File file = new File(ShowConfig.CHARACTER_VOICE + "\\" + this.getIDS()[0] + "\\" + voice_file);

		try (FileInputStream fis = new FileInputStream(file)) {
			audio = new AudioClip(file.toURI().toString());
		} catch (Exception e) {
			audio = null;
		}

		if (audio == null) {
			byte[] bytes = ZLib.decompress(Downloader.download(ShowConfig.getCharacterVoiceNetpath(this.getIDS()[0], voice_file)));
			if (bytes != null) {
				try {
					FileUtils.writeByteArrayToFile(file, bytes);
				} catch (Exception e2) {}
				try (FileInputStream fis = new FileInputStream(file)) {
					audio = new AudioClip(file.toURI().toString());
				} catch (Exception e1) {
					audio = null;
				}
			}
		}

		return audio;
	}

	public Image getHome(int expressionType) {
		File file = new File(String.format("%s\\%d%s.png",
				ShowConfig.CHARACTER_HOME, this.getId(), (expressionType == 0 ? "" : ("_0" + expressionType))
		/**/));

		Image image;
		try (FileInputStream fis = new FileInputStream(file)) {
			image = new Image(fis);
		} catch (Exception e) {
			image = null;
		}

		if (image == null) {
			byte[] bytes = ZLib.decompress(Downloader.download(ShowConfig.getCharacterHomeNetpath(this.getId(), expressionType)));
			if (bytes != null) {
				try {
					FileUtils.writeByteArrayToFile(file, bytes);
					image = new Image(new ByteArrayInputStream(bytes));
				} catch (Exception e2) {}
			}
		}

		return image;
	}

	private Image getImage(String filedir, IntFunction<String> urlStr, int width, int height) {
		int image_id = this.getId();
		if (this.getOeb() == 3 && this.getKariBloom()) {
			image_id = image_id - 300000 + 1;
		}

		File file = new File(String.format("%s\\%d.png", filedir, image_id));

		Image image;
		try (FileInputStream fis = new FileInputStream(file)) {
			image = new Image(fis);
		} catch (Exception e) {
			image = null;
		}

		System.out.println(filedir);
		System.out.println(System.currentTimeMillis());
		if (image == null) {
			byte[] bytes = ZLib.decompress(Downloader.download(urlStr.apply(image_id)));
			if (bytes != null) {
				try {
					FileUtils.writeByteArrayToFile(file, bytes);
					image = new Image(new ByteArrayInputStream(bytes));
				} catch (Exception e2) {}
			}
		}
		System.out.println(System.currentTimeMillis());

		return (image != null) ? image : new WritableImage(width, height);
	}

	public Image getStandS() {
		if (this.standS == null) {
			Image image = this.getImage(ShowConfig.CHARACTER_STAND_S, ShowConfig::getCharacterStandSNetpath, 329, 467);
			if (lazyLoad) {
				return image;
			} else {
				this.standS = image;
			}
		}
		return this.standS;
	}

	public Image getStand() {
		if (this.stand == null) {
			Image image = this.getImage(ShowConfig.CHARACTER_STAND, ShowConfig::getCharacterStandNetpath, 960, 640);
			if (lazyLoad) {
				return image;
			} else {
				this.stand = image;
			}
		}
		return this.stand;
	}

	public Image getIcon() {
		if (this.icon == null) {
			BufferedImage result = new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_ARGB);

			for (BufferedImage image : new BufferedImage[] {
					ICON_DECORATION[0][this.getRarity() - 1],
					SwingFXUtils.fromFXImage(this.getImage(ShowConfig.CHARACTER_ICON, ShowConfig::getCharacterIconNetpath, 50, 50), null),
					ICON_DECORATION[1][this.getRarity() - 1],
					ICON_DECORATION[2][this.getAttackAttribute() - 1] }
			/**/) {
				result.createGraphics().drawImage(
						image.getScaledInstance(ICON_SIZE, ICON_SIZE, java.awt.Image.SCALE_SMOOTH),
						0, 0, null);
			}

			this.icon = SwingFXUtils.toFXImage(result, null);
		}
		return this.icon;
	}

	public abstract String getVersion();

	public CharacterSkill getMainSkill() {
		if (this.mainSkill == null) {
			this.mainSkill = GetMasterData.MASTERSKILL.stream().filter(ms -> ms.id == this.getMainSkillNumber()).findFirst().orElse(null);
		}
		return this.mainSkill;
	}

	public abstract int getMainSkillNumber();

	public CharacterLeaderSkillDescription getSkill() {
		if (this.skill == null) {
			this.skill = GetMasterData.MASTERLEADERSKILLDESCRIPTION.stream().filter(mlsd -> mlsd.cid == this.getId()).findFirst().orElse(null);
		}
		return this.skill;
	}

	public abstract boolean isCharacter();

	public abstract int getBloomNumber();

	public abstract int getBid();

	public abstract int getOeb();

	public abstract boolean hasBloom();

	public abstract boolean getKariBloom();

	/** 未实现 */
	public String getChineseName() {
		return "";
	}

	public abstract int getCountry();

	public String getCountryString() {
		return CountryFilter.SIS[this.getCountry()].getString();
	}

	public abstract int[] getPower();

	public abstract int[] getDefense();

	public abstract int[] getAttack();

	public abstract int[] getHP();

	public abstract int getMove();

	public abstract int getAttackAttribute();

	public String getAttackAttributeString() {
		return AttackAttributeFilter.SIS[this.getAttackAttribute()].getString();
	}

	public abstract int getRarity();

	public String getRarityString() {
		return "★★★★★★★★★★".substring(0, this.getRarity());
	}

	public abstract int getId();

	public abstract String getName();
}
