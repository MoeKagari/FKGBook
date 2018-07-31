package fkg.book.temp;

import fkg.book.gui.AbstractCharaData;

/**
 * @author MoeKagari
 */
public interface WindowPane {
	default void showCharaStand(AbstractCharaData chara) {

	}

	default void showCharaDetail(AbstractCharaData chara) {

	}

	default void focusParentSizeChange(double width, double height) {

	}

	default void windowShowBefore() {

	}

	default void windowShowAfter() {

	}
}
