package fkg.gui.jfx;

import java.util.function.IntConsumer;
import java.util.function.UnaryOperator;

import fkg.config.AppConfig;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import tool.FXUtils;

public class LeftOptionPane extends VBox {
	public final LoadBox loadBox = new LoadBox();
	public final PortBox portBox = new PortBox();
	public final Button confirmButton = new Button("更改");
	public final PatchBox patchBox = new PatchBox();

	public LeftOptionPane(ApplicationMain main) {
		this.confirmButton.setOnAction(ev -> {
			try {
				ApplicationMain.MainStart.server.restart();

				Alert alert = new Alert(AlertType.NONE, "代理服务器重启动成功", ButtonType.CLOSE);
				alert.setTitle("");

				//使 alert 居 stage 中央
				double[] location = FXUtils.getStageCenterLocation(main.primaryStage);
				//alert 的 size [376,103]
				//alert显示之前 不能直接由alert获取
				//随着contentText 的长度变化 ,可能会有偏差
				alert.setX(location[0] - 376 / 2);
				alert.setY(location[1] - 103 / 2);

				alert.showAndWait();
			} catch (Exception e) {
				FXUtils.showExceptionDialog(e);
			}
		});

		this.setMinWidth(180);
		this.setMaxWidth(180);
		this.setSpacing(20);
		this.setAlignment(Pos.TOP_CENTER);
		this.setPadding(new Insets(0, 0, 0, 0));
		this.getChildren().addAll(
				//FXUtils.createVBox(0, Pos.CENTER, false, new Label("加载模式(图片)"), this.loadBox),
				FXUtils.createVBox(0, Pos.CENTER, false, new Label("端口"), this.portBox, this.confirmButton),
				FXUtils.createVBox(0, Pos.CENTER, false, new Label("补丁"), this.patchBox)
		/**/);
	}

	public class LoadBox extends HBox {
		public final RadioButton lazyLoad = new RadioButton("懒加载");
		public final RadioButton preLoad = new RadioButton("预加载");

		public LoadBox() {
			ToggleGroup group = new ToggleGroup();
			this.lazyLoad.setToggleGroup(group);
			this.preLoad.setToggleGroup(group);
			if (AppConfig.isLazyLoad()) {
				this.lazyLoad.setSelected(true);
			} else {
				this.preLoad.setSelected(true);
			}
			this.lazyLoad.selectedProperty().addListener((source, oldValue, newValue) -> {
				AppConfig.setLazyLoad(this.lazyLoad.isSelected());
			});
			this.preLoad.selectedProperty().addListener((source, oldValue, newValue) -> {
				AppConfig.setLazyLoad(this.lazyLoad.isSelected());
			});
			this.lazyLoad.setTooltip(new Tooltip("预加载图片 : 软件占用内存大,读写硬盘次数少\n懒加载图片 : 软件占用内存小,读写硬盘次数多\n重启才有效"));
			this.preLoad.setTooltip(new Tooltip("预加载图片 : 软件占用内存大,读写硬盘次数少\n懒加载图片 : 软件占用内存小,读写硬盘次数多\n重启才有效"));

			this.setSpacing(4);
			this.setAlignment(Pos.CENTER);
			this.setBorder(FXUtils.createNewBorder());
			this.getChildren().addAll(this.preLoad, this.lazyLoad);
		}
	}

	public class PatchBox extends HBox {
		public final CheckBox allCG = new CheckBox("全CG");
		public final CheckBox replace = new CheckBox("替换副团长");
		public final TextField futuanzhangID = new TextField("154301");

		public PatchBox() {
			this.setSpacing(15);
			this.setAlignment(Pos.CENTER);
			this.setBorder(FXUtils.createNewBorder());
			{
				this.allCG.setSelected(AppConfig.isAllCG());
				this.allCG.selectedProperty().addListener((source, oldValue, newValue) -> AppConfig.setAllCG(newValue));
				HBox.setHgrow(this.allCG, Priority.ALWAYS);

				this.replace.setSelected(AppConfig.isTihuan());
				this.replace.selectedProperty().addListener((source, oldValue, newValue) -> AppConfig.setTihuan(newValue));

				this.futuanzhangID.setMinWidth(80);
				this.futuanzhangID.setMaxWidth(80);
				this.futuanzhangID.setText(String.valueOf(AppConfig.getFutuanzhangID()));
				this.futuanzhangID.textProperty().addListener((source, oldValue, newValue) -> {
					try {
						AppConfig.setFutuanzhangID(Integer.parseInt(newValue));
					} catch (Exception ex) {
						this.futuanzhangID.setText(oldValue);
					}
				});
			}
			this.getChildren().addAll(
					FXUtils.createHBox(0, Pos.CENTER_LEFT, false, this.allCG),
					FXUtils.createVBox(2, Pos.CENTER, false, this.replace, this.futuanzhangID)//
			);
		}
	}

	public class PortBox extends HBox {
		public final Spinner<Integer> listenerPort = this.createPortSpinner(AppConfig.getServerPort(), AppConfig::setServerPort);
		public final CheckBox useProxy = new CheckBox("使用代理");
		public final Spinner<Integer> proxyPort = this.createPortSpinner(AppConfig.getAgentPort(), AppConfig::setAgentPort);

		public PortBox() {
			this.useProxy.setSelected(AppConfig.isUseProxy());
			this.useProxy.selectedProperty().addListener((source, oldValue, newValue) -> AppConfig.setUseProxy(newValue));

			this.setSpacing(2);
			this.setAlignment(Pos.CENTER);
			this.setBorder(FXUtils.createNewBorder());
			this.getChildren().addAll(this.listenerPort, FXUtils.createVBox(2, Pos.CENTER, false, this.useProxy, this.proxyPort));
		}

		private Spinner<Integer> createPortSpinner(int defaultValue, IntConsumer change) {
			int min = 1, max = 65535, initial = 1;
			Spinner<Integer> spinner = new Spinner<>(min, max, initial);
			spinner.setEditable(true);

			TextField editor = spinner.getEditor();
			editor.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), defaultValue, new UnaryOperator<Change>() {
				@Override
				public Change apply(Change t) {
					Integer value;

					try {
						value = Integer.parseInt(t.getControlNewText());
					} catch (Exception ex) {
						value = null;
					}

					if (value != null) {
						if (value >= min && value <= max) {
							return t;
						}
					}
					return null;
				}
			}));
			editor.textProperty().addListener((source, oldValue, newValue) -> {
				if (oldValue != newValue) {
					change.accept(Integer.parseInt(newValue));
				}
			});

			return spinner;
		}
	}
}
