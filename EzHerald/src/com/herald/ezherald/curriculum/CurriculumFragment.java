package com.herald.ezherald.curriculum;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.R;
import com.herald.ezherald.academic.DataTypeTransition;
import com.herald.ezherald.activity.ActiInfoDetail;
import com.herald.ezherald.exercise.FragmentA;
import com.herald.ezherald.exercise.FragmentB;
import com.herald.ezherald.exercise.FragmentC;


public class CurriculumFragment extends SherlockFragment {
	
	ListView listView;
	CourseAdapter adapter;
	Context context;
	CurriDBAdapter dbAdapter;
	
	String selectedItem = null;
	List<String> termList = null;
	
	private ProgressDialog progressDialog;
	
	ActionBar bar ;
	
	ActionBar.Tab tab1 ;
	ActionBar.Tab tab2 ;
	ActionBar.Tab tab3 ;
	ActionBar.Tab tab4 ;
	ActionBar.Tab tab5 ;
	ActionBar.Tab tab6 ;
	ActionBar.Tab tab7 ;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
		context = this.getActivity();
		adapter = new CourseAdapter(context);
		dbAdapter = new CurriDBAdapter(context);
		termList = new ArrayList<String>();
		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v;
		v = inflater.inflate(R.layout.curri_main, null);
		
		createTabs();
		
		listView = (ListView) v.findViewById(R.id.curri_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Attendance att = (Attendance) adapter.getItem(arg2);
				String courseName = att.getAttCourseName();
				Intent intent = new Intent(context,CourseDetailActivity.class);
				intent.putExtra("courseName", courseName);
				startActivity(intent);
			}
			
		});
		
		progressDialog = new ProgressDialog(context);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("Please wait ... ");
//		progressDialog.show();
		
		dbAdapter.open();
		if(dbAdapter.isEmpty())
		{
//			progressDialog.show();
//			new requestCurriculum().execute("http://jwc.seu.edu.cn");
			new requestTerms().execute("");
		}
		dbAdapter.close();
		
		ActionBar actionBar = this.getSherlockActivity().getSupportActionBar();
		actionBar.setTitle("课表");
		
		
		return v;
	}
	
	private void createTabs()
	{
		bar = getSherlockActivity(). getSupportActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		tab1 = bar.newTab();
		tab2 = bar.newTab();
		tab3 = bar.newTab();
		tab4 = bar.newTab();
		tab5 = bar.newTab();
		tab6 = bar.newTab();
		tab7 = bar.newTab();
		tab1.setText("一");
		tab2.setText("二");
		tab3.setText("三");
		tab4.setText("四");
		tab5.setText("五");
		tab6.setText("六");
		tab7.setText("七");
		tab1.setTabListener(new MyTabListener());
		tab2.setTabListener(new MyTabListener());
		tab3.setTabListener(new MyTabListener());
		tab4.setTabListener(new MyTabListener());
		tab5.setTabListener(new MyTabListener());
		tab6.setTabListener(new MyTabListener());
		tab7.setTabListener(new MyTabListener());
		bar.addTab(tab1);
		bar.addTab(tab2);
		bar.addTab(tab3);
		bar.addTab(tab4);
		bar.addTab(tab5);
		bar.addTab(tab6);
		bar.addTab(tab7);
		selectBar(bar);
	}
	
	private int getWeekday()
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
	
	private void selectBar(ActionBar bar)
	{
		switch(getWeekday())
		{
		case 1:
			bar.selectTab(tab1);
			break;
		case 2:
			bar.selectTab(tab2);
			break;
		case 3:
			bar.selectTab(tab3);
			break;
		case 4:
			bar.selectTab(tab4);
			break;
		case 5:
			bar.selectTab(tab5);
			break;
		case 6:
			bar.selectTab(tab6);
			break;
		case 7:
			bar.selectTab(tab7);
			break;	
		}
	}
	
	
	private class MyTabListener implements ActionBar.TabListener
    {
		
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			dbAdapter.open();
			List<Attendance> al = new ArrayList<Attendance>();
			switch(tab.getPosition()){
				case 0:
					al =  dbAdapter.getAttByWeekday(1);
					break;
				case 1:
					al =  dbAdapter.getAttByWeekday(2);
					break;
				case 2:
					al =  dbAdapter.getAttByWeekday(3);
					break;
				case 3:
					al =  dbAdapter.getAttByWeekday(4);
					break;
				case 4:
					al =  dbAdapter.getAttByWeekday(5);
					break;
				case 5:
					al =  dbAdapter.getAttByWeekday(6);
					break;
				case 6:
					al =  dbAdapter.getAttByWeekday(7);
					break;
				default:
					Log.w("error","no such a tag in evercise");
					return;
			}
			
			dbAdapter.close();
			adapter.setAtts(al);
			adapter.notifyDataSetChanged();
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}
    }
	
	@Override 
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_curri_main, menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.menu_curri_action_refresh:
			progressDialog.show();
