package fkg.book.temp;

import com.moekagari.tool.acs.ArrayUtils;

import java.util.List;

/**
 * @author MoeKagari
 */
public class AbstractCharaDataCharaType {
	private static final List<Integer> OTHER_CHARA_LIST = ArrayUtils.asList(
			136701, 117101, 112901, 114901, 148301,
			128801, 131923, 124705, 139201, 156501,
			130805, 150203, 150811, 117603, 132913,
			164403, 110809, 157603, 125607, 154405,
			110001, 122801, 162701, 143003, 120003,
			120819, 115504
	);
	private final AbstractCharaData chara;

	public AbstractCharaDataCharaType(AbstractCharaData chara) {
		this.chara = chara;
	}

	private boolean isEventChara() {
		return this.chara.isEventChara();
	}

	public boolean isNotEventChara() {
		return !this.isEventChara();
	}

	public boolean isGachaChara() {
		return this.isNotEventChara() && this.isNotOtherChara();
	}

	public boolean isNotGachaChara() {
		return !this.isGachaChara();
	}

	public boolean isOtherChara() {
		return OTHER_CHARA_LIST.contains(this.chara.getAllChara()[0].getId());
	}

	public boolean isNotOtherChara() {
		return !this.isOtherChara();
	}
}
