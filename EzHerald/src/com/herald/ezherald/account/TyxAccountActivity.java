package com.herald.ezherald.account;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


import cn.edu.seu.herald.auth.*;

import com.herald.ezherald.BaseFrameActivity;
import com.herald.ezherald.R;


import android.os.Bundle;

import android.support.v4.app.Fragment;


public class TyxAccountActivity extends BaseFrameActivity {
	
	Fragment mContentFrag;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		mContentFrag = new IDCardAccountFragment();
		super.SetBaseFrameActivity(mContentFrag);
		super.onCreate(savedInstanceState);

	}
	
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockActivity#onCreateOptionsMenu(com.actionbarsherlock.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		
		return super.onCreateOptionsMenu(menu);
	}
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockActivity#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		/*
		 * 上侧Title位置的按钮点击相应
		 */
		return super.onOptionsItemSelected(item);
	}	
}
