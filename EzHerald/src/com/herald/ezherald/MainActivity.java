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
 * @author �β�ΰ
 * @since 2013.05.14
 * @updated 2013.7.1
 * �������Activity
 * 
 * 
 */
public class MainActivity extends BaseFrameActivity {

	private static final String KEY_SHOWED_UPDATE = "showedUpdate"; //�˴������Ѿ���ʾ��������
	Fragment mContentFrag;
	Menu mActionMenu;
	Handler mMoveHandler;
	SlidingMenu mSlidingMenu;

	private final String PREF_NAME = "com.herald.ezherald_preferences";
	private final String KEY_NAME_FIRST_START = "first_start";
	private final String KEY_NAME_LAST_REFRESH = "main_last_refresh_timestamp";
	private final String KEY_NAME_REFRESH_FREQ = "sync_frequency";
	private final int MAX_BANNER_SIZE = 5;
	private final boolean DEBUG_ALWAYS_SHOW_GUIDE = false; // ʼ����ʾ��������
	private final boolean DEBUG_ALWAYS_UPDATE_ONLINE = false; // ʼ�մ���վ�������ݣ������¾�

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
		
		//����Ƿ��й̼��汾����
		if(!mShowedUpdate){
			intent = new Intent();
			intent.setClass(this, AppUpdateActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * ͨ�����SharedPreferences�ж��Ƿ���Ҫ���߸���
	 * 
	 * @return
	 */
	private boolean checkRefreshState() {
		SharedPreferences appPreferences = getSharedPreferences(PREF_NAME,
				MODE_PRIVATE);
		long timestamp = appPreferences.getLong(KEY_NAME_LAST_REFRESH, 0);

		long timeInMinute = timestamp / 60000; // 1���� = 60 000����
		long currentTimeInMinute = System.currentTimeMillis() / 60000;
		long timeGap = currentTimeInMinute - timeInMinute; // ʱ���
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
	 * ��SharedPreference�ж�ȡ�Ƿ��Ѿ����й����� pref: true:�Ѿ����й� false:û�����й�����Ҫ����һ��Guide��
	 * 
	 * @return
	 */
	private boolean checkGuideState() {

		// @ref Pg251 <Android4 ������ž���>
		SharedPreferences appPreferences = getSharedPreferences(PREF_NAME,
				MODE_PRIVATE);
		return appPreferences.getBoolean(KEY_NAME_FIRST_START, false);
	}

	/**
	 * ����Guide�Ѿ��Ķ���
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

		//����Ƿ���Ҫ���߸���
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
		 * �ϲ�Titleλ�õİ�ť�����Ӧ
		 */
		switch (item.getItemId()) {
		case R.id.main_content_refresh:
			MainContentFragment mainFrag = (MainContentFragment) mContentFrag;
			mainFrag.refreshInfo(); // ��ģ�������(GridView��)ͬ�����¾��У������ģ����ȡ
			requestInfoUpdate("blabla", item);
			return true;
		}

		return super.onOptionsItemSelected(item); // ��������bug�����ɣ���Ϣ��������������!
	}

	public void requestInfoUpdate(String url, MenuItem item) {
		item.setVisible(false);
		MenuItem doingItem = mActionMenu
				.findItem(R.id.mainframe_menu_item_doing);
		doingItem.setVisible(true);
		new UpdateBannerImageTask().execute(url);
	}

	/**
	 * ��ĳ��վ����һ��ͼ
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
	 * ��Http����
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
	 * �����߳�����Toast
	 * 
	 * @param str
	 *            ��ʾ������
	 */
	private void showToastInWorkingThread(final String str) {
		new Thread() {
			@Override
			public void run() {
				Looper.prepare(); // ���ǹؼ�
				Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT)
						.show();
				Looper.loop();
			}
		}.start();
	}

	/**
	 * ��������ͼƬ
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
			// ////////////�����ݿ�������Ϣ������еĻ���
			MainFrameDbAdapter dbAdapter = new MainFrameDbAdapter(
					getBaseContext());
			dbAdapter.open();
			Cursor cs = dbAdapter.getAllImages();
			if (cs != null && cs.moveToFirst()) {
				int count = 0;
				do {
					byte[] inBytes = cs.getBlob(1); // ͼƬ��Ϣ��blob��Ϣ
					retList.add(BitmapFactory.decodeByteArray(inBytes, 0,
							inBytes.length));
					count++;
				} while (count < MAX_BANNER_SIZE && cs.moveToNext());
			} else {
				Log.w("MainActivity", "db record does not exist");
			}

			// ///////////////////////////////////////
			ArrayList<Bitmap> updList = new ArrayList<Bitmap>();
			boolean haveUpdate = checkBannerImageUpdateState(); // TODO:�ӷ�������GET�Ƿ���update��Ȼ������Ƿ�����
			Log.d("MainActivity: AsyncTask", "haveRemoveUpdate?" + haveUpdate);
			// ////////////////////////////////////////////////////////////////////////////
			ArrayList<String> remoteImgUrls = null;
			if(haveUpdate){
				remoteImgUrls = getRemoveUpdateImgUrls(REMOTE_UPDATE_QUERY_URL); // Զ�̸��µ�ͼƬurl�������
			}

			if (remoteImgUrls!= null && !remoteImgUrls.isEmpty()) {
				//�ж�����Ҫ������..
				for (String urlStr : remoteImgUrls) {
					Bitmap bmp = testGetBitmap(urlStr);
					if (bmp != null) {
						updList.add(bmp);
					} else {
						showToastInWorkingThread("���粻�������������...");
					}
				}
				

				// �������ݿ�
				while (retList.size() < MAX_BANNER_SIZE && updList.size() > 0) {
					Bitmap tmpBmp = updList.get(updList.size() - 1);
					retList.add(tmpBmp);
					dbAdapter.insertImage(retList.size() - 1, tmpBmp);
					updList.remove(updList.size() - 1);
				}
				int cnt = 0;
				while (updList.size() > 0 && cnt < MAX_BANNER_SIZE) {
					// ��Ҫ�滻�ˣ�
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
			// �޸���Ӧ����ͼ
			for (int i = 0; i < result.size(); i++) {
				((MainContentFragment) mContentFrag).updateImageItem(i,
						result.get(i));
			}

			((MainContentFragment) mContentFrag).refreshViewFlowImage();

			// �Ļ�ActionBarͼ��
			MenuItem item = mActionMenu.findItem(R.id.main_content_refresh);
			item.setVisible(true);
			MenuItem doingItem = mActionMenu
					.findItem(R.id.mainframe_menu_item_doing);
			doingItem.setVisible(false);

			// ����SharedPreference���������µ�ʱ��
			setLastRefreshTime(System.currentTimeMillis());

			super.onPostExecute(result);
		}

	}

	/**
	 * ViewFlow�е���¼�����Ӧ
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
	 * ��û��Զ�̵�����ͼƬ�б�
	 * 
	 * @return ͼƬ��URLs
	 */
	public ArrayList<String> getRemoveUpdateImgUrls(String srcURL) {
		// reference: <Android4������ž���>����P400
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
		String jsonStr = stringBuilder.toString(); // JSONԪ����
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
					0); // ���������µ�ʱ��
			// ����json����
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
					// ��Ҫ���£������б�
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
	 * ������ͼƬ���µĸ���ʱ���뱾�ص�������ʱ��ȶԣ����ж��Ƿ���Ҫ��������ͼƬ
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

		// �ȶ�ʱ��
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = format.parse(str);
			long updTimeInServer = date.getTime(); // ������ʱ�䣨���ӣ�
			SharedPreferences appPreferences = getSharedPreferences(PREF_NAME,
					MODE_PRIVATE);
			long timestampHere = appPreferences.getLong(KEY_NAME_LAST_REFRESH,
					0); // ���������µ�ʱ��
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
	 * ����SharedPreference������ҳ���������µ�ʱ��
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
	 * ��������banner�����ĶԻ���
	 * 
	 * @param context
	 *            ������
	 * @param content
	 *            �ı�����
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

		builder.setPositiveButton("�õ�", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ��ʱûɶ��
			}
		});

		builder.show();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
