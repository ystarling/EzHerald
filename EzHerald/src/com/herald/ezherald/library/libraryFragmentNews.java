/**
 * 
 */
package com.herald.ezherald.library;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;

/**
 * @author BIG_SEA
 *	The fragment of news
 */
public class libraryFragmentNews extends SherlockFragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group , Bundle save){
		View view=inflater.inflate(R.layout.library_fragment_news, null);
		Activity ac=getActivity();
		ac.setTitle("Í¼Êé¹Ý¹«¸æ");
		return view;
		
	}
}
