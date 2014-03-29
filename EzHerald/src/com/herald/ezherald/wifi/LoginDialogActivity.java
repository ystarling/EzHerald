package com.herald.ezherald.wifi;



import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.UserAccount;

public class LoginDialogActivity extends Activity {
	private Context context;
	private AlertDialog.Builder builder;
	private UserAccount user;
	private final String URL = "https://w.seu.edu.cn/portal/login.php";
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case 0:doLoginFailed();break;
			case 1:doLoginSuccess();break;
			}
		}

		private void doLoginFailed() {
			// TODO Auto-generated method stub
			Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
		}

		private void doLoginSuccess() {
			// TODO Auto-generated method stub
			Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
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
				//dialog.
				/*
				 * Example json
				 * {"error":"\u8ba4\u8bc1\u5931\u8d25, \u5931\u8d25\u53ef\u80fd\u539f\u56e0\uff1a1\u3001\u7528\u6237\u540d\u5bc6\u7801\u9519\u8bef\uff1b2\u3001\u65b0\u589e\u7528\u6237\u672a\u4fee\u6539\u521d\u59cb\u5316\u5bc6\u7801\uff0c\u8bf7\u767b\u5f55\u6821\u56ed\u4fe1\u606f\u95e8\u6237\u4fee\u6539\u521d\u59cb\u5bc6\u7801\u3002"}
				 * {"success":"\u8ba4\u8bc1\u6210\u529f","login_username":"213xxxxxx","login_ip":"xxx.xxx.xxx.xxx","login_location":"\u672a\u77e5\u4f4d\u7f6e","login_time":0,"login_url":null}
				 */
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
								HttpGet get = new HttpGet("https://w.seu.edu.cn/portal/init.php");
								response = client.execute(get);
								if (response.getStatusLine().getStatusCode() != 200) {
									handler.obtainMessage(0).sendToTarget();//return false;
								} else {
									String msg = EntityUtils.toString(response.getEntity());
									while(!msg.startsWith("{")){
										msg = msg.substring(1);
									}
									JSONObject json = new JSONObject(msg);
									if(json.has("login")){
										handler.obtainMessage(1).sendToTarget();//return true;
									}else{
										handler.obtainMessage(0).sendToTarget();//return false;
									}
								}
							}
							
						}catch(Exception e){
							e.printStackTrace();
							Toast.makeText(context, "失败", Toast.LENGTH_SHORT).show();
							//dialog.dismiss();
						}
						dialog.dismiss();
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
		builder.show();
	}
}
