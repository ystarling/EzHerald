package com.herald.ezherald.radio;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.herald.ezherald.R;
import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.UserAccount;
import com.herald.ezherald.mainframe.SecondMenuFragment;

public class RadioDemandSongFragment extends Fragment {
	private Button btn_demand;
	private EditText edt_song,edt_message;
	private final static int SUCCESS = 1;
	private final static int FAILED = 0;
	private final static String URL = "http://herald.seu.edu.cn/seub/mobile/comment/";
	private ProgressDialog progress;
	private boolean isDetaced;
	private String jsonStr;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				onSuccess();
				break;
			case FAILED:
				onFailed();
				break;
			}
		}

		private void onFailed() {
			if( !isDetaced ) {
				progress.cancel();
				Toast.makeText(getActivity(), "点歌失败", Toast.LENGTH_LONG).show();
			}
			
		}

		private void onSuccess() {
			if ( !isDetaced ) {
				progress.cancel();
				Toast.makeText(getActivity(), "点歌成功", Toast.LENGTH_LONG).show();
			}
			
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		isDetaced = false;
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.radio_demand_song, container,false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initView();
		super.onActivityCreated(savedInstanceState);
	}
	private void  initView() {
		progress = new ProgressDialog(getActivity());
		progress.setTitle("正在提交");
		progress.setCancelable(true);
		progress.setIndeterminate(true);
		btn_demand = (Button)getActivity().findViewById(R.id.btn_demand);
		edt_message = (EditText)getActivity().findViewById(R.id.edt_message);
		edt_song = (EditText)getActivity().findViewById(R.id.edt_song);
		btn_demand.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				progress.show();
				new Thread() {
					@Override
					public void run() {
						try {
							HttpClient client = new DefaultHttpClient();
							HttpPost post = new HttpPost(URL);
							List<NameValuePair> params = new ArrayList<NameValuePair>();
							String message = "歌名:"+edt_song.getText().toString()+"\n";
							message.concat("留言:"+edt_message.getText().toString());
							UserAccount user = Authenticate.getIDcardUser(getActivity());
							//SecondMenuFragment second = new SecondMenuFragment();
							Log.v("message",message);
							Log.v("author",getSavedUserName(user.getUsername()));
							Log.v("card",user.getUsername());
							params.add(new BasicNameValuePair("content", message));
							params.add(new BasicNameValuePair("author", getSavedUserName(user.getUsername())));
							params.add(new BasicNameValuePair("card_num", user.getUsername()));
							post.setEntity(new UrlEncodedFormEntity(params));
							HttpResponse response = client.execute(post);
							if( response.getStatusLine().getStatusCode() == 200 ) {
								jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
								if(jsonStr.equals("success")){
									handler.obtainMessage(SUCCESS).sendToTarget();
								}else{
									handler.obtainMessage(FAILED).sendToTarget();
								}
								
							} else {
								handler.obtainMessage(FAILED).sendToTarget();
							}
						} catch (Exception e) {
							e.printStackTrace();
							handler.obtainMessage(FAILED).sendToTarget();
						} 
					};
				}.start();
			}
		});
	}
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		isDetaced = true;
		super.onDetach();
	}
	
	public String getSavedUserName(String card){
		String url = "http://herald.seu.edu.cn/EzHerald/getname/?cardnum="+card;
		HttpGet get = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse;
		String name="";
		try {
			httpResponse = httpClient.execute(get);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				name = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e("name Not Found","");
			}
				
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
	}
		
}
