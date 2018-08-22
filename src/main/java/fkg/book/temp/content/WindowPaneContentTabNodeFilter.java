package fkg.book.temp.content;

import com.moekagari.tool.acs.ArrayUtils;
import com.moekagari.tool.acs.ExStreamUtils;
import com.moekagari.tool.other.FXUtils;
import fkg.book.temp.AbstractCharaData;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Ellipse;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author MoeKagari
 */
public class WindowPaneContentTabNodeFilter extends VBox implements WindowPaneContentTabNode {
	private final FilterChild filterChildMain, filterChildSkill, filterChildAbility;

	public WindowPaneContentTabNodeFilter() {
		this.setId("filter");
		this.setAlignment(Pos.CENTER);

		List<ColumnConstraints> columnConstraintsList = ExStreamUtils.concat(
				ExStreamUtils.of(FXUtils.createColumnConstraints(150, HPos.LEFT)),
				ExStreamUtils.generate(() -> FXUtils.createColumnConstraints(210, 220)).limit(6)
		).toList();
		this.getChildren().addAll(
				this.filterChildMain = new FilterChildMain(columnConstraintsList),
				this.filterChildSkill = new FilterChildSkill(columnConstraintsList),
				this.filterChildAbility = new FilterChildAbility(columnConstraintsList)
		);
	}

	public Predicate<AbstractCharaData> getFilter() {
		return this.filterChildMain.filter.and(this.filterChildSkill.filter).and(this.filterChildAbility.filter);
	}

	private static class FilterChild extends GridPane {
		private Predicate<AbstractCharaData> filter = chara -> true;

		FilterChild(String styleClass, List<ColumnConstraints> columnConstraintsList, FilterChildRowInfo... filterChildRowInfoArray) {
			this.setAlignment(Pos.CENTER);
			this.getColumnConstraints().addAll(columnConstraintsList);
			FXUtils.addStyleClass(this, "child", styleClass);

			int rowIndex = -1;
			int maxChoiceNumberInOneRow = columnConstraintsList.size() - 1;
			for(int rowInfoIndex = 0; rowInfoIndex < filterChildRowInfoArray.length; rowInfoIndex++) {
				FilterChildRowInfo filterChildRowInfo = filterChildRowInfoArray[rowInfoIndex];
				FilterChildChoice[] filterChildChoiceArray = filterChildRowInfo.filterChildChoiceArray;

				//添加行头
				this.add(FXUtils.createHBox(
						3, Pos.CENTER_LEFT, false,
						box -> box.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE),
						filterChildRowInfo.selectAll, filterChildRowInfo.cancelAll, filterChildRowInfo.filterChildLabel
				), 0, rowInfoIndex);

				//添加行choice
				for(int choiceIndex = 0; choiceIndex < filterChildChoiceArray.length; choiceIndex++) {
					FilterChildChoice filterChildChoice = filterChildChoiceArray[choiceIndex];

					//遇到新一行时 , rowIndex + 1
					rowIndex += choiceIndex % maxChoiceNumberInOneRow == 0 ? 1 : 0;

					//null = 占位符
					if(filterChildChoice != null) {
						this.add(filterChildChoice, 1 + choiceIndex % maxChoiceNumberInOneRow, rowIndex);
					}
				}

				//更新filter
				this.filter = this.filter.and(filterChildRowInfo.buildPredicate());
			}

			this.getRowConstraints().addAll(ExStreamUtils.generate(() -> FXUtils.createRowConstraints(35)).limit(rowIndex + 1).toList());
		}

		static class FilterChildButton extends Button {
			FilterChildButton(String text, FilterChildChoice[] filterChildChoiceArray, boolean setSelected) {
				super(text);
				this.setOnAction(ev -> ArrayUtils.forEach(filterChildChoiceArray, filterChildChoice -> filterChildChoice.setSelected(setSelected)));

				Ellipse shape = new Ellipse();
				shape.radiusXProperty().bind(this.widthProperty().divide(2));
				shape.radiusYProperty().bind(this.heightProperty().divide(2));
				this.setShape(shape);
			}
		}

