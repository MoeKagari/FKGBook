package fkg.book.other.cache;

import com.moekagari.tool.compress.ZLib;
import com.moekagari.tool.other.Downloader;
import com.moekagari.tool.io.FileUtils;

import java.io.File;

public class CacheDownloader {
	public static void download(int id, CacheInformation cain) {
		byte[] bytes = Downloader.download(cain.getUrlstr());
		if(bytes == null) {
			System.out.println(cain.getFilename());
			return;
		}

		bytes = ZLib.decompress(bytes);
		if(bytes == null) {
			System.out.println(cain.getFilename());
			return;
		}

		String filepath = "cache\\" + id + "\\" + cain.getName() + "." + cain.getExten();
		FileUtils.write(new File(filepath), bytes);
	}
}
