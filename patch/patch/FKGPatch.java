package patch;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

public class FKGPatch extends Thread {

	private ServerSocket server;

	public FKGPatch(ServerSocket server) {
		this.server = server;
		this.createTrayIcon();
	}

	@Override
	public void run() {
		while (true) {
			try {
				Socket socket = this.server.accept();
				new Transfer(socket, agent_port).start();
			} catch (IOException e) {
				System.out.println("已停止");
				break;
			}
		}
	}

	private void createTrayIcon() {
		try {
			TrayIcon trayicon = new TrayIcon(ImageIO.read(this.getClass().getResourceAsStream("/icon.png")));
			trayicon.setImageAutoSize(true);

			PopupMenu menu = new PopupMenu();
			MenuItem exit = new MenuItem("退出");
			exit.addActionListener(ev -> {
				try {
					this.server.close();
					SystemTray.getSystemTray().remove(trayicon);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			menu.add(exit);

			trayicon.setPopupMenu(menu);
			SystemTray.getSystemTray().add(trayicon);
		} catch (IOException | AWTException e) {
			System.out.println("trayicon 创建失败");
		}
	}

	private static int client_port;
	private static int agent_port;

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("请输入监听的端口与代理的端口");
			return;
		}

		try {
			client_port = Integer.parseInt(args[0]);
			agent_port = Integer.parseInt(args[1]);
		} catch (Exception e) {
			System.out.println("请输入正确的数字(范围1~65535)");
		}

		int port = client_port;
		try {
			ServerSocket server = new ServerSocket(port);
			System.out.println("监听端口" + port + "成功.");
			System.out.println("----------------------------------------------------");
			new FKGPatch(server).start();
		} catch (IOException e) {
			System.out.println("监听端口" + port + "失败.");
			System.out.println("有可能端口已被占用.");
			return;
		}
	}

}
