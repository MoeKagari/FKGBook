package fkg.book.other.sd;

import fkg.book.other.sd.AnimatedSDStage.AnimatedSDStageStateChangeListener;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class AnimatedSDStageManager {
	private final Stage parent;
	private final List<AnimatedSDStage> children = new ArrayList<>();

	public AnimatedSDStageManager(Stage primaryStage) {
		this.parent = primaryStage;
		//仅作 父级 用 , 此style无任务栏
		this.parent.initStyle(StageStyle.UTILITY);
		//移动到可见区域外
		this.parent.setX(Double.MAX_VALUE);
		this.parent.setY(Double.MAX_VALUE);
		this.parent.setTitle("父级(不可见)");

		for(File sdDirectory : new File("resources\\animated_sd").listFiles()) {
			if(sdDirectory.isDirectory()) {
				String sdDirectoryPath = sdDirectory.getPath();
				AnimatedSDStage child = new AnimatedSDStage(
						primaryStage,
						sdDirectory.getName(),
						IntStream.range(0, 1000)
						         .mapToObj(index -> sdDirectoryPath + "\\" + index + ".png")
						         .map(File::new)
						         .filter(File::exists)
						         .map(File::toURI)
						         .map(URI::toString)
						         .map(Image::new)
						         .toArray(Image[]::new)
						/**/
				);
				child.addAnimatedSDStageStateChangeListener(new AnimatedSDStageStateChangeListener() {
					public @Override void hideAfter() {
						if(AnimatedSDStageManager.this.children.stream().noneMatch(AnimatedSDStage::isShowing)) {
							AnimatedSDStageManager.this.parent.close();//全部 sd(child) close之后,parent close
							//所有stage close之后,程序自动退出
						}
					}
				});
				this.children.add(child);
			}
		}
	}

	public void show() {
		this.parent.show();
		this.children.forEach(AnimatedSDStage::show);
	}
}
