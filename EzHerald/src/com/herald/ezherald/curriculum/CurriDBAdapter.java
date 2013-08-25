package com.herald.ezherald.curriculum;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class CurriDBAdapter {

	private String DBName = "curriDB";
	private int DBVersion = 1;
	
	private String tbnCourses = "curri_courses";
	private String tbnAttendances = "curri_attendances";
	
	private String clCourseId = "course_id";
	private String clCourseName = "course_name";
	private String clCourseLecturer = "course_lecturer";
	private String clCourseBeginweek = "course_begin_week";
	private String clCourseEndweek = "course_end_week";
	private String clCourseCredit = "course_credit";
	
	private String clAttId = "_id";
	private String clAttCourseName = "course_name";
	private String clAttPeriodBegin = "period_begin";
	private String clAttPeriodEnd = "period_end";
	private String clAttWeekBegin = "week_begin";
	private String clAttWeekEnd = "week_end";
	private String clAttPlace = "place";
	private String clAttWeekday = "weekday";
	
	private String createTable_Courses = "create table " + tbnCourses +
			"("+clCourseId+" integer primary key autoincrement," +
				clCourseName+" text not null," +
				clCourseLecturer+" text not null,"+
				clCourseBeginweek+" integer ,"+
				clCourseEndweek+" integer ,"+
				clCourseCredit+" integer not null"
				+");";
	
	private String createTable_Attendances = "create table "+tbnAttendances+
			"("+clAttId+" integer primary key autoincrement,"+
			clAttCourseName+" text not null,"+
			clAttPeriodBegin+" integer not null,"+
			clAttPeriodEnd+" integer not null,"+
			clAttWeekBegin+" integer not null,"+
			clAttWeekEnd+" integer not null,"+
			clAttPlace+" text not null,"+
			clAttWeekday+" integer not null"+
			");";
	
	
	private class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context c)
		{
			super(c	, DBName, null, DBVersion );
		}

		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(createTable_Attendances);
			db.execSQL(createTable_Courses);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS "+ tbnAttendances);
			db.execSQL("DROP TABLE IF EXISTS "+ tbnCourses);
			
			onCreate(db);
		}
		
	}
	
	final Context context;
	DatabaseHelper DBHelper;
	SQLiteDatabase db;
	
	public CurriDBAdapter(Context c)
	{
		context = c;
		DBHelper = new DatabaseHelper(c);
	}
	
	public CurriDBAdapter open()
	{
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		if(DBHelper != null)
		{
			DBHelper.close();
		}
	}
	
	
	public long insertCourse(Course course)
	{
		ContentValues value = new ContentValues();
//		value.put(clCourseId, course.getCourseId());
		value.put(clCourseName, course.getCourseName());
		value.put(clCourseLecturer, course.getLecturer());
		value.put(clCourseBeginweek, course.getBeginWeek());
		value.put(clCourseEndweek, course.getEndWeek());
		value.put(clCourseCredit, course.getCredit());
		
		return db.insert(tbnCourses, null, value);
	}
	
	public long insertAttendance(Attendance att)
	{
		ContentValues values = new ContentValues();
		values.put(clAttCourseName, att.getAttCourseName());
		values.put(clAttPlace, att.getAttPlace());
		values.put(clAttPeriodBegin, att.getAttPeriodBegin());
		values.put(clAttPeriodEnd, att.getAttPeriodEnd());
		values.put(clAttWeekBegin, att.getAttWeekBegin());
		values.put(clAttWeekEnd, att.getAttWeekEnd());
		values.put(clAttWeekday, att.getAttWeekday());
		
		return db.insert(tbnAttendances, null, values);
	}
	
	public List<Course> getCourse(String courseName)
	{
		Cursor mCursor = db.query(true, tbnCourses, 
				new String[]{clCourseBeginweek,clCourseCredit,clCourseEndweek,clCourseLecturer,clCourseName},
				clCourseName+"='"+courseName+"'", null, null, null, null, null);
		List<Course> courseList = new ArrayList<Course>();
		if(mCursor.moveToFirst())
		{
			
			do
			{
				String name = mCursor.getString(mCursor.getColumnIndex(clCourseName));
				String teacher = mCursor.getString(mCursor.getColumnIndex(clCourseLecturer));
				int credit = mCursor.getInt(mCursor.getColumnIndex(clCourseCredit));
				int begin = mCursor.getInt(mCursor.getColumnIndex(clCourseBeginweek));
				int end = mCursor.getInt(mCursor.getColumnIndex(clCourseEndweek));
				courseList.add(new Course(name,teacher,begin,end,credit));
				
			}while(mCursor.moveToNext());
			
		}
		return courseList;
	}
	
	public List<Attendance> getAttByWeekday(int weekday)
	{
		Cursor mCursor = db.query(true, tbnAttendances, 
				new String[]{clAttCourseName,clAttPeriodBegin,clAttPeriodEnd,clAttPlace,clAttWeekBegin,
				clAttWeekday,clAttWeekEnd},
				clAttWeekday+"="+weekday, null, null, null, null, null);
		List<Attendance> attendances = new ArrayList<Attendance>();
		if(mCursor.moveToFirst())
		{
			
			do
			{
				String courseName = mCursor.getString(mCursor.getColumnIndex(clAttCourseName));
				String place = mCursor.getString(mCursor.getColumnIndex(clAttPlace));
				int beginWeek = mCursor.getInt(mCursor.getColumnIndex(clAttWeekBegin));
				int endWeek = mCursor.getInt(mCursor.getColumnIndex(clAttWeekEnd));
				int beginPeriod = mCursor.getInt(mCursor.getColumnIndex(clAttPeriodBegin));
				int endPeriod = mCursor.getInt(mCursor.getColumnIndex(clAttPeriodEnd));
				int day = mCursor.getInt(mCursor.getColumnIndex(clAttWeekday));
				attendances.add(new Attendance(courseName,place,beginPeriod,endPeriod,
						beginWeek,endWeek,day));
				
			}while(mCursor.moveToNext());
			
		}
		return attendances;
	}
	
	public boolean isEmpty()
	{
		Cursor mCursor = db.query(true, tbnAttendances, 
				new String[]{clAttCourseName,clAttPeriodBegin,clAttPeriodEnd,clAttPlace,clAttWeekBegin,
				clAttWeekday,clAttWeekEnd},
				null, null, null, null, null, null);
		if(mCursor.moveToFirst())
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public void clear()
	{
		String sql_1 = "delete from "+tbnAttendances+";";
		String sql_2 = "delete from "+tbnCourses+";";
		
		db.execSQL(sql_1);
		db.execSQL(sql_2);
		
		String sql_3 = "update sqlite_sequence set seq=0 where name='"+tbnAttendances+"';";
		String sql_4 = "update sqlite_sequence set seq=0 where name='"+tbnCourses+"';";
		
		db.execSQL(sql_3);
		db.execSQL(sql_4);
		
	}
	
	

}
