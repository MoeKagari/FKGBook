package show.filter;

import show.CharacterData;

public class OEBFilter implements Filter {

	public static final String[] STRING_STAGE = { "原始角色", "进化角色", "无开花进化", "开花角色", "最高进化", "全部角色" };
	private static final int[] STAGE = { 1, 2, 6, 3, 4, 5 };

	private final int oeb;

	public OEBFilter(int index) {
		this.oeb = STAGE[index];
	}

	@Override
	public boolean filter(CharacterData cd) {
		boolean flag = false;

		switch (this.oeb) {
			case 1:
			case 2:
			case 3:
				if (cd.getOEB() == this.oeb) flag = true;
				break;
			case 4:
				switch (cd.getOEB()) {
					case 1:
						flag = false;
						break;
					case 2:
						if (!cd.hasBloom()) flag = true;
						break;
					case 3:
						flag = true;
						break;
				}
				break;
			case 5:
				flag = true;
				break;
			case 6:
				switch (cd.getOEB()) {
					case 1:
						flag = false;
						break;
					case 2:
						if (!cd.hasBloom()) flag = true;
						break;
					case 3:
						flag = false;
						break;
				}
				break;
			default:
				flag = false;
				break;
		}

		return flag;
	}

}
