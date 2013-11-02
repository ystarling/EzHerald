package com.herald.ezherald.curriculum;

import java.util.Collections;
import java.util.List;

import com.herald.ezherald.mainframe.MainContentGridItemObj;
import com.herald.ezherald.mainframe.MainContentInfoGrabber;

import android.content.Context;

public class CurriDataGrabber implements MainContentInfoGrabber {
	
	CurriDBAdapter dbAdapter;
	
	public CurriDataGrabber(Context context)
	{
		dbAdapter = new CurriDBAdapter(context);
	}
	
	
	public List<Attendance> getNextAtts()
	{
		dbAdapter.open();
		int weekday = Tool.getWeekday();
		int period = Tool.getCurrCoursePeriod();
		List<Attendance> atts = dbAdapter.getNextAttByPeroid(weekday, period);
		Collections.sort(atts);
		dbAdapter.close();
		return atts;
	}
	
	public List<Attendance> getAttsByWeekday()
	{
		dbAdapter.open();
		int weekday = Tool.getWeekday();
		List<Attendance> atts = dbAdapter.getAttByWeekday(weekday);
		Collections.sort(atts);
		dbAdapter.close();
		return atts;
	}
	
	public MainContentGridItemObj provide()
	{
		List<Attendance> atts = getNextAtts();
		MainContentGridItemObj item = new MainContentGridItemObj();
		if(null == atts)
		{
			item.setContent1("��ȡ�α���� ==");
			item.setContent2("���ǲ��ǻ�û���¿α�");
		}else if(!atts.isEmpty())
		{
			Attendance nextAtt = atts.get(0);
			item.setContent1("�½ڿΣ�"+nextAtt.getAttCourseName());
			item.setContent2(nextAtt.getAttPlace());	
		}
		else
		{
			item.setContent1("��ϲ��ϲ");
			item.setContent2("����ľ�п���!");
		}
		return item;
	}


	@Override
	public MainContentGridItemObj GrabInformationObject() {
		// TODO Auto-generated method stub
		return provide();
	}
	
	
	


}
