package patch.api.getMaster;

import java.io.File;
import java.io.UnsupportedEncodingException;

import tool.FileUtil;

public interface GameData {

	public static GameData[] get(final String key, final GameDataDeal gdd) {
		File file = new File(GetMaster.dir + "\\" + key + ".csv");
		if (!file.exists() | !file.isFile()) {
			System.out.println("�ļ������� in GameData��" + key);
			return null;
		}

		byte[] bytes = FileUtil.read(file);
		if (bytes == null) {
			System.out.println("bytes == null in GameData :" + key);
			return null;
		}

		try {
			String[] temp = new String(bytes, "utf-8").split("\n");
			GameData[] result = new GameData[temp.length];
			for (int i = 0; i < temp.length; i++)
				result[i] = gdd.deal(temp[i]);
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
