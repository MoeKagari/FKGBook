package fkg.patch.api.getMaster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import fkg.patch.api.GetMaster;

public interface GetMasterData {
	public static List<GetMasterData> get(String key, Function<String, GetMasterData> gdd) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(GetMaster.dir + "\\" + key + ".csv")), "utf-8"))) {
			return reader.lines().map(gdd).collect(Collectors.toList());
		} catch (IOException e) {
			return null;
		}
	}
}
