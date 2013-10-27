package com.herald.ezherald.settingframe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.herald.ezherald.R;
import com.herald.ezherald.mainframe.MainGuideActivity;

public class SettingFragment extends SherlockListFragment {
	/*
	 * 软件设置的ListFragment (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater
	 * , android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.second_list, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		String menuItemsStr[] = getResources().getStringArray(
				R.array.setting_menu_titles);
		String menuItemsSubStr[] = getResources().getStringArray(
				R.array.setting_menu_subtitles);

		List<Map<String, String>> mListItems = new ArrayList<Map<String, String>>();
		for (int i = 0; i < menuItemsStr.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("title", menuItemsStr[i]);
			map.put("subtitle", menuItemsSubStr[i]);
			mListItems.add(map);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),
				mListItems, android.R.layout.simple_list_item_2, new String[] {
						"title", "subtitle" }, new int[] { android.R.id.text1,
						android.R.id.text2 });
		// ArrayAdapter<String> menuItemAdapter = new ArrayAdapter<String>(
		// getActivity(), android.R.layout.simple_list_item_1,
		// android.R.id.text1, menuItemsStr);

		setListAdapter(simpleAdapter);
	}


	/**
	 * 列表项点击的响应
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent();
		switch (position) {
		case 0:
			i.setClass(getActivity(), MainContentModulePrefActivity.class);
			break;
		case 1:
			i.setClass(getActivity(), MainContentListColorPrefActivity.class);
			break;
		case 2:
			i.setClass(getActivity(), SyncFrequencySettingActivity.class);
			break;
		case 3:
			i.setClass(getActivity(), AppUpdateActivity.class);
			break;
		case 4:
			i.setClass(getActivity(), MainGuideActivity.class);
			break;
		case 5:
			i.setClass(getActivity(), AboutThisApp.class);
			break;
		default:
			Toast.makeText(getActivity(), "Default", Toast.LENGTH_SHORT).show();
			break;
		}
		if (i != null) {
			startActivity(i);
		}
	}

}
