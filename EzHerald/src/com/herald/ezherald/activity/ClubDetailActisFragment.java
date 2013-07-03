package com.herald.ezherald.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;
import com.herald.ezherald.academic.ListFootView;

public class ClubDetailActisFragment extends SherlockFragment {
	
	private ListView listView;
	private Activity context;
	private ListFootView foot;
	private ActiInfoAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
	}
	
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v;
		v = inflater.inflate(R.layout.acti_club_detail_actis, null);
		context = getActivity();
		
		listView  = (ListView) v.findViewById(R.id.acti_club_detail_acti_list);
		
		ActiInfo [] actiArr = {new ActiInfo(0,"herald","招新","2013-6-27","先声网招新了,欢迎热爱技术的同学踊跃报名","",false),
				new ActiInfo(0,"空手道社","南京内部赛","2013-6-27","南京极真空手道将在全民健身中心举办南京内部赛","",false)};
		
		
		
		
		foot = new ListFootView(context);
		foot.setOnClickListener(new OnClickListener(){
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
		
		adapter = new ActiInfoAdapter(context);
		adapter.setActiInfoList(actiArr);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				// TODO Auto-generated method stub
				context.startActivity(new Intent(context,ActiInfoDetailActivity.class));
			}
			
		});
		
//		View f = LayoutInflater.from(context).inflate(R.layout.academic_list_foot_view, null);
//		Toast.makeText(context, "foot", Toast.LENGTH_SHORT).show();
//		listView.addFooterView(f);
		
		return v;
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
			
		}
	}

	
	
	

}
