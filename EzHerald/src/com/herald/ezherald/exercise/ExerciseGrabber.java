package com.herald.ezherald.exercise;

import android.content.Context;

import com.herald.ezherald.mainframe.MainContentFlowItemAdapter;
import com.herald.ezherald.mainframe.MainContentGridItemAdapter;
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
		item.setContent1(String.format("�Ѵ�%d��", times));
		item.setContent2(String.format("ʣ��%d���Ͽ�", remain>=0?remain:0));
		return item;
	}
	
}
