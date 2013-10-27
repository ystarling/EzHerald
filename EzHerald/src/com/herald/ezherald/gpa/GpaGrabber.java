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
		MainContentGridItemObj item = new MainContentGridItemObj();
		float gpa;
		try {
			gpa = gpaInfo.calcAverage();
			item.setContent1("所选课程绩点");
			item.setContent2(String.format("%.2f", gpa));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			item.setContent1("还没有成绩");
			item.setContent2("点击进入更新");
		}
		return item;
	}

}
