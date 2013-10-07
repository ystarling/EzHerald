package com.herald.ezherald.freshman;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;

public class FreshmanInfo {
	public static final int STUDY=0,LIFE=1,PLAY=2,FAQ=3;
	private static final String SharedPreferenceName = "FreshMan";
	private List<List<String>> titles;
	private List<List<String>> content;
	private Activity activity;
	private SharedPreferences shared;
	private Handler handler;
	private final String url = "http://herald.seu.edu.cn/xyzn/index/info_for_android/";
	private final int SUCCESS = 1;
	private final int FAILED = 0;
	private String jsonStr;
	FreshmanInfo(Activity activity){
		for(int i=0;i<4;i++){
			titles.add(new ArrayList<String>());
			content.add(new ArrayList<String>());
		}
		this.activity = activity;
		shared = activity.getSharedPreferences(SharedPreferenceName,0);
		jsonStr = shared.getString("json", null);
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch(msg.what){
				case SUCCESS:
					onSuccess(msg.obj);
					break;
				case FAILED:
					onFailed();
					break;
				default:
					break;
				}
			}

			private void onFailed() {
				// TODO Auto-generated method stub
				save();
			}

			private void onSuccess(Object obj) {
				// TODO Auto-generated method stub
				jsonStr = (String)obj;
				dealJson();
				save();
			}
		};
		
		
	}
	protected void dealJson() {
		// TODO Auto-generated method stub
		
	}
	public List<List<String>> getContent() {
		return content;
	}
	public void setContent(List<List<String>> content) {
		this.content = content;
	}
	public void update(){

		new Thread(){
			@Override
			public void run(){
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				try {
					HttpResponse response = client.execute(get);
					if(response.getStatusLine().getStatusCode() != 200){
						throw new Exception("can't link to herald");
					}
					String result = response.getEntity().toString();
					Message msg = handler.obtainMessage(SUCCESS, result);
		        	handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.obtainMessage(FAILED).sendToTarget();
				}
			}
		}.start();
	}
	
	public void save(){
		Editor editor = shared.edit();
		editor.putString("json", jsonStr);
		editor.commit();
	}
}
