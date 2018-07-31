package fkg.book.other.icon;

import com.moekagari.tool.other.FXUtils;
import com.moekagari.tool.other.FXUtils.FXResourceRadioButton;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.UnaryOperator;

public class FKGIconGenerator {
	private final static int ICON_SIZE = 100;
	private final Stage primaryStage;

	public FKGIconGenerator(Stage stage) {
		this.primaryStage = stage;
		this.primaryStage.setTitle("花骑士头像生成器");
		this.primaryStage.setResizable(false);
		this.primaryStage.setOnCloseRequest(ev -> {
			if(FXUtils.showCloseConfirmationWindow(this.primaryStage)) {

			} else {
				ev.consume();
			}
		});
		this.primaryStage.setScene(new Scene(this.createContent(), 800, 600));
	}

	private Parent createContent() {
		StackPane mainPane = new StackPane();
		mainPane.setAlignment(Pos.CENTER);

		/* ------------------------------------------------------------------ */

		VBox optionBox = FXUtils.createVBox(5, Pos.TOP_CENTER, false);
		FXUtils.setMinMaxWidth(optionBox, 85);
		StackPane.setAlignment(optionBox, Pos.TOP_LEFT);

		/* ------------------------------------------------------------------ */

		ToggleGroup backgroundGroup = new ToggleGroup();
		List<FXResourceRadioButton<Image>> backgroundButtons = this.createDecorationButtons(6, "background", index -> index == 0 ? "空" : "★★★★★★★★★★".substring(0, index), backgroundGroup, 6);
		ImageView backgroundImageView = new ImageView(((FXResourceRadioButton<Image>) backgroundGroup.getSelectedToggle()).getResource());
		backgroundGroup.selectedToggleProperty().addListener((source, oldValue, newValue) -> {
			backgroundImageView.setImage(((FXResourceRadioButton<Image>) newValue).getResource());
		});
		optionBox.getChildren().add(FXUtils.createVBox(1, Pos.TOP_LEFT, true, box -> {
			box.getChildren().add(FXUtils.createHBox(0, Pos.CENTER, false, new Label("背景")));
			box.getChildren().addAll(backgroundButtons);
		}));

		/* ------------------------------------------------------------------ */

		ToggleGroup frameGroup = new ToggleGroup();
		List<FXResourceRadioButton<Image>> frameButtons = this.createDecorationButtons(6, "frame", index -> index == 0 ? "空" : "★★★★★★★★★★".substring(0, index), frameGroup, 6);
		ImageView frameImageView = new ImageView(((FXResourceRadioButton<Image>) frameGroup.getSelectedToggle()).getResource());
		frameGroup.selectedToggleProperty().addListener((source, oldValue, newValue) -> {
			frameImageView.setImage(((FXResourceRadioButton<Image>) newValue).getResource());
		});
		optionBox.getChildren().add(FXUtils.createVBox(1, Pos.TOP_LEFT, true, box -> {
			box.getChildren().add(FXUtils.createHBox(0, Pos.CENTER, false, new Label("框")));
			box.getChildren().addAll(frameButtons);
		}));

		/* ------------------------------------------------------------------ */

		ToggleGroup attributeGroup = new ToggleGroup();
		List<FXResourceRadioButton<Image>> attributeButtons = this.createDecorationButtons(4, "attribute", index -> new String[]{"空", "斩", "打", "突", "魔"}[index], attributeGroup, 0);
		ImageView attributeImageView = new ImageView(((FXResourceRadioButton<Image>) attributeGroup.getSelectedToggle()).getResource());
		attributeGroup.selectedToggleProperty().addListener((source, oldValue, newValue) -> {
			attributeImageView.setImage(((FXResourceRadioButton<Image>) newValue).getResource());
		});
		optionBox.getChildren().add(FXUtils.createVBox(1, Pos.TOP_LEFT, true, box -> {
			box.getChildren().add(FXUtils.createHBox(0, Pos.CENTER, false, new Label("属性")));
			box.getChildren().addAll(attributeButtons);
		}));

		/* ------------------------------------------------------------------ */

		ToggleGroup oebGroup = new ToggleGroup();
		List<FXResourceRadioButton<Image>> oebButtons = this.createDecorationButtons(3, "oeb", index -> new String[]{"空", "原始", "进化", "开花"}[index], oebGroup, 0);
		ImageView oebImageView = new ImageView(((FXResourceRadioButton<Image>) oebGroup.getSelectedToggle()).getResource());
		oebGroup.selectedToggleProperty().addListener((source, oldValue, newValue) -> {
			oebImageView.setImage(((FXResourceRadioButton<Image>) newValue).getResource());
		});
		optionBox.getChildren().add(FXUtils.createVBox(1, Pos.TOP_LEFT, true, box -> {
			box.getChildren().add(FXUtils.createHBox(0, Pos.CENTER, false, new Label("状态")));
			box.getChildren().addAll(oebButtons);
		}));

		/* ------------------------------------------------------------------ */

		//用户图像
		ImageView userImageView = new ImageView();

		/* ------------------------------------------------------------------ */

		//阴影
		int shadowSize = 3000;
		WritableImage shadowImage = new WritableImage(shadowSize, shadowSize);
		for(int w = 0; w < shadowSize; w++) {
			for(int h = 0; h < shadowSize; h++) {
				if((w >= (shadowSize - ICON_SIZE) / 2)&&(w < (shadowSize + ICON_SIZE) / 2)&&(h >= (shadowSize - ICON_SIZE) / 2)&&(h < (shadowSize + ICON_SIZE) / 2)) {
					shadowImage.getPixelWriter().setArgb(w, h, 0);
				} else {
					shadowImage.getPixelWriter().setArgb(w, h, 0xD0808080);
				}
			}
		}
		ImageView shadowImageView = new ImageView(shadowImage);

		/* ------------------------------------------------------------------ */

		VBox operationBox = FXUtils.createVBox(5, Pos.TOP_CENTER, false);
		FXUtils.setMinMaxWidth(operationBox, 160);
		StackPane.setAlignment(operationBox, Pos.TOP_RIGHT);
		{
			ScaleBox userImageScaleBox = new ScaleBox(true, "图像缩放", Arrays.asList(userImageView));
			ScaleBox decorationImageScaleBox = new ScaleBox(false, "装饰缩放", Arrays.asList(backgroundImageView, shadowImageView, frameImageView, attributeImageView, oebImageView));

			HBox buttonBox = FXUtils.createHBox(5, Pos.CENTER, false);
			operationBox.getChildren().add(buttonBox);
			{
				Button exportButton = new Button("导出");
				FXUtils.setMinMaxWidth(exportButton, 40);
				exportButton.setOnAction(ev -> {
					try {
						SnapshotParameters params = new SnapshotParameters();
						double blankSize = decorationImageScaleBox.getScaleValue() * ICON_SIZE;
						params.setViewport(new Rectangle2D((mainPane.getWidth() - blankSize) / 2, (mainPane.getHeight() - blankSize) / 2, blankSize, blankSize));
						ImageIO.write(SwingFXUtils.fromFXImage(mainPane.snapshot(params, null), null), "png", new File(String.format("icon_%d.png", System.currentTimeMillis())));
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				});
				buttonBox.getChildren().add(exportButton);

				Button fullScreenButton = new Button("全屏");
				FXUtils.setMinMaxWidth(fullScreenButton, 65);
				fullScreenButton.setOnAction(ev -> {
					if(this.primaryStage.isFullScreen()) {
						this.primaryStage.setFullScreen(false);
					} else {
						this.primaryStage.setFullScreen(true);
					}
				});
				this.primaryStage.fullScreenProperty().addListener((source, oldValue, newValue) -> {
					if(newValue) {
						fullScreenButton.setText("退出全屏");
					} else {
						fullScreenButton.setText("全屏");
					}
				});
				buttonBox.getChildren().add(fullScreenButton);
			}

			operationBox.getChildren().add(userImageScaleBox);
			operationBox.getChildren().add(decorationImageScaleBox);
		}

		/* ------------------------------------------------------------------ */

		//鼠标拖动
		double[] xys = {0, 0, 0, 0};
		mainPane.setOnMousePressed(ev -> {
			xys[0] = userImageView.getTranslateX();
			xys[1] = userImageView.getTranslateY();
			xys[2] = ev.getScreenX();
			xys[3] = ev.getScreenY();
		});
		mainPane.setOnMouseDragged(ev -> {
			if(userImageView.getImage() != null) {
				userImageView.setTranslateX(xys[0] + ev.getScreenX() - xys[2]);
				userImageView.setTranslateY(xys[1] + ev.getScreenY() - xys[3]);
			}
		});

		//拖入图片
		mainPane.setOnDragOver(ev -> ev.acceptTransferModes(TransferMode.ANY));
		mainPane.setOnDragDropped(ev -> {
			try {
				List<File> files = ev.getDragboard().getFiles();
				if(files != null&&files.size() == 1) {
					userImageView.setImage(SwingFXUtils.toFXImage(ImageIO.read(files.get(0)), null));
				} else {
					if(files == null) {
						System.out.println("files == null");
					} else {
						System.out.println("files.size() != 1");
					}
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		});

		mainPane.getChildren().addAll(
				backgroundImageView,
				userImageView,
				shadowImageView,
				frameImageView,
				attributeImageView,
				oebImageView,

				optionBox,
				operationBox
				/**/
		);
		return mainPane;
	}

	private class LevelBox extends VBox {
		private final VBox levelTextBox;
		private final Label levelTextLabel;

		public LevelBox() {
			{
				this.levelTextBox = new VBox();
				this.levelTextBox.setAlignment(Pos.BOTTOM_CENTER);
				FXUtils.setMinMaxSize(this.levelTextBox, ICON_SIZE, ICON_SIZE);
				StackPane.setAlignment(this.levelTextBox, Pos.CENTER);

				this.levelTextLabel = new Label("Lv.80");
				this.levelTextLabel.setFont(Font.font("微软雅黑", FontPosture.REGULAR, 15));
			}

			this.setSpacing(5);
			this.setAlignment(Pos.CENTER);
			this.setBorder(FXUtils.createNewBorder());

			RadioButton levelLabelButton = new RadioButton("等级");
			this.getChildren().add(FXUtils.createHBox(0, Pos.CENTER, false, levelLabelButton));
			levelLabelButton.selectedProperty().addListener((source, oldValue, newValue) -> {
				if(newValue) {
					this.levelTextBox.getChildren().setAll(this.levelTextLabel);
				} else {
					this.levelTextBox.getChildren().setAll();
				}
			});

			HBox fontConfigBox = new HBox(2);
			this.getChildren().add(fontConfigBox);
			{
				ComboBox<String> fontComboBox = new ComboBox<>();
				fontConfigBox.getChildren().add(fontComboBox);
				fontComboBox.getItems().addAll(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
				fontComboBox.getSelectionModel().select("微软雅黑");

				int min = 1, max = 100, initial = 1;
				Spinner<Integer> fontSizeSpinner = new Spinner<>(min, max, initial);
				fontConfigBox.getChildren().add(fontSizeSpinner);
				fontSizeSpinner.setEditable(true);
				FXUtils.setMinMaxWidth(fontSizeSpinner, 60);
				fontSizeSpinner.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 15, new UnaryOperator<Change>() {
					@Override
					public Change apply(Change change) {
						Integer value;

						try {
							value = Integer.parseInt(change.getControlNewText());
						} catch(Exception ex) {
							value = null;
						}

						if(value != null) {
							if(value >= min&&value <= max) {
								return change;
							}
						}
						return null;
					}
				}));

				fontComboBox.getSelectionModel().selectedItemProperty().addListener((source, oldValue, newValue) -> {
					fontComboBox.setTooltip(new Tooltip(newValue));
					this.levelTextLabel.setFont(new Font(fontComboBox.getSelectionModel().getSelectedItem(), Integer.parseInt(fontSizeSpinner.getEditor().getText())));
				});
				fontSizeSpinner.getEditor().textProperty().addListener((source, oldValue, newValue) -> {
					this.levelTextLabel.setFont(new Font(fontComboBox.getSelectionModel().getSelectedItem(), Integer.parseInt(fontSizeSpinner.getEditor().getText())));
				});
			}
		}
	}

	private class ScaleBox extends VBox {
		private final TextField scaleValueField;

		public double getScaleValue() {
			return Double.parseDouble(this.scaleValueField.getText());
		}

		public ScaleBox(boolean canResetLocation, String text, List<Node> objects) {
			this.setSpacing(5);
			this.setAlignment(Pos.CENTER);
			this.setBorder(FXUtils.createNewBorder());
			this.getChildren().add(FXUtils.createHBox(0, Pos.CENTER, false, new Label(text)));

			ToggleGroup stepValueGroup = new ToggleGroup();
			VBox stepValuePane = FXUtils.createVBox(1, Pos.CENTER, false);
			this.getChildren().add(stepValuePane);
			{
				Button resetStepValueButton = new Button("重置");
				resetStepValueButton.setOnAction(ev -> {
					objects.forEach(imageView -> {
						imageView.setScaleX(1.00);
						imageView.setScaleY(1.00);
					});
				});

				Node[][] nodess = {
						{new FXResourceRadioButton<>(1.00, String.format("%.2f", 1.00)), FXUtils.createHBox(0, Pos.CENTER, false, box -> HBox.setHgrow(box, Priority.ALWAYS), resetStepValueButton)},
						{
								new FXResourceRadioButton<>(0.10, String.format("%.2f", 0.10)), new FXResourceRadioButton<>(0.20, String.format("%.2f", 0.20)),
								new FXResourceRadioButton<>(0.50, String.format("%.2f", 0.50))
						},
						{
								new FXResourceRadioButton<>(0.01, String.format("%.2f", 0.01)), new FXResourceRadioButton<>(0.02, String.format("%.2f", 0.02)),
								new FXResourceRadioButton<>(0.05, String.format("%.2f", 0.05))
						}
				};
				for(Node[] nodes : nodess) {
					for(Node node : nodes) {
						if(node instanceof FXResourceRadioButton) {
							FXResourceRadioButton<Double> rb = ((FXResourceRadioButton<Double>) node);
							rb.setToggleGroup(stepValueGroup);
							if(rb.getResource() == 1.00) {
								rb.setSelected(true);
							}
						}
					}
					stepValuePane.getChildren().add(FXUtils.createHBox(10, Pos.CENTER_LEFT, false, nodes));
				}
			}

			HBox handlerBox = FXUtils.createHBox(5, Pos.CENTER, false);
			this.getChildren().add(handlerBox);
			{
				this.scaleValueField = new TextField(String.format("%.2f", 1.00));
				this.scaleValueField.setEditable(false);
				objects.get(0).scaleXProperty().addListener((source, oldValue, newValue) -> {
					this.scaleValueField.setText(String.format("%.2f", newValue.doubleValue()));
				});

				Button decreaseButton = new Button("-");
				decreaseButton.setOnAction(ev -> {
					double scaleValue = Double.parseDouble(this.scaleValueField.getText()) - ((FXResourceRadioButton<Double>) stepValueGroup.getSelectedToggle()).getResource();
					if(scaleValue >= 0.01) {
						objects.forEach(imageView -> {
							imageView.setScaleX(scaleValue);
							imageView.setScaleY(scaleValue);
						});
					}
				});

				Button increaseButton = new Button("+");
				increaseButton.setOnAction(ev -> {
					double scaleValue = Double.parseDouble(this.scaleValueField.getText()) + ((FXResourceRadioButton<Double>) stepValueGroup.getSelectedToggle()).getResource();
					objects.forEach(imageView -> {
						imageView.setScaleX(scaleValue);
						imageView.setScaleY(scaleValue);
					});
				});

				handlerBox.getChildren().addAll(decreaseButton, this.scaleValueField, increaseButton);
			}

			if(canResetLocation) {
				Button resetLocationButton = new Button("重置位置");
				resetLocationButton.setOnAction(ev -> {
					objects.forEach(imageView -> {
						imageView.setTranslateX(0);
						imageView.setTranslateY(0);
					});
				});
				this.getChildren().add(resetLocationButton);
			}
		}
	}

	private List<FXResourceRadioButton<Image>> createDecorationButtons(int length, String name, IntFunction<String> text, ToggleGroup group, int defaultButton) {
		List<FXResourceRadioButton<Image>> buttons = new ArrayList<>();
		for(int index = 0; index <= length; index++) {
			BufferedImage bi;
			try {
				bi = ImageIO.read(FKGIconGenerator.class.getResourceAsStream(String.format("/fkg/book/other/icon/decoration/%s_%d.png", name, index)));
			} catch(Exception e) {
				bi = new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_ARGB);
			}

			FXResourceRadioButton<Image> button = new FXResourceRadioButton<>(SwingFXUtils.toFXImage(bi, null), text.apply(index));
			button.setToggleGroup(group);
			if(index == defaultButton) {
				button.setSelected(true);
			}

			buttons.add(button);
		}
		return buttons;
	}

	public static class MainStart extends Application {
		public static void main(String[] args) {
			launch(args);
		}

		@Override
		public void start(Stage primaryStage) throws Exception {
			FKGIconGenerator iconGenerator = new FKGIconGenerator(primaryStage);
			iconGenerator.primaryStage.show();
		}
	}
}
