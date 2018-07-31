package fkg.book.temp.content.book;

import com.moekagari.tool.other.FXUtils;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

import java.io.IOException;

/**
 * @author MoeKagari
 */
public class BookBodyContentRowCellWiki extends Button implements BookBodyContentRowCell {
	public BookBodyContentRowCellWiki(String name) {
		super("WIKI");
		this.setCursor(Cursor.HAND);
		this.setBackground(FXUtils.createBackground(Color.TRANSPARENT));
		this.setOnAction(ev -> {
			try {
				String url = "https://xn--eckq7fg8cygsa1a1je.xn--wiki-4i9hs14f.com/index.php?" + name;
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
			} catch(IOException e) {
				e.printStackTrace();
			}
		});
	}
}
