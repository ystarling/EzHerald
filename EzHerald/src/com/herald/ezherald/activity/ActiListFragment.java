package com.herald.ezherald.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.R;
import com.herald.ezherald.academic.CustomListView;
import com.herald.ezherald.academic.CustomListView.OnRefreshListener;
import com.herald.ezherald.academic.DataTypeTransition;
import com.herald.ezherald.academic.ListFootView;

public class ActiListFragment extends SherlockFragment implements ActionBar.OnNavigationListener {
	
	private CustomListView listView;
	private ActiInfoAdapter adapter;
	private ListFootView foot;
	
	private Menu mMenu;
	
	final private int ALLACTI=0;
	final private int CONCERNEDACTI=1;
	private int ACTITYPE = ALLACTI;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRetainInstance(true);	
		setHasOptionsMenu(true);
	}
	
	@Override 
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_acti_list, menu);
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		mMenu = menu;
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.menu_acti_list_action_refresh:
			try {
				onRefreshActionStart();
				new RefreshActiList().execute(new URL("http://jwc.seu.edu.cn"));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				onRefreshActionComplete();
				e.printStackTrace();
			}
			break;
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	// 刷新菜单开始旋转
	public void onRefreshActionStart()
	{
		//REFRESHSTATE = REFRESHING ;
		MenuItem muItem = mMenu.findItem(R.id.menu_acti_list_action_refresh);
		muItem.setActionView(R.layout.academic_refresh_progress);	
		
	}
	
	// 刷新菜单停止旋转
	public void onRefreshActionComplete()
	{
		//REFRESHSTATE = REFRESHDOWN;
		MenuItem muItem = mMenu.findItem(R.id.menu_acti_list_action_refresh);
		muItem.setActionView(null);	
	}
	
	
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v;
		v = inflater.inflate(R.layout.acti_activity_list, null);
		listView = (CustomListView) v.findViewById(R.id.acti_acti_list);
		ActiInfo [] actiArr = {new ActiInfo(0,"herald","招新","2013-6-27","先声网招新了,欢迎热爱技术的同学踊跃报名","",false),
				new ActiInfo(0,"空手道社","南京内部赛","2013-6-27","南京极真空手道将在全民健身中心举办南京内部赛","",false)};
		
		adapter = new ActiInfoAdapter(getActivity());
		adapter.setActiInfoList(actiArr);
		listView.setAdapter(adapter);
		
		foot = new ListFootView(getActivity());
		foot.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//Toast.makeText(getActivity(), "more", Toast.LENGTH_SHORT).show();
				try {
					foot.startRequestData();
					new RequestActiList().execute(new URL("http://jwc.seu.edu.cn"));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		listView.addFooterView(foot.getFootView());
		
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), ""+position+"   "+id, Toast.LENGTH_SHORT).show();
				startActivity(new Intent(getActivity(), ActiInfoDetailActivity.class));
			}
			
		});
		
		listView.setonRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				try {
					onRefreshActionStart();
					new RefreshActiList().execute(new URL("http://jwc.seu.edu.cn"));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					onRefreshActionComplete();
					e.printStackTrace();
				}
				
			}
			
		});
		
		ActionBar actionBar = getActivity().getActionBar();
		SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.acti_list_action_spinner, 
				android.R.layout.simple_spinner_dropdown_item);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setListNavigationCallbacks(mSpinnerAdapter, this);
		
		
		return v;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		
		Log.v("ClubList", "come in");
		
		switch(itemPosition)
		{
		case 0:
			ACTITYPE = ALLACTI;
			return true;
		case 1:
			ACTITYPE = CONCERNEDACTI;
			return true;
		case 2:
			Log.v("ClubList", "is here");
			startActivity(new Intent(getActivity(),ClubListActivity.class));
			
			return true;
		case 3:
			Log.v("ClubList", "is here");
			startActivity(new Intent(getActivity(),ClubListActivity.class));
			
			return true;
		
		}
		return false;
	}
	
	private class RefreshActiList extends AsyncTask<URL,Integer,List<ActiInfo>>
	{

		@Override
		protected List<ActiInfo> doInBackground(URL... params) {
			// TODO Auto-generated method stub
			List<ActiInfo> actiList = new ArrayList<ActiInfo>();
			int response = -1;
			InputStream in;
			URL url = params[0];
			URLConnection conn;
			try {
				conn = url.openConnection();
				if (!( conn instanceof HttpURLConnection ) )
				{
					throw new IOException("NOT AN HTTP CONNECTION");
				}
				else
				{
					HttpURLConnection httpConn = (HttpURLConnection) conn;
					httpConn.setAllowUserInteraction(false);
					httpConn.setInstanceFollowRedirects(true);
					httpConn.setRequestMethod("GET");
					httpConn.connect();
					response = httpConn.getResponseCode();
					if( response == HttpURLConnection.HTTP_OK)
					{
						in = httpConn.getInputStream();
						String str = DataTypeTransition.InputStreamToString(in);
						//Log.v("Net test", str);
						actiList.add(new ActiInfo(0,"学生会","招新","2013-6-30",
								"东南大学学生会招新了，学生会致力于服务学生日常生活...","",true) );
						actiList.add(new ActiInfo(0,"篮协","东大全明星赛","2013-6-30",
								"东大篮协将举办一场梅园vs桃园的全明星赛，明星由选举产生...","",true) );
						return actiList;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(List<ActiInfo> result)
		{
			if (result != null)
			{
				adapter.setActiInfoList(result);
				adapter.notifyDataSetChanged();
				listView.onRefreshComplete();
				onRefreshActionComplete();
			}
			
		}
		
	}
	
	private class RequestActiList extends AsyncTask<URL,Integer,List<ActiInfo>>
	{

		@Override
		protected List<ActiInfo> doInBackground(URL... params) {
			// TODO Auto-generated method stub
			List<ActiInfo> actiList = new ArrayList<ActiInfo>();
			int response = -1;
			InputStream in;
			URL url = params[0];
			URLConnection conn;
			try {
				conn = url.openConnection();
				if (!( conn instanceof HttpURLConnection ) )
				{
					throw new IOException("NOT AN HTTP CONNECTION");
				}
				else
				{
					HttpURLConnection httpConn = (HttpURLConnection) conn;
					httpConn.setAllowUserInteraction(false);
					httpConn.setInstanceFollowRedirects(true);
					httpConn.setRequestMethod("GET");
					httpConn.connect();
					response = httpConn.getResponseCode();
					if( response == HttpURLConnection.HTTP_OK)
					{
						in = httpConn.getInputStream();
						//String str = DataTypeTransition.InputStreamToString(in);
						actiList.add(new ActiInfo(0,"学生会","招新","2013-6-30",
								"东南大学学生会招新了，学生会致力于服务学生日常生活...","",true) );
						actiList.add(new ActiInfo(0,"篮协","东大全明星赛","2013-6-30",
								"东大篮协将举办一场梅园vs桃园的全明星赛，明星由选举产生...","",true) );
						return actiList;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		 
		@Override
		protected void onPostExecute(List<ActiInfo>result)
		{
			adapter.addActiInfoList(result);
			adapter.notifyDataSetChanged();
			foot.endRequestData();
			listView.onRequestComplete();
			
		}
	}

}
