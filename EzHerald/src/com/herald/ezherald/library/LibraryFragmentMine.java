/**
 * 
 */
package com.herald.ezherald.library;

import com.actionbarsherlock.app.SherlockFragment;

import com.herald.ezherald.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author BIG_SEA
 *
 */
public class LibraryFragmentMine extends SherlockFragment{
	@Override
	/**
	 * 
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup group,Bundle save){
		View view=inflater.inflate(R.layout.library_fragment_mine, null);
		Activity ac=getActivity();
		ac.setTitle("Œ“µƒÕº Èπ›");
		return view;
		
	}

}
