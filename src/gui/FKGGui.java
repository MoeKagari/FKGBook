package gui;

import java.io.IOException;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.ModifyListener;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TrayItem;

import patch.FKGPatcher;
import show.ShowFlowerInformation;

public class FKGGui {

	public static FKGGui gui;

	public static void main(String[] args) {
		gui = new FKGGui();
		gui.display();
	}

	private Image logo = new Image(this.display, this.getClass().getResourceAsStream("/icon.png"));
	public static final SimpleDateFormat CONSOLE_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	private Display display = Display.getDefault();
	private Shell shell;
	private Composite composite;
	private org.eclipse.swt.widgets.List console;

	private Integer futuanzhangID = GuiConfig.getFutuanzhangID();
	private FKGPatcher patcher = null;
	private ShowFlowerInformation shower = null;
	private TrayItem trayItem;

	public FKGGui() {
		this.shell = new Shell(this.display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		this.shell.setImage(this.logo);
		this.shell.setLayout(new GridLayout(1, false));
		this.shell.setLayoutData(new GridData(GridData.FILL_BOTH));
		this.shell.setSize(260, 400);
		this.shell.setText("美少女花骑士");
		this.shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent arg0) {
				if (FKGGui.this.patcher != null) {
					try {
						FKGGui.this.patcher.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (FKGGui.this.shower != null) {
					FKGGui.this.shower.dispose();
				}
				FKGGui.this.logo.dispose();
				FKGGui.this.trayItem.dispose();
			}

			@Override
			public void shellIconified(ShellEvent arg0) {
				FKGGui.this.shell.setVisible(false);
			}
		});

		this.composite = new Composite(this.shell, SWT.NONE);
		this.composite.setLayout(new GridLayout(2, false));
		this.composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Group portGroup = new Group(this.composite, SWT.NONE);
		portGroup.setText("端口");
		portGroup.setLayout(new GridLayout(2, false));
		portGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		{
			Text serverPort = new Text(portGroup, SWT.CENTER);
			serverPort.setText(String.valueOf(2222));
			serverPort.setToolTipText("监听端口");
			serverPort.setLayoutData(new GridData(GridData.FILL_BOTH));
			serverPort.addModifyListener(this.getIntegerTextListener(serverPort, "监听端口只能为数字"));

			Text agentPort = new Text(portGroup, SWT.CENTER);
			agentPort.setText(String.valueOf(1080));
			agentPort.setToolTipText("代理端口");
			agentPort.setLayoutData(new GridData(GridData.FILL_BOTH));
			agentPort.addModifyListener(this.getIntegerTextListener(agentPort, "代理端口只能为数字"));

			Button apply = new Button(portGroup, SWT.PUSH);
			apply.setText("应用");
			apply.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
			apply.addSelectionListener(new ControlSelectionListener(ev -> {
				try {
					int port1 = Integer.parseInt(serverPort.getText());
					int port2 = Integer.parseInt(agentPort.getText());

					if (this.patcher == null || this.patcher.getPort1() != port1) {
						if (this.patcher != null) {
							this.patcher.close();
							this.printMessage("关闭成功");
						}
						this.patcher = new FKGPatcher(new ServerSocket(port1), port2);
						this.patcher.start();
						this.printMessage("开启成功: " + port1 + "→" + port2);
					} else if (this.patcher.getPort2() != port2) {
						this.patcher.setPort2(port2);
						this.printMessage("更换代理端口成功");
					}
				} catch (Exception ev1) {
					this.printMessage((this.patcher == null ? "开启" : "更换配置") + "失败");
					return;
				}
			}));

			Button cancel = new Button(portGroup, SWT.PUSH);
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
		}

		Button allFKG = new Button(this.composite, SWT.PUSH);
		allFKG.setText("所有花娘");
		allFKG.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 2));
		allFKG.addSelectionListener(new ControlSelectionListener(ev -> {
			if (this.shower == null) this.shower = new ShowFlowerInformation();
			this.shower.display();
			this.shower.setFocus();
		}));

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
						this.futuanzhangID = Integer.parseInt(futuanzhangIDText.getText());
					} catch (Exception ev1) {
						this.printMessage("副团长ID只能有数字");
						this.futuanzhangID = null;
					}
				});
			}
		}

		this.console = new org.eclipse.swt.widgets.List(this.composite, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		this.console.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		this.trayItem = new TrayItem(this.display.getSystemTray(), SWT.NONE);
		this.trayItem.setImage(this.logo);
		this.trayItem.addListener(SWT.Selection, ev -> {
			this.shell.setVisible(true);
			this.shell.setMinimized(false);
		});
		this.trayItem.addMenuDetectListener(new TrayItemMenuListener());
	}

	private ModifyListener getIntegerTextListener(Text control, String mexxage) {
		return ev -> {
			try {
				Integer.parseInt(control.getText());
			} catch (Exception ev1) {
				this.printMessage(mexxage);
			}
		};
	}

	public void display() {
		this.shell.open();
		while (this.shell.isDisposed() == false) {
			if (this.display.readAndDispatch() == false) {
				this.display.sleep();
			}
		}
	}

	public Integer getFutuanzhangID() {
		return this.futuanzhangID;
	}

	private void printMessage(String message) {
		if (this.console.isDisposed()) return;
		if (this.console.getItemCount() >= 200) this.console.remove(0);

		this.console.add(CONSOLE_TIME_FORMAT.format(new Date()) + " " + message);
		this.console.setSelection(this.console.getItemCount() - 1);
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
