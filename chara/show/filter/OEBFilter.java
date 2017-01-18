package show.filter;

import show.CharacterData;

public class OEBFilter implements Filter {

	public static final String[] STRING_STAGE = { "原始角色", "进化角色", "开花角色", "最高进化", "全部角色" };
	private static final int[] STAGE = { 1, 2, 3, 4, 5 };

	private final int oeb;

	public OEBFilter(int index) {
		this.oeb = STAGE[index];
	}

	@Override
	public boolean filter(CharacterData cd) {
		boolean flag = false;

		switch (oeb) {
			case 1:
			case 2:
			case 3:
				if (cd.getOEB() == oeb) flag = true;
				break;
			case 4:
				if (cd.getOEB() == 2 && !cd.hasBloom()) flag = true;
				if (cd.getOEB() == 3) flag = true;
				break;
			case 5:
				flag = true;
				break;
			default:
				flag = false;
				break;
		}

		return flag;
	}

}
