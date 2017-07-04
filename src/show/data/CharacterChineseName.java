package show.data;

import java.io.File;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import show.config.ShowConfig;

public class CharacterChineseName {

	private static HashMap<Integer, String> cns;

	public static HashMap<Integer, String> get() {
		if (cns == null) {
			cns = new HashMap<>();

			try {
				byte[] bytes = FileUtils.readFileToByteArray(new File(ShowConfig.ID_CHINESENAME));
				for (String source : new String(bytes, "utf-8").split("\r\n")) {
					String[] temp = source.trim().split(",");
					if (temp.length == 2) {
						cns.put(Integer.parseInt(temp[0]), temp[1]);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

			new HashMap<>(cns).forEach((id, name) -> {
				cns.put(id + 1, name);
				cns.put(id + 300000, name);
			});
		}

		return cns;
	}

}
