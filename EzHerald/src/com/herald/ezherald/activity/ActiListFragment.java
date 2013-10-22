package com.herald.ezherald.activity;

import java.io.ByteArrayOutputStream;
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
import android.graphics.Bitmap;
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
import android.widget.ListView;
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

public class ActiListFragment extends SherlockFragment implements ActionBar.OnNavigationListener {
	
	private CustomListView listView;
	private ActiInfoAdapter adapter;
	private ListFootView foot;
	
	private ProgressDialog progressDialog;
	
	private Menu mMenu;
	
	private Activity context;
	
	final private int ALLACTI=0;
	final private int CONCERNEDACTI=1;
	private int ACTITYPE = ALLACTI;
	

	ActiDBAdapter DBAdapter;
	
	private final String noActivityHint = "NOACTIVITYCANGET";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRetainInstance(true);	
		setHasOptionsMenu(true);
		
		context = getActivity();
		progressDialog = new ProgressDialog(context);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("Please wait ... ");
		
		DBAdapter = new ActiDBAdapter(context);
		DBAdapter.open();
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if(DBAdapter != null)
		{
			DBAdapter.close();
		}
		
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
				if(ACTITYPE == ALLACTI)
				{
					new RefreshActiList().execute(new URL(getResources().getString(R.string.acti_url_activity_list)));
				}
				else if(ACTITYPE == CONCERNEDACTI)
				{
					new RefreshActiList().execute(new URL(getResources().getString(R.string.acti_url_focus_activity_list)));
				}
				
			
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
					else if(ACTITYPE == CONCERNEDACTI)
					{
						if(adapter.getLastActiId() != null)
						{
							foot.startRequestData();
							new RequestActiList().execute(new URL(getResources().getString(R.string.acti_url_focus_more_activity)
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
				Toast.makeText(getActivity(), ""+position+"   "+id, Toast.LENGTH_SHORT).show();
				
				ActiInfo actiInfo = (ActiInfo) adapter.getItem((int)id);
				Toast.makeText(context, ""+actiInfo.getId(), Toast.LENGTH_SHORT).show();
				boolean isVote = actiInfo.checkIsVote();
				Intent intent = new Intent(context,ActiInfoDetailActivity.class);
				intent.putExtra("clubName", actiInfo.getClubName());
				intent.putExtra("title", actiInfo.getActiTitle());
				intent.putExtra("date", actiInfo.getActiPubTime());
				intent.putExtra("startTime", actiInfo.getStartTime());
				intent.putExtra("endTime", actiInfo.getEndTime());
				intent.putExtra("actiId", actiInfo.getId());
				intent.putExtra("clubId", actiInfo.getClubId());
//				Bitmap bit_icon = DBAdapter.getClubIconByActi(actiInfo.getId());
//				ByteArrayOutputStream os_icon = null;
//				if(bit_icon != null)
//				{
//					os_icon = new ByteArrayOutputStream();
//					bit_icon.compress(Bitmap.CompressFormat.PNG, 100, os_icon);
//				}
				
//				intent.putExtra("clubIcon",os_icon.toByteArray());
				intent.putExtra("place", actiInfo.getPlace());
				intent.putExtra("picName", actiInfo.getActiPicName());
				intent.putExtra("iconName", actiInfo.getClubIconName() );
//				Bitmap bit_pic = DBAdapter.getActiPicByActi(actiInfo.getId());
//				ByteArrayOutputStream os_pic = new ByteArrayOutputStream();
//				bit_pic.compress(Bitmap.CompressFormat.PNG, 100, os_pic);
//				intent.putExtra("actiPic",os_pic.toByteArray());
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
					if(ACTITYPE == ALLACTI)
					{
						new RefreshActiList().execute(new URL(getResources().getString(R.string.acti_url_activity_list)));
					}
					else if(ACTITYPE == CONCERNEDACTI )
					{
						new RefreshActiList().execute(new URL(getResources().getString(R.string.acti_url_focus_activity_list)));
					}
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					onRefreshActionComplete();
					e.printStackTrace();
				}
				
			}
			
		});
		
		ActionBar actionBar = this.getSherlockActivity().getSupportActionBar();
		SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.acti_list_action_spinner, 
				R.layout.academic_spinner_textitem);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setListNavigationCallbacks(mSpinnerAdapter, this);
		
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
		case 1:
		{
			ACTITYPE = CONCERNEDACTI;
			try {
				new RefreshActiList().execute(new URL(getResources().getString(R.string.acti_url_focus_activity_list)));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
			
		case 2:
			startActivity(new Intent(getActivity(),ClubListActivity.class));
			
			return true;
		case 3:
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
		protected void onPostExecute(List<ActiInfo> result)
		{
			if (result != null)
			{
				DBAdapter.clearActiListTb();
				insertActiInfoToDB(result,DBAdapter);
				
				adapter.setActiInfoList(result);
				adapter.notifyDataSetChanged();
				
				
				
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
						item.getClubId(),item.getClubName(), item.getActiTitle(),item.getActiIntro(),
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
		protected void onPostExecute(List<ActiInfo>result)
		{
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
			
			foot.endRequestData();
			listView.onRequestComplete();
			
		}
	}

}
