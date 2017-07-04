package fkg.server.proxy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ProxyConfiguration;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Request.ContentListener;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.client.util.InputStreamContentProvider;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.util.HttpCookieStore;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import fkg.gui.AppConfig;
import fkg.patch.CommunicateHandler;

@SuppressWarnings("serial")
public class ProxyServlet extends HttpServlet {
	private static final Set<String> HOP_HEADERS = new HashSet<>();
	static {
		HOP_HEADERS.add("proxy-connection");
		HOP_HEADERS.add("connection");
		HOP_HEADERS.add("keep-alive");
		HOP_HEADERS.add("transfer-encoding");
		HOP_HEADERS.add("te");
		HOP_HEADERS.add("trailer");
		HOP_HEADERS.add("proxy-authorization");
		HOP_HEADERS.add("proxy-authenticate");
		HOP_HEADERS.add("upgrade");
	}

	private HttpClient client;
	private long timeout = 60000;

	@Override
	public void destroy() {
		try {
			this.client.stop();
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	@Override
	public void init() throws ServletException {
		try {
			this.client = this.createHttpClient();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	protected HttpClient createHttpClient() throws ServletException {
		HttpClient client = new HttpClient();
		if (AppConfig.isUseProxy()) {
			client.setProxyConfiguration(new ProxyConfiguration("127.0.0.1", AppConfig.getAgentPort()));
		}

		QueuedThreadPool executor = new QueuedThreadPool(256);
		String servletName = this.getServletConfig().getServletName();
		int dot = servletName.lastIndexOf('.');
		if (dot >= 0) servletName = servletName.substring(dot + 1);
		executor.setName(servletName);
		client.setExecutor(executor);

		client.setFollowRedirects(false);
		client.setCookieStore(new HttpCookieStore.Empty());
		client.setMaxConnectionsPerDestination(32768);
		client.setIdleTimeout(30000);

		try {
			client.start();
			client.getContentDecoderFactories().clear();
			return client;
		} catch (Exception x) {
			throw new ServletException(x);
		}
	}

	@Override
	protected void service(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
		AsyncContext asyncContext = httpRequest.startAsync();
		asyncContext.setTimeout(0);

		StringBuffer url = httpRequest.getRequestURL();
		String query = httpRequest.getQueryString();
		if (query != null) url.append("?").append(query);
		URI targetUri = URI.create(url.toString());

		ProxyRequestHandler prh = new ProxyRequestHandler(httpRequest, httpResponse, asyncContext, targetUri);
		prh.send();
	}

	private Request createProxyRequest(HttpServletRequest httpRequest, URI targetUri, InputStreamContentProvider contentProvider) {
		Request proxyRequest = this.client.newRequest(targetUri).method(HttpMethod.fromString(httpRequest.getMethod())).version(HttpVersion.fromString(httpRequest.getProtocol()));

		for (Enumeration<String> headerNames = httpRequest.getHeaderNames(); headerNames.hasMoreElements();) {
			String headerName = headerNames.nextElement();

			String lowerHeaderName = headerName.toLowerCase(Locale.ENGLISH);
			if (HOP_HEADERS.contains(lowerHeaderName)) continue;

			for (Enumeration<String> headerValues = httpRequest.getHeaders(headerName); headerValues.hasMoreElements();) {
				String headerValue = headerValues.nextElement();
				if (headerValue != null) {
					proxyRequest.header(headerName, headerValue);
				}
			}
		}

		proxyRequest.content(contentProvider);
		proxyRequest.timeout(this.timeout, TimeUnit.MILLISECONDS);
		return proxyRequest;
	}

	private class ProxyRequestHandler extends Response.Listener.Empty implements ContentListener {
		private final URI targetUri;
		private final ByteArrayOutputStream requestBody = new ByteArrayOutputStream();
		private final ByteArrayOutputStream responseBody = new ByteArrayOutputStream();
		private final HttpServletRequest httpRequest;
		private final HttpServletResponse httpResponse;
		private final CommunicateHandler handler;
		private final AsyncContext asyncContext;

		private final InputStream httpRequestInputStream;
		private final ByteArrayOutputStream contentBuffer = new ByteArrayOutputStream();
		private boolean retryEnabled = true;

		private void send() {
			Request proxyRequest = ProxyServlet.this.createProxyRequest(this.httpRequest, this.targetUri, new InputStreamContentProvider(this.httpRequestInputStream) {
				@Override
				public long getLength() {
					return fkg.server.proxy.ProxyServlet.ProxyRequestHandler.this.httpRequest.getContentLength();
				}

				@Override
				protected ByteBuffer onRead(byte[] buffer, int offset, int length) {
					if (length > 0) {
						if (ProxyServlet.ProxyRequestHandler.this.contentBuffer.size() < 256 * 1024) {
							ProxyServlet.ProxyRequestHandler.this.contentBuffer.write(buffer, offset, length);
						} else {
							ProxyRequestHandler.this.retryEnabled = false;
						}
					}
					return super.onRead(buffer, offset, length);
				}
			});
			proxyRequest.onRequestContent(this);
			proxyRequest.send(this);
		}

		@Override
		public void onContent(Request request, ByteBuffer content) {
			if (this.handler.storeRequestBody()) {
				byte[] buffer;
				int offset;
				int length = content.remaining();
				if (content.hasArray()) {
					buffer = content.array();
					offset = content.arrayOffset();
				} else {
					buffer = new byte[length];
					content.get(buffer);
					offset = 0;
				}
				this.requestBody.write(buffer, offset, length);
			}
		}

		public ProxyRequestHandler(HttpServletRequest httpRequest, HttpServletResponse httpResponse, AsyncContext asyncContext, URI targetUri) throws IOException {
			this.httpRequest = httpRequest;
			this.httpResponse = httpResponse;
			this.httpRequestInputStream = httpRequest.getInputStream();
			this.handler = CommunicateHandler.get(httpRequest.getServerName(), httpRequest.getRequestURI());
			this.asyncContext = asyncContext;
			this.targetUri = targetUri;
		}

		@Override
		public void onBegin(Response proxyResponse) {
			this.retryEnabled = false;
			this.httpResponse.setStatus(proxyResponse.getStatus());
		}

		@Override
		public void onHeaders(Response proxyResponse) {
			this.handler.onHeaders(proxyResponse, this.httpResponse, this::defaultOnHeaders);
		}

		private void defaultOnHeaders(Response proxyResponse, HttpServletResponse httpResponse) {
			for (HttpField field : proxyResponse.getHeaders()) {
				String headerName = field.getName();
				if (HOP_HEADERS.contains(headerName.toLowerCase(Locale.ENGLISH))) {
					continue;
				}

				String headerValue = field.getValue();
				if ((headerValue == null) || (headerValue.trim().length() == 0)) {
					continue;
				}

				httpResponse.addHeader(headerName, headerValue);
			}
		}

		@Override
		public void onContent(Response proxyResponse, ByteBuffer content) {
			byte[] buffer;
			int offset;
			int length = content.remaining();
			if (content.hasArray()) {
				buffer = content.array();
				offset = content.arrayOffset();
			} else {
				buffer = new byte[length];
				content.get(buffer);
				offset = 0;
			}

			if (this.handler.storeResponseBody()) {
				this.responseBody.write(buffer, offset, length);
			}

			try {
				this.handler.onContent(this.httpResponse, buffer, offset, length);
			} catch (IOException e) {
				proxyResponse.abort(e);
			}
		}

		@Override
		public void onSuccess(Response proxyResponse) {
			try {
				this.handler.onSuccess(this.httpRequest, this.httpResponse, this.requestBody, this.responseBody);
			} catch (IOException e) {
				proxyResponse.abort(e);
			}
			this.asyncContext.complete();
		}

		@Override
		public void onFailure(Response proxyResponse, Throwable failure) {
			if (this.retryEnabled && (failure instanceof EOFException) && (HttpVersion.fromString(this.httpRequest.getProtocol()) == HttpVersion.HTTP_1_1)) {
				return;
			}

			this.retryEnabled = false;
			if (!this.httpResponse.isCommitted()) {
				if (failure instanceof TimeoutException) {
					this.httpResponse.setStatus(HttpServletResponse.SC_GATEWAY_TIMEOUT);
				} else {
					this.httpResponse.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
				}
			}
			this.asyncContext.complete();
		}

		@Override
		public void onComplete(Result result) {
			if (this.retryEnabled) {
				this.retryEnabled = false;
				Request proxyRequest = ProxyServlet.this.createProxyRequest(this.httpRequest, this.targetUri,
						new InputStreamContentProvider(new SequenceInputStream(new ByteArrayInputStream(this.contentBuffer.toByteArray()), this.httpRequestInputStream)) {
							@Override
							public long getLength() {
								return ProxyServlet.ProxyRequestHandler.this.httpRequest.getContentLength();
							}
						});
				proxyRequest.onRequestContent(this);
				proxyRequest.send(this);
			}
		}
	}
}