//			new requestCurriculum().execute("http://jwc.seu.edu.cn");
			new requestTerms().execute("");
			Toast.makeText(context, "refresh", Toast.LENGTH_SHORT).show();
			break;
		case R.id.menu_curri_action_set:
			createItemDialog();
			break;
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	String tmpSelect = null;
	String [] terms = null;
	protected void createItemDialog()
	{
//		final String [] items = {"13-14-1","12-13-3"};
//		terms = (String[]) termList.toArray();
		termList.toArray(terms);
		tmpSelect = terms[0];
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("请选择学期")
		.setPositiveButton("选择", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
//				selectedItem = items[which];
				Toast.makeText(context, tmpSelect, Toast.LENGTH_LONG).show();
				SharedPreferences settings = getActivity().getSharedPreferences("curriculum", 0);
				Editor editor = settings.edit();
				editor.putString("selectedItem", tmpSelect);
				editor.commit();
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		})
		.setSingleChoiceItems(terms, 0, null)
		.setItems(terms, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
//				Toast.makeText(context, ""+which, Toast.LENGTH_LONG).show();
				tmpSelect = terms[which];
			}
			
		})
		.create().show();
	}
	
	private class requestTerms extends AsyncTask<String ,Integer, List<String>>
	{

		@Override
		protected List<String> doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			Log.v("CURRI TERM", "beginning");
			try {
				URL url = new URL("http://herald.seu.edu.cn/herald_web_service/curriculum/term/");
				int response = -1;
				InputStream in = null;
				URLConnection conn;
				conn = url.openConnection();
				if (!(conn instanceof HttpURLConnection)) {
					throw new IOException("NOT AN HTTP CONNECTION");
				}
				else
				{
					HttpURLConnection httpConn = (HttpURLConnection) conn;
					httpConn.setAllowUserInteraction(false);
					httpConn.setInstanceFollowRedirects(true);
					httpConn.setRequestMethod("GET");
					httpConn.setConnectTimeout(5000);
					Log.w("CURRI TERM", "before connect");
					httpConn.connect();
					response = httpConn.getResponseCode();
					Log.w("CURRI TERM", "after connect");
					if (response == HttpURLConnection.HTTP_OK) {
						in = httpConn.getInputStream();
						String str = DataTypeTransition.InputStreamToString(in);
						
						List<String> terms = new ArrayList<String>();
						JSONArray jsonArr = new JSONArray(str);
						for(int i=0;i<jsonArr.length();++i)
						{
							Log.w("CURRI TERM",(String)jsonArr.get(i));
							
							terms.add((String) jsonArr.get(i));
						}
						
						return terms;
					}
				}
				
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(List<String> terms)
		{
			Toast.makeText(context, "term completed", Toast.LENGTH_SHORT).show();
			if(terms != null)
			{
				dbAdapter.open();
				for(String term : terms)
				{
					dbAdapter.insertTerm(term);
				}
				dbAdapter.close();
			}
//			progressDialog.cancel();
		}
	}
	
	private class requestCurriculum extends 
	AsyncTask<String ,Integer ,AttsAndCourses >
	{

		@Override
		protected AttsAndCourses doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			try {
				URL url = new URL(arg0[0]);
				int response = -1;
				InputStream in = null;
				URLConnection conn;
				conn = url.openConnection();
				if (!(conn instanceof HttpURLConnection)) {
					throw new IOException("NOT AN HTTP CONNECTION");
				}
				else
				{
					HttpURLConnection httpConn = (HttpURLConnection) conn;
					httpConn.setAllowUserInteraction(false);
					httpConn.setInstanceFollowRedirects(true);
					httpConn.setRequestMethod("GET");
					httpConn.setConnectTimeout(5000);
					httpConn.connect();
					response = httpConn.getResponseCode();
					if (response == HttpURLConnection.HTTP_OK) {
						in = httpConn.getInputStream();
						String str = DataTypeTransition.InputStreamToString(in);
						
						List<Attendance> attendances = new ArrayList<Attendance>();
						attendances.add(new Attendance("高数","j8-111",1,2,1,16,2));
						attendances.add(new Attendance("物理","j6-101",1,2,1,16,1));
						attendances.add(new Attendance("英语","j8-111",3,5,1,16,5));
						attendances.add(new Attendance("数学分析","j8-111",1,2,1,16,4));
						attendances.add(new Attendance("c++","j8-111",8,9,1,16,3));
						attendances.add(new Attendance("数据库理论","j8-111",1,2,1,16,3));
						List<Course> courses = new ArrayList<Course>();
						courses.add(new Course("高数","周华健",1,16,5));
						courses.add(new Course("物理","张道宇",1,16,5));
						courses.add(new Course("英语","金晶",1,16,5));
						courses.add(new Course("数学分析","张福宝",1,16,5));
						courses.add(new Course("c++","于文雪",1,16,5));
						courses.add(new Course("数据库理论","章某",1,16,5));
						
						
						return new AttsAndCourses(attendances, courses);
					}
				}
				
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(AttsAndCourses result) {
			
			if(result!=null)
			{
				dbAdapter.open();
				for(Attendance att: result.getAttendances())
				{
					dbAdapter.insertAttendance(att);
				}
				for(Course c: result.getCourses())
				{
					dbAdapter.insertCourse(c);
				}
				dbAdapter.close();
				selectBar(bar);
				progressDialog.cancel();
			}
			else
			{
				Toast.makeText(context, "数据读取失败．．．", Toast.LENGTH_SHORT).show();
			}
			
			
		}
		
	}
	
	private class AttsAndCourses
	{
		List<Attendance> atts;
		List<Course> courses;
		
		public AttsAndCourses(List<Attendance> a,List<Course> c)
		{
			atts = a;
			courses = c;
		}
		
		public List<Attendance> getAttendances()
		{
			return atts;
		}
		
		public List<Course> getCourses()
		{
			return courses;
		}
		
	}
	

	


}
