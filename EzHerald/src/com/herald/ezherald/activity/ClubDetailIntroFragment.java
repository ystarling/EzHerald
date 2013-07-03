package com.herald.ezherald.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;
import com.herald.ezherald.academic.DataTypeTransition;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
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
		progressDialog.setMessage("Please waiting ... ");
		
		tvIcon.setImageResource(R.drawable.ic_launcher);
		tvName.setText(clubDetailIntro.clubName);
		
		adapter = new CommentListAdapter();
		comList.setAdapter(adapter);
		
		if(clubDetailIntro.haveFocus)
		{
			btnFocus.setText("ȡ����ע");
			btnFocus.setBackgroundColor(Color.GRAY);
		}
		else
		{
			btnFocus.setText("��ע");
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
					new SendComment().execute(new URL("http://jwc.seu.edu.cn"));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		
		//Toast.makeText(getActivity(), bundle.getString("clubName","no clubName"), Toast.LENGTH_SHORT).show();
		try {
			new RequestClubDetailIntro().execute(new URL("http://jwc.seu.edu.cn"));
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
						//Log.v("Net test", str);
						clubDetailIntro.clubName = "�羺��";
						clubDetailIntro.clubIconUrl = "";
						clubDetailIntro.clubIntro = "���������Ž��ܣ�������ϸ��չʾ�������";
						clubDetailIntro.haveFocus = true;
						clubDetailIntro.comList.add(new Comment("dota dota dota","����С��","2013-6-30"));
						clubDetailIntro.comList.add(new Comment("hahhahahha","he bo xue","2013-6-30"));
						clubDetailIntro.comNum = 20;
						
						return clubDetailIntro;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		
		@Override 
		protected void onPostExecute(ClubDetailIntro intro)
		{
			introProgress.setVisibility(View.GONE);
			msgProgress.setVisibility(View.GONE);
			
			adapter.setCommentList(intro.comList);
			adapter.notifyDataSetChanged();
			comNum += intro.comList.size();
			totalComNum = intro.comNum;
			
			tvIntro.setText(intro.clubIntro);	
			tvComNum.setText(">>>��"+intro.comNum+"������,����鿴��������.");
		}
		
	}
	
	private class ClubDetailIntro
	{
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
	
	
	
	private class Comment
	{
		public String content;
		public String name;
		public String date;
		
		public Comment(String con, String n, String d)
		{
			content = con;
			name = n;
			date = d;
		}
	}
	
	private class CommentListAdapter extends BaseAdapter
	{
		List<Comment> comList;
		
		public CommentListAdapter()
		{
			comList = new ArrayList<Comment>();
		}
		
		public void setCommentList(Comment [] comArr)
		{
			comList.clear();
			for (int loop = 0; loop < comArr.length; ++ loop)
			{
				comList.add(comArr[loop]);
			}
		}
		
		public void addCommentList(Comment [] comArr)
		{
			for (int loop = 0; loop < comArr.length; ++ loop)
			{
				comList.add(comArr[loop]);
			}
		}
		
		public void setCommentList(List<Comment> cl)
		{
			comList.clear();
			comList.addAll(cl);
		}
		
		public void addCommentList(List<Comment> cl)
		{
			comList.addAll(cl);
		}
		

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return comList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return comList.get(position);
			
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = LayoutInflater.from(context).inflate(R.layout.acti_club_detail_comment_list_item, null);
			Comment comment = comList.get(position);
			TextView tvCommenter = (TextView) convertView.findViewById(R.id.acti_club_detail_comment_comenter);
			TextView tvDate=  (TextView) convertView.findViewById(R.id.acti_club_detail_comment_date);
			TextView tvContent = (TextView) convertView.findViewById(R.id.acti_club_detail_comment_content);
			tvCommenter.setText(comment.name);
			tvDate.setText(comment.date);
			tvContent.setText(comment.content);
			
			return convertView;
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
					btn.setText("ȡ����ע");
					btn.setBackgroundColor(Color.GRAY);
				}
				else
				{
					clubDetailIntro.haveFocus = false;
					btn.setText("��ע");
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
						cl.add(new Comment("��Һã����Ǻβ�ΰ","�β�ΰ","2013-7-2"));
						cl.add(new Comment("��Һã����Ǻβ�ΰ","�β�ΰ","2013-7-2"));
						cl.add(new Comment("��Һã����Ǻβ�ΰ","�β�ΰ","2013-7-2"));
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
				tvComNum.setText(">>>��"+totalComNum+"������,û�и�������.");
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
				Toast.makeText(context, "���Է��ͳɹ�", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(context, "���Է���ʧ��", Toast.LENGTH_SHORT).show();
			}
			progressDialog.cancel();
			
		}
		
	}
	
	
	

}