package fkg.gui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import fkg.patch.api.GetTurningCardSheet;
import tool.Downloader;

public class Puzzle {
	private static int proxyPort = -1;

	public static void main(String[] args) {
		if (args.length > 0) {
			try {
				proxyPort = Integer.parseInt(args[0]);
			} catch (Exception e) {
				FKGGui.gui.printMessage("puzzle: 代理端口只能含有数字");
			}
		}

		File file = new File(GetTurningCardSheet.filename);
		if (file.exists() == false) {
			FKGGui.gui.printMessage("puzzle: 无文件");
			JOptionPane.showMessageDialog(null, "无文件");
			return;
		}

		try {
			byte[] bytes = FileUtils.readFileToByteArray(new File(GetTurningCardSheet.filename));
			deal(bytes);
			FKGGui.gui.printMessage("puzzle: 完成");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int[] getRowCol(int size) {
		switch (size) {
			case 9:
				return new int[] { 3, 3, 0 };
			case 12:
				return new int[] { 3, 4, 0 };
			case 16:
				return new int[] { 4, 4, 0 };
			case 20:
				return new int[] { 4, 5, 0 };
			case 26:
				return new int[] { 5, 6, 4 };
			case 32:
				return new int[] { 6, 6, 4 };
			case 40:
				return new int[] { 5, 8, 0 };
			case 56:
				return new int[] { 7, 8, 0 };
			case 63:
				return new int[] { 8, 8, 1 };
			case 64:
				return new int[] { 8, 8, 0 };
			default:
				return null;
		}
	}

	private static void deal(byte[] source) {
		long time = Calendar.getInstance().getTimeInMillis();
		Json.createReader(new ByteArrayInputStream(source)).readObject().getJsonArray("turningCardSheetList").stream().//
				map(Card::new).collect(Collectors.groupingBy(o -> o.turningCardSheetGroupId)).forEach((turningCardSheetGroupId, turningCardSheetGroupIdResult) -> {
					turningCardSheetGroupIdResult.stream().collect(Collectors.groupingBy(o -> o.sheetType)).forEach((sheetType, sheetTypeResult) -> {
						sheetTypeResult.stream().collect(Collectors.groupingBy(o -> o.level)).forEach((level, levelResult) -> {
							deal(levelResult, time + String.format("\\%d\\%02d\\%02d.png", turningCardSheetGroupId, sheetType, level));
						});
					});
				});
	}

	private static void deal(List<Card> levelResult, String filename) {
		List<BufferedImage> images = new ArrayList<>();
		Collections.sort(levelResult, (a, b) -> Integer.compare(a.cardOrderNum, b.cardOrderNum));
		levelResult.stream().map(Puzzle::downloadCard).forEach(images::add);

		int[] rc = getRowCol(levelResult.size());
		if (rc == null) {
			System.out.println("新布局: size = " + levelResult.size());
			System.out.println(filename);
			for (int i = 0; i < images.size(); i++) {
				saveImage(new File(filename + "\\" + i + ".png"), images.get(i));
			}
			return;
		}
		int r = rc[0], c = rc[1];
		int space = 10;
		BufferedImage base = new BufferedImage((space + 100) * c + space, (space + 100) * r + space, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = base.createGraphics();

		int count = 0;
		for (int s = 0; s < r; s++) {
			for (int t = 0; t < c; t++) {
				if (rc[2] == 4) {
					if (s == 0 && t == 0) continue;
					if (s == 0 && t == c - 1) continue;
					if (s == r - 1 && t == 0) continue;
					if (s == r - 1 && t == c - 1) continue;
				} else if (rc[2] == 1) {
					if (s == r - 1 && t == c - 1) continue;
				}

				g.drawImage(images.get(count), space + (space + 100) * t, space + (space + 100) * s, null);
				count++;
			}
		}

		saveImage(new File(filename), base);
	}

	private static BufferedImage downloadCard(Card card) {
		int size = 100;
		if (card.textureName.startsWith("000001")) {//金币
			int amount = Integer.parseInt(card.turningCardItemRateGroupName.substring(0, card.turningCardItemRateGroupName.indexOf("\u30b4")));
			BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
			Graphics g = image.createGraphics();
			g.setFont(new Font(null, Font.PLAIN, 15));
			g.drawString(amount + "金币", 10, 50);
			return image;
		} else {
			File file = new File("resources\\puzzle\\cache\\product\\images\\item\\100x100\\" + card.textureName);
			try {
				return ImageIO.read(file);
			} catch (Exception e) {
				String urlStr = "http://dugrqaqinbtcq.cloudfront.net/product/images/item/100x100/" + card.textureName;
				byte[] bytes = Downloader.download(urlStr, proxyPort);
				if (bytes == null) {
					return new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
				} else {
					try {
						FileUtils.writeByteArrayToFile(file, bytes);
					} catch (IOException ex) {

					}
					try {
						return ImageIO.read(new ByteArrayInputStream(bytes));
					} catch (Exception ex) {
						return new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
					}
				}
			}
		}
	}

	public static class Card {
		int id;//???
		int turningCardSheetGroupId;//种类,普�?�puzzle,按时后续�?放puzzle,条件�?放puzzle......
		int level;//第几�?
		int sheetType;// 4种，左下角人物去不同
		int turningCardItemRateGroupId;
		String turningCardItemRateGroupName;
		int cardOrderNum;//从左到右,从上到下的编�?
		String textureName;//内容
		boolean keyItemFlag;//是否为keyitem,不翻到的�?,不能进入下一�?
		int hintGroupId;//没什么用,关键item左下角人物和荠菜motion不同

		public Card(JsonValue jo) {
			this((JsonObject) jo);
		}

		public Card(JsonObject jo) {
			this.id = jo.getInt("id");
			this.turningCardSheetGroupId = jo.getInt("turningCardSheetGroupId");
			this.level = jo.getInt("level");
			this.sheetType = jo.getInt("sheetType");
			this.turningCardItemRateGroupId = jo.getInt("turningCardItemRateGroupId");
			this.turningCardItemRateGroupName = jo.getString("turningCardItemRateGroupName");
			this.cardOrderNum = jo.getInt("cardOrderNum");
			this.textureName = jo.getString("textureName");
			this.keyItemFlag = jo.getInt("keyItemFlag") == 1;
			this.hintGroupId = jo.getInt("hintGroupId");
		}

		// "id": 5869,
		// "turningCardSheetGroupId": 3,
		// "level": 4,
		// "sheetType": 1,
		// "turningCardItemRateGroupId": 84,
		// "turningCardItemRateGroupName": "\u5f37\u5316\u970a\u9752\u306e\u30de\u30cb\u30e520\u624d x1",
		// "cardOrderNum": 8,
		// "textureName": "109918.png",
		//"hintGroupId": 0,//2017.03.06新增
		// "keyItemFlag": 0
	}

	private static void saveImage(File file, BufferedImage image) {
		try {
			file.getParentFile().getAbsoluteFile().mkdirs();
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
