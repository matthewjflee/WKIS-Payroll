package controller;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

import javax.swing.JOptionPane;

//Connect user to the WKIS Database
public class ConnectDatabase {
	
	private final String DATABASE_URL = "jdbc:oracle:thin:@localhost:1521:XE";
	private Connection conn;
	private Connection connection;
	private CallableStatement cstmt;
	private String username;
	private String password;
	private String txtFileName;
	private String ctrlFile;
	private String path;
	private String logFile;
	private String exportFilePath;
	private String delimitedFile;
	private String aliasPath;
	private int exitValue;
	
	//Connect database
	public ConnectDatabase() {
		getUserCredentials();
		getConnection();
	}
	
	//Grab user credential input
	private void getUserCredentials() {
		Scanner keyboard = new Scanner(System.in);
		
		username = JOptionPane.showInputDialog("Username: ");
		password = JOptionPane.showInputDialog("Password: ");
		
		keyboard.close();

	}
	
	//Get database connection
	private Connection getConnection() {
		
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				connection = DriverManager.getConnection(DATABASE_URL, username, password);
			} catch (ClassNotFoundException | SQLException e) {
				JOptionPane.showMessageDialog(null, "Invalid Username or Password! Please try again!");
				System.exit(0);
			}
			return connection;
	}
	
	//Close database connection
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Something went wrong closing the database connection.");
			
		}
	}
	
    //Check if the user has permissions
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
	    
	//Create the control file
    public void createControlFile() {
    	txtFileName = JOptionPane.showInputDialog("Please enter filename of payroll file:");
    	path = JOptionPane.showInputDialog("Please enter location for the files:");
    	String[] textFileNameSplit = txtFileName.split("\\.");
    	logFile = textFileNameSplit[0] + ".log";
    	setCtrlFile();
    	
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
			e.printStackTrace();
		}	
    }
    
    //Create the control file
    private void setCtrlFile() {
    	String[] textFileNameSplit = txtFileName.split("\\.");
    	
    	ctrlFile = textFileNameSplit[0] + ".ctl";
    }

    //Load table from file with SQL Loader
    public int loadTable() {
        String sqlldr = "sqlldr userid=" + username + "/" + password + " control="
                        + path + "/" + ctrlFile + " log=" + path + "/" + logFile;
        
        	try {
        		Runtime rt = Runtime.getRuntime();
            	Process proc = rt.exec(sqlldr);
				exitValue = proc.waitFor();
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
        	return exitValue;
    }
    
	//Perform month end operations
	public void performMonthEnd() {
		String sql = "{CALL MONTH_END_SP}";
		
		try {
			conn = getConnection();
			cstmt = conn.prepareCall(sql);
			cstmt.execute();

			conn.close();
			cstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//User input for export file information
	public void getExportFileInfo() {
		this.exportFilePath = JOptionPane.showInputDialog("Directory file path: ");
		this.exportFilePath = "\'" + this.exportFilePath + "\'";
		this.delimitedFile = JOptionPane.showInputDialog("Exported file name: ");
		this.aliasPath = JOptionPane.showInputDialog("File path alias: ").toUpperCase();
	}
	
	//Create the directory alias
	public boolean createDirectory() {
		String sql = "CREATE OR REPLACE DIRECTORY " + aliasPath +  " AS " + exportFilePath;
		int rowsAffected = -3;
		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			rowsAffected = ps.executeUpdate();
			
			ps.close();
			conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return rowsAffected == 0;
	}
	
	//Create delimited file
	public void createDelimitedFile() {
		String sql = "{CALL WRITE_FILE_SP(?,?)}";
		
		try {
			conn = getConnection();
			cstmt = conn.prepareCall(sql);
			cstmt.setString(1, aliasPath);
			cstmt.setString(2, delimitedFile);
			
			cstmt.execute();
			conn.close();
			cstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
