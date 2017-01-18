package show;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import show.config.AppConfig;
import tool.FileUtil;

public class CharacterChineseName {

	private static HashMap<Integer, String> cns;

	public static HashMap<Integer, String> get() {
		if (cns == null) {
			byte[] bytes = FileUtil.read(AppConfig.ID_CHINESENAME);
			if (bytes == null) return null;

			cns = new HashMap<>();
			String[] sources = new String[0];
			try {
				sources = new String(bytes, "utf-8").split("\r\n");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			for (String source : sources) {
				String[] temp = source.trim().split(",");
				if (temp.length == 2) {
					cns.put(Integer.parseInt(temp[0]), temp[1]);
				}
			}

			new HashMap<>(cns).forEach((id, name) -> {
				cns.put(id + 1, name);
				cns.put(id + 300000, name);
			});
		}

		return cns;
	}

}
