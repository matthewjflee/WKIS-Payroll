package controller;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Scanner;

import javax.swing.JOptionPane;

import model.User;

public class ConnectDatabase {
	
	private Connection conn;
	private Connection connection;
	private Statement smt;
	private PreparedStatement pstmt;
	private CallableStatement cstmt;
	private ResultSet rs;
	private final String DATABASE_URL = "jdbc:oracle:thin:@localhost:1521:XE";
	private String username;
	private String password;
	
	private String txtFileLocation;
	private String txtFileName;
	private String ctrlFile;
	private String path;
	private String logFile;
	private User user;
	private int exitValue;
	
	
	public ConnectDatabase() {
		getUserCredentials();
		getConnection();
//		createControlFile();
//		loadTable();
	}
	    
    public void createControlFile() {
    	setTxtFileName();
    	setPath();
    	setLogFile();
    	setCtrlFile();
//    	setLogFile();
    	
    	try {
			FileWriter fw = new FileWriter(path + "\\" + ctrlFile);
			PrintWriter pw = new PrintWriter(fw);
			
			pw.println("LOAD DATA");
			pw.println("INFILE " + "\'" + path + "\\" + txtFileName + "\'");
			pw.println("REPLACE");
			pw.println("INTO TABLE payroll_load");
			pw.println("FIELDS TERMINATED BY \';\' OPTIONALLY ENCLOSED BY \'\"\'");
			pw.println("TRAILING NULLCOLS");
			pw.println("(payroll_date DATE \"Month dd, yyyy\",");
			pw.println("employee_id,");
			pw.println("amount,");
			pw.println("status)");
			
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }

    public int loadTable() {
        String sqlldr = "sqlldr userid=" + username + "/" + password + " control="
                        + path + "/" + ctrlFile + " log=" + path + "/" + logFile;
        
        	try {
        		Runtime rt = Runtime.getRuntime();
            	Process proc = rt.exec(sqlldr);
				exitValue = proc.waitFor();
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	return exitValue;
    }
    

    private void setTxtFileName() {
    	txtFileName = JOptionPane.showInputDialog("Please enter the name of the delimited text file (please include .txt):");
    }
    
    private void setPath() {
    	path = JOptionPane.showInputDialog("Please enter location for the files to be stored:");
    }
    
    private void setCtrlFile() {
    	System.out.println(txtFileName);
    	String[] textFileNameSplit = txtFileName.split("\\.");
    	
    	for(int i = 0; i < textFileNameSplit.length; i++) {
    		System.out.println(textFileNameSplit[i]);
    	}
    	
    	ctrlFile = textFileNameSplit[0] + ".ctl";
    }
    
    private void setLogFile() {
    	
    	String[] textFileNameSplit = txtFileName.split("\\.");
    	logFile = textFileNameSplit[0] + ".log";

    }
	
	public String hasPermission() {
		String permission = null;
		
		String sql = "{? = call CHECK_USER_PERMISSION_FN}";
		
		try {
			conn = getConnection();
			cstmt = conn.prepareCall(sql);
			cstmt.registerOutParameter(1, Types.VARCHAR);
			cstmt.execute();
			permission = cstmt.getString(1);
			
			conn.close();
			cstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return permission;
	}
	
	private void getUserCredentials() {
		Scanner keyboard = new Scanner(System.in);
		
		username = JOptionPane.showInputDialog("Username: ");
		password = JOptionPane.showInputDialog("Password: ");
		
		keyboard.close();

	}
	
	private Connection getConnection() {
		
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				connection = DriverManager.getConnection(DATABASE_URL, username, password);
				JOptionPane.showMessageDialog(null, "Successfully Connected");
			} catch (ClassNotFoundException | SQLException e) {
				JOptionPane.showMessageDialog(null, "Invalid Username or Password! Please try again!");
				System.exit(0);
			}
			return connection;
	}
	
	public void closeConnection() {
		try {
			connection.close();
			JOptionPane.showMessageDialog(null, "Disconnected from the database.");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Something went wrong closing the database connection.");
			
		}
	}
	

}
