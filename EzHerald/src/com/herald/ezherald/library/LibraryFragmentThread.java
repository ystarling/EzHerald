package com.herald.ezherald.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class LibraryFragmentThread extends Thread{

	private ProgressDialog dialog1;
	Activity activity;
	String Book_Search_Value;
	//Context context;
	JSONArray jsonarray;
	//ListView listview;
	//LibraryBookMyAdapter myAdapter;
	int CountOfScroll;
	LibraryFragment libraryfragment;
	//int length;
	//boolean isLastRow = false;
	//private MyHandle myHandler = new MyHandle();// 初始化Handler
	private MyHandle2 myHandler2 = new MyHandle2();
	private MyHandle3 myHandler3 = new MyHandle3();
	
	
	public LibraryFragmentThread(String search_value,Activity ac, int num,LibraryFragment th) {
		this.Book_Search_Value = search_value;
		this.activity = ac;

		this.CountOfScroll=num;
		this.libraryfragment=th;
		

		
		dialog1 = new ProgressDialog(activity);
		dialog1.setCanceledOnTouchOutside(false);
		dialog1.setMessage("加载中...");
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ShowMsg2("ProgressDialog");
			
			HttpResponse response = null;
			try {
				DefaultHttpClient client = new DefaultHttpClient();
				String url_value = "?strText=" + Book_Search_Value + "&page="
						+ CountOfScroll;
				LibraryUrl url = new LibraryUrl();
				HttpGet get;

				get = new HttpGet(url.getLIBRARY_SEARCH_URL() + url_value);
				response = client.execute(get);

			} catch (Exception ex) {
				Log.d("Notwoking", ex.getLocalizedMessage());
				if (!ex.getLocalizedMessage().isEmpty()) {
					ShowMsg2("NetWorkingError");
				}
				throw new IOException("Error connecting");
			}

			// HttpConnectionParams.setSoTimeout(httpParams, 5000);

			InputStream isr = response.getEntity().getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(isr,
					"UTF-8"));

			String line = null;
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			
			
			jsonarray = new JSONArray(sb.toString());
			
			String[] BookName = new String[jsonarray.length()];
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject json = jsonarray.getJSONObject(i);
				BookName[i] = json.getString("title");
				Log.d("id:", "" + BookName[i]);
			}
			
			
//			JSONArray json1=new JSONArray();
//			JSONObject jsonO=new JSONObject();
//			jsonO.put("title", "zhang");
//			jsonO.put("author", "yu");
//			jsonO.put("publisher", "yu");
//			jsonO.put("isbn", "yu");
//			jsonO.put("doctype", "yu");
//			jsonO.put("marc_no", "110");
//			jsonO.put("store_num", "yu");
//			jsonO.put("lendable_num", "yu");
//			json1.put(jsonO);
//			JSONObject jsonO1=new JSONObject();
//			jsonO1.put("title", "zhang");
//			jsonO1.put("author", "yu");
//			jsonO1.put("publisher", "yu");
//			jsonO1.put("isbn", "yu");
//			jsonO1.put("doctype", "yu");
//			jsonO1.put("marc_no", "112");
//			jsonO1.put("store_num", "yu");
//			jsonO1.put("lendable_num", "yu");
//			json1.put(jsonO1);
//			Log.e("sss",json1+"");
			
			
			ShowMsg3(jsonarray);
			
//			Log.d("LoadMore","true");
			dialog1.cancel();
		} catch (Exception e) {
			Log.e("Error",e.getMessage());
			ShowMsg2("ValueError");
		}
	}

	public void ShowMsg2(String e) {
		Message msg = Message.obtain();
		msg.obj = e;
		msg.setTarget(myHandler2);
		msg.sendToTarget();
	}

	public class MyHandle2 extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			String va = (String) msg.obj;

			if (va == "ProgressDialog") {
				dialog1.show();
			}
			if(va=="NetWorkingError"){
				Toast toast1 = Toast.makeText(activity, "网络请求错误...",
						Toast.LENGTH_LONG);
				toast1.show();
				dialog1.cancel();
			}
			if(va=="ValueError"){
				Toast toast1 = Toast.makeText(activity, "网络请求错误...",
						Toast.LENGTH_LONG);
				toast1.show();
				dialog1.cancel();
			}
			super.handleMessage(msg);
		}

	}
	
	public void ShowMsg3(JSONArray e) {
	Message msg = Message.obtain();
	msg.obj = e;
	msg.setTarget(myHandler3);
	msg.sendToTarget();
	}
	
	public class MyHandle3 extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			JSONArray json3=(JSONArray) msg.obj; 
			if(CountOfScroll==1){
				libraryfragment.SetData(json3,CountOfScroll);
				
			}else{
				libraryfragment.LoadMoreData(json3,CountOfScroll);
			}
				super.handleMessage(msg);
		}

		
	}

		public void SetRemind() {
			Toast toast1 = Toast.makeText(activity, "该图书不存在!",
					Toast.LENGTH_SHORT);
			toast1.show();
		}


}
