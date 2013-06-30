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

import com.herald.ezherald.BaseFrameActivity;
import com.herald.ezherald.MainActivity;
import com.herald.ezherald.R;
import com.herald.ezherald.academic.AcademicActivity;
import com.herald.ezherald.account.AccountActivity;
import com.herald.ezherald.activity.ActiActivity;
import com.herald.ezherald.agenda.AgendaActivity;
import com.herald.ezherald.curriculum.CurriculumActivity;
import com.herald.ezherald.exercise.ExerciseActivity;
import com.herald.ezherald.freshman.FreshmanActivity;
import com.herald.ezherald.gpa.GPAActivity;
import com.herald.ezherald.library.LibraryActivity;

/*
 * 标准左侧侧滑菜单用的ListFragment
 * (non-Javadoc)
 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
 */
public class MainMenuFragment extends ListFragment {

	private final boolean DEBUG_DONOT_KILL_ACTIVITY = false;
	private List<Map<String, Object>> mListItems;
	private MainMenuListItemAdapter mListViewAdapter;
	private String mMenuItemsStr[]; // 文字(title)
	private Integer mMenuItemsIconResId[] = {
			R.drawable.abs__ic_voice_search_api_holo_light,
			R.drawable.abs__ic_voice_search_api_holo_light,
			R.drawable.abs__ic_voice_search_api_holo_light,
			R.drawable.abs__ic_voice_search_api_holo_light,
			R.drawable.abs__ic_voice_search_api_holo_light,
			R.drawable.abs__ic_voice_search_api_holo_light,
			R.drawable.abs__ic_voice_search_api_holo_light,
			R.drawable.abs__ic_voice_search_api_holo_light,
			R.drawable.abs__ic_voice_search_api_holo_light }; // 图标(icon)

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		mMenuItemsStr = getResources().getStringArray(R.array.main_menu_items);

		mListItems = getListItems();
		mListViewAdapter = new MainMenuListItemAdapter(getActivity(),
				mListItems);
		setListAdapter(mListViewAdapter);
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
			i.setClass(getActivity(), MainActivity.class);
			break;
		case 1:
			i.setClass(getActivity(), CurriculumActivity.class);
			break;
		case 2:
			i.setClass(getActivity(), ActiActivity.class);
			break;
		case 3:
			i.setClass(getActivity(), AgendaActivity.class);
			break;
		case 4:
			i.setClass(getActivity(), LibraryActivity.class);
			break;
		case 5:
			i.setClass(getActivity(), GPAActivity.class);
			break;
		case 6:
			i.setClass(getActivity(), ExerciseActivity.class);
			break;
		case 7:
			i.setClass(getActivity(), AcademicActivity.class);
			break;
		case 8:
			i.setClass(getActivity(), FreshmanActivity.class);
			break;
		}
		if (i != null) {
			startActivity(i);
			TryKillMyself();
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

	/**
	 * 调用新的Activity后杀死自己..
	 */
	private void TryKillMyself() {

		if (!DEBUG_DONOT_KILL_ACTIVITY) {
			if (getActivity() instanceof BaseFrameActivity) {
				BaseFrameActivity baseActivity = (BaseFrameActivity) getActivity();
				baseActivity.KillMyself();
			}
		}
	}

	/**
	 * @deprecated
	 * @param newContent
	 */
	private void switchFragment(Fragment newContent) {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof MainActivity) {
			Log.d("MainMenuFrag", "I am in MainActivity.");
			MainActivity mainActivity = (MainActivity) getActivity();
			mainActivity.switchContent(newContent);
		}
	}

}
