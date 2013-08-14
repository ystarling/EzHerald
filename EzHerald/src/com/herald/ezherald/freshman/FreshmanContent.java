package com.herald.ezherald.freshman;

import android.os.Bundle;

import com.herald.ezherald.BaseFrameActivity;

public class FreshmanContent extends BaseFrameActivity {
	private FreshmanContentFragment mContentFrag;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle bundle = getIntent().getExtras();
		int type = bundle.getInt("type");
		mContentFrag = new FreshmanContentFragment();
		mContentFrag.setType(type);
		super.SetBaseFrameActivity(mContentFrag);
		super.onCreate(savedInstanceState);
	}
	
}
