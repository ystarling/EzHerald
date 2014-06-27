package com.herald.ezherald.mainframe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.herald.ezherald.account.AccountActivity;
import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.UserAccount;
import com.herald.ezherald.activity.ActiActivity;
import com.herald.ezherald.bookingOffice.BookingActivity;
import com.herald.ezherald.curriculum.CurriculumActivity;
import com.herald.ezherald.emptyclassroom.EmptyClassroomActivity;
import com.herald.ezherald.exercise.ExerciseActivity;
import com.herald.ezherald.freshman.FreshmanActivity;
import com.herald.ezherald.gpa.GPAActivity;
import com.herald.ezherald.library.LibraryActivity;
import com.herald.ezherald.radio.RadioActivity;
import com.tendcloud.tenddata.TCAgent;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主菜单Fragment
 * @author BorisHe
 *
 */
public class MainMenuFragment extends ListFragment {

	public static final String TEXT_IDCARD_NOT_LOGIN = "一卡通\n未登录";
	public static final String TEXT_IDCARD_IS_LOGIN = "一卡通\n已登录";
	public static final String TEXT_TYX_NOT_LOGIN = "体育系\n未登录";
	public static final String TEXT_TYX_IS_LOGIN = "体育系\n已登录";
	public static final String TEXT_LIB_NOT_LOGIN = "图书馆\n未登录";
	public static final String TEXT_LIB_IS_LOGIN = "图书馆\n已登录";
	
	public static final String SHARED_PREF_NAME = "pref_secondmenu";

	private static final String KEY_SHOWED_UPDATE = "showedUpdate"; //此次运行已经显示过更新了
	private final boolean DEBUG_DONOT_KILL_ACTIVITY = false;
	private List<Map<String, Object>> mListItems;
	private MainMenuListItemAdapter mListViewAdapter;
	private String mMenuItemsStr[]; // 文字(title)
	private Integer mMenuItemsIconResId[] = {
			R.drawable.main_2ndmenu_ic_account, //账户的图标
			R.drawable.main_menu_ic_mainframe,
			R.drawable.main_menu_ic_curriculum,
			R.drawable.main_menu_ic_activity,
			R.drawable.main_menu_ic_library,
			R.drawable.main_menu_ic_gpa,
			R.drawable.main_menu_ic_exercise,
			R.drawable.main_menu_ic_academic,
			R.drawable.main_menu_ic_freshman,
			R.drawable.main_menu_ic_emptcls, 
			R.drawable.main_menu_ic_gpa,
			R.drawable.main_menu_ic_gpa
			}; // 图标(icon)
	
	private Integer mMenuItemsIconSelectedResId[] = {
			R.drawable.main_2ndmenu_ic_account_login, //已登录账户
			R.drawable.main_menu_ic_mainframe_selected,
			R.drawable.main_menu_ic_curriculum_selected,
			R.drawable.main_menu_ic_activity_selected,
			R.drawable.main_menu_ic_library_selected,
			R.drawable.main_menu_ic_gpa_selected,
			R.drawable.main_menu_ic_exercise_selected,
			R.drawable.main_menu_ic_academic_selected,
			R.drawable.main_menu_ic_freshman_selected,
			R.drawable.main_menu_ic_emptcls_selected, 
			R.drawable.main_menu_ic_gpa_selected,
			R.drawable.main_menu_ic_gpa_selected
	}; // 选中状态的图标(icon)
			
	private String mIdCardState; 
	private String mLibState;
	private String mTyxState;
	
	private final String BUNDLE_KEY_USERNAME = "username";
	
	private final String PREF_KEY_USERID = "userid";
	private final String PREF_KEY_USERNAME = "username";
	
	private boolean mViewDestroyed = false;
	private UserNameHandler mUserNameHandler = new UserNameHandler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mViewDestroyed = false;
		return inflater.inflate(R.layout.list, null);
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		mMenuItemsStr = getResources().getStringArray(R.array.main_menu_items);
		updateLoginUserNameTitles();

