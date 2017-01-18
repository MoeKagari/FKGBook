package patch.api.getMaster;

public class CharacterInformation implements GameData {
	public static final String key = "masterCharacter";

	int cid1, cid2;
	/**
	 * ��Ŀ,masterCharacterCategory
	 */
	int category;
	/**
	 * ����<br>
	 * if 0���޹�������<br>
	 * if 1,�����󥿩`��`��<br>
	 * if 2,�Хʥʥ��`�����<br>
	 * if 3,�֥�å���ҥ�<br>
	 * if 4,�٥륬��åȥХ�`<br>
	 * if 5,��ꥣ���å�
	 */
	int country;
	int e;// ?????????
	/**
	 * ��������ͬ�ڽ�ɫ��
	 */
	String flower_name;
	String characterExplain;// ???????????
	/**
	 * ϡ�ж�
	 */
	int rarity;
	/**
	 * ��������<br>
	 * if 1,ն����<br>
	 * if 2,������<br>
	 * if 3,ͻ����<br>
	 * if 4,ħ����<br>
	 * if 5,������
	 */
	int attackAttribute;
	String j;// ????????????
	/**
	 * ��������1����������2���������� CharacterLeaderSkill,CharacterLeaderSkill,CharacterSkill
	 */
	public int passiveSkill1, passiveSkill2, activeSkill;
	/**
	 * all 0,����5�����Եľ����ͧ
	 */
	String n;// �������
	String o;//��Ϸ��δ��
	int minLevelHP, maxLevelHP;
	int minLevelAttack, maxLevelAttack;
	int minLevelDefense, maxLevelDefense;
	int minLevelMove, maxLevelMove;
	/**
	 * x,y,z<br>
	 * �������޷ֱ����ӵ�HP������������������ֵ4000��1000��400<br>
	 * ֻ���ڽ�ɫ���ԣ���isCharacter == 1<br>
	 */
	String x, y, z;
	/**
	 * ���ۻ�õĽ��ֵ
	 */
	int worthGold;
	/**
	 * masterCharacterLevel,�ڶ���<br>
	 * �������飬���ϳɾ���<br>
	 * 1��3��5��7��9���ֱ�Ϊ2��3��4��5��6��δ����<br>
	 * 2��4��6��8��10���ֱ�Ϊ2��3��4��5��6�ǽ���<br>
	 * 11��12���ֱ�Ϊ5��6�ǿ���<br>
	 * 101��5��ǿ�����3�����ޡ����ɫ����<br>
	 * 102��20��ǿ���顢���ɫװ��<br>
	 * 103��100��ǿ����<br>
	 * 201��ȫ��������<br>
	 * 202����<br>
	 * 300��5��װ����6��װ�����ᾧ������4�ּ���<br>
	 * 400����������<br>
	 * 203��5�����Ծ����ͧ
	 */
	String ab;
	/**
	 * ������isCharacter == 1 ����<br>
	 * ��ɫ���
	 */
	String ac;
	/**
	 * �Ƿ�Ϊδ������ɫ<br>
	 * if 0��Ϊδ������ɫ
	 */
	int beforeEvolution;
	/**
	 * if 0,�����ɫ,��:���������������ϡ�������,etc<br>
	 * if 1,��ɫ
	 */
	int isCharacter;
	int favorHP, favorAttack, favorDefense;
	/**
	 * book id
	 */
	private int bid;
	/**
	 * if 1,ԭʼ��ɫ<br>
	 * if 2,������ɫ<br>
	 * if 3,������ɫ
	 */
	int oeb;
	/**
	 * �ֺ� isCharacter һ��<br>
	 * �ֺ� ap һ��
	 */
	String ak;
	/**
	 * ���ڽ�ɫ����<br>
	 * 0��1��2��3��4�ֱ�Ϊ2��3��4��5��6��<br>
	 * ���ڷǽ�ɫ����, ֻ��һ��ֵ��0
	 */
	String al;
	int secondFavorHP, secondFavorAttack, secondFavorDefense;
	/**
	 * �ֺ� isCharacter һ��<br>
	 * �ֺ� ak һ��
	 */
	String ap;
	/**
	 * ������ţ���1��ʼ,�����ڽ�����ɫ�������� hasBloom == 1
	 */
	int bloomNumber;
	/**
	 * �Ƿ�Ϊ������ɫ<br>
	 * if 1��Ϊ������ɫ<br>
	 * if 0����Ϊ������ɫ
	 */
	String ar;
	/**
	 * 1Ϊ�п���<br>
	 * 0Ϊ�޿���<br>
	 * �����ڽ���ɫɫ���ԣ��� oeb == 2
	 */
	int hasBloom;
	/**
	 * ��ɫ������ͬ�ڻ���
	 */
	String character_name;
	String time1, time2;
	/**
	 * all 0, no use?
	 */
	String aw;
	String version;

