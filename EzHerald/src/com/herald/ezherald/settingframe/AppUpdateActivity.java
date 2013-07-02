package com.herald.ezherald.settingframe;

import com.herald.ezherald.R;
import com.herald.ezherald.R.layout;
import com.herald.ezherald.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AppUpdateActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_app_update);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.app_update, menu);
		return true;
	}

}
