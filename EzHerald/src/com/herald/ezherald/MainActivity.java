package com.herald.ezherald;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.taptwo.android.widget.ViewFlow;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.mainframe.MainContentFragment;
import com.herald.ezherald.mainframe.MainFrameDbAdapter;
import com.herald.ezherald.mainframe.MainGuideActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	private final int MAX_BANNER_SIZE = 5;
	private final boolean DEBUG_ALWAYS_SHOW_GUIDE = false;	//ʼ����ʾ��������
	private final boolean DEBUG_ALWAYS_UPDATE_ONLINE = false; 		//ʼ�մ���վ�������ݣ������¾�

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
	 * �����ã���ĳ��վ����һ��ͼ
	 * @return
	 */
	@Deprecated
	private Bitmap testGetBitmap(String URL){
		Bitmap bitmap = null;
		InputStream in = null;
		try{
			in = OpenHttpConnection(URL);
			bitmap = BitmapFactory.decodeStream(in);
			in.close();
		} catch (IOException e1){
			Log.d("MainActivity:test", e1.getLocalizedMessage());
		}
		return bitmap;
	}
	
	
	
	
	/**
	 * ��Http����
	 * @param uRL
	 * @return
	 * @throws IOException
	 */
	private InputStream OpenHttpConnection(String urlStr) throws IOException{
		InputStream in = null;
		int response = -1;
		
		URL url = new URL(urlStr);
		URLConnection conn = url.openConnection();
		
		if(!(conn instanceof HttpURLConnection)){
			throw new IOException("Not an HTTP connection");
		}
		try{
			HttpURLConnection httpConn = (HttpURLConnection)conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			response = httpConn.getResponseCode();
			if(response == HttpURLConnection.HTTP_OK){
				in = httpConn.getInputStream();
			}
		} catch (Exception ex){
			Log.d("Notwoking", ex.getLocalizedMessage());
			throw new IOException("Error connecting");
		}
		return in;
	}
	

	/**
	 * ��������ͼƬ
	 */
	private class UpdateBannerImageTask extends AsyncTask<String, Void, ArrayList<Bitmap>>{

		@Override
		protected ArrayList<Bitmap> doInBackground(String... url) {
			// TODO Auto-generated method stub
			/*try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			*/
			ArrayList<Bitmap> retList = new ArrayList<Bitmap>();
			//////////////�����ݿ�������Ϣ������еĻ���
			MainFrameDbAdapter dbAdapter = new MainFrameDbAdapter(getBaseContext());
			dbAdapter.open();
			Cursor cs = dbAdapter.getAllImages();
			if(cs != null && cs.moveToFirst()){
				int count = 0;
				do{
					byte[] inBytes = cs.getBlob(1); //ͼƬ��Ϣ��blob��Ϣ
					retList.add(BitmapFactory.decodeByteArray(inBytes, 0, inBytes.length));
					count ++;
				}while(count < MAX_BANNER_SIZE && cs.moveToNext());		
			} else {
				Log.w("MainActivity", "db record does not exist");
			}
			
			/////////////////////////////////////////
			ArrayList<Bitmap> updList = new ArrayList<Bitmap>();
			boolean haveUpdate = false; //TODO:�ӷ�������GET�Ƿ���update��Ȼ������Ƿ�����

			
			//////////////////////////////////////////////////////////////////////////////
			if(haveUpdate || DEBUG_ALWAYS_UPDATE_ONLINE){
				Bitmap bmp = testGetBitmap("http://static.dayandcarrot.net/temp/pic0.png");
				updList.add(bmp);
				//�������ݿ�
				while(retList.size() < MAX_BANNER_SIZE && updList.size()>0){
					Bitmap tmpBmp = updList.get(updList.size()-1);
					retList.add(tmpBmp);
					dbAdapter.insertImage(retList.size()-1, tmpBmp);
					updList.remove(updList.size()-1);
				}
				int cnt = 0;
				while(updList.size()>0 && cnt < 5){
					//��Ҫ�滻�ˣ�
					retList.remove(cnt++);
					Bitmap tmpBmp = updList.get(updList.size()-1);
					retList.add(tmpBmp);
					dbAdapter.updateImage(cnt, tmpBmp);
				}
			}
			//////////////////////////////////////////////////////////////////////////////
			
			return retList;
		}

		@Override
		protected void onPostExecute(ArrayList<Bitmap> result) {
			// �޸���Ӧ����ͼ
			for(int i=0; i<result.size(); i++){
				((MainContentFragment)mContentFrag).updateImageItem(i, result.get(i));
			}
			
			((MainContentFragment)mContentFrag).refreshViewFlowImage();
			
			//�Ļ�ActionBarͼ��
			MenuItem item = mActionMenu.findItem(R.id.main_content_refresh);
			item.setVisible(true);
			MenuItem doingItem = mActionMenu.findItem(R.id.mainframe_menu_item_doing);
			doingItem.setVisible(false);
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
