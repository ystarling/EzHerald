package com.herald.ezherald.emptyclassroom;


import java.io.IOException;
import java.util.List;

import org.apache.http.HttpException;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.nfc.NfcAdapter.CreateBeamUrisCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Advanceable;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;


public class EmptyClassroomFragment extends SherlockFragment {
	
	private Context context  =null;
	private View view = null;
	
	private ActionBar actionBar = null;
	private ActionBar.Tab bar_common = null;
	private ActionBar.Tab bar_advance = null;
	
	private String COMMON_SEARCH = "快捷搜索";
	private String ADVANCE_SEARCH = "高级搜索";
	
	private String proTitle = "正在加载...";
	private String requestFailed = "加载失败!!";
	
	private LinearLayout week_layout = null;
	private LinearLayout period_layout = null;
	private LinearLayout common_search_layout = null;
	private LinearLayout advance_search_layout = null;
	private Button btn_search_today = null;
	private Button btn_search_tomorrow = null;
	private Button btn_advance_search = null;
	private ListView listView = null;
	private Spinner spinner = null;
	private TextView tv_from_period  = null;
	private TextView tv_to_period = null;
	private TextView tv_week = null;
	private TextView tv_day = null;
	
	
	private AsyncTask<Void, Integer, String> requestTask = null;
	
	private ProgressDialog progressDialog = null;
	
	private RoomAdapter listAdapter = null;
	
	private UrlFactory urlFactory = null;
	
	private final Integer TODAY = 1;
	private final Integer TOMORROW = 2;
	private final Integer ADVANCE = 3;
	
	private final String [] campus = {"九龙湖","四牌楼","丁家桥","全部"};
	private final String CAMPUS_JLH = "jlh";
	private final String CAMPUS_SPL = "spl";
	private final String CAMPUS_DJQ = "djq";
	private final String CAMPUS_ALL = "all";
	private String selected_campus = CAMPUS_JLH;
	
	private ArrayAdapter<String> spinnerAdapter = null;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		context = getActivity();
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle(this.proTitle);
		progressDialog.setCanceledOnTouchOutside(false);
		
