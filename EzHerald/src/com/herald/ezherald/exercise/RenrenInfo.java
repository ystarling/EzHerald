package com.herald.ezherald.exercise;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;
import cn.edu.seu.herald.ws.api.exercise.Broadcast;
import cn.edu.seu.herald.ws.api.exercise.ObjectFactory;
/**
 * @author xie
 * 体育系人人的消息
 */
public class RenrenInfo{
	private String info;
	private String date;
	private SharedPreferences pref;
	public Activity activity;
	
	public RenrenInfo(Activity activity){
		this.activity = activity;
		try {
			pref = activity.getApplication().getSharedPreferences("Renren", 0);
			setInfo(pref.getString("info", null));
			setDate(pref.getString("date", null));
		} catch (Exception e) {
			setInfo(null);
			setDate(null);
		}
	}
	/**
	 * 更新数据
	 */
	public void update(){
		
		try {
			//TODO
			//ObjectFactory factory = new ObjectFactory();
			//Broadcast broadcast = factory.createBroadcast();
			//setInfo(broadcast.getInfo());
			//setDate(broadcast.getDate().toString());
			throw new Exception();
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(activity, "更新失败", Toast.LENGTH_SHORT).show();
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
	 * @return boolean
	 * 数据是否为空
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