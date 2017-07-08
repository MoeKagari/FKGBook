package fkg.gui.book;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JWindow;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import fkg.gui.AppConfig;
import fkg.gui.FKGGui;
import fkg.gui.FKGGui.ControlSelectionListener;
import fkg.gui.book.CharacterListTableModel.ColumnManager;
import fkg.gui.book.data.CharacterData;
import fkg.gui.book.filter.AttackAttributeFilter;
import fkg.gui.book.filter.CountryFilter;
import fkg.gui.book.filter.Filter;
import fkg.gui.book.filter.OEBFilter;
import fkg.gui.book.filter.RarityFilter;
import fkg.gui.book.filter.SkilltypeFilter;
import tool.FunctionUtils;

public class ShowFlowerGrilInformation {
	private int rowHeight = 50;

	public final JDialog frame;
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
			return ShowFlowerGrilInformation.this.table.getRowCount();
		}

		@Override
		public int getColumnCount() {
			return 1;
		}
	};

	public final SkillPanel skillPanel;

	private JRadioButton[] jrb_oeb;
	private JCheckBox jcb_cn;
	private JCheckBox jcb_bid;
	private JCheckBox jcb_bloomnumber;
	private JRadioButton[] jrb_favor;
	private JComboBox<String> jcb_rarity, jcb_attribute, jcb_country, jcb_skilltype;
	private JButton clear;

	private final FKGGui gui;

	public ShowFlowerGrilInformation(FKGGui gui) {
		this.gui = gui;
		this.skillPanel = new SkillPanel();
		this.scltm = new CharacterListTableModel();

		this.frame = new JDialog();
		this.frame.setTitle("所有花娘");
		this.frame.setSize(1600, 771);
		this.frame.setLocationRelativeTo(null);
		this.frame.setResizable(false);
		this.frame.getContentPane().setLayout(new BorderLayout());
		this.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		this.table = new JTable(this.scltm);
		// 设置居中
		List<ColumnManager> cms = this.scltm.getCMS();
		for (int i = 0; i < cms.size(); i++) {
			if (cms.get(i).isCenter()) {
				this.table.getColumnModel().getColumn(i).setCellRenderer(getTCR_Center());
			}
		}
		this.table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					int rowNum = ShowFlowerGrilInformation.this.table.getSelectedRow();
					int id = (int) ShowFlowerGrilInformation.this.table.getValueAt(rowNum, 1);
					CharacterData cd = CharacterData.get().get(id);
					if (cd != null) {
						ShowFlowerGrilInformation.this.skillPanel.showCharacter(cd);
					}
				}
			}
		});
		this.table.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				ShowFlowerGrilInformation.this.frame.requestFocusInWindow();
			}
		});

		JTable rowHeader = new JTable(this.rowHeaderModel);
		rowHeader.setRowHeight(this.rowHeight);
		rowHeader.getColumnModel().getColumn(0).setCellRenderer(getTCR_Center());
		this.jsc = new JScrollPane(this.table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.jsc.setRowHeaderView(rowHeader);

		this.initComponent();
		this.layoutFrame();
		this.updateTable(false);
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
		this.frame.getContentPane().add(this.skillPanel, BorderLayout.EAST);
	}

	/*-----------------------------------------------------------------------------------*/

	public void dispose() {
		this.frame.dispose();
		this.skillPanel.showStand.window.dispose();
		this.skillPanel.csv.shell.dispose();
	}

	private void updateTable(boolean clearSelection) {
		if (clearSelection) {
			Arrays.asList(new JComboBox[] { this.jcb_rarity, this.jcb_attribute, this.jcb_country, this.jcb_skilltype }).forEach(jcb -> jcb.setSelectedIndex(0));
		}

		ShowConfig.setOEBIndex(findIndex(this.jrb_oeb, JRadioButton::isSelected));
		ShowConfig.setUseChineseName(this.jcb_cn.isSelected());
		ShowConfig.setSortByBid(this.jcb_bid.isSelected());
		ShowConfig.setSortByBloomNumber(this.jcb_bloomnumber.isSelected());
		ShowConfig.setFavorIndex(findIndex(this.jrb_favor, JRadioButton::isSelected));
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

			// 设置头像栏的width，并且不可改变大小
			TableColumn tc0 = this.table.getColumnModel().getColumn(0);
			int iconWidth = 50;
			tc0.setMaxWidth(iconWidth);
			tc0.setMinWidth(iconWidth);
			tc0.setResizable(false);

			this.table.getTableHeader().setReorderingAllowed(false);	// 设置列不可拖动			
			this.table.setAutoCreateRowSorter(true);// 设置列可排序
		}
		int h = this.jsc.getVerticalScrollBar().getSize().height;
		this.jsc.getRowHeader().setPreferredSize(new Dimension(30, h));
	}

	/*-----------------------------------------------------------------------------------*/

	private void initComponent() {
		ButtonGroup bg_oeb = new ButtonGroup();
		this.jrb_oeb = new JRadioButton[OEBFilter.SIS.length];
		for (int i = 0; i < this.jrb_oeb.length; i++) {
			this.jrb_oeb[i] = new JRadioButton(OEBFilter.SIS[i].getString());
			this.jrb_oeb[i].setFocusPainted(false);
			this.jrb_oeb[i].addActionListener(ev -> this.updateTable(false));
			bg_oeb.add(this.jrb_oeb[i]);
		}
		this.jrb_oeb[ShowConfig.getOEBIndex()].setSelected(true);

		this.jcb_cn = new JCheckBox("中文名");
		this.jcb_cn.setFocusPainted(false);
		this.jcb_cn.setSelected(ShowConfig.isUseChineseName());
		this.jcb_cn.addActionListener(ev -> this.updateTable(false));

		this.jcb_bid = new JCheckBox("图鉴顺序");
		this.jcb_bid.setFocusPainted(false);
		this.jcb_bid.setSelected(ShowConfig.isSortByBid());
		this.jcb_bid.addActionListener(ev -> {
			this.scltm.fireTableStructureChanged();
			this.updateTable(false);
		});

		this.jcb_bloomnumber = new JCheckBox("开花顺序");
		this.jcb_bloomnumber.setFocusPainted(false);
		this.jcb_bloomnumber.setSelected(ShowConfig.isSortByBloomNumber());
		this.jcb_bloomnumber.addActionListener(ev -> {
			this.scltm.fireTableStructureChanged();
			this.updateTable(false);
		});

		ButtonGroup bg_favor = new ButtonGroup();
		this.jrb_favor = new JRadioButton[ShowConfig.STRING_FAVOR.length];
		for (int i = 0; i < this.jrb_favor.length; i++) {
			this.jrb_favor[i] = new JRadioButton(ShowConfig.STRING_FAVOR[i]);
			this.jrb_favor[i].setFocusPainted(false);
			this.jrb_favor[i].addActionListener(ev -> this.updateTable(false));
			bg_favor.add(this.jrb_favor[i]);
		}
		this.jrb_favor[ShowConfig.getFavorIndex()].setSelected(true);

		this.jcb_rarity = new JComboBox<>(Filter.toStringArray(RarityFilter.SIS));
		this.jcb_rarity.setEditable(false);
		this.jcb_rarity.setSelectedIndex(ShowConfig.getRarityIndex());
		this.jcb_attribute = new JComboBox<>(Filter.toStringArray(AttackAttributeFilter.SIS));
		this.jcb_attribute.setEditable(false);
		this.jcb_attribute.setSelectedIndex(ShowConfig.getAAIndex());
		this.jcb_country = new JComboBox<>(Filter.toStringArray(CountryFilter.SIS));
		this.jcb_country.setEditable(false);
		this.jcb_country.setSelectedIndex(ShowConfig.getCountryIndex());
		this.jcb_skilltype = new JComboBox<>(Filter.toStringArray(SkilltypeFilter.SIS));
		this.jcb_skilltype.setEditable(false);
		this.jcb_skilltype.setSelectedIndex(ShowConfig.getSTIndex());
		for (JComboBox<?> jcb : new JComboBox[] { this.jcb_rarity, this.jcb_attribute, this.jcb_country, this.jcb_skilltype }) {
			jcb.addActionListener(ev -> this.updateTable(false));
		}

		this.clear = new JButton("清除");
		this.clear.setFocusPainted(false);
		this.clear.addActionListener(ev -> this.updateTable(true));
	}

	private static TableCellRenderer getTCR_Center() {
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(JLabel.CENTER);
		return tcr;
	}

	private static <T> int findIndex(T[] ts, Predicate<T> infi) {
		for (int i = 0; i < ts.length; i++) {
			if (infi.test(ts[i])) {
				return i;
			}
		}
		return -1;
	}

	/**	 右侧显示skill	 */
	@SuppressWarnings("serial")
	public class SkillPanel extends JPanel {
		private class CharacterButton {
			private final int oeb;
			private final JRadioButton button;
			private CharacterData cd;

			public CharacterButton(int oeb, JRadioButton button) {
				this.oeb = oeb;
				this.button = button;
			}
		}

		private final CharacterButton[] cbs;

		private final JButton lihui;
		private final ShowPicture showStand;

		private final JButton story;
		public final CharacterStoryViewer csv;

		private final PicturePanel picturePanel;

		public SkillPanel() {
			this.picturePanel = new PicturePanel();
			this.setLayout(new BorderLayout());
			this.setPreferredSize(new Dimension(451, 707));
			/*-----------------------------------------------------------*/
			JPanel north = new JPanel();
			ButtonGroup bg = new ButtonGroup();

			String[] string_oeb = { "原始", "进化", "开花" };
			this.cbs = new CharacterButton[string_oeb.length];
			for (int i = 0; i < string_oeb.length; i++) {
				CharacterButton cb = this.cbs[i] = new CharacterButton(i + 1, new JRadioButton(string_oeb[i]));
				cb.button.setFocusPainted(false);
				cb.button.setEnabled(false);
				cb.button.addActionListener(ev -> this.reflash(cb.oeb));
				bg.add(cb.button);
				north.add(cb.button);
			}

			this.showStand = new ShowPicture(ShowConfig.CHARACTER_STAND, 960, 640);
			this.lihui = new JButton("立绘");
			this.lihui.setFocusPainted(false);
			this.lihui.addActionListener(ev -> {
				for (CharacterButton cb : this.cbs) {
					if (cb.button.isSelected()) {
						CharacterData cd = cb.cd;
						if (cd != null) {
							this.showStand.display(cd.getStand());
						}
					}
				}
			});
			north.add(this.lihui);

			this.csv = new CharacterStoryViewer(ShowFlowerGrilInformation.this.gui);
			this.story = new JButton("个人剧情");
			this.story.setFocusPainted(false);
			this.story.addActionListener(ev -> {
				for (CharacterButton cb : this.cbs) {
					if (cb.button.isSelected()) {
						CharacterData cd = cb.cd;
						if (cd != null) {
							this.csv.shell.getDisplay().asyncExec(() -> this.csv.show(cd));
						}
					}
				}
			});
			north.add(this.story);

			this.add(north, BorderLayout.NORTH);
			this.add(this.picturePanel, BorderLayout.CENTER);
		}

		private void reflash(int oeb) {
			this.cbs[oeb - 1].button.setSelected(true);
			this.picturePanel.reflash(this.cbs[oeb - 1].cd.getSkillImage());
		}

		public void showCharacter(CharacterData cd) {
			int oeb = cd.ci.getOeb();
			int id = cd.ci.getID() - (oeb == 2 ? 1 : (oeb == 3 ? 300000 : 0));

			this.cbs[0].cd = CharacterData.get().get(id);
			this.cbs[1].cd = CharacterData.get().get(id + 1);
			this.cbs[2].cd = CharacterData.get().get(id + 300000);

			for (int i = 0; i < this.cbs.length; i++) {
				this.cbs[i].button.setEnabled(this.cbs[i].cd != null);
				this.cbs[i].button.setSelected(false);
			}

			this.reflash(oeb);
		}

	}

	@SuppressWarnings("serial")
	private class PicturePanel extends JPanel {
		private Image image;

		public void reflash(Image image) {
			this.image = image;
			this.repaint();
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			if (this.image != null) {
				g.drawImage(this.image, 0, 0, this);
			}
		}
	}

	public class ShowPicture {
		private final JWindow window;
		private final PicturePanel picturePanel;

		public ShowPicture(String filedir, int width, int height) {
			this.picturePanel = new PicturePanel();

			this.window = new JWindow();
			this.window.setSize(width, height);
			this.window.setLocationRelativeTo(null);
			this.window.setAlwaysOnTop(true);
			this.window.setContentPane(this.picturePanel);
			this.window.setBackground(new Color(0, 0, 0, 0f));
			DragWindowMoveListener ma = new DragWindowMoveListener(this.window);
			this.window.addMouseListener(ma);
			this.window.addMouseMotionListener(ma);
		}

		public void display(BufferedImage image) {
			this.picturePanel.reflash(image);
			this.window.setVisible(true);
		}
	}

	public class CharacterStoryViewer {
		private class StoryButton {
			private String[] story;
			private final int index;
			private final Button button;

			public StoryButton(int index, Button button) {
				this.index = index;
				this.button = button;
			}
		}

		public final Shell shell;
		private final Text text;
		private final StoryButton[] storyButtons = new StoryButton[4];
		private int id = -1;

		public CharacterStoryViewer(FKGGui gui) {
			this.shell = new Shell(gui.display, SWT.ON_TOP | SWT.TOOL | SWT.CLOSE | SWT.TITLE | SWT.RESIZE);
			this.shell.setImage(gui.logo);
			this.shell.setLayout(new org.eclipse.swt.layout.GridLayout(1, false));
			this.shell.setLayoutData(new GridData(GridData.FILL_BOTH));
			this.shell.setSize(AppConfig.getStoryViewerSize());
			this.shell.setLocation(AppConfig.getStoryViewerLocation());
			this.shell.addShellListener(new ShellAdapter() {
				@Override
				public void shellClosed(ShellEvent ev) {
					ev.doit = false;
					CharacterStoryViewer.this.shell.setVisible(false);
				}
			});
			this.shell.addControlListener(new ControlAdapter() {
				@Override
				public void controlResized(ControlEvent arg0) {
					AppConfig.setStoryViewerSize(CharacterStoryViewer.this.shell.getSize());
				}

				@Override
				public void controlMoved(ControlEvent arg0) {
					AppConfig.setStoryViewerLocation(CharacterStoryViewer.this.shell.getLocation());
				}
			});

			Composite storyButtonComposite = new Composite(this.shell, SWT.BORDER);
			storyButtonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			storyButtonComposite.setLayout(new org.eclipse.swt.layout.GridLayout(4, false));
			for (int index = 0; index < this.storyButtons.length; index++) {
				StoryButton storyButton = new StoryButton(index + 1, new Button(storyButtonComposite, SWT.CHECK));
				storyButton.button.setText("个人剧情" + storyButton.index);
				storyButton.button.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true));
				storyButton.button.addSelectionListener(new ControlSelectionListener(ev -> {
					Arrays.stream(this.storyButtons).forEach(sb -> {
						if (sb != storyButton) {
							sb.button.setSelection(false);
						}
					});
					this.reflash(storyButton.index);
				}));
				this.storyButtons[index] = storyButton;
			}

			this.text = new Text(this.shell, SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL);
			this.text.setLayoutData(new GridData(GridData.FILL_BOTH));
			this.text.setFont(new Font(gui.display, "微软雅黑", 16, SWT.NORMAL));
		}

		public void show(CharacterData cd) {
			int oeb = cd.ci.getOeb();
			int id = cd.ci.getID() - (oeb == 2 ? 1 : (oeb == 3 ? 300000 : 0));
			if (this.id == id) {
				return;
			}

			String[][] storys = cd.getCharacterStory();
			for (int i = 0; i < 4; i++) {
				this.storyButtons[i].story = storys[i];
			}

			this.id = id;
			this.reflash(0);
			this.shell.setVisible(true);
		}

		private void reflash(int index) {
			if (index == 0) {
				index = 1;
				Arrays.stream(this.storyButtons).forEach(sb -> sb.button.setSelection(sb.index == 1));
			}

			for (StoryButton sb : this.storyButtons) {
				sb.button.setEnabled(sb.story != null);
				if (sb.button.getSelection()) {
					int show_id = this.id + (index == 3 ? 1 : (index == 4 ? 300000 : 0));
					this.shell.setText(FunctionUtils.notNull(sb.story[0], name -> String.format("%d - %s", show_id, name), ""));
					this.text.setText(FunctionUtils.notNull(sb.story[1], FunctionUtils::returnSelf, ""));
				}
			}
		}
	}

	public class DragWindowMoveListener extends MouseAdapter {
		private JWindow window;
		private java.awt.Point oldLocation;
		private java.awt.Point newLocation;
		private boolean closeWindow = false;
		private boolean allowDrag = false;

		public DragWindowMoveListener(JWindow window) {
			this.window = window;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				this.allowDrag = true;
				this.closeWindow = true;
				this.oldLocation = e.getPoint();
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (this.allowDrag) {
				this.closeWindow = false;
				this.newLocation = e.getPoint();
				this.window.setLocation(//
						(int) (this.window.getLocation().getX() + this.newLocation.getX() - this.oldLocation.getX()), //
						(int) (this.window.getLocation().getY() + this.newLocation.getY() - this.oldLocation.getY())//
				);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1 && this.closeWindow) {
				this.window.setVisible(false);
			}

			this.closeWindow = false;
			this.allowDrag = false;
		}
	}
}
