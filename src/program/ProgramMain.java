package program;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenu;
import javax.swing.JLayeredPane;

import java.awt.CardLayout;
import javax.swing.JTextField;

import database.DatabaseManager;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;

import java.awt.FlowLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.SwingConstants;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class ProgramMain extends JFrame {

	private final static String TAG_CONNECT = "CONNECT";
	private final static String TAG_DATABASES = "DATABASES";
	private final static String TAG_ADD_TABLE = "ADD_TABLE";
	
	private boolean isCreating;
	// To save old data from gotten table
	private Vector<Vector<String>> oldData;
	private int countRows;
	
	private JLayeredPane panel_slider;
	private CardLayout container;
	private JPanel panel_connect;
	private JTextField tF_pass;
	private DatabaseManager databaseManager;  
	private JTextField tF_user;
	private JPanel contentPane;
	private JLabel lbl_status;
	private JPanel panel_statusBar;
	private JList<String> list_databases;
	private JPanel panel_databases;
	private JList<String> list_tables;
	private JMenuItem mnIt_connect;
	private JPanel panel_table;
	private DefaultTableModel modelNewDBTable;
	private JTable table;
	private JMenu mn_database;
	private JMenu mn_table;
	private JMenu mn_rows;
	private JMenuItem mnIt_disconnect;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProgramMain frame = new ProgramMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ProgramMain() {
		setResizable(false);
		setTitle("Default - Database Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 500, 450);
		setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mn_connection = new JMenu("Connection");
		menuBar.add(mn_connection);
		
		mnIt_connect = new JMenuItem("Connect");
		mnIt_connect.addActionListener(new MntmConnectActionListener());
		mn_connection.add(mnIt_connect);
		
		mnIt_disconnect = new JMenuItem("Disconnect");
		mnIt_disconnect.addActionListener(new MnIt_disconnectActionListener());
		mnIt_disconnect.setVisible(false);
		mn_connection.add(mnIt_disconnect);
		
		mn_database = new JMenu("Database");
		mn_database.setEnabled(false);
		menuBar.add(mn_database);
		
		JMenuItem mntmDbShowList = new JMenuItem("Show list");
		mntmDbShowList.addActionListener(new MntmDbShowListActionListener());
		mn_database.add(mntmDbShowList);
		
		JMenuItem mntmDbCreate = new JMenuItem("Create");
		mntmDbCreate.addActionListener(new MntmDbCreateActionListener());
		mn_database.add(mntmDbCreate);
		
		JMenuItem mntmDbDrop = new JMenuItem("Drop");
		mntmDbDrop.addActionListener(new MntmDbDropActionListener());
		mn_database.add(mntmDbDrop);
		
		mn_table = new JMenu("Table");
		mn_table.setEnabled(false);
		menuBar.add(mn_table);
		
		JMenuItem mntmTabShowList = new JMenuItem("Show list");
		mntmTabShowList.addActionListener(new MntmTabShowListActionListener());
		mn_table.add(mntmTabShowList);
		
		JMenuItem mntmTabCreate = new JMenuItem("Create");
		mntmTabCreate.addActionListener(new MntmTabCreateActionListener());
		mn_table.add(mntmTabCreate);
		
		JMenuItem mntmTabDrop = new JMenuItem("Drop");
		mntmTabDrop.addActionListener(new MntmTabDropActionListener());
		mn_table.add(mntmTabDrop);
		
		mn_rows = new JMenu("Rows");
		mn_rows.setEnabled(false);
		menuBar.add(mn_rows);
		
		JMenuItem mntmAddRow = new JMenuItem("Add row");
		mntmAddRow.addActionListener(new MntmAddRowActionListener());
		mn_rows.add(mntmAddRow);
		
		JMenuItem mntmAddRows = new JMenuItem("Add rows");
		mntmAddRows.addActionListener(new MntmAddRowsActionListener());
		mn_rows.add(mntmAddRows);
		
		JMenuItem mntmDeleteRow = new JMenuItem("Delete row");
		mntmDeleteRow.addActionListener(new MntmDeleteRowActionListener());
		mn_rows.add(mntmDeleteRow);
		
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		// Contains all panels
		panel_slider = new JLayeredPane();
		panel_slider.setLayout(new CardLayout(0, 0));
		contentPane.add(panel_slider);
		
		panel_connect = new JPanel();
		panel_connect.addComponentListener(new Panel_connectComponentListener());
		panel_slider.add(panel_connect, TAG_CONNECT);
		panel_connect.setLayout(null);
		
		tF_user = new JTextField();
		tF_user.setBounds(246, 122, 114, 20);
		panel_connect.add(tF_user);
		tF_user.setColumns(10);
		
		JLabel lbl_username = new JLabel("Username:");
		lbl_username.setBounds(141, 124, 87, 16);
		panel_connect.add(lbl_username);
		
		tF_pass = new JTextField();
		tF_pass.setBounds(246, 154, 114, 20);
		panel_connect.add(tF_pass);
		tF_pass.setColumns(10);
		
		JLabel lbl_password = new JLabel("Password:");
		lbl_password.setBounds(141, 156, 87, 16);
		panel_connect.add(lbl_password);
		
		JButton btn_connect = new JButton("Connect");
		btn_connect.addActionListener(new BtnConnectActionListener());
		btn_connect.setBounds(192, 277, 98, 26);
		panel_connect.add(btn_connect);
		
		panel_databases = new JPanel();
		panel_databases.addComponentListener(new Panel_databasesComponentListener());
		panel_slider.add(panel_databases, TAG_DATABASES);
		panel_databases.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 93, 181, 263);
		panel_databases.add(scrollPane);
		
		list_databases = new JList<String>();
		list_databases.addMouseListener(new List_databasesMouseListener());
		list_databases.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(list_databases);
		
		JLabel lbl_databases = new JLabel("Databases");
		lbl_databases.setHorizontalAlignment(SwingConstants.LEFT);
		lbl_databases.setBounds(12, 65, 89, 16);
		panel_databases.add(lbl_databases);
		
		JButton btn_backDb = new JButton("Back");
		btn_backDb.addActionListener(new Btn_backDbActionListener());
		btn_backDb.setBounds(12, 12, 98, 26);
		panel_databases.add(btn_backDb);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(247, 93, 193, 263);
		panel_databases.add(scrollPane_3);
		
		JLabel lblTables = new JLabel("Tables");
		lblTables.setBounds(247, 65, 55, 16);
		panel_databases.add(lblTables);
		
		// For accessing to all panels
		container = (CardLayout) panel_slider.getLayout();
		
		list_tables = new JList<String>();
		list_tables.addMouseListener(new List_tablesMouseListener());
		scrollPane_3.setViewportView(list_tables);
		
		panel_table = new JPanel();
		panel_table.addComponentListener(new Panel_addTableComponentListener());
		panel_slider.add(panel_table, TAG_ADD_TABLE);
		panel_table.setLayout(null);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(12, 50, 470, 316);
		panel_table.add(scrollPane_2);
		
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowHeight(20);
		
		scrollPane_2.setViewportView(table);
		
		JButton btn_confirm = new JButton("Confirm");
		btn_confirm.addActionListener(new Btn_createNewTableActionListener());
		btn_confirm.setBounds(384, 13, 98, 26);
		panel_table.add(btn_confirm);
		
		JButton btn_backToTables = new JButton("Back");
		btn_backToTables.addActionListener(new Btn_backToTablesActionListener());
		btn_backToTables.setBounds(12, 15, 89, 23);
		panel_table.add(btn_backToTables);
		
		panel_statusBar = new JPanel();
		panel_statusBar.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		contentPane.add(panel_statusBar);
		FlowLayout fl_panel_statusBar = new FlowLayout(FlowLayout.LEFT, 5, 0);
		panel_statusBar.setLayout(fl_panel_statusBar);
		
		lbl_status = new JLabel("Disconnected");
		lbl_status.setHorizontalAlignment(SwingConstants.CENTER);
		panel_statusBar.add(lbl_status);
	}
	
	// Try to connect
	private class BtnConnectActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// Class for managing databases
			databaseManager = new DatabaseManager(tF_user.getText(),
					tF_pass.getText());
			databaseManager.connect();
			// If connected, get all databases
			if(databaseManager.isConnected()) {
				lbl_status.setText("Connection is successful");
				container.show(panel_slider, TAG_DATABASES);
			} else {
				lbl_status.setText("Connection is failed");
			}
		}
	}
	
	// Menu Item Connect
	private class MntmConnectActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Class for managing databases
			databaseManager = new DatabaseManager(tF_user.getText(),
					tF_pass.getText());
			databaseManager.connect();
			// If connected, get all databases
			if(databaseManager.isConnected()) {
				lbl_status.setText("Connection is successful");
				container.show(panel_slider, TAG_DATABASES);
			} else {
				lbl_status.setText("Connection is failed");
			}
		}
	}
	
	// Back to Connect
	private class Btn_backDbActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			container.show(panel_slider, TAG_CONNECT);
		}
	}
	
	// Confirm changes in Panel Table
	private class Btn_createNewTableActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if(isCreating) {
				// Get table name
				String tableName = JOptionPane.showInputDialog("Enter table name:");
				if(tableName != null) {
					if(isNameValid(tableName)) {
						Vector<String> rowName = new Vector<>();
						Vector<String> rowType = new Vector<>();
						// Get data from table
						for(int i = 0; i < modelNewDBTable.getRowCount(); ++i) {
							rowName.addElement(modelNewDBTable.getValueAt(i, 0).toString());
							rowType.addElement(modelNewDBTable.getValueAt(i, 1).toString());
						}
						// Execute query
						databaseManager.createTable(tableName, rowName, rowType);
						// Show status
						lbl_status.setText("Table " + tableName + " created");
						// Show Panel Tables
						container.show(panel_slider, TAG_DATABASES);
					} else {
						JOptionPane.showMessageDialog(contentPane, "Name is invalid", "Error", JOptionPane.ERROR_MESSAGE);
						lbl_status.setText("Table " + tableName + " not created");
					}
				}
			} else {
				int newCountRows = modelNewDBTable.getRowCount();
				// If added new rows, insert to table
				if(newCountRows > countRows) {
					for(int i = countRows; i < newCountRows; ++i) {
						Vector<String> row = new Vector<>();
						for(int j = 0; j < modelNewDBTable.getColumnCount(); ++j) {
							String data = modelNewDBTable.getValueAt(i, j).toString();
							if(data != null && !data.isEmpty()) {
								row.addElement(data);
							}
						}
						databaseManager.insertRow(list_tables.getSelectedValue(), row);
					}
				}
				// Update changed rows
				updateChangedRows();
				// Show Panel Tables
				container.show(panel_slider, TAG_DATABASES);
			}
		}
	}
	
	// Back From Table to Lists
	private class Btn_backToTablesActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Show Panel Tables
			container.show(panel_slider, TAG_DATABASES);
			lbl_status.setText("Connected");
		}
	}
	
	// Panel Table
	private class Panel_addTableComponentListener extends ComponentAdapter {
		// When Hidden
		@Override
		public void componentHidden(ComponentEvent arg0) {
			if(modelNewDBTable != null) {
				modelNewDBTable = new DefaultTableModel();
				table.setModel(modelNewDBTable);
			}
			isCreating = false;
			mn_rows.setEnabled(false);
		}
		// When Shown
		@Override
		public void componentShown(ComponentEvent arg0) {
			mn_rows.setEnabled(true);
			if(isCreating) {
				setModelNewTable();
			} else {
				setModelShowTable();
			}
		}
	}
	
	// Panel Connection
	private class Panel_connectComponentListener extends ComponentAdapter {
		// When Panel Connection Shown
		@Override
		public void componentShown(ComponentEvent e) {
			// Disconnect
			databaseManager.disconnect();
			// Show status
			lbl_status.setText("Disconnected");
			// Change enabling in menu
			mn_table.setEnabled(false);
			mn_database.setEnabled(false);
			mnIt_connect.setVisible(true);
			mnIt_disconnect.setVisible(false);
		}
		
		// When Panel Connection Hidden
		@Override
		public void componentHidden(ComponentEvent e) {
			mnIt_connect.setVisible(false);
			mnIt_disconnect.setVisible(true);
		}
	}
	
	// When Panel Databases shown
	private class Panel_databasesComponentListener extends ComponentAdapter {
		@Override
		public void componentShown(ComponentEvent e) {
			String db = list_databases.getSelectedValue(); 
			refreshListDatabases();
			list_tables.setModel(new DefaultListModel<>());
			if(databaseManager.isDatabaseUsed) {
				String table = list_tables.getSelectedValue();
				mn_table.setEnabled(true);
				refreshListTables();
				list_tables.setSelectedValue(table, true);
			}
			mn_database.setEnabled(true);
			mn_rows.setEnabled(false);
			list_databases.setSelectedValue(db, true);
		}
	}
	
	// DoubleClick on list of databases item
	private class List_databasesMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent mEvent) {
			if(mEvent.getClickCount() == 2) {
				String database = list_databases.getSelectedValue();
				if(!database.isEmpty()) {
					databaseManager.useDB(database);
					// Refresh list of Tables
					refreshListTables();
					mn_table.setEnabled(true);
					lbl_status.setText("Database " + database + " used");
				}
			}
		}
	}
	
	// Create new Database
	private class MntmDbCreateActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			String database = JOptionPane.showInputDialog("Enter database name");
			if(database != null) {
				if(isNameValid(database)) {
					databaseManager.createDB(database);
					refreshListDatabases();
					lbl_status.setText("Database " + database + " created");
				} else {
					JOptionPane.showMessageDialog(contentPane, "Name is invalid", "Error", JOptionPane.ERROR_MESSAGE);
					lbl_status.setText("Database " + database + " not created");
				}
			}
		}
	}
	
	// Drop database
	private class MntmDbDropActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(!list_databases.isSelectionEmpty()) {
				String database = list_databases.getSelectedValue();
				databaseManager.dropDB(database);
				refreshListDatabases();
				lbl_status.setText("Database " + database + " deleted");
				list_tables.setModel(new DefaultListModel<>());
				mn_table.setEnabled(false);
			}
		}
	}
	
	// Show list of databases
	private class MntmDbShowListActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			container.show(panel_slider, TAG_DATABASES);
		}
	}
	
	// Show list of tables
	private class MntmTabShowListActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(databaseManager.isDatabaseUsed) {
				refreshListTables();
				container.show(panel_slider, TAG_DATABASES);
			}
		}
	}
	
	// Create new Table
	private class MntmTabCreateActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			container.show(panel_slider, TAG_ADD_TABLE);
			isCreating = true;
		}
	}
	
	// Drop Table
	private class MntmTabDropActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(!list_tables.isSelectionEmpty()) {
				String table = list_tables.getSelectedValue();
				databaseManager.dropTable(table);
				refreshListTables();
				lbl_status.setText("Table " + table + " deleted");
			}
		}
	}
	
	// Add new row
	private class MntmAddRowActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			modelNewDBTable.addRow(new Object[]{});
		}
	}
	
	// Add many rows
	private class MntmAddRowsActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String count = JOptionPane.showInputDialog("Enter count:");
			if(!count.isEmpty()) {
				for(int i = 0; i < Integer.parseInt(count); ++i) {
					modelNewDBTable.addRow(new Object[]{});
				}
			}
		}
	}
	
	// Delete row
	private class MntmDeleteRowActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(table.getSelectedRow() == -1) {
				return;
			}
			databaseManager.deleteRow(list_tables.getSelectedValue(),
					table.getColumnName(0), 
					table.getValueAt(table.getSelectedRow(), 0).toString());
			setModelShowTable();
		}
	}
	
	// Menu Item Disconnect
	private class MnIt_disconnectActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			container.show(panel_slider, TAG_CONNECT);
		}
	}
	
	// DoubleClick on table
	private class List_tablesMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent mouseEvent) {
			if(mouseEvent.getClickCount() == 2) {
				if(!list_tables.isSelectionEmpty()) {
					String table = list_tables.getSelectedValue();
					isCreating = false;
					container.show(panel_slider, TAG_ADD_TABLE);
					lbl_status.setText("Table " + table + " used");
				}
			}
		}
	}
	
	// Getting list of databases
	private void refreshListDatabases() {
		// For sending list to JList
		DefaultListModel<String> model = new DefaultListModel<>();
		// Contains database list
		Vector<String> databases = databaseManager.showDB();
		if(databases != null) {
			// Getting list of databases
			for(String db : databases) {
				model.addElement(db);
			}
		}
		// JList contains list model
		list_databases.setModel(model);
	}
	
	// Getting list of tables
	private void refreshListTables() {
		if(databaseManager.isDatabaseUsed) {
			DefaultListModel<String> model = new DefaultListModel<>();
			Vector<String> tables = databaseManager.showTables();
			if(tables != null) {
				for(String table : tables) {
					model.addElement(table);
				}
			}
			list_tables.setModel(model);
		}
	}
	
	// Setting model for creating new table
	private void setModelNewTable() {
		modelNewDBTable = new DefaultTableModel();
		modelNewDBTable.addColumn("Name");
		modelNewDBTable.addColumn("Type");
		table.setModel(modelNewDBTable);
		
		// Column Name
		TableColumn columnName = table.getColumnModel().getColumn(0);
		columnName.setCellEditor(new DefaultCellEditor(new JTextField()));
		// Column Type
		TableColumn columnType = table.getColumnModel().getColumn(1);
		JComboBox<String> comboBox = new JComboBox<>();
		comboBox.addItem("CHAR");
		comboBox.addItem("TEXT");
		comboBox.addItem("INT");
		comboBox.addItem("FLOAT");
		
		columnType.setCellEditor(new DefaultCellEditor(comboBox));
		
		modelNewDBTable.addRow(new Object[]{});
	}
	
	// Setting model for showing table
	private void setModelShowTable() {
		if (!list_tables.isSelectionEmpty()) {
			oldData = new Vector<>();
			modelNewDBTable = new DefaultTableModel();
			Vector<String> columns = databaseManager.getColumnsName(list_tables.getSelectedValue());
			if (columns != null) {
				Vector<Vector<String>> data = new Vector<>();
				data = databaseManager.selectTable(list_tables.getSelectedValue());
				modelNewDBTable.setDataVector(data, columns);
				for(int i = 0; i < modelNewDBTable.getRowCount(); ++i) {
					Vector<String> row = new Vector<>();
					for(int j = 0; j < modelNewDBTable.getColumnCount(); ++j) {
						row.addElement(modelNewDBTable.getValueAt(i, j).toString());
					}
					oldData.addElement(row);
				}
				table.setModel(modelNewDBTable);
				// For insert new rows
				countRows = modelNewDBTable.getRowCount();
			} 
		}
	}
	
	// Verification correctness name for db or table
	private boolean isNameValid(String name) {
		if(name.length() == 0 || !Character.isJavaIdentifierStart(name.charAt(0))) {
			return false;
		}
		for(int i = 1; i < name.length(); ++i) {
			if(!Character.isJavaIdentifierPart(name.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	// Get all rows, compare with old rows and update if is smth new
	private void updateChangedRows() {
		for(int i = 0; i < modelNewDBTable.getRowCount(); ++i) {
			if(i >= oldData.size()) {
				return;
			}
			// Number of column, on which is unchanged cell
			int unchangedCell = -1;
			// Number of row, on which is row to update 
			int isChangedRow = -1;
			Vector<String> row = new Vector<>();
			// Check row for changed: if true returns column, false - -1;
			for(int j = 0; j < modelNewDBTable.getColumnCount(); ++j) {
				String oldCell = oldData.elementAt(i).elementAt(j);
				String newCell = modelNewDBTable.getValueAt(i, j).toString();
				if(oldCell.equals(newCell)) {
					unchangedCell = j;
				} else {
					isChangedRow = i;
				}
				row.add(newCell);
			}
			if(isChangedRow != -1 && unchangedCell != -1) {
				String[] notChanged = new String[2];
				notChanged[0] = modelNewDBTable.getColumnName(unchangedCell);
				notChanged[1] = modelNewDBTable.getValueAt(isChangedRow, unchangedCell).toString();
				databaseManager.updateRow(list_tables.getSelectedValue(), row, notChanged);
			}
		}
	}
}
