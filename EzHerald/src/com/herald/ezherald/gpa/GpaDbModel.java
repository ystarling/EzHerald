package com.herald.ezherald.gpa;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author xie
 * GPA�����ݿ�ģ����
 * �����ú���Ҫclose()
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
				Record r = new Record(cursor.getString(0),cursor.getString(1),cursor.getFloat(2),cursor.getString(3),cursor.getString(4),cursor.getString(5), cursor.getInt(6)==1);//TODO need to be tested
				records.add(r);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return records;
		//TODO
	}
	public void update(List<Record> records){
		String sql = "DELETE * FROM "+ GpaDbHelper.DATABASE_NAME;
		db.execSQL(sql);
		sql = "INSERT INTO %s (name, score, credit, semester, scoreType, extra, isSelected ) VALUES" +
				             "(%s, %s, %f, %s, %s, %s, %d);";
		for(Record r:records) { 
			String sSql = String.format(sql, GpaDbHelper.DATABASE_NAME,r.getName(),r.getScore(),r.getCredit(),r.getSemester(),r.getScoreType(),r.getExtra(),r.isSelected()?1:0);
			db.execSQL(sSql);
		}
	}
}