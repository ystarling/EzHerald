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
import android.widget.Toast;

public class MainContentListColorPrefActivity extends SherlockActivity {

	private String[] mColorNames; // 模块名(英文)
	private String[] mColorTitles; // 模块名（中文）
	private Set<String> mChoosenColors; // 已选择的模块
	private boolean[] mCheckedItems;
	private Dialog mDialog;

	private String PREF_NAME;
	private String KEY_NAME;
	private final int DIALOG_ID = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.setting_main_content_module_pref);
		PREF_NAME = getResources().getString(R.string.main_frame_preferences);
		KEY_NAME = getResources().getString(
				R.string.main_frame_list_color_pref_key);
		readXMLForChoiceArray();
		loadSharedPreferences();
		showDialog(DIALOG_ID, savedInstanceState);
	}

	/**
	 * 从XML资源文件中读取可以配置的模块
	 */
	private void readXMLForChoiceArray() {
		Resources resources = getResources();
		mColorNames = resources
				.getStringArray(R.array.pref_listview_color_values);
		mColorTitles = resources
				.getStringArray(R.array.pref_listview_color_titles);
	}

	/**
	 * 从SharedPreferences里面读取当前的模块配置情况
	 */

	private void loadSharedPreferences() {
		// 获得偏好设置
		SharedPreferences appPrefs = getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		Set<String> result_set = null;

		result_set = SharedPreferencesHandler.getStringSet(appPrefs, KEY_NAME,
				null);
		// }

		if (result_set == null) {
			// Load default settings
			String[] defaultModePref = getResources().getStringArray(
					R.array.pref_listview_color_values);
			mChoosenColors = new HashSet<String>();
			for (String str : defaultModePref) {
				mChoosenColors.add(str);
			}
		} else {
			mChoosenColors = result_set;
		}

		mCheckedItems = new boolean[mColorNames.length];
		for (int i = 0; i < mColorNames.length; i++) {
			if (mChoosenColors.contains(mColorNames[i])) {
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
			builder.setTitle("选择主界面列表随机显示的颜色");
			builder.setCancelable(false);
			builder.setMultiChoiceItems(mColorTitles, mCheckedItems,
					new OnMultiChoiceClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							String moduleName = mColorNames[which];
							if (isChecked) {
								mChoosenColors.add(moduleName);
							} else {
								mChoosenColors.remove(moduleName);
							}
						}
					});
			builder.setPositiveButton("确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (mChoosenColors.isEmpty()) {
						Toast.makeText(getApplicationContext(),
								"没有选择可用颜色，将使用随机颜色", Toast.LENGTH_LONG).show();
						finish();
					} else {
						Toast.makeText(getApplicationContext(),
								"更改将在应用下次开启时生效", Toast.LENGTH_LONG).show();
						saveCurrentPreferences();
						finish();
					}
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
		SharedPreferencesHandler.putStringSet(editor, KEY_NAME, mChoosenColors);
		editor.commit();
	}

}
