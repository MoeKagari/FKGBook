package patch;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

import patch.api.ApiResponse;
import patch.api.ApiResponseFactory;

public class Transfer extends Thread {
	private final int port;
	private Socket client, agent;
	private InputStream ais, cis;
	private OutputStream aos, cos;
	private CountDownLatch count = new CountDownLatch(2);

	private String header = null;
	private ByteArrayOutputStream request = new ByteArrayOutputStream();
	private ByteArrayOutputStream response = new ByteArrayOutputStream();

	public Transfer(Socket client, int port) {
		this.setDaemon(true);
		this.client = client;
		this.port = port;
	}

	@Override
	public void run() {
		try {
			this.client.setSoTimeout(20000);
			this.cis = this.client.getInputStream();
			this.cos = this.client.getOutputStream();
		} catch (IOException e) {
			System.out.println("³õÊ¼»¯IOÊ§°Ü.");
			this.closeIO();
			return;
		}
		/*---------------------------------------------------*/
		try {
			this.agent = new Socket("127.0.0.1", this.port);
			this.agent.setSoTimeout(20000);
			this.ais = this.agent.getInputStream();
			this.aos = this.agent.getOutputStream();
		} catch (IOException e) {
			System.out.println("Á¬½Ó´úÀí·þÎñÆ÷Ê§°Ü.");
			this.closeIO();
			return;
		}
		/*---------------------------------------------------*/
		try {
			this.header = this.readHeader();
		} catch (IOException e) {
			System.out.println("read headerÊ§°Ü.");
		} finally {
			if (this.header == null) {
				System.out.println("header == null");
				this.closeIO();
				return;
			}
		}
		/*---------------------------------------------------*/
		try {
			byte[] bytes = this.header.getBytes();
			this.writeCTA(bytes, 0, bytes.length);
		} catch (IOException e) {
			System.out.println("write headerÊ§°Ü.");
			this.closeIO();
			return;
		}
		/*---------------------------------------------------*/
		ApiResponse patch = ApiResponseFactory.get(this.header.trim());
		if (patch != null) patch.response(this);
		else this.handle();
		/*---------------------------------------------------*/
		try {
			this.count.await();
		} catch (InterruptedException e) {
			System.out.println("waitTransfer() falied.");
		} finally {
			if (patch != null) {
				Thread thread = new Thread(() -> patch.deal(this.response.toByteArray()));
				thread.setDaemon(true);
				thread.start();
			}
			this.closeIO();
		}
	}

	/*-------------------------------------------------*/
	private String readHeader() throws IOException {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			int b;

			do {
				if (baos.size() > 1000) return null;
				b = this.cis.read();
				baos.write(b);
			} while (b != '\n');

			return baos.toString();
		}
	}

	private void closeIO() {
		this.closeIO(this.cis);
		this.closeIO(this.cos);
		this.closeIO(this.client);

		this.closeIO(this.request);
		this.closeIO(this.response);

		this.closeIO(this.ais);
		this.closeIO(this.aos);
		this.closeIO(this.agent);
	}

	private void closeIO(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				System.out.println("closeIO() ´íÎó.");
				e.printStackTrace();
			}
		}
	}

	private void agentToClient() {
		try {
			byte[] buffer = new byte[1024];
			int len;
			while ((len = this.readATC(buffer)) != -1) {
				this.writeATC(buffer, 0, len);
			}
		} catch (IOException e) {
			System.out.println("a to c ´íÎó:" + this.header);
		} finally {
			this.countDown();
		}
	}

	private void clientToAgent() {
		try {
			byte[] buffer = new byte[1024];
			int len;
			while ((len = this.readCTA(buffer)) != -1) {
				this.writeCTA(buffer, 0, len);
			}
		} catch (IOException e) {
			System.out.println("c to a ´íÎó:" + this.header);
		} finally {
			this.countDown();
		}
	}

	/*-----------------------------------------------------------*/

	public void writeCTA(byte[] buffer, int off, int len) throws IOException {
		this.aos.write(buffer, off, len);
		this.aos.flush();
		this.request.write(buffer, 0, len);
	}

	public void writeATC(byte[] buffer, int off, int len) throws IOException {
		this.cos.write(buffer, off, len);
		this.cos.flush();
		this.response.write(buffer, 0, len);
	}

	public int readCTA(byte[] buffer) throws IOException {
		return this.cis.read(buffer);
	}

	public int readATC(byte[] buffer) throws IOException {
		return this.ais.read(buffer);
	}

	public void handle() {
		new Thread(() -> this.clientToAgent()).start();
		new Thread(() -> this.agentToClient()).start();
	}

	public void countDown() {
		this.count.countDown();
	}

	public String getHeader() {
		return this.header;
	}

}
