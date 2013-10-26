package com.herald.ezherald.account;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;

public class TyxAccountFragment extends SherlockFragment {
	private String userName;
	private String password;

	private EditText view_userName;
	private EditText view_password;
	private TextView TyxUserName;
	private TextView TyxInfo;
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
		UserAccount tyxAccount = Authenticate.getTyxUser(getSherlockActivity());
		if (tyxAccount == null) {
			view = inflater.inflate(R.layout.account_tyxaccount_activity,
					group, false);
			findViewsById(view);
			setListener();
		} else {
			view = inflater.inflate(R.layout.account_tyxaccountlogoff_activity,
					group, false);
			findViewsByIdLogOff(view, tyxAccount);
			setListenerLogOff();
		}
		return view;
	}

	private void findViewsById(View view) {
		view_userName = (EditText) view.findViewById(R.id.TyxLoginUserNameEdit);
		view_password = (EditText) view.findViewById(R.id.TyxLoginPasswordEdit);
		view_loginSubmit = (Button) view.findViewById(R.id.TyxLoginSubmit);
	}

	private void findViewsByIdLogOff(View view, UserAccount userAccount) {
		TyxInfo = (TextView) view.findViewById(R.id.TyxInfo);
		TyxUserName = (TextView) view.findViewById(R.id.TyxUserName);
		TyxUserName.setText(userAccount.getUsername());
		view_logoffSubmit = (Button) view.findViewById(R.id.TyxLogoffSubmit);
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
					+ " WHERE type=" + "'" + Authenticate.TYX_TYPE + "'");
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
		public final String HERALD_WS_TYX_URI = "http://herald.seu.edu.cn/herald_web_service/tyx/checkAccount/";
		public final String POST_KEY_USERNAME = "card_number";
		public final String POST_KEY_PASSWORD = "password";
		
		public final int VERIFY_AUTH_OKAY = 200; 
		public final int VERIFY_AUTH_FAIL = 300;
		public final int VERIFY_SERVICE_ERROR = 500;
		
		public void run() {
			Message msg = new Message();
			userName = view_userName.getText().toString();
			password = view_password.getText().toString();
			try {
				if (isNetworkAvailable(getActivity())) {
					int verifyResult = verifyLibUserPswd(userName, password);
					if(verifyResult == VERIFY_AUTH_OKAY){
						databaseHelper = new DatabaseHelper(getActivity(),
								Authenticate.DATABASE_NAME);
						SQLiteDatabase database = databaseHelper
								.getWritableDatabase();
						database.execSQL("DELETE FROM "
								+ Authenticate.TABLE_NAME + " WHERE type="
								+ "'" + Authenticate.TYX_TYPE + "'");
						ContentValues values = new ContentValues();
						values.put("id", 1);
						values.put("username", userName);
	
						values.put("password", EncryptionHelper.encryptDES(password, EncryptionHelper.KEY));
						
						values.put("type", Authenticate.TYX_TYPE);
						database.insert(Authenticate.TABLE_NAME, null, values);
						database.close();
						Intent newActivity = new Intent(getSherlockActivity(),
								AccountActivity.class);
						startActivity(newActivity);
						getSherlockActivity().finish();
						authenTrueHandler.sendMessage(msg);
					}else if (verifyResult == VERIFY_AUTH_FAIL) {
						authenErrorHandler.sendMessage(msg);
					}else {
						serviceErrorHandler.sendMessage(msg);
					}
					
				}else {
					netErrorHandler.sendMessage(msg);
				}
				
				proDialog.dismiss();
			}catch (Exception e) {
				Message message = new Message();
				unknownErrorHandler.sendMessage(message);
				proDialog.dismiss();

			}
		}
		
		private int verifyLibUserPswd(String userName, String passWord) {			
			int retValue = VERIFY_SERVICE_ERROR;
			HttpPost httpPost = new HttpPost(HERALD_WS_TYX_URI);  //创建HttpPost对象
			
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
				Toast.makeText(getActivity(), "抱歉，体育系登录服务出错，请稍后再试", Toast.LENGTH_SHORT).show();
				
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