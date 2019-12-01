package controller;

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
	
	
	public ConnectDatabase() {
		getUserCredentials();
		getConnection();
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
			JOptionPane.showMessageDialog(null, "Diconnected from the database.");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Something went wrong closing the database connection.");
			
		}
	}
	

}
