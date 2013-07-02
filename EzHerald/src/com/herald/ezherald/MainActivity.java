package com.herald.ezherald;

import java.util.ArrayList;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.mainframe.MainContentFragment;
import com.herald.ezherald.mainframe.MainGuideActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.herald.ezherald.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/*
 * @author 何博伟
 * @since 2013.05.14
 * @updated 2013.7.1
 * 程序的主Activity
 * 
 * 
 */
public class MainActivity extends BaseFrameActivity {

	Fragment mContentFrag;
	Menu mActionMenu;
	Handler mMoveHandler;
	SlidingMenu mSlidingMenu;
	
	private final String PREF_NAME = "com.herald.ezherald_preferences";
	private final String KEY_NAME = "first_start";
	private final boolean DEBUG_ALWAYS_SHOW_GUIDE = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mContentFrag = new MainContentFragment();
		super.SetBaseFrameActivity(mContentFrag);
		super.onCreate(savedInstanceState);
		
		mSlidingMenu = super.menu;
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		
		/*mMoveHandler = new Handler(){
			@Override
			public void handleMessage(android.os.Message msg) {
				Log.d("mMoveHandler", "received");
				mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			};
		};*/
		
		boolean isOldUser = checkGuideState();
		Log.d("MainActivity", "GuideViewed ?:" + isOldUser);
		if((!isOldUser) || DEBUG_ALWAYS_SHOW_GUIDE)
		{
			Intent i = new Intent();
			i.setClass(this, MainGuideActivity.class);
			startActivity(i);
			setGuideViewed();
		}
	}
	
	
	/**
	 * 从SharedPreference中读取是否已经运行过程序
	 * pref: true:已经运行过
	 * 		false:没有运行过（需要运行一次Guide）
	 * @return
	 */
	private boolean checkGuideState() {
		
		//@ref Pg251 <Android4 编程入门经典>
		SharedPreferences appPreferences = 
				getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		return appPreferences.getBoolean(KEY_NAME, false);
	}
	
	/**
	 * 设置Guide已经阅读过
	 */
	private void setGuideViewed(){
		SharedPreferences appPreferences = 
				getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = appPreferences.edit();
		prefEditor.putBoolean(KEY_NAME, true);
		prefEditor.commit();
	}


	/**
	 * @deprecated
	 * @param fragment
	 */
	public void switchContent(Fragment fragment) {
		/*
		 * 切换content碎片内容
		 * 
		 * @param fragment 传入的要替换的碎片
		 */
		mContentFrag = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.empty_frame_content, fragment).commit();
		getSlidingMenu().showContent();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu_main_content, menu);
		mActionMenu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * 上侧Title位置的按钮点击相应
		 */
		switch (item.getItemId()) {
		case R.id.action_settings:
			// Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
			menu.showSecondaryMenu();
			break;
		case R.id.mainframe_menu_item_exit:
			finish();
			break;
		case android.R.id.home:
			menu.toggle(true); // 点击了程序图标后，会弹出/收回侧面菜单
			break;
		case R.id.main_content_refresh:
			MainContentFragment mainFrag = (MainContentFragment)mContentFrag;
			mainFrag.refreshInfo(); //各模块的内容(GridView中)同步更新就行，向各个模块索取
			requestInfoUpdate("blabla", item);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void requestInfoUpdate(String url, MenuItem item)
	{
		item.setVisible(false);
		MenuItem doingItem = mActionMenu.findItem(R.id.mainframe_menu_item_doing);
		doingItem.setVisible(true);
		new UpdateBannerImageTask().execute(url);
	}
	
	/**
	 * 联网更新图片
	 */
	private class UpdateBannerImageTask extends AsyncTask<String, Void, ArrayList<String>>{

		@Override
		protected ArrayList<String> doInBackground(String... url) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			// TODO Auto-generated method stub
			MenuItem item = mActionMenu.findItem(R.id.main_content_refresh);
			item.setVisible(true);
			MenuItem doingItem = mActionMenu.findItem(R.id.mainframe_menu_item_doing);
			doingItem.setVisible(false);
			Toast.makeText(getBaseContext(), "Done", Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}
		
	}

	/*@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		MainContentFragment mainFrag = (MainContentFragment)mContentFrag;
		boolean isViewFlowOnTouch = mainFrag.isViewFlowOnTouch();
		
		if(ev.getAction() == MotionEvent.ACTION_MOVE){
			Log.d("MainActivity", "" + isViewFlowOnTouch);
			super.menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			if(!isViewFlowOnTouch){
				Message message = mMoveHandler.obtainMessage(0);
				mMoveHandler.sendMessageDelayed(message, 1000);  //TODO:临时解决方案
			}
		}
		return super.dispatchTouchEvent(ev);
	}*/
	
}
