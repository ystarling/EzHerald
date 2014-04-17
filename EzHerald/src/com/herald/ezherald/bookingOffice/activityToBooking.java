package com.herald.ezherald.bookingOffice;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.herald.ezherald.R;

public class activityToBooking extends ListFragment{
	    
	     OnItemSelectedListener mCallback;
	     
	     public interface OnItemSelectedListener{
	    	 public void OnItemSelected(int position);
	     }
	     
	     
	     @Override
	     public void onCreate(Bundle savedInstanceState){
	    	 super.onCreate(savedInstanceState);
	    	// int layout = android.R.layout.simple_list_item_1;
	    	 activityToBookingAdapter adapter= new activityToBookingAdapter(getActivity());
	    	 setListAdapter(adapter);
	    	 //setListAdapter(new ArrayAdapter<String>(getActivity(),layout,activity_list));
	     }
	     
	     @Override
	     public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
	    	 
	    	 
	    	 return inflater.inflate(R.layout.booking_listview,container, false);
	     }
	     @Override
	     public void onStart() {
	         super.onStart();

	         // When in two-pane layout, set the listview to highlight the selected list item
	         // (We do this during onStart because at the point the listview is available.)
	         if (getFragmentManager().findFragmentById(R.id.booking_fragment_container) != null) {
	             getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	         }
	     }
	     @Override 
	     public void onAttach(Activity activity){
	    	 super.onAttach(activity);
	    	// This makes sure that the container activity has implemented
	         // the callback interface. If not, it throws an exception.
	    	 try{
	    		 mCallback = (OnItemSelectedListener) activity;
	    	 }
	    	 catch(ClassCastException e){
	    		 throw new ClassCastException(activity.toString() +
	    				 " must implement OnItemSelectedListener!");
	    	 }
	     }
	     
	     @Override
	     public void onListItemClick(ListView l,View v,int position,long id){
	    	 
	    	 mCallback.OnItemSelected(position);
	    	
	    	 // Set the item as checked to be highlighted when in two-pane layout
	         //getListView().setItemChecked(position, true);
	    	 
	     }
  
}
