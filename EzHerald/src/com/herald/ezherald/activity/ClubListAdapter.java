package com.herald.ezherald.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.herald.ezherald.R;
import com.herald.ezherald.academic.DataTypeTransition;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class ClubListAdapter extends BaseAdapter {
	
	private List<ClubItem> clubList ;
	Context context;
	View currentView;
	
	private ProgressDialog progressDialog;
	
	public void  setFocusState(int position, boolean focus)
	{
		clubList.get(position).setFocus(focus);
		
	}
	
	
	public ClubListAdapter(Context c)
	{
		context = c;
		clubList = new ArrayList<ClubItem>();
		clubList.add(new ClubItem("","跆拳道",true));
		clubList.add(new ClubItem("","学生会",false));
		progressDialog = new ProgressDialog(c);
		progressDialog.setMessage("请稍候 ... ");
	}
	
	public void setClubList(ClubItem [] clubArr)
	{
		clubList.clear();
		for (int loop = 0; loop < clubArr.length; ++loop)
		{
			clubList.add(clubArr[loop]);
		}
	}
	
	public void setClubList(List<ClubItem> clubList)
	{
		clubList.clear();
		clubList.addAll(clubList);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return clubList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return clubList.get(arg0) ;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	public void changeBtnState(boolean focus, Button btn)
	{
		if (focus)
		{
			btn.setText("取消关注");
			btn.setBackgroundColor(Color.GRAY);
		}
		else
		{
			btn.setText("关注");
			btn.setBackgroundColor(Color.rgb(100, 100, 255));
		}
		
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		convertView = LayoutInflater.from(context).inflate(R.layout.acti_club_list_item, null);
		ClubHolder holder = new ClubHolder();
		holder.clubIcon = (ImageView) convertView.findViewById(R.id.acti_club_list_icon);
		holder.clubName = (TextView) convertView.findViewById(R.id.acti_club_list_name);
		holder.focusBtn = (Button) convertView.findViewById(R.id.acti_club_list_focus_btn);
		ClubItem club = clubList.get(position);
		holder.clubName.setText(club.getClubName());
		holder.clubIcon.setImageResource(R.drawable.ic_launcher);
//		if (club.checkFocus())
//		{
//			holder.focusBtn.setText("已关注");
//			holder.focusBtn.setBackgroundColor(Color.GRAY);
//		}
//		else
//		{
//			holder.focusBtn.setText("关注");
//			holder.focusBtn.setBackgroundColor(Color.rgb(100, 100, 255));
//		}
		changeBtnState(club.checkFocus(),holder.focusBtn);
		
		OnClickListener listener;

		listener = new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progressDialog.show();
				currentView = v;
				new RequestFocus().execute(position);
				//changeBtnState(false,(Button) v);
				Log.v("club list btn", "cancel focus");
			}
			
		};
	

		holder.focusBtn.setOnClickListener(listener);
		
		
		Log.v("ClubListAdapter", "is here");
		
		return convertView;
	}
	
	
	private class ClubHolder{
		public ImageView clubIcon;
		public TextView clubName;
		public Button focusBtn;
	}
	
	private class RequestFocus extends AsyncTask<Integer , Integer, Integer>
	{
		int pos;
		ClubItem club;

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			int response = -1;
			InputStream in = null;
			URLConnection conn;
			pos = params[0];
			club = clubList.get(pos);
			
			URL url;
			try {
				String url_str;
				if(club.checkFocus())
				{
					// 取消关注的链接  
					url_str = "http://jwc.seu.edu.cn";
				}
				else
				{
					//  关注的链接
					url_str = "http://jwc.seu.edu.cn";
				}
				url = new URL(url_str);
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
							
							return 1;
						}
					}	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			return 0;
		}
		
		@Override 
		protected void onPostExecute(Integer result)
		{
			if ( result == 1 )
			{
				if(club.checkFocus())
				{
					club.setFocus(false);
					changeBtnState(false,(Button)currentView);
					//Log.v("request focus finished", "cancel focus");
				}
				else
				{
					club.setFocus(true);
					changeBtnState(true,(Button)currentView);
					//Log.v("request focus finished", "cancel focus");
				}
				progressDialog.cancel();
			}
		}
		
	}
	

	
	
	

}
