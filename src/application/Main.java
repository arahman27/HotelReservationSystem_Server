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

package application;
	
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import application.dao.UserCRUD;
import application.dao.UserDatabase;

public class Main extends Application {
	private TextArea ta = new TextArea();
	private ServerSocket ss;
	
	@Override
	public void start(Stage ps) {
		try {
			ta.setWrapText(true);
			
			Scene sc = new Scene(new ScrollPane(ta), 483, 185);
			ps.setTitle("Hotel Reservation System Server");
			ps.setScene(sc);
			ps.show();
			
			new Thread(()->listen()).start();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void listen() {
		try {
			ss = new ServerSocket(8000);
			
			Platform.runLater(()->ta.appendText("Hotel Reservation System server started: " + new Date() + "\n"));
			
			while(true) {
				Socket s = ss.accept();
				Platform.runLater(()->{
					ta.appendText("\n**Client connected at " + new Date() + "**");
				
				});

				new ServerThread(this, s);
			
			}	
		}catch(IOException e) {
			e.printStackTrace();
			
		}
	}
		
	class ServerThread extends Thread{
		private Main server;
		private Socket socket;
		
		public ServerThread(Main server, Socket s) {
			this.server = server;
			this.socket = s;
			start();
			
		}
		
		public void run() {
			String username = "";
			
			try {
				DataInputStream din = new DataInputStream(socket.getInputStream());
				DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
				
				while(true) {
					String s = din.readUTF();
					String[] userInfo = s.split(",");
					
					if(userInfo[0].equals("Login")) {
						if(UserDatabase.isOk()) {
							String userCheck = UserCRUD.checkUser(userInfo[1], userInfo[2]);
							
							if(userCheck.equals("Found")) {
								dout.writeUTF("success");
								username = userInfo[1];
								ta.appendText("\n  *User: " + userInfo[1] + " successfuly logged in at " + new Date());
								
							}
							else if(userCheck.equals("User already logged in")) {
								dout.writeUTF("User already logged in");
								ta.appendText("\n  *User: " + userInfo[1] + " is already logged in");
								
							}
							else {
								dout.writeUTF("failed");
								ta.appendText("\n  *User: " + userInfo[1] + " login failed at " + new Date());
								
							}
							
						}
						else {
							ta.appendText("\nERROR: Could not load into database");
							
						}
					}
				}
			}catch(IOException e) {
				UserCRUD.changeStatus(username, "out");
				ta.appendText("\n**Client disconnected at " + new Date() + "**");

			}
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}