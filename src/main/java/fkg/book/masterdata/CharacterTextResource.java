package fkg.book.masterdata;

import fkg.book.masterdata.GetMasterData.GetMasterDataLineSpliter;

public class CharacterTextResource {
	String index;
	int character;
	/**
	 * 29 : 个人介绍
	 * 48 : 隠しステージ
	 */
	int type;
	String text, resourceName;
	String f, g, h;

	public CharacterTextResource(String source) {
		GetMasterDataLineSpliter gmdls = new GetMasterDataLineSpliter(source);

		this.index = gmdls.next();
		this.character = gmdls.nextInt();
		this.type = gmdls.nextInt();
		this.text = gmdls.next();
		this.resourceName = gmdls.next();
		this.f = gmdls.next();
		this.g = gmdls.next();
		this.h = gmdls.next();
	}

	public boolean isIntroduction() {
		return this.getType() == 29;
	}

	public int getCharacter() {
		return this.character;
	}

	public int getType() {
		return this.type;
	}

	public String getText() {
		return this.text;
	}

	public String getResourceName() {
		return this.resourceName;
	}
}
