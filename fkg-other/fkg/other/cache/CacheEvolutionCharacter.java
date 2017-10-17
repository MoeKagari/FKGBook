package fkg.other.cache;

import java.util.ArrayList;
import java.util.HashMap;

import fkg.masterdata.CharacterInformation;
import fkg.masterdata.GetMasterData;

public class CacheEvolutionCharacter {

	final static HashMap<Integer, CharacterInformation> cis = new HashMap<>();
	static {
		int a = xia = 110000;
		int b = shang = 162000;
		for (GetMasterData gd : GetMasterData.MASTERCHARACTER) {
			if (gd instanceof CharacterInformation) {
				CharacterInformation ci = (CharacterInformation) gd;
				int id = ci.getId();
				if (id < a || id > b) continue;
				cis.put(ci.getId(), ci);
			}
		}
	}

	final static int xia;
	final static int shang;

	public static void main(String[] args) {
		int num = 10;
		int per = (shang - xia) / num;

		for (int i = 0; i < num; i++)
			getThread(getList(xia + i * per, xia + (i + 1) * per)).start();

		getThread(getList(xia + num * per, shang)).start();
	}

	private static ArrayList<Integer> getList(int xia, int shang) {
		ArrayList<Integer> ids = new ArrayList<>();
		for (int i = xia; i <= shang; i++) {
			ids.add(i);
		}
		return ids;
	}

	private static Thread getThread(ArrayList<Integer> ids) {
		return new Thread(() -> ids.parallelStream().forEach(id -> {
			if (cis.get(id) == null) {
				ArrayList<CacheInformation> aci = CacheInformation.get(id);
				for (CacheInformation cain : aci) {
					CacheDownloader.download(id, cain);
				}
			}
		}));
	}

}
