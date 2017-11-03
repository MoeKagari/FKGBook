package fkg.masterdata;

public class CharacterMypageVoiceResourceGroup implements GetMasterData {
	public int id;
	public int characterId;
	public int character_expression_type_id;
	public int voice_resource_id;//CharacterMypageVoiceResource

	public CharacterMypageVoiceResourceGroup(String source) {
		String[] info = source.trim().split(",", -1);

		int index = 0;
		this.id = Integer.parseInt(info[index++]);
		this.characterId = Integer.parseInt(info[index++]);
		this.character_expression_type_id = Integer.parseInt(info[index++]);
		this.voice_resource_id = Integer.parseInt(info[index++]);
	}
}
