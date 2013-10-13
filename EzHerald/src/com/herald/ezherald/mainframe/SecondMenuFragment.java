package com.herald.ezherald.mainframe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.UserAccount;
import com.herald.ezherald.account.UserInfoActivity;
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
			R.drawable.main_2ndmenu_ic_account,
			R.drawable.main_2ndmenu_ic_setting,
			R.drawable.main_2ndmenu_ic_accsetting,
	}; // 图标(icon)
	
	private final String BUNDLE_KEY_USERNAME = "username";
	private UserNameHandler mUserNameHandler = new UserNameHandler();
	private final String PREF_KEY_USERID = "userid";
	private final String PREF_KEY_USERNAME = "username";
	
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
		 * TODO:替换row0为当前登录的用户名（现在还是一卡通号）
		 */
		
		updateLoginUserNameTitles();
		
		/*UserAccount account = Authenticate.getIDcardUser(getActivity());
		if(null != account){
			mMenuItemsStr[0] = account.getUsername();
		} else {
			mMenuItemsStr[0] = "尚未登陆";
		}*/ //现通过updateLoginUserNameTitles()方法调用
		
		mListItems = getListItems();
		mListViewAdapter = new MainMenuListItemAdapter(getActivity(), mListItems);
		setListAdapter(mListViewAdapter);
	}

	/**
	 * 更新数据集中的登陆状态
	 */
	private void updateLoginUserNameTitles() {
		UserAccount account = Authenticate.getIDcardUser(getActivity());
		if(null != account){
			String currUserId = account.getUsername();
			String oldUserId = getSavedUserId();
			if(oldUserId != null && currUserId.equals(oldUserId)){
				mMenuItemsStr[0] = currUserId + "\n" + getSavedUserName();
			} else {
				mMenuItemsStr[0] = account.getUsername();
				new Thread(new UserNameRunnable(account.getUsername())).start();
			}
		} else {
			mMenuItemsStr[0] = "尚未登陆";
		}
	}


	/*
	 * 	 * 
	 * @see
	 * android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView
	 * , android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);

		Intent i = new Intent();
		switch (position) {
		case 0:
			i.setClass(getActivity(), AccountActivity.class);
			break;
		case 1:
			i.setClass(getActivity(), SettingActivity.class);
			MainActivity mainActivity = (MainActivity)getActivity();
			mainActivity.needRefreshContent = true;
			break;
		case 2:
			i.setClass(getActivity(), AccountActivity.class);
			break;
		}
		if (i != null) {
			startActivity(i);
			//getActivity().finish(); //20130704
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
	 * 行为：更新右侧的登陆状态信息
	 */
	@Override
	public void onResume() {
		
		super.onResume();
		
		//Toast.makeText(getActivity(), "OnResume", Toast.LENGTH_SHORT).show();
		
		updateLoginUserNameTitles();
		
		mListItems = getListItems();
		mListViewAdapter.setmListItems(mListItems);
		mListViewAdapter.notifyDataSetChanged();
		
	}
	
	/**
	 * 获得Preferences里面保存着的一卡通号
	 * 如果没有则返回null
	 * @return
	 */
	public String getSavedUserId(){
		SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
		String uid = prefs.getString(PREF_KEY_USERID, null);
		
		return uid;
	}
	
	public boolean setSavedUserId(String id){
		SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString(PREF_KEY_USERID, id);
		return editor.commit();
	}
	
	/**
	 * 获得Preferences里面保存着的姓名
	 * 如果没有则返回null
	 * @return
	 */
	public String getSavedUserName(){
		SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
		String name = prefs.getString(PREF_KEY_USERNAME, null);
		
		return name;
	}
	
	public boolean setSavedUserName(String name){
		SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString(PREF_KEY_USERNAME, name);
		return editor.commit();
	}
	
	
	/**
	 * 收到用户名更新请求后，将右侧菜单的内容更新
	 * @author BorisHe
	 *
	 */
	private class UserNameHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			String name = bundle.getString(BUNDLE_KEY_USERNAME);
			if(name == null)
			{
				Log.e("SecondMenuFragment", "Message contains nothing");
				return;
			}
			mMenuItemsStr[0] = name;
			mListItems = getListItems();
			mListViewAdapter.setmListItems(mListItems);
			mListViewAdapter.notifyDataSetChanged();
		}
		
	}
	
	/**
	 * 联网通过一卡通号获取用户的姓名
	 * @author BorisHe
	 *
	 */
	private class UserNameRunnable implements Runnable{
		private String mIdNum;
		private final String UPDATE_URI = "http://herald.seu.edu.cn/EzHerald/getname/";
		
		public UserNameRunnable(String id){
			mIdNum = id;
		}
		
		@Override
		public void run() {
			String url = UPDATE_URI + "?cardnum=" + mIdNum;
			HttpGet httpGet = new HttpGet(url);
			
			try{
				//取得HttpClient对象
				HttpClient httpClient = new DefaultHttpClient();
				//请求HttpClient，拿到Response
				HttpResponse httpResponse = httpClient.execute(httpGet);
				
				if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					String username = EntityUtils.toString(httpResponse.getEntity());
					
					setSavedUserId(mIdNum);  //保存Prefs!
					setSavedUserName(username);
					
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putString(BUNDLE_KEY_USERNAME, mIdNum + "\n" + username);
					msg.setData(bundle);
					
					Looper.prepare();
					mUserNameHandler.sendMessage(msg);
				} else {
					Log.e("SecondMenuFragment", "Fail to get username by id --- Connection error");
				}
			} catch (ClientProtocolException e){
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	
}
