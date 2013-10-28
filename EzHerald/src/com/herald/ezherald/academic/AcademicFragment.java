package com.herald.ezherald.academic;

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

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.R;
import com.herald.ezherald.academic.CustomListView.OnRefreshListener;
import com.herald.ezherald.mainframe.MainContentGridItemObj;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class AcademicFragment extends SherlockFragment implements
		ActionBar.OnNavigationListener {

	private CustomListView listView;
	private JwcInfoAdapter adapter;
	private ListFootView foot;
	
	private AcademicDBAdapter dbAdapter;

	public Menu mMenu;

	private final int ALL = 0;
	private final int JIAOWU = 1;
	private final int XUEJI = 2;
	private final int SHIJIAN = 3;
	private final int HEZUO = 4;
	private final int JIAOYAN = 5;
	private final int JIAOPING = 6;

	private int JwcInfoMode = ALL;
	
	private final String REFRESH_URL = "http://herald.seu.edu.cn/herald_web_service/jwc/%d/";
	private final String MORE_URL = "http://herald.seu.edu.cn/herald_web_service/jwc/more/%d/%d";
	
	
	private Integer lastid = null;
	private Context context;
	
	private ProgressDialog progressDialog;
	
	RefreshJwcInfo refreshTask;
	RequestJwcInfo requestTask;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
		context = getActivity();
		dbAdapter = new AcademicDBAdapter(context);
		
		
		progressDialog = new ProgressDialog(context);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("请稍候 ... ");
		
	}
	
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}



	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_academic_list, menu);
//		mMenu = menu;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockActivity#onOptionsItemSelected(com.
	 * actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * 锟较诧拷Title位锟矫的帮拷钮锟斤拷锟斤拷锟接�
		 */
		switch (item.getItemId()) {
		case R.id.academic_list_action_refresh:
			// item.setActionView(R.layout.academic_refresh_progress);
			refreshInfo();
			// item.setActionView(null);
			super.onOptionsItemSelected(item);
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}
	
	@Override 
	public void onDestroy()
	{
		
		if (refreshTask != null && refreshTask.getStatus() == AsyncTask.Status.RUNNING)
		{
			refreshTask.cancel(true);
		}
		if (requestTask !=null && requestTask.getStatus() == AsyncTask.Status.RUNNING )
		{
			requestTask.cancel(true);
		}
//		onRefreshActionComplete();
		progressDialog.dismiss();
		super.onDestroy();
	}
	
	@Override 
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		progressDialog.dismiss();
	}
	
	
