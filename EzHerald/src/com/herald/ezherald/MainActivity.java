package com.herald.ezherald;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.taptwo.android.widget.ViewFlow;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.mainframe.MainContentFragment;
import com.herald.ezherald.mainframe.MainFrameDbAdapter;
import com.herald.ezherald.mainframe.MainGuideActivity;
import com.herald.ezherald.settingframe.AppUpdateActivity;

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
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
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

	private static final String KEY_SHOWED_UPDATE = "showedUpdate"; //此次运行已经显示过更新了
	Fragment mContentFrag;
	Menu mActionMenu;
	Handler mMoveHandler;
	SlidingMenu mSlidingMenu;

	private final String PREF_NAME = "com.herald.ezherald_preferences";
	private final String KEY_NAME_FIRST_START = "first_start";
	private final String KEY_NAME_LAST_REFRESH = "main_last_refresh_timestamp";
	private final String KEY_NAME_REFRESH_FREQ = "sync_frequency";
	private final int MAX_BANNER_SIZE = 5;
	private final boolean DEBUG_ALWAYS_SHOW_GUIDE = false; // 始终显示引导界面
	private final boolean DEBUG_ALWAYS_UPDATE_ONLINE = false; // 始终从网站更新数据，不论新旧

	private final String REMOTE_UPDATE_CHECK_URL = "http://121.248.63.105/EzHerald/picupdatetime/";
	private final String REMOTE_UPDATE_QUERY_URL = "http://121.248.63.105/EzHerald/picturejson/";
	
	private boolean mShowedUpdate = false;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mContentFrag = new MainContentFragment();
		super.SetBaseFrameActivity(mContentFrag);
		super.onCreate(savedInstanceState);

		mSlidingMenu = super.menu;

		boolean isOldUser = checkGuideState();
		Log.d("MainActivity", "GuideViewed ?:" + isOldUser);
		if ((!isOldUser) || DEBUG_ALWAYS_SHOW_GUIDE) {
			Intent i = new Intent();
			i.setClass(this, MainGuideActivity.class);
			startActivity(i);
			setGuideViewed();
		} 
		
		Intent intent = getIntent();
		mShowedUpdate = intent.getBooleanExtra(KEY_SHOWED_UPDATE, false);
		
		//检查是否有固件版本更新
		if(!mShowedUpdate){
			intent = new Intent();
			intent.setClass(this, AppUpdateActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * 通过检查SharedPreferences判断是否需要在线更新
	 * 
	 * @return
	 */
	private boolean checkRefreshState() {
		SharedPreferences appPreferences = getSharedPreferences(PREF_NAME,
				MODE_PRIVATE);
		long timestamp = appPreferences.getLong(KEY_NAME_LAST_REFRESH, 0);

		long timeInMinute = timestamp / 60000; // 1分钟 = 60 000毫秒
		long currentTimeInMinute = System.currentTimeMillis() / 60000;
		long timeGap = currentTimeInMinute - timeInMinute; // 时间差
		Log.d("MainActivity", "Time interval = " + timeGap + " minutes");

		String strPrefTimeInterval = appPreferences.getString(
				KEY_NAME_REFRESH_FREQ, null);
		int prefTimeInterval = -1;
		if (strPrefTimeInterval != null) {
			prefTimeInterval = Integer.parseInt(strPrefTimeInterval);
		}
		Log.d("MainActivity", "Pref. time interval = " + prefTimeInterval
				+ " minutes");

		if (timeGap > prefTimeInterval) {
			Log.d("MainActivity", "checkRefreshState() = true");
			return true;
		}

		return false;
	}

	/**
	 * 从SharedPreference中读取是否已经运行过程序 pref: true:已经运行过 false:没有运行过（需要运行一次Guide）
	 * 
	 * @return
	 */
	private boolean checkGuideState() {

		// @ref Pg251 <Android4 编程入门经典>
		SharedPreferences appPreferences = getSharedPreferences(PREF_NAME,
				MODE_PRIVATE);
		return appPreferences.getBoolean(KEY_NAME_FIRST_START, false);
	}

	/**
	 * 设置Guide已经阅读过
	 */
	private void setGuideViewed() {
		SharedPreferences appPreferences = getSharedPreferences(PREF_NAME,
				MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = appPreferences.edit();
		prefEditor.putBoolean(KEY_NAME_FIRST_START, true);
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

		//检查是否需要在线更新
		boolean needOnlineRefresh = checkRefreshState();
		
		if (needOnlineRefresh) {

			MenuItem item = mActionMenu.findItem(R.id.main_content_refresh);
			requestInfoUpdate("blabla", item);
			
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * 上侧Title位置的按钮点击相应
		 */
		switch (item.getItemId()) {
		case R.id.main_content_refresh:
			MainContentFragment mainFrag = (MainContentFragment) mContentFrag;
			mainFrag.refreshInfo(); // 各模块的内容(GridView中)同步更新就行，向各个模块索取
			requestInfoUpdate("blabla", item);
			return true;
		}

		return super.onOptionsItemSelected(item); // 这就是奇怪bug的来由，消息被处理了两次了!
	}

	public void requestInfoUpdate(String url, MenuItem item) {
		item.setVisible(false);
		MenuItem doingItem = mActionMenu
				.findItem(R.id.mainframe_menu_item_doing);
		doingItem.setVisible(true);
		new UpdateBannerImageTask().execute(url);
	}

	/**
	 * 从某网站下载一个图
	 * 
	 * @return
	 */
	private Bitmap testGetBitmap(String URL) {
		Bitmap bitmap = null;
		InputStream in = null;
		try {
			in = OpenHttpConnection(URL);
			if (in == null)
				throw new IOException("Instream is null");
			bitmap = BitmapFactory.decodeStream(in);
			in.close();
		} catch (IOException e1) {
			Log.d("MainActivity:test", e1.getLocalizedMessage());
		}
		return bitmap;
	}

	/**
	 * 开Http连接
	 * 
	 * @param uRL
	 * @return
	 * @throws IOException
	 */
	private InputStream OpenHttpConnection(String urlStr) throws IOException {
		InputStream in = null;
		int response = -1;

		URL url = new URL(urlStr);
		URLConnection conn = url.openConnection();

		if (!(conn instanceof HttpURLConnection)) {
			throw new IOException("Not an HTTP connection");
		}
		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
			} else
				throw new IOException("Newwork error.");
		} catch (Exception ex) {
			Log.d("Notwoking", ex.getLocalizedMessage());
			throw new IOException("Error connecting");
		}
		return in;
	}

	/**
	 * 工作线程中用Toast
	 * 
	 * @param str
	 *            提示的文字
	 */
	private void showToastInWorkingThread(final String str) {
		new Thread() {
			@Override
			public void run() {
				Looper.prepare(); // 这是关键
				Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT)
						.show();
				Looper.loop();
			}
		}.start();
	}

	/**
	 * 联网更新图片
	 */
	private class UpdateBannerImageTask extends
			AsyncTask<String, Void, ArrayList<Bitmap>> {

		@Override
		protected ArrayList<Bitmap> doInBackground(String... url) {
			// TODO Auto-generated method stub
			/*
			 * try { Thread.sleep(5000); } catch (InterruptedException e) { //
			 * TODO Auto-generated catch block e.printStackTrace(); }
			 */
			ArrayList<Bitmap> retList = new ArrayList<Bitmap>();
			// ////////////从数据库载入信息（如果有的话）
			MainFrameDbAdapter dbAdapter = new MainFrameDbAdapter(
					getBaseContext());
			dbAdapter.open();
			Cursor cs = dbAdapter.getAllImages();
			if (cs != null && cs.moveToFirst()) {
				int count = 0;
				do {
					byte[] inBytes = cs.getBlob(1); // 图片信息是blob信息
					retList.add(BitmapFactory.decodeByteArray(inBytes, 0,
							inBytes.length));
					count++;
				} while (count < MAX_BANNER_SIZE && cs.moveToNext());
			} else {
				Log.w("MainActivity", "db record does not exist");
			}

			// ///////////////////////////////////////
			ArrayList<Bitmap> updList = new ArrayList<Bitmap>();
			boolean haveUpdate = checkBannerImageUpdateState(); // TODO:从服务器先GET是否有update，然后决定是否下载
			Log.d("MainActivity: AsyncTask", "haveRemoveUpdate?" + haveUpdate);
			// ////////////////////////////////////////////////////////////////////////////
			ArrayList<String> remoteImgUrls = null;
			if(haveUpdate){
				remoteImgUrls = getRemoveUpdateImgUrls(REMOTE_UPDATE_QUERY_URL); // 远程更新的图片url放在这边
			}

			if (remoteImgUrls!= null && !remoteImgUrls.isEmpty()) {
				//有东西需要更新了..
				for (String urlStr : remoteImgUrls) {
					Bitmap bmp = testGetBitmap(urlStr);
					if (bmp != null) {
						updList.add(bmp);
					} else {
						showToastInWorkingThread("网络不大给力的样子呐...");
					}
				}
				

				// 更新数据库
				while (retList.size() < MAX_BANNER_SIZE && updList.size() > 0) {
					Bitmap tmpBmp = updList.get(updList.size() - 1);
					retList.add(tmpBmp);
					dbAdapter.insertImage(retList.size() - 1, tmpBmp);
					updList.remove(updList.size() - 1);
				}
				int cnt = 0;
				while (updList.size() > 0 && cnt < MAX_BANNER_SIZE) {
					// 需要替换了！
					retList.remove(cnt);
					Bitmap tmpBmp = updList.get(updList.size() - 1);
					updList.remove(updList.size() - 1);
					retList.add(tmpBmp);
					dbAdapter.updateImage(cnt++, tmpBmp);
				}
			}
			dbAdapter.close();
			// ////////////////////////////////////////////////////////////////////////////

			return retList;
		}

		@Override
		protected void onPostExecute(ArrayList<Bitmap> result) {
			// 修改相应的视图
			for (int i = 0; i < result.size(); i++) {
				((MainContentFragment) mContentFrag).updateImageItem(i,
						result.get(i));
			}

			((MainContentFragment) mContentFrag).refreshViewFlowImage();

			// 改回ActionBar图标
			MenuItem item = mActionMenu.findItem(R.id.main_content_refresh);
			item.setVisible(true);
			MenuItem doingItem = mActionMenu
					.findItem(R.id.mainframe_menu_item_doing);
			doingItem.setVisible(false);

			// 更新SharedPreference里面最后更新的时间
			setLastRefreshTime(System.currentTimeMillis());

			super.onPostExecute(result);
		}

	}

	/**
	 * ViewFlow中点击事件的响应
	 * 
	 * @param v
	 */
	public void onImageClick(View v) {

		ViewFlow vf = ((MainContentFragment) mContentFrag).getViewFlow();
		int currScr = vf.getCurrentScreen();
		Log.d("Image", "Clicked! currScreen = " + currScr);
		showInfoDialog(this, "Title", "" + currScr);
	}

	/**
	 * 获得获得远程的最新图片列表
	 * 
	 * @return 图片的URLs
	 */
	public ArrayList<String> getRemoveUpdateImgUrls(String srcURL) {
		// reference: <Android4编程入门经典>书上P400
		StringBuilder stringBuilder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(srcURL);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}
			} else {
				Log.e("MainActivity:getRemoveUpdateImgUrls",
						"Http response status error");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String jsonStr = stringBuilder.toString(); // JSON元数据
		// Log.d("MainActivity:getRemoveUpdateImgUrls", "JSON src data = " +
		// jsonStr);
		ArrayList<String> retStr = new ArrayList<String>();
		
		try {
			JSONArray jsonArray = new JSONArray(jsonStr);
			Log.i("MainActivity:getRemoveUpdateImgUrls",
					"# of surveys in feed: " + jsonArray.length());
			SharedPreferences appPreferences = getSharedPreferences(PREF_NAME,
					MODE_PRIVATE);
			long timestampHere = appPreferences.getLong(KEY_NAME_LAST_REFRESH,
					0); // 本地最后更新的时间
			// 处理json数据
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				/*Log.d("MainActivity:getRemoveUpdateImgUrls", "jsonStr @" + i
						+ " : " + jsonObject.toString());*/
				String remoteTimeStampStr = jsonObject.getString("updatetime");
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				long remoteTimeStamp = format.parse(remoteTimeStampStr)
						.getTime();
				if (remoteTimeStamp > timestampHere || DEBUG_ALWAYS_UPDATE_ONLINE) {
					// 需要更新，加入列表
					Log.d("MainActivity:getRemoveUpdateImgUrls", "need update : " + jsonObject.getString("url"));
					retStr.add(jsonObject.getString("url"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return retStr;
	}

	/**
	 * 将网上图片最新的更新时间与本地的最后更新时间比对，以判断是否需要联网更新图片
	 * 
	 * @return
	 */

	public boolean checkBannerImageUpdateState() {

		int BUFFER_SIZE = 200;
		InputStream in = null;
		try {
			in = OpenHttpConnection(REMOTE_UPDATE_CHECK_URL);
		} catch (IOException e) {
			Log.w("MainActivity", e.getLocalizedMessage());
			return false;
		}
		InputStreamReader isr = new InputStreamReader(in);
		int charRead;
		String str = "";
		char[] inputBuffer = new char[BUFFER_SIZE];
		try {
			while ((charRead = isr.read(inputBuffer)) > 0) {
				// --Convert chars to a string
				String readStr = String.copyValueOf(inputBuffer, 0, charRead);
				str += readStr;
				inputBuffer = new char[BUFFER_SIZE];
			}
			in.close();
		} catch (IOException e) {
			Log.w("MainActivity", e.getLocalizedMessage());
			return false;
		}

		// 比对时间
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = format.parse(str);
			long updTimeInServer = date.getTime(); // 服务器时间（分钟）
			SharedPreferences appPreferences = getSharedPreferences(PREF_NAME,
					MODE_PRIVATE);
			long timestampHere = appPreferences.getLong(KEY_NAME_LAST_REFRESH,
					0); // 本地最后更新的时间
			Log.d("MainActivity", "Remote : " + updTimeInServer + ", local : "
					+ timestampHere);
			if (updTimeInServer > timestampHere) {
				return true;
			}
		} catch (ParseException e) {
			Log.e("MainActivity", "Unable to parse string " + str);
			e.printStackTrace();
		}
		
		if(DEBUG_ALWAYS_UPDATE_ONLINE)
			return true;

		return false;
	}

	/**
	 * 更新SharedPreference里面首页内容最后更新的时间
	 * 
	 * @param currentTimeMillis
	 */
	public void setLastRefreshTime(long currentTimeMillis) {
		SharedPreferences appPrefs = getSharedPreferences(PREF_NAME,
				MODE_PRIVATE);
		SharedPreferences.Editor editor = appPrefs.edit();
		editor.putLong(KEY_NAME_LAST_REFRESH, currentTimeMillis);
		editor.commit();
		Log.d("MainActivity", "SetLastRefreshTime = " + currentTimeMillis);
	}

	/**
	 * 主界面点击banner弹出的对话框
	 * 
	 * @param context
	 *            上下文
	 * @param content
	 *            文本内容
	 */
	public void showInfoDialog(Context context, String title, String content) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View dialogView = inflater.inflate(R.layout.main_frame_alert_dialog,
				null);
		TextView dialogTextView = (TextView) dialogView
				.findViewById(R.id.main_frame_alert_dialog_textview);
		dialogTextView.setText(content);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(true);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle(title);
		builder.setView(dialogView);

		builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 暂时没啥用
			}
		});

		builder.show();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
