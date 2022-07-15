package com.tuck.matches.beans;

public class LoginForm {
	
	public LoginForm(){
		
	}
	
	public LoginForm(String username, String password){
		this.userName=username;
		this.password=password;
	}

	private String userName;
	private String password;
	
	public String getUserName() {
		return userName;
	}
	@Override
	public String toString() {
		return "LoginForm [userName=" + userName + ", password=" + password + "]";
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
