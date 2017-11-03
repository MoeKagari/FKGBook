package fkg.gui.jfx.part.left;

import fkg.config.AppConfig;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import tool.FXUtils;

public class LoadBox extends HBox {
	private final RadioButton lazyLoad = new RadioButton("懒加载");
	private final RadioButton preLoad = new RadioButton("预加载");

	public LoadBox() {
		ToggleGroup group = new ToggleGroup();
		String tootip = "预加载图片 : 软件占用内存大,读写硬盘次数少\n懒加载图片 : 软件占用内存小,读写硬盘次数多\n重启才有效";

		this.lazyLoad.setSelected(AppConfig.isLazyLoad());
		this.lazyLoad.setToggleGroup(group);
		this.lazyLoad.setTooltip(new Tooltip(tootip));

		this.preLoad.setSelected(AppConfig.isLazyLoad() == false);
		this.preLoad.setToggleGroup(group);
		this.preLoad.setTooltip(new Tooltip(tootip));

		group.selectedToggleProperty().addListener((source, oldValue, newValue) -> {
			AppConfig.setLazyLoad(this.lazyLoad.isSelected());
		});

		this.setSpacing(4);
		this.setAlignment(Pos.CENTER);
		this.setBorder(FXUtils.createNewBorder());
		this.getChildren().addAll(this.preLoad, this.lazyLoad);
	}
}
