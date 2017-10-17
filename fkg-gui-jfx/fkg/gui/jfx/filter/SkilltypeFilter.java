package fkg.gui.jfx.filter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.io.FileUtils;

import fkg.config.ShowConfig;
import fkg.gui.jfx.CenterShowListPane.CharacterData;
import fkg.other.StringInteger;

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
			sts.sort(Comparator.comparing(StringInteger::getString));
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

		if (cd.getSkill() == null) {
			System.out.println("skill == null : " + cd.getName() + "(" + cd.getId() + ")");
			return false;
		}

		return Arrays.stream(cd.getSkill().skills).anyMatch(si -> si.getInteger() == this.skilltype);
	}
}
