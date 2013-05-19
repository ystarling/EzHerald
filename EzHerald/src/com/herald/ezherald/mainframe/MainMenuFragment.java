package com.herald.ezherald.mainframe;

import android.app.Activity;
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
import com.herald.ezherald.academic.AcademicActivity;
import com.herald.ezherald.account.AccountActivity;
import com.herald.ezherald.activity.ActiActivity;
import com.herald.ezherald.agenda.AgendaActivity;
import com.herald.ezherald.curriculum.CurriculumActivity;
import com.herald.ezherald.exercise.ExerciseActivity;
import com.herald.ezherald.freshman.FreshmanActivity;
import com.herald.ezherald.gpa.GPAActivity;
import com.herald.ezherald.library.LibraryActivity;
import com.herald.ezherald.stubframe.StubContentFragment;

public class MainMenuFragment extends ListFragment {
	/*
	 * 标准左侧侧滑菜单用的ListFragment
	 * (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		String menuItemsStr[] = getResources().getStringArray(R.array.main_menu_items);
		ArrayAdapter<String> menuItemAdapter = 
				new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
						android.R.id.text1, menuItemsStr);
		setListAdapter(menuItemAdapter);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		/*
		Fragment newContent = null;
		switch (position)
		{
		case 0:
			newContent = new StubContentFragment();
			break;
		case 1:
			newContent = new MainContentFragment();
			break;
		}
		if (newContent != null){
			switchFragment(newContent);
		}
		*/
		Intent i = new Intent();
		switch (position){
		case 0:
			i.setClass(getActivity(), CurriculumActivity.class);
			break;
		case 1:
			i.setClass(getActivity(), ActiActivity.class);
			break;
		case 2:
			i.setClass(getActivity(), AgendaActivity.class);
			break;
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
		if (i != null){
			startActivity(i);			
		}
	}

	private void switchFragment(Fragment newContent) {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof MainActivity){
			Log.d("MainMenuFrag", "I am in MainActivity.");
			MainActivity mainActivity = (MainActivity)getActivity();
			mainActivity.switchContent(newContent);
		}
	}
	
	

}
