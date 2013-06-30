package com.herald.ezherald.mainframe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.herald.ezherald.MainActivity;
import com.herald.ezherald.R;
import com.herald.ezherald.account.AccountActivity;
import com.herald.ezherald.settingframe.SettingActivity;

/**
 * 右侧侧滑菜单的Fragment
 * @author BorisHe
 *	@updated 20130629
 */
public class SecondMenuFragment extends ListFragment {
	
	private List<Map<String, Object>> mListItems;
	private MainMenuListItemAdapter mListViewAdapter;
	private String mMenuItemsStr[]; // 文字(title)
	private Integer mMenuItemsIconResId[] = {
			R.drawable.abs__ic_voice_search_api_holo_light,
			R.drawable.abs__ic_voice_search_api_holo_light,
			R.drawable.abs__ic_voice_search_api_holo_light,
	}; // 图标(icon)
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.second_list, null);
	}

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mMenuItemsStr = getResources().getStringArray(
				R.array.second_menu_items);
		/**
		 * TODO:替换row0为当前登录的用户名
		 */
		/*
		ArrayAdapter<String> menuItemAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_list_item_1,
				android.R.id.text1, menuItemsStr);
		setListAdapter(menuItemAdapter);
		*/
		mListItems = getListItems();
		mListViewAdapter = new MainMenuListItemAdapter(getActivity(), mListItems);
		setListAdapter(mListViewAdapter);
	}

	/*
	 * 	 * 
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
			i.setClass(getActivity(), AccountActivity.class);
			break;
		case 1:
			i.setClass(getActivity(), SettingActivity.class);
			break;
		case 2:
			i.setClass(getActivity(), AccountActivity.class);
			break;
		}
		if (i != null) {
			startActivity(i);
		}
	}
	
	/**
	 * 初始化菜单项信息
	 */
	private List<Map<String, Object>> getListItems() {
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < mMenuItemsStr.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("icon", mMenuItemsIconResId[i]);
			map.put("title", mMenuItemsStr[i]);
			listItems.add(map);
		}
		return listItems;
	}
}
