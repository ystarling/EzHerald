package com.herald.ezherald.gpa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author xie
 * GPA的数据库模型类
 * http://www.vogella.com/articles/AndroidSQLite/article.html
 */
public class GpaDbModel {
	private SQLiteDatabase db;
	private GpaDbHelper dbHelper;
	public class Record{
		//TODO detail
	}
	public GpaDbModel(Context context) { 
		dbHelper = new GpaDbHelper(context);
	}
	public void add(Record record){
		//TODO
	}
	public void del(int id){
		//TODO
	}
	public Record[] all(){
		return null;
		//TODO
	}
	public Record find(int id){
		return null;
		//TODO
	}
	public void change(int id,Record newRecord){
		//TODO
	}
}
