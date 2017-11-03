package fkg.masterdata;

public class CharacterMypageVoiceResource implements GetMasterData {
	public int id;
	public int characterId;
	public String voice_file;

	public CharacterMypageVoiceResource(String source) {
		String[] info = source.trim().split(",", -1);

		int index = 0;
		this.id = Integer.parseInt(info[index++]);
		this.characterId = Integer.parseInt(info[index++]);
		this.voice_file = info[index++];
	}
}
