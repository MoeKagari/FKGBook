package patch.other.cache;

import java.util.ArrayList;

import patch.api.getMaster.CharacterInformation;
import patch.api.getMaster.GameData;

/**
 * É¨Ãè¿ª»¨½ÇÉ«
 */
public class CacheBloomCharacter {

	public static void main(String[] args) {
		int flag = 1;
		if (flag == 0) scanAll();
		else scanNew();
	}

	private static void scanAll() {
		int count = 1;
		GameData[] cis = CharacterInformation.get();
		for (GameData obj : cis) {
			if (obj instanceof CharacterInformation) {
				CharacterInformation ci = (CharacterInformation) obj;
				if (ci.isCharacter() && ci.getOeb() == 1) {
					int id = ci.getID() + 300000;
					ArrayList<CacheInformation> aci = CacheInformation.get(id);

					for (CacheInformation cain : aci)
						CacheDownloader.download(id, cain);

					System.out.println(id + "," + (count++));
				}
			}
		}
	}

	private static void scanNew() {
		int count = 1;
		GameData[] cis = CharacterInformation.get();
		for (GameData obj : cis) {
			if (obj instanceof CharacterInformation) {
				CharacterInformation ci = (CharacterInformation) obj;
				if (ci.isCharacter() && ci.getOeb() == 2 && ci.hasBloom() == false) {
					int id = ci.getID() - 1 + 300000;
					ArrayList<CacheInformation> aci = CacheInformation.get(id);

					for (CacheInformation cain : aci)
						CacheDownloader.download(id, cain);

					System.out.println(id + "," + (count++));
				}
			}
		}
	}

}
