package patch;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.IntSupplier;

public class FKGPatcher extends Thread {
	private final ServerSocket server;
	private final IntSupplier port;

	public FKGPatcher(ServerSocket server, IntSupplier port) {
		this.setDaemon(true);
		this.server = server;
		this.port = port;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Socket socket = this.server.accept();
				new Transfer(socket, this.port.getAsInt()).start();
			} catch (IOException e) {
				break;
			}
		}
	}

	public void close() throws IOException {
		this.server.close();
	}

	public int getPort1() {
		return this.server.getLocalPort();
	}

	public int getPort2() {
		return this.port.getAsInt();
	}
}
