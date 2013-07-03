package com.herald.ezherald.settingframe;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.herald.ezherald.BaseFrameActivity;

public class AppUpdateActivity extends BaseFrameActivity {

	Fragment mContentFrag;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		mContentFrag = new AppUpdateFragment();
		super.SetBaseFrameActivity(mContentFrag);
		super.onCreate(savedInstanceState);
	}
}
