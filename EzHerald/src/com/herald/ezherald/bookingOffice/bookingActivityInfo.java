package com.herald.ezherald.bookingOffice;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.herald.ezherald.R;


public class bookingActivityInfo extends Fragment{

	  private String[] test ={"first","second","third"};
	  public static String ARG_POSITION = "position";
	  private int mCurrentPosition = -1;
	  @Override 
      public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    	  
		  if(savedInstanceState!=null){
			  mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
		  }
    	  return inflater.inflate(R.layout.booking_activity_info, container, false);
      }
      
      
      @Override 
      public void onStart(){
    	  super.onStart();
    	  Bundle args = getArguments();
    	  
    	  if(args==null){
    		  updateInfoView(args.getInt(ARG_POSITION));
    	  }
    	  else if(mCurrentPosition!=-1){
    		  updateInfoView(mCurrentPosition);
    	  }
      }
      
      @Override
      public void onSaveInstanceState(Bundle outState){
    	  super.onSaveInstanceState(outState);
    	  outState.putInt(ARG_POSITION, mCurrentPosition);
      }
      
      public void updateInfoView(int position){
    	  TextView info = (TextView)getActivity().findViewById(R.id.activity_info);
    	  info.setText(test[position]);
    	 
    	  mCurrentPosition = position;
      }
}