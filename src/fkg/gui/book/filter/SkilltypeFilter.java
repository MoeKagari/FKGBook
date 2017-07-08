package fkg.gui.book.filter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.io.FileUtils;

import fkg.gui.book.ShowConfig;
import fkg.gui.book.data.CharacterData;

public class SkilltypeFilter implements Filter {
	public static final StringInteger[] SIS;

	static {
		ArrayList<StringInteger> sts = new ArrayList<>();

		try {
			byte[] bytes = FileUtils.readFileToByteArray(new File(ShowConfig.CHARACTER_SKILLTYPE));
			if (bytes != null) {
				for (String source : new String(bytes, "utf-8").split("\r\n")) {
					String[] temp = source.trim().split(",");
					if (temp.length == 2) {
						sts.add(new StringInteger(temp[1], Integer.parseInt(temp[0])));
					}
				}
			}
			Collections.sort(sts, (a, b) -> a.getString().compareTo(b.getString()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		sts.add(0, new StringInteger("无", 0));
		int len = sts.size();
		SIS = new StringInteger[len];
		for (int i = 0; i < len; i++) {
			SIS[i] = sts.get(i);
		}
	}

	private final int skilltype;

	public SkilltypeFilter(int index) {
		this.skilltype = SIS[index].getInteger();
	}

	@Override
	public boolean filter(CharacterData cd) {
		if (this.skilltype == 0) {
			return true;
		}

		for (int st : cd.skill.skill1Type) {
			if (this.skilltype == st) {
				return true;
			}
		}

		for (int st : cd.skill.skill2Type) {
			if (this.skilltype == st) {
				return true;
			}
		}

		return false;
	}
}
