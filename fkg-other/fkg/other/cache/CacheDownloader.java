package fkg.other.cache;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import tool.Downloader;
import tool.compress.ZLib;

class CacheDownloader {
	public static void download(int id, CacheInformation cain) {
		byte[] bytes = Downloader.download(cain.getUrlstr());
		if (bytes == null) {
			System.out.println(cain.getFilename());
			return;
		}

		bytes = ZLib.decompress(bytes);
		if (bytes == null) {
			System.out.println(cain.getFilename());
			return;
		}

		String exten = cain.getExten();

		String filepath = "cache\\" + id + "\\" + cain.getName() + "." + exten;
		try {
			FileUtils.writeByteArrayToFile(new File(filepath), bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
