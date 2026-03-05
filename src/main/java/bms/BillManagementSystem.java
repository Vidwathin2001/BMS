package bms;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;

public class BillManagementSystem {
	JFrame jf;
	JLabel head=new JLabel("Billing System");
	JSpinner date=new JSpinner(new SpinnerDateModel());
	JTextField name=new JTextField(15);
	JPanel header=new JPanel();
	JPanel center=new JPanel();
	JPanel botom=new JPanel();
	JLabel cDate=new JLabel("Date: ");
	JTextField Addr=new JTextField(15);
	JTextField ph=new JTextField(15);
	JTextField txtbno=new JTextField(15);
	JTextField qty=new JTextField(15);
	JButton add=new JButton("Add");
	DefaultTableModel model;
	JTable table;
	JComboBox<String> listitem=new JComboBox<>();
	JTextField txtemail=new JTextField(15);
	JTextField txttotal=new JTextField(15);
	JTextField txtgst=new JTextField(15);
	JTextField txtgrand=new JTextField(15);
	JButton btnsave=new JButton("Save");
	int billno=0;
	static Connection connection;
	
	
	
	public static void get_connection() throws SQLException {
		try {
		connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/bms","root","ctti");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	BillManagementSystem() throws SQLException{
		jf=new JFrame("Bill Management System");
		jf.setLayout(new BorderLayout());
		get_connection();
		getitem();
		getBillNo();
		header.setLayout(new FlowLayout(FlowLayout.CENTER));
		Font font1=new Font("Arial",Font.BOLD,30);
		head.setFont(font1);
		JMenu m_menu,m_menu1;
		JMenuItem i1,i2,i3;
		JMenuBar jmb=new JMenuBar();
		center.setLayout(new javax.swing.BoxLayout(center, javax.swing.BoxLayout.Y_AXIS));
		JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
		String[] columns = {"Item", "Quantity","Price","Total price","Delete"};
		model=new DefaultTableModel(columns,0);
		table=new JTable(model);
		JScrollPane sp = new JScrollPane(table);
		sp.setPreferredSize(new java.awt.Dimension(700, 200));
		txtgst.setText("18");
		txtgst.setEditable(false);
		
		m_menu=new JMenu("File");
		m_menu1=new JMenu("Logout");
		
		i1=new JMenuItem("New");
		i2=new JMenuItem("Save");
		i3=new JMenuItem("Open");
		m_menu.add(i1);
		m_menu.add(i2);
		m_menu.add(i3);
		
		jmb.add(m_menu);
		jmb.add(m_menu1);
		
		header.add(head);
		jf.add(header,BorderLayout.NORTH);
		
		JPanel row1=new JPanel();
		row1.setLayout(new FlowLayout(FlowLayout.LEFT));
		row1.add(new JLabel("Bill No: "));
		row1.add(txtbno);
		row1.add(Box.createHorizontalStrut(50));
		row1.add(cDate);
		row1.add(date);
		
		JPanel row2=new JPanel();
		row2.setLayout(new FlowLayout(FlowLayout.CENTER));
		row2.add(Box.createVerticalStrut(50));
		row2.add(new JLabel("Name: "));
		row2.add(name);
		row2.add(Box.createHorizontalStrut(50));
		row2.add(new JLabel("Phone: "));
		row2.add(ph);
		row2.add(Box.createHorizontalStrut(50));
		row2.add(new JLabel("Address: "));
		row2.add(Addr);
		
		JPanel row3=new JPanel();
		row3.setLayout(new FlowLayout(FlowLayout.CENTER));
		row3.add(Box.createVerticalStrut(50));
		row3.add(new JLabel("Item: "));
		row3.add(listitem);
		row3.add(Box.createHorizontalStrut(50));
		row3.add(new JLabel("Quantity: "));
		row3.add(qty);
		row3.add(Box.createHorizontalStrut(50));
		row3.add(add);
		
		center.add(row1);
		center.add(row2);
		center.add(row3);
		wrapper.add(center);
		
		center.add(Box.createVerticalStrut(30));
		center.add(sp);
		
		botom.setLayout(new FlowLayout(FlowLayout.CENTER));
	center.add(Box.createVerticalStrut(30));		
		txttotal.setColumns(10);
		botom.add(new JLabel("Total: "));
		botom.add(txttotal);
		botom.add(new JLabel("GST: "));
		botom.add(txtgst);
		botom.add(new JLabel("Grand Total: "));
		botom.add(txtgrand);
		botom.add(btnsave);
		center.add(botom);
		
		btnsave.addActionListener(e->saveDetails());
		
		
		add.addActionListener(e->load_itemsandprcie());
		table.addMouseListener(new java.awt.event.MouseAdapter(){
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int row=table.rowAtPoint(e.getPoint());
				int column=table.columnAtPoint(e.getPoint());
				
				if(column==4) {
					deleteRow(row);
				}
			}
		});
		
		jf.add(wrapper,BorderLayout.CENTER);
		jf.setJMenuBar(jmb);
		jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
		jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}
	
