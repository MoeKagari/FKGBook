package patch.other.cache;

import java.util.ArrayList;

import tool.MD5;

class CacheInformation {

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

//		path.add(new CacheInformation("product/images/character/i/", "icon_s_" + id, "png"));
//		path.add(new CacheInformation("product/images/character/i/", "icon_m_" + id, "png"));
		path.add(new CacheInformation("product/images/character/i/", "icon_l_" + id, "png"));
		path.add(new CacheInformation("product/images/character/s/", "stand_s_" + id, "png"));
//		path.add(new CacheInformation("product/voice/c/" + String.format("%06d", id) + "/", "fkg_skillattack001", "mp3"));
//		//对于进化前ID
//		path.add(new CacheInformation("product/images/character/s/", "chibi_" + id, "swf"));
//
//		path.add(new CacheInformation("product/images/character/s/", "home_" + id, "png"));
//		for (int i = 0; i <= 10; i++)
//			path.add(new CacheInformation("product/images/character/s/", "home_" + id + "_0" + i, "png"));
//
//		path.add(new CacheInformation("product/images/character/s/", "bustup_" + id, "png"));
//		for (int i = 0; i <= 10; i++)
//			path.add(new CacheInformation("product/images/character/s/", "bustup_" + id + "_0" + i, "png"));
//		path.add(new CacheInformation("product/images/character/s/", "cutin_" + id, "png"));
//		path.add(new CacheInformation("product/images/character/s/", "stand_" + id, "png"));
		path.add(new CacheInformation("product/images/hscene_r18/", "r18_" + id + "_000", "png"));
		path.add(new CacheInformation("product/images/hscene_r18/", "r18_" + id + "_001", "png"));
		if (id > 400000) {
			path.add(new CacheInformation("product/images/hscene_r18/", "r18_" + (id - 300000) + "_100", "png"));
			path.add(new CacheInformation("product/images/hscene_r18/", "r18_" + (id - 300000) + "_101", "png"));
		}

		return path;
	}

	public static void main(String[] args) {
		for (int id : new int[] { //
				131925//
		}) {
			for (CacheInformation cain : get(id))
				CacheDownloader.download(id, cain);
		}
	}

}
