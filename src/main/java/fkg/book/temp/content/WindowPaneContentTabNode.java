package fkg.book.temp.content;

import javafx.scene.Node;

/**
 * @author MoeKagari
 */
public interface WindowPaneContentTabNode {
	default void activeBefore() {}

	default void activeAfter() {}

	default Node castToNode() { return (Node) this; }
}
