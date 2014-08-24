package com.herald.ezherald.bookingOffice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookingDBAdapter {
	static final String DATABASE_NAME = "bookingDB.db";
	static final int DATABASE_VERSION = 1;
	static final String TABLE_BOOKING_LIST = "tb_booking_list";
	
	static final String CREAT_TB_BOOKING_LIST = 
			"create table " +TABLE_BOOKING_LIST +
			"(_id integer primary key autoincrement," +
			"id text not null,"+
			"caption text,"+
            "posterUrl text," +
            "number int not null," +
            "activity_time text," +
            "deadline text, " +
            "poster blob" +
			")";

    final Context mContext;
    DatabaseHelper mDBHelper;
    SQLiteDatabase mDB;
    public BookingDBAdapter(Context context){
        mContext = context;
        mDBHelper = new DatabaseHelper(mContext);
    }
    public Long insert(String id,String caption,String posterUrl,int number,String activity_time,String deadline){

        ContentValues value = new ContentValues();
        value.put("id",id);
        value.put("caption",caption);
        value.put("posterUrl",posterUrl);
        value.put("number",number);
        value.put("activity_time",activity_time);
        value.put("deadline",deadline);
        Long LongRet = mDB.insert(TABLE_BOOKING_LIST,null,value);

        return LongRet;
    }
    public void close(){
        mDB.close();
    }

    public boolean isExists(String id){
        boolean boolRet = false;
        Cursor cursor = mDB.query(false,TABLE_BOOKING_LIST,new String[]{"id","caption","number"},"id="+id,null,null,null,null,null);

        int count = cursor.getCount();
        if(count > 0){
            boolRet = true;
        }
        return boolRet;
    }


    public BookingDBAdapter open(){
        mDB = mDBHelper.getWritableDatabase();
        return this;

    }



    public class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context){
                     super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        public DatabaseHelper(Context context,String name,SQLiteDatabase.CursorFactory factor,int version){
            super(context,name,factor,version);
        }
        @Override
        public void onCreate(SQLiteDatabase db){

                db.execSQL(CREAT_TB_BOOKING_LIST);
        }
        public void onUpgrade(SQLiteDatabase db,int olderVersion,int newVersion){
                db.execSQL("DROP TABLE IF EXISTS "+TABLE_BOOKING_LIST);
                onCreate(db);
        }

    }
			

}
