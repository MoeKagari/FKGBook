package fkg.book.temp.content.book;

import com.moekagari.tool.lambda.LambdaUtils;
import com.moekagari.tool.other.FXUtils;
import fkg.book.gui.AbstractCharaData;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.function.Function;

/**
 * @author MoeKagari
 */
public class BookHeadCell extends HBox {
	public final Function<AbstractCharaData, BookBodyContentRowCell> value;
	public final ColumnConstraints columnConstraints;

	public BookHeadCell(String name, Function<AbstractCharaData, BookBodyContentRowCell> value) {
		this(name, value, (Double) null);
	}

	public BookHeadCell(String name, Function<AbstractCharaData, BookBodyContentRowCell> value, Integer width) {
		this(name, value, width.doubleValue());
	}

	public BookHeadCell(String name, Function<AbstractCharaData, BookBodyContentRowCell> value, Double width) {
		this(name, value, width, HPos.LEFT);
	}

	public BookHeadCell(String name, Function<AbstractCharaData, BookBodyContentRowCell> value, Integer width, HPos hPos) {
		this(name, value, width.doubleValue(), hPos);
	}

	public BookHeadCell(String name, Function<AbstractCharaData, BookBodyContentRowCell> value, Double width, HPos hPos) {
		super(LambdaUtils.init(new Label(name), label -> label.setPrefHeight(35)));

		this.value = value;

		FXUtils.addStyleClass(this, "book-head-cell");
		this.setAlignment(hPos == HPos.CENTER ? Pos.CENTER : Pos.valueOf("CENTER_" + hPos));

		if(width == null) {
			this.columnConstraints = new ColumnConstraints();
			this.columnConstraints.setHgrow(Priority.ALWAYS);
			this.columnConstraints.setMinWidth(250);
		} else {
			this.columnConstraints = new ColumnConstraints(width);
		}
		this.columnConstraints.setHalignment(hPos);
	}
}
