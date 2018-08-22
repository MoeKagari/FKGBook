package fkg.book.other.cache;

import com.moekagari.tool.compress.MD5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.IntStream;

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

	public static ArrayList<CacheInformation> get(int id) {
		ArrayList<CacheInformation> path = new ArrayList<>();

		//path.add(new CacheInformation("product/images/character/i/", "icon_s_" + id, "png"));
		//path.add(new CacheInformation("product/images/character/i/", "icon_m_" + id, "png"));
		path.add(new CacheInformation("product/images/character/i/", "icon_l_" + id, "png"));
		path.add(new CacheInformation("product/images/character/s/", "stand_s_" + id, "png"));
		path.add(new CacheInformation("product/images/character/s/", "stand_" + id, "png"));
		path.add(new CacheInformation("product/images/character/s/", "cutin_" + id, "png"));

		path.add(new CacheInformation("product/images/character/s/", "home_" + id, "png"));
		for(int i = 0; i <= 10; i++) {
			path.add(new CacheInformation("product/images/character/s/", "home_" + id + "_0" + i, "png"));
		}


		path.add(new CacheInformation("product/images/character/s/", "bustup_" + id, "png"));
		for(int i = 0; i <= 10; i++) {
			path.add(new CacheInformation("product/images/character/s/", "bustup_" + id + "_0" + i, "png"));
		}
		path.add(new CacheInformation("product/images/character/s/", "bustup_" + id + "_2", "png"));
		for(int i = 0; i <= 10; i++) {
			path.add(new CacheInformation("product/images/character/s/", "bustup_" + id + "_2_0" + i, "png"));
		}

		//对于进化前ID
		path.add(new CacheInformation("product/images/character/s/", "chibi_" + id, "swf"));


		if(id < 400000) {
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

		path.add(new CacheInformation("product/voice/c/" + (id - 300000) + "/", "fkg_" + id + "_quest_1_009", "mp3"));

		return path;
	}

	public static void main(String[] args) throws IOException {
		IntStream.of(
				//112821,
				144405,
				//168101,
				160019
		).parallel().forEach(id -> {
			CacheInformation.get(id).parallelStream().forEach(cain -> CacheDownloader.download(id, cain));
			CacheInformation.get(id + 1).parallelStream().forEach(cain -> CacheDownloader.download(id + 1, cain));
		});
	}
}
