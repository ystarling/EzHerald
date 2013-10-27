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

	private String[] mColorNames; // ģ����(Ӣ��)
	private String[] mColorTitles; // ģ���������ģ�
	private Set<String> mChoosenColors; // ��ѡ���ģ��
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
	 * ��XML��Դ�ļ��ж�ȡ�������õ�ģ��
	 */
	private void readXMLForChoiceArray() {
		Resources resources = getResources();
		mColorNames = resources
				.getStringArray(R.array.pref_listview_color_values);
		mColorTitles = resources
				.getStringArray(R.array.pref_listview_color_titles);
	}

	/**
	 * ��SharedPreferences�����ȡ��ǰ��ģ���������
	 */

	private void loadSharedPreferences() {
		// ���ƫ������
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
			builder.setTitle("ѡ���������б������ʾ����ɫ");
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
			builder.setPositiveButton("ȷ��", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (mChoosenColors.isEmpty()) {
						Toast.makeText(getApplicationContext(),
								"û��ѡ�������ɫ����ʹ�������ɫ", Toast.LENGTH_LONG).show();
						finish();
					} else {
						Toast.makeText(getApplicationContext(),
								"���Ľ���Ӧ���´ο���ʱ��Ч", Toast.LENGTH_LONG).show();
						saveCurrentPreferences();
						finish();
					}
				}
			});

			builder.setNegativeButton("ȡ��", new OnClickListener() {

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
