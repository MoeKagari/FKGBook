package tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader {

	public static byte[] download(String urlStr) {
		HttpURLConnection huc;
		try {
			huc = (HttpURLConnection) new URL(urlStr).openConnection();
		} catch (IOException e) {
			return null;
		}

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); InputStream is = huc.getInputStream()) {
			byte[] b = new byte[1024];
			int len;
			while ((len = is.read(b)) != -1) {
				baos.write(b, 0, len);
			}
			return baos.toByteArray();
		} catch (IOException e) {
			return null;
		} finally {
			huc.disconnect();
		}
	}

}
