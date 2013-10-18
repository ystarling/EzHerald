package com.herald.ezherald.library;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.UserAccount;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class LibraryFragmentMineRenewThread extends Thread{
	Context context;
	Activity activity;
	JSONArray jsonarray;
	JSONObject jsonobject;
	String barcode;
	ProgressDialog dialog;
	private MyHandle2 myHandler2=new MyHandle2();
	public LibraryFragmentMineRenewThread(Activity ac,Context cn,String barcode){
		this.context=cn;
		this.activity=ac;
		this.barcode=barcode;
		dialog=new ProgressDialog(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage("Please wait ...");
		}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			ShowMsg2("BeginDialog");
			HttpResponse response=null;
			try{
			DefaultHttpClient client=new DefaultHttpClient();
			List<NameValuePair> list=new ArrayList<NameValuePair>();
			
			UserAccount LibrAccount = Authenticate.getLibUser(context);
			NameValuePair pair1=new BasicNameValuePair("username",LibrAccount.getUsername());
			list.add(pair1);
			NameValuePair pair2=new BasicNameValuePair("password",LibrAccount.getPassword());
			list.add(pair2);
			NameValuePair pair3=new BasicNameValuePair("barcode",barcode); 
			list.add(pair3);
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,"UTF-8");
			// 设置网络超时参数
			HttpParams httpParams = client.getParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
			HttpConnectionParams.setSoTimeout(httpParams, 5000);
			
			LibraryUrl url=new LibraryUrl();
			
			HttpPost post=new HttpPost(url.getLIBRARY_MINE_BOOK_RENEW());
			post.setEntity(entity);
			
			response=client.execute(post);
			}catch(Exception ex){
				Log.d("Networking",ex.getMessage());
				if(!ex.getMessage().isEmpty()){
					ShowMsg2("Networking");
				}
			}
			
			InputStream isr=response.getEntity().getContent();
			BufferedReader br=new BufferedReader(new InputStreamReader(isr,"UTF-8"));
			
			String line=null;
			StringBuffer sb=new StringBuffer();
			while((line=br.readLine())!=null){
				sb.append(line);
			}
	       
			jsonobject=new JSONObject(sb.toString());
			
			Log.e("Renew return value",jsonobject.getString("result").toString());
			if(jsonobject.getString("result").equals("false")){
				ShowMsg2("RenewFalse");
			}else{
				ShowMsg2("RenewSuccess");
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void ShowMsg2(String e){
		Message msg=Message.obtain();
		msg.obj=e;
		msg.setTarget(myHandler2);
		msg.sendToTarget();
	}
	public class MyHandle2 extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			String va=(String) msg.obj;

			if(va=="BeginDialog"){
				dialog.show();
			}else
			if(va=="Networking"){
				Toast toast1=Toast.makeText(activity, "网络连接错误...", Toast.LENGTH_LONG);
				toast1.show();
				dialog.cancel();
			}else
			if(va=="RenewSuccess"){
				
				Toast toast1=Toast.makeText(activity, "续借成功...", Toast.LENGTH_LONG);
				toast1.show();
				dialog.cancel();
				
			}else
				if(va=="RenewFalse"){
					
					Toast toast1=Toast.makeText(activity, "续借失败...", Toast.LENGTH_LONG);
					toast1.show();
					dialog.cancel();
				}
			super.handleMessage(msg);
		}

	
	}
}
