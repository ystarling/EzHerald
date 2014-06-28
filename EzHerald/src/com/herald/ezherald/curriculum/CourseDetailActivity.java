package com.herald.ezherald.curriculum;

import java.util.List;

import android.annotation.SuppressLint;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.R;

public class CourseDetailActivity extends SherlockActivity {
	
	CourseDetailAdapter adapter;
	ListView listView ;
	CurriDBAdapter dbAdapter;
	
	private Context context;
	
	
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.curri_course_detail);
		
		Context context = this;

		adapter = new CourseDetailAdapter(this);
		dbAdapter = new CurriDBAdapter(this);
		dbAdapter.open();
		
		listView = (ListView) this.findViewById(R.id.curri_course_deail_list);
		listView.setAdapter(adapter);
		
		Bundle bundle = this.getIntent().getExtras();
		String courseName = bundle.getString("courseName");
		List<Course> courses = dbAdapter.getCourse(courseName);
		adapter.setCourses(courses);
		
		ActionBar actionBar = getSupportActionBar();
//		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setTitle("课程详情");
		actionBar.setDisplayHomeAsUpEnabled(true);
		
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		dbAdapter.close();
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = new MenuInflater(this);
//		inflater.inflate(R.leftMenu.menu_acti_detail, leftMenu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
//	@Override
//	public void onPause()
//	{
//		dbAdapter.close();
//	}
//	
//	@Override
//	public void onResume()
//	{
//		dbAdapter.open();
//	}

	

}
