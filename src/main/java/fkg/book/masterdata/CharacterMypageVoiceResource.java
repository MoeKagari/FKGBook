package fkg.book.masterdata;

import fkg.book.masterdata.GetMasterData.GetMasterDataLineSpliter;

public class CharacterMypageVoiceResource {
	public int id;
	public int characterId;
	public String voice_file;

	public CharacterMypageVoiceResource(String source) {
		GetMasterDataLineSpliter gmdls = new GetMasterDataLineSpliter(source);

		this.id = gmdls.nextInt();
		this.characterId = gmdls.nextInt();
		this.voice_file = gmdls.next();
	}
}
