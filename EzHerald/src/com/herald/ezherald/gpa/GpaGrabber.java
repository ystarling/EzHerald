package com.herald.ezherald.gpa;
import android.content.Context;

import com.herald.ezherald.mainframe.MainContentGridItemObj;
import com.herald.ezherald.mainframe.MainContentInfoGrabber;
public class GpaGrabber implements MainContentInfoGrabber{

	private GpaInfo gpaInfo;
	public GpaGrabber(Context context){
		gpaInfo = new GpaInfo(context);
	}
	@Override
	public MainContentGridItemObj GrabInformationObject() {
		// TODO Auto-generated method stub
		final float eps = (float) 1e-8;
		MainContentGridItemObj item = new MainContentGridItemObj();
		float gpa = gpaInfo.calcAverage();
		if( Float.isNaN(gpa) || gpa < eps ){
			item.setContent1("还没有成绩");
			item.setContent2("点击进入更新");
		} else {
			item.setContent1("所选课程绩点");
			item.setContent2(String.format("%.2f", gpa));
		}
		return item;
	}

}
