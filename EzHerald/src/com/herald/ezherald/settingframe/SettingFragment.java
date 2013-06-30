package com.herald.ezherald.settingframe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.herald.ezherald.R;

public class SettingFragment extends ListFragment {
	/*
	 * 标准左侧侧滑菜单用的ListFragment (non-Javadoc)
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
		ArrayAdapter<String> menuItemAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_list_item_1,
				android.R.id.text1, menuItemsStr);
		setListAdapter(menuItemAdapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView
	 * , android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		/*
		 * Fragment newContent = null; switch (position) { case 0: newContent =
		 * new StubContentFragment(); break; case 1: newContent = new
		 * MainContentFragment(); break; } if (newContent != null){
		 * switchFragment(newContent); }
		 */
		Intent i = new Intent();
		switch (position) {
		case 0:
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
