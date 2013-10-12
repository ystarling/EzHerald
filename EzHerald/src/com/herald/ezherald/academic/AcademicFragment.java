package com.herald.ezherald.academic;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.R;
import com.herald.ezherald.academic.CustomListView.OnRefreshListener;
import com.herald.ezherald.mainframe.MainContentGridItemObj;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class AcademicFragment extends SherlockFragment implements
		ActionBar.OnNavigationListener {

	private CustomListView listView;
	private JwcInfoAdapter adapter;
	private ListFootView foot;

	public Menu mMenu;

	private final int ALL = 0;
	private final int JIAOWU = 1;
	private final int XUEJI = 2;
	private final int SHIJIAN = 3;
	private final int HEZUO = 4;
	private final int JIAOYAN = 5;
	private final int JIAOPING = 6;

	private int JwcInfoMode = ALL;
	
	private final String REFRESH_URL = "http://herald.seu.edu.cn/herald_web_service/jwc/";
	private final String MORE_URL = "http://herald.seu.edu.cn/herald_web_service/jwc/more/%d/";
	
	
	private Integer lastid = null;
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
		context = getActivity();
		
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
			try {
				// item.setActionView(R.layout.academic_refresh_progress);
				onRefreshActionStart();
				new RefreshJwcInfo().execute(new URL(REFRESH_URL));
				// item.setActionView(null);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				onRefreshActionComplete();
				e.printStackTrace();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}

	// 刷锟铰菜碉拷锟斤拷始锟斤拷转
	public void onRefreshActionStart() {
		// REFRESHSTATE = REFRESHING ;
		if(mMenu == null)
			return;
		
		MenuItem muItem = mMenu.findItem(R.id.academic_list_action_refresh);
		muItem.setActionView(R.layout.academic_refresh_progress);
	}

	// 刷锟铰菜碉拷停止锟斤拷转
	public void onRefreshActionComplete() {
		// REFRESHSTATE = REFRESHDOWN;
		if(mMenu == null)
			return;
		
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
					foot.startRequestData();
					int id = adapter.getLastItemId();
					String url = String.format(MORE_URL, id);
					new RequestJwcInfo().execute(new URL(url));
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
				try {
					onRefreshActionStart();
					new RefreshJwcInfo().execute(new URL(REFRESH_URL));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					onRefreshActionComplete();
					e.printStackTrace();
				}
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
				Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
				JwcInfo info = (JwcInfo) adapter.getItem(position-1);
				int i = info.GetId();
				intent.putExtra("id", i);
				startActivity(intent);
			}
		});
		// actionbar 锟斤拷 spinner
		SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.academic_list_action_spinner,
				android.R.layout.simple_spinner_dropdown_item);
		ActionBar actionBar = this.getSherlockActivity().getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		actionBar.setListNavigationCallbacks(mSpinnerAdapter, this);
		
		try {
			onRefreshActionStart();
			new RefreshJwcInfo().execute(new URL(REFRESH_URL));
			//ew grabber().execute();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return v;
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
		Toast.makeText(getActivity(), "" + itemPosition + "   " + itemId,
				Toast.LENGTH_SHORT).show();
		return false;
	}

	private class RefreshJwcInfo extends AsyncTask<URL, Integer, List<JwcInfo>> {

		@Override
		protected List<JwcInfo> doInBackground(URL... arg0) {
			// TODO Auto-generated method stub
			// onRefreshActionStart();
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
		protected void onPostExecute(List<JwcInfo> result) {
			if (result != null) {
				// adapter.addJwcInfoList(result);
				// adapter.foreAddJwcInfoList(result);
				adapter.setJwcInfoList(result);
				adapter.notifyDataSetChanged();
				listView.onRefreshComplete();
				onRefreshActionComplete();
				// Log.v("Watch", "onPostExecute");
				
			}

		}

	}

	private class RequestJwcInfo extends AsyncTask<URL, Integer, List<JwcInfo>> {

		@Override
		protected List<JwcInfo> doInBackground(URL... params) {
			// TODO Auto-generated method stub

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
		protected void onPostExecute(List<JwcInfo> result) {
			if (result != null) {
				adapter.addJwcInfoList(result);
				adapter.notifyDataSetChanged();
				foot.endRequestData();
				listView.onRequestComplete();
			}

		}
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
