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
			item.setContent1(String.format("�Ѵ�%d��", times));
		else
			item.setContent1("û���ܲ�����");
		item.setContent2(String.format("ʣ��%d���Ͽ�", remain>=0?remain:0));
		return item;
	}
	
}
