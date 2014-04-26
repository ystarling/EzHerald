package com.herald.ezherald.bookingOffice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.herald.ezherald.BaseFrameActivity;
import com.herald.ezherald.R;

public class BookingActivity extends BaseFrameActivity implements BookingList.OnItemSelectedListener{
    
	public void  OnItemSelected(int position){
		 Intent intent = new Intent(this,BookingDetailActivity.class);
		 intent.putExtra("position", position);
		 startActivity(intent);
		
    	 
    }
	@Override 
    public void onCreate(Bundle savedInstanceState){
		  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    	  SetBaseFrameActivity(new BookingFragment());
    	  super.onCreate(savedInstanceState);
    	  BookingList firstFragment = new BookingList();
    	  FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    	  transaction.add(R.id.booking_fragment_container,firstFragment);
    	  transaction.addToBackStack(null);
    	  transaction.commit();
    	    	  
    }
	
}
	
