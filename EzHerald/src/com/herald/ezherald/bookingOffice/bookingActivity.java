package com.herald.ezherald.bookingOffice;

import android.os.Bundle;

import com.herald.ezherald.BaseFrameActivity;
import com.herald.ezherald.R;
import android.support.v4.app.*;

public class bookingActivity extends BaseFrameActivity implements activityToBooking.OnItemSelectedListener{
    
	public void  OnItemSelected(int position){
		bookingActivityInfo infoFrag = new bookingActivityInfo();
    	 Bundle args = new Bundle();
    	 args.putInt(bookingActivityInfo.ARG_POSITION,position);
    	 infoFrag.setArguments(args);
    	 FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    	 transaction.replace(R.id.booking_fragment_container,infoFrag);
    	 transaction.addToBackStack(null);
    	 transaction.commit();
    	 
    }
	@Override 
    public void onCreate(Bundle savedInstanceState){
    	  SetBaseFrameActivity(new bookingFragment());
    	  super.onCreate(savedInstanceState);
    	  activityToBooking firstFragment = new activityToBooking();
    	  getSupportFragmentManager().beginTransaction().add(R.id.booking_fragment_container,firstFragment).commit();
    	    	  
    }
	
}
	