package com.herald.ezherald.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.BaseFrameActivity;
import com.herald.ezherald.R;
import com.herald.ezherald.academic.AcademicFragment;
import com.herald.ezherald.academic.CustomSlidingFragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class ActiActivity extends BaseFrameActivity {

	Fragment mContentFrag;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

//		mContentFrag = new ActiListFragment();
		mContentFrag = new TmpActivity();
		super.SetBaseFrameActivity(mContentFrag);
		super.onCreate(savedInstanceState);
		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.actionbarsherlock.app.SherlockActivity#onCreateOptionsMenu(com.
	 * actionbarsherlock.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {

		return super.onCreateOptionsMenu(menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockActivity#onOptionsItemSelected(com.
	 * actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * �ϲ�Titleλ�õİ�ť�����Ӧ
		 */
		return super.onOptionsItemSelected(item);
	}

}
