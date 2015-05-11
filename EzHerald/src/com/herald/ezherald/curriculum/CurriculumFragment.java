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
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
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
import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.IDCardAccountActivity;
import com.herald.ezherald.account.UserAccount;
import com.herald.ezherald.api.APIAccount;
import com.herald.ezherald.api.APIAccountActivity;
import com.herald.ezherald.api.APIClient;
import com.herald.ezherald.api.APIFactory;
import com.herald.ezherald.api.FailHandler;
import com.herald.ezherald.api.Status;
import com.herald.ezherald.api.SuccessHandler;


public class CurriculumFragment extends SherlockFragment {
	
	ListView listView;
	CourseAdapter adapter;
	Context context;
	CurriDBAdapter dbAdapter;
	
	String selectedItem = null;
	List<String> termList = null;
	
	private ProgressDialog progressDialog;
	private String prefName = "curriculum";
	private String pref_term = "selectedTerm";
	private String pref_prev_user = "prev_user";
	
	
	private String curri_url = "http://herald.seu.edu.cn/herald_web_service/" +
			"curriculums/%s/%s/";
	private String term_url = "http://herald.seu.edu.cn/herald_web_service/curriculums/term/";
	//API迁移 2015.5.7
	
	ActionBar bar ;
	
	ActionBar.Tab tab1 ;
	ActionBar.Tab tab2 ;
	ActionBar.Tab tab3 ;
	ActionBar.Tab tab4 ;
	ActionBar.Tab tab5 ;
	ActionBar.Tab tab6 ;
	ActionBar.Tab tab7 ;
	
	private LayoutInflater inflater;
	private ViewGroup container;
	private Bundle savedInstanceState;
	
	private AsyncTask<String ,Integer, List<String>> termTask;
	private AsyncTask<String ,Integer ,AttsAndCourses> currTask;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		context = this.getSherlockActivity();
		adapter = new CourseAdapter(context);
		dbAdapter = new CurriDBAdapter(context);
		dbAdapter.open();
		termList = new ArrayList<String>();
		
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
		
		UserAccount acount = Authenticate.getIDcardUser(context);
		
		initProgressDialog();
		
		
	}
	
	public void initProgressDialog()
	{
		progressDialog = new ProgressDialog(context);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("请稍候 ... ");
	}
	
	@Override 
	public void onDestroy()
	{
		if (termTask != null && termTask.getStatus()==AsyncTask.Status.RUNNING)
		{
			termTask.cancel(true);
		}
		if (currTask != null && currTask.getStatus()==AsyncTask.Status.RUNNING)
		{
			currTask.cancel(true);
		}
		
		
		dbAdapter.close();
		super.onDestroy();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		this.inflater = inflater;
		this.container = container;
		this.savedInstanceState = savedInstanceState;
		String cardNum = null;
		bar = getSherlockActivity(). getSupportActionBar();
		return null;
		

	}
	
	@Override
	public void onResume()
	{
		Toast.makeText(context, "onResume", Toast.LENGTH_SHORT).show();
//		SharedPreferences prefs = getSherlockActivity().getSharedPreferences(prefName, Context.MODE_PRIVATE);
//		String username = prefs.getString(pref_prev_user, null);
//		UserAccount acount = Authenticate.getIDcardUser(context);
//		if(null == acount)
//		{
//			dbAdapter.clear();
//			Toast.makeText(context, "请先登录", Toast.LENGTH_LONG).show();
//			getActivity().setContentView(setNotLoginView(inflater, container, savedInstanceState));
//		}
//		else
//		{
//			if( null == username || !username.equals(acount.getUsername()) )
//			{
//				dbAdapter.clear();
//				getSherlockActivity().getSharedPreferences(prefName, 0).edit()
//				.putString(pref_prev_user, acount.getUsername()).commit();
//			}
//
//			getActivity().setContentView(setLoginView(inflater, container, savedInstanceState));
//		}
		APIAccount apiAccount=new APIAccount(context);
		if(apiAccount.isUUIDValid())
		{
			getActivity().setContentView(setLoginView(inflater, container, savedInstanceState));
		}
		else
		{
			dbAdapter.clear();
			Toast.makeText(getActivity(),"请先登录！",Toast.LENGTH_SHORT).show();
			Intent intent=new Intent(context, APIAccountActivity.class);
			startActivity(intent);
		}
		super.onResume();
	}

	
	
	private View setNotLoginView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		
		View v;
		v = inflater.inflate(R.layout.curri_not_login, null);
		bar.removeAllTabs();
		bar.setTitle("尚未登录");
		Button login_btn = (Button) v.findViewById(R.id.curri_btn_to_login);
		login_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, IDCardAccountActivity.class);
				startActivity(intent);
			}
			
		});
		return v;
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);
//		UserAccount acount = Authenticate.getIDcardUser(context);
//		if(null == acount)
//		{
//			menu.clear();
//		}
		APIAccount apiAccount=new APIAccount(context);
		if(!apiAccount.isUUIDValid())
		{
			menu.clear();
		}

	}
	
	private View setLoginView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v;
		v = inflater.inflate(R.layout.curri_main, null);
		Log.v("TabCount", ""+bar.getTabCount());
		if(bar.getTabCount()==0)
		{
			createTabs();
		}
		
		
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
		
		
//		progressDialog.show();
		
		if(dbAdapter.isEmpty())
		{
			update();
		}
		
