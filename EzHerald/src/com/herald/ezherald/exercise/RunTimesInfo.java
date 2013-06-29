package com.herald.ezherald.exercise;

import android.app.Activity;
import android.content.SharedPreferences;

public class RunTimesInfo {
	private int times;
	private int adjustTimes;
	private SharedPreferences pref;
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
	public RunTimesInfo(Activity activity){
		this.activity = activity; 
		pref = activity.getApplication().getSharedPreferences("RunTimes", 0);
		setTimes(pref.getInt("times", 0));
		setAdjustTimes(pref.getInt("adjustTimes",0));
	}
	public boolean isSet(){
		return times != 0;
	}
	public void update(){
		//TODO 更新跑操信息
	}
}