		mListItems = getListItems();
		mListViewAdapter = new MainMenuListItemAdapter(getActivity(),
				mListItems);
		setListAdapter(mListViewAdapter);
		
		
		getListView().setCacheColorHint(Color.TRANSPARENT);
		getListView().setDivider(getResources().getDrawable(R.drawable.main_menu_divider_color));
		getListView().setDividerHeight(1);
		getListView().setPadding(0, 0, 0, 0);
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
				mMenuItemsStr[0] = getSavedUserName();
			} else {
				mMenuItemsStr[0] = account.getUsername();
				new Thread(new UserNameRunnable(account.getUsername())).start();
			}
			mIdCardState = TEXT_IDCARD_IS_LOGIN;
		} else {
			mMenuItemsStr[0] = "尚未登陆";
			mIdCardState = TEXT_IDCARD_NOT_LOGIN;
		}
		
		//更新另外2个登陆账户的状态
		account = Authenticate.getLibUser(getActivity());
		if(null != account){
			mLibState =  TEXT_LIB_IS_LOGIN;
		} else {
			mLibState = TEXT_LIB_NOT_LOGIN;
		}
		
		account = Authenticate.getTyxUser(getActivity());
		if(null != account){
			mTyxState = TEXT_TYX_IS_LOGIN;
		} else {
			mTyxState = TEXT_TYX_NOT_LOGIN;
		}
	}


	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		String menuTarget = "Unknown";
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
			menuTarget = "Account";
			break;
		case 1:
			i.setClass(getActivity(), MainActivity.class);
			menuTarget = "MainActivity";
			break;
		case 2:
			i.setClass(getActivity(), CurriculumActivity.class);
			menuTarget = "Curriculum";
			break;
		case 3:
			i.setClass(getActivity(), ActiActivity.class);
			menuTarget = "Activity";
			break;
		/*case 3:
			i.setClass(getActivity(), AgendaActivity.class);
			break;*/
		case 4:
			i.setClass(getActivity(), LibraryActivity.class);
			menuTarget = "Library";
			break;
		case 5:
			i.setClass(getActivity(), GPAActivity.class);
			menuTarget = "GPA";
			break;
		case 6:
			i.setClass(getActivity(), ExerciseActivity.class);
			menuTarget = "Exercise";
			break;
		case 7:
			i.setClass(getActivity(), AcademicActivity.class);
			menuTarget = "Academic";
			break;
		case 8:
			i.setClass(getActivity(), FreshmanActivity.class);
			menuTarget = "CampusGuide";
			break;
		case 9:
			i.setClass(getActivity(), EmptyClassroomActivity.class);
			menuTarget = "EmptyClass";
			break;
			
		case 10:
			i.setClass(getActivity(), RadioActivity.class);
			menuTarget = "Radio";
			break;
		case 11:
			i.setClass(getActivity(), BookingActivity.class);
			menuTarget = "bookingOffice";
			break;
		
		}
		TCAgent.onEvent(getActivity(), "主菜单点击", menuTarget);
		
		if (i != null) {
			i.putExtra(KEY_SHOWED_UPDATE, true);
			startActivity(i);
			if(position != 0)
			TryKillMyself();
		}
	}

	/**
	 * 初始化菜单项信息
	 */
	private List<Map<String, Object>> getListItems() {
		//获取parent的类信息
		String localActivityName = getActivity().getLocalClassName();
		Log.d("MainMenuFragment", "Parent`s class = " + localActivityName);
		int selectedId = getActivityIdFromString(localActivityName);
		
		
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < mMenuItemsStr.length; i++) {
			
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("icon", mMenuItemsIconResId[i]);
			map.put("icon_selected", mMenuItemsIconSelectedResId[i]);
			map.put("title", mMenuItemsStr[i]);
			if(i == 0){
				//账户的附加信息
				map.put("idCardState", mIdCardState);
				map.put("tyxState", mTyxState);
				map.put("libState", mLibState);
				
				if(mIdCardState.equals(TEXT_IDCARD_IS_LOGIN)){
					map.put("icon", R.drawable.main_2ndmenu_ic_account_login);
				}
			}
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
	 * 从getPackageName()得到的信息获得当前的Activity是哪个
	 * ID排序见R.array.main_menu_items里面的顺序，从0开始
	 * @param localActivityName getActivity().getPackageName()的返回值
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
			return 1;
		}
		else if(localModuleName.equals("curriculum")){
			return 2;
		}
		else if(localModuleName.equals("activity")){
			return 3;
		}
		else if(localModuleName.equals("library")){
			return 4;
		}
		else if(localModuleName.equals("gpa")){
			return 5;
		}
		else if(localModuleName.equals("exercise")){
			return 6;
		}
		else if(localModuleName.equals("academic")){
			return 7;
		}
		else if(localModuleName.equals("freshman")){
			return 8;
		}
		else if(localModuleName.equals("emptyclassroom")){
			return 9;
		}
		/*
		else if(localModuleName.equals("radio")){
			return 10;
		}
		*/
		return -1;
	}

	/**
	 * 调用新的Activity后杀死自己..
	 */
	private void TryKillMyself() {

		if (!DEBUG_DONOT_KILL_ACTIVITY) {
			if (getActivity() instanceof BaseFrameActivity) {
				BaseFrameActivity baseActivity = (BaseFrameActivity) getActivity();
				baseActivity.killMyself();
			}
		}
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
			if(mViewDestroyed)
			return;
			
			try {
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
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	}
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
					
					if(mViewDestroyed)
						return;
					
					setSavedUserId(mIdNum);  //保存Prefs!
					setSavedUserName(username);
					
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putString(BUNDLE_KEY_USERNAME, username);
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
	
	/**
	 * 获得Preferences里面保存着的一卡通号
	 * 如果没有则返回null
	 * @return
	 */
	public String getSavedUserId(){
		SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
		String uid = prefs.getString(PREF_KEY_USERID, null);
		
		return uid;
	}
	
	public boolean setSavedUserId(String id){
		try{
			SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
			Editor editor = prefs.edit();
			editor.putString(PREF_KEY_USERID, id);
			return editor.commit();
		} catch (NullPointerException e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	/**
	 * 获得Preferences里面保存着的姓名
	 * 如果没有则返回null
	 * @return
	 */
	public String getSavedUserName(){
		SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);//.getPreferences(Context.MODE_PRIVATE);
		String name = prefs.getString(PREF_KEY_USERNAME, null);
		
		return name;
	}
	
	public boolean setSavedUserName(String name){
		try{
			SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
			Editor editor = prefs.edit();
			editor.putString(PREF_KEY_USERNAME, name);
			return editor.commit();
		} catch (NullPointerException e){
			e.printStackTrace();
			return false;
		}	
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
	
	@Override
	public void onDestroyView() {
		mViewDestroyed = true;
		mUserNameHandler.removeCallbacksAndMessages(null);
		super.onDestroyView();
	}
}
