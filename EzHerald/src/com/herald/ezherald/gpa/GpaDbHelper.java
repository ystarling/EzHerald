package com.herald.ezherald.gpa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author xie
 *	GPA的数据库操作的类
 */
public class GpaDbHelper extends SQLiteOpenHelper {
	public final static String DATABASE_NAME="GPA";
	public final static int VERSION = 1;
	public GpaDbHelper(Context context){
		super(context , DATABASE_NAME ,null,VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String format = "CREATE TABLE %s ();";//TODO not completed
		String sql = String.format(format, DATABASE_NAME);//TODO not completed

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion , int newVersion) {
		// TODO Auto-generated method stub
		String format = "DROP TABLE IF EXISTS %s ;";//TODO not completed
		String sql = String.format(format, DATABASE_NAME);//TODO not completed
		db.execSQL(sql);
	}

}
