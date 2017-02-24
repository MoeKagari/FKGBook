package patch;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FKGPatcher extends Thread {
	private ServerSocket server;
	private int port;

	public FKGPatcher(ServerSocket server, int port) {
		this.setDaemon(true);
		this.server = server;
		this.port = port;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Socket socket = this.server.accept();
				new Transfer(socket, this.port).start();
			} catch (IOException e) {
				break;
			}
		}
	}

	public void close() throws IOException {
		this.server.close();
	}

	public boolean needChange(int port1, int port2) {
		return this.server.getLocalPort() != port1 || this.port != port2;
	}

	public int getPort1() {
		return this.server.getLocalPort();
	}

	public int getPort2() {
		return this.port;
	}

	public void setPort2(int port) {
		this.port = port;
	}
}
