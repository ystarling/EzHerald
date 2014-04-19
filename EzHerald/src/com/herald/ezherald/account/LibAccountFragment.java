package com.herald.ezherald.account;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.EntityUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.widget.TextView;

import android.widget.EditText;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;

public class LibAccountFragment extends SherlockFragment {
	private String userName;
	private String password;

	private EditText view_userName;
	private EditText view_password;
	private TextView LibInfo;
	private TextView LibUserName;
	private Button view_loginSubmit;
	private Button view_logoffSubmit;
	private DatabaseHelper databaseHelper = null;

	private ProgressDialog proDialog;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group,
			Bundle saved) {
		// super.onCreate(savedInstanceState);
		View view = null;
		UserAccount libAccount = Authenticate.getLibUser(getSherlockActivity());
		if (libAccount == null) {
			view = inflater.inflate(R.layout.account_libaccount_activity,
					group, false);
			findViewsById(view);
			setListener();
		} else {
			view = inflater.inflate(R.layout.account_libaccountlogoff_activity,
					group, false);
			findViewsByIdLogOff(view, libAccount);
			setListenerLogOff();
		}
		return view;
	}

	private void findViewsById(View view) {
		view_userName = (EditText) view.findViewById(R.id.LibLoginUserNameEdit);
		view_password = (EditText) view.findViewById(R.id.LibLoginPasswordEdit);
		view_loginSubmit = (Button) view.findViewById(R.id.LibLoginSubmit);
	}

	private void findViewsByIdLogOff(View view, UserAccount userAccount) {
		LibInfo = (TextView) view.findViewById(R.id.LibInfo);
		LibUserName = (TextView) view.findViewById(R.id.LibUserName);
		LibUserName.setText(userAccount.getUsername());
		view_logoffSubmit = (Button) view.findViewById(R.id.LibLogoffSubmit);
	}

	private void setListenerLogOff() {

		view_logoffSubmit.setOnClickListener(logoffsubmitListener);

	}

	private OnClickListener logoffsubmitListener = new OnClickListener() {
		public void onClick(View v) {
			databaseHelper = new DatabaseHelper(getActivity(),
					Authenticate.DATABASE_NAME);
			SQLiteDatabase database = databaseHelper.getWritableDatabase();
			database.execSQL("DELETE FROM " + Authenticate.TABLE_NAME
					+ " WHERE type=" + "'" + Authenticate.LIBRARY_TYPE + "'");
			database.close();
			Intent newActivity = new Intent(getSherlockActivity(),
					AccountActivity.class);
			startActivity(newActivity);
			getSherlockActivity().finish();
		}
	};

	private void setListener() {
		view_loginSubmit.setOnClickListener(submitListener);

	}

	private OnClickListener submitListener = new OnClickListener() {
		public void onClick(View v) {
			proDialog = ProgressDialog.show(getActivity(), "请稍候", "", true,
					true);

			Thread loginThread = new Thread(new LoginFailureHandler());
			loginThread.start();
		}
	};

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {			
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {					
						return true;
					}
				}
			}
		}
	
		return false;
	}

	class LoginFailureHandler implements Runnable {
		public final String HERALD_WS_LIB_URI = "http://121.248.63.105/herald_web_service/library/check_account/";
		public final String POST_KEY_USERNAME = "username";
		public final String POST_KEY_PASSWORD = "password";
		
		public final int VERIFY_AUTH_OKAY = 200; 
		public final int VERIFY_AUTH_FAIL = 300;
		public final int VERIFY_SERVICE_ERROR = 500;
		
		public void run() {
			try {
			Message msg = new Message();
			if(isNetworkAvailable(getActivity())){
				
				userName = view_userName.getText().toString();
				password = view_password.getText().toString();

				int verifyResult = verifyLibUserPswd(userName, password);					
				if(verifyResult == VERIFY_AUTH_OKAY){
					databaseHelper = new DatabaseHelper(getActivity(),
							Authenticate.DATABASE_NAME);
					SQLiteDatabase database = databaseHelper
							.getWritableDatabase();
					database.execSQL("DELETE FROM "
							+ Authenticate.TABLE_NAME + " WHERE type="
							+ "'" + Authenticate.LIBRARY_TYPE + "'");
					ContentValues values = new ContentValues();
					values.put("id", 1);
					values.put("username", userName);
					values.put("password", EncryptionHelper.encryptDES(password, EncryptionHelper.KEY));
					values.put("type", Authenticate.LIBRARY_TYPE);
					database.insert(Authenticate.TABLE_NAME, null, values);
					database.close();
					
					Intent newActivity = new Intent(getSherlockActivity(),
							AccountActivity.class);
					startActivity(newActivity);					
					getSherlockActivity().finish();
					authenTrueHandler.sendMessage(msg);
				} else if(verifyResult == VERIFY_AUTH_FAIL){
					authenErrorHandler.sendMessage(msg);
				} else {
					serviceErrorHandler.sendMessage(msg);
				}
				
			}else{
				netErrorHandler.sendMessage(msg);	
			}
			proDialog.dismiss();
			}catch (Exception e) {	
				Message msg = new Message();
				unknownErrorHandler.sendMessage(msg);
				proDialog.dismiss();

			}
		}
		
		/**
		 * 验证给定的用户名，密码是否合法
		 * @param userName
		 * @param password
		 * @return
		 */
		private int verifyLibUserPswd(String userName, String passWord) {			
			int retValue = VERIFY_SERVICE_ERROR;
			HttpPost httpPost = new HttpPost(HERALD_WS_LIB_URI);  //创建HttpPost对象
			
			//设置Http POST请求参数
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair(POST_KEY_USERNAME, userName));
			params.add(new BasicNameValuePair(POST_KEY_PASSWORD, passWord));
			
			HttpResponse httpResponse = null;
			try{
				//设置post请求参数
				httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				httpResponse = new DefaultHttpClient().execute(httpPost);
				if(httpResponse.getStatusLine().getStatusCode() == 200){
					String result = EntityUtils.toString(httpResponse.getEntity());
					if(result.equals("True"))
						retValue = VERIFY_AUTH_OKAY;
					else{
						if(result.equals("False")){
						retValue = VERIFY_AUTH_FAIL;	
						}else {
						retValue = VERIFY_SERVICE_ERROR;
						}
						
					}
						
				}
			} catch (ClientProtocolException e){
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return retValue;
		}

		Handler unknownErrorHandler = new Handler() {
			public void handleMessage(Message msg) {
				
				if (proDialog != null) {
					proDialog.dismiss();
				}
				Toast.makeText(getActivity(), "抱歉，服务出错，请稍后再试！",
						Toast.LENGTH_SHORT).show();
			}
		};
		Handler netErrorHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (proDialog != null) {
					proDialog.dismiss();
				}			
				Toast.makeText(getActivity(), "当前网络不可用", Toast.LENGTH_SHORT).show();
				
			}
		};
		Handler serviceErrorHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (proDialog != null) {
					proDialog.dismiss();
				}			
				Toast.makeText(getActivity(), "抱歉，图书馆登录服务出错，请稍后再试", Toast.LENGTH_SHORT).show();
				
			}
		};
		Handler authenErrorHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (proDialog != null) {
					proDialog.dismiss();
				}			
				Toast.makeText(getActivity(), "错误的用户名或密码", Toast.LENGTH_SHORT).show();
				
			}
		};
		Handler authenTrueHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (proDialog != null) {
					proDialog.dismiss();
				}			
				Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
				
			}
		};
	}

}
