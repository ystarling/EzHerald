package com.herald.ezherald;

import java.util.ArrayList;

import org.taptwo.android.widget.ViewFlow;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.mainframe.MainContentFragment;
import com.herald.ezherald.mainframe.MainGuideActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.herald.ezherald.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/*
 * @author �β�ΰ
 * @since 2013.05.14
 * @updated 2013.7.1
 * �������Activity
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
	private final boolean DEBUG_ALWAYS_SHOW_GUIDE = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mContentFrag = new MainContentFragment();
		super.SetBaseFrameActivity(mContentFrag);
		super.onCreate(savedInstanceState);
		
		mSlidingMenu = super.menu;
		
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
	 * ��SharedPreference�ж�ȡ�Ƿ��Ѿ����й�����
	 * pref: true:�Ѿ����й�
	 * 		false:û�����й�����Ҫ����һ��Guide��
	 * @return
	 */
	private boolean checkGuideState() {
		
		//@ref Pg251 <Android4 ������ž���>
		SharedPreferences appPreferences = 
				getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		return appPreferences.getBoolean(KEY_NAME, false);
	}
	
	/**
	 * ����Guide�Ѿ��Ķ���
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
		 * �л�content��Ƭ����
		 * 
		 * @param fragment �����Ҫ�滻����Ƭ
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
		 * �ϲ�Titleλ�õİ�ť�����Ӧ
		 */
		switch (item.getItemId()) {
		/*case R.id.action_settings:
			// Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
			menu.showSecondaryMenu(true);
			break;
		case R.id.mainframe_menu_item_exit:
			finish();
			break;
		case android.R.id.home:
			menu.toggle(true); // ����˳���ͼ��󣬻ᵯ��/�ջز���˵�
			break;*/
		case R.id.main_content_refresh:
			MainContentFragment mainFrag = (MainContentFragment)mContentFrag;
			mainFrag.refreshInfo(); //��ģ�������(GridView��)ͬ�����¾��У������ģ����ȡ
			requestInfoUpdate("blabla", item);
			return true;
		}
		
		return super.onOptionsItemSelected(item); //��������bug�����ɣ���Ϣ��������������!
	}
	
	
	public void requestInfoUpdate(String url, MenuItem item)
	{
		item.setVisible(false);
		MenuItem doingItem = mActionMenu.findItem(R.id.mainframe_menu_item_doing);
		doingItem.setVisible(true);
		new UpdateBannerImageTask().execute(url);
	}
	
	/**
	 * ��������ͼƬ
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

	
	/**
	 * ViewFlow�е���¼�����Ӧ
	 * @param v
	 */
	public void onImageClick(View v){
		
		ViewFlow vf = ((MainContentFragment)mContentFrag).getViewFlow();
		int currScr = vf.getCurrentScreen();
		Log.d("Image", "Clicked! currScreen = " + currScr);
		showInfoDialog(this, "Title", "" + currScr);
	}

	/**
	 * ��������banner�����ĶԻ���
	 * @param context ������
	 * @param content �ı�����
	 */
	public void showInfoDialog(Context context, String title,  String content){
		LayoutInflater inflater = LayoutInflater.from(this);
		View dialogView = inflater.inflate(R.layout.main_frame_alert_dialog, null);
		TextView dialogTextView = 
				(TextView)dialogView.findViewById(R.id.main_frame_alert_dialog_textview);
		dialogTextView.setText(content);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(true);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle(title);
		builder.setView(dialogView);
		
		builder.setPositiveButton("�õ�", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//��ʱûɶ��
			}
		});
		
		builder.show();
	}
}
