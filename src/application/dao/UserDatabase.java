/**********************************************
Project: Hotel Reservation System
Course:APD545 - Semester 5
Last Name: Rahman
First Name: Aditya
ID: 046207130
Section: NAA
This assignment represents my own work in accordance with Seneca Academic Policy.
Signature - Aditya Rahman
Date: August 13, 2023
**********************************************/

package application.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.Main;

public class UserDatabase {
	private static final String location = Main.class.getResource("/database/users.db").toExternalForm();
	
	private static final String requiredTable = "Users";
	
	protected static Connection connect() {
		String dbPrefix = "jdbc:sqlite:";
		Connection connection;
		
		try {
			connection = DriverManager.getConnection(dbPrefix+location);
			
		}catch(SQLException ex) {
			Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + "Could not connect to db at " + location);
			return null;
			
		}
		
		return connection;
		
	}
	
	private static boolean checkDrivers() {
		try {
			Class.forName("org.sqlite.JDBC");
			DriverManager.registerDriver(new org.sqlite.JDBC());
			return true;
			
		}catch(ClassNotFoundException | SQLException ex) {
			Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + "Could not start the SQLite drivers");
			return false;
		}
	}
	
	private static boolean checkConnection() {
		try (Connection connection = connect()) {
			return connection != null;
			
		}catch(SQLException ex) {
			Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + "Could not connect to the database");
			return false;
		}
	}
	
	private static boolean checkTables() {
		String query = "select Distinct tbl_name from sqlite_master where tbl_name = '" + requiredTable + "'";
		
		try(Connection connection = UserDatabase.connect()){
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				if(rs.getString("tbl_name").equals(requiredTable)) {
					return true;
					
				}
				
			}
		}catch(SQLException ex) {
			Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + "Could not find the tables in database");
			return false;
		}
		
		return false;
	}
	
	public static boolean isOk() {
		if(!checkDrivers()) return false;
		if(!checkConnection()) return false;
		return checkTables();
		
	}
}
