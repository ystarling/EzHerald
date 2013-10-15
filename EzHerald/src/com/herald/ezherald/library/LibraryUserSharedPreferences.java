package com.herald.ezherald.library;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.UserAccount;

public class LibraryUserSharedPreferences {
	private String Username;
	private String Userpassword;
	private String Userdate;
	private Context context;
	
	
	public LibraryUserSharedPreferences() {
//		SharedPreferences shared=getContext().getSharedPreferences("Libr_Content_Gradder", 0);
//		UserAccount account=Authenticate.getLibUser(getContext());
//		setUsername(account.getUsername());
//		setUserpassword(account.getPassword());
		
	}
	public void Saved(){
//		Editor editor=shared.edit();
//		editor.clear();
//		editor.putString("libr_name", getUsername());
//		editor.putString("libr_password", getUserpassword());
//		if(!shared.getString("libr_remand_date","").isEmpty())
//		{
//			editor.putString("libr_remand_date", getUserdate());
//		}
//		editor.commit();
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public String getUserpassword() {
		return Userpassword;
	}
	public void setUserpassword(String userpassword) {
		Userpassword = userpassword;
	}
	public String getUserdate() {
		return Userdate;
	}
	public void setUserdate(String userdate) {
		Userdate = userdate;
	}
	
}
