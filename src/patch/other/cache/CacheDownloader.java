package patch.other.cache;

import tool.Downloader;
import tool.FileUtil;
import tool.ZLibUtils;

class CacheDownloader {

	public static void download(int id, CacheInformation cain) {
		byte[] bytes = Downloader.download(cain.getUrlstr());
		if (bytes == null) return;

		bytes = ZLibUtils.decompress(bytes);
		if (bytes == null) return;

		String exten = cain.getExten();

		String filepath = "cache\\" + id + "\\" + cain.getName() + "." + exten;
		FileUtil.save(filepath, bytes);
	}

}
