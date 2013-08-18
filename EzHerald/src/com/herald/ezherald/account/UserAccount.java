package com.herald.ezherald.account;

public class UserAccount {
	private String username = null;
	private String password = null;
	public  UserAccount(String username,String password) {
		this.username = username;
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
}
