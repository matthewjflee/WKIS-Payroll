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
	
	private Connection conn;
	private Connection connection;
	private CallableStatement cstmt;
	private final String DATABASE_URL = "jdbc:oracle:thin:@localhost:1521:XE";
	private String username;
	private String password;
	
	private String txtFileName;
	private String ctrlFile;
	private String path;
	private String logFile;
	private int exitValue;
	
	private String exportFilePath;
	private String delimitedFile;
	private String aliasPath;
	
	//Connect database
	public ConnectDatabase() {
		getUserCredentials();
		getConnection();
	}
	    
	///Create the control file
    public void createControlFile() {
    	setTxtFileName();
    	setPath();
    	setLogFile();
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
    

    //Set text file name
    private void setTxtFileName() {
    	txtFileName = JOptionPane.showInputDialog("Please enter filename of payroll file:");
    }
    
    //Set path for the files
    private void setPath() {
    	path = JOptionPane.showInputDialog("Please enter location for the files:");
    }
    
    //Create the control file
    private void setCtrlFile() {
    	System.out.println(txtFileName);
    	String[] textFileNameSplit = txtFileName.split("\\.");
    	
    	for(int i = 0; i < textFileNameSplit.length; i++) {
    		System.out.println(textFileNameSplit[i]);
    	}
    	
    	ctrlFile = textFileNameSplit[0] + ".ctl";
    }
    
    //Create the log file
    private void setLogFile() {
    	
    	String[] textFileNameSplit = txtFileName.split("\\.");
    	logFile = textFileNameSplit[0] + ".log";

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
	
	//User input for export file information
	public void getExportFileInfo() {
		this.exportFilePath = JOptionPane.showInputDialog("Directory file path: ");
		this.exportFilePath = "\'" + this.exportFilePath + "\'";
		System.out.println(this.exportFilePath);
		this.delimitedFile = JOptionPane.showInputDialog("Exported file name: ");
		this.aliasPath = JOptionPane.showInputDialog("File path alias: ").toUpperCase();
		System.out.println(aliasPath);
	}
	
	//Create the directory alias
	public boolean createDirectory() {
		String sql = "CREATE OR REPLACE DIRECTORY " + aliasPath +  " AS " + exportFilePath;
		int rowsAffected = -3;
		System.out.println(sql);
		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			System.out.println(aliasPath + "\t" + exportFilePath);
			rowsAffected = ps.executeUpdate();
			System.out.println(rowsAffected);
			
			ps.close();
			conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return rowsAffected == 0;
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
	
	//Grab user credential input
	private void getUserCredentials() {
		Scanner keyboard = new Scanner(System.in);
		
		username = JOptionPane.showInputDialog("Username: ");
		password = JOptionPane.showInputDialog("Password: ");
		
		keyboard.close();

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
	

}
