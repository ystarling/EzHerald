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


import cn.edu.seu.herald.auth.AuthenticationService;
import cn.edu.seu.herald.auth.AuthenticationServiceException;
import cn.edu.seu.herald.auth.AuthenticationServiceFactory;
import cn.edu.seu.herald.auth.AuthenticationServiceFactoryImpl;
import cn.edu.seu.herald.auth.StudentUser;



import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;


public class IDCardAccountFragment extends SherlockFragment {
	private String userName;
	private String password;

	private EditText view_userName;
	private EditText view_password;
    private TextView IDCardInfo;
    private TextView IDCardUserName;
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
		UserAccount IDCardAccount = Authenticate.getIDcardUser(getSherlockActivity());
		if(IDCardAccount==null){
	    view = inflater.inflate(R.layout.account_idcardaccount_activity,group,false);
		findViewsById(view);
		setListener();
		}else {
	    view = inflater.inflate(R.layout.account_idcardaccountlogoff_activity,group,false);
		findViewsByIdLogOff(view,IDCardAccount);
		setListenerLogOff();	
		}
	    return view;		
	}
	private void findViewsById(View view) {
		view_userName = (EditText) view.findViewById(R.id.loginUserNameEdit);
		view_password = (EditText) view.findViewById(R.id.loginPasswordEdit);
		view_loginSubmit = (Button) view.findViewById(R.id.loginSubmit);	
	}
	private void findViewsByIdLogOff(View view,UserAccount userAccount) {
		IDCardInfo = (TextView) view.findViewById(R.id.IDCardInfo);
		IDCardUserName = (TextView) view.findViewById(R.id.IDCardUserName);		
		IDCardUserName.setText(userAccount.getUsername());
		view_logoffSubmit = (Button) view.findViewById(R.id.logoffSubmit);		
	}
	private void setListenerLogOff(){
		
		view_logoffSubmit.setOnClickListener(logoffsubmitListener);

	}
	private OnClickListener logoffsubmitListener = new OnClickListener() {
		public void onClick(View v) {
			databaseHelper = new DatabaseHelper(getActivity(), Authenticate.DATABASE_NAME);
			SQLiteDatabase database = databaseHelper.getWritableDatabase();
			database.execSQL("DELETE FROM "+Authenticate.TABLE_NAME + " WHERE type=" +"'" +Authenticate.IDCARD_TYPE+ "'");
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

		protected static final int LOGIN_REQ_RESULT_CODE = 200;  //请求结果的code

		public void run() {
			boolean loginState = false;
			boolean isNetError = !isNetworkAvailable(getActivity());
			boolean isServiceError = false;
			userName = view_userName.getText().toString();
			password = view_password.getText().toString();
			try {
				if(!isNetError){
					//Log.v("mynet", "querystart");
				AuthenticationServiceFactory factory = new AuthenticationServiceFactoryImpl();
				
				AuthenticationService service = factory.getAuthenticationService();
				//Log.v("mynet", "querystop");
				StudentUser studentUser = service.authenticate(userName,password);
				
			if(studentUser == null){
				loginState = false;
			}else {
				//登陆成功
				loginState = true;
				Log.v("mynet", "insertstart");
				databaseHelper = new DatabaseHelper(getActivity(), Authenticate.DATABASE_NAME);
				SQLiteDatabase database = databaseHelper.getWritableDatabase();
				database.execSQL("DELETE FROM "+Authenticate.TABLE_NAME + " WHERE type=" +"'" +Authenticate.IDCARD_TYPE+ "'");
				ContentValues values = new ContentValues();
				values.put("id", 1);
				values.put("username", userName);
				values.put("password", password);
							
				values.put("type", Authenticate.IDCARD_TYPE);
				database.insert(Authenticate.TABLE_NAME, null, values);
				database.close();
				
				IDCardAccountActivity currActivity = (IDCardAccountActivity)getActivity();
				boolean isRemoteCall = currActivity.isRemoteModuleCall;
				if(!isRemoteCall){
					//如果是别的模块跳转到这里的，不用跳回去！
					Log.d("IDCardAccountFragment: isRemoteCall", "" +isRemoteCall);
					Intent newActivity = new Intent(getSherlockActivity(),AccountActivity.class);
			        startActivity(newActivity);
				}
		        getSherlockActivity().finish();
		        
			}
			}
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putBoolean("loginState", loginState);
			bundle.putBoolean("isNetError", isNetError);
			message.setData(bundle);
			loginHandler.sendMessage(message);
		
		}catch (AuthenticationServiceException e) {
			Log.v("mytestlog", e.getMessage());
			isServiceError = true;
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putBoolean("loginState", loginState);
			bundle.putBoolean("isNetError", isNetError);
			bundle.putBoolean("isServiceError", isServiceError);
			message.setData(bundle);
			loginHandler.sendMessage(message);
			
		}		
			catch (Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				Log.v("errorlog", "\r\n" + sw.toString() + "\r\n");
				
		}
	}
	
	Handler loginHandler = new Handler(){
	public void handleMessage(Message msg) {
			Intent returnMsgIntent = new Intent();
			returnMsgIntent.putExtra("loginSucceed", false);
		
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
					Toast.makeText(getActivity(), "当前服务不可用，请稍后再试",
							Toast.LENGTH_SHORT).show();
				}else{
				if (loginState) {
					Toast.makeText(getActivity(), "登录成功！",
							Toast.LENGTH_SHORT).show();
					returnMsgIntent.putExtra("loginSucceed", true);
				} else {
					Toast.makeText(getActivity(), "错误的用户名或密码",
							Toast.LENGTH_SHORT).show();
				}
				}

			}
			getActivity().setResult(LOGIN_REQ_RESULT_CODE, returnMsgIntent);
			
		}
	};
}
	
   
	    
}