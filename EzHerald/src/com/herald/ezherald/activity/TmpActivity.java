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

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.app.SherlockActivity;

import com.herald.ezherald.R;
import com.herald.ezherald.academic.CustomListView;
import com.herald.ezherald.academic.DataTypeTransition;
import com.herald.ezherald.academic.ListFootView;
import com.herald.ezherald.academic.CustomListView.OnRefreshListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TmpActivity extends SherlockFragment {
	
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
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRetainInstance(true);	
		setHasOptionsMenu(true);
		
		context = getActivity();
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("Please wait ... ");
		
		DBAdapter = new ActiDBAdapter(context);
		DBAdapter.open();
	}
	
	
	
//	@SuppressLint("NewApi")
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved)
	{
		super.onCreate(saved);
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
					foot.startRequestData();
					new RequestActiList().execute(new URL("http://herald.seu.edu.cn/herald_league_api" +
							"/index.php/command/select/selectoperate/getmore/lastactivityid/2"));
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
				ActiInfo actiInfo = (ActiInfo) adapter.getItem(position-1);
				boolean isVote = actiInfo.checkIsVote();
				if(isVote)
				{
					Intent intent = new Intent(context,VoteDetailActivity.class);
					startActivity(intent);
				}
				else
				{
					Intent intent = new Intent(context,ActiInfoDetailActivity.class);
					intent.putExtra("clubName", actiInfo.getClubName());
					intent.putExtra("title", actiInfo.getActiTitle());
					intent.putExtra("date", actiInfo.getActiPubTime());
					intent.putExtra("startTime", actiInfo.getStartTime());
					intent.putExtra("endTime", actiInfo.getEndTime());
					intent.putExtra("actiId", actiInfo.getId());
					intent.putExtra("clubId", actiInfo.getClubId());
					Bitmap bit_icon = DBAdapter.getClubIconByActi(actiInfo.getId());
					ByteArrayOutputStream os_icon = new ByteArrayOutputStream();
					bit_icon.compress(Bitmap.CompressFormat.PNG, 100, os_icon);
					intent.putExtra("clubIcon",os_icon.toByteArray());
					intent.putExtra("place", actiInfo.getPlace());
					intent.putExtra("picName", actiInfo.getActiPicName());
					startActivity(intent);
				}
				
			}
			
		});
		
		listView.setonRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				try {
//					onRefreshActionStart();
					new RefreshActiList().execute(new URL("http://herald.seu.edu.cn/herald_league_api" +
							"/index.php/command/select/selectoperate/getactivity"));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
//					onRefreshActionComplete();
					e.printStackTrace();
				}
				
			}
			
		});
		
		ActionBar actionBar = this.getSherlockActivity().getSupportActionBar();
		SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.acti_list_action_spinner, 
				android.R.layout.simple_spinner_dropdown_item);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setListNavigationCallbacks(mSpinnerAdapter, new tmp() );
		
		if(DBAdapter.checkIfDBEmpty())
		{
			try {
				progressDialog.show();
				new RefreshActiList().execute(new URL("http://herald.seu.edu.cn/herald_league_api" +
						"/index.php/command/select/selectoperate/getactivity"));
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
	
	private class tmp implements ActionBar.OnNavigationListener
	{

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
				startActivity(new Intent(getActivity(),ClubListActivity.class));
				
				return true;
			case 3:
				startActivity(new Intent(getActivity(),ClubListActivity.class));
				
				return true;
			
			}
			
			return false;
		}
		
	}
	
	
//	@Override
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
//				insertActiInfoToDB(result,DBAdapter);
				
				adapter.setActiInfoList(result);
				adapter.notifyDataSetChanged();
				listView.onRefreshComplete();
//				onRefreshActionComplete();
				progressDialog.cancel();
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

	

}
