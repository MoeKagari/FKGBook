package fkg.book.gui;

import com.moekagari.tool.acs.ArrayUtils;
import com.moekagari.tool.acs.ExStreamUtils;
import com.moekagari.tool.lambda.LambdaUtils;
import com.moekagari.tool.other.FXUtils;
import com.moekagari.tool.other.FXUtils.FXResourceRadioButton;
import fkg.book.gui.AbstractCharaData.AttackAttributeFilter;
import fkg.book.gui.AbstractCharaData.CharaFilter;
import fkg.book.gui.AbstractCharaData.CountryFilter;
import fkg.book.gui.AbstractCharaData.RarityFilter;
import fkg.book.gui.AbstractCharaData.SkillTypeFilter;
import fkg.book.masterdata.CharacterLeaderSkillDescription.AbilityInfo;
import fkg.book.masterdata.GetMasterData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ShowDataNode extends BorderPane {
	final CharaFilterPane charaFilterPane;
	final CharaDataTable charaDataTable;

	ShowDataNode(FKGBOOKWindow window) {
		this.charaDataTable = new CharaDataTable();
		this.charaFilterPane = new CharaFilterPane();
		HBox.setHgrow(this.charaFilterPane, Priority.ALWAYS);

		Button configWindowButton = new Button("设置");
		configWindowButton.setOnAction(ev -> new ConfigWindow(window).show());

		this.setTop(FXUtils.createHBox(
				0, Pos.CENTER_LEFT, false,
				box -> BorderPane.setMargin(box, new Insets(4, 4, 4, 0)),
				this.charaFilterPane, configWindowButton
				/**/
		));
		this.setCenter(this.charaDataTable);
		this.updateTable();
		this.charaDataTable.getSelectionModel().selectedItemProperty().addListener(FXUtils.makeChangeListener((oldValue, newValue) -> {
			window.showCharacter(newValue);
		}));
	}

	void selectChara(int targetDeputyLeaderId) {
		this.charaDataTable.getItems().stream()
		                   .filter(item -> item.getId() == targetDeputyLeaderId)
		                   .findFirst()
		                   .ifPresent(futuanzhang -> {
			                   this.charaDataTable.getSelectionModel().select(futuanzhang);
			                   this.charaDataTable.scrollTo(futuanzhang);
		                   });
	}

	void updateTable() {
		ObservableList<AbstractCharaData> items = FXCollections.observableArrayList(
				GetMasterData.ALL_CHARA.values()
				                       .stream()
				                       .filter(AbstractCharaData::canShowInApplication)
				                       .filter(this.charaFilterPane::filter)
				                       .sorted(
						                       Comparator.comparingInt(AbstractCharaData::getBid)
						                                 .reversed()
						                                 .thenComparing(Comparator.comparingInt(AbstractCharaData::getOeb))
						                       /**/)
				                       .collect(Collectors.toList())
				/**/);

		this.charaDataTable.getItems().clear();//改变favor时,需要主动清除
		this.charaDataTable.setItems(items);
	}

	class CharaFilterPane extends VBox {
		final FavorRadioButtonGroup favorRadioButtonGroup;
		final OebCheckBoxGroup oebCheckBoxGroup;
		final FilterComboBoxGroup filterComboBoxGroup;

		boolean filter(AbstractCharaData data) {
			return this.oebCheckBoxGroup.showEventCharaFlagCheckBox.test(data)
					&&
					Stream.concat(this.oebCheckBoxGroup.choiceList.stream(), this.oebCheckBoxGroup.otherChoiceList.stream())
					      .anyMatch(choice -> choice.test(data))
					&&
					this.filterComboBoxGroup.filterComboBoxUnitList.stream()
					                                               .map(fcbu -> fcbu.comboBox.getSelectionModel().getSelectedItem())
					                                               .allMatch(rb -> rb.test(data));
		}

		int getFavorIndex() {
			return this.favorRadioButtonGroup.favorRadioButtonList.stream()
			                                                      .filter(RadioButton::isSelected)
			                                                      .mapToInt(FXResourceRadioButton::getResource)
			                                                      .sum();
		}

		CharaFilterPane() {
			this.favorRadioButtonGroup = new FavorRadioButtonGroup();
			this.oebCheckBoxGroup = new OebCheckBoxGroup();
			this.filterComboBoxGroup = new FilterComboBoxGroup();

			this.setSpacing(4);
			this.getChildren().addAll(this.favorRadioButtonGroup, this.oebCheckBoxGroup, this.filterComboBoxGroup);
		}

		class FavorRadioButtonGroup extends HBox {
			final List<FXResourceRadioButton<Integer>> favorRadioButtonList;

			FavorRadioButtonGroup() {
				ToggleGroup group = new ToggleGroup();

				this.favorRadioButtonList = IntStream.of(0, 1, 2).boxed().map(FXResourceRadioButton<Integer>::new).collect(Collectors.toList());
				this.favorRadioButtonList.forEach(favorRadioButton -> {
					int favorValue = favorRadioButton.getResource().intValue();
					favorRadioButton.setText((2 - favorValue) + "00%");
					favorRadioButton.setSelected(favorValue == 0);
					favorRadioButton.setToggleGroup(group);
				});

				//后加 事件处理器
				group.selectedToggleProperty().addListener(FXUtils.makeChangeListener(ShowDataNode.this::updateTable));

				this.setSpacing(5);
				this.getChildren().addAll(this.favorRadioButtonList);
			}
		}

		class OebCheckBoxGroup extends VBox {
			final ShowEventCharaFlagCheckBox showEventCharaFlagCheckBox;
			final List<OebChoiceCheckBox> choiceList, otherChoiceList;

			OebCheckBoxGroup() {
				this.showEventCharaFlagCheckBox = new ShowEventCharaFlagCheckBox();

				//choice
				OebChoiceCheckBox

						notHaveBloomChoice = new OebChoiceCheckBox("开花(未)", AbstractCharaData::isNotHaveBloom,
						                                           FXUtils.makeTooltip(15, "包含满足以下条件的角色", "1.进化角色", "2.高星并无开花", "3.低星并无升华")
				),

						mostLevel = new OebChoiceCheckBox("最高进化", AbstractCharaData::isMostLevel),

						originalChoice = new OebChoiceCheckBox("原始", chara -> chara.getOeb() == 1),
						evolutionChoice = new OebChoiceCheckBox("进化", chara -> chara.getOeb() == 2),
						kariBloomChoice = new OebChoiceCheckBox("开花(假)", chara -> chara.getOeb() == 3 && chara.isKariBloom()),
						notKariBloomChoice = new OebChoiceCheckBox("开花(真)", chara -> chara.getOeb() == 3 && !chara.isKariBloom()),
						sublimationEvolutionChoice = new OebChoiceCheckBox("升华(进化)", chara -> chara.getOeb() == 99 && !chara.isBloomChara()),
						sublimationBloomChoice = new OebChoiceCheckBox("升华(开花)", chara -> chara.getOeb() == 99 && chara.isBloomChara());

				this.otherChoiceList = ArrayUtils.asList(notHaveBloomChoice, mostLevel);
				this.choiceList = ArrayUtils.asList(
						originalChoice, evolutionChoice, kariBloomChoice,
						notKariBloomChoice, sublimationEvolutionChoice, sublimationBloomChoice
						/**/
				);

				//初始化状态 以及 事件监听
				Stream.concat(this.choiceList.stream(), this.otherChoiceList.stream()).forEach(choice -> {
					boolean isOther = this.otherChoiceList.contains(choice);

					if(!isOther) {
						choice.setSelected(true);//默认选中
					}

					choice.setOnAction(ev -> {
						this.choiceList.forEach(c -> {
							if(isOther) {
								c.setSelected(!choice.isSelected());
							}
						});
						this.otherChoiceList.forEach(oc -> {
							if(oc != choice) {
								oc.setSelected(false);
							}
						});
						ShowDataNode.this.updateTable();
					});
				});

				//shortcut
				OebShortcutButton//
						originalShortcut = new OebShortcutButton("原始角色", originalChoice),
						evolutionShortcut = new OebShortcutButton("进化角色", evolutionChoice),
						bloomShortcut = new OebShortcutButton("开花角色", kariBloomChoice, notKariBloomChoice),
						sublimationShortcut = new OebShortcutButton("升华角色", sublimationEvolutionChoice, sublimationBloomChoice),
						allShortcut = new OebShortcutButton("所有角色", this.choiceList);

				this.setSpacing(3);
				this.setAlignment(Pos.CENTER_LEFT);
				this.setPadding(new Insets(0, 0, 0, 3));
				this.setBorder(FXUtils.createNewBorder(0, 0, 0, 1));
				this.getChildren().addAll(
						FXUtils.createHBox(
								10, Pos.CENTER_LEFT, false,
								this.showEventCharaFlagCheckBox,
								new Separator(Orientation.VERTICAL),
								FXUtils.createHBox(
										3, Pos.CENTER_LEFT, false,
										this.otherChoiceList
										/**/
								),
								new Separator(Orientation.VERTICAL),
								FXUtils.createHBox(
										3, Pos.CENTER_LEFT, false,
										originalShortcut, evolutionShortcut, bloomShortcut, sublimationShortcut, allShortcut
										/**/
								)
								/**/
						),
						FXUtils.createHBox(3, Pos.CENTER_LEFT, false, this.choiceList)
						/**/
				);
			}

			class ShowEventCharaFlagCheckBox extends CheckBox {
				ShowEventCharaFlagCheckBox() {
					this.setText("活动角色");
					this.setAllowIndeterminate(true);
					this.setIndeterminate(true);
					this.setTooltip(FXUtils.makeTooltip(15, "横杠 : 全部角色", "打钩 : 只包含活动角色", "无钩 : 不包含活动角色"));
					this.setOnAction(FXUtils.makeEventHandler(ShowDataNode.this::updateTable));
				}

				boolean test(AbstractCharaData data) {
					return this.isIndeterminate() || (this.isSelected() ? data.isEventChara() : !data.isEventChara());
				}
			}

			class OebShortcutButton extends Button {
				OebShortcutButton(String name, List<OebChoiceCheckBox> targetChoiceList) {
					this(name, targetChoiceList.stream().toArray(OebChoiceCheckBox[]::new));
				}

				OebShortcutButton(String name, OebChoiceCheckBox... targetChoiceArray) {
					this.setText(name);
					this.setOnAction(ev -> {
						Stream.concat(
								OebCheckBoxGroup.this.otherChoiceList.stream(),
								OebCheckBoxGroup.this.choiceList.stream()
								/**/
						).forEach(choice -> choice.setSelected(false));
						ArrayUtils.forEach(targetChoiceArray, choice -> choice.setSelected(true));
						ShowDataNode.this.updateTable();
					});
				}
			}

			class OebChoiceCheckBox extends CheckBox {
				final Predicate<AbstractCharaData> choicePredicate;

				OebChoiceCheckBox(String name, Predicate<AbstractCharaData> choicePredicate) {
					this(name, choicePredicate, null);
				}

				OebChoiceCheckBox(String name, Predicate<AbstractCharaData> choicePredicate, Tooltip tooltip) {
					this.setText(name);
					this.setTooltip(tooltip);
					this.choicePredicate = choicePredicate;
				}

				boolean test(AbstractCharaData data) {
					return this.isSelected() && this.choicePredicate.test(data);
				}
			}
		}

		class FilterComboBoxGroup extends HBox {
			final List<FilterComboBoxUnit> filterComboBoxUnitList;

			FilterComboBoxGroup() {
				FilterComboBoxUnit rfcbu = new FilterComboBoxUnit("稀有度", RarityFilter.values()),
						aafcbu = new FilterComboBoxUnit("攻击属性", AttackAttributeFilter.values()),
						cfcbu = new FilterComboBoxUnit("国家", CountryFilter.values()),
						sfcbu = new FilterComboBoxUnit("技能", SkillTypeFilter.values());
				this.filterComboBoxUnitList = ArrayUtils.asList(rfcbu, aafcbu, cfcbu, sfcbu);

				this.setSpacing(20);
				this.getChildren().addAll(
						rfcbu, aafcbu, cfcbu, sfcbu,
						FXUtils.createButton(
								"重置",
								ev -> {
									this.filterComboBoxUnitList.stream().forEach(fcbu -> fcbu.comboBox.getSelectionModel().select(0));
									ShowDataNode.this.updateTable();
								}
								/**/
						)
						/**/
				);
			}

			class FilterComboBoxUnit extends HBox {
				ComboBox<CharaFilter> comboBox;

				FilterComboBoxUnit(String name, CharaFilter[] choices) {
					this.comboBox = new ComboBox<>(FXCollections.observableArrayList(choices));
					this.comboBox.getSelectionModel().select(0);
					this.comboBox.getSelectionModel().selectedItemProperty().addListener(FXUtils.makeChangeListener(ShowDataNode.this::updateTable));

					this.setSpacing(3);
					this.setAlignment(Pos.CENTER_LEFT);
					this.getChildren().addAll(new Label(name), new Label(":"), this.comboBox);
				}
			}
		}
	}

	class CharaDataTable extends TableView<AbstractCharaData> {
		CharaDataTable() {
			this.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

			//行号
			FXUtils.addNumberColumn(this);

			//头像
			FXUtils.addNewColumn(this, "", false, new Callback<TableColumn<AbstractCharaData, Image[]>, TableCell<AbstractCharaData, Image[]>>() {
				@Override
				public TableCell<AbstractCharaData, Image[]> call(TableColumn<AbstractCharaData, Image[]> param) {
					return new TableCell<AbstractCharaData, Image[]>() {
						final ImageView[] imageViews = new ImageView[]{new ImageView(), new ImageView(), new ImageView(), new ImageView(), new ImageView()};

						{
							this.setGraphic(LambdaUtils.init(new StackPane(), stackPane -> {
								stackPane.getChildren().addAll(this.imageViews);
								stackPane.setScaleX(0.5);
								stackPane.setScaleY(0.5);
								stackPane.setMinSize(50, 50);
								stackPane.setMaxSize(50, 50);
								stackPane.setPrefSize(50, 50);
							}));
						}

						@Override protected void updateItem(Image[] item, boolean empty) {
							super.updateItem(item, empty);

							for(int index = 0; index < 5; index++) {
								if(!empty && item != null) {
									this.imageViews[index].setImage(item[index]);
								} else {
									this.imageViews[index].setImage(null);
								}
							}
						}
					};
				}
			}, AbstractCharaData::getIcons);

			FXUtils.addNewColumn(this, "ID", FXUtils.getIntegerTableCellFactory(), AbstractCharaData::getId);
			FXUtils.addNewColumn(this, "花名", FXUtils.getStringTableCellFactory(), AbstractCharaData::getName);
			FXUtils.addNewColumn(this, "稀有度", FXUtils.getStringTableCellFactory(), AbstractCharaData::getRarityString);
			FXUtils.addNewColumn(this, "属性", FXUtils.getStringTableCellFactory(), AbstractCharaData::getAttackAttributeString);
			FXUtils.addNewColumn(this, "移动力", FXUtils.getIntegerTableCellFactory(), AbstractCharaData::getMove);
			FXUtils.addNewColumn(this, "HP", FXUtils.getIntegerTableCellFactory(), cd -> cd.getHP()[ShowDataNode.this.charaFilterPane
					.getFavorIndex()]);
			FXUtils.addNewColumn(this, "攻击力", FXUtils.getIntegerTableCellFactory(), cd -> cd.getAttack()[ShowDataNode.this.charaFilterPane
					.getFavorIndex()]);
			FXUtils.addNewColumn(this, "防御力", FXUtils.getIntegerTableCellFactory(), cd -> cd.getDefense()[ShowDataNode.this.charaFilterPane
					.getFavorIndex()]);
			FXUtils.addNewColumn(this, "综合力", FXUtils.getIntegerTableCellFactory(), cd -> cd.getPower()[ShowDataNode.this.charaFilterPane
					.getFavorIndex()]);
			FXUtils.addNewColumn(this, "国家", FXUtils.getStringTableCellFactory(), AbstractCharaData::getCountryString);
			FXUtils.addNewColumn(this, "版本", FXUtils.getStringTableCellFactory(), AbstractCharaData::getVersion);
			FXUtils.addNewColumn(this, "图鉴编号", FXUtils.getIntegerTableCellFactory(), AbstractCharaData::getBid);
			FXUtils.addNewColumn(this, "开花编号",
			                     TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
				                     @Override
				                     public String toString(Integer integer) {
					                     return Optional.ofNullable(integer).filter(i -> i != 0).map(Object::toString).orElse("");
				                     }

				                     @Override
				                     public Integer fromString(String string) {
					                     return Optional.ofNullable(string).filter(s -> !"".equals(s)).map(String::trim).map(Integer::valueOf)
					                                    .orElse(Integer.valueOf(0));
				                     }
			                     }),
			                     cd -> cd.getAllChara()[1].getBloomNumber()
					/**/
			);
			FXUtils.addNewColumn(this, "状态", FXUtils.getStringTableCellFactory(), AbstractCharaData::getStateString);

			FXUtils.addNewColumn(this, "kariBloom", FXUtils.getStringTableCellFactory(), cd -> cd.isKariBloom() ? "是" : "");
			FXUtils.addNewColumn(this, "ability_type", FXUtils.getStringTableCellFactory(), cd -> {
				return Arrays.toString(ExStreamUtils.of(cd.getAbility().abilityArray).mapToInt(AbilityInfo::getType).toArray());
			});
			FXUtils.addNewColumn(this, "skill_number", FXUtils.getStringTableCellFactory(), cd -> cd.getSkillNumber() + "," + cd.getSkill()
			                                                                                                                    .toString());
		}
	}
}
