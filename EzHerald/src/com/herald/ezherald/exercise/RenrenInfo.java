package com.herald.ezherald.exercise;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
/**
 * @author xie
 * 体育系人人的早操播报消息
 */
public class RenrenInfo{
	public static final boolean DEBUG = false;//TODO　just for debug,must be removed before release 
	private static final String url = "http://page.renren.com/601258593/fdoing"; 
	private final int SUCCESS = 1,FAILED = 0;
	private String info;
	private String date;
	private String message;//联网读取到的信息
	private SharedPreferences pref;
	private FragmentA father;
	public Activity activity;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch (msg.what){
			case SUCCESS:
				message = (String)msg.obj;
				onSuccess();
				break;
			case FAILED:
				onFailed();
			}
		}
	};
	/**
	 * @param activity 调用者的activity
	 * 构造时会从sharedPreference尝试读数据
	 */
	public RenrenInfo(Activity activity,FragmentA father){
		this.activity = activity;
		this.father = father;
		try {
			pref = activity.getApplication().getSharedPreferences("Renren", 0);
			setInfo(pref.getString("info", null));
			setDate(pref.getString("date", null));
		} catch (Exception e) {
			setInfo(null);
			setDate(null);
		}
	}
	protected void onSuccess() {
		// TODO Auto-generated method stub
		//TODO 显示的bug，进度条
		final String[] target= {"早操播报","跑操早播报"};
		
		Document document = Jsoup.parse(message);
		Elements feeds = document.getElementsByClass("list");
		Elements lists  = feeds.get(0).children();
		for(int i=1;i<lists.size();i++){//跳过第一个(i=0)
			Element feed = lists.get(i);
			String data = feed.text();
			for (int j=0;j<target.length;j++){
				if(data.indexOf(target[j])!=-1){
					info = data;//TODO 信息进一步处理
					save();
					father.show();
					return;
				}
			}
		}
		save();
	}
	protected void onFailed() {
		// TODO Auto-generated method stub
		Toast.makeText(activity, "更新失败", Toast.LENGTH_SHORT).show();
	}
	/**
	 * 更新数据
	 */
	public void update(){
		if(DEBUG){//一些测试数据
			setInfo("今天正常跑操。20 ~ 30℃。多云转晴。没起床的各位亲们赶紧起来跑操吧。");
			DateFormat fmt = SimpleDateFormat.getDateTimeInstance(); 
			setDate(fmt.format(new Date()));
			save();
			return ;
		}else{
			new Thread(){
				@Override
	    		public void run() {
					try {
						Log.w("update","updating renren");
						HttpClient client = new DefaultHttpClient();
						HttpGet get = new HttpGet(url);
						HttpResponse response = client.execute(get);
						if (response.getStatusLine().getStatusCode() != 200) {
							throw new Exception();
						}
						String message = EntityUtils.toString(response.getEntity());
						Message msg = handler.obtainMessage(SUCCESS,
								message);
						handler.sendMessage(msg);
					} catch (Exception e) {
						// TODO: handle exception
						handler.obtainMessage(FAILED).sendToTarget();
					}
				}
			}.start();
		}
		
	}
	/**
	 * 保存数据到sharedPreference;
	 */
	public void save(){
		Editor editor = pref.edit();
		editor.putString("info", getInfo());
		editor.putString("date", getDate());
		editor.commit();
	}
	
	/**
	 * @return boolean 数据是否为空
	 */
	public boolean isSet(){
		if(info == null || date == null)
			return false;
		return true;
	}
	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}