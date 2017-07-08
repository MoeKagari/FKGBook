package fkg.gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TrayItem;

import fkg.gui.book.ShowFlowerGrilInformation;
import fkg.patch.FKGServerServlet;
import server.ServerConfig;

public class FKGGui {
	public static void main(String[] args) {
		String message = AppConfig.load();

		FKGServerServlet server = new FKGServerServlet(new ServerConfig(AppConfig::getServerPort, AppConfig::isUseProxy, () -> "127.0.0.1", AppConfig::getAgentPort));
		try {
			server.start();
		} catch (Exception e) {
			try {
				server.end();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return;
		}

		gui = new FKGGui(message, server);
		gui.display();

		try {
			server.end();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		AppConfig.store();
	}

	public static FKGGui gui;

	private final SimpleDateFormat CONSOLE_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	public final Display display = new Display();
	public final Image logo = new Image(this.display, this.getClass().getResourceAsStream("/icon.png"));
	private final Shell shell;
	private final Composite composite;
	private final TrayItem trayItem;
	private final org.eclipse.swt.widgets.List console;

	private final ShowFlowerGrilInformation shower;

	public FKGGui(String message, FKGServerServlet server) {
		this.shell = new Shell(this.display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		this.shell.setImage(this.logo);
		this.shell.setLayout(new GridLayout(1, false));
		this.shell.setLayoutData(new GridData(GridData.FILL_BOTH));
		this.shell.setSize(260, 400);
		this.shell.setText("美少女花骑士book");
		this.shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent ev) {
				MessageBox box = new MessageBox(FKGGui.this.shower.skillPanel.csv.shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION | SWT.ON_TOP);
				box.setText("退出");
				box.setMessage("退出?");
				if (ev.doit = box.open() == SWT.YES) {
					FKGGui.this.shower.dispose();
					FKGGui.this.trayItem.dispose();
					FKGGui.this.logo.dispose();
				}
			}

			@Override
			public void shellIconified(ShellEvent e) {
				FKGGui.this.shell.setVisible(false);
			}
		});

		this.composite = new Composite(this.shell, SWT.NONE);
		this.composite.setLayout(new GridLayout(1, false));
		this.composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite buttonComposite = new Composite(this.composite, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(2, false));
		buttonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		{
			this.shower = new ShowFlowerGrilInformation(this);
			Button allFKG = new Button(buttonComposite, SWT.PUSH);
			allFKG.setText("所有花娘");
			allFKG.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
			allFKG.addSelectionListener(new ControlSelectionListener(ev -> {
				this.shower.frame.setVisible(true);
				this.shower.frame.requestFocus();
			}));

			Composite puzzleComposite = new Composite(buttonComposite, SWT.NONE);
			puzzleComposite.setLayout(new GridLayout(1, false));
			puzzleComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			{
				Button puzzle = new Button(puzzleComposite, SWT.PUSH);
				puzzle.setText("翻牌Card");
				puzzle.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
				puzzle.addSelectionListener(new ControlSelectionListener(ev -> {
					Thread thread;
					if (AppConfig.isUseProxyForPuzzle()) {
						thread = new Thread(() -> Puzzle.main(new String[] { String.valueOf(AppConfig.getAgentPort()) }));
					} else {
						thread = new Thread(() -> Puzzle.main(new String[] {}));
					}
					thread.setDaemon(true);
					thread.start();
				}));

				Button useProxyForPuzzle = new Button(puzzleComposite, SWT.CHECK);
				useProxyForPuzzle.setText("使用代理");
				useProxyForPuzzle.setSelection(AppConfig.isUseProxyForPuzzle());
				useProxyForPuzzle.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
				useProxyForPuzzle.addSelectionListener(new ControlSelectionListener(ev -> AppConfig.setUseProxyForPuzzle(useProxyForPuzzle.getSelection())));
			}
		}

		Group portGroup = new Group(this.composite, SWT.NONE);
		portGroup.setText("端口");
		portGroup.setLayout(new GridLayout(3, false));
		portGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		{
			Spinner serverPort = new Spinner(portGroup, SWT.CENTER);
			serverPort.setMinimum(1);
			serverPort.setMaximum(65535);
			serverPort.setSelection(AppConfig.getServerPort());
			serverPort.setToolTipText("监听端口");
			serverPort.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			serverPort.addSelectionListener(new ControlSelectionListener(ev -> AppConfig.setServerport(serverPort.getSelection())));

			Composite agentComposite = new Composite(portGroup, SWT.NONE);
			agentComposite.setLayout(new GridLayout(1, false));
			agentComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			{
				Spinner agentPort = new Spinner(agentComposite, SWT.CENTER);
				agentPort.setMinimum(1);
				agentPort.setMaximum(65535);
				agentPort.setSelection(AppConfig.getAgentPort());
				agentPort.setToolTipText("代理端口");
				agentPort.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				agentPort.addSelectionListener(new ControlSelectionListener(ev -> AppConfig.setAgentport(agentPort.getSelection())));

				Button useProxy = new Button(agentComposite, SWT.CHECK);
				useProxy.setText("使用代理");
				useProxy.setSelection(AppConfig.isUseProxy());
				useProxy.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
				useProxy.addSelectionListener(new ControlSelectionListener(ev -> AppConfig.setUseProxy(useProxy.getSelection())));
			}

			Button apply = new Button(portGroup, SWT.PUSH);
			apply.setText("应用");
			apply.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
			apply.addSelectionListener(new ControlSelectionListener(ev -> {
				try {
					server.restart();
					this.printMessage("重启动: " + server.getConfig().getListenPort() + " → " + //
					(server.getConfig().isUseProxy() ? String.valueOf(server.getConfig().getProxyPort()) : "直连"));
				} catch (Exception ex) {
					ex.printStackTrace();
					this.printMessage("更换配置失败");
					this.printMessage(ex.getMessage());
				}
			}));
		}

		if (AppConfig.patch()) {
			this.initPatchGroup();
		}

		this.console = new org.eclipse.swt.widgets.List(this.composite, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		this.console.setLayoutData(new GridData(GridData.FILL_BOTH));
		this.printMessage(message);

		this.trayItem = new TrayItem(this.display.getSystemTray(), SWT.NONE);
		this.trayItem.setImage(this.logo);
		this.trayItem.addListener(SWT.Selection, ev -> {
			this.shell.setVisible(true);
			this.shell.setMinimized(false);
		});
		this.trayItem.addMenuDetectListener(new TrayItemMenuListener());
	}

	private void initPatchGroup() {
		Group patchGroup = new Group(this.composite, SWT.NONE);
		patchGroup.setText("补丁");
		patchGroup.setLayout(new GridLayout(2, false));
		patchGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		{
			Button allcg = new Button(patchGroup, SWT.CHECK);
			allcg.setText("全CG");
			allcg.setToolTipText("需要重启游戏");
			allcg.setSelection(AppConfig.isAllCG());
			allcg.addSelectionListener(new ControlSelectionListener(ev -> AppConfig.setAllCG(allcg.getSelection())));

			Composite tihuanComposite = new Composite(patchGroup, SWT.NONE);
			tihuanComposite.setLayout(new GridLayout(1, false));
			tihuanComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
			{
				Button tihuanButton = new Button(tihuanComposite, SWT.CHECK);
				tihuanButton.setText("替换副团长");
				tihuanButton.setToolTipText("需要重启游戏\nID对应的角色必须存在(不能为假开花角色ID)");
				tihuanButton.setSelection(AppConfig.isTihuan());
				tihuanButton.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true));
				tihuanButton.addSelectionListener(new ControlSelectionListener(ev -> AppConfig.setTihuan(tihuanButton.getSelection())));

				Text futuanzhangIDText = new Text(tihuanComposite, SWT.CENTER);
				futuanzhangIDText.setText(String.valueOf(AppConfig.getFutuanzhangID()));
				futuanzhangIDText.setToolTipText("副团长ID");
				futuanzhangIDText.setLayoutData(new GridData(GridData.FILL_BOTH));
				TextChangListener listener = new TextChangListener(futuanzhangIDText, AppConfig::setFutuanzhangID, "副团长ID只能有数字");
				futuanzhangIDText.addVerifyListener(listener);
				futuanzhangIDText.addModifyListener(listener);
			}
		}
	}

	public void printMessage(String message) {
		if (message == null) return;
		if (this.console.isDisposed()) return;
		if (this.console.getItemCount() >= 200) this.console.remove(0);

		this.console.add(this.CONSOLE_TIME_FORMAT.format(new Date()) + " " + message);
		this.console.setSelection(this.console.getItemCount() - 1);
	}

	private void display() {
		this.shell.open();
		while (this.shell.isDisposed() == false) {
			if (this.display.readAndDispatch() == false) {
				this.display.sleep();
			}
		}
	}

	private class TextChangListener implements VerifyListener, ModifyListener {
		private final Text control;
		private final IntConsumer handler;
		private final String errorMessage;

		private String origin;
		private int start;
		private boolean doit = true;

		public TextChangListener(Text control, IntConsumer handler, String errorMessage) {
			this.control = control;
			this.handler = handler;
			this.errorMessage = errorMessage;
		}

		@Override
		public void verifyText(VerifyEvent ev) {
			if (this.doit) {
				this.origin = this.control.getText();
				this.start = ev.start;
			}
		}

		@Override
		public void modifyText(ModifyEvent ev) {
			if (this.doit == false) {
				this.doit = true;
				return;
			}

			try {
				this.handler.accept(Integer.parseInt(String.valueOf(this.control.getText())));
			} catch (Exception e) {
				this.doit = false;
				this.control.setText(this.origin);
				this.control.setSelection(this.start);
				FKGGui.this.printMessage(this.errorMessage);
			}
		}
	}

	public static class ControlSelectionListener extends SelectionAdapter {
		private final Consumer<SelectionEvent> handler;

		public ControlSelectionListener(Consumer<SelectionEvent> handler) {
			this.handler = handler;
		}

		public ControlSelectionListener(Runnable run) {
			this.handler = ev -> run.run();
		}

		@Override
		public void widgetSelected(SelectionEvent ev) {
			this.handler.accept(ev);
		}
	}

	public class TrayItemMenuListener implements MenuDetectListener {
		private Menu menu;

		public TrayItemMenuListener() {
			this.menu = new Menu(FKGGui.this.shell);

			final MenuItem dispose = new MenuItem(this.menu, SWT.NONE);
			dispose.setText("退出");
			dispose.addSelectionListener(new ControlSelectionListener(FKGGui.this.shell::close));
		}

		@Override
		public void menuDetected(MenuDetectEvent e) {
			this.menu.setVisible(true);
		}
	}
}
