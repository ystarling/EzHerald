package com.herald.ezherald.exercise;

import android.content.Context;

import com.herald.ezherald.mainframe.MainContentGridItemObj;
import com.herald.ezherald.mainframe.MainContentInfoGrabber;

public class ExerciseGrabber implements MainContentInfoGrabber {
	private RunTimes runTimes;
	public ExerciseGrabber(Context context) {
		runTimes = new RunTimes(context);
	}
	@Override
	public MainContentGridItemObj GrabInformationObject() {
		// TODO Auto-generated method stub
		int times = runTimes.getTimes();
		int remain = runTimes.getRemainDays();
		MainContentGridItemObj item = new MainContentGridItemObj();
		if(times >= 0)
			item.setContent1(String.format("已打卡%d次", times));
		else
			item.setContent1("没有跑操数据");
		item.setContent2(String.format("剩余%d天上课", remain>=0?remain:0));
		return item;
	}
	
}
