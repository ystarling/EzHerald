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
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
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
import android.widget.ListView;

import com.herald.ezherald.BaseFrameActivity;
import com.herald.ezherald.MainActivity;
import com.herald.ezherald.R;
import com.herald.ezherald.academic.AcademicActivity;
import com.herald.ezherald.account.AccountActivity;
import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.UserAccount;
import com.herald.ezherald.activity.ActiActivity;
import com.herald.ezherald.curriculum.CurriculumActivity;
import com.herald.ezherald.emptyclassroom.EmptyClassroomActivity;
import com.herald.ezherald.exercise.ExerciseActivity;
import com.herald.ezherald.freshman.FreshmanActivity;
import com.herald.ezherald.gpa.GPAActivity;
import com.herald.ezherald.library.LibraryActivity;
import com.herald.ezherald.radio.RadioActivity;
import com.tendcloud.tenddata.TCAgent;

/**
 * ���˵�Fragment
 * @author BorisHe
 *
 */
public class MainMenuFragment extends ListFragment {

	public static final String TEXT_IDCARD_NOT_LOGIN = "һ��ͨ\nδ��¼";
	public static final String TEXT_IDCARD_IS_LOGIN = "һ��ͨ\n�ѵ�¼";
	public static final String TEXT_TYX_NOT_LOGIN = "����ϵ\nδ��¼";
	public static final String TEXT_TYX_IS_LOGIN = "����ϵ\n�ѵ�¼";
	public static final String TEXT_LIB_NOT_LOGIN = "ͼ���\nδ��¼";
	public static final String TEXT_LIB_IS_LOGIN = "ͼ���\n�ѵ�¼";
	
	public static final String SHARED_PREF_NAME = "pref_secondmenu";

	private static final String KEY_SHOWED_UPDATE = "showedUpdate"; //�˴������Ѿ���ʾ��������
	private final boolean DEBUG_DONOT_KILL_ACTIVITY = false;
	private List<Map<String, Object>> mListItems;
	private MainMenuListItemAdapter mListViewAdapter;
	private String mMenuItemsStr[]; // ����(title)
	private Integer mMenuItemsIconResId[] = {
			R.drawable.main_2ndmenu_ic_account, //�˻���ͼ��
			R.drawable.main_menu_ic_mainframe,
			R.drawable.main_menu_ic_curriculum,
			R.drawable.main_menu_ic_activity,
			//R.drawable.main_menu_ic_agenda,
			R.drawable.main_menu_ic_library,
			R.drawable.main_menu_ic_gpa,
			R.drawable.main_menu_ic_exercise,
			R.drawable.main_menu_ic_academic,
			R.drawable.main_menu_ic_freshman,
			R.drawable.main_menu_ic_emptcls, 
			R.drawable.main_menu_ic_gpa}; // ͼ��(icon)
	
	private Integer mMenuItemsIconSelectedResId[] = {
			R.drawable.main_2ndmenu_ic_account_login, //�ѵ�¼�˻�
			R.drawable.main_menu_ic_mainframe_selected,
			R.drawable.main_menu_ic_curriculum_selected,
			R.drawable.main_menu_ic_activity_selected,
			//R.drawable.main_menu_ic_agenda,
			R.drawable.main_menu_ic_library_selected,
			R.drawable.main_menu_ic_gpa_selected,
			R.drawable.main_menu_ic_exercise_selected,
			R.drawable.main_menu_ic_academic_selected,
			R.drawable.main_menu_ic_freshman_selected,
			R.drawable.main_menu_ic_emptcls_selected, 
			R.drawable.main_menu_ic_gpa_selected,
	}; // ѡ��״̬��ͼ��(icon)
			
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
	 * �������ݼ��еĵ�½״̬
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
			mMenuItemsStr[0] = "��δ��½";
			mIdCardState = TEXT_IDCARD_NOT_LOGIN;
		}
		
		//��������2����½�˻���״̬
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
		}
		TCAgent.onEvent(getActivity(), "���˵����", menuTarget);
		
		if (i != null) {
			i.putExtra(KEY_SHOWED_UPDATE, true);
			startActivity(i);
			if(position != 0)
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
			if(i == 0){
				//�˻��ĸ�����Ϣ
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
			return 1;
		}
		else if(localModuleName.equals("curriculum")){
			return 2;
		}
		else if(localModuleName.equals("activity")){
			return 3;
		}
		/*else if(localModuleName.equals("agenda")){
			return 3;
		}*/
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
		else if(localModuleName.equals("radio")){
			return 9;
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
	 * �յ��û�����������󣬽��Ҳ�˵������ݸ���
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
	 * ����ͨ��һ��ͨ�Ż�ȡ�û�������
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
				//ȡ��HttpClient����
				HttpClient httpClient = new DefaultHttpClient();
				//����HttpClient���õ�Response
				HttpResponse httpResponse = httpClient.execute(httpGet);
				
				if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					String username = EntityUtils.toString(httpResponse.getEntity());
					
					if(mViewDestroyed)
						return;
					
					setSavedUserId(mIdNum);  //����Prefs!
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
	 * ���Preferences���汣���ŵ�һ��ͨ��
	 * ���û���򷵻�null
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
	 * ���Preferences���汣���ŵ�����
	 * ���û���򷵻�null
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
	 * ��Ϊ�������Ҳ�ĵ�½״̬��Ϣ
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
