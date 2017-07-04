package show;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import show.config.ShowConfig;
import show.data.CharacterData;
import show.filter.AttackAttributeFilter;
import show.filter.CountryFilter;
import show.filter.Filter;

@SuppressWarnings("serial")
public class CharacterListTableModel extends AbstractTableModel {
	private final ArrayList<ColumnManager> cms;
	private final ArrayList<CharacterData> cds = new ArrayList<>();// 存储当前table显示的CharacterData

	public CharacterListTableModel() {
		this.cms = this.initCMS();
	}

	private ArrayList<ColumnManager> initCMS() {
		ArrayList<ColumnManager> array = new ArrayList<>();

		array.add(new ColumnManager(false, "", ImageIcon.class, CharacterData::getIcon));
		array.add(new ColumnManager(true, "ID", Integer.class, cd -> cd.ci.getID()));
		array.add(new ColumnManager(true, "花名", String.class, cd -> {
			String cn = cd.chineseName;
			if (cn == null) {
				System.out.println(cd.ci.getID() + "," + cd.ci.getName());
			}
			if (ShowConfig.isUseChineseName() && cn != null) {
				return cn;
			}
			return cd.ci.getName();
		}));
		array.add(new ColumnManager(false, "稀有度", String.class, cd -> "★★★★★★★★★★".substring(0, cd.ci.getRarity())));
		array.add(new ColumnManager(true, "攻击属性", String.class, cd -> AttackAttributeFilter.SIS[cd.ci.getAttackAttributeNumber()].getString()));
		array.add(new ColumnManager(true, "移动力", Integer.class, cd -> cd.ci.getMove()));
		array.add(new ColumnManager(true, "HP", Integer.class, cd -> cd.getHp()[ShowConfig.getFavorIndex()]));
		array.add(new ColumnManager(true, "攻击力", Integer.class, cd -> cd.getAttack()[ShowConfig.getFavorIndex()]));
		array.add(new ColumnManager(true, "防御力", Integer.class, cd -> cd.getDefense()[ShowConfig.getFavorIndex()]));
		array.add(new ColumnManager(true, "综合力", Integer.class, cd -> cd.getPower()[ShowConfig.getFavorIndex()]));
		array.add(new ColumnManager(false, "国家", String.class, cd -> CountryFilter.SIS[cd.ci.getCountryNumber()].getString()));
		array.add(new ColumnManager(true, "状态", String.class, cd -> {
			switch (cd.ci.getOeb()) {
				case 1:
					return "原始";
				case 2:
					return "进化";
				case 3:
					return "开花";
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
			if (flag == true) {
				this.cds.add(cd);
			}
		}

		final boolean sortByBid = ShowConfig.isSortByBid();
		final boolean sortByBloomNumber = ShowConfig.isSortByBloomNumber();
		Collections.sort(this.cds, (a, b) -> {
			int res;

			if (sortByBloomNumber) {
				res = -1 * Integer.compare(a.bloomNumber, b.bloomNumber);
			} else {
				res = 0;
			}

			if (res == 0) {
				if (sortByBid) {
					res = -1 * Integer.compare(a.ci.getBid(), b.ci.getBid());
				} else {
					res = Integer.compare(a.ci.getID(), b.ci.getID());
				}
			}

			return res;
		});
	}

	/*-----------------------------------------------------------*/

	/**
	 * 管理列的标题和相关属性
	 */
	public class ColumnManager {
		private final String colName;
		private final Function<CharacterData, Object> va;
		private final Class<?> cc;
		private final boolean isCenter;

		public ColumnManager(boolean isCenter, String colName, Class<?> cc, Function<CharacterData, Object> va) {
			this.isCenter = isCenter;
			this.colName = colName;
			this.cc = cc;
			this.va = va;
		}

		public boolean isCenter() {
			return this.isCenter;
		}

		public Class<?> getColumnClass() {
			return this.cc;
		}

		public Object getValueAt(CharacterData cd) {
			return this.va.apply(cd);
		}

		public String getColName() {
			return this.colName;
		}

	}

}
