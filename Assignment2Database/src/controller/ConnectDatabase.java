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
		hasPermission();
	}
	
	public boolean hasPermission() {
		boolean permission = false;
		
		String sql = "{? = call CHECK_USER_PERMISSION_FN}";
		
		try {
			Connection conn = setConnection();
			cstmt = conn.prepareCall(sql);
			cstmt.registerOutParameter(1, Types.VARCHAR);
			cstmt.execute();
			String permissionString = cstmt.getString(1);
			System.out.println(permissionString);
			
			conn.close();
			cstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return permission;
	}
	
	private void getUserCredentials() {
		Scanner keyboard = new Scanner(System.in);
		
		//ask user for username and password
		System.out.print("Username: ");
		username = keyboard.nextLine();
		
		System.out.print("Password: ");
		password = keyboard.nextLine();
		
		keyboard.close();

	}
	
	private Connection setConnection() {
		
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				connection = DriverManager.getConnection(DATABASE_URL, username, password);
			} catch (ClassNotFoundException | SQLException e) {
				JOptionPane.showMessageDialog(null, "Invalid Username or Password! Please try again!");
			}
			
			return connection;
	}
	

}
