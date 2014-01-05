package com.herald.ezherald.radio;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.herald.ezherald.R;


public class RadioForcastFragment extends Fragment {
	private SharedPreferences shared;
	private String forcast;
	private Context context;
	private TextView txtForcast;
	private Button btnUpdate;
	private boolean isDetached;
	private final static String URL = "";
	private final static int SUCCESS = 1;
	private final static int FAILED = 0;
	private Handler handler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			if( !isDetached ) {
				btnUpdate.setText("更新");
				switch (msg.what) {
				case SUCCESS:
					Toast.makeText(context, "更新成功", Toast.LENGTH_LONG).show();
					btnUpdate.setText(forcast);
					break;
				case FAILED:
				default:
					Toast.makeText(context, "更新失败", Toast.LENGTH_LONG).show();
					break;
				}
			}
			
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.radio_forcast, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		txtForcast = (TextView) getActivity().findViewById(R.id.txt_boardcast);
		btnUpdate = (Button) getActivity().findViewById(R.id.btn_update);
		context = getActivity();
		shared = context.getSharedPreferences("radio", 0);
		forcast = shared.getString("forcast", null);
		if( forcast != null ){
			txtForcast.setText(forcast);
		}else{
			txtForcast.setText("请先更新");
		}
		btnUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnUpdate.setText("正在更新....");
				new Thread(){
					public void run(){
						try {
							HttpClient client = new DefaultHttpClient();
							HttpGet get = new HttpGet(URL);
							HttpResponse response = client.execute(get);
							if( response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
								forcast = EntityUtils.toString(response.getEntity());
								handler.obtainMessage(SUCCESS).sendToTarget();
							}
						}catch(Exception e){
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
		super.onDetach();
		isDetached = true;
	}
}