//		ActionBar actionBar = this.getSherlockActivity().getSupportActionBar();
//		actionBar.setTitle("课表");
//		bar = this.getSherlockActivity().getSupportActionBar();
		setTitle("课表");
		
		return v;
	}
	
	private void setTitle(String title)
	{
		bar.setTitle(title);
	}
	
	private void createTabs()
	{
		
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		tab1 = bar.newTab();
		tab2 = bar.newTab();
		tab3 = bar.newTab();
		tab4 = bar.newTab();
		tab5 = bar.newTab();
		tab6 = bar.newTab();
		tab7 = bar.newTab();
		tab1.setText("周一");
		tab2.setText("周二");
		tab3.setText("周三");
		tab4.setText("周四");
		tab5.setText("周五");
		tab6.setText("周六");
		tab7.setText("周日");
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
	
	public String getNextAtt()
	{
		CurriDataGrabber provider = new CurriDataGrabber(context);
		List<Attendance> atts = provider.getNextAtts();
//		int weekday = Tool.getWeekday();
//		int period = Tool.getCurrCoursePeriod();
//		List<Attendance> atts = dbAdapter.getNextAttByPeroid(weekday, period);
//		Collections.sort(atts);
		String str = "";
		if(atts != null)
		{
			for(Attendance att: atts)
			{
				str+=att.getAttCourseName();
			}
		}
		return str;
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
			update();
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
		try{
			termList = dbAdapter.getTerms();
			terms = termList.toArray(new String[termList.size()]);
			tmpSelect = terms[0];

			AlertDialog.Builder builder = new Builder(context);
			builder.setTitle("请选择学期")
			.setPositiveButton("选择", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
//					selectedItem = items[which];
//					Toast.makeText(context, tmpSelect, Toast.LENGTH_LONG).show();
					SharedPreferences settings = getSherlockActivity().getSharedPreferences(prefName, 0);
					Editor editor = settings.edit();
					editor.putString(pref_term, tmpSelect);
					editor.commit();
					bar.setTitle("课表:"+tmpSelect);
//					update();
//					new requestCurriculum().execute(curri_url);
					Message msg = new Message();
					msg.what = 4;
					mHandler.sendMessage(msg);
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Message msg = new Message();
					msg.what = 3;
					mHandler.sendMessage(msg);
				}
			})
			.setSingleChoiceItems(terms, 0, null)
			.setItems(terms, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
//					Toast.makeText(context, ""+which, Toast.LENGTH_LONG).show();
					tmpSelect = terms[which];
				}
				
			})
			.create().show();
		}
		catch(Exception e)
		{
			Toast.makeText(context, "请求学期失败, 请检查网络状况", Toast.LENGTH_LONG).show();
		}
		
	}


	private void APIclient_requestTerms(){
		//api迁移时代替requestTerms的函数
		APIClient apiclient_requestTerms=APIFactory.getAPIClient(context, "api/term", new SuccessHandler() {
					@Override
					public void onSuccess(String data) {
						try
						{
							List<String> terms = new ArrayList<String>();
							JSONObject jsonContent=new JSONObject(data);
							JSONArray jsonArr = jsonContent.getJSONArray("content");
							for(int i=0;i<jsonArr.length();++i)
							{
								Log.w("CURRI TERM",(String)jsonArr.get(i));

								terms.add((String) jsonArr.get(i));
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
						}

						try{
							if(terms != null)
							{
								dbAdapter.clear();
								for(String term : terms)
								{
									dbAdapter.insertTerm(term);
								}
							}
							SharedPreferences settings = getActivity().getSharedPreferences(prefName, 0);
							String term = settings.getString(pref_term, null);
							if(null == term)
							{
								createItemDialog();
							}
							Message msg = new Message();
							msg.what = TERM_REQ_COMPLETED;
							mHandler.sendMessage(msg);
						}
						catch(Exception e)
						{
							Log.e("REQUEST TERM", "error occured when requesting terms");
						}

					}
				},
				new FailHandler() {
					@Override
					public void onFail(Status status, String message) {

						Log.e("REQUEST TERM", "error occured when requesting terms");
					}
				});
		APIAccount apiAccount=new APIAccount(context);
		apiAccount.autoLogin();
		if(apiAccount.isUUIDValid()) {
			apiclient_requestTerms.addUUIDToArg();
			apiclient_requestTerms.requestWithoutCache();
			Log.v("CURRI TERM", "beginning");
		}
		else
		{
			Toast.makeText(getActivity(),"请先登录！",Toast.LENGTH_SHORT).show();
			Intent intent=new Intent(context,APIAccountActivity.class);
			context.startActivity(intent);
		}


	}
	
	private class requestTerms extends AsyncTask<String ,Integer, List<String>>
	{

		@Override
		protected List<String> doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			Log.v("CURRI TERM", "beginning");
			if (isCancelled())
			{
				return null;
			}
			
			try {
				URL url = new URL(term_url);
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
		public void onProgressUpdate(Integer... pro) 
		  {
		    //Task被取消了，不再继续执行后面的代码
		    if(isCancelled()) 
		      return;
		  }
		
		@Override
		protected void onPostExecute(List<String> terms)
		{
//			Toast.makeText(context, "term completed", Toast.LENGTH_SHORT).show();
			try{
				if(terms != null)
				{
					dbAdapter.clear();
					for(String term : terms)
					{
						dbAdapter.insertTerm(term);					
					}
				}
				SharedPreferences settings = getActivity().getSharedPreferences(prefName, 0);
				String term = settings.getString(pref_term, null);
				if(null == term)
				{
					createItemDialog();
				}
				Message msg = new Message();
				msg.what = TERM_REQ_COMPLETED;
				mHandler.sendMessage(msg);
			}
			catch(Exception e)
			{
				Log.e("REQUEST TERM", "error occured when requesting terms");
			}
			
		}
	}

	private void APIclient_requestCourse()
	{
		APIClient request_courses=APIFactory.getAPIClient(context, "api/sidebar", new SuccessHandler() {
					@Override
					public void onSuccess(String data) {
						try
						{
							JSONObject jsonObject=new JSONObject(data);
							JSONArray courseArr=jsonObject.getJSONArray("content");
							List<Course> courses = new ArrayList<Course>();
							for(int i=0;i<courseArr.length();++i)
							{
								String lecturer = courseArr.getJSONArray(i).getString(0);
								String courseName = courseArr.getJSONArray(i).getString(1);
								String weeks = courseArr.getJSONArray(i).getString(2);
								float credit = Float.parseFloat(courseArr.getJSONArray(i).getString(3));
								courses.add(new Course(courseName, lecturer, weeks, credit));
								Log.v("course", ""+courseArr.get(i));
							}
							for(Course c: courses)
							{
								dbAdapter.insertCourse(c);
							}
							APIclient_requestAttendences();
						}
						catch (JSONException e)
						{
							e.printStackTrace();
						}
					}
				},
				new FailHandler() {
					@Override
					public void onFail(Status status, String message) {
						Log.w("APICient_course_request",status.toString());
					}
				});


		APIAccount apiAccount=new APIAccount(context);
		apiAccount.autoLogin();
		if(apiAccount.isUUIDValid()) {
			Log.v("CURRI courses", "beginning");
			request_courses.addUUIDToArg();
			request_courses.requestWithCache();
		}
		else
		{
			Toast.makeText(getActivity(),"请先登录！",Toast.LENGTH_SHORT).show();
			Intent intent=new Intent(context,APIAccountActivity.class);
			context.startActivity(intent);
		}

	}

	private void APIclient_requestAttendences()
	{
		APIClient request_attendences=APIFactory.getAPIClient(context, "api/curriculum", new SuccessHandler() {
					@Override
					public void onSuccess(String data) {
						try{
							List<Attendance> attendances = new ArrayList<Attendance>();
							JSONObject jsonObject= new JSONObject(data);
							JSONArray dayAttArr=jsonObject.getJSONArray("content");
							for(int j=0;j<dayAttArr.length();++j)
							{
								Log.v("Attendance", ""+dayAttArr.getJSONArray(j));
								JSONArray dayAtt = dayAttArr.getJSONArray(j);
								Log.v("att", ""+dayAttArr.get(j));
								for(int k = 0;k<dayAtt.length();++k)
								{
									JSONArray attArr = dayAtt.getJSONArray(k);
									String courseName = attArr.getString(0);
									String courseTime =attArr.getString(1);
									CourseTimeGet courseTimeGet=new CourseTimeGet(courseTime);
									int beginWeek = courseTimeGet.GetBeginWeek();
									int endWeek = courseTimeGet.GetEndWeek();
									int beginPeriod = courseTimeGet.GetBeginPeriod();
									int endPeriod = courseTimeGet.GetEndPeroid();
									String place = attArr.getString(2);
									Attendance att = new Attendance(courseName, place,beginPeriod, endPeriod,
											beginWeek, endWeek, j+1);
								}
								for(Attendance attendence : attendances) {
									dbAdapter.insertAttendance(attendence);
								}
								Message msg=new Message();
								msg.what=CURRI_REQ_COMPLETED;
								mHandler.sendMessage(msg);
							}
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}


					}
				},
				new FailHandler() {
					@Override
					public void onFail(Status status, String message) {
						Log.w("APICient_att_request",status.toString());
					}
				});
		APIAccount apiAccount=new APIAccount(context);
		apiAccount.autoLogin();
		if(apiAccount.isUUIDValid()) {
			Log.v("CURRI attendences", "beginning");
			request_attendences.addUUIDToArg();
			request_attendences.requestWithCache();
		}
		else
		{
			Toast.makeText(getActivity(),"请先登录！",Toast.LENGTH_SHORT).show();
			Intent intent=new Intent(context,APIAccountActivity.class);
			context.startActivity(intent);
		}
	}

	public class CourseTimeGet//用于分割上课周数和每日时间段的结果，返回每项的结果的int数据 2015.5.11
	{
		int beginWeek;
		int endWeek;
		int beginPeriod;
		int endPeriod;
		public CourseTimeGet(String str)
		{
			try{
				beginWeek=Integer.parseInt(str.substring(1,str.indexOf("-")));
				endWeek=Integer.parseInt(str.substring(str.indexOf("-")+1,
						str.indexOf("]")-1));
				beginPeriod=Integer.parseInt(str.substring(str.indexOf("]")+1,
						str.lastIndexOf("-")));
				endPeriod=Integer.parseInt(str.substring(str.lastIndexOf("-")+
						1,str.length()-1));
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Log.e("CourseTimeSub","substring error");
			}
		}

		private int GetBeginWeek()
		{
			return beginWeek;
		}

		private int GetEndWeek() {
			return endWeek;
		}

		private int GetBeginPeriod() {
			return beginPeriod;
		}

		private int GetEndPeroid() {
			return endPeriod;
		}

	}


	private class requestCurriculum extends 
	AsyncTask<String ,Integer ,AttsAndCourses >
	{

		@Override
		protected AttsAndCourses doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			if(isCancelled())
				return null;
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
						JSONArray jsonArr = new JSONArray(str);
						JSONArray courseArr = (JSONArray) jsonArr.get(0);
						JSONArray dayAttArr = (JSONArray) jsonArr.get(1);
						List<Course> courses = new ArrayList<Course>();
						List<Attendance> attendances = new ArrayList<Attendance>();
						for(int i=0;i<courseArr.length();++i)
						{
							String courseName = courseArr.getJSONArray(i).getString(0);
							String lecturer = courseArr.getJSONArray(i).getString(1);
							float credit = Float.parseFloat(courseArr.getJSONArray(i).getString(2));
							String weeks = courseArr.getJSONArray(i).getString(3);
							courses.add(new Course(courseName, lecturer, weeks, credit));
							Log.v("course", ""+courseArr.get(i));
						}
						for(int j=0;j<dayAttArr.length();++j)
						{
							Log.v("Attendance", "" + dayAttArr.getJSONArray(j));
							JSONArray dayAtt = dayAttArr.getJSONArray(j);
							Log.v("att", "" + dayAttArr.get(j));
							for(int k = 0;k<dayAtt.length();++k)
							{
								JSONArray attArr = dayAtt.getJSONArray(k);
								String courseName = attArr.getString(0);
								int beginWeek = Integer.parseInt(attArr.getJSONArray(1).getString(0));
								int endWeek = Integer.parseInt(attArr.getJSONArray(1).getString(1));
								int beginPeriod = Integer.parseInt(attArr.getJSONArray(2).getString(0));
								int endPeriod = Integer.parseInt(attArr.getJSONArray(2).getString(1));
								String place = attArr.getString(3);
								Attendance att = new Attendance(courseName, place,beginPeriod, endPeriod,
										beginWeek, endWeek, j+1);
								attendances.add(att);
							}
						}
						
						return new AttsAndCourses(attendances, courses);
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
		public void onProgressUpdate(Integer... pro) 
		  {
		    //Task被取消了，不再继续执行后面的代码
		    if(isCancelled()) 
		      return;
		  }

		@Override
		protected void onPostExecute(AttsAndCourses result) {
			
			if(result!=null)
			{
				for(Attendance att: result.getAttendances())
				{
					dbAdapter.insertAttendance(att);
				}
				for(Course c: result.getCourses())
				{
					dbAdapter.insertCourse(c);
				}

			}
			else
			{
				Toast.makeText(context, "数据读取失败．．．", Toast.LENGTH_SHORT).show();
			}
			Message msg = new Message();
			msg.what = CURRI_REQ_COMPLETED;
			mHandler.sendMessage(msg);
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
	
	public void update()
	{
		progressDialog.show();
		//TODO API迁移
//		termTask = new requestTerms();
//		termTask.execute("");
		APIclient_requestTerms();

	}
	
	final int TERM_REQ_COMPLETED = 1;
	final int CURRI_REQ_COMPLETED = 2;
	final int CURRI_REQ_CANCEL = 3;
	final int CURRI_REQ_BEGIN = 4;
	
	public Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case TERM_REQ_COMPLETED:
			{
				String cardNum = null;
//				UserAccount acount = Authenticate.getIDcardUser(context);
				APIAccount acount=new APIAccount(context);
				if(!acount.isUUIDValid())
				{
					Toast.makeText(context, "请先登录", Toast.LENGTH_LONG).show();
				}
				else
				{
//					cardNum = acount.getUsername();
					SharedPreferences preferences = getActivity().getSharedPreferences(prefName, 0);
					String term = preferences.getString(pref_term, null);
//					String url = String.format(curri_url, cardNum, term);
					if(null == term)
					{
						Toast.makeText(context, "请先设置学期", Toast.LENGTH_SHORT).show();
					}
					else
					{
//						currTask = new requestCurriculum();
//						currTask.execute(url);
//						Toast.makeText(context, "thread success", Toast.LENGTH_SHORT).show();
						APIclient_requestCourse();
					}
				}

				break;
			}
			case CURRI_REQ_CANCEL:
			{
				onRefreshCompleted();
				break;
			}
				
			case CURRI_REQ_COMPLETED:{
				onRefreshCompleted();
				break;
			}
			case CURRI_REQ_BEGIN:
			{
				update();
				break;
			}
			
			}
			super.handleMessage(msg);
		}
	};
	
	private String get_curr_url()
	{
		UserAccount acount = Authenticate.getIDcardUser(context);
		String cardNum = acount.getUsername();
		SharedPreferences preferences = getActivity().getSharedPreferences(prefName, 0);
		String term = preferences.getString(pref_term, null);
		String url = String.format(curri_url, cardNum, term);
		return url;
	}
	
	public void onRefreshCompleted()
	{
		List<Attendance> al = new ArrayList<Attendance>();
		al =  dbAdapter.getAttByWeekday(getWeekday());
		adapter.setAtts(al);
		adapter.notifyDataSetChanged();
		if(progressDialog.isShowing())
		{
			progressDialog.cancel();
		}
		
		selectBar(bar);
	
	}
	
	public void onRefreshStart()
	{
		progressDialog.show();
//		dbAdapter.clear();
	}
	

	


	
}
