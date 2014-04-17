package com.herald.ezherald.wifi;



import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.UserAccount;

public class LoginDialogActivity extends Activity {
	private Context context;
	private AlertDialog.Builder builder;
	private UserAccount user;
	private final String URL = "https://w.seu.edu.cn/portal/login.php";
	private static final int SUCCESS = 1;
	private static final int FAILED = 0;
	private AlertDialog dialog;
	private Handler handler = new Handler(){
		private String result;
		@Override
		public void handleMessage(Message msg){
			result = (String)msg.obj;
			switch(msg.what){
			case FAILED:onLoginFailed();break;
			case SUCCESS:onLoginSuccess();break;
			}
			LoginDialogActivity.this.finish();
		}

		private void onLoginFailed() {
			Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
		}

		private void onLoginSuccess() {
			//TODO 显示流量情况
			Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.context = this;
		super.onCreate(savedInstanceState);
		showDialog();
	}

	private void showDialog() {
		builder = new AlertDialog.Builder(context);
		builder.setTitle("发现seu-wlan");
		builder.setMessage("是否自动登录");
		builder.setPositiveButton("是", new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				user = Authenticate.getIDcardUser(context);
				if(user == null) {
					Toast.makeText(context, "请先登录先声", Toast.LENGTH_SHORT).show();
					LoginDialogActivity.this.finish();
					//TODO lead to app to login
				} else {
					login(dialog);
				}
				
			}

			private void login(final DialogInterface dialog) {
				/*
				 * Example json
				 * {"error":"\u8ba4\u8bc1\u5931\u8d25, \u5931\u8d25\u53ef\u80fd\u539f\u56e0\uff1a1\u3001\u7528\u6237\u540d\u5bc6\u7801\u9519\u8bef\uff1b2\u3001\u65b0\u589e\u7528\u6237\u672a\u4fee\u6539\u521d\u59cb\u5316\u5bc6\u7801\uff0c\u8bf7\u767b\u5f55\u6821\u56ed\u4fe1\u606f\u95e8\u6237\u4fee\u6539\u521d\u59cb\u5bc6\u7801\u3002"}
				 * {"success":"\u8ba4\u8bc1\u6210\u529f","login_username":"213xxxxxx","login_ip":"xxx.xxx.xxx.xxx","login_location":"\u672a\u77e5\u4f4d\u7f6e","login_time":0,"login_url":null}
				 */
				//ProgressBar progress = new ProgressBar(context);
				final ProgressDialog progress = new ProgressDialog(context);
				progress.setTitle("正在登录");
				progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progress.show();
				new Thread(){
					@Override
					public void run(){
						HttpClient client = new DefaultHttpClient();
						HttpPost post = new HttpPost(URL);
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("username",user.getUsername()));
						params.add(new BasicNameValuePair("password",user.getPassword()));						
						try{
							post.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
							HttpResponse response = client.execute(post);
							if(response.getStatusLine().getStatusCode() == 200){
								HttpEntity entity = response.getEntity();
								InputStream is = entity.getContent();
								long total = entity.getContentLength();
								progress.setMax(100);
								ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
								byte[] buffer = new byte[1024];
								int count=0,length = -1;
								while((length=is.read(buffer))!=-1){
									bos.write(buffer);
									count+=length;
									progress.setProgress(100*(int)(count/total));
								}
								String result = new String(bos.toByteArray());
								//String result = EntityUtils.toString(response.getEntity());
								while(!result.startsWith("{")){
									result = result.substring(1);
								}
								Log.v("res",result);
								JSONObject json = new JSONObject(result);
								if(json.has("success")){
									handler.obtainMessage(SUCCESS,json.getString("success")).sendToTarget();
								} else {
									handler.obtainMessage(FAILED, json.getString("error")).sendToTarget();
								}
							}
							
						}catch(Exception e){
							e.printStackTrace();
							handler.obtainMessage(FAILED, "网络错误").sendToTarget();
						}
						dialog.dismiss();
						progress.dismiss();
					}
				}.start();
			}
			
		});
		builder.setNegativeButton("否",new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
			
		});
		builder.setCancelable(false);
		dialog = builder.create();
		dialog.show();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
	}
}
