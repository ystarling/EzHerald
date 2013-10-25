package com.herald.ezherald.activity;

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

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;
import com.herald.ezherald.academic.DataTypeTransition;
import com.herald.ezherald.account.Authenticate;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ClubDetailIntroFragment extends SherlockFragment {
	
	View v;
	Context context;
	ClubDetailIntro clubDetailIntro;
	CommentListAdapter adapter ;
	int comNum = 0;
	int totalComNum = 0;
	
	TextView tvIntro;
	ListView comList;
	ProgressBar introProgress;
	ProgressBar msgProgress;
	TextView tvName;
	Button btnFocus;
	ImageView tvIcon;
	TextView tvMoreCom;
	TextView tvComNum;
	EditText writeCom;
	Button btnSend;
	ProgressDialog progressDialog;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		context = getActivity();
		clubDetailIntro = new ClubDetailIntro();
	}
	
	
	@SuppressLint("NewApi")
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		v = inflater.inflate(R.layout.acti_club_detail_intro, null);
		Bundle bundle = getActivity().getIntent().getExtras();
		clubDetailIntro.clubName = bundle.getString("clubName");
		clubDetailIntro.haveFocus = bundle.getBoolean("focus");
		clubDetailIntro.clubId = bundle.getInt("clubid");
		byte [] bytes_icon = bundle.getByteArray("icon");
		tvName = (TextView) v.findViewById(R.id.acti_club_detail_name);
		btnFocus = (Button) v.findViewById(R.id.acti_club_detail_focus);
		tvIcon = (ImageView) v.findViewById(R.id.acti_club_detail_icon);
		tvMoreCom = (TextView) v.findViewById(R.id.acti_club_detail_more_msg);
		comList = (ListView) v.findViewById(R.id.acti_club_detail_msg_list);
		tvIntro = (TextView) v.findViewById(R.id.acti_club_detail_intro);
		introProgress = (ProgressBar) v.findViewById(R.id.acti_club_detail_intro_progress);
		msgProgress  = (ProgressBar) v.findViewById(R.id.acti_club_detail_msg_progress);
		tvComNum = (TextView) v.findViewById(R.id.acti_club_detail_more_msg);
		writeCom = (EditText) v.findViewById(R.id.acti_club_detail_write_msg);
		btnSend = (Button) v.findViewById(R.id.acti_club_detail_send_msg);
		
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("Please wait ... ");
		
