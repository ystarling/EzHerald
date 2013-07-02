package com.herald.ezherald.account;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.herald.ezherald.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class IDCardAccountActivity extends Activity {
	private String userName;
	private String password;

	private EditText view_userName;
	private EditText view_password;
	private CheckBox view_rememberMe;
	private Button view_loginSubmit;

	private static final int MENU_EXIT = Menu.FIRST - 1;
	private static final int MENU_ABOUT = Menu.FIRST;

	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";

	private String SHARE_LOGIN_USERNAME = "MAP_LOGIN_USERNAME";
	private String SHARE_LOGIN_PASSWORD = "MAP_LOGIN_PASSWORD";

	private boolean isNetError;
	private boolean loginState;

	private ProgressDialog proDialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_idcardaccount_activity);
		findViewsById();
		Log.v("mytestlog", "start");
		initView(false);
		setListener();
	}

	private void findViewsById() {
		view_userName = (EditText) findViewById(R.id.loginUserNameEdit);
		view_password = (EditText) findViewById(R.id.loginPasswordEdit);
		view_rememberMe = (CheckBox) findViewById(R.id.loginRememberMeCheckBox);
		view_loginSubmit = (Button) findViewById(R.id.loginSubmit);
	}

	private void initView(boolean isRememberMe) {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		String userName = share.getString(SHARE_LOGIN_USERNAME, "");
		String password = share.getString(SHARE_LOGIN_PASSWORD, "");
		Log.d(this.toString(), "userName=" + userName + " password=" + password);
		if (!"".equals(userName)) {
			view_userName.setText(userName);
		}
		if (!"".equals(password)) {
			view_password.setText(password);
			view_rememberMe.setChecked(true);
		}

		if (view_password.getText().toString().length() > 0) {
			// view_loginSubmit.requestFocus();
			// view_password.requestFocus();
		}
		share = null;
	}

	private void setListener() {
		view_loginSubmit.setOnClickListener(submitListener);
		view_rememberMe.setOnCheckedChangeListener(rememberMeListener);
	}

	private OnClickListener submitListener = new OnClickListener() {
		public void onClick(View v) {
			proDialog = ProgressDialog.show(IDCardAccountActivity.this, "请稍候",
					"", true, true);

			Thread loginThread = new Thread(new LoginFailureHandler());
			loginThread.start();
		}
	};

	class LoginFailureHandler implements Runnable {

		public void run() {
			userName = view_userName.getText().toString();
			password = view_password.getText().toString();

			String validateURL = "http://herald.seu.edu.cn/authentication/";
			loginState = validateLocalLogin(userName, password, validateURL);
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putBoolean("loginState", loginState);
			bundle.putBoolean("isNetError", isNetError);
			message.setData(bundle);
			loginHandler.sendMessage(message);

			/*
			 * if(loginState) { Intent intent = new Intent();
			 * intent.setClass(MainActivity.this, IndexPage.class); Bundle
			 * bundle = new Bundle(); bundle.putString("MAP_USERNAME",
			 * userName); intent.putExtras(bundle);
			 * 
			 * startActivity(intent); proDialog.dismiss(); Looper.prepare();
			 * Toast.makeText(MainActivity.this, "登录成功！",
			 * Toast.LENGTH_SHORT).show(); Looper.loop(); Message message = new
			 * Message(); Bundle bundle = new Bundle();
			 * bundle.putBoolean("loginState", loginState);
			 * message.setData(bundle); loginHandler.sendMessage(message); }
			 * else {
			 * 
			 * }
			 */

		}

	}

	Handler loginHandler = new Handler() {
		public void handleMessage(Message msg) {
			isNetError = msg.getData().getBoolean("isNetError");

			if (proDialog != null) {
				proDialog.dismiss();
			}
			if (isNetError) {
				Toast.makeText(IDCardAccountActivity.this, "当前网络不可用",
						Toast.LENGTH_SHORT).show();
			} else {
				if (loginState) {
					Toast.makeText(IDCardAccountActivity.this, "登录成功！",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(IDCardAccountActivity.this, "错误的用户名或密码",
							Toast.LENGTH_SHORT).show();
					clearSharePassword();
				}

			}
		}
	};

	private boolean validateLocalLogin(String username, String password,
			String validateUrl) {
		boolean loginState = false;
		HttpURLConnection conn = null;

		Log.v("mytestlog", validateUrl);
		try {
			URL url = new URL(validateUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(5000);
			// 此方法在正式链接之前设置才有效。
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);

			// 正式创建链接
			conn.connect();

			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

			String postContent = URLEncoder.encode("username", "UTF-8") + "="
					+ URLEncoder.encode(username, "UTF-8") + "&"
					+ URLEncoder.encode("password", "UTF-8") + "="
					+ URLEncoder.encode(password, "UTF-8");

			dos.write(postContent.getBytes());
			dos.flush();
			// 执行完dos.close()后，POST请求结束
			dos.close();
			// 开始GET数据
			// 开始GET数据
			// 需要根据API更改
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				Log.d(this.toString(), "HTTP ERROR");
				isNetError = true;
				return false;
			}
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				loginState = true;
				String encoding = conn.getContentEncoding();
				Log.v("mytestlog",
						"return:"
								+ ((Integer) conn.getResponseCode()).toString());
				InputStream is = conn.getInputStream();
				int read = -1;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while ((read = is.read()) != -1) {
					baos.write(read);
				}
				byte[] data = baos.toByteArray();
				baos.close();

				String content = null;
				if (encoding != null) {
					content = new String(data, encoding);
				} else {
					content = new String(data);
				}
				Log.v("mytestlog", content);
			}
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			Log.v("errorlog", "\r\n" + sw.toString() + "\r\n");
			isNetError = true;

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		if (loginState) {
			if (isRememberMe()) {
				saveSharePreferences(true, true);
			} else {
				saveSharePreferences(true, false);
			}
		} else {
			if (!isNetError) {
				clearSharePassword();
			}
		}
		if (!view_rememberMe.isChecked()) {
			clearSharePassword();
		}
		return loginState;
	}

	private OnCheckedChangeListener rememberMeListener = new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (view_rememberMe.isChecked()) {
				Toast.makeText(IDCardAccountActivity.this, "已记住",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	private void saveSharePreferences(boolean saveUserName, boolean savePassword) {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		if (saveUserName) {
			Log.d(this.toString(), "saveUserName="
					+ view_userName.getText().toString());
			share.edit()
					.putString(SHARE_LOGIN_USERNAME,
							view_userName.getText().toString()).commit();
		}
		if (savePassword) {
			share.edit()
					.putString(SHARE_LOGIN_PASSWORD,
							view_password.getText().toString()).commit();
		}
		share = null;
	}

	private boolean isRememberMe() {
		if (view_rememberMe.isChecked()) {
			return true;
		}
		return false;
	}

	private void clearSharePassword() {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		share.edit().putString(SHARE_LOGIN_PASSWORD, "").commit();
		share = null;
	}

	/*
	 * public boolean onCreateOptionsMenu(Menu menu) {
	 * super.onCreateOptionsMenu(menu); menu.add(0, MENU_EXIT, 0,
	 * getResources().getText(R.string.MENU_EXIT)); menu.add(0, MENU_ABOUT, 0,
	 * getResources().getText(R.string.MENU_ABOUT)); return true; }
	 * 
	 * 
	 * public boolean onMenuItemSelected(int featureId, MenuItem item) {
	 * super.onMenuItemSelected(featureId, item); switch (item.getItemId()) {
	 * case MENU_EXIT: finish(); break; case MENU_ABOUT: alertAbout(); break; }
	 * return true; }
	 * 
	 * 
	 * private void alertAbout() { new
	 * AlertDialog.Builder(MainActivity.this).setTitle(R.string.MENU_ABOUT)
	 * .setMessage(R.string.aboutInfo).setPositiveButton( R.string.ok_label, new
	 * DialogInterface.OnClickListener() { public void onClick( DialogInterface
	 * dialoginterface, int i) { } }).show(); }
	 */

}
