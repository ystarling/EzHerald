package com.herald.ezherald.library;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class LibraryFragmentA extends SherlockFragment{
	
	View view;
	 public void onCreate(Bundle saveInstanceState){
		onCreate(saveInstanceState);
	}
	 public View OncreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstaceState){
		 super.onCreate(saveInstaceState);
		 view=inflater.inflate(R.layout.library_fragment_a,null);
		return view;
	 }
	 
}
