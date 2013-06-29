package com.herald.ezherald.exercise;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

/**
 * @author xie
 * 跑操次数的信息
 */
public class RunTimesInfo {
	private int times;
	private int adjustTimes;
	private SharedPreferences pref;
	private Editor editor;
	private Activity activity;
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public int getAdjustTimes() {
		return adjustTimes;
	}
	public void setAdjustTimes(int adjustTimes) {
		this.adjustTimes = adjustTimes;
	}
	/**
	 * @param activity 调用者的Activity
	 * 构造时会尝试从sharedPreference读取数据
	 */
	public RunTimesInfo(Activity activity){
		this.activity = activity; 
		pref = activity.getApplication().getSharedPreferences("RunTimes", 0);
		setTimes(pref.getInt("times", 0));
		setAdjustTimes(pref.getInt("AdjustTimes",0));
	}
	/**
	 * @return Boolean 是否已经有数据
	 */
	public boolean isSet(){
		return times != 0;
	}
	/**
	 * 更新数据
	 */
	public void update(){
		try {
//			setAdjustTimes(pref.getInt("AdjustTimes",0));
//			ObjectFactory factory = new ObjectFactory();
//			RunTimesData runTimesData = factory.createRunTimesData();
//			setTimes(runTimesData.getTimes().intValue());
			throw new Exception();
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(activity, "更新失败", Toast.LENGTH_SHORT).show();
		}
	}
	/**
	 * 保存数据到sharedPreference
	 */
	public void save(){
		editor = pref.edit();
		editor.putInt("RunTimes", getAdjustTimes());
		editor.putInt("AdjustTimes", getTimes());
		editor.commit();
	}
}
