package com.herald.ezherald.account;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final int VERSION =1;
	
	public DatabaseHelper(Context context, String name) {
		super(context, name, null, VERSION);
		// TODO Auto-generated constructor stub
	}
	
	public DatabaseHelper(Context context, String name,int ver) {
		super(context, name, null, ver);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE account (id int ,username varchar(20),password varchar(30),type varchar(10))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	   

}
