package com.herald.ezherald.bookingOffice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.herald.ezherald.BaseFrameActivity;
import com.herald.ezherald.R;

public class bookingActivity extends BaseFrameActivity implements activityToBooking.OnItemSelectedListener{
    
	public void  OnItemSelected(int position){
		 Intent intent = new Intent(this,bookingDetailActivity.class);
		 intent.putExtra("position", position);
		 startActivity(intent);
		
    	 
    }
	@Override 
    public void onCreate(Bundle savedInstanceState){
		  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    	  SetBaseFrameActivity(new bookingFragment());
    	  super.onCreate(savedInstanceState);
    	  activityToBooking firstFragment = new activityToBooking();
    	  FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    	  transaction.add(R.id.booking_fragment_container,firstFragment);
    	  transaction.addToBackStack(null);
    	  transaction.commit();
    	    	  
    }
	
}
	