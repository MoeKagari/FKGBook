package fkg.masterdata;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import tool.Downloader;
import tool.compress.MD5;
import tool.compress.ZLib;

public class MasterStory implements GetMasterData {
	private final int id;
	/**
	 * 目前只有2,4,6
	 * 2017-1-13 5:12:41
	 * 2:masterStage
	 * 4:masterCharacterQuest
	 * 6:通关某一group后出现的后续剧情
	 */
	private final int type;
	/**
	 * 相应type里面的id
	 */
	private final int idInType;

	public MasterStory(String source) {
		String[] info = source.trim().split(",", -1);

		int index = 0;
		this.id = Integer.parseInt(info[index++]);
		this.type = Integer.parseInt(info[index++]);
		this.idInType = Integer.parseInt(info[index++]);
	}

	/*----------------------------------------------------------*/

	public int getId() {
		return this.id;
	}

	public int getType() {
		return this.type;
	}

	public String getTypeString() {
		switch (this.getType()) {
			case 2:
				return "EventStory";
			case 4:
				return "CharacterStory";
			case 6:
				return "";
			default:
				return "";
		}
	}

	public int getIdInType() {
		return this.idInType;
	}

	public static void main(String[] args) {
		for (int id : new int[] { 2578, 2579, 2580 }) {
			String urlStr = "http://dugrqaqinbtcq.cloudfront.net/product/event/story/" + MD5.getMD5(String.format("story_%06d", id)) + ".bin";
			byte[] bytes = Downloader.download(urlStr);
			if (bytes == null) {
				System.out.println(id + " " + 1);
				continue;
			}
			bytes = ZLib.decompress(bytes);
			if (bytes == null) {
				System.out.println(id + " " + 2);
				continue;
			}

			try {
				FileUtils.writeByteArrayToFile(new File(id + ""), bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
