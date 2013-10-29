package com.herald.ezherald.freshman;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.herald.ezherald.BaseFrameActivity;

public class FreshmanActivity extends BaseFrameActivity {
	Fragment mContentFrag;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		mContentFrag = new FreshmanFragment();
		super.SetBaseFrameActivity(mContentFrag);
		super.onCreate(savedInstanceState);
	}

}
