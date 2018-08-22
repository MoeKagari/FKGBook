package fkg.book.temp;

import com.moekagari.tool.acs.ExStreamUtils;
import com.moekagari.tool.compress.MD5;
import com.moekagari.tool.compress.ZLib;
import com.moekagari.tool.other.Downloader;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.Optional;
import java.util.function.IntFunction;

/**
 * @author MoeKagari
 */
public class AbstractCharaDataImage {
	private static final int ICON_SIZE = 100;
	private static final Image[][] ICON_DECORATION = ExStreamUtils.of(
			ExStreamUtils.rangeClosed(1, 6).mapToObj(index -> String.format("background_%d.png", index)),
			ExStreamUtils.rangeClosed(1, 6).mapToObj(index -> String.format("frame_%d.png", index)),
			ExStreamUtils.rangeClosed(1, 4).mapToObj(index -> String.format("attribute_%d.png", index)),
			ExStreamUtils.rangeClosed(1, 3).mapToObj(index -> String.format("oeb_%d.png", index))
	).map(s -> s.map(filename -> "/icon/decoration/" + filename)
	            .map(AbstractCharaData.class::getResourceAsStream)
	            .map(Image::new)
	            .toArray(Image[]::new)
	).toArray(Image[][]::new);

	private static final String NET_PATH_PREFIX = "http://dugrqaqinbtcq.cloudfront.net/product/";
	//@formatter:off
	private static final String CHARACTER_CUTIN = "resources\\character_image\\cutin";
	private static String getCutinNetPath(int id) {
		return NET_PATH_PREFIX + "images/character/i/" + MD5.getMD5("cutin_" + id) + ".bin";
	}

	private static final String CHARACTER_ICON = "resources\\character_image\\icon";
	private static String getIconNetPath(int id) {
		return NET_PATH_PREFIX + "images/character/i/" + MD5.getMD5("icon_l_" + id) + ".bin";
	}

	private static final String CHARACTER_STAND = "resources\\character_image\\stand";
	private static String getStandNetPath(int id) {
		return NET_PATH_PREFIX + "images/character/s/" + MD5.getMD5("stand_" + id) + ".bin";
	}

	private static final String CHARACTER_STAND_S = "resources\\character_image\\stand_s";
	private static String getStandSNetPath(int id) {
		return NET_PATH_PREFIX + "images/character/s/" + MD5.getMD5("stand_s_" + id) + ".bin";
	}

	public static final String CHARACTER_STORY = "resources\\character_story";
	/** 非角色ID,而是角色的个人剧情对应的MasterStory里面的ID */
	public static String getStoryNetPath(int id) {
		return NET_PATH_PREFIX + "event/story/" + MD5.getMD5(String.format("story_%06d", id)) + ".bin";
	}
	//@formatter:on

	private final AbstractCharaData chara;
	private Image[] icons;

	public AbstractCharaDataImage(AbstractCharaData chara) {
		this.chara = chara;
	}

	private Image getImage(String dir, IntFunction<String> urlStr, int width, int height) {
		int image_id = this.chara.getImageId();
		File file = new File(String.format("%s\\%d.png", dir, image_id));

		Image image = null;
		try(FileInputStream fis = new FileInputStream(file)) {
			image = new Image(fis);
		} catch(Exception e) {
			e.printStackTrace();
		}

		image = Optional.ofNullable(image)
		                .orElseGet(() -> {
			                byte[] bytes = ZLib.decompress(Downloader.download(urlStr.apply(image_id)));
			                if(bytes != null) {
				                try {
					                Files.createDirectories(file.getParentFile().toPath());
					                Files.write(file.toPath(), bytes);
					                return new Image(new ByteArrayInputStream(bytes));
				                } catch(Exception e) {
					                e.printStackTrace();
				                }
			                }
			                return null;
		                });

		return image != null ? image : new WritableImage(width, height);
	}

	public Image getCutin() {
		return this.getImage(CHARACTER_CUTIN, AbstractCharaDataImage::getCutinNetPath, 960, 640);
	}

	public Image getStandS() {
		return this.getImage(CHARACTER_STAND_S, AbstractCharaDataImage::getStandSNetPath, 329, 467);
	}

	public Image getStand() {
		return this.getImage(CHARACTER_STAND, AbstractCharaDataImage::getStandNetPath, 960, 640);
	}

	public Image[] getIcons() {
		return this.icons = Optional.ofNullable(this.icons).orElseGet(() -> {
			int icon_index_oeb;
			switch(this.chara.getOeb()) {
				case 1:
					icon_index_oeb = 1;
					break;
				case 2:
					icon_index_oeb = 2;
					break;
				case 3:
					icon_index_oeb = 3;
					break;
				case 99:
					icon_index_oeb = this.chara.isBloomChara() ? 3 : 2;
					break;
				default:
					throw new RuntimeException("不可能的OEB : " + this.chara.getOeb());
			}
			return new Image[]{
					ICON_DECORATION[0][this.chara.getRarity() - 1],
					this.getImage(CHARACTER_ICON, AbstractCharaDataImage::getIconNetPath, ICON_SIZE, ICON_SIZE),
					ICON_DECORATION[1][this.chara.getRarity() - 1],
					ICON_DECORATION[2][this.chara.getAttackAttribute() - 1],
					ICON_DECORATION[3][icon_index_oeb - 1]
			};
		});
	}
}
