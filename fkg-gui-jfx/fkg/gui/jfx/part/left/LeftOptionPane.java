package fkg.gui.jfx.part.left;

import fkg.gui.jfx.ApplicationMain;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import tool.FXUtils;

public class LeftOptionPane extends VBox {
	public final LoadBox loadBox = new LoadBox();
	public final PortBox portBox = new PortBox();
	public final Button confirmButton = new Button("更改");
	public final PatchBox patchBox = new PatchBox();

	public LeftOptionPane(ApplicationMain main) {
		this.confirmButton.setOnAction(ev -> {
			try {
				ApplicationMain.server.restart();
				Alert alert = new Alert(AlertType.NONE, "代理服务器重启动成功", ButtonType.CLOSE);
				alert.initOwner(main.primaryStage);
				alert.showAndWait();
			} catch (Exception e) {
				FXUtils.showExceptionDialog(e);
			}
		});

		this.setMinWidth(180);
		this.setMaxWidth(180);
		this.setSpacing(20);
		this.setAlignment(Pos.BOTTOM_LEFT);
		this.setPadding(new Insets(0, 0, 0, 0));
		this.getChildren().addAll(
				FXUtils.createVBox(0, Pos.CENTER, false, new Label("懒加载图片"), this.loadBox),
				FXUtils.createVBox(0, Pos.CENTER, false, new Label("端口"), this.portBox, this.confirmButton),
				FXUtils.createVBox(0, Pos.CENTER, false, new Label("补丁"), this.patchBox)
		/**/);
	}
}
