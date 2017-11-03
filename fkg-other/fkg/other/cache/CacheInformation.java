package fkg.other.cache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

import tool.Downloader;
import tool.compress.MD5;
import tool.compress.ZLib;

public class CacheInformation {
	private final static String profix = "http://dugrqaqinbtcq.cloudfront.net/";

	private String dir;
	private String name;
	private String exten;

	public CacheInformation(String dir, String name, String exten) {
		this.dir = dir;
		this.name = name;
		this.exten = exten;
	}

	public String getDir() {
		return this.dir;
	}

	public String getName() {
		return this.name;
	}

	public String getExten() {
		return this.exten;
	}

	public String getUrlstr() {
		return profix + this.dir + MD5.getMD5(this.name) + "." + "bin";
	}

	public String getFilename() {
		return this.dir + this.name + "." + "bin";
	}
	/*-------------------------------------------------*/

	public static CacheInformation get_stand_s_id(int id) {
		return new CacheInformation("product/images/character/s/", "stand_s_" + id, "png");
	}

	public static ArrayList<CacheInformation> get(int id) {
		ArrayList<CacheInformation> path = new ArrayList<>();

		path.add(new CacheInformation("product/images/character/i/", "icon_s_" + id, "png"));
		path.add(new CacheInformation("product/images/character/i/", "icon_m_" + id, "png"));
		path.add(new CacheInformation("product/images/character/i/", "icon_l_" + id, "png"));
		path.add(new CacheInformation("product/images/character/s/", "stand_s_" + id, "png"));
		//对于进化前ID
		path.add(new CacheInformation("product/images/character/s/", "chibi_" + id, "swf"));

		path.add(new CacheInformation("product/images/character/s/", "home_" + id, "png"));
		for (int i = 0; i <= 10; i++)
			path.add(new CacheInformation("product/images/character/s/", "home_" + id + "_0" + i, "png"));

		path.add(new CacheInformation("product/images/character/s/", "bustup_" + id, "png"));
		for (int i = 0; i <= 10; i++)
			path.add(new CacheInformation("product/images/character/s/", "bustup_" + id + "_0" + i, "png"));
		path.add(new CacheInformation("product/images/character/s/", "bustup_" + id + "_2", "png"));
		for (int i = 0; i <= 10; i++)
			path.add(new CacheInformation("product/images/character/s/", "bustup_" + id + "_2_0" + i, "png"));
		path.add(new CacheInformation("product/images/character/s/", "cutin_" + id, "png"));
		path.add(new CacheInformation("product/images/character/s/", "stand_" + id, "png"));

		if (id < 400000) {
			path.add(new CacheInformation("product/images/hscene_r18/", "r18_" + id + "_000", "png"));
			path.add(new CacheInformation("product/images/hscene_r18/", "r18_" + id + "_001", "png"));
			path.add(new CacheInformation("product/images/hscene_r18/", "r18_" + (id + 0) + "_100", "png"));
			path.add(new CacheInformation("product/images/hscene_r18/", "r18_" + (id + 0) + "_101", "png"));
		} else {
			path.add(new CacheInformation("product/images/hscene_r18/", "r18_" + (id - 300000) + "_000", "png"));
			path.add(new CacheInformation("product/images/hscene_r18/", "r18_" + (id - 300000) + "_001", "png"));
			path.add(new CacheInformation("product/images/hscene_r18/", "r18_" + (id - 300000) + "_100", "png"));
			path.add(new CacheInformation("product/images/hscene_r18/", "r18_" + (id - 300000) + "_101", "png"));
		}

		//sound
		path.addAll(Arrays.asList(
				new CacheInformation("product/voice/c/", "", "mp3")
		/**/));

		return path;
	}

	public static void main(String[] args) throws IOException {
		for (int id : new int[] { //
				431919//
		}) {
			for (CacheInformation cain : get(id)) {
				CacheDownloader.download(id, cain);
			}
		}

		try (BufferedReader br = new BufferedReader(new FileReader(new File("resources\\getMaster\\masterCharacterMypageVoiceResource.csv")))) {
			br.lines().forEach(line -> {
				String[] tokens = line.split(",", -1);

				int id = Integer.parseInt(tokens[1]);
				id = id > 400000 ? (id - 300000) : id;
				id = id % 2 == 0 ? (id - 1) : id;

				String urlStr = profix + "product/voice/c/" + id + "/" + MD5.getMD5(tokens[2].split("\\.")[0]) + ".bin";
				//6ea6ceae5ad2530d6454ff4c86bd8581
				if (id == 131919) {
					byte[] bytes = ZLib.decompress(Downloader.download(urlStr));
					try {
						FileUtils.writeByteArrayToFile(new File("voice\\" + tokens[2]), bytes);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

}
