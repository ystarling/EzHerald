package com.herald.ezherald.bookingOffice;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;


public class bookingFragment extends SherlockFragment {
	   
	
	    
        @Override
        public void onCreate(Bundle savedInstanceState){
        	super.onCreate(savedInstanceState);
  	      	
        }
        
        @Override 
        public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
          	
        	
        	return inflater.inflate(R.layout.booking_fragment,container,false);
        }
}