		listAdapter  = new RoomAdapter(context);
		urlFactory = new UrlFactory();
		
	}
	
	@Override
	public View  onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.emproom, null);
		
		initWidget();
		
		setOnClickListener();
		
		createTabs();
		
		listView.setAdapter(listAdapter);
		
		return view;
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
	
	private boolean initWidget()
	{
		try{
			week_layout = (LinearLayout) view.findViewById(R.id.emproom_select_week_and_day);
			period_layout = (LinearLayout) view.findViewById(R.id.emproom_select_peroid);
			common_search_layout = (LinearLayout) view.findViewById(R.id.emproom_quick_search);
			advance_search_layout = (LinearLayout) view.findViewById(R.id.emproom_advance_search);
			btn_search_today = (Button) view.findViewById(R.id.btn_search_today);
			btn_search_tomorrow = (Button) view.findViewById(R.id.btn_search_tomorrow);
			btn_advance_search = (Button) view.findViewById(R.id.btn_advance_search);
			listView  = (ListView) view.findViewById(R.id.emproom_room_list);
			spinner = (Spinner) view.findViewById(R.id.emproom_campus_spinner);
			tv_day = (TextView) view.findViewById(R.id.emproom_day);
			tv_week = (TextView) view.findViewById(R.id.emproom_week);
			tv_from_period = (TextView) view.findViewById(R.id.emproom_from_peroid);
			tv_to_period = (TextView) view.findViewById(R.id.emproom_to_peroid);
			
			spinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, campus);
			spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
			spinner.setAdapter(spinnerAdapter);
//			spinner.setOnItemClickListener(new OnItemClickListener(){
//
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1,
//						int arg2, long arg3) {
//					// TODO Auto-generated method stub
//					Toast.makeText(context, ""+arg2, Toast.LENGTH_LONG).show();					
//				}			
//			});
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					switch(arg2)
					{
					case 0:
						selected_campus = CAMPUS_JLH;
						break;
					case 1:
						selected_campus = CAMPUS_SPL;
						break;
					case 2:
						selected_campus = CAMPUS_DJQ;
						break;
					case 3:
						selected_campus = CAMPUS_ALL;
						break;
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});

			return true;
		}
		catch(Exception e)
		{
			Log.e("INIT WIDGET", "fatal error occured when initial widgets");
			return false;
		}
		
	}
	
	private boolean createTabs()
	{
		try{	
			actionBar = getSherlockActivity().getSupportActionBar();
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			bar_common = actionBar.newTab();
			bar_advance = actionBar.newTab();
			bar_common.setText(COMMON_SEARCH);
			bar_advance.setText(ADVANCE_SEARCH);
			bar_common.setTabListener(new ACTabListener());
			bar_advance.setTabListener(new ACTabListener());
			actionBar.addTab(bar_common);
			actionBar.addTab(bar_advance);
			actionBar.selectTab(bar_common);
			return true;
		}
		catch(Exception e){
			Log.e("CREATE BAR", "fatal error occured when create tabs");
			return false;
		}
		
	}
	
	private class ACTabListener implements ActionBar.TabListener{

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			switch(tab.getPosition()){
			case 0:
			{
				week_layout.setVisibility(View.GONE);
				advance_search_layout.setVisibility(View.GONE);
				period_layout.setVisibility(View.VISIBLE);
				common_search_layout.setVisibility(View.VISIBLE);
//				Toast.makeText(context, "common search", Toast.LENGTH_LONG).show();
				break;
			}
				
			case 1:
			{
				week_layout.setVisibility(View.VISIBLE);
				advance_search_layout.setVisibility(View.VISIBLE);
				period_layout.setVisibility(View.VISIBLE);
				common_search_layout.setVisibility(View.GONE);
//				Toast.makeText(context, "advance search", Toast.LENGTH_LONG).show();
				break;
			}
				
			}
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
	
	private void setOnClickListener()
	{
		this.btn_search_today.setOnClickListener(
				new OnClickListenerCreator(TODAY).getListener()
				);
		
		this.btn_search_tomorrow.setOnClickListener(
				new OnClickListenerCreator(TOMORROW).getListener()
				);
		
		this.btn_advance_search.setOnClickListener(
				new OnClickListenerCreator(ADVANCE).getListener()
				);		
	}
	
	private class OnClickListenerCreator{
		private Integer buttonType = null;
		public OnClickListenerCreator(Integer type)
		{
			buttonType = type;
		}
		
		public OnClickListener getListener()
		{
			return new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onRefreshStart();
					requestTask = new requestEmptyRoom(buttonType);
					requestTask.execute();
				}
			};
		}
	}
	

	
	private class UrlFactory
	{
		
		public String getTodayUrl()
		{
			String from_period = (String) tv_from_period.getText().toString();
			String to_period = (String) tv_to_period.getText().toString();
			String url = String.format("http://herald.seu.edu.cn/queryEmptyClassrooms/query/%s/today/%s/%s/",
					selected_campus, from_period, to_period);
			return url;
		}
		
		public String getTomorrowUrl()
		{
			String from_period = tv_from_period.getText().toString();
			String to_period = (String) tv_to_period.getText().toString();
			String url = String.format("http://herald.seu.edu.cn/queryEmptyClassrooms/query/%s/tomorrow/%s/%s/",
					selected_campus, from_period, to_period);
			return url;
		}
		
		public String getAdvanceUrl()
		{
			String from_period = (String) tv_from_period.getText().toString();
			String to_period = (String) tv_to_period.getText().toString();
			String week = (String) tv_week.getText().toString();
			String day = (String) tv_day.getText().toString();
			String url = String.format("http://herald.seu.edu.cn/queryEmptyClassrooms/query/%s/%s/%s/%s/%s/",
					selected_campus, week, day, from_period, to_period);
			return url;
		}
		
	}
	
	
	
	
	private class requestEmptyRoom extends AsyncTask<Void, Integer, String>
	{
		private Integer requestType = null;
		private String url = null;
		public requestEmptyRoom(Integer type)
		{
			requestType = type;
			switch(type)
			{
			case 1:
				url = urlFactory.getTodayUrl();
				break;
			case 2:
				url = urlFactory.getTomorrowUrl();
				break;
			case 3:
				url = urlFactory.getAdvanceUrl();
				break;
			}
		}

		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			if(isCancelled())
				return null;
			
			try {
				return new NetRequest().request(url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (HttpException e) {
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
		    	return ;
		}
		
		@Override
		protected void onPostExecute(String result)
		{
			if(null != result)
			{
				List<String> roomList;
				
				try {
					roomList = DataParser.strToList(result);
//					Toast.makeText(context, ""+roomList.size(), Toast.LENGTH_LONG).show();
					listAdapter.setRoomList(roomList);
					listAdapter.notifyDataSetChanged();
//					Toast.makeText(context, "request success!!", Toast.LENGTH_LONG).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.v("REQUEST ROOM", ""+e);
					e.printStackTrace();
				}
				
			}
			else{
				Toast.makeText(context, requestFailed, Toast.LENGTH_LONG).show();
			}
//			Toast.makeText(context, "exe here", Toast.LENGTH_LONG).show();
			onRefreshCompleted();
		}

	}
	
	private void onRefreshStart()
	{
		progressDialog.show();
	}
	
	private void onRefreshCompleted()
	{
		progressDialog.cancel();
	}
	
	
	

}
