package com.herald.ezherald.gpa;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author xie
 * GPA的数据库模型类
 * 在掉用后需要close()
 * http://www.vogella.com/articles/AndroidSQLite/article.html
 */
public class GpaDbModel {
	private SQLiteDatabase db;
	private GpaDbHelper dbHelper;
	public GpaDbModel(Context context) { 
		dbHelper = new GpaDbHelper(context);
	}
	public void open(){
		db = dbHelper.getWritableDatabase();
	}
	public void close(){
		db.close();
	}
	public List<Record> all(){
		List<Record> records = new ArrayList<Record>();
		String sql = "SELECT * FROM "+GpaDbHelper.DATABASE_NAME;
		Cursor cursor = db.rawQuery(sql, null);
		if(cursor.moveToFirst()){// not empty
			do{
				Record r = new Record(cursor.getString(1),cursor.getString(2),cursor.getFloat(3),cursor.getString(4),cursor.getString(5),cursor.getString(6), cursor.getInt(7)==1);//0 for id //TODO  need to be tested
				records.add(r);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return records;
		//TODO
	}
	public void update(List<Record> records){
		String sql = "INSERT OR IGNORE INTO %s (name, score, credit, semester, scoreType, extra, isSelected ) VALUES" +
				             "(\"%s\", \"%s\", %f, \"%s\", \"%s\", \"%s\", %d)  ";
		String sql2 = "SELECT 1 FROM %s WHERE name = \"%s\" LIMIT 1;";
		for(Record r:records) { 
			String sSql2= String.format(sql2, GpaDbHelper.DATABASE_NAME,r.getName());
			Cursor cursor = db.rawQuery(sSql2, null);
			if(cursor.getCount() == 0){//不存在数据
				String sSql = String.format(sql, GpaDbHelper.DATABASE_NAME,r.getName(),r.getScore(),r.getCredit(),r.getSemester(),r.getScoreType(),r.getExtra(),r.isSelected()?1:0);
				db.execSQL(sSql);
			}
			cursor.close();
			
		}
	}
	public void changeSelection(String name, boolean newState) {
		// TODO Auto-generated method stub
		String sql = "UPDATE %s SET isSelected = %d WHERE name = \"%s\";";
		sql = String.format(sql, GpaDbHelper.DATABASE_NAME,newState?1:0,name);
		db.execSQL(sql);
	}
	/**
	 * 删除所有记录
	 */
	public void clear(){
		db.delete(GpaDbHelper.DATABASE_NAME, null, null);
	}
}