//		tvIcon.setImageResource(R.drawable.ic_launcher);
		tvIcon.setImageBitmap(BitmapFactory.decodeByteArray(bytes_icon, 0, bytes_icon.length));
		tvName.setText(clubDetailIntro.clubName);
		
		adapter = new CommentListAdapter(context);
		comList.setAdapter(adapter);
		
		if(clubDetailIntro.haveFocus)
		{
			btnFocus.setText("取消关注");
			btnFocus.setBackgroundColor(Color.GRAY);
		}
		else
		{
			btnFocus.setText("关注");
			btnFocus.setBackgroundColor(Color.rgb(100, 100, 255));
		}
		
		btnFocus.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				progressDialog.show();
				
				try {
					new RequestFocus().execute(new URL("http://jwc.seu.edu.cn"));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		tvMoreCom.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					progressDialog.show();
					new RequestMoreComment().execute(new URL("http://jwc.seu.edu.cn"));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		btnSend.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					progressDialog.show();
					String cardNum = Authenticate.getIDcardUser(context).getUsername().trim();
					String url_str="http://herald.seu.edu.cn/herald_league_api/index.php/command/comment/senderid/" +
							+Integer.parseInt(cardNum)+"/sendertype/1/receiveid/"+clubDetailIntro.clubId+"/receivetype/2";
					new SendComment().execute(new URL(url_str));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		
		//Toast.makeText(getActivity(), bundle.getString("clubName","no clubName"), Toast.LENGTH_SHORT).show();
		try {
			new RequestClubDetailIntro().execute(new URL(getResources().getString(R.string.acti_url_club_intro)
					+clubDetailIntro.clubId ));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return v;
	}
	
	private class RequestClubDetailIntro extends AsyncTask<URL,Integer,ClubDetailIntro>{

		@Override
		protected ClubDetailIntro doInBackground(URL... params) {
			// TODO Auto-generated method stub
			//ClubDetailIntro clubDetailIntro = new ClubDetailIntro();
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
						JSONObject jsonObj = new JSONObject(str);
						clubDetailIntro.clubIntro = jsonObj.getString("introduce");
						clubDetailIntro.comNum = Integer.parseInt(jsonObj.getString("comment_num") );
						JSONArray jsonArr = jsonObj.getJSONArray("comment");
						for(int i=0;i<jsonArr.length();++i)
						{
							JSONObject com = jsonArr.getJSONObject(i);
							int id = Integer.parseInt(com.getString("comment_id"));
							String content = com.getString("content");
							String sender = com.getString("sender");
							String date = com.getString("comment_time");
							clubDetailIntro.comList.add(new Comment(id,content,sender,date));
						}
						//Log.v("Net test", str);
//						clubDetailIntro.clubName = "电竞社";
//						clubDetailIntro.clubIconUrl = "";
//						clubDetailIntro.clubIntro = "这里是社团介绍，尽量详细地展示你的社团";
//						clubDetailIntro.haveFocus = true;
//						clubDetailIntro.comList.add(new Comment(1,"dota dota dota","无名小卒","2013-6-30"));
//						clubDetailIntro.comList.add(new Comment(2,"hahhahahha","he bo xue","2013-6-30"));
//						clubDetailIntro.comNum = 20;
						
						return clubDetailIntro;
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
		protected void onPostExecute(ClubDetailIntro intro)
		{
			if(intro != null)
			{
				introProgress.setVisibility(View.GONE);
				msgProgress.setVisibility(View.GONE);
				
				adapter.setCommentList(intro.comList);
				adapter.notifyDataSetChanged();
				comNum += intro.comList.size();
				totalComNum = intro.comNum;
				
				tvIntro.setText(intro.clubIntro);	
				tvComNum.setText(">>>共"+intro.comNum+"条留言,点击查看更多留言.");
			}
			else
			{
				Toast.makeText(context, "请求失败！", Toast.LENGTH_SHORT).show();
			}
			
		}
		
	}
	
	private class ClubDetailIntro
	{
		public int clubId;
		public String clubName;
		public String clubIconUrl;
		public boolean haveFocus;
		public String clubIntro;
		public List<Comment> comList;
		public int comNum;
		
		public ClubDetailIntro()
		{
			comList = new ArrayList<Comment>();
		}
	}

	private class RequestFocus extends AsyncTask<URL,Integer,Integer>
	{

		@Override
		protected Integer doInBackground(URL... params) {
			// TODO Auto-generated method stub
			
			int response = -1;
			InputStream in = null;
			URLConnection conn;
			URL url = params[0];
			
			try {
				conn = url.openConnection();
				if (!(conn instanceof HttpURLConnection)) {
					throw new IOException("NOT AN HTTP CONNECTION");
				} else
				{
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
				
			return 0;
		}
		
		@Override
		protected void onPostExecute(Integer result)
		{
			Button btn = (Button) v.findViewById(R.id.acti_club_detail_focus);
			if (result == 1)
			{
				if( !clubDetailIntro.haveFocus)
				{
					clubDetailIntro.haveFocus = true;
					btn.setText("取消关注");
					btn.setBackgroundColor(Color.GRAY);
				}
				else
				{
					clubDetailIntro.haveFocus = false;
					btn.setText("关注");
					btn.setBackgroundColor(Color.rgb(100, 100, 255));
				}
			}
			progressDialog.cancel();
		}
		
	}
	
	private class  RequestMoreComment extends AsyncTask<URL,Integer,List<Comment> >{

		@Override
		protected List<Comment> doInBackground(URL... params) {
			// TODO Auto-generated method stub
			int response = -1;
			InputStream in = null;
			URLConnection conn;
			URL url = params[0];
			
			try {
				conn = url.openConnection();
				if (!(conn instanceof HttpURLConnection)) {
					throw new IOException("NOT AN HTTP CONNECTION");
				} else
				{
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
						cl.add(new Comment(1,"大家好，我是何博伟","何博伟","2013-7-2"));
						cl.add(new Comment(2,"大家好，我是何博伟","何博伟","2013-7-2"));
						cl.add(new Comment(3,"大家好，我是何博伟","何博伟","2013-7-2"));
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
		protected  void onPostExecute(List<Comment> cl)
		{
			comNum += cl.size();
			adapter.addCommentList(cl);
			adapter.notifyDataSetChanged();

			if(comNum >= totalComNum)
			{
				tvComNum.setText(">>>共"+totalComNum+"条留言,没有更多留言.");
				tvMoreCom.setOnClickListener(null);
			}
			progressDialog.cancel();
		}
		
	}
	
	private class SendComment extends AsyncTask<URL,Integer,Integer>{

		@Override
		protected Integer doInBackground(URL... params) {
			// TODO Auto-generated method stub
			int response = -1;
			InputStream in = null;
			URLConnection conn;
			URL url = params[0];
			String msg = writeCom.getText().toString();
			if(msg.replaceAll(" ", "")!=null+"" && msg.replaceAll(" ", "")!="")
			{
				try {
					conn = url.openConnection();
					if (!(conn instanceof HttpURLConnection)) {
						throw new IOException("NOT AN HTTP CONNECTION");
					} else
					{
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
		protected void onPostExecute(Integer result){
			if(result == 1)
			{
				writeCom.setText(null);
				Toast.makeText(context, "留言发送成功", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(context, "留言发送失败", Toast.LENGTH_SHORT).show();
			}
			progressDialog.cancel();
			
		}
		
	}
	
	
	

}
