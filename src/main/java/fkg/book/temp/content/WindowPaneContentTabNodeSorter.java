package fkg.book.temp.content;

import com.moekagari.tool.acs.ArrayUtils;
import com.moekagari.tool.acs.CollectionUtils;
import com.moekagari.tool.acs.ExStreamUtils;
import com.moekagari.tool.other.FXUtils;
import fkg.book.temp.AbstractCharaData;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/**
 * @author MoeKagari
 */
public class WindowPaneContentTabNodeSorter extends VBox implements WindowPaneContentTabNode {
	private static final int MAX_ITEM_IN_ONE_ROW = 6;
	private final SorterChild sorterChildMultiple, sorterChildSingle;

	public WindowPaneContentTabNodeSorter() {
		this.setId("sorter");
		this.setAlignment(Pos.CENTER);

		List<ColumnConstraints> columnConstraintsList = ExStreamUtils
				.generate(() -> FXUtils.createColumnConstraints(210, 220))
				.limit(MAX_ITEM_IN_ONE_ROW).toList();
		this.getChildren().addAll(
				this.sorterChildMultiple = new SorterChildMultiple(columnConstraintsList),
				new Label("↓"),
				this.sorterChildSingle = new SorterChildSingle(columnConstraintsList)
		);
	}

	public Comparator<AbstractCharaData> getSorter() {
		return this.sorterChildMultiple.sorter.thenComparing(this.sorterChildSingle.sorter);
	}

	private static class SorterChild extends GridPane {
		private Comparator<AbstractCharaData> sorter = (a, b) -> 0;

		SorterChild(String styleClass, List<ColumnConstraints> columnConstraintsList, SorterChildChoice... sorterChildChoiceArray) {
			this.setAlignment(Pos.CENTER);
			this.getColumnConstraints().addAll(columnConstraintsList);
			FXUtils.addStyleClass(this, "child", styleClass);

			int rowIndex = -1;
			int maxChoiceNumberInOneRow = columnConstraintsList.size();
			//添加行choice
			for(int choiceIndex = 0; choiceIndex < sorterChildChoiceArray.length; choiceIndex++) {
				SorterChildChoice sorterChildChoice = sorterChildChoiceArray[choiceIndex];

				//遇到新一行时 , rowIndex + 1
				rowIndex += choiceIndex % maxChoiceNumberInOneRow == 0 ? 1 : 0;

				//null = 占位符
				if(sorterChildChoice != null) {
					this.add(sorterChildChoice, choiceIndex % maxChoiceNumberInOneRow, rowIndex);
					this.sorter = this.sorter.thenComparing(sorterChildChoice.buildSorter());

					//注册事件
					sorterChildChoice.setOnMouseClicked(ev -> {
						ArrayUtils.forEach(sorterChildChoiceArray, Objects::nonNull, choice -> choice.setSelected(choice == sorterChildChoice));
					});
				}
			}

			this.getRowConstraints().addAll(ExStreamUtils.generate(() -> FXUtils.createRowConstraints(35)).limit(rowIndex + 1).toList());
		}

		static class SorterChildChoice extends Label {
			final Comparator<AbstractCharaData> sorter;
			final BooleanProperty selected;

			<T extends Comparable<T>> SorterChildChoice(String text, Function<AbstractCharaData, T> mapper) {
				this(text, mapper, false);
			}

			<T extends Comparable<T>> SorterChildChoice(String text, Function<AbstractCharaData, T> mapper, boolean defaultSelected) {
				this(text, Comparator.comparing(mapper), defaultSelected);
			}

			SorterChildChoice(String text, ToIntFunction<AbstractCharaData> mapper) {
				this(text, mapper, false);
			}

			SorterChildChoice(String text, ToIntFunction<AbstractCharaData> mapper, boolean defaultSelected) {
				this(text, Comparator.comparingInt(mapper), defaultSelected);
			}

			SorterChildChoice(String text, Comparator<AbstractCharaData> sorter, boolean defaultSelected) {
				super(text);
				this.sorter = sorter.reversed();//默认从大到小

				this.setAlignment(Pos.CENTER_LEFT);
				FXUtils.addStyleClass(this, "choice");
				FXUtils.setMaxSize(this, Double.MAX_VALUE, Double.MAX_VALUE);

				this.selected = new SimpleBooleanProperty();
				this.selected.addListener(FXUtils.makeChangeListener((oldValue, newValue) -> FXUtils.ableStyleClass(newValue, this, "active")));
				this.selected.set(defaultSelected);
			}

			Comparator<AbstractCharaData> buildSorter() {
				return (a, b) -> this.isSelected() ? this.sorter.compare(a, b) : 0;
			}

