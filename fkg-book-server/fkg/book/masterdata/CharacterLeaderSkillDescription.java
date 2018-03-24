package fkg.book.masterdata;

import fkg.book.masterdata.GetMasterData.GetMasterDataLineSpliter;

public class CharacterLeaderSkillDescription {
	public final int cid, cid1;
	public final AbilityInfo[] abilityArray;

	public CharacterLeaderSkillDescription(String source) {
		GetMasterDataLineSpliter gmdls = new GetMasterDataLineSpliter(source);

		this.cid = gmdls.nextInt();
		this.cid1 = gmdls.nextInt();
		this.abilityArray = new AbilityInfo[] {
				new AbilityInfo(gmdls.nextInt(), gmdls.next()),
				new AbilityInfo(gmdls.nextInt(), gmdls.next()),
				new AbilityInfo(gmdls.nextInt(), gmdls.next()),
				new AbilityInfo(gmdls.nextInt(), gmdls.next())
		};
	}

	public static class AbilityInfo {
		private final int type;
		private final String description;

		public AbilityInfo(int integer, String string) {
			this.type = integer;
			this.description = string;
		}

		public AbilityInfo(String string, int integer) {
			this.description = string;
			this.type = integer;
		}

		public int getType() {
			return this.type;
		}

		public String getDescription() {
			return this.description;
		}
	}
}
