package com.herald.ezherald.freshman;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;

public class FreshmanInfo {
	public static final int STUDY=0,LIFE=1,PLAY=2,FAQ=3;
	private static final String SharedPreferenceName = "FreshMan";
	private static final boolean DEBUG = true;
	private List<List<String>> titles;
	private List<List<String>> content;
	private Activity activity;
	private SharedPreferences shared;
	private Handler handler;
	private final String url = "http://herald.seu.edu.cn/xyzn/index/info_for_android/";
	private final int SUCCESS = 1;
	private final int FAILED = 0;
	private String jsonStr;
	
	private String testJson = "{\"play\": [{\"content\": \"内容\", \"title\": \"标题\"}, {\"content\": \"...\", \"title\": \"...\"}], \"study\": [{\"content\": \"内容\", \"title\": \"标题\"}, {\"content\": \"..\", \"title\": \"..\"}],\"faq\": [{\"content\": \"问题内容\", \"best_reply\": \"最佳回复\", \"title\": \"问题标题\"},{\"content\": \"...\", \"best_reply\": \"...\", \"title\": \"...\"}], \"life\": [{\"content\": \"内容\", \"title\": \"标题\"}]} ";
	
	FreshmanInfo(Activity activity){
		
		this.activity = activity;
		shared = activity.getSharedPreferences(SharedPreferenceName,0);
		jsonStr = shared.getString("json", null);
		if(jsonStr != null){
			dealJson();
		}
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
				if(obj!=null)
					jsonStr = (String)obj;
				
				dealJson();
				save();
			}
		};
		update();
		
	}
	private void addTitle(JSONArray json,int type){
		for(int i=0;i<json.length();++i){
			try {
				titles.get(type).add(json.getJSONObject(i).getString("title"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void addContent(JSONArray json,int type){
		for(int i=0;i<json.length();++i){
			try {
				content.get(type).add(json.getJSONObject(i).getString("title"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void dealJson() {
		titles = new ArrayList<List<String>>();
		content = new ArrayList<List<String>>();
		for(int i=0;i<4;i++){
			titles.add(new ArrayList<String>());
			content.add(new ArrayList<String>());
		}
		try {
			JSONObject root = new JSONObject(jsonStr);
			JSONArray play = root.getJSONArray("play");
			JSONArray study = root.getJSONArray("study");
			JSONArray life = root.getJSONArray("life");
			JSONArray faq = root.getJSONArray("faq");
			
			addTitle(play,PLAY);
			addContent(play, PLAY);
			
			addTitle(study,STUDY);
			addContent(study, STUDY);
			
			addTitle(life,LIFE);
			addContent(life, LIFE);
			
			addTitle(faq,FAQ);
			addContent(faq, FAQ);
			
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public List<List<String>> getContent() {
		return content;
	}
	public void setContent(List<List<String>> content) {
		this.content = content;
	}
	public void update(){
		if(DEBUG){
			jsonStr = testJson;
			//Message msg = handler.obtainMessage(SUCCESS, null);
        	//handler.sendMessage(msg);
			dealJson();
			save();
		return;
		}
		
		
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
	public List<List<String>> getTitles() {
		return titles;
	}
}
