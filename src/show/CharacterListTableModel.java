package show;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import show.config.ShowConfig;
import show.filter.AttackAttributeFilter;
import show.filter.CountryFilter;
import show.filter.Filter;

@SuppressWarnings("serial")
public class CharacterListTableModel extends AbstractTableModel {
	private final ArrayList<ColumnManager> cms = this.initCMS();
	private final ArrayList<CharacterData> cds = new ArrayList<>();// �洢��ǰtable��ʾ��CharacterData

	private ArrayList<ColumnManager> initCMS() {
		ArrayList<ColumnManager> array = new ArrayList<>();

		array.add(new ColumnManager(false, "", () -> ImageIcon.class, cd -> cd.getIcon()));
		array.add(new ColumnManager(true, "ID", () -> Integer.class, cd -> cd.getId()));
		array.add(new ColumnManager(true, "����", () -> String.class, cd -> {
			String cn = cd.getChineseName();
			if (ShowConfig.isUseChineseName() && cn != null) return cd.getChineseName();
			if (cn == null) System.out.println(cd.getId() + "," + cd.getName());
			return cd.getName();
		}));
		array.add(new ColumnManager(false, "ϡ�ж�", () -> String.class, cd -> "�����������".substring(0, cd.getRarity())));
		array.add(new ColumnManager(true, "��������", () -> String.class, cd -> AttackAttributeFilter.STRING_ATTRIBUTE[cd.getAttackAttributeNumber()]));
		array.add(new ColumnManager(true, "�ƶ���", () -> Integer.class, cd -> cd.getMove()));
		array.add(new ColumnManager(true, "HP", () -> Integer.class, cd -> cd.getHp()[ShowConfig.getFavorIndex()]));
		array.add(new ColumnManager(true, "������", () -> Integer.class, cd -> cd.getAttack()[ShowConfig.getFavorIndex()]));
		array.add(new ColumnManager(true, "������", () -> Integer.class, cd -> cd.getDefense()[ShowConfig.getFavorIndex()]));
		array.add(new ColumnManager(true, "�ۺ���", () -> Integer.class, cd -> cd.getPower()[ShowConfig.getFavorIndex()]));
		array.add(new ColumnManager(false, "����", () -> String.class, cd -> CountryFilter.STRING_COUNTRY[cd.getCountryNumber()]));
		array.add(new ColumnManager(true, "״̬", () -> String.class, cd -> {
			switch (cd.getOEB()) {
				case 1:
					return "ԭʼ";
				case 2:
					return "����";
				case 3:
					return "����";
				default:
					return "";
			}
		}));

		return array;
	}

	public ArrayList<ColumnManager> getCMS() {
		return this.cms;
	}

	/*-----------------------------------------------------------*/

	@Override
	public String getColumnName(int index) {
		return this.cms.get(index).getColName();
	}

	@Override
	public int getColumnCount() {
		return this.cms.size();
	}

	@Override
	public int getRowCount() {
		return this.cds.size();
	}

	@Override
	public Object getValueAt(int r, int c) {
		return this.cms.get(c).getValueAt(this.cds.get(r));
	}

	@Override
	public Class<?> getColumnClass(int c) {
		return this.cms.get(c).getColumnClass();
	}

	@Override
	public boolean isCellEditable(int r, int c) {
		return false;
	}

	/*-----------------------------------------------------------*/

	public void updateCDS(ArrayList<Filter> filters) {
		this.cds.clear();

		for (CharacterData cd : CharacterData.get().values()) {
			boolean flag = true;
			for (Filter filter : filters) {
				flag = flag && filter.filter(cd);
			}
			if (flag == true) this.cds.add(cd);
		}

		final boolean sortByBid = ShowConfig.isSortByBid();
		final boolean sortByBloomNumber = ShowConfig.isSortByBloomNumber();
		Collections.sort(this.cds, (a, b) -> {
			int res;

			if (sortByBloomNumber) {
				res = -1 * Integer.compare(a.getBloomNumber(), b.getBloomNumber());
			} else {
				res = 0;
			}

			if (res == 0) {
				if (sortByBid) {
					res = -1 * Integer.compare(a.getBid(), b.getBid());
				} else {
					res = Integer.compare(a.getId(), b.getId());
				}
			}

			return res;
		});
	}

	/*-----------------------------------------------------------*/

	/**
	 * �����еı�����������
	 */
	public class ColumnManager {
		private final String colName;
		private final Function<CharacterData, Object> va;
		private final Supplier<Class<?>> cc;
		private final boolean isCenter;

		public ColumnManager(boolean isCenter, String colName, Supplier<Class<?>> cc, Function<CharacterData, Object> va) {
			this.isCenter = isCenter;
			this.colName = colName;
			this.cc = cc;
			this.va = va;
		}

		public boolean isCenter() {
			return this.isCenter;
		}

		public Class<?> getColumnClass() {
			return this.cc.get();
		}

		public Object getValueAt(CharacterData cd) {
			return this.va.apply(cd);
		}

		public String getColName() {
			return this.colName;
		}

	}

}