	public void saveDetails() {
		if(name==null) {
			JOptionPane.showMessageDialog(jf,"Please enter name");return;
		}else if(ph==null) {
			JOptionPane.showMessageDialog(jf,"Please enter phone number");return;
		}else if(Addr==null) {
			JOptionPane.showMessageDialog(jf,"Please enter Address");return;
		}else if(txtgrand==null) {
			JOptionPane.showMessageDialog(jf,"Please add items and quantity");return;
		}
		java.util.Date sdate=(java.util.Date) date.getValue();
		java.sql.Date sqldate=new java.sql.Date(sdate.getTime());
		try {
			String query="insert into bills values(?,?,?,?,?,?,?,?);";
			PreparedStatement stmt=connection.prepareStatement(query);
			stmt.setInt(1, Integer.parseInt(txtbno.getText()));
			stmt.setDate(2, sqldate);
			stmt.setString(3, name.getText());
			stmt.setString(4, ph.getText());
			stmt.setString(5, Addr.getText());
			stmt.setString(6, txttotal.getText());
			stmt.setInt(7, Integer.parseInt(txtgst.getText()));
			stmt.setString(8, txtgrand.getText());
			
			stmt.executeUpdate();
			JOptionPane.showMessageDialog(jf, "Saved");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

public void load_itemsandprcie() {
	try {
		String Query="select * from items";
		Statement stmt=connection.createStatement();
		String item=(String) listitem.getSelectedItem();
		String Quantity=qty.getText();
		ResultSet rs=stmt.executeQuery(Query);
		int price=0;
		
		while(rs.next()) {
			if(rs.getString("item_name").equals(item)) {
				price=rs.getInt("price");
			}
		}
		int qnty=Integer.parseInt(Quantity);
		double total=qnty*price;
		
		if(!Quantity.isEmpty()) {
			model.addRow(new Object[] {item,Quantity,price,total,"Delete"});
			qty.setText("");
			calculate_total();
		}
		
	}catch(Exception e) {
		e.printStackTrace();
	}
}
	
	public void deleteRow(int row) {
		model.removeRow(row);
		calculate_total();
	}

	public void calculate_total() {
		double sum=0;
		for(int i=0;i<table.getRowCount();i++) {
			sum+=Double.parseDouble(table.getValueAt(i,3).toString());
		}
		txttotal.setText(String.valueOf(sum));
		
		int gst=Integer.parseInt(txtgst.getText().toString());
		Double gtotal=sum+((sum*gst)/100);
		txtgrand.setText(String.valueOf(gtotal));
	}

	public void getitem() {
		String item=(String) listitem.getSelectedItem();
		String Quantity=qty.getText();
		try {
			String Query="select * from items";
			Statement stmt=connection.createStatement();
			ResultSet rs=stmt.executeQuery(Query);
			
			while(rs.next()) {
				listitem.addItem(rs.getString("item_name"));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void getBillNo() {
		try {
			String query="select no from lastbillno";
			Statement stmt=connection.createStatement();
			ResultSet rs=stmt.executeQuery(query);
			
			while(rs.next()) {
				billno=rs.getInt("no");
			}
			if(billno>=0) {
				txtbno.setText(String.valueOf(billno)+1);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws SQLException {
		try {
			new BillManagementSystem();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
