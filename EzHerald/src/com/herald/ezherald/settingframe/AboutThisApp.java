package com.herald.ezherald.settingframe;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.R;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

public class AboutThisApp extends SherlockActivity {
	private TextView mAppVersionText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_activity_about);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mAppVersionText = (TextView)findViewById(R.id.setting_about_version_textView);
		
		PackageInfo pkgInfo;
		try {
			pkgInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			mAppVersionText.setText(pkgInfo.versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
