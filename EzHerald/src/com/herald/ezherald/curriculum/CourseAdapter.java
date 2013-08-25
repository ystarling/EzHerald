package com.herald.ezherald.curriculum;

import java.util.ArrayList;
import java.util.List;

import cn.edu.seu.herald.ws.api.curriculum.Curriculum;

import com.herald.ezherald.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class CourseAdapter extends BaseAdapter {
	
	List<Attendance> courseList;
	Context context;
	LayoutInflater inflater;
	
	public CourseAdapter(Context c)
	{
		context = c;
		courseList = new ArrayList<Attendance>();
		inflater = LayoutInflater.from(context);
	}
	
	public void setAtts(List<Attendance> cl)
	{
		courseList = cl;
	}
	
	public void addAttendance(Attendance c)
	{
		courseList.add(c);
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
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View courseItemView = inflater.inflate(R.layout.curri_course_item, null);
		TextView tvCourseName = (TextView) courseItemView.findViewById(R.id.curri_course_name); 
		TextView tvAttPeriod = (TextView) courseItemView.findViewById(R.id.curri_course_period);
		TextView tvAttWeeks = (TextView) courseItemView.findViewById(R.id.curri_course_weeks);
		TextView tvAttPlace = (TextView) courseItemView.findViewById(R.id.curri_course_place);
		tvCourseName.setText(courseList.get(position).getAttCourseName());
		tvAttPeriod.setText(courseList.get(position).getAttPeriod());
		tvAttWeeks.setText(courseList.get(position).getAttWeeks());
		tvAttPlace.setText(courseList.get(position).getAttPlace() );
		
		
		return courseItemView;
	}

	

}
