package fkg.patch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.BiConsumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.api.Response;

import fkg.gui.AppConfig;
import fkg.patch.api.GetBook;
import fkg.patch.api.GetMaster;
import fkg.patch.api.GetTurningCardSheet;
import fkg.patch.api.Login;

public abstract class CommunicateHandler {
	public final String serverName;
	public final String uri;

	public CommunicateHandler(String serverName, String uri) {
		this.serverName = serverName;
		this.uri = uri;
	}

	public void onHeaders(Response proxyResponse, HttpServletResponse httpResponse, BiConsumer<Response, HttpServletResponse> defaultOnHeaders) {
		defaultOnHeaders.accept(proxyResponse, httpResponse);
	}

	public void onContent(HttpServletResponse httpResponse, byte[] buffer, int offset, int length) throws IOException {
		httpResponse.getOutputStream().write(buffer, offset, length);
	}

	public void onSuccess(HttpServletRequest httpRequest, HttpServletResponse httpResponse, ByteArrayOutputStream requestBody, ByteArrayOutputStream responseBody) throws IOException {

	}

	/** if false, onSuccess 的参数 requestBody 无内容 */
	public boolean storeRequestBody() {
		return false;
	}

	/** if false, onSuccess 的参数 responseBody 无内容 */
	public boolean storeResponseBody() {
		return false;
	}

	public static CommunicateHandler get(String serverName, String uri) {
		if (FKGApiHandler.getServerName().equals(serverName)) {
			if (Login.getUri().equals(uri)) {
				if (AppConfig.patch() && AppConfig.isTihuan()) {
					return new Login();
				}
			} else if (GetBook.getUri().equals(uri)) {
				if (AppConfig.patch() && AppConfig.isAllCG()) {
					return new GetBook();
				}
			} else if (GetTurningCardSheet.getUri().equals(uri)) {
				return new GetTurningCardSheet();
			} else if (GetMaster.getUri().equals(uri)) {
				return new GetMaster();
			}
		}

		return new CommunicateHandler(serverName, uri) {};
	}

	public static class FKGApiHandler extends CommunicateHandler {
		public FKGApiHandler(String uri) {
			super(getServerName(), uri);
		}

		public static String getServerName() {
			return "web.flower-knight-girls.co.jp";
		}
	}
}
