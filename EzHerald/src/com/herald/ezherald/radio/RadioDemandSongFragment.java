package com.herald.ezherald.radio;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.herald.ezherald.R;

public class RadioDemandSongFragment extends Fragment {
	private Button btn_demand;
	private EditText edt_song,edt_message;
	private final static int SUCCESS = 1;
	private final static int FAILED = 0;
	private final static String URL = "www.baidu.com";
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
							params.add(new BasicNameValuePair("", ""));
							post.setEntity(new UrlEncodedFormEntity(params));
							HttpResponse response = client.execute(post);
							if( response.getStatusLine().getStatusCode() == 200 ) {
								jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
								handler.obtainMessage(SUCCESS).sendToTarget();
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
}
