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
			item.setContent1("��ѡ�γ̼���");
			item.setContent2(String.format("%.2f", gpa));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			item.setContent1("��û�гɼ�");
			item.setContent2("����������");
		}
		return item;
	}

}
