package com.prometheus.user;

public final class User extends Thread {
	
	//from database
	private String name;
	private String username;
	private int ID;
	private char[] password;//uses char array because String type
	//storages data in more than one only place, making it unsafe
	
	Thread currentUser = new Thread();
	
	protected User(){
		//read from database according to ID, calling readFromDB();
		//readFromDB();
		
		currentUser.start();
		currentUser.run();
		
	}
	
	
	protected User(String name, String username, int id, char[] password ) {
		
		
		this.name = name;
		
		this.password = password;
		writeToDB();
		
		this.username = username;
		
	}
	
	//name - getter/setter
	public String gettName() {
		return name;
	}
	
	public void settName(String name) {
		this.name = name;
	}

	//password - getter/setter
	public char[] getPassword() {
		return password;
	}
	
	public void setPassword(char[] password) {
		this.password = password;
		writeToDB();
	}
	
	//ID - getter
	public int getID() {
		return ID;
	}
	
	//username - getter/setter
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	//class constructor

	
	private void writeToDB(){
		//write data to Database 
		/*
		 * this.name;
		 * this.password;
		 * this.username;
		 */
	}
	
	private void readFromDB(){
		//read data from Database 
		/*
		 * this.name = name;
		 * this.password = password;
		 * this.username = username;
		 */
	}
	
}
