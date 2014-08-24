package com.herald.ezherald.bookingOffice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.R;

public class BookingDetailActivity extends SherlockActivity{
	private String[] test = {"first","second","third"};
	public static String ARG_POSITION = "position";
	private int mCurrentPosition = -1;
	
	@SuppressLint("NewApi")
	@Override 
	public void onCreate(Bundle savedInstanceState){
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.booking_activity_info);
		TextView text = (TextView)findViewById(R.id.booking_activity_info);
		Intent intent = getIntent();
		
		
		text.setText(test[intent.getIntExtra(ARG_POSITION,-1)]);
		
		
		
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem muItem){
		switch(muItem.getItemId()){
		case android.R.id.home:
			finish();
		    return true;
		
		}
		return false;
	}

}
