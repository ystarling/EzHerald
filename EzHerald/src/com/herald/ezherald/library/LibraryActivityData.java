package com.herald.ezherald.library;

import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.herald.ezherald.R;

public class LibraryActivityData extends SherlockActivity{
	
	public void onCreate(Bundle saveInstanceSate){
		super.onCreate(saveInstanceSate);
		setContentView(R.layout.library_activity_book_main);
		setTitle("ËÑË÷½á¹ûÏÔÊ¾");
		TextView libr_book_name=(TextView) findViewById(R.id.libr_book_name);
		TextView libr_book_author=(TextView) findViewById(R.id.libr_book_author);
		TextView libr_book_press=(TextView) findViewById(R.id.libr_book_press);
		TextView libr_book_date=(TextView) findViewById(R.id.libr_book_date);
	}
}
