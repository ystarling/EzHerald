package com.herald.ezherald.mainframe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.herald.ezherald.BaseFrameActivity;
import com.herald.ezherald.MainActivity;
import com.herald.ezherald.R;
import com.herald.ezherald.academic.AcademicActivity;
import com.herald.ezherald.activity.ActiActivity;
import com.herald.ezherald.curriculum.CurriculumActivity;
import com.herald.ezherald.exercise.ExerciseActivity;
import com.herald.ezherald.freshman.FreshmanActivity;
import com.herald.ezherald.gpa.GPAActivity;
import com.herald.ezherald.library.LibraryActivity;

/*
 * ��׼���໬�˵��õ�ListFragment
 * (non-Javadoc)
 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
 */
public class MainMenuFragment extends ListFragment {

	private static final String KEY_SHOWED_UPDATE = "showedUpdate"; //�˴������Ѿ���ʾ��������
	private final boolean DEBUG_DONOT_KILL_ACTIVITY = false;
	private List<Map<String, Object>> mListItems;
	private MainMenuListItemAdapter mListViewAdapter;
	private String mMenuItemsStr[]; // ����(title)
	private Integer mMenuItemsIconResId[] = {
			R.drawable.main_menu_ic_mainframe,
			R.drawable.main_menu_ic_curriculum,
			R.drawable.main_menu_ic_activity,
			//R.drawable.main_menu_ic_agenda,
			R.drawable.main_menu_ic_library,
			R.drawable.main_menu_ic_gpa,
			R.drawable.main_menu_ic_exercise,
			R.drawable.main_menu_ic_academic,
			R.drawable.main_menu_ic_freshman }; // ͼ��(icon)
	
	private Integer mMenuItemsIconSelectedResId[] = {
			R.drawable.main_menu_ic_mainframe_selected,
			R.drawable.main_menu_ic_curriculum_selected,
			R.drawable.main_menu_ic_activity_selected,
			//R.drawable.main_menu_ic_agenda,
			R.drawable.main_menu_ic_library_selected,
			R.drawable.main_menu_ic_gpa_selected,
			R.drawable.main_menu_ic_exercise_selected,
			R.drawable.main_menu_ic_academic_selected,
			R.drawable.main_menu_ic_freshman_selected }; // ѡ��״̬��ͼ��(icon)

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
		
		setListAdapter(mListViewAdapter);
		getListView().setCacheColorHint(Color.TRANSPARENT);
		getListView().setDivider(getResources().getDrawable(R.drawable.main_menu_divider_color));
		getListView().setDividerHeight(1);
		getListView().setPadding(0, 0, 0, 0);
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
		/*case 3:
			i.setClass(getActivity(), AgendaActivity.class);
			break;*/
		case 3:
			i.setClass(getActivity(), LibraryActivity.class);
			break;
		case 4:
			i.setClass(getActivity(), GPAActivity.class);
			break;
		case 5:
			i.setClass(getActivity(), ExerciseActivity.class);
			break;
		case 6:
			i.setClass(getActivity(), AcademicActivity.class);
			break;
		case 7:
			i.setClass(getActivity(), FreshmanActivity.class);
			break;
		}
		if (i != null) {
			i.putExtra(KEY_SHOWED_UPDATE, true);
			startActivity(i);
			TryKillMyself();
		}
	}

	/**
	 * ��ʼ���˵�����Ϣ
	 */
	private List<Map<String, Object>> getListItems() {
		//��ȡparent������Ϣ
		String localActivityName = getActivity().getLocalClassName();
		Log.d("MainMenuFragment", "Parent`s class = " + localActivityName);
		int selectedId = getActivityIdFromString(localActivityName);
		
		
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < mMenuItemsStr.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("icon", mMenuItemsIconResId[i]);
			map.put("icon_selected", mMenuItemsIconSelectedResId[i]);
			map.put("title", mMenuItemsStr[i]);
			if(selectedId == i){
				map.put("selected", true);
			} else {
				map.put("selected", false);
			}
			listItems.add(map);
		}
		return listItems;
	}

	/**
	 * ��getPackageName()�õ�����Ϣ��õ�ǰ��Activity���ĸ�
	 * ID�����R.array.main_menu_items�����˳�򣬴�0��ʼ
	 * @param localActivityName getActivity().getPackageName()�ķ���ֵ
	 * @return
	 */
	private int getActivityIdFromString(String localActivityName) {
		String[] splitResult = localActivityName.split("\\."); //http://www.cnblogs.com/liubiqu/archive/2008/08/14/1267867.html
		String localModuleName = "main";
		if(splitResult.length > 0){
			localModuleName = splitResult[0];
		} 
		Log.d("MainMenuFragment", "localModuleName = " + localModuleName);
		
		if(localModuleName.equals("MainActivity")){
			return 0;
		}
		else if(localModuleName.equals("curriculum")){
			return 1;
		}
		else if(localModuleName.equals("activity")){
			return 2;
		}
		/*else if(localModuleName.equals("agenda")){
			return 3;
		}*/
		else if(localModuleName.equals("library")){
			return 3;
		}
		else if(localModuleName.equals("gpa")){
			return 4;
		}
		else if(localModuleName.equals("exercise")){
			return 5;
		}
		else if(localModuleName.equals("academic")){
			return 6;
		}
		else if(localModuleName.equals("freshman")){
			return 7;
		}
		
		return -1;
	}

	/**
	 * �����µ�Activity��ɱ���Լ�..
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
