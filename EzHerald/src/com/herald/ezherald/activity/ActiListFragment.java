package com.herald.ezherald.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
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

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.R;
import com.herald.ezherald.academic.CustomListView;
import com.herald.ezherald.academic.CustomListView.OnRefreshListener;
import com.herald.ezherald.academic.DataTypeTransition;
import com.herald.ezherald.academic.ListFootView;
import com.herald.ezherald.mainframe.MainContentGridItemObj;

public class ActiListFragment extends SherlockFragment implements ActionBar.OnNavigationListener {
	
	private CustomListView listView;
	private ActiInfoAdapter adapter;
	private ListFootView foot;
	
	private ProgressDialog progressDialog;
	
	private Menu mMenu;
	
	private Activity context;
	
	final private int ALLACTI=0;
	private int ACTITYPE = ALLACTI;
	

	ActiDBAdapter DBAdapter;
	
	private AsyncTask<URL,Integer,List<ActiInfo>> requestTask;
	private AsyncTask<URL,Integer,List<ActiInfo>> refreshTask;
	
	private final String noActivityHint = "NOACTIVITYCANGET";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRetainInstance(true);	
		setHasOptionsMenu(true);
		
		context = getActivity();
		initProgressDialog();
		
		DBAdapter = new ActiDBAdapter(context);
		DBAdapter.open();
	}
	
	private void initProgressDialog()
	{
		progressDialog = new ProgressDialog(context);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("请稍候 ... ");
	}
	
	@Override
	public void onDestroy()
	{
		if(requestTask!=null && requestTask.getStatus()==AsyncTask.Status.RUNNING)
		{
			requestTask.cancel(true);
		}
		if(refreshTask!=null && refreshTask.getStatus()==AsyncTask.Status.RUNNING)
		{
			refreshTask.cancel(true);
		}
		
		if(DBAdapter != null)
		{
			DBAdapter.close();
		}
		super.onDestroy();
	}
	
//	@Override
//	public void onPause()
//	{
//		DBAdapter.close();
//	}
//	
//	@Override
//	public void onResume()
//	{
//		DBAdapter.open();
//	}
	
	
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

				new RefreshActiList().execute(new URL(getResources().getString(R.string.acti_url_activity_list)));

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
		progressDialog.show();
	}
	
	// 刷新菜单停止旋转
	public void onRefreshActionComplete()
	{
		//REFRESHSTATE = REFRESHDOWN;
		MenuItem muItem = mMenu.findItem(R.id.menu_acti_list_action_refresh);
		muItem.setActionView(null);	
		listView.onRefreshComplete();
		progressDialog.cancel();
	}
	
	
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v;
		v = inflater.inflate(R.layout.acti_activity_list, null);
		listView = (CustomListView) v.findViewById(R.id.acti_acti_list);
		
		adapter = new ActiInfoAdapter(getActivity());
		//adapter.setActiInfoList(actiArr);
		listView.setAdapter(adapter);
		
