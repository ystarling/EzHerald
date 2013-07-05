package com.herald.ezherald.activity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ActiDBAdapter {
	
	static final String DATABASE_NAME = "actiDB";
	static final int DATABASE_VERSION = 1;
	static final String TABLE_ACTI_LIST = "acti_tb_acti_list";
	
	
	static final String CREATE_TB_ACTI_LIST = 
			"create table "+TABLE_ACTI_LIST+
			" (_id integer primary key autoincrement," +
					"id int not null ,"+
					"is_vote integer not null," +
					"club_name text not null," +
					"club_id int not null," +
					"club_icon_name text not null,"+
					"club_icon blob ," +
					"acti_title text not null," +
					"acti_pub_time text not null," +
					"start_time text not null," +
					"end_time text not null," +
					"acti_intro text not null," +
					"hold_place text not null," +
					"acti_pic_name text ,"+
					"acti_pic blob )";
	
	
	
	final Context context;
	DatabaseHelper DBHelper;
	SQLiteDatabase db;
	
	public ActiDBAdapter(Context c)
	{
		context = c;
		DBHelper = new DatabaseHelper(context);
	}
	
	
	private class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context c)
		{
			super(c	, DATABASE_NAME, null, DATABASE_VERSION );
		}

		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(CREATE_TB_ACTI_LIST);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_ACTI_LIST );
			
			onCreate(db);
		}
		
	}
	
	
	public ActiDBAdapter open()
	{
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		DBHelper.close();
	}
	
	public long insertActiListItem(int id,int isVote,String clubName,int clubId,String iconName,
			String actiTitle,String intro,
			String actiPubTime, String startTime,String endTime, String place,String picName,
			Bitmap icon,Bitmap actiPic )
	{
		ContentValues value = new ContentValues();
		value.put("id", id);
		value.put("is_vote", isVote);
		value.put("club_name", clubName);
		value.put("club_id", clubId);
		if (icon!=null)
		{
			ByteArrayOutputStream os_icon = new ByteArrayOutputStream();
			icon.compress(Bitmap.CompressFormat.PNG, 100, os_icon);
			value.put("club_icon", os_icon.toByteArray());
		}		
		value.put("acti_intro",intro);
		value.put("acti_title", actiTitle);
		value.put("acti_pub_time", actiPubTime);
		value.put("start_time", startTime);
		value.put("end_time", endTime);
		value.put("hold_place", place);	
		value.put("club_icon_name", iconName);
		value.put("acti_pic_name", picName);
		return db.insert(TABLE_ACTI_LIST, null, value);
	}
	
	public boolean checkHaveIcon(int acti_id)
	{
		try
		{
			Cursor mCursor = db.query(true, TABLE_ACTI_LIST, 
					new String[]{"club_icon"},
					"id="+acti_id, null, null, null, null, null);
			
			if (mCursor.moveToFirst())
			{
				Log.v("db row count",""+mCursor.getCount());
			
				//mCursor.moveToFirst();
				byte[] in = mCursor.getBlob(mCursor.getColumnIndex("club_icon"));
				if(in!=null)
				{
					Log.v("QUERY success", "have icon");
					return true;
				}
				else
				{
					Log.v("QUERY success", "no icon");
					return false;
				}	
			}
			else
			{
				Log.v("QUERY success", "no acti corresponed to the acti_id");
				return false;
			}
		}
		catch( SQLException e)
		{
			Log.v("QUERY ERROR", "cursor is null");
		}
		
		return false;	
	}
	
	public boolean checkShouldHavePic(int acti_id)
	{
		Cursor mCursor = db.query(true, TABLE_ACTI_LIST,
				new String[]{"acti_pic_name"}, "id="+acti_id, 
				null, null, null, null, null);
		if(mCursor.moveToFirst())
		{
			String pic_name = mCursor.getString(mCursor.getColumnIndex("acti_pic_name"));
			if(pic_name=="")
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		
		return false;
		
		
	}
	
	
	public boolean checkHaveActiPic(int acti_id)
	{
		Cursor mCursor = db.query(true, TABLE_ACTI_LIST, 
				new String[]{"acti_pic"}, "id="+acti_id,
				null, null, null, null, null);
		if(mCursor != null)
		{
			if(mCursor.moveToFirst())
			{
				byte [] in = mCursor.getBlob(mCursor.getColumnIndex("acti_pic"));
				if(in!=null)
				{
					return true;
				}
			}

		}
		return false;
	}
	
	
	public Bitmap getClubIconByActi(int acti_id)
	{
		Cursor mCursor = db.query(true, TABLE_ACTI_LIST, 
				new String[]{"club_icon"},
				"id="+acti_id, null, null, null, null, null);
		
		try{
			if (mCursor!=null)
			{
				mCursor.moveToFirst();
				
				byte[] in = mCursor.getBlob(mCursor.getColumnIndex("club_icon"));
				Bitmap bitout = BitmapFactory.decodeByteArray(in, 0, in.length);
				return bitout;
			}
			else
			{
				return null;
			}
		}
		catch(SQLException e)
		{
			return null;
		}
		
		
	}
	
	public Bitmap getActiPicByActi(int acti_id)
	{
		Cursor mCursor = db.query(true, TABLE_ACTI_LIST, 
				new String[]{"acti_pic"},
				"id="+acti_id, null, null, null, null, null);
		
		if (mCursor!=null)
		{
			mCursor.moveToFirst();
			
			byte[] in = mCursor.getBlob(mCursor.getColumnIndex("acti_pic"));
			Bitmap bitout = BitmapFactory.decodeByteArray(in, 0, in.length);
			return bitout;
		}
		else
		{
			return null;
		}
	}
	
	public boolean updateClubIconByActi(int acti_id, Bitmap icon)
	{
		ContentValues values = new ContentValues();
		
		if (icon!=null)
		{
			ByteArrayOutputStream os_icon = new ByteArrayOutputStream();
			icon.compress(Bitmap.CompressFormat.PNG, 100, os_icon);
			values.put("club_icon", os_icon.toByteArray());
			int num  =db.update(TABLE_ACTI_LIST, values, "id="+acti_id, null);
			if (num>0)
			{
				Log.v("Save Icon", "save icon success; acti_id="+acti_id+";num="+num);
				return true;
			}
			else
			{	
				Log.v("Save Icon", "save icon failed;acti_id="+acti_id+";num="+num);
				return false;
			}
			
			
		}
		Log.v("Save Icon", "save icon failed,icon is null");
		return false;
		
	}
	
	public boolean updateActiPicByActi(int acti_id,Bitmap pic)
	{
		ContentValues values = new ContentValues();
		
		if (pic!=null)
		{
			ByteArrayOutputStream os_pic = new ByteArrayOutputStream();
			pic.compress(Bitmap.CompressFormat.PNG, 100, os_pic);
			values.put("acti_pic", os_pic.toByteArray());
			return db.update(TABLE_ACTI_LIST, values, "id="+acti_id, null)>0;
		}
		return false;
	}
	
	
	public boolean clearActiListTb()
	{
		try{
			db.execSQL("delete from "+TABLE_ACTI_LIST);
			return true;
		}
		catch(SQLException e)
		{
			Log.v("SQL ERROR","clear table_acti_list failed!");
			return false;
		}
		
	}
	
	public boolean checkIfDBEmpty()
	{
		Cursor mCursor = db.query(false, TABLE_ACTI_LIST, new String [] {"id"}, 
				null, null, null, null, null, null);
		if(mCursor.getCount()>0)
		{
			return false;
		}
		return true;
	}
	
	public List<ActiInfo> getAllFromTBActiList()
	{
		List<ActiInfo> actiList = new ArrayList<ActiInfo>();
		Cursor mCursor = db.query(false, TABLE_ACTI_LIST, new String [] {"id","is_vote","club_name","club_id",
				"club_icon_name","acti_title","start_time","end_time","hold_place","acti_pub_time",
				"acti_intro","acti_pic_name"}, 
				null, null, null, null, null, null);
		if(mCursor != null)
		{
			if(mCursor.moveToFirst())
			{
				//mCursor.getInt(mCursor.getColumnIndex("is_vote"));
				ActiInfo item;
				do{
					item = new ActiInfo(mCursor.getInt(mCursor.getColumnIndex("id")),
							mCursor.getInt(mCursor.getColumnIndex("is_vote"))==1, 
							mCursor.getString(mCursor.getColumnIndex("club_name")),
							mCursor.getInt(mCursor.getColumnIndex("club_id")),
							mCursor.getString(mCursor.getColumnIndex("club_icon_name")),
							mCursor.getString(mCursor.getColumnIndex("acti_title")),
							mCursor.getString(mCursor.getColumnIndex("start_time")),
							mCursor.getString(mCursor.getColumnIndex("end_time")),
							mCursor.getString(mCursor.getColumnIndex("hold_place")),
							mCursor.getString(mCursor.getColumnIndex("acti_pub_time")),
							mCursor.getString(mCursor.getColumnIndex("acti_intro")),
							mCursor.getString(mCursor.getColumnIndex("acti_pic_name"))
							);
					actiList.add(item);
					
				}while(mCursor.moveToNext());
				
			}
		}
		
		return actiList;
	}
	

	
	
		

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
