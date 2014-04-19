package com.herald.ezherald.settingframe;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.herald.ezherald.R;

public class SyncFrequencySettingActivity extends SherlockActivity {
	
	private static final int DIALOG_ID = 1;
	private static final String PREF_NAME = "com.herald.ezherald_preferences";
	private static final String KEY_NAME_REFRESH_FREQ = "sync_frequency";
	private static final String DEFAULT_FREQ = "360";
	
	private String[] mTimeSpanTitles;
	private String[] mTimeSpanValues;
	private String mUpdateFreq;
	private int mCheckedItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getSavedValues();
		
		showDialog(DIALOG_ID, savedInstanceState);
	}

	private void getSavedValues() {
		// TODO Auto-generated method stub
		Resources resources = getResources();
		mTimeSpanTitles = resources.getStringArray(R.array.pref_sync_frequency_titles);
		mTimeSpanValues = resources.getStringArray(R.array.pref_sync_frequency_values);
		
		SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		mUpdateFreq = preferences.getString(KEY_NAME_REFRESH_FREQ, DEFAULT_FREQ);
		
		for(int i=0; i<mTimeSpanValues.length; i++){
			if(mTimeSpanValues[i].equals(mUpdateFreq)){
				mCheckedItem = i;
				break;
			}
		}
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id, Bundle args) {
		Dialog dialog = null;
		switch(id){
		case DIALOG_ID:
			Builder builder = new Builder(this);
			builder.setTitle("选择主界面信息更新频率");
			builder.setCancelable(false);
			builder.setSingleChoiceItems(mTimeSpanTitles, mCheckedItem, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					mUpdateFreq = mTimeSpanValues[which];
				}
			});
			builder.setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					saveCurrentPreference();
					finish();
				}
			});
			builder.setNegativeButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			
			dialog = builder.create();
			return dialog;
		}
		
		return super.onCreateDialog(id, args);
	}

	protected void saveCurrentPreference() {
		SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(KEY_NAME_REFRESH_FREQ, mUpdateFreq);
		editor.commit();
	}
	
	
	
}
