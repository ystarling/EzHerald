package com.herald.ezherald.freshman;

import com.herald.ezherald.mainframe.MainContentGridItemObj;
import com.herald.ezherald.mainframe.MainContentInfoGrabber;

public class FreshmanGrabber implements MainContentInfoGrabber {

	@Override
	public MainContentGridItemObj GrabInformationObject() {
		// TODO Auto-generated method stub
		MainContentGridItemObj item = new MainContentGridItemObj();
		item.setContent1("生活，学习，娱乐，问答");
		item.setContent2("尽在校园指南");
		return item;
	}

}
