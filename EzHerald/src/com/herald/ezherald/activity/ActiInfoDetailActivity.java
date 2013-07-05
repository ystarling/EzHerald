package com.herald.ezherald.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.R;
import com.herald.ezherald.academic.DataTypeTransition;
import com.herald.ezherald.academic.JwcInfo;


public class ActiInfoDetailActivity extends SherlockActivity {
	
	private Activity context;
	private ProgressDialog progressDialog;
	private ActiInfoDetail actiInfoDetail;
	
	TextView clubName;
	TextView actiTitle ;
	TextView actiPubTime;
	TextView actiDetail ;
	ImageView clubIcon ;
	ImageView actiPic ;
	ScrollListView listView;
	TextView moreComment;
	CommentListAdapter commentAdapter;
	EditText commentContent;
	Button sendComment;
	ScrollView scrollView;
	
	int currCommentNum;
	int totalCommentNum;
	

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acti_acti_info_detail);
		context  = this;
		commentAdapter = new CommentListAdapter(context);
		currCommentNum = 0;
		totalCommentNum = 0;
		//actiInfoDetail = new ActiInfoDetail();
		
		clubName = (TextView) findViewById(R.id.acti_detail_club_name);
		actiTitle = (TextView) findViewById(R.id.acti_detail_acti_title);
		actiPubTime = (TextView) findViewById(R.id.acti_detail_acti_pubtime);
		actiDetail = (TextView) findViewById(R.id.acti_detail_acti_detail);
		clubIcon = (ImageView) findViewById(R.id.acti_detail_club_icon);
		actiPic = (ImageView) findViewById(R.id.acti_detail_acti_pic);
		moreComment = (TextView) findViewById(R.id.acti_acti_detail_more_msg);
		listView = (ScrollListView) findViewById(R.id.acti_acti_detail_msg_list);
		commentContent = (EditText) findViewById(R.id.acti_acti_detail_write_msg);
		sendComment = (Button) findViewById(R.id.acti_acit_detail_send_msg);
		scrollView = (ScrollView) findViewById(R.id.acti_acti_detail_scroll);
		
		
		listView.setAdapter(commentAdapter);
		listView.setParentScrollView(scrollView);
        listView.setMaxHeight(10000);
        
        OnClickListener iconClickListener = new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,ClubDetailActivity.class);
				intent.putExtra("clubName", actiInfoDetail.getClubName());
				intent.putExtra("focus", true);
				startActivity(intent);
			}
        	
        };
        
        clubIcon.setOnClickListener(iconClickListener);
        clubName.setOnClickListener(iconClickListener);
        
        actiPic.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(context,ClubAlbumActivity.class));
			}
        	
        });
		
		moreComment.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					
					if(currCommentNum < totalCommentNum)
					{
						progressDialog.show();
						new RequestMoreComment().execute(new URL("http://jwc.seu.edu.cn"));
					}
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		sendComment.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					progressDialog.show();
					new SendComment().execute(new URL("http://jwc.seu.edu.cn"));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		
		Bundle bundle = this.getIntent().getExtras();
		
		clubName.setText(bundle.getString("clubName"));
		actiTitle.setText(bundle.getString("title"));
		actiPubTime.setText(bundle.getString("date"));
		
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("Please wait ... ");
		progressDialog.show();

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		try {
			new RequestActiInfoDetail()
					.execute(new URL("http://jwc.seu.edu.cn"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.menu_acti_detail, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem muItem) {
		switch (muItem.getItemId()) {
		case R.id.menu_acti_detail_action_share:
			Intent intent = new Intent(Intent.ACTION_SEND);

			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
			intent.putExtra(Intent.EXTRA_TEXT,
					"I would like to ‘herald campus’ this with you...");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(Intent.createChooser(intent, "分享到"));

			return true;
		case android.R.id.home:
			finish();
			return true;

		}
		return false;
	}

	private class RequestActiInfoDetail extends
			AsyncTask<URL, Integer, ActiInfoDetail> {

		@Override
		protected ActiInfoDetail doInBackground(URL... params) {
			// TODO Auto-generated method stub
			URL url = params[0];
			int response = -1;
			InputStream in = null;
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
						actiInfoDetail = new ActiInfoDetail("空手道社",
								"南京内部赛", "2013-6-27", "南京极真空手道将在全民健身中心举办南京内部赛." +
										"南京极真空手道将在全民健身中心举办南京内部赛." +
										"南京极真空手道将在全民健身中心举办南京内部赛." +
										"南京极真空手道将在全民健身中心举办南京内部赛." +
										"南京极真空手道将在全民健身中心举办南京内部赛" +
										"南京极真空手道将在全民健身中心举办南京内部赛" +
										"南京极真空手道将在全民健身中心举办南京内部赛" +
										"南京极真空手道将在全民健身中心举办南京内部赛" +
										"南京极真空手道将在全民健身中心举办南京内部赛" +
										"南京极真空手道将在全民健身中心举办南京内部赛" +
										"南京极真空手道将在全民健身中心举办南京内部赛" +
										"南京极真空手道将在全民健身中心举办南京内部赛" +
										"南京极真空手道将在全民健身中心举办南京内部赛" +
										"南京极真空手道将在全民健身中心举办南京内部赛" +
										"南京极真空手道将在全民健身中心举办南京内部赛" +
										"南京极真空手道将在全民健身中心举办南京内部赛",
								"", "",10);
						List<Comment> cl = new ArrayList<Comment>();
						cl.add(new Comment("我叫何博学","何博伟","2013-6-30"));
						cl.add(new Comment("我叫何博学","何博伟","2013-6-30"));
						currCommentNum += cl.size();
						totalCommentNum = actiInfoDetail.getCommentNum();
						actiInfoDetail.setCommentList(cl);
						
						return actiInfoDetail;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ActiInfoDetail result) {
			if (result != null) {
				actiDetail.setText(result.getActiDetail());
				clubIcon.setImageResource(R.drawable.ic_launcher);
				actiPic.setImageResource(R.drawable.ic_launcher);
				moreComment.setText(">>>共"+result.getCommentNum()+"条评论,点击加载更多评论.");
				List<Comment> cl = result.getCommentList();
				commentAdapter.setCommentList(cl);
				commentAdapter.notifyDataSetChanged();
				progressDialog.cancel();
			}
		}

	}
	
	private class RequestMoreComment extends AsyncTask<URL,Integer,List<Comment> >
	{

		@Override
		protected List<Comment> doInBackground(URL... params) {
			// TODO Auto-generated method stub
			URL url = params[0];
			int response = -1;
			InputStream in = null;
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
						List<Comment> cl = new ArrayList<Comment>();
						cl.add(new Comment("我叫何博学","何博伟","2013-6-30"));
						cl.add(new Comment("我叫何博学","何博伟","2013-6-30"));
						currCommentNum += cl.size();
						
						return cl;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override 
		protected void onPostExecute(List<Comment> cl)
		{
			if(cl != null)
			{
				commentAdapter.addCommentList(cl);
				commentAdapter.notifyDataSetChanged();
				if(currCommentNum >= totalCommentNum)
				{
					moreComment.setText(">>>共"+totalCommentNum+"条评论,没有更多评论.");
				}
				progressDialog.cancel();
			}
			
		}
		
	}
	
	
	private class SendComment extends AsyncTask<URL,Integer,Integer>
	{

		@Override
		protected Integer doInBackground(URL... params) {
			// TODO Auto-generated method stub
			URL url = params[0];
			int response = -1;
			InputStream in = null;
			URLConnection conn;
			String msg = commentContent.getText().toString();
			if(msg.replaceAll(" ", "")!=null+"" && msg.replaceAll(" ", "")!="")
			{
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
			}

			
			
			return 0;
		}
		
		@Override
		protected void onPostExecute(Integer result)
		{
			if(result == 1)
			{
				commentContent.setText(null);
				Toast.makeText(context, "评论成功", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(context, "评论失败", Toast.LENGTH_SHORT).show();
			}
			progressDialog.cancel();
		}
		
		
	}
	
	

}
