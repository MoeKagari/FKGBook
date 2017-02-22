package show;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import show.CharacterListTableModel.ColumnManager;
import show.config.ShowConfig;
import show.filter.AttackAttributeFilter;
import show.filter.CountryFilter;
import show.filter.Filter;
import show.filter.OEBFilter;
import show.filter.RarityFilter;
import show.filter.SkilltypeFilter;
import tool.IndexFinder;

public class ShowFlowerInformation {

	private int rowHeight = 50;

	private JFrame frame;
	private JTable table;
	private CharacterListTableModel scltm;
	private JScrollPane jsc;
	private AbstractTableModel rowHeaderModel = new AbstractTableModel() {
		private static final long serialVersionUID = 1L;

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return rowIndex + 1;
		}

		@Override
		public int getRowCount() {
			return ShowFlowerInformation.this.table.getRowCount();
		}

		@Override
		public int getColumnCount() {
			return 1;
		}
	};

	private SkillPanel panel;

	private JRadioButton[] jrb_oeb;
	private JCheckBox jcb_cn;
	private JCheckBox jcb_bid;
	private JCheckBox jcb_bloomnumber;
	private JRadioButton[] jrb_favor;
	private JComboBox<String> jcb_rarity, jcb_attribute, jcb_country, jcb_skilltype;
	private JButton clear;

	public ShowFlowerInformation() {
		this.panel = new SkillPanel();
		this.scltm = new CharacterListTableModel();
		this.initFrame();
		this.initTable();
		this.initJSC();
		this.initComponent();
		this.layoutFrame();

		this.updateTable();
		this.frame.requestFocus();
	}

	private void layoutFrame() {
		JPanel frame_center = new JPanel();
		frame_center.setLayout(new BorderLayout());
		{
			JPanel frame_center_north = new JPanel();
			frame_center_north.setLayout(new GridLayout(2, 1));
			{
				JPanel frame_center_north_1 = new JPanel();
				{
					Arrays.asList(this.jrb_oeb).forEach(jrb -> frame_center_north_1.add(jrb));
					frame_center_north_1.add(this.jcb_cn);
					frame_center_north_1.add(this.jcb_bid);
					frame_center_north_1.add(this.jcb_bloomnumber);
					Arrays.asList(this.jrb_favor).forEach(jrb -> frame_center_north_1.add(jrb));
				}
				frame_center_north.add(frame_center_north_1);
			}
			{
				JPanel frame_center_north_2 = new JPanel();
				{
					frame_center_north_2.add(new JLabel("稀有度："));
					frame_center_north_2.add(this.jcb_rarity);
					frame_center_north_2.add(new JLabel("攻击属性："));
					frame_center_north_2.add(this.jcb_attribute);
					frame_center_north_2.add(new JLabel("国家："));
					frame_center_north_2.add(this.jcb_country);
					frame_center_north_2.add(new JLabel("技能类型："));
					frame_center_north_2.add(this.jcb_skilltype);
					frame_center_north_2.add(this.clear);
				}
				frame_center_north.add(frame_center_north_2);
			}
			frame_center.add(frame_center_north, BorderLayout.NORTH);
		}
		{
			frame_center.add(this.jsc, BorderLayout.CENTER);
		}
		/*-------------------*/
		this.frame.getContentPane().add(frame_center, BorderLayout.CENTER);
		this.frame.getContentPane().add(this.panel, BorderLayout.EAST);
	}

	/*-----------------------------------------------------------------------------------*/

	public void setFocus() {
		this.frame.requestFocus();
	}

	public void dispose() {
		this.frame.dispose();
		this.panel.dispose();
	}

	public void display() {
		this.frame.setVisible(true);
	}

	private void clearSelectionAndUpdateTable() {
		Arrays.asList(new JComboBox[] { this.jcb_rarity, this.jcb_attribute, this.jcb_country, this.jcb_skilltype }).forEach(jcb -> jcb.setSelectedIndex(0));
		this.updateTable();
	}

	private void updateTable() {
		ShowConfig.setOEBIndex(IndexFinder.find(this.jrb_oeb, jrb -> jrb.isSelected()));
		ShowConfig.setUseChineseName(this.jcb_cn.isSelected());
		ShowConfig.setSortByBid(this.jcb_bid.isSelected());
		ShowConfig.setSortByBloomNumber(this.jcb_bloomnumber.isSelected());
		ShowConfig.setFavorIndex(IndexFinder.find(this.jrb_favor, jrb -> jrb.isSelected()));
		ShowConfig.setRarityIndex(this.jcb_rarity.getSelectedIndex());
		ShowConfig.setAAIndex(this.jcb_attribute.getSelectedIndex());
		ShowConfig.setCountryIndex(this.jcb_country.getSelectedIndex());
		ShowConfig.setSTIndex(this.jcb_skilltype.getSelectedIndex());

		ArrayList<Filter> filters = new ArrayList<>();
		filters.add(new OEBFilter(ShowConfig.getOEBIndex()));
		filters.add(new RarityFilter(ShowConfig.getRarityIndex()));
		filters.add(new AttackAttributeFilter(ShowConfig.getAAIndex()));
		filters.add(new CountryFilter(ShowConfig.getCountryIndex()));
		filters.add(new SkilltypeFilter(ShowConfig.getSTIndex()));
		this.scltm.updateCDS(filters);

		this.scltm.fireTableDataChanged();
		this.rowHeaderModel.fireTableDataChanged();
		{
			this.table.setRowHeight(this.rowHeight);
			// 设置单选
			this.table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			// 设置头像栏的width和height，并且不可改变大小
			TableColumn tc0 = this.table.getColumnModel().getColumn(0);
			int iconWidth = 50;
			tc0.setMaxWidth(iconWidth);
			tc0.setMinWidth(iconWidth);
			tc0.setResizable(false);
			// 设置列不可拖动
			this.table.getTableHeader().setReorderingAllowed(false);
			// 设置列可排序
			this.table.setAutoCreateRowSorter(true);
			// 设置居中
			ArrayList<ColumnManager> cms = this.scltm.getCMS();
			for (int i = 0; i < cms.size(); i++) {
				if (cms.get(i).isCenter()) {
					this.table.getColumnModel().getColumn(i).setCellRenderer(getTCR_Center());
				}
			}
		}
		int h = this.jsc.getVerticalScrollBar().getSize().height;
		this.jsc.getRowHeader().setPreferredSize(new Dimension(30, h));
	}

	/*-----------------------------------------------------------------------------------*/

	private void initComponent() {
		{
			ButtonGroup bg_oeb = new ButtonGroup();
			this.jrb_oeb = new JRadioButton[OEBFilter.STRING_STAGE.length];
			for (int i = 0; i < this.jrb_oeb.length; i++) {
				this.jrb_oeb[i] = new JRadioButton(OEBFilter.STRING_STAGE[i]);
				this.jrb_oeb[i].setFocusPainted(false);
				this.jrb_oeb[i].addActionListener(ev -> this.updateTable());
				bg_oeb.add(this.jrb_oeb[i]);
			}
			this.jrb_oeb[ShowConfig.getOEBIndex()].setSelected(true);
		}
		{
			this.jcb_cn = new JCheckBox("中文名");
			this.jcb_cn.setFocusPainted(false);
			this.jcb_cn.setSelected(ShowConfig.isUseChineseName());
			this.jcb_cn.addActionListener(ev -> this.updateTable());
		}
		{
			this.jcb_bid = new JCheckBox("图鉴顺序");
			this.jcb_bid.setFocusPainted(false);
			this.jcb_bid.setSelected(ShowConfig.isSortByBid());
			this.jcb_bid.addActionListener(ev -> {
				this.scltm.fireTableStructureChanged();
				this.updateTable();
			});
		}
		{
			this.jcb_bloomnumber = new JCheckBox("开花顺序");
			this.jcb_bloomnumber.setFocusPainted(false);
			this.jcb_bloomnumber.setSelected(ShowConfig.isSortByBloomNumber());
			this.jcb_bloomnumber.addActionListener(ev -> {
				this.scltm.fireTableStructureChanged();
				this.updateTable();
			});
		}
		{
			ButtonGroup bg_favor = new ButtonGroup();
			this.jrb_favor = new JRadioButton[ShowConfig.STRING_FAVOR.length];
			for (int i = 0; i < this.jrb_favor.length; i++) {
				this.jrb_favor[i] = new JRadioButton(ShowConfig.STRING_FAVOR[i]);
				this.jrb_favor[i].setFocusPainted(false);
				this.jrb_favor[i].addActionListener(ev -> this.updateTable());
				bg_favor.add(this.jrb_favor[i]);
			}
			this.jrb_favor[ShowConfig.getFavorIndex()].setSelected(true);
		}
		{
			this.jcb_rarity = new JComboBox<>(RarityFilter.STRING_RARITY);
			this.jcb_rarity.setEditable(false);
			this.jcb_rarity.setSelectedIndex(ShowConfig.getRarityIndex());
			this.jcb_attribute = new JComboBox<>(AttackAttributeFilter.STRING_ATTRIBUTE);
			this.jcb_attribute.setEditable(false);
			this.jcb_attribute.setSelectedIndex(ShowConfig.getAAIndex());
			this.jcb_country = new JComboBox<>(CountryFilter.STRING_COUNTRY);
			this.jcb_country.setEditable(false);
			this.jcb_country.setSelectedIndex(ShowConfig.getCountryIndex());
			this.jcb_skilltype = new JComboBox<>(SkilltypeFilter.STRING_SKILLTYPE);
			this.jcb_skilltype.setEditable(false);
			this.jcb_skilltype.setSelectedIndex(ShowConfig.getSTIndex());
			for (JComboBox<?> jcb : new JComboBox[] { this.jcb_rarity, this.jcb_attribute, this.jcb_country, this.jcb_skilltype }) {
				jcb.addActionListener(ev -> this.updateTable());
			}
		}
		{
			this.clear = new JButton("清除");
			this.clear.setFocusPainted(false);
			this.clear.addActionListener(ev -> this.clearSelectionAndUpdateTable());
		}
	}

	private void initJSC() {
		JTable rowHeader = new JTable(this.rowHeaderModel);
		rowHeader.setRowHeight(this.rowHeight);
		rowHeader.getColumnModel().getColumn(0).setCellRenderer(getTCR_Center());

		this.jsc = new JScrollPane(this.table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.jsc.setRowHeaderView(rowHeader);
	}

	private void initTable() {
		this.table = new JTable(this.scltm);
		this.table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					int rowNum = ShowFlowerInformation.this.table.getSelectedRow();
					int id = (int) ShowFlowerInformation.this.table.getValueAt(rowNum, 1);
					CharacterData cd = CharacterData.get().get(id);
					if (cd != null) {
						ShowFlowerInformation.this.panel.showCharacter(cd);
					}
				}
			}
		});
		this.table.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				ShowFlowerInformation.this.frame.requestFocusInWindow();
			}
		});
	}

	private void initFrame() {
		this.frame = new JFrame("所有花娘");
		this.frame.setSize(1500, 773);
		this.frame.setLocationRelativeTo(null);
		this.frame.setResizable(false);
		this.frame.getContentPane().setLayout(new BorderLayout());
		this.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	private static DefaultTableCellRenderer getTCR_Center() {
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(JLabel.CENTER);
		return tcr;
	}

}
