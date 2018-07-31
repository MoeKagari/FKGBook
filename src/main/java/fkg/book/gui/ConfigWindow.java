package fkg.book.gui;

import com.moekagari.tool.acs.ArrayUtils;
import com.moekagari.tool.function.BooleanConsumer;
import com.moekagari.tool.lambda.LambdaUtils;
import com.moekagari.tool.other.FXUtils;
import fkg.book.main.FKGBOOKApplicationMain;
import fkg.book.main.FKGBOOKMainConfig;
import fkg.book.masterdata.GetMasterData;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.IntegerStringConverter;

import java.util.List;
import java.util.function.IntConsumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class ConfigWindow extends Stage {
	private final static Insets TAB_NODE_PADDING = new Insets(5, 5, 0, 5);

	ConfigWindow(FKGBOOKWindow window) {
		this.initOwner(window.getStage());
		this.initModality(Modality.APPLICATION_MODAL);
		this.initStyle(StageStyle.UTILITY);

		this.setTitle("设置");
		this.setResizable(false);
		this.setScene(new Scene(
				FXUtils.createTabPane(
						tabPane -> {
							tabPane.setTabMinWidth(50);
							tabPane.setTabMaxWidth(50);
							tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
						},
						new Tab("网络", new NetConfigBox(this)),
						new Tab("补丁", new PatchConfigBox())
						/**/
				),
				480, 270
				/**/
		));
	}

	private static CheckBox createCheckBox(String text, boolean defaultValue, BooleanConsumer changeHandler) {
		CheckBox checkBox = new CheckBox(text);
		checkBox.setSelected(defaultValue);
		checkBox.selectedProperty().addListener(FXUtils.makeChangeListener((oldValue, newValue) -> {
			changeHandler.accept(newValue.booleanValue());
		}));
		return checkBox;
	}

	private static Spinner<Integer> createPortSpinner(int defaultValue, IntConsumer changeHandler) {
		int min = 1, max = 65535, initial = 1;
		Spinner<Integer> spinner = new Spinner<>(min, max, initial);
		spinner.setEditable(true);
		LambdaUtils.init(
				spinner.getEditor(),
				editor -> {
					editor.textProperty().addListener((source, oldValue, newValue) -> {
						changeHandler.accept(Integer.parseInt(newValue));
					});
					editor.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), defaultValue, new UnaryOperator<Change>() {
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
				}
				/**/
		);
		return spinner;
	}

	static class PatchConfigBox extends VBox {
		public PatchConfigBox() {
			this.setSpacing(3);
			this.setAlignment(Pos.TOP_LEFT);
			this.setPadding(TAB_NODE_PADDING);
			this.getChildren().addAll(
					createCheckBox(
							"全CG",
							FKGBOOKApplicationMain.getMainConfig().isUseAllcgPatch(),
							FKGBOOKApplicationMain.getMainConfig()::setUseAllcgPatch
							/**/
					),
					FXUtils.createHBox(
							3, Pos.CENTER_LEFT, false,
							createCheckBox(
									"替换副团长",
									FKGBOOKApplicationMain.getMainConfig().isReplaceDeputyLeader(),
									FKGBOOKApplicationMain.getMainConfig()::setReplaceDeputyLeader
									/**/
							),
							LambdaUtils.init(new ComboBox<Integer>(), comboBox -> {
								List<Integer> items = GetMasterData.ALL_CHARA.values().stream()
								                                             .filter(AbstractCharaData::canShowInApplication)
								                                             .filter(AbstractCharaData::canBeSetDeputyLeader)
								                                             .mapToInt(AbstractCharaData::getId)
								                                             .sorted()
								                                             .boxed()
								                                             .collect(Collectors.toList());
								comboBox.setItems(FXCollections.observableArrayList(items));

								Integer targetDeputyLeaderId = Integer.valueOf(FKGBOOKApplicationMain.getMainConfig().getTargetDeputyLeaderId());
								if(!comboBox.getItems().contains(targetDeputyLeaderId)) {
									targetDeputyLeaderId = Integer.valueOf(FKGBOOKMainConfig.DEFAULT_DEPUTYLEADERID);
								}
								comboBox.getSelectionModel().select(targetDeputyLeaderId);
								comboBox.getSelectionModel().selectedItemProperty().addListener(FXUtils.makeChangeListener((oldValue, newValue) -> {
									FKGBOOKApplicationMain.getMainConfig().setTargetDeputyLeaderId(newValue.intValue());
								}));
							})
							/**/
					),
					FXUtils.createVBox(
							2, Pos.CENTER_LEFT, true,
							box -> box.setPadding(new Insets(2)),
							createCheckBox(
									"跳过剧情(总开关)",
									FKGBOOKApplicationMain.getMainConfig().isSkipEvent(),
									FKGBOOKApplicationMain.getMainConfig()::setSkipEvent
									/**/
							),
							FXUtils.createGridPane(gridPane -> {
								int colNumber = 2;
								gridPane.getColumnConstraints().add(FXUtils.createColumnConstraintsPercent(100 / colNumber));
								gridPane.getColumnConstraints().add(FXUtils.createColumnConstraintsPercent(100 / colNumber));

								CheckBox[] checkBoxArray = {
										createCheckBox(
												"表区版Hscene的剧情",
												FKGBOOKApplicationMain.getMainConfig().getSkipEventConfig().isSkipEventHscene(),
												FKGBOOKApplicationMain.getMainConfig().getSkipEventConfig()::setSkipEventHscene
												/**/
										),
										createCheckBox(
												"里区版Hscene的剧情",
												FKGBOOKApplicationMain.getMainConfig().getSkipEventConfig().isSkipEventHsceneR18(),
												FKGBOOKApplicationMain.getMainConfig().getSkipEventConfig()::setSkipEventHsceneR18
												/**/
										),
										createCheckBox(
												"新角色时的个人介绍",
												FKGBOOKApplicationMain.getMainConfig().getSkipEventConfig().isSkipEventNew(),
												FKGBOOKApplicationMain.getMainConfig().getSkipEventConfig()::setSkipEventNew
												/**/
										),
										createCheckBox(
												"某些地图里面战斗中出现的剧情",
												FKGBOOKApplicationMain.getMainConfig().getSkipEventConfig().isSkipEventPerformance(),
												FKGBOOKApplicationMain.getMainConfig().getSkipEventConfig()::setSkipEventPerformance
												/**/
										),
										createCheckBox(
												"地图的剧情",
												FKGBOOKApplicationMain.getMainConfig().getSkipEventConfig().isSkipEventStory(),
												FKGBOOKApplicationMain.getMainConfig().getSkipEventConfig()::setSkipEventStory
												/**/
										),
										createCheckBox(
												"引导剧情",
												FKGBOOKApplicationMain.getMainConfig().getSkipEventConfig().isSkipEventTutorial(),
												FKGBOOKApplicationMain.getMainConfig().getSkipEventConfig()::setSkipEventTutorial
												/**/
										)
								};
								ArrayUtils.forEachWithIndex(checkBoxArray, (index, checkBox) -> {
									GridPane.setConstraints(checkBox, index % colNumber, index / colNumber);
								});
								gridPane.getChildren().addAll(checkBoxArray);
							})
							/**/
					)
					/**/
			);
		}
	}

	static class NetConfigBox extends VBox {
		public NetConfigBox(Stage parent) {
			this.setSpacing(3);
			this.setAlignment(Pos.TOP_LEFT);
			this.setPadding(TAB_NODE_PADDING);
			this.getChildren().addAll(
					FXUtils.createHBox(
							3, Pos.CENTER_LEFT, false,
							new Label("监听端口"), new Label(":"),
							createPortSpinner(
									FKGBOOKApplicationMain.getServerConfig().getListenPort(),
									FKGBOOKApplicationMain.getServerConfig()::setListenPort
									/**/
							)
							/**/
					),
					createCheckBox(
							"使用代理",
							FKGBOOKApplicationMain.getServerConfig().isUseProxy(),
							FKGBOOKApplicationMain.getServerConfig()::setUseProxy
							/**/
					),
					FXUtils.createHBox(
							3, Pos.CENTER_LEFT, false,
							new Label("代理端口"), new Label(":"),
							createPortSpinner(
									FKGBOOKApplicationMain.getServerConfig().getProxyPort(),
									FKGBOOKApplicationMain.getServerConfig()::setProxyPort
									/**/
							)
							/**/
					),
					FXUtils.createButton("重启", ev -> {
						try {
							FKGBOOKApplicationMain.getServer().restart();
							{
								Alert alert = new Alert(AlertType.NONE, "代理服务器重启成功", ButtonType.CLOSE);
								alert.initOwner(parent);
								alert.showAndWait();
							}
						} catch(Exception e) {
							FXUtils.showExceptionDialog(parent, e);
						}
					})
					/**/
			);
		}
	}
}
