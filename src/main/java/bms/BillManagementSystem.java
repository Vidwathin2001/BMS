package bms;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	
	BillManagementSystem() throws SQLException{
		jf=new JFrame("Bill Management System");
		jf.setLayout(new BorderLayout());
		get_connection();
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
		
		add.addActionListener(e->getitem());
		
		jf.add(wrapper,BorderLayout.CENTER);
		jf.setJMenuBar(jmb);
		jf.setSize(1600,860);
		jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}

	private void get_connection() throws SQLException {
		try(Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bms","root","ctti")){
			//JOptionPane.showMessageDialog(jf, "Connected");
			String Query="select * from items";
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(Query);
			
			while(rs.next()) {
				String items=rs.getString("item_name");
				listitem.addItem(items);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void getitem() {
		String item=(String) listitem.getSelectedItem();
		String Quantity=qty.getText();
		
		if(!Quantity.isEmpty()) {
			model.addRow(new Object[] {item,Quantity});
			qty.setText("");
		}
	}
	
	public static void main(String[] args) throws SQLException {
		try {
			new BillManagementSystem();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
