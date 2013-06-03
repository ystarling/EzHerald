package com.herald.ezherald.mainframe;

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
import android.widget.Toast;

import com.herald.ezherald.MainActivity;
import com.herald.ezherald.R;
import com.herald.ezherald.account.AccountActivity;
import com.herald.ezherald.appsetting.AppSettingActivity;

public class SecondMenuFragment extends ListFragment {
	/*
	 * 标准左侧侧滑菜单用的ListFragment
	 * (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.second_list, null);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		String menuItemsStr[] = getResources().getStringArray(R.array.second_menu_items);
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
		// TODO :(需要自定义ListFragment)增加一个更美观的用户登录状态显示
		//参考：
		// http://www.doc88.com/p-70581205513.html
		// http://www.imobilebbs.com/wordpress/archives/918
		// http://androiddada.iteye.com/blog/1340228
		// http://blog.csdn.net/xyz_lmn/article/details/6906396
		super.onListItemClick(l, v, position, id);
		
		Intent i = new Intent();
		switch (position){
		case 0:
			Toast.makeText(getActivity(), "完善中。", Toast.LENGTH_SHORT).show();
			i = null;
			break;
		case 1:
			i.setClass(getActivity(), AppSettingActivity.class); //账户设置
			break;
		case 2:
			i.setClass(getActivity(), AccountActivity.class); //主程序设置
			break;
		default:
			i = null;	
		}
		if (i != null){
			startActivity(i);			
		}
	}

	/*
	private void switchFragment(Fragment newContent) {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof MainActivity){
			Log.d("MainMenuFrag", "I am in MainActivity.");
			MainActivity mainActivity = (MainActivity)getActivity();
			mainActivity.switchContent(newContent);
		}
	}
	*/
	

}
