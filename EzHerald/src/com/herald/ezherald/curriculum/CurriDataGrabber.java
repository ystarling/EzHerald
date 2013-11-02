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
			item.setContent1("读取课表出错 ==");
			item.setContent2("您是不是还没更新课表？");
		}else if(!atts.isEmpty())
		{
			Attendance nextAtt = atts.get(0);
			item.setContent1("下节课："+nextAtt.getAttCourseName());
			item.setContent2(nextAtt.getAttPlace());	
		}
		else
		{
			item.setContent1("恭喜恭喜");
			item.setContent2("今天木有课了!");
		}
		return item;
	}


	@Override
	public MainContentGridItemObj GrabInformationObject() {
		// TODO Auto-generated method stub
		return provide();
	}
	
	
	


}
