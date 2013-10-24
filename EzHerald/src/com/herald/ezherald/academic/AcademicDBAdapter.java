package com.herald.ezherald.academic;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

@SuppressLint("NewApi")
public class AcademicDBAdapter {
	
	private String dbName = "academicDB";
	private int dbVersion = 1;
	
	private String tbnJwcInfoList = "jwc_info_list";
	
	private String cln_id = "id";
	private String clnTitle = "title";
	private String clnId = "info_id";
	private String clnDate = "pub_date";
	private String clnType = "info_type";
	
	private String CREATE_TABLE = "create table "+ tbnJwcInfoList+ " ( " +
			cln_id + " integer primary key autoincrement ," +
			clnId + " integer unique not null , "+
			clnType+" varchar(30) not null , "+
			clnDate+" varchar(30) not null , "+
			clnTitle+" varchar(1000) not null );";
	
	
	private class DatabaseHelper extends SQLiteOpenHelper
	{
		
		public DatabaseHelper(Context c)
		{
			super(c, dbName, null, dbVersion);
		}

		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(CREATE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS "+ tbnJwcInfoList);
			
			onCreate(db);
		}
		
	}
	
	private Context context;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	
	public AcademicDBAdapter(Context c)
	{
		context = c;
		dbHelper = new DatabaseHelper(c);
	}
	
	public AcademicDBAdapter open()
	{
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		if(dbHelper != null)
		{
			dbHelper.close();
		}
	}
	
	public List<JwcInfo> getAllJwcInfo()
	{
		Cursor cursor = db.query(true, tbnJwcInfoList, new String [] {clnDate, clnId, clnTitle, clnType}, 
				null, null,	null, null, null, null);
		List<JwcInfo> jwcInfoList = new ArrayList<JwcInfo>();
		if(cursor.moveToFirst())
		{
			do{
				String type = cursor.getString(cursor.getColumnIndex(clnType));
				String title = cursor.getString(cursor.getColumnIndex(clnTitle));
				String date = cursor.getString(cursor.getColumnIndex(clnDate));
				int id = cursor.getInt(cursor.getColumnIndex(clnId));
				jwcInfoList.add(new JwcInfo(type, title, date, id));
			}while(cursor.moveToNext());
		}
		
		return jwcInfoList;
	}
	
	public long insertJwcInfo(JwcInfo info)
	{
		ContentValues values = new ContentValues();
		values.put(clnId, info.GetId());
		values.put(clnType, info.GetType());
		values.put(clnDate, info.GetDate());
		values.put(clnTitle, info.GetTitle());
		
		return db.insert(tbnJwcInfoList, null, values);
	}
	
	public void clear()
	{
		String delete_sql = "delete from "+ tbnJwcInfoList +";";
		String update_seq_sql = "update sqlite_sequence set seq=0 where name='"+tbnJwcInfoList+"';";
		db.execSQL(delete_sql);
		db.execSQL(update_seq_sql);
	}
	
	public boolean refreshJwcInfo(List<JwcInfo> infoList)
	{
		try
		{
			clear();
			for(JwcInfo info : infoList)
			{
				insertJwcInfo(info);
			}
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
		
	}
	
	public boolean addJwcInfo(List<JwcInfo> infoList)
	{
		try
		{
			for(JwcInfo info : infoList)
			{
				insertJwcInfo(info);
			}
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	
	

}
