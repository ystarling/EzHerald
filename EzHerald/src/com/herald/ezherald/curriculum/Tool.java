package com.herald.ezherald.curriculum;

import java.util.Calendar;
import java.util.Date;

public class Tool {
	
	public static int getTodayMinutes()
	{
		Date date = new Date();
		int h = date.getHours();
		int m = date.getMinutes();
		return h*60 + m ;
	}
	
	public static int getCurrCoursePeriod()
	{
		int minutes = getTodayMinutes();
		if( minutes < 8*60 )
			return 0;
		else if(minutes<8*60+45  )
			return 1;
		else if(minutes<9*60+35)
			return 2;
		else if( minutes<10*60+30 )
			return 3;
		else if( minutes<11*60+25)
			return 4;
		else if(minutes<14*60)
			return 5;
		else if(minutes<14*60+45)
			return 6;
		else if(minutes<15*60+35)
			return 7;
		else if(minutes<16*60+30)
			return 8;
		else if(minutes<17*60+20)
			return 9;
		else if(minutes<18*60+30)
			return 10;
		else if(minutes<19*60+15)
			return 11;
		else if(minutes<20*60+15)
			return 12;
		else if(minutes<21*60+5)
			return 13;
		else
			return 14;
	}
	
	public static int getWeekday()
	{
		Calendar cal = Calendar.getInstance();
		int weekday = cal.get(Calendar.DAY_OF_WEEK);
		if(weekday == 1)
		{
			return 7;
		}
		else
		{
			return weekday-1;
		}
	}
	
	public static String getWeekdayStr()
	{
		String day = "";
		switch(Tool.getWeekday()){
		case 1:
			day = "周一";
			break;
		case 2:
			day = "周二";
			break;
		case 3:
			day = "周三";
			break;
		case 4:
			day = "周四";
			break;
		case 5:
			day = "周五";
			break;
		case 6:
			day = "周六";
			break;
		case 7:
			day = "周日";
			break;
		}
		return day;
	}
	


}
