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

public class MainStart {
	public static void main(String[] args) {
		int port = 2222;
		try {
			ServerSocket server = new ServerSocket(port);
			System.out.println("�����˿�" + port + "�ɹ�.");
			System.out.println("----------------------------------------------------");
			new FKGAllCGPatch(server).start();
		} catch (IOException e) {
			System.out.println("�����˿�" + port + "ʧ��.");
			System.out.println("�п��ܶ˿��ѱ�ռ��.");
			return;
		}
	}

	public static class FKGAllCGPatch extends Thread {
		private ServerSocket server;

		public FKGAllCGPatch(ServerSocket server) {
			this.server = server;
			this.createTrayIcon();
		}

		@Override
		public void run() {
			while (true) {
				try {
					Socket socket = this.server.accept();
					new Transfer(socket).start();
				} catch (IOException e) {
					System.out.println("��ֹͣ");
					break;
				}
			}
		}

		private void createTrayIcon() {
			try {
				TrayIcon trayicon = new TrayIcon(ImageIO.read(this.getClass().getResourceAsStream("/icon.png")));
				trayicon.setImageAutoSize(true);

				PopupMenu menu = new PopupMenu();
				MenuItem exit = new MenuItem("�˳�");
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
				System.out.println("trayicon ����ʧ��");
			}
		}
	}

}
