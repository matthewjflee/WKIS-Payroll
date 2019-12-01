package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.ConnectDatabase;


public class WKISGui extends JFrame {

	private static final long serialVersionUID = -8456621528234132136L;
	private JLabel lblStepOne;
	private JButton btnCheck;
	
	private JLabel lblStepTwo;
	private JButton btnProcess;
	
	private JLabel lblStepThree;
	private JButton btnPerform;
	
	private JLabel lblStepFour;
	private JButton btnExport;
	
	private JButton btnClose;
	
	private Container content;
	
	private ConnectDatabase cb;
	
	public WKISGui() {
		super("Integration Assignment");
		this.setBounds(100,100,600,250);
		content = this.getContentPane();
		JPanel mainPanel = new JPanel(new BorderLayout(10,10));
		content.add(mainPanel);
		content.add(createCenterPanel(), BorderLayout.CENTER);
		content.add(createNorthPanel(), BorderLayout.NORTH);
		content.add(createSouthPanel(), BorderLayout.SOUTH);
		content.add(createEastPanel(), BorderLayout.EAST);
		content.add(createWestPanel(), BorderLayout.WEST);
		
		cb = new ConnectDatabase();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	private JPanel createWestPanel() {
		JPanel westPanel = new JPanel();
		westPanel.setBounds(100, 100, 50, 50);
		return westPanel;
	}

	private Component createEastPanel() {
		JPanel eastPanel = new JPanel();
		eastPanel.setBounds(100, 100, 200, 200);
		return eastPanel;
	}

	private Component createSouthPanel() {
		JPanel southPanel = new JPanel();
		
		btnClose = new JButton("Close");
		southPanel.add(btnClose);
		
		return southPanel;
	}

	private Component createNorthPanel() {
		JPanel northPanel = new JPanel();
		JLabel title = new JLabel("WKIS Payroll");
		northPanel.setBounds(100, 100, 50, 50);
		northPanel.add(title);
		return northPanel;
	}

	private JPanel createCenterPanel() {
		JPanel centerPanel = new JPanel();
		
		centerPanel.add(centerPanelGrid(), new BorderLayout());
		
		return centerPanel;
		
	}
	
	private JPanel centerPanelGrid() {
		JPanel centerCenter = new JPanel();
		centerCenter.setLayout(new GridLayout(6,2));
		
		lblStepOne = new JLabel("Step 1: Database Login and Check");
		btnCheck = new JButton("Check");
		btnCheck.addActionListener(e -> {
			String permission = cb.hasPermission();
				
			if(permission.equals("N")) {
				JOptionPane.showMessageDialog(null, "You do not have permission to continue.");
				System.exit(0);
			}
			else {
				JOptionPane.showMessageDialog(null, "Everything looks good. Please continue.");
			}
		});
		
		lblStepTwo = new JLabel("Step 2: Process Delimited Text File");
		btnProcess = new JButton("Process");
		
		lblStepThree = new JLabel("Step 3: Perform Month End");
		btnPerform = new JButton("Perform");
		
		lblStepFour = new JLabel("Step 4: Export Data");
		btnExport = new JButton("Export");

		centerCenter.add(lblStepOne);
		centerCenter.add(btnCheck);
		centerCenter.add(lblStepTwo);
		centerCenter.add(btnProcess);
		centerCenter.add(lblStepThree);
		centerCenter.add(btnPerform);
		centerCenter.add(lblStepFour);
		centerCenter.add(btnExport);
		
		
		return centerCenter;
	}
}
