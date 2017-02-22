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
}
