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

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.R;
import com.herald.ezherald.academic.CustomListView.OnRefreshListener;

import android.annotation.SuppressLint;
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

	private Menu mMenu;

	private final int ALL = 0;
	private final int JIAOWU = 1;
	private final int XUEJI = 2;
	private final int SHIJIAN = 3;
	private final int HEZUO = 4;
	private final int JIAOYAN = 5;
	private final int JIAOPING = 6;

	private int JwcInfoMode = ALL;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
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
		 * 上侧Title位置的按钮点击相应
		 */
		switch (item.getItemId()) {
		case R.id.academic_list_action_refresh:
			try {
				// item.setActionView(R.layout.academic_refresh_progress);
				onRefreshActionStart();
				new RefreshJwcInfo().execute(new URL("http://jwc.seu.edu.cn"));
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

	// 刷新菜单开始旋转
	public void onRefreshActionStart() {
		// REFRESHSTATE = REFRESHING ;
		MenuItem muItem = mMenu.findItem(R.id.academic_list_action_refresh);
		muItem.setActionView(R.layout.academic_refresh_progress);

	}

	// 刷新菜单停止旋转
	public void onRefreshActionComplete() {
		// REFRESHSTATE = REFRESHDOWN;
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
		// 数据获取，暂且直接给出
		JwcInfo[] jwcArr = {
				new JwcInfo("[教务管理]", "四六级考试", "2013-6-15",
						"四六级考试将在九龙湖校区进行，请各位同学做好准备"),
				new JwcInfo("[教务管理]", "期末考试", "2013-6-17",
						"期末考试记录说明：这学期学校继续严厉打击作弊行为...") };
		// 设置listView的adapter
		adapter = new JwcInfoAdapter(getActivity());
		adapter.setJwcInfoList(jwcArr);
		listView.setAdapter(adapter);
		// 添加底部“加载更多”的view
		foot = new ListFootView(getActivity().getApplicationContext());
		foot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					foot.startRequestData();
					new RequestJwcInfo().execute(new URL(
							"http://jwc.seu.edu.cn"));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		listView.addFooterView(foot.getFootView());
		// 设置listview的刷新操作
		listView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				try {
					onRefreshActionStart();
					new RefreshJwcInfo().execute(new URL(
							"http://jwc.seu.edu.cn"));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					onRefreshActionComplete();
					e.printStackTrace();
				}
			}

		});
		// 设置listview没个item的点击操作
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						AcademicDetailActivity.class);
				intent.putExtra("url", "http://jwc.seu.edu.cn");
				startActivity(intent);

			}

		});
		// actionbar 的 spinner
		SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.academic_list_action_spinner,
				android.R.layout.simple_spinner_dropdown_item);
		ActionBar actionBar = this.getSherlockActivity().getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// 下面注释起来的方法也可以的
		// class OnNavigationListener implements ActionBar.OnNavigationListener{
		//
		// @Override
		// public boolean onNavigationItemSelected(int itemPosition,
		// long itemId) {
		// // TODO Auto-generated method stub
		// Toast.makeText(getActivity(), ""+itemPosition+"   "+itemId,
		// Toast.LENGTH_SHORT).show();
		// return false;
		// }
		//
		// }

		actionBar.setListNavigationCallbacks(mSpinnerAdapter, this);

		return v;
	}

	// actionbar 的 spinner 的item点击响应
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
		// try {
		// onRefreshActionStart();
		// new RefreshJwcInfo().execute(new URL("http://jwc.seu.edu.cn") );
		// //return true;
		// } catch (MalformedURLException e) {
		// // TODO Auto-generated catch block
		// onRefreshActionComplete();
		// e.printStackTrace();
		// //return false;
		// }
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
						// String str =
						// DataTypeTransition.InputStreamToString(in);
						// return str;
						List<JwcInfo> list = new ArrayList<JwcInfo>();
						list.add(new JwcInfo("[生活提醒]", "紧急天气预警", "2013-6-23",
								"南京蛋疼的天气又来雨了，估计周三才能停，童鞋们忍一忍吧。"));
						list.add(new JwcInfo("[生活提醒]", "暑期放假通知", "2013-6-25",
								"又要放假了，小心被子发霉啊，该咋办问爹妈吧。"));
						return list;
					}
				}
			} catch (IOException e) {
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
						// String str =
						// DataTypeTransition.InputStreamToString(in);
						// return str;
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						List<JwcInfo> list = new ArrayList<JwcInfo>();
						list.add(new JwcInfo("[生活提醒]", "紧急天气预警", "2013-6-23",
								"南京蛋疼的天气又来雨了，估计周三才能停，童鞋们忍一忍吧。"));
						list.add(new JwcInfo("[生活提醒]", "暑期放假通知", "2013-6-25",
								"又要放假了，小心被子发霉啊，该咋办问爹妈吧。"));
						return list;
					}
				}
			} catch (IOException e) {
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

}
