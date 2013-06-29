package com.herald.ezherald.activity;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;



import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.R;
import com.herald.ezherald.academic.DataTypeTransition;
import com.herald.ezherald.academic.JwcInfo;

public class ActiInfoDetailActivity extends SherlockActivity {
	
	@SuppressLint("NewApi")
	@Override 
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acti_acti_info_detail);
		
		try {
			new RequestActiInfoDetail().execute(new URL("http://jwc.seu.edu.cn"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override 
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.menu_acti_detail, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem muItem)
	{
		switch(muItem.getItemId())
		{
		case R.id.menu_acti_detail_action_share:
		      Intent intent=new Intent(Intent.ACTION_SEND);
		      
		      intent.setType("text/plain");
		      intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
		      intent.putExtra(Intent.EXTRA_TEXT, "I would like to ‘herald campus’ this with you...");
		      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		      startActivity(Intent.createChooser(intent,"分享到"));
		      
		      return true;
		case android.R.id.home:
			finish();
			return true;
		
		}
		return false;
	}
	
	private class RequestActiInfoDetail extends AsyncTask<URL, Integer, ActiInfoDetail>
	{

		@Override
		protected ActiInfoDetail doInBackground(URL... params) {
			// TODO Auto-generated method stub
			URL url = params[0];
			int response = -1;
			InputStream in = null;
			URLConnection conn;
			
			try {
				conn = url.openConnection();
				if (!(conn instanceof HttpURLConnection) )
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
					if (response == HttpURLConnection.HTTP_OK)
					{
						in = httpConn.getInputStream();
						String str = DataTypeTransition.InputStreamToString(in);
						ActiInfoDetail actiDetail = new ActiInfoDetail("空手道社","南京内部赛","2013-6-27",
								"南京极真空手道将在全民健身中心举办南京内部赛","","");
						
						return actiDetail;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override 
		protected void onPostExecute(ActiInfoDetail result)
		{
			if(result != null)
			{
				TextView clubName = (TextView) findViewById(R.id.acti_detail_club_name);
				TextView actiTitle = (TextView) findViewById(R.id.acti_detail_acti_title);
				TextView actiPubTime = (TextView) findViewById(R.id.acti_detail_acti_pubtime);
				TextView actiDetail = (TextView) findViewById(R.id.acti_detail_acti_detail);
				ImageView clubIcon = (ImageView) findViewById(R.id.acti_detail_club_icon);
				ImageView actiPic = (ImageView) findViewById(R.id.acti_detail_acti_pic);
				
				clubName.setText(result.getClubName());
				actiTitle.setText(result.getActiTitle());
				actiPubTime.setText(result.getActiPubTime());
				actiDetail.setText(result.getActiDetail());
				clubIcon.setImageResource(R.drawable.ic_launcher);
				actiPic.setImageResource(R.drawable.ic_launcher);
			}
		}
		
	}
	
	
	

}
