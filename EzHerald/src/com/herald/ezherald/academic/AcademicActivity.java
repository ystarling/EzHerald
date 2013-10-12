package com.herald.ezherald.academic;

import android.os.Bundle;
import android.support.v4.app.Fragment;


import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.BaseFrameActivity;

import com.herald.ezherald.academic.AcademicFragment;

public class AcademicActivity extends BaseFrameActivity {

	Fragment mContentFrag;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		mContentFrag = new AcademicFragment();
		super.SetBaseFrameActivity(mContentFrag);
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		return true;//super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return true;//super.onOptionsItemSelected(item);
	}

	
	
}
