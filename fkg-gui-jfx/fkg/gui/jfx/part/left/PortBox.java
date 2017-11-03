package fkg.gui.jfx.part.left;

import java.util.function.IntConsumer;
import java.util.function.UnaryOperator;

import fkg.config.AppConfig;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.HBox;
import javafx.util.converter.IntegerStringConverter;
import tool.FXUtils;
import tool.function.FunctionUtils;

public class PortBox extends HBox {
	private final Spinner<Integer> listenerPort = this.createPortSpinner(AppConfig.getServerPort(), AppConfig::setServerPort);
	private final CheckBox useProxy = new CheckBox("使用代理");
	private final Spinner<Integer> proxyPort = this.createPortSpinner(AppConfig.getAgentPort(), AppConfig::setAgentPort);

	public PortBox() {
		this.useProxy.setSelected(AppConfig.isUseProxy());
		this.useProxy.selectedProperty().addListener((source, oldValue, newValue) -> AppConfig.setUseProxy(newValue));

		this.setSpacing(2);
		this.setAlignment(Pos.CENTER);
		this.setBorder(FXUtils.createNewBorder());
		this.getChildren().addAll(this.listenerPort, FXUtils.createVBox(2, Pos.CENTER, false, this.useProxy, this.proxyPort));
	}

	private Spinner<Integer> createPortSpinner(int defaultValue, IntConsumer changeHandler) {
		int min = 1, max = 65535, initial = 1;
		Spinner<Integer> spinner = new Spinner<>(min, max, initial);
		spinner.setEditable(true);
		FunctionUtils.use(spinner.getEditor(), editor -> {
			editor.textProperty().addListener((source, oldValue, newValue) -> {
				changeHandler.accept(Integer.parseInt(newValue));
			});
		}, editor -> {
			editor.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), defaultValue, new UnaryOperator<Change>() {
				@Override
				public Change apply(Change change) {
					Integer value;

					try {
						value = Integer.parseInt(change.getControlNewText());
					} catch (Exception ex) {
						value = null;
					}

					if (value != null) {
						if (value >= min && value <= max) {
							return change;
						}
					}
					return null;
				}
			}));
		});
		return spinner;
	}
}