//		new GabberTestTask().execute();
		
		foot = new ListFootView(getActivity());
		foot.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//Toast.makeText(getActivity(), "more", Toast.LENGTH_SHORT).show();
				try {
					
					if(ACTITYPE == ALLACTI)
					{
						if(adapter.getLastActiId() != null)
						{
							foot.startRequestData();
							new RequestActiList().execute(new URL(getResources().getString(R.string.acti_url_more_activity)
									+adapter.getLastActiId()));
						}
						
					}
					
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
//				Toast.makeText(getActivity(), ""+position+"   "+id, Toast.LENGTH_SHORT).show();
				
				ActiInfo actiInfo = (ActiInfo) adapter.getItem((int)id);
//				Toast.makeText(context, ""+actiInfo.getId(), Toast.LENGTH_SHORT).show();
				boolean isVote = actiInfo.checkIsVote();
				Intent intent = new Intent(context,ActiInfoDetailActivity.class);
				intent.putExtra("clubName", actiInfo.getClubName());
				intent.putExtra("title", actiInfo.getActiTitle());
				Log.d("Intent", "acti title: "+actiInfo.getActiTitle());
				intent.putExtra("date", actiInfo.getActiPubTime());
				intent.putExtra("startTime", actiInfo.getStartTime());
				intent.putExtra("endTime", actiInfo.getEndTime());
				intent.putExtra("actiId", actiInfo.getId());
				intent.putExtra("clubId", actiInfo.getClubId());

				intent.putExtra("place", actiInfo.getPlace());
				intent.putExtra("picName", actiInfo.getActiPicName());
				intent.putExtra("iconName", actiInfo.getClubIconName() );

				intent.putExtra("isVote", actiInfo.checkIsVote());
				startActivity(intent);
				
				
			}
			
		});
		
		listView.setonRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				try {
					onRefreshActionStart();

					new RefreshActiList().execute(new URL(getResources().getString(R.string.acti_url_activity_list)));
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					onRefreshActionComplete();
					e.printStackTrace();
				}
				
			}
			
		});
		
		ActionBar actionBar = this.getSherlockActivity().getSupportActionBar();

		actionBar.setTitle("校园活动");
		
		if(DBAdapter.checkIfDBEmpty())
		{
			try {
				progressDialog.show();
				new RefreshActiList().execute(new URL(getResources().getString(R.string.acti_url_activity_list)));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			List<ActiInfo> actiList = DBAdapter.getAllFromTBActiList();
			adapter.setActiInfoList(actiList);
			adapter.notifyDataSetChanged();
		}
		
		
		
		
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
		
		}
		return false;
	}
	
	private class RefreshActiList extends AsyncTask<URL,Integer,List<ActiInfo>>
	{

		@Override
		protected List<ActiInfo> doInBackground(URL... params) {
			// TODO Auto-generated method stub
			if (isCancelled())
				return null;
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
						if(str==noActivityHint)
						{
							return actiList;
						}
						JSONArray jsonArray = new JSONArray(str);
						//Log.v("Net test", str);
						for(int loop = 0;loop<jsonArray.length();++loop)
						{
							JSONObject jsonObject = jsonArray.getJSONObject(loop);
							int acti_id = Integer.parseInt(jsonObject.getString("id"));
							int league_id = Integer.parseInt(jsonObject.getString("league_id"));
							String acti_title = jsonObject.getString("name");
							String start_time = jsonObject.getString("start_time");
							String end_time = jsonObject.getString("end_time");
							String intro = jsonObject.getString("introduction");
							String release_time = jsonObject.getString("release_time");
							String place = jsonObject.getString("place");
							boolean isVote = jsonObject.getBoolean("isvote");
							JSONObject league_obj = jsonObject.getJSONObject("league_info");
							String league_name = league_obj.getString("league_name");
							String league_icon = league_obj.getString("avatar_address");
							String acti_pic = jsonObject.getString("post_add");
							

							ActiInfo tmpInfo = new ActiInfo(acti_id, isVote, league_name,league_id, league_icon, 
									acti_title, start_time, end_time,place, release_time, 
									intro, acti_pic);
							actiList.add(tmpInfo);
						}
						
						return actiList;
					}
				}
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
		protected void onPostExecute(List<ActiInfo> result)
		{
			try{
				if (result != null)
				{
					DBAdapter.clearActiListTb();
					insertActiInfoToDB(result,DBAdapter);
					
					adapter.setActiInfoList(result);
					adapter.notifyDataSetChanged();

				}
				
			}
			catch(Exception e)
			{
				Log.e("REQUEST", "error occured when refresh acti-list");
			}
			
			onRefreshActionComplete();
			
		}
		
	}
	
	private void insertActiInfoToDB(List<ActiInfo> l, ActiDBAdapter adapter)
	{
		if (l != null)
		{
			for (int loop = 0; loop < l.size(); ++loop)
			{
				ActiInfo item = l.get(loop);
				adapter.insertActiListItem(item.getId(), item.checkIsVote()?1:0, item.getClubName(),
						item.getClubId(),item.getClubIconName(), item.getActiTitle(),item.getActiIntro(),
						item.getActiPubTime(),item.getStartTime(), item.getEndTime(), item.getPlace(),
						item.getActiPicName(),
						null, null);
			}
		}
	}
	
	
	private class RequestActiList extends AsyncTask<URL,Integer,List<ActiInfo>>
	{

		@Override
		protected List<ActiInfo> doInBackground(URL... params) {
			// TODO Auto-generated method stub
			if(isCancelled())
				return null;
			
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
						if(str==noActivityHint)
						{
							return actiList;
						}
						JSONArray jsonArray = new JSONArray(str);
						//Log.v("Net test", str);
						for(int loop = 0;loop<jsonArray.length();++loop)
						{
							JSONObject jsonObject = jsonArray.getJSONObject(loop);
							int acti_id = Integer.parseInt(jsonObject.getString("id"));
							int league_id = Integer.parseInt(jsonObject.getString("league_id"));
							String acti_title = jsonObject.getString("name");
							String start_time = jsonObject.getString("start_time");
							String end_time = jsonObject.getString("end_time");
							String intro = jsonObject.getString("introduction");
							String release_time = jsonObject.getString("release_time");
							String place = jsonObject.getString("place");
							boolean isVote = jsonObject.getBoolean("isvote");
							JSONObject league_obj = jsonObject.getJSONObject("league_info");
							String league_name = league_obj.getString("league_name");
							String league_icon = league_obj.getString("avatar_address");
							//String acti_pic = jsonObject.getString("");
							String acti_pic = jsonObject.getString("post_add");

							ActiInfo tmpInfo = new ActiInfo(acti_id, isVote, league_name,league_id, league_icon, 
									acti_title, start_time, end_time,place, release_time, 
									intro, acti_pic);
							actiList.add(tmpInfo);
						}
						return actiList;
					}
				}
			} catch (IOException e) {				// TODO Auto-generated catch block
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
		protected void onPostExecute(List<ActiInfo>result)
		{
			try{
				if(result == null)
				{
					Toast.makeText(context, "加载失败", Toast.LENGTH_LONG).show();
				}
				else if(result.size() == 0)
				{
					Toast.makeText(context, "没有更多信息", Toast.LENGTH_LONG).show();
				}
				else{
					insertActiInfoToDB(result,DBAdapter);
					
					adapter.addActiInfoList(result);
					adapter.notifyDataSetChanged();
				}
			}
			catch(Exception e)
			{
				Log.e("REQUEST ACTI", "error occured when requesting");
			}

			foot.endRequestData();
			listView.onRequestComplete();
			
		}
	}
	
	
	private class GabberTestTask extends AsyncTask<Void, Integer, MainContentGridItemObj>{

		@Override
		protected MainContentGridItemObj doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return new ActivityDataGrabber().GrabInformationObject();
		}
		
		@Override 
		protected void onPostExecute(MainContentGridItemObj obj)
		{
			Toast.makeText(context, "gabber test:"+obj.getContent1()+obj.getContent2(), Toast.LENGTH_LONG).show();
		}
		
	}

}
