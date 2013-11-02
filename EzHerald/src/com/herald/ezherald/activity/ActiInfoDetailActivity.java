package com.herald.ezherald.activity;

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
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.R;
import com.herald.ezherald.academic.DataTypeTransition;
import com.herald.ezherald.account.Authenticate;



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
	EditText commentContent;
	Button sendComment;
	ScrollView scrollView;
	TextView time;
	TextView place;
	
	int currCommentNum;
	int totalCommentNum;
	
	private AsyncTask<URL, Integer, ActiInfoDetail> detailTask;
	private AsyncTask<ViewAndUrl,Integer,Bitmap> iconTask;
	private AsyncTask<ViewAndUrl,Integer,Bitmap> imageTask;
	
	
	private byte [] bytes_icon;
	

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acti_acti_info_detail);
		context  = this;
		currCommentNum = 0;
		totalCommentNum = 0;
		actiInfoDetail = new ActiInfoDetail();
		
		initView();
        
        initProgressDialog();	
		
		onRequestStart();
		
		try {
			onRequestStart();
			iconTask = new RequestImage();
			iconTask.execute(new ViewAndUrl(clubIcon,new URL(actiInfoDetail.getClubIconUrl()),
					0,actiInfoDetail.getActiId()));
			detailTask = new RequestActiInfoDetail();
			detailTask.execute(new URL(getResources().getString(R.string.acti_url_activity_detail)
					+actiInfoDetail.getActiId()) );
			imageTask = new RequestImage();
			imageTask.execute(new ViewAndUrl(actiPic,new URL(actiInfoDetail.getActiPicUrl()),
					1,actiInfoDetail.getActiId()));
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Log.v("ICON", "error: "+e);
			e.printStackTrace();
			onRequestCompleted();
		}
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
	}
	
	@Override
	public void onDestroy()
	{
		if(detailTask != null && detailTask.getStatus()==AsyncTask.Status.RUNNING)
		{
			detailTask.cancel(true);
		}
		if(iconTask != null && iconTask.getStatus() == AsyncTask.Status.RUNNING)
		{
			iconTask.cancel(true);
		}
		if(imageTask != null && imageTask.getStatus() == AsyncTask.Status.RUNNING)
		{
			imageTask.cancel(true);
		}
		
		super.onDestroy();
	}
	
	public void initProgressDialog()
	{
		progressDialog = new ProgressDialog(context);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("请稍候 ... ");
	}
	
	public void onRequestStart()
	{
		progressDialog.show();
	}
	
	public void onRequestCompleted()
	{
		progressDialog.cancel();
	}
	
	public void initView()
	{
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
		time = (TextView) findViewById(R.id.acti_detail_acti_time);
		place = (TextView) findViewById(R.id.acti_detail_acti_place);
		
		
		listView.setParentScrollView(scrollView);
        listView.setMaxHeight(10000);
		
		
		Bundle bundle = this.getIntent().getExtras();
		actiInfoDetail.setActiId(bundle.getInt("actiId"));
		actiInfoDetail.setClubId(bundle.getInt("clubId"));
		actiInfoDetail.setClubName(bundle.getString("clubName"));
		actiInfoDetail.setIsVote(bundle.getBoolean("isVote"));
		actiInfoDetail.setIconName(bundle.getString("iconName"));
		actiInfoDetail.setActiPicName(bundle.getString("picName"));
		actiInfoDetail.setActiTitle(bundle.getString("title"));
		
		clubName.setText(bundle.getString("clubName"));
		actiTitle.setText(bundle.getString("title"));
		actiPubTime.setText(bundle.getString("date"));
		time.setText(bundle.getString("startTime")+"至"+bundle.getString("endTime"));
		place.setText(bundle.getString("place"));
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
					actiInfoDetail.getClubName()+" 发布了新活动《"+actiInfoDetail.getActiTitle()+"》"+"(消息来自先声客户端)");
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
			
			if(isCancelled())
			{
				return null;
			}
			
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
						JSONArray jsonArr = new JSONArray(str);
						JSONObject jsonObj = (JSONObject) jsonArr.get(0);
						actiInfoDetail.setActiPicName(jsonObj.getString("post_add"));
						actiInfoDetail.setActiIntro(jsonObj.getString("introduction"));
						actiInfoDetail.setCommentNum(Integer.parseInt(jsonObj.getString("comment_num")));
//						actiInfoDetail.setCommentNum(10);
						JSONArray comJsonArr = jsonObj.getJSONArray("comment");
						if(actiInfoDetail.checkIsVote())
						{
							JSONObject voted = jsonObj.getJSONObject("havaVote");
							JSONArray voteJsonArr = jsonObj.getJSONObject("vote_info").getJSONArray("vote_item_info");
							Map<String,Integer> result = new HashMap<String,Integer>();
							for(int i=0;i<voteJsonArr.length();++i)
							{
								JSONObject obj = voteJsonArr.getJSONObject(i);
								result.put(obj.getString("name"), Integer.parseInt(obj.getString("suport_num")));
							}
							actiInfoDetail.setVoteResult(result);
						}
						totalCommentNum = actiInfoDetail.getCommentNum();

						return actiInfoDetail;
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
		protected void onPostExecute(ActiInfoDetail result) {
			try{
				if (result != null) {
					actiDetail.setText( Html.fromHtml(Html.fromHtml(Html.fromHtml(result.getActiDetail()).toString()).toString()));	
				}
			}
			catch(Exception e)
			{
				Log.e("REQUEST ERROR", "error occured when requesting");
			}
			onRequestCompleted();
			
		}

	}
	


}
