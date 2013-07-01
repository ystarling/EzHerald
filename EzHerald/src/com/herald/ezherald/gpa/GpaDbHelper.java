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
	public final static String DATABASE_NAME    = "GPA";
	private final static String COLUM_ID        = "id integer primary key autoincrement";
	private final static String COLUM_NAME      = "name text not null";
	private final static String COLUM_SCORE     = "score text not null";
	private final static String COLUM_CREDIT    = "credit real not null";
	private final static String COLUM_SEMESETER = "semeser txt not null";
	private final static String COLUM_SCORE_TYPE= "scoreType text not null";
	private final static String COLUM_EXTRA     = "extra text not null";
	private final static String COLUM_ISSELECTED= "isEelected  integer  not null";

	public final static int VERSION = 1;
	public GpaDbHelper(Context context){
		super(context , DATABASE_NAME ,null,VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String format = "CREATE TABLE %s ( %s , %s , %s , %s , %s , %s , %s);";
		String sql = String.format(format, DATABASE_NAME,COLUM_ID,DATABASE_NAME,COLUM_NAME,COLUM_SCORE,COLUM_CREDIT,COLUM_SEMESETER,COLUM_SCORE_TYPE,COLUM_EXTRA,COLUM_ISSELECTED);
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion , int newVersion) {
		// TODO Auto-generated method stub
		String format = "DROP TABLE IF EXISTS %s ;";
		String sql = String.format(format, DATABASE_NAME);
		db.execSQL(sql);
	}

}
