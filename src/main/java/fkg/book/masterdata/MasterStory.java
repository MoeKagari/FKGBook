package fkg.book.masterdata;

import com.moekagari.tool.compress.MD5;
import com.moekagari.tool.compress.ZLib;
import com.moekagari.tool.other.Downloader;
import com.moekagari.tool.io.FileUtils;
import fkg.book.masterdata.GetMasterData.GetMasterDataLineSpliter;

import java.io.File;
import java.util.stream.IntStream;

public class MasterStory {
	private final int id;
	/**
	 * 目前只有2,4,6<br>
	 * 2017-1-13 5:12:41<br>
	 * 2:masterStage<br>
	 * 4:masterCharacterQuest<br>
	 * 6:通关某一group后出现的后续剧情
	 */
	private final int type;
	/**
	 * 相应type里面的id
	 */
	private final int idInType;

	public MasterStory(String source) {
		GetMasterDataLineSpliter gmdls = new GetMasterDataLineSpliter(source);

		this.id = gmdls.nextInt();
		this.type = gmdls.nextInt();
		this.idInType = gmdls.nextInt();
	}

	public int getId() {
		return this.id;
	}

	public int getType() {
		return this.type;
	}

	public String getTypeString() {
		switch(this.getType()) {
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
		int start = 3950, end = start + 1000;
		for(IntStream intStream : new IntStream[]{
				//		IntStream.of(3331, 3332, 3333),
				IntStream.range(start, end)
		}) {
			intStream.forEach(MasterStory::downloadStory);
		}
	}

	private static void downloadStory(int id) {
		String urlStr = "http://dugrqaqinbtcq.cloudfront.net/product/event/story/" + MD5.getMD5(String.format("story_%06d", id)) + ".bin";
		byte[] bytes = Downloader.download(urlStr);
		if(bytes == null) {
			System.out.println(id + " " + 1);
			return;
		}
		bytes = ZLib.decompress(bytes);
		if(bytes == null) {
			System.out.println(id + " " + 2);
			return;
		}

		FileUtils.write(new File("story\\" + id + ""), bytes);
	}
}
