package show.filter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

import show.CharacterData;
import show.config.AppConfig;
import tool.FileUtil;

public class SkilltypeFilter implements Filter {
	public final static String[] STRING_SKILLTYPE;
	private final static int[] SKILLTYPE;

	private static class Skilltype {
		private final int id;
		private final String type;

		public int getId() {
			return this.id;
		}

		public String getType() {
			return this.type;
		}

		public Skilltype(int id, String type) {
			this.id = id;
			this.type = type;
		}

	}

	static {
		ArrayList<Skilltype> sts = new ArrayList<>();

		byte[] bytes = FileUtil.read(AppConfig.CHARACTER_SKILLTYPE);
		if (bytes != null) {
			String[] sources = new String[0];
			try {
				sources = new String(bytes, "utf-8").split("\r\n");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			for (String source : sources) {
				String[] temp = source.trim().split(",");
				if (temp.length == 2) {
					sts.add(new Skilltype(Integer.parseInt(temp[0]), temp[1]));
				}
			}
		}

		Collections.sort(sts, (a, b) -> a.getType().compareTo(b.getType()));

		int len = sts.size() + 1;
		SKILLTYPE = new int[len];
		STRING_SKILLTYPE = new String[len];
		SKILLTYPE[0] = 0;
		STRING_SKILLTYPE[0] = "нч";
		for (int i = 1; i < len; i++) {
			SKILLTYPE[i] = sts.get(i - 1).getId();
			STRING_SKILLTYPE[i] = sts.get(i - 1).getType();
		}
	}

	private final int skilltype;

	public SkilltypeFilter(int index) {
		this.skilltype = SKILLTYPE[index];
	}

	@Override
	public boolean filter(CharacterData cd) {
		if (this.skilltype == 0) {
			return true;
		}

		for (int st : cd.getSkill().getSkill1Type()) {
			if (this.skilltype == st) {
				return true;
			}
		}

		for (int st : cd.getSkill().getSkill2Type()) {
			if (this.skilltype == st) {
				return true;
			}
		}

		return false;
	}

}
