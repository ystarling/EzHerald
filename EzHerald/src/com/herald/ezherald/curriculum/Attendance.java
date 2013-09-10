package com.herald.ezherald.curriculum;

public class Attendance implements Comparable {
	
//	private Course course;
	private String courseName;
	private String place;
	private int periodBegin;
	private int periodEnd;
	private int weekBegin;
	private int weekEnd;
	private int weekDay;
	
	public Attendance(String cn,String p,int pb,int pe,int wb, int we,int wd)
	{
		this.courseName = cn;
		this.place = p;
		this.periodBegin = pb;
		this.periodEnd = pe;
		this.weekBegin = wb;
		this.weekEnd = we;
		this.weekDay = wd;
	}
	
	public int getAttPeriodBegin()
	{
		return this.periodBegin;
	}
	
	public int getAttPeriodEnd()
	{
		return this.periodEnd;
	}
	
	public String getAttPeriod()
	{
		return this.periodBegin+" - "+this.periodEnd;
	}
	
	public String getAttCourseName()
	{
		return this.courseName;
	}
	
	public String getAttPlace()
	{
		return this.place;
	}
	
	public int getAttWeekBegin()
	{
		return this.weekBegin;
	}
	
	public int getAttWeekEnd()
	{
		return this.weekEnd;
	}
	
	public String getAttWeeks()
	{
		return this.weekBegin+" - "+this.weekEnd;
	}
	
	public int getAttWeekday()
	{
		return this.weekDay;
	}

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return this.periodBegin - ((Attendance) arg0).periodBegin;
	}

	

}
