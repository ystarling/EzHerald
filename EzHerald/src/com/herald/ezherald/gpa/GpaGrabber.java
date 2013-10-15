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
		if(gpa < eps ){
			item.setContent1("��û�гɼ�");
			item.setContent2("�������");
		} else {
			item.setContent1("��ѡ�γ̼���");
			item.setContent2(Float.toString(gpa));
		}
		return item;
	}

}
