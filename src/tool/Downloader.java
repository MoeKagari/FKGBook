package tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class Downloader {
	public static byte[] download(String urlStr) {
		return download(urlStr, -1);
	}

	public static byte[] download(String urlStr, int proxyPort) {
		Proxy proxy = null;
		if (proxyPort != -1) {
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", proxyPort));
		}

		HttpURLConnection huc;
		try {
			if (proxy == null) {
				huc = (HttpURLConnection) new URL(urlStr).openConnection();
			} else {
				huc = (HttpURLConnection) new URL(urlStr).openConnection(proxy);
			}
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
