package com.herald.ezherald.freshman;

import com.herald.ezherald.mainframe.MainContentGridItemObj;
import com.herald.ezherald.mainframe.MainContentInfoGrabber;

public class FreshmanGrabber implements MainContentInfoGrabber {

	@Override
	public MainContentGridItemObj GrabInformationObject() {
		// TODO Auto-generated method stub
		MainContentGridItemObj item = new MainContentGridItemObj();
		item.setContent1("���ѧϰ�����֣��ʴ�");
		item.setContent2("����У԰ָ��");
		return item;
	}

}