			void setSelected(boolean value) {
				this.selected.set(value);
			}

			boolean isSelected() {
				return this.selected.get();
			}
		}
	}

	private static class SorterChildMultiple extends SorterChild {
		SorterChildMultiple(List<ColumnConstraints> columnConstraintsList) {
			super("multiple", columnConstraintsList, getSorterChildChoiceArray(
					new SorterChildChoice("国家", AbstractCharaData::getCountry),
					new SorterChildChoice("属性", AbstractCharaData::getAttackAttribute),
					new SorterChildChoice("星级", AbstractCharaData::getRarity)
			));
		}

		static SorterChildChoice[] getSorterChildChoiceArray(SorterChildChoice... sorterChildChoiceArray) {
			return ExStreamUtils.mapEntryStream(
					getAllCombination(ArrayUtils.asList(sorterChildChoiceArray))
							.stream().map(SorterChildMultiple::getAllPermutation).flatMap(List::stream)
							.collect(Collectors.groupingBy(List::size))
			)
			                    .sortedObj(Map.Entry::getKey).map(Map.Entry::getValue)
			                    .flatMap(css -> ExStreamUtils.concat(
					                    ExStreamUtils.stream(css).map(cs -> new SorterChildChoice(
							                    cs.size() == 0 ? "不采用" :
									                    ExStreamUtils.stream(cs).joining(SorterChildChoice::getText, " → "),
							                    cs.stream()
							                      .map(c -> c.sorter.reversed())
							                      .reduce(Comparator::thenComparing)
							                      .orElse((a, b) -> 0),
							                    cs.size() == 0
					                    )).sortedObj(SorterChildChoice::getText),
					                    ExStreamUtils.nullStream(MAX_ITEM_IN_ONE_ROW - css.size())
			                    ))
			                    .toArray(SorterChildChoice[]::new);
		}

		static List<List<SorterChildChoice>> getAllCombination(List<SorterChildChoice> sorterChildChoiceList) {
			if(sorterChildChoiceList.size() == 0) {
				return ArrayUtils.asList(CollectionUtils.emptyList());
			}

			SorterChildChoice first = sorterChildChoiceList.get(0);
			List<List<SorterChildChoice>> allCombinationOfOthers = getAllCombination(sorterChildChoiceList.subList(1, sorterChildChoiceList.size()));

			List<List<SorterChildChoice>> result = new ArrayList<>(allCombinationOfOthers);
			for(List<SorterChildChoice> cs : allCombinationOfOthers) {
				List<SorterChildChoice> temp = new ArrayList<>(cs);
				temp.add(first);
				result.add(temp);
			}

			return result;
		}

		static List<List<SorterChildChoice>> getAllPermutation(List<SorterChildChoice> sorterChildChoiceList) {
			if(sorterChildChoiceList.size() == 0) {
				return ArrayUtils.asList(CollectionUtils.emptyList());
			}

			List<List<SorterChildChoice>> result = new ArrayList<>();

			for(SorterChildChoice sorterChildChoice : sorterChildChoiceList) {
				List<SorterChildChoice> temp = new ArrayList<>(sorterChildChoiceList);
				temp.remove(sorterChildChoice);
				for(List<SorterChildChoice> cs : getAllPermutation(temp)) {
					List<SorterChildChoice> oneResult = new ArrayList<>(cs);
					oneResult.add(0, sorterChildChoice);
					result.add(oneResult);
				}
			}

			return result;
		}
	}

	private static class SorterChildSingle extends SorterChild {
		SorterChildSingle(List<ColumnConstraints> columnConstraintsList) {
			super("single", columnConstraintsList,
			      null,
			      new SorterChildChoice("HP", AbstractCharaData::getHPFinal),
			      new SorterChildChoice("攻击力", AbstractCharaData::getAttackFinal),
			      new SorterChildChoice("防御力", AbstractCharaData::getDefenseFinal),
			      new SorterChildChoice("总和力", AbstractCharaData::getPowerFinal),
			      null,

			      null,
			      new SorterChildChoice("花名", AbstractCharaData::getName),
			      new SorterChildChoice("图鉴", AbstractCharaData::getBid, true),
			      new SorterChildChoice("状态", AbstractCharaData::getStateString),
			      new SorterChildChoice("衣装", AbstractCharaData::getVersion),
			      null,

			      null,
			      null,
			      new SorterChildChoice("ID", AbstractCharaData::getId),
			      new SorterChildChoice("移动力", AbstractCharaData::getMove),
			      null,
			      null
			);
		}
	}
}