		static class FilterChildLabel extends Label {
			FilterChildLabel(String text) {
				this.setText(text);
				this.setMaxWidth(Double.MAX_VALUE);
				this.setAlignment(Pos.CENTER_LEFT);
			}
		}

		static class FilterChildChoice extends Label {
			final Predicate<AbstractCharaData> filter;
			final BooleanProperty selected;

			FilterChildChoice(String text, Predicate<AbstractCharaData> filter) {
				this(text, filter, false);
			}

			FilterChildChoice(String text, Predicate<AbstractCharaData> filter, boolean defaultSelected) {
				super(text);
				this.filter = filter;

				this.setAlignment(Pos.CENTER);
				FXUtils.addStyleClass(this, "choice");
				FXUtils.setMaxSize(this, Double.MAX_VALUE, Double.MAX_VALUE);
				this.setOnMouseClicked(FXUtils.makeEventHandler(this::reverse));

				this.selected = new SimpleBooleanProperty();
				this.selected.addListener(FXUtils.makeChangeListener((oldValue, newValue) -> FXUtils.ableStyleClass(newValue, this, "active")));
				this.selected.set(defaultSelected);
			}

			void reverse() {
				this.setSelected(!this.isSelected());
			}

			void select() {
				this.setSelected(true);
			}

			void cancel() {
				this.setSelected(false);
			}

			void setSelected(boolean value) {
				this.selected.set(value);
			}

			boolean isSelected() {
				return this.selected.get();
			}
		}

		static class FilterChildRowInfo {
			final FilterChildButton selectAll/*全部选择*/, cancelAll/*全部取消*/;
			final FilterChildLabel filterChildLabel;
			final FilterChildChoice[] filterChildChoiceArray;

			FilterChildRowInfo(String rowName, FilterChildChoice... filterChildChoiceArray) {
				this.filterChildLabel = new FilterChildLabel(rowName);
				this.filterChildChoiceArray = filterChildChoiceArray;
				this.selectAll = new FilterChildButton("选", this.filterChildChoiceArray, true);
				this.cancelAll = new FilterChildButton("消", this.filterChildChoiceArray, false);
			}

