package com.herald.ezherald.bookingOffice;

public class bookingDBAdapter {
	static final String DATABASE = "bookingDB";
	static final int VERSION = 1;
	static final String TABLE_BOOKING_LIST = "tb_booking_list";
	
	static final String CREAT_TB_BOOKING_LIST = 
			"create table " +TABLE_BOOKING_LIST +
			"(_id interger pramary key autoincreament," +
			"id int not null,"+
			"content text not null"+
			")";
	/*
	final Context context;
	DatabaseHelper DBHelper;
	SQLLiteDatebase db;
		*/	
			

}
