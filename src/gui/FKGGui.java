package gui;

import java.io.IOException;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
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

import patch.FKGPatcher;
import show.ShowFlowerInformation;

public class FKGGui {

	public static void main(String[] args) {
		String message = GuiConfig.load();
		new FKGGui(message).display();
		GuiConfig.store();
	}

	public static boolean ZHIYONG = true;//是否自用,非自用时不初始化[补丁面板],以及直接不使用替换和全CG
	public static final SimpleDateFormat CONSOLE_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	private Image logo = new Image(this.display, this.getClass().getResourceAsStream("/icon.png"));
	private Display display = Display.getDefault();
	private Shell shell;
	private Composite composite;
	private TrayItem trayItem;
	private org.eclipse.swt.widgets.List console;

	private FKGPatcher patcher = null;
	private ShowFlowerInformation shower = null;

	public FKGGui(String message) {
		this.shell = new Shell(this.display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		this.shell.setImage(this.logo);
		this.shell.setLayout(new GridLayout(1, false));
		this.shell.setLayoutData(new GridData(GridData.FILL_BOTH));
		this.shell.setSize(260, 400);
		this.shell.setText("美少女花骑士book");
		this.shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				MessageBox box = new MessageBox(FKGGui.this.shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION | SWT.ON_TOP);
				box.setText("退出");
				box.setMessage("退出?");
				e.doit = box.open() == SWT.YES;
				if (e.doit) FKGGui.this.dispose();
			}

			@Override
			public void shellIconified(ShellEvent e) {
				FKGGui.this.shell.setVisible(false);
			}
		});

		this.composite = new Composite(this.shell, SWT.NONE);
		this.composite.setLayout(new GridLayout(1, false));
		this.composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Button allFKG = new Button(this.composite, SWT.PUSH);
		allFKG.setText("所有花娘");
		allFKG.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		allFKG.addSelectionListener(new ControlSelectionListener(ev -> {
			if (this.shower == null) this.shower = new ShowFlowerInformation();
			this.shower.display();
			this.shower.setFocus();
		}));

		Group portGroup = new Group(this.composite, SWT.NONE);
		portGroup.setText("端口");
		portGroup.setLayout(new GridLayout(2, false));
		portGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		{
			Spinner serverPort = new Spinner(portGroup, SWT.CENTER);
			serverPort.setMinimum(1);
			serverPort.setMaximum(65535);
			serverPort.setSelection(GuiConfig.getServerPort());
			serverPort.setToolTipText("监听端口");
			serverPort.setLayoutData(new GridData(GridData.FILL_BOTH));
			serverPort.addSelectionListener(new ControlSelectionListener(ev -> GuiConfig.setServerport(serverPort.getSelection())));

			Spinner agentPort = new Spinner(portGroup, SWT.CENTER);
			agentPort.setMinimum(1);
			agentPort.setMaximum(65535);
			agentPort.setSelection(GuiConfig.getAgentPort());
			agentPort.setToolTipText("代理端口");
			agentPort.setLayoutData(new GridData(GridData.FILL_BOTH));
			agentPort.addSelectionListener(new ControlSelectionListener(ev -> {
				GuiConfig.setAgentport(agentPort.getSelection());
				if (this.patcher != null) this.printChangeAgentPort("更换配置: ");
			}));

			Composite portButtonComposite = new Composite(portGroup, SWT.NONE);
			portButtonComposite.setLayout(new GridLayout(3, false));
			portButtonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
			{
				Button apply = new Button(portButtonComposite, SWT.PUSH);
				apply.setText("应用");
				apply.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
				apply.addSelectionListener(new ControlSelectionListener(ev -> {
					try {
						int port1 = GuiConfig.getServerPort();

						if (this.patcher == null || this.patcher.getPort1() != port1) {
							if (this.patcher != null) {
								this.patcher.close();
								this.printMessage("关闭成功");
							}
							this.patcher = new FKGPatcher(new ServerSocket(port1), () -> GuiConfig.getAgentPort());
							this.patcher.start();
							this.printChangeAgentPort("开启成功: ");
						}
					} catch (Exception ev1) {
						this.printMessage((this.patcher == null ? "开启" : "更换配置") + "失败");
						return;
					}
				}));

				Button cancel = new Button(portButtonComposite, SWT.PUSH);
				cancel.setText("关闭");
				cancel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
				cancel.addSelectionListener(new ControlSelectionListener(ev -> {
					if (this.patcher != null) try {
						this.patcher.close();
						this.patcher = null;
						this.printMessage("关闭成功");
					} catch (Exception e) {
						this.printMessage("关闭失败");
					}
				}));

				Button useProxy = new Button(portButtonComposite, SWT.CHECK);
				useProxy.setText("使用代理");
				useProxy.setSelection(GuiConfig.isUseProxy());
				useProxy.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
				useProxy.addSelectionListener(new ControlSelectionListener(ev -> {
					GuiConfig.setUseProxy(useProxy.getSelection());
					if (this.patcher != null) this.printChangeAgentPort("更换配置: ");
				}));
			}
		}

		if (ZHIYONG) this.initPatchGroup();

		this.console = new org.eclipse.swt.widgets.List(this.composite, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		this.console.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		if (message != null) this.console.add(message);

		this.trayItem = new TrayItem(this.display.getSystemTray(), SWT.NONE);
		this.trayItem.setImage(this.logo);
		this.trayItem.addListener(SWT.Selection, ev -> {
			this.shell.setVisible(true);
			this.shell.setMinimized(false);
		});
		this.trayItem.addMenuDetectListener(new TrayItemMenuListener());
	}

	private void initPatchGroup() {
		Group handlerGroup = new Group(this.composite, SWT.NONE);
		handlerGroup.setText("补丁");
		handlerGroup.setLayout(new GridLayout(2, false));
		handlerGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		{
			Button allcg = new Button(handlerGroup, SWT.CHECK);
			allcg.setText("全CG");
			allcg.setToolTipText("需要重启游戏");
			allcg.setSelection(GuiConfig.isAllCG());
			allcg.addSelectionListener(new ControlSelectionListener(ev -> GuiConfig.setAllCG(allcg.getSelection())));

			Composite tihuanComposite = new Composite(handlerGroup, SWT.NONE);
			tihuanComposite.setLayout(new GridLayout(1, false));
			tihuanComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
			{
				Button tihuanButton = new Button(tihuanComposite, SWT.CHECK);
				tihuanButton.setText("替换副团长");
				tihuanButton.setToolTipText("需要重启游戏");
				tihuanButton.setSelection(GuiConfig.isTihuan());
				tihuanButton.addSelectionListener(new ControlSelectionListener(ev -> GuiConfig.setTihuan(tihuanButton.getSelection())));

				Text futuanzhangIDText = new Text(tihuanComposite, SWT.CENTER);
				futuanzhangIDText.setText(String.valueOf(GuiConfig.getFutuanzhangID()));
				futuanzhangIDText.setToolTipText("副团长ID");
				futuanzhangIDText.setLayoutData(new GridData(GridData.FILL_BOTH));
				futuanzhangIDText.addModifyListener(ev -> {
					try {
						GuiConfig.setFutuanzhangID(Integer.parseInt(futuanzhangIDText.getText()));
					} catch (Exception ev1) {
						this.printMessage("副团长ID只能有数字");
						GuiConfig.setFutuanzhangID(null);
					}
				});
			}
		}
	}

	private void printChangeAgentPort(String profix) {
		String proxyStringForConsole = this.patcher == null ? "" : (this.patcher.getPort1() + "→" + (GuiConfig.isUseProxy() ? this.patcher.getPort2() : "直连"));
		this.printMessage(profix + proxyStringForConsole);
	}

	private void printMessage(String message) {
		if (this.console.isDisposed()) return;
		if (this.console.getItemCount() >= 200) this.console.remove(0);

		this.console.add(CONSOLE_TIME_FORMAT.format(new Date()) + " " + message);
		this.console.setSelection(this.console.getItemCount() - 1);
	}

	private void dispose() {
		if (FKGGui.this.patcher != null) {
			try {
				this.patcher.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (FKGGui.this.shower != null) {
			this.shower.dispose();
		}
		this.logo.dispose();
		this.trayItem.dispose();
	}

	private void display() {
		this.shell.open();
		while (this.shell.isDisposed() == false) {
			if (this.display.readAndDispatch() == false) {
				this.display.sleep();
			}
		}
	}

	public class ControlSelectionListener extends SelectionAdapter {
		private final Consumer<SelectionEvent> handler;

		public ControlSelectionListener(Consumer<SelectionEvent> handler) {
			this.handler = handler;
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
			dispose.addSelectionListener(new ControlSelectionListener(ev -> FKGGui.this.shell.close()));
		}

		@Override
		public void menuDetected(MenuDetectEvent e) {
			this.menu.setVisible(true);
		}

	}

}