			Predicate<AbstractCharaData> buildPredicate() {
				return chara -> ExStreamUtils.of(this.filterChildChoiceArray).anyMatch(
						filterChildChoice -> filterChildChoice.isSelected() && filterChildChoice.filter.test(chara)
				);
			}
		}
	}

	private static class FilterChildMain extends FilterChild {
		FilterChildMain(List<ColumnConstraints> columnConstraintsList) {
			super("main", columnConstraintsList,
			      new FilterChildRowInfo(
					      "获得途径",
					      new FilterChildChoice("卡池角色", AbstractCharaData::isGachaChara, true),
					      new FilterChildChoice("活动角色", AbstractCharaData::isEventChara, true),
					      new FilterChildChoice("其它角色", AbstractCharaData::isOtherChara, true)
			      ),
			      new FilterChildRowInfo(
					      "开花升华",
					      new FilterChildChoice("普通开花(真)", chara -> chara.getOeb() == 3 && !chara.isKariBloom(), true),
					      new FilterChildChoice("普通开花(假)", chara -> chara.getOeb() == 3 && chara.isKariBloom(), true),
					      new FilterChildChoice("升华开花(真)", chara -> chara.getOeb() == 99 && !chara.isKariBloom(), true),
					      new FilterChildChoice("升华开花(假)", chara -> chara.getOeb() == 99 && chara.isKariBloom(), true),
					      new FilterChildChoice("还未开花", chara -> chara.getOeb() == 1 || chara.getOeb() == 2, true)
			      ),
			      new FilterChildRowInfo(
					      "星级",
					      new FilterChildChoice("六星", chara -> chara.getRarity() == 6, true),
					      new FilterChildChoice("五星", chara -> chara.getRarity() == 5, true),
					      new FilterChildChoice("四星", chara -> chara.getRarity() == 4, true),
					      new FilterChildChoice("三星", chara -> chara.getRarity() == 3, true),
					      new FilterChildChoice("二星", chara -> chara.getRarity() == 2, true)
			      ),
			      new FilterChildRowInfo(
					      "国家",
					      new FilterChildChoice("ウィンターローズ", chara -> chara.getCountry() == 1, true),
					      new FilterChildChoice("バナナオーシャン", chara -> chara.getCountry() == 2, true),
					      new FilterChildChoice("ブロッサムヒル", chara -> chara.getCountry() == 3, true),
					      new FilterChildChoice("ベルガモットバレー", chara -> chara.getCountry() == 4, true),
					      new FilterChildChoice("リリィウッド", chara -> chara.getCountry() == 5, true),
					      new FilterChildChoice("ロータスレイク", chara -> chara.getCountry() == 7, true)
			      ),
			      new FilterChildRowInfo(
					      "属性",
					      new FilterChildChoice("斩", chara -> chara.getAttackAttribute() == 1, true),
					      new FilterChildChoice("打", chara -> chara.getAttackAttribute() == 2, true),
					      new FilterChildChoice("突", chara -> chara.getAttackAttribute() == 3, true),
					      new FilterChildChoice("魔", chara -> chara.getAttackAttribute() == 4, true)
			      )
			);
		}
	}

	private static class FilterChild_Skill_or_Ability extends FilterChild {
		FilterChild_Skill_or_Ability(
				String styleClass, List<ColumnConstraints> columnConstraintsList,
				String rowName, FilterChildChoice... filterChildChoiceArrayExpectDefault
		) {
			super(styleClass, columnConstraintsList, getFilterChildRowInfo(columnConstraintsList, rowName, filterChildChoiceArrayExpectDefault));
		}

		static FilterChildRowInfo getFilterChildRowInfo(List<ColumnConstraints> columnConstraintsList, String rowName, FilterChildChoice[] filterChildChoiceArrayExpectDefault) {
			FilterChildChoice defaultFilterChildChoice = new FilterChildChoice("不采用", chara -> true, true);
			List<FilterChildChoice> filterChildChoiceList = ArrayUtils.asList(filterChildChoiceArrayExpectDefault);

			defaultFilterChildChoice.setOnMouseClicked(ev -> {
				if(!defaultFilterChildChoice.isSelected()) {
					defaultFilterChildChoice.select();
					filterChildChoiceList.forEach(FilterChildChoice::cancel);
				}
			});
			filterChildChoiceList.forEach(filterChildChoice -> filterChildChoice.setOnMouseClicked(ev -> {
				filterChildChoice.reverse();
				defaultFilterChildChoice.setSelected(filterChildChoiceList.stream().noneMatch(FilterChildChoice::isSelected));
			}));

			FilterChildRowInfo filterChildRowInfo = new FilterChildRowInfo(rowName, ExStreamUtils.concat(
					ExStreamUtils.of(defaultFilterChildChoice)
					             .concat(ExStreamUtils.nullStream(columnConstraintsList.size() - 1 - 1)),
					filterChildChoiceList.stream()
			).toArray(FilterChildChoice[]::new)) {
				@Override Predicate<AbstractCharaData> buildPredicate() {
					return chara -> ExStreamUtils.of(this.filterChildChoiceArray).notNull().allMatch(
							filterChildChoice -> !filterChildChoice.isSelected() || filterChildChoice.filter.test(chara)
					);
				}
			};
			filterChildRowInfo.selectAll.setVisible(false);
			filterChildRowInfo.selectAll.setOnAction(ev -> {});
			filterChildRowInfo.cancelAll.setOnAction(ev -> {
				defaultFilterChildChoice.select();
				filterChildChoiceList.forEach(FilterChildChoice::cancel);
			});

			return filterChildRowInfo;
		}
	}

	private static class FilterChildSkill extends FilterChild_Skill_or_Ability {
		FilterChildSkill(List<ColumnConstraints> columnConstraintsList) {
			super("skill", columnConstraintsList, "技能"
			);
		}
	}

	private static class FilterChildAbility extends FilterChild_Skill_or_Ability {
		FilterChildAbility(List<ColumnConstraints> columnConstraintsList) {
			super("ability", columnConstraintsList, "能力"
			);
		}
	}
}