//	@Override
//	public boolean onMenuItemSelected(int featureId, MenuItem item)
//	{
//		onOptionsItemSelected(item);
//		return true;
//	}

	// 刷锟铰菜碉拷锟斤拷始锟斤拷转
	public void onRefreshActionStart() {
		// REFRESHSTATE = REFRESHING ;
		if(mMenu == null)
			return;
		progressDialog.show();
		MenuItem muItem = mMenu.findItem(R.id.academic_list_action_refresh);
		muItem.setActionView(R.layout.academic_refresh_progress);
	}

	// 刷锟铰菜碉拷停止锟斤拷转
	public void onRefreshActionComplete() {
		// REFRESHSTATE = REFRESHDOWN;
		if(mMenu == null)
			return;
		
		progressDialog.cancel();
		MenuItem muItem = mMenu.findItem(R.id.academic_list_action_refresh);
		muItem.setActionView(null);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {

		mMenu = menu;

	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v;
		v = inflater.inflate(R.layout.academic_activity_main, null);
		listView = (CustomListView) v.findViewById(R.id.list);
		// 锟斤拷锟斤拷listView锟斤拷adapter
		adapter = new JwcInfoAdapter(getActivity());
//		adapter.setJwcInfoList(jwcArr);
		listView.setAdapter(adapter);
		// 锟斤拷拥撞锟斤拷锟斤拷锟斤拷馗锟洁”锟斤拷view
		foot = new ListFootView(getActivity().getApplicationContext());
		foot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					onRefreshActionStart();
					foot.startRequestData();
					int id = adapter.getLastItemId();
					String url = String.format(MORE_URL, id, JwcInfoMode);
					requestTask = new RequestJwcInfo();
					requestTask.execute(new URL(url));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		listView.addFooterView(foot.getFootView());
		// 锟斤拷锟斤拷listview锟斤拷刷锟铰诧拷锟斤拷
		listView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				refreshInfo();
			}

		});
		// 锟斤拷锟斤拷listview没锟斤拷item锟侥碉拷锟斤拷锟斤拷锟�
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						AcademicDetailActivity.class);
//				Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
				JwcInfo info = (JwcInfo) adapter.getItem(position-1);
				int i = info.GetId();
				intent.putExtra("id", i);
				startActivity(intent);
			}
		});
		// actionbar 锟斤拷 spinner
		SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.academic_list_action_spinner,
				R.layout.academic_spinner_textitem);
		ActionBar actionBar = this.getSherlockActivity().getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		actionBar.setListNavigationCallbacks(mSpinnerAdapter, this);
		
		initJwcInfoListView();
		
		refreshInfo();

		return v;
	}
	
	public void refreshInfo()
	{
		try {
			onRefreshActionStart();
			refreshTask = new RefreshJwcInfo();
			String url = String.format(REFRESH_URL, JwcInfoMode);
			refreshTask.execute(new URL(url));
			//ew grabber().execute();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initJwcInfoListView()
	{
		dbAdapter.open();
		List<JwcInfo> jwcInfoList = dbAdapter.getAllJwcInfo();
		dbAdapter.close();
		adapter.setJwcInfoList(jwcInfoList);
		adapter.notifyDataSetChanged();
	}
	

	// actionbar 锟斤拷 spinner 锟斤拷item锟斤拷锟斤拷锟接�
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		switch (itemPosition) {
		case 0:
			JwcInfoMode = ALL;
			break;
		case 1:
			JwcInfoMode = JIAOWU;
			break;
		case 2:
			JwcInfoMode = XUEJI;
			break;
		case 3:
			JwcInfoMode = SHIJIAN;
			break;
		case 4:
			JwcInfoMode = HEZUO;
			break;
		case 5:
			JwcInfoMode = JIAOYAN;
			break;
		case 6:
			JwcInfoMode = JIAOPING;
			break;
		}
//		Toast.makeText(getActivity(), "" + itemPosition + "   " + itemId,
//				Toast.LENGTH_SHORT).show();
		refreshInfo();
		return false;
	}

	private class RefreshJwcInfo extends AsyncTask<URL, Integer, List<JwcInfo>> {

		@Override
		protected List<JwcInfo> doInBackground(URL... arg0) {
			// TODO Auto-generated method stub
			// onRefreshActionStart();
			if(isCancelled()) 
				return null;
			List<JwcInfo> jwcList = new ArrayList<JwcInfo>();
			InputStream in = null;
			int response = -1;
			URL url = arg0[0];
			URLConnection conn;
			try {
				conn = url.openConnection();
				if (!(conn instanceof HttpURLConnection)) {
					throw new IOException("NOT AN HTTP CONNECTION");
				} else {
					HttpURLConnection httpConn = (HttpURLConnection) conn;
					httpConn.setAllowUserInteraction(false);
					httpConn.setInstanceFollowRedirects(true);
					httpConn.setRequestMethod("GET");
					httpConn.connect();
					response = httpConn.getResponseCode();
					if (response == HttpURLConnection.HTTP_OK) {
						in = httpConn.getInputStream();
						 String str = DataTypeTransition.InputStreamToString(in);
						// return str;
						List<JwcInfo> list = new ArrayList<JwcInfo>();
						JSONArray jsonArr = new JSONArray(str);
						for (int i=0; i<jsonArr.length(); ++i)
						{
							JSONArray jsonItem = (JSONArray) jsonArr.get(i);
							int id = Integer.parseInt(jsonItem.getString(0));
							String type = jsonItem.getString(1);
							String title = jsonItem.getString(2);
							String date = jsonItem.getString(3);
							list.add(new JwcInfo(type, title, date, id));
							
						}
						
						return list;
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
		protected void onPostExecute(List<JwcInfo> result) {
			try{
				if (result != null) {
					// adapter.addJwcInfoList(result);
					// adapter.foreAddJwcInfoList(result);
					adapter.setJwcInfoList(result);
					adapter.notifyDataSetChanged();
					refreshDB(result);
					listView.onRefreshComplete();
					onRefreshActionComplete();
					// Log.v("Watch", "onPostExecute");
					
				}
			}
			catch( Exception e)
			{
				e.printStackTrace();
			}
			

		}

	}
	
	private void refreshDB(List<JwcInfo> infoList)
	{
		dbAdapter.open();
		dbAdapter.refreshJwcInfo(infoList);
		dbAdapter.close();
	}
	

	private class RequestJwcInfo extends AsyncTask<URL, Integer, List<JwcInfo>> {

		@Override
		protected List<JwcInfo> doInBackground(URL... params) {
			// TODO Auto-generated method stub
			if(isCancelled()) 
				return null;
			List<JwcInfo> jwcList = new ArrayList<JwcInfo>();
			InputStream in = null;
			int response = -1;
			URL url = params[0];
			URLConnection conn;
			try {
				conn = url.openConnection();
				if (!(conn instanceof HttpURLConnection)) {
					throw new IOException("NOT AN HTTP CONNECTION");
				} else {
					HttpURLConnection httpConn = (HttpURLConnection) conn;
					httpConn.setAllowUserInteraction(false);
					httpConn.setInstanceFollowRedirects(true);
					httpConn.setRequestMethod("GET");
					httpConn.connect();
					response = httpConn.getResponseCode();
					if (response == HttpURLConnection.HTTP_OK) {
						in = httpConn.getInputStream();
						 String str = DataTypeTransition.InputStreamToString(in);
						// return str;

						List<JwcInfo> list = new ArrayList<JwcInfo>();
						JSONArray jsonArr = new JSONArray(str);
						for (int i=0; i<jsonArr.length(); ++i)
						{
							JSONArray jsonItem = (JSONArray) jsonArr.get(i);
							int id = Integer.parseInt(jsonItem.getString(0));
							String type = jsonItem.getString(1);
							String title = jsonItem.getString(2);
							String date = jsonItem.getString(3);
							list.add(new JwcInfo(type, title, date, id));
							
						}
						
						return list;
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
		protected void onPostExecute(List<JwcInfo> result) {
			try{
				if (result != null) {
					if(result.size() == 0)
					{
						Toast.makeText(context, "没有更多了.", Toast.LENGTH_LONG).show();
					}
					adapter.addJwcInfoList(result);
					adapter.notifyDataSetChanged();
					addIntoDB(result);
					foot.endRequestData();
					listView.onRequestComplete();
				}
				onRefreshActionComplete();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			

		}
	}
	
	private void addIntoDB(List<JwcInfo> infoList)
	{
		dbAdapter.open();
		dbAdapter.addJwcInfo(infoList);
		dbAdapter.close();
	}
	
	
	private class grabber extends AsyncTask<Void, Integer, MainContentGridItemObj>
	{

		@Override
		protected MainContentGridItemObj doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			MainContentGridItemObj obj = new AcademicDataGrabber().GrabInformationObject();
			return obj;
		}
		
		@Override
		protected void onPostExecute(MainContentGridItemObj obj)
		{
			Toast.makeText(context, obj.getContent1()+ obj.getContent2(), Toast.LENGTH_LONG).show();
		}
		
	}
	
}
