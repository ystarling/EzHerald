package com.herald.ezherald.account;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;




import cn.edu.seu.herald.auth.*;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.BaseFrameActivity;
import com.herald.ezherald.R;




import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;


public class IDCardAccountActivity extends SherlockFragmentActivity {
	public boolean isRemoteModuleCall;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.account_idcardaccount_activity_prefrag);		
		Intent i = getIntent();
		isRemoteModuleCall = i.getBooleanExtra("remoteCall", false);
		//DEBUG//
		//Toast.makeText(this, "isRemote?" + isRemoteModuleCall, Toast.LENGTH_LONG).show();
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
