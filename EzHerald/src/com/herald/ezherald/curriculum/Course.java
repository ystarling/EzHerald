package com.herald.ezherald.curriculum;

public class Course {

//	private int courseId;
	private String courseName;
	private String lecturer;
	private String weeks;
	private float credit;
	
	public Course(String name)
	{
		courseName = name;
	}
	
	public Course()
	{
		
	}
	
	/**
	 * 构造函数
	 * @param id
	 * @param name
	 * @param lec
	 * @param weeks
	 * @param credit
	 */
	public Course(String name,String lec,String weeks,float credit)
	{
//		this.courseId = id;
		this.courseName = name;
		this.lecturer = lec;
		this.weeks = weeks;
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
	

	
	public float getCredit()
	{
		return this.credit;
	}
	
	public String getWeeks()
	{
		return this.weeks;
	}
	

}
