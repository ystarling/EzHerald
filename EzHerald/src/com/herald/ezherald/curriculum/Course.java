package com.herald.ezherald.curriculum;

public class Course {

//	private int courseId;
	private String courseName;
	private String lecturer;
	private int beginWeek;
	private int endWeek;
	private int credit;
	
	public Course(String name)
	{
		courseName = name;
	}
	
	public Course()
	{
		
	}
	
	/**
	 * ¹¹Ôìº¯Êý
	 * @param id
	 * @param name
	 * @param lec
	 * @param begin
	 * @param end
	 * @param credit
	 */
	public Course(String name,String lec,int begin,int end,int credit)
	{
//		this.courseId = id;
		this.courseName = name;
		this.lecturer = lec;
		this.beginWeek = begin;
		this.endWeek = end;
		this.credit = credit;
	}
	
	
	public void setCourseName(String name)
	{
		courseName = name;
	}
	
	
	public String getCourseName()
	{
		return courseName;
	}
	
	public String getLecturer()
	{
		return this.lecturer;
	}
	
	public int getBeginWeek()
	{
		return this.beginWeek;
	}
	
	public int getEndWeek()
	{
		return this.endWeek;
	}
	
	public int getCredit()
	{
		return this.credit;
	}
	
	

}