	public CharacterInformation(String source) {
		String[] info = source.trim().split(",");

		int index = 0;
		this.cid1 = Integer.parseInt(info[index++]);
		this.cid2 = Integer.parseInt(info[index++]);
		this.category = Integer.parseInt(info[index++]);
		this.country = Integer.parseInt(info[index++]);
		this.e = Integer.parseInt(info[index++]);
		this.flower_name = info[index++];
		this.characterExplain = info[index++];
		this.rarity = Integer.parseInt(info[index++]);
		this.attackAttribute = Integer.parseInt(info[index++]);
		this.j = info[index++];
		this.passiveSkill1 = Integer.parseInt(info[index++]);
		this.passiveSkill2 = Integer.parseInt(info[index++]);
		this.activeSkill = Integer.parseInt(info[index++]);
		this.n = info[index++];
		this.o = info[index++];
		this.minLevelHP = Integer.parseInt(info[index++]);
		this.maxLevelHP = Integer.parseInt(info[index++]);
		this.minLevelAttack = Integer.parseInt(info[index++]);
		this.maxLevelAttack = Integer.parseInt(info[index++]);
		this.minLevelDefense = Integer.parseInt(info[index++]);
		this.maxLevelDefense = Integer.parseInt(info[index++]);
		this.minLevelMove = Integer.parseInt(info[index++]);
		this.maxLevelMove = Integer.parseInt(info[index++]);
		this.x = info[index++];
		this.y = info[index++];
		this.z = info[index++];
		this.worthGold = Integer.parseInt(info[index++]);
		this.ab = info[index++];
		this.ac = info[index++];
		this.beforeEvolution = Integer.parseInt(info[index++]);
		this.isCharacter = Integer.parseInt(info[index++]);
		this.favorHP = Integer.parseInt(info[index++]);
		this.favorAttack = Integer.parseInt(info[index++]);
		this.favorDefense = Integer.parseInt(info[index++]);
		this.bid = (Integer.parseInt(info[index++]));
		this.oeb = Integer.parseInt(info[index++]);
		this.ak = info[index++];
		this.al = info[index++];
		this.secondFavorHP = Integer.parseInt(info[index++]);
		this.secondFavorAttack = Integer.parseInt(info[index++]);
		this.secondFavorDefense = Integer.parseInt(info[index++]);
		this.ap = info[index++];
		this.bloomNumber = Integer.parseInt(info[index++]);
		this.ar = info[index++];
		this.hasBloom = Integer.parseInt(info[index++]);
		this.character_name = info[index++];
		this.time1 = info[index++];
		this.time2 = info[index++];
		this.aw = info[index++];
		this.version = info[index++];
	}

	/*----------------------------------------------------------*/

	public static CharacterInformation getElement(GameData[] cis, int id) {
		for (GameData obj : cis) {
			if (obj instanceof CharacterInformation) {
				CharacterInformation ci = (CharacterInformation) obj;
				if (ci.getID() == id) return ci;
			}
		}

		return null;
	}

	public static GameData[] get() {
		return GameData.get(key, CharacterInformation::new);
	}

	/*----------------------------------------------------------*/

	/**
	 * ����<br>
	 * if 0���޹�������<br>
	 * if 1,�����󥿩`��`��<br>
	 * if 2,�Хʥʥ��`�����<br>
	 * if 3,�֥�å���ҥ�<br>
	 * if 4,�٥륬��åȥХ�`<br>
	 * if 5,��ꥣ���å�
	 */
	public String getCountry() {
		String[] c = { "��", "�����󥿩`��`��", "�Хʥʥ��`�����", "�֥�å���ҥ�", "�٥륬��åȥХ�`", "��ꥣ���å�" };

		return c[this.country];
	}

	public int getCountryNumber() {
		return this.country;
	}

	public int getAttackAttributeNumber() {
		return this.attackAttribute;
	}

	public String getAttackAttribute() {
		final String[] aa = new String[] { "ն", "��", "ͻ", "ħ", "��" };
		return aa[this.attackAttribute - 1];
	}

	public String getName() {
		return this.character_name;
	}

	public int getID() {
		return this.cid1;
	}

	public int getOeb() {
		return this.oeb;
	}

	public boolean isCharacter() {
		return this.isCharacter == 1;
	}

	public GameData[] getSkill(GameData[] css, GameData[] clss) {
		return new GameData[] { CharacterLeaderSkill.getElement(clss, this.passiveSkill1), CharacterLeaderSkill.getElement(clss, this.passiveSkill2), CharacterSkill.getElement(css, this.activeSkill) };
	}

	public int[] getHP() {
		return new int[] { this.maxLevelHP, this.favorHP, this.secondFavorHP };
	}

	public int[] getAttack() {
		return new int[] { this.maxLevelAttack, this.favorAttack, this.secondFavorAttack };
	}

	public int[] getDefense() {
		return new int[] { this.maxLevelDefense, this.favorDefense, this.secondFavorDefense };
	}

	public int[] getPower() {
		return new int[] { this.maxLevelHP + this.maxLevelAttack + this.maxLevelDefense, this.favorHP + this.favorAttack + this.favorDefense, this.secondFavorHP + this.secondFavorAttack + this.secondFavorDefense };
	}

	public int getMove() {
		return this.minLevelMove;
	}

	public boolean hasBloom() {
		return this.hasBloom == 1;
	}

	public int getRarity() {
		return this.rarity;
	}

	public int getBloomNumber() {
		return this.bloomNumber;
	}

	public int getBid() {
		return this.bid;
	}

}
