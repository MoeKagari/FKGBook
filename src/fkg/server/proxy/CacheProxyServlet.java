package fkg.server.proxy;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import fkg.gui.AppConfig;

/** 为了使用缓存*/
@SuppressWarnings("serial")
public class CacheProxyServlet extends ProxyServlet {
	@Override
	protected void service(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
		String cachePath = AppConfig.cachePath();
		if (cachePath != null) {
			if ("dugrqaqinbtcq.cloudfront.net".equals(httpRequest.getServerName())) {
				String url = httpRequest.getRequestURI();
				File cacheFile = new File(cachePath + url.replace('/', '\\'));
				if (cacheFile.exists() && cacheFile.isFile()) {
					byte[] bytes = null;
					try {
						bytes = FileUtils.readFileToByteArray(cacheFile);
					} catch (IOException e) {

					}
					if (bytes != null) {
						httpResponse.getOutputStream().write(bytes, 0, bytes.length);
						return;
					}
				}
			}
		}

		super.service(httpRequest, httpResponse);
	}
}
