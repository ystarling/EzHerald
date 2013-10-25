package com.herald.ezherald.settingframe;

import java.util.HashSet;
import java.util.Set;

import com.actionbarsherlock.app.SherlockActivity;
import com.herald.ezherald.R;
import com.herald.ezherald.mainframe.SharedPreferencesHandler;

import android.os.Bundle;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;

public class MainContentModulePrefActivity extends SherlockActivity {

	private String[] mModuleNames; // 模块名(英文)
	private String[] mModuleTitles; // 模块名（中文）
	private Set<String> mChoosenModules; // 已选择的模块
	private boolean[] mCheckedItems;
	private Dialog mDialog;

	private final String PREF_NAME = "com.herald.ezherald_preferences";
	private final String KEY_NAME = "module_choice";
	private final int DIALOG_ID = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.setting_main_content_module_pref);

		readXMLForChoiceArray();
		loadSharedPreferences();
		showDialog(DIALOG_ID, savedInstanceState);
	}

	/**
	 * 从XML资源文件中读取可以配置的模块
	 */
	private void readXMLForChoiceArray() {
		Resources resources = getResources();
		mModuleNames = resources
				.getStringArray(R.array.pref_module_choice_values);
		mModuleTitles = resources
				.getStringArray(R.array.pref_module_choice_titles);
	}

	/**
	 * 从SharedPreferences里面读取当前的模块配置情况
	 */

	private void loadSharedPreferences() {
		// 获得偏好设置
		SharedPreferences appPrefs = getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		Set<String> result_set = null;
		// try {
		// result_set = appPrefs.getStringSet(KEY_NAME, null);
		// } catch (NoSuchMethodError e) {
		result_set = SharedPreferencesHandler.getStringSet(appPrefs, KEY_NAME,
				null);
		// }

		if (result_set == null) {
			// Load default settings
			String[] defaultModePref = getResources().getStringArray(
					R.array.pref_module_choice_def_vals);
			mChoosenModules = new HashSet<String>();
			for (String str : defaultModePref) {
				mChoosenModules.add(str);
			}
		} else {
			mChoosenModules = result_set;
		}

		mCheckedItems = new boolean[mModuleNames.length];
		for (int i = 0; i < mModuleNames.length; i++) {
			if (mChoosenModules.contains(mModuleNames[i])) {
				mCheckedItems[i] = true;
			} else {
				mCheckedItems[i] = false;
			}
		}
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id, Bundle args) {
		switch (id) {
		case DIALOG_ID:
			Builder builder = new Builder(this);
			builder.setTitle("选择需要在主界面显示的模块");
			builder.setMultiChoiceItems(mModuleTitles, mCheckedItems,
					new OnMultiChoiceClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							String moduleName = mModuleNames[which];
							if (isChecked) {
								mChoosenModules.add(moduleName);
							} else {
								mChoosenModules.remove(moduleName);
							}
						}
					});

			builder.setPositiveButton("确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					saveCurrentPreferences();
					finish();
				}
			});

			builder.setNegativeButton("取消", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					mDialog.cancel();
					finish();
				}
			});

			mDialog = builder.create();
			return mDialog;
		}

		return super.onCreateDialog(id, args);
	}

	protected void saveCurrentPreferences() {
		SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		Editor editor = prefs.edit();
		SharedPreferencesHandler
				.putStringSet(editor, KEY_NAME, mChoosenModules);
		editor.commit();
	}

}
