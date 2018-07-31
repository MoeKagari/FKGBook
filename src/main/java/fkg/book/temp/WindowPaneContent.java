package fkg.book.temp;

import com.moekagari.tool.acs.ArrayUtils;
import com.moekagari.tool.other.FXUtils;
import fkg.book.temp.content.WindowPaneContentTabNode;
import fkg.book.temp.content.WindowPaneContentTabNodeApi;
import fkg.book.temp.content.WindowPaneContentTabNodeBook2;
import fkg.book.temp.content.WindowPaneContentTabNodeConfig;
import fkg.book.temp.content.WindowPaneContentTabNodeFilter;
import fkg.book.temp.content.WindowPaneContentTabNodeHensei;
import fkg.book.temp.content.WindowPaneContentTabNodeSorter;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.util.function.Supplier;

/**
 * @author MoeKagari
 */
public class WindowPaneContent extends BorderPane implements WindowPane {
	public final Window window;
	public final WindowPaneContentTab<WindowPaneContentTabNodeBook2> book2;
	public final WindowPaneContentTab<WindowPaneContentTabNodeFilter> filter;
	public final WindowPaneContentTab<WindowPaneContentTabNodeSorter> sorter;
	public final WindowPaneContentTab<WindowPaneContentTabNodeHensei> hensei;
	public final WindowPaneContentTab<WindowPaneContentTabNodeApi> api;
	public final WindowPaneContentTab<WindowPaneContentTabNodeConfig> config;

	public WindowPaneContent(Window window) {
		FXUtils.addStyleClass(this, "content");
		this.window = window;
		this.book2 = new WindowPaneContentTab<>(true, "图鉴", new WindowPaneContentTabNodeBook2(this));
		this.filter = new WindowPaneContentTab<>("过滤", new WindowPaneContentTabNodeFilter());
		this.sorter = new WindowPaneContentTab<>("排序", new WindowPaneContentTabNodeSorter());
		this.hensei = new WindowPaneContentTab<>("编成", new WindowPaneContentTabNodeHensei());
		this.api = new WindowPaneContentTab<>("API", new WindowPaneContentTabNodeApi());
		this.config = new WindowPaneContentTab<>("设置", new WindowPaneContentTabNodeConfig());

		FXUtils.addStyleClass(this.book2, "book2");
		FXUtils.addStyleClass(this.filter, "filter");
		FXUtils.addStyleClass(this.sorter, "sorter");
		FXUtils.addStyleClass(this.hensei, "hensei");
		FXUtils.addStyleClass(this.api, "api");
		FXUtils.addStyleClass(this.config, "config");

		//聚合 book , filter , sorter
		Node bookFilterSorter = FXUtils.createGridPane(gridPane -> {
			gridPane.getColumnConstraints().addAll(
					FXUtils.createColumnConstraints(Priority.ALWAYS),
					FXUtils.createColumnConstraints(Priority.ALWAYS),
					FXUtils.createColumnConstraints(Priority.ALWAYS),
					FXUtils.createColumnConstraints(Priority.ALWAYS)
			);

			Supplier<Node> createFiller = () -> {
				Pane filler = new Pane();
				FXUtils.addStyleClass(filler, "filler");
				return filler;
			};

			gridPane.add(this.book2, 0, 0, 4, 1);
			gridPane.add(createFiller.get(), 0, 1);
			gridPane.add(this.filter, 1, 1);
			gridPane.add(this.sorter, 2, 1);
			gridPane.add(createFiller.get(), 3, 1);
			FXUtils.addStyleClass(gridPane, "tab-group");
		});

		this.setTop(FXUtils.createGridPane(gridPane -> ArrayUtils.forEachWithIndex(
				new Node[]{bookFilterSorter, this.hensei, this.api, this.config},
				(index, node) -> {
					gridPane.getColumnConstraints().add(FXUtils.createColumnConstraints(300, null, Priority.ALWAYS));
					gridPane.addColumn(index, node);
				}
		)));
	}

	@Override public void windowShowBefore() {
		ArrayUtils.forEach(
				new WindowPaneContentTab[]{this.book2, this.filter, this.sorter, this.hensei, this.api, this.config},
				windowPaneContentTab -> windowPaneContentTab.setActive(windowPaneContentTab.defaultActive)
		);
	}

	public class WindowPaneContentTab<T extends WindowPaneContentTabNode> extends Label {
		public final T windowPaneContentTabNode;
		private final boolean defaultActive;

		private WindowPaneContentTab(String tabLabelName, T windowPaneContentTabNode) {
			this(false, tabLabelName, windowPaneContentTabNode);
		}

		private WindowPaneContentTab(boolean defaultActive, String tabName, T windowPaneContentTabNode) {
			super(tabName);
			this.defaultActive = defaultActive;
			this.windowPaneContentTabNode = windowPaneContentTabNode;
			this.windowPaneContentTabNode.castToNode().getStyleClass().add("tab-node");

			FXUtils.addStyleClass(this, "tab");
			this.setMaxWidth(Double.MAX_VALUE);
			this.setMaxHeight(Double.MAX_VALUE);
			this.setOnMouseClicked(ev -> {
				//避免重复点击
				if(WindowPaneContent.this.getCenter() != this.windowPaneContentTabNode) {
					FXUtils.consumeRecursively(
							(Parent) WindowPaneContent.this.getTop(),
							node -> node instanceof WindowPaneContentTab,
							node -> ((WindowPaneContentTab) node).setActive(node == this)
					);
				}
			});
		}

		private void setActive(boolean active) {
			if(active) {
				this.windowPaneContentTabNode.activeBefore();
				WindowPaneContent.this.setCenter(this.windowPaneContentTabNode.castToNode());
				this.windowPaneContentTabNode.activeAfter();
			}
			FXUtils.ableStyleClass(active, this, "active");
		}
	}
}
