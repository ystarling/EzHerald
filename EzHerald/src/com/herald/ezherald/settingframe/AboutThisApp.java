package com.herald.ezherald.settingframe;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.R;

import android.os.Bundle;

public class AboutThisApp extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_activity_about);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	
}
