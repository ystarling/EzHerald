package com.herald.ezherald.curriculum;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.BaseFrameActivity;
import com.herald.ezherald.R;
import com.herald.ezherald.academic.AcademicFragment;

public class CurriculumActivity extends BaseFrameActivity {

	Fragment mContentFrag;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		mContentFrag = new CurriculumFragment();
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
