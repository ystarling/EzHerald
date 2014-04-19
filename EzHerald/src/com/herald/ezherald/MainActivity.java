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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.app.ProgressDialog;
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
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.util.Pair;
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

	private static final String KEY_SHOWED_UPDATE = "showedUpdate"; // 此次运行已经显示过更新了
	Fragment mContentFrag;
	Menu mActionMenu;
	Handler mMoveHandler;
	SlidingMenu mSlidingMenu;

	public boolean needRefreshContent = false; // 是否需要刷新Content
	public boolean isReceivingData = false; // 当前是否已经在更新Image
	private boolean doNotUpdateUI = false;// 不更新UI

	private final static String PREF_NAME = "com.herald.ezherald_preferences";
	private final String KEY_NAME_FIRST_START = "first_start";
	private final static String KEY_NAME_LAST_REFRESH = "main_last_refresh_timestamp";
	private final String KEY_NAME_REFRESH_FREQ = "sync_frequency";
	private final int MAX_BANNER_SIZE = 5;

	private final boolean DEBUG_ALWAYS_SHOW_GUIDE = false; // 始终显示引导界面
	private final boolean DEBUG_ALWAYS_UPDATE_ONLINE = false; // 始终从网站更新数据，不论新旧
	private final boolean DEBUG_DONOT_REFRESH = false; // 禁止更新主界面轮播图

	private final String REMOTE_UPDATE_CHECK_URL = "http://121.248.63.105/EzHerald/picupdatetime/";
	private final String REMOTE_UPDATE_QUERY_URL = "http://121.248.63.105/EzHerald/picturejson/";
	private final int CONN_TIMEOUT = 5000;

	private boolean mShowedUpdate = false;

	private UpdateBannerImageTask mUpdateBannerImageTask = null;
	private boolean mIsUpdateBannerTaskDone = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		mShowedUpdate = intent.getBooleanExtra(KEY_SHOWED_UPDATE, false);
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

		doNotUpdateUI = false;
	}

	/**
	 * 通过检查SharedPreferences判断是否需要在线更新
	 * 
	 * @return
	 */
	private boolean checkRefreshState() {
		if (DEBUG_DONOT_REFRESH) {
			return false;
		}

		SharedPreferences appPreferences = getSharedPreferences(PREF_NAME,
				MODE_PRIVATE);
		long timestamp = appPreferences.getLong(KEY_NAME_LAST_REFRESH, 0);

		float timeInMinute = timestamp / 60000.0f; // 1分钟 = 60 000毫秒
		float currentTimeInMinute = System.currentTimeMillis() / 60000.0f;
		float timeGap = currentTimeInMinute - timeInMinute; // 时间差
		Log.d("MainActivity", "Time interval = " + timeGap + " minutes");

		String strPrefTimeInterval = appPreferences.getString(
				KEY_NAME_REFRESH_FREQ, null);
		int prefTimeInterval = (timestamp == 0) ? 0 : 180;
		if (strPrefTimeInterval != null) {
			prefTimeInterval = Integer.parseInt(strPrefTimeInterval);
		}
		Log.d("MainActivity", "Pref. time interval = " + prefTimeInterval
				+ " minutes");

		if (prefTimeInterval >= 0 && timeGap > prefTimeInterval) {
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

		// 检查是否需要在线更新
		boolean needOnlineRefresh = checkRefreshState();

		if (needOnlineRefresh) {

			MenuItem item = mActionMenu.findItem(R.id.main_content_refresh);
			requestInfoUpdate("blabla", item);

		}

		// 检查应用程序更新
		new CheckAppRefreshStateTask().execute(this);

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
		if (isReceivingData || mUpdateBannerImageTask != null)
			return; // Already receiving data

		item.setVisible(false);
		MenuItem doingItem = mActionMenu
				.findItem(R.id.mainframe_menu_item_doing);
		doingItem.setVisible(true);
		mUpdateBannerImageTask = new UpdateBannerImageTask();
		mUpdateBannerImageTask.execute(url);
	}

	/**
	 * 从某网站下载一个图
	 * 
	 * @return
	 */
	private Bitmap tryGetBitmap(String URL) {
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
		} catch (IllegalStateException e) {
			e.printStackTrace();
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
		conn.setConnectTimeout(CONN_TIMEOUT);
		conn.setUseCaches(true);

		if (!(conn instanceof HttpURLConnection)) {
			throw new IOException("Not an HTTP connection");
		}
		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.setConnectTimeout(CONN_TIMEOUT);
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
	 * 联网更新图片 同时更新数据库内容
	 */
	private class UpdateBannerImageTask extends
			AsyncTask<String, Void, Void> {
		private boolean connFail = false;
		private long lastSuccTimeStamp = -1;

		@Override
		protected Void doInBackground(String... url) {
			isReceivingData = true;

			// ///////////////////////////////////////
			//ArrayList<Bitmap> updList = new ArrayList<Bitmap>(); // 图片更新的列表
			boolean haveUpdate = checkBannerImageUpdateState(); // 从服务器先GET是否有update，然后决定是否下载

			// ////////////////////////////////////////////////////////////////////////////
			List<Pair<String, Long>> remoteImgUrls = null;
			if (haveUpdate) {
				remoteImgUrls = getRemoveUpdateImgUrls(REMOTE_UPDATE_QUERY_URL); // 远程更新的图片url放在这边
			}

			if (remoteImgUrls != null && !remoteImgUrls.isEmpty()) {
				//dbAdapter.open();
				// 有东西需要更新了..

				int count = 1;
				int size = remoteImgUrls.size();
				for (Pair<String, Long> pair : remoteImgUrls) {
					String urlStr = pair.first;
					Long urlTimeStamp = pair.second;
					showToastInWorkingThread("正在下载图片..." + count++ + "/" + size);
					Bitmap bmp = tryGetBitmap(urlStr);
					if (bmp != null) {
						//updList.add(bmp);
						//20131105 直接进行数据库操作，一张一张更新图片。数据库操作结束后，统一从数据库拿图片
						boolean stateSucc = insertBitmapToDb(bmp);
						if (urlTimeStamp > lastSuccTimeStamp && stateSucc) {
							lastSuccTimeStamp = urlTimeStamp; // 更新成功下载的时间戳
						}
					} else {
						showToastInWorkingThread("网络不大给力的样子呐...");
						connFail = true;
						break;
					}
					if(!bmp.isRecycled()){
						bmp.recycle();
						System.gc();
					}
				}

				// 更新数据库
//				int currImgSize = updList.size(); // 当前从网上更新到的新图片数量
//				int dbImgSize = dbAdapter.getCurrentImageCount(); // 数据库中的老图片数量
//				int removeSize = currImgSize + dbImgSize - MAX_BANNER_SIZE; // 需要删除的图片数量
//
//				if (removeSize > 0) {
//					// 需要删掉一些原图片然后更新
//					for (int i = dbImgSize - removeSize; i < dbImgSize; i++) {
//						dbAdapter.deleteImage(i);
//					}
//				}
//				// 增加原来的标号
//				int currIdOld = dbAdapter.getCurrentImageCount() - 1; // 当前最底图片的标号(如果为空会变成-1)
//				int currIdNew = currIdOld + currImgSize; // 挪动完毕后最底图片的标号(0 -- 4)
//				while (currIdOld >= 0) {
//					// 挪动
//					Cursor cs = dbAdapter.getImage(currIdOld);
//					if (cs != null && cs.moveToFirst()) {
//						byte[] inBytes = cs.getBlob(1);
//						updList.add(currImgSize, BitmapFactory.decodeByteArray(
//								inBytes, 0, inBytes.length));
//
//					}
//					dbAdapter.alterImageId(currIdOld, currIdNew);
//					currIdOld--;
//					currIdNew--;
//				}
//
//				// 增加新图到数据库
//				for (int id = 0; id < currImgSize; id++) {
//					dbAdapter.insertImage(id, updList.get(id));
//				}
//
//				dbAdapter.close();
			}

			// ////////////////////////////////////////////////////////////////////////////
			return null;
		}
		
		/**
		 * 塞入一张图到数据库
		 * @param bmp
		 * @return 操作结果成功否
		 */
		private boolean insertBitmapToDb(Bitmap bmp) {
			MainFrameDbAdapter dbAdapter = new MainFrameDbAdapter(getBaseContext());
			dbAdapter.open();
			int currImgSize = 1; // 当前从网上更新到的新图片数量
			int dbImgSize = dbAdapter.getCurrentImageCount(); // 数据库中的老图片数量
			int removeSize = currImgSize + dbImgSize - MAX_BANNER_SIZE; // 需要删除的图片数量

			if (removeSize > 0) {
				// 需要删掉一些原图片然后更新
				for (int i = dbImgSize - removeSize; i < dbImgSize; i++) {
					dbAdapter.deleteImage(i);
				}
			}
			// 增加原来的标号
			int currIdOld = dbAdapter.getCurrentImageCount() - 1; // 当前最底图片的标号(如果为空会变成-1)
			int currIdNew = currIdOld + currImgSize; // 挪动完毕后最底图片的标号(0 -- 4)
			while (currIdOld >= 0) {
				// 挪动
				dbAdapter.alterImageId(currIdOld, currIdNew);
				currIdOld--;
				currIdNew--;
			}

			// 增加新图到数据库
			long result = dbAdapter.insertImage(0, bmp);

			dbAdapter.close();
			
			if(result != -1){
				return true;
			}
			return false;
		}

		/**
		 * 更新SharedPreference里面首页内容最后更新的时间
		 * 
		 * @param currentTimeMillis
		 */
		public void setLastRefreshTime(long currentTimeMillis) {
			if (currentTimeMillis == -1) {
				return;
			}
			SharedPreferences appPrefs = getSharedPreferences(PREF_NAME,
					MODE_PRIVATE);
			SharedPreferences.Editor editor = appPrefs.edit();
			editor.putLong(KEY_NAME_LAST_REFRESH, currentTimeMillis);
			editor.commit();
			Log.d("MainActivity", "SetLastRefreshTime = " + currentTimeMillis);
		}
		

		@Override
		protected void onPostExecute(Void result) {
			// 数据库更新完毕之后修改View

			setLastRefreshTime(lastSuccTimeStamp);

			if (doNotUpdateUI) {
				Log.w("MainActivity", "Do not update UI...");

				isReceivingData = false;
				mUpdateBannerImageTask = null;
				mIsUpdateBannerTaskDone = true;
				return;
			}

			// 修改相应的视图
//			for (int i = 0; i < result.size(); i++) {
//				((MainContentFragment) mContentFrag).updateImageItem(i,
//						result.get(i));
//			}
//
//			((MainContentFragment) mContentFrag).refreshViewFlowImage();
			((MainContentFragment)mContentFrag).refreshImageFromDb();
			((MainContentFragment) mContentFrag).refreshViewFlowImage();

			// 改回ActionBar图标
			MenuItem item = mActionMenu.findItem(R.id.main_content_refresh);
			item.setVisible(true);
			MenuItem doingItem = mActionMenu
					.findItem(R.id.mainframe_menu_item_doing);
			doingItem.setVisible(false);

			isReceivingData = false;
			mUpdateBannerImageTask = null;
			mIsUpdateBannerTaskDone = true;
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			setLastRefreshTime(lastSuccTimeStamp);
			
			super.onCancelled();
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
	 * @return List<Map<图片地址，图片更新时间戳>>
	 */

	public List<Pair<String, Long>> getRemoveUpdateImgUrls(String srcURL) {
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
		List<Pair<String, Long>> retList = new ArrayList<Pair<String, Long>>();

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
				/*
				 * Log.d("MainActivity:getRemoveUpdateImgUrls", "jsonStr @" + i
				 * + " : " + jsonObject.toString());
				 */
				String remoteTimeStampStr = jsonObject.getString("updatetime");
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				long remoteTimeStamp = format.parse(remoteTimeStampStr)
						.getTime();
				if (remoteTimeStamp > timestampHere
						|| DEBUG_ALWAYS_UPDATE_ONLINE) {
					// 需要更新，加入列表
					Log.d("MainActivity:getRemoveUpdateImgUrls",
							"need update : " + jsonObject.getString("url"));
					Pair<String, Long> pair = new Pair<String, Long>(
							jsonObject.getString("url"), remoteTimeStamp);
					retList.add(0, pair);  //倒序插入
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retList;
	}

	/**
	 * 将网上图片最新的更新时间与本地的最后更新时间比对，以判断是否需要联网更新图片
	 * 
	 * @return
	 */

	public boolean checkBannerImageUpdateState() {

		int BUFFER_SIZE = 1024;
		InputStream in = null;
		try {
			in = OpenHttpConnection(REMOTE_UPDATE_CHECK_URL);
		} catch (IOException e) {
			//Log.w("MainActivity", e.getLocalizedMessage());
			showToastInWorkingThread("远程服务器链接超时，网络不大给力？");
			return false;
		}
		if (in == null)
			return false;

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

		if (DEBUG_ALWAYS_UPDATE_ONLINE)
			return true;

		return false;
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

		// builder.show();
	}

	/**
	 * 销毁时如果还有没有搞完的异步线程，设置flag让线程取消更新UI的操作！
	 */
	@Override
	protected void onDestroy() {
		doNotUpdateUI = true;
		Log.d("MainActivity", "onDestroy");
		if(mUpdateBannerImageTask != null){
			mUpdateBannerImageTask.cancel(true);
		}
		super.onDestroy();
	}
	
	

	@Override
	protected void onPause() {
		doNotUpdateUI = true;
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		doNotUpdateUI = false;
		if (mUpdateBannerImageTask == null && mIsUpdateBannerTaskDone) {
			// 如果更新完成，修改"正在刷新"什么的
			// 改回ActionBar图标
			MenuItem item = mActionMenu.findItem(R.id.main_content_refresh);
			item.setVisible(true);
			MenuItem doingItem = mActionMenu
					.findItem(R.id.mainframe_menu_item_doing);
			doingItem.setVisible(false);
			//从数据库重新读取图片
			if(mContentFrag != null && mContentFrag instanceof MainContentFragment){
				((MainContentFragment)mContentFrag).refreshImageFromDb();
			}
			mIsUpdateBannerTaskDone = false;
		}
	}

	/**
	 * 异步获取更新状态，防止启动时间缓慢
	 * 
	 * @author BorisHe
	 * 
	 */
	public class CheckAppRefreshStateTask extends
			AsyncTask<Context, Void, Void> {

		@Override
		protected Void doInBackground(Context... context) {
			if (context == null || context.length == 0) {
				Log.e("CheckAppRefreshStateTask", "Null context");
				return null;
			}

			// 检查是否有固件版本更新
			if (!mShowedUpdate) {
				Intent intent = new Intent();
				intent.setClass(context[0], AppUpdateActivity.class);
				startActivity(intent);
			}
			return null;
		}

	}

}
