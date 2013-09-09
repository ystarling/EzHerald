package com.herald.ezherald.account;
import java.io.PrintWriter;
import java.io.StringWriter;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.widget.TextView;

import android.widget.EditText;
import android.widget.Toast;






import cn.edu.seu.herald.ws.api.AuthenticationException;
import cn.edu.seu.herald.ws.api.HeraldWebServicesFactory;
import cn.edu.seu.herald.ws.api.LibraryService;
import cn.edu.seu.herald.ws.api.MorningExerciseService;
import cn.edu.seu.herald.ws.api.ServiceException;
import cn.edu.seu.herald.ws.api.exercise.RunTimesData;
import cn.edu.seu.herald.ws.api.impl.HeraldWebServicesFactoryImpl;
import cn.edu.seu.herald.ws.api.library.User;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;


public class TyxAccountFragment extends SherlockFragment {
	private String userName;
	private String password;

	private EditText view_userName;
	private EditText view_password;
    private TextView TyxInfo;
    private TextView TyxUserName;
	private Button view_loginSubmit;
	private Button view_logoffSubmit;
	private DatabaseHelper databaseHelper = null;

	private ProgressDialog proDialog;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved){
		//super.onCreate(savedInstanceState);
		View view = null;
		UserAccount tyxAccount = Authenticate.getTyxUser(getSherlockActivity());
		if(tyxAccount==null){
	    view = inflater.inflate(R.layout.account_tyxaccount_activity,group,false);
		findViewsById(view);
		setListener();
		}else {
	    view = inflater.inflate(R.layout.account_tyxaccountlogoff_activity,group,false);
		findViewsByIdLogOff(view,tyxAccount);
		setListenerLogOff();	
		}
	    return view;		
	}
	private void findViewsById(View view) {
		view_userName = (EditText) view.findViewById(R.id.TyxLoginUserNameEdit);
		view_password = (EditText) view.findViewById(R.id.TyxLoginPasswordEdit);
		view_loginSubmit = (Button) view.findViewById(R.id.TyxLoginSubmit);	
	}
	private void findViewsByIdLogOff(View view,UserAccount userAccount) {
		TyxInfo = (TextView) view.findViewById(R.id.TyxInfo);
		TyxUserName = (TextView) view.findViewById(R.id.TyxUserName);		
		TyxUserName.setText(userAccount.getUsername());
		view_logoffSubmit = (Button) view.findViewById(R.id.TyxLogoffSubmit);		
	}
	private void setListenerLogOff(){
		
		view_logoffSubmit.setOnClickListener(logoffsubmitListener);

	}
	private OnClickListener logoffsubmitListener = new OnClickListener() {
		public void onClick(View v) {
			databaseHelper = new DatabaseHelper(getActivity(), Authenticate.DATABASE_NAME);
			SQLiteDatabase database = databaseHelper.getWritableDatabase();
			database.execSQL("DELETE FROM "+Authenticate.TABLE_NAME + " WHERE type=" +"'" +Authenticate.TYX_TYPE+ "'");
			database.close();
			Intent newActivity = new Intent(getSherlockActivity(),AccountActivity.class);     
	        startActivity(newActivity);
	        getSherlockActivity().finish();
		}
	};
	/*private void initView(boolean isRememberMe) {
		
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
*/
	private void setListener() {
		view_loginSubmit.setOnClickListener(submitListener);
		
	}

	private OnClickListener submitListener = new OnClickListener() {
		public void onClick(View v) {
			proDialog = ProgressDialog.show(getActivity(), "请稍候",
					"", true, true);

			Thread loginThread = new Thread(new LoginFailureHandler());
			loginThread.start();
		}
	};


	public static boolean isNetworkAvailable(Context context) {
		Log.v("mynet", "start");
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);   
        if (connectivity == null) {   
            Log.i("NetWorkState", "Unavailabel");   
            return false;   
        } else {   
            NetworkInfo[] info = connectivity.getAllNetworkInfo();   
            if (info != null) {   
                for (int i = 0; i < info.length; i++) {   
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {   
                        Log.i("NetWorkState", "Availabel");
                        Log.v("mynet", "yes");
                        return true;   
                    }   
                }   
            }   
        }
        Log.v("mynet", "no");
        return false;   
    }
	class LoginFailureHandler implements Runnable {

		public void run() {
			boolean loginState = false;
			boolean isNetError = !isNetworkAvailable(getActivity());
			boolean isServiceError = false;
			userName = view_userName.getText().toString();
			password = view_password.getText().toString();
			try {
				if(!isNetError){
					final String HERALD_WS_BASE_URI = "http://herald.seu.edu.cn/ws";
					
					HeraldWebServicesFactory factory = new HeraldWebServicesFactoryImpl(HERALD_WS_BASE_URI);
					
					MorningExerciseService exeService = factory.getMorningExerciseService();

					Log.v("myname", userName);
					RunTimesData runTimesData = exeService.getRunTimesData(userName, password);
				
			if(runTimesData == null){
				loginState = false;
			}else {
				
				loginState = true;
				Log.v("mynet", "insertstart");
				databaseHelper = new DatabaseHelper(getActivity(), Authenticate.DATABASE_NAME);
				SQLiteDatabase database = databaseHelper.getWritableDatabase();
				database.execSQL("DELETE FROM "+Authenticate.TABLE_NAME + " WHERE type=" +"'" +Authenticate.TYX_TYPE+ "'");
				ContentValues values = new ContentValues();
				values.put("id", 1);
				values.put("username", userName);
				values.put("password", password);
				values.put("type", Authenticate.TYX_TYPE);
				database.insert(Authenticate.TABLE_NAME, null, values);
				database.close();
				Intent newActivity = new Intent(getSherlockActivity(),AccountActivity.class);     
		        startActivity(newActivity);
		        getSherlockActivity().finish();
			}
			}
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putBoolean("loginState", loginState);
			bundle.putBoolean("isNetError", isNetError);
			bundle.putBoolean("isServiceError", isServiceError);
			message.setData(bundle);
			loginHandler.sendMessage(message);
			

		}catch (AuthenticationException e) {
			Log.v("TyxAccountServiceEx", "TyxAccountServiceEx");
			loginState = false;
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putBoolean("loginState", loginState);
			bundle.putBoolean("isNetError", isNetError);
			bundle.putBoolean("isServiceError", isServiceError);
			message.setData(bundle);
			loginHandler.sendMessage(message);
			proDialog.dismiss();
			
		}
		catch (ServiceException e) {
			Log.v("TyxAccountAuthenEx", "TyxAccountAuthenEx");
			isServiceError = true;
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putBoolean("loginState", loginState);
			bundle.putBoolean("isNetError", isNetError);
			bundle.putBoolean("isServiceError", isServiceError);
			message.setData(bundle);
			loginHandler.sendMessage(message);
			proDialog.dismiss();
			
		}		
	
			catch (Exception e) {
				Log.v("TyxAccountEx", "TyxAccountEx");
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				Log.v("errorlog", "\r\n" + sw.toString() + "\r\n");
				
				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putBoolean("isUnknownError", false);
				message.setData(bundle);
				UnknownErrorHandler.sendMessage(message);				
				proDialog.dismiss();
				
				
		}
	}
	
		Handler UnknownErrorHandler = new Handler(){
			public void handleMessage(Message msg) {										
					boolean isUnknownError = msg.getData().getBoolean("isUnknownError");
					if (proDialog != null) {
						proDialog.dismiss();
					}
					Toast.makeText(getActivity(), "抱歉，服务出错，请稍后再试！",
							Toast.LENGTH_SHORT).show();
				}
			};
		
	Handler loginHandler = new Handler(){
	public void handleMessage(Message msg) {
			
			boolean isNetError = msg.getData().getBoolean("isNetError");
			boolean loginState = msg.getData().getBoolean("loginState");
			boolean isServiceError = msg.getData().getBoolean("isServiceError");
			if (proDialog != null) {
				proDialog.dismiss();
			}
			if (isNetError) {
				Toast.makeText(getActivity(), "当前网络不可用",
						Toast.LENGTH_SHORT).show();
			} else {
				if(isServiceError){
					Toast.makeText(getActivity(), "抱歉，体育系登录服务出错，请稍后再试！",
							Toast.LENGTH_SHORT).show();
				}else {
					if (loginState) {
						Toast.makeText(getActivity(), "登录成功！",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getActivity(), "错误的用户名或密码",
								Toast.LENGTH_SHORT).show();
					}
				}

			}
		}
	};
}
	
   
	    
}