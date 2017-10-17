package fkg.masterdata;

import fkg.other.StringInteger;

public class CharacterLeaderSkillDescription implements GetMasterData {
	public final int cid, cid1;
	public final StringInteger[] skills;

	public CharacterLeaderSkillDescription(String source) {
		String[] tokens = source.split(",", -1);
		int index = 0;

		this.cid = Integer.parseInt(tokens[index++]);
		this.cid1 = Integer.parseInt(tokens[index++]);
		this.skills = new StringInteger[] {
				new StringInteger(Integer.parseInt(tokens[index++]), tokens[index++]),
				new StringInteger(Integer.parseInt(tokens[index++]), tokens[index++]),
				new StringInteger(Integer.parseInt(tokens[index++]), tokens[index++]),
				new StringInteger(Integer.parseInt(tokens[index++]), tokens[index++]),
				/**/ };
	}
}
