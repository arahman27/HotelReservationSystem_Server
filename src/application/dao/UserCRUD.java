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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserCRUD {	
	public static String checkUser(String name, String password) {
		try(Connection connection = UserDatabase.connect()){
			PreparedStatement ps = null;
			String query = "Select * from Users where username = ? and password = ?";
			
			ps = connection.prepareStatement(query);
			ps.setString(1, name);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				if(rs.getString(4).equals("out")) {
					String updateQuery = "Update Users Set status='in' where username='" + rs.getString(2) + "'";
					ps = connection.prepareStatement(updateQuery);
					ps.executeUpdate();
					return "Found";
					
				}
				else {
					return "User already logged in";
					
				}	
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "Not found";
		
	}
	
	public static void changeStatus(String username, String status) {
		try(Connection connection = UserDatabase.connect()){
			PreparedStatement ps = null;
			String query = "Update Users Set status='" + status + "' where username = '" + username + "'";
			
			ps = connection.prepareStatement(query);
			ps.executeUpdate();
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
