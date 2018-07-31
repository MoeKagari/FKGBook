package fkg.book.temp.content;

import com.moekagari.tool.acs.ArrayUtils;
import com.moekagari.tool.acs.ExStreamUtils;
import com.moekagari.tool.other.FXUtils;
import fkg.book.gui.AbstractCharaData;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.Comparator;

/**
 * @author MoeKagari
 */
public class WindowPaneContentTabNodeSorter extends GridPane implements WindowPaneContentTabNode {
	public WindowPaneContentTabNodeSorter() {
		this.setId("sorter");
		this.setAlignment(Pos.CENTER);

		int columnLength = 5, extraColumnLength = 4;
		this.getColumnConstraints().addAll(
				ExStreamUtils.generate(() -> FXUtils.createColumnConstraints(100, 150))
				             .limit(columnLength + extraColumnLength).toList()
		);

		ArrayUtils.forEachWithIndex(
				new PaneSorterChoice[]{
						new PaneSorterChoice("ID", true),
						new PaneSorterChoice("花名"),
						new PaneSorterChoice("国家"),
						new PaneSorterChoice("稀有度"),
						new PaneSorterChoice("属性"),
						new PaneSorterChoice("移动力"),
						new PaneSorterChoice("HP"),
						new PaneSorterChoice("攻击力"),
						new PaneSorterChoice("防御力"),
						new PaneSorterChoice("总和力"),
						new PaneSorterChoice("状态"),
						new PaneSorterChoice("版本")
				},
				(index, node) -> this.add(node, extraColumnLength / 2 + index % columnLength, index / columnLength)
		);
	}

	public Comparator<AbstractCharaData> getSorter() {
		Comparator<AbstractCharaData> sorter = Comparator.comparingInt(AbstractCharaData::getBid).reversed();
		return sorter.thenComparingInt(AbstractCharaData::getOeb);
	}

	private class PaneSorterChoice extends Label {
		public final BooleanProperty selected = new SimpleBooleanProperty();

		public PaneSorterChoice(String text) {
			this(text, false);
		}

		public PaneSorterChoice(String text, boolean defaultSelected) {
			super(text);
			FXUtils.addStyleClass(this, "choice");
			this.setMaxWidth(Double.MAX_VALUE);

			this.selected.addListener(FXUtils.makeChangeListener((oldValue, newValue) -> FXUtils.ableStyleClass(newValue, this, "active")));
			this.selected.set(defaultSelected);
			this.setOnMouseClicked(ev -> WindowPaneContentTabNodeSorter.this.getChildren().forEach(
					node -> ((PaneSorterChoice) node).selected.set(node == this)
			));
		}
	}
}
