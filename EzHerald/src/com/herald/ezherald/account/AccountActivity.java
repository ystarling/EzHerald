package com.herald.ezherald.account;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.R;

public class AccountActivity extends SherlockFragmentActivity 
{
	
	
	
	Fragment mContentFrag;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		mContentFrag = new AccountFragment();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_activity_main_pre_frag);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

}
