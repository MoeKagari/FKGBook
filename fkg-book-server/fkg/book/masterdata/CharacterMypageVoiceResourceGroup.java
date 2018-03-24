package fkg.book.masterdata;

import fkg.book.masterdata.GetMasterData.GetMasterDataLineSpliter;

public class CharacterMypageVoiceResourceGroup {
	public int id;
	public int characterId;
	public int character_expression_id;
	public int voice_resource_id;//CharacterMypageVoiceResource

	public CharacterMypageVoiceResourceGroup(String source) {
		GetMasterDataLineSpliter gmdls = new GetMasterDataLineSpliter(source);

		this.id = gmdls.nextInt();
		this.characterId = gmdls.nextInt();
		this.character_expression_id = gmdls.nextInt();
		this.voice_resource_id = gmdls.nextInt();
	}
}
