package patch.api.getMaster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface GameData {

	public static GameData[] get(final String key, final Function<String, GameData> gdd) {
		File file = new File(GetMaster.dir + "\\" + key + ".csv");
		if (!file.exists() | !file.isFile()) {
			System.out.println("文件不存在 in GameData：" + key);
			return null;
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"))) {
			return reader.lines().map(gdd).collect(Collectors.toList()).toArray(new GameData[0]);
		} catch (IOException e) {
			return null;
		}
	}

}
