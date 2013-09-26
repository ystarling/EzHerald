package com.herald.ezherald.curriculum;

import java.util.ArrayList;
import java.util.List;

import com.herald.ezherald.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class CourseDetailAdapter extends BaseAdapter {
	
	private List<Course> courseList;
	private Context context;
	LayoutInflater inflater;
	
	
	public CourseDetailAdapter(Context c)
	{
		context = c;
		courseList = new ArrayList<Course>();
		inflater = LayoutInflater.from(context);
	}
	
	public void addCourse(Course c)
	{
		courseList.add(c);
	}
	
	public void setCourses(List<Course> cl)
	{
		courseList = cl;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return courseList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return courseList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.curri_course_list_item, null);
		TextView tvCourseName = (TextView) view.findViewById(R.id.curri_course_item_name);
		TextView tvLecturer = (TextView) view.findViewById(R.id.curri_course_item_lecturer);
		TextView tvWeeks = (TextView) view.findViewById(R.id.curri_course_item_weeks);
		TextView tvCredit = (TextView) view.findViewById(R.id.curri_course_item_credit);
		
		Course course = courseList.get(arg0);
		tvCourseName.setText(course.getCourseName());
		tvLecturer.setText(course.getLecturer());
		tvWeeks.setText(course.getWeeks());
		tvCredit.setText(""+course.getCredit());
		
		return view;
	}


}
