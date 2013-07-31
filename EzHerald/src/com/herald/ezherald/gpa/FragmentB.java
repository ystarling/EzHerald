
package com.herald.ezherald.gpa;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.herald.ezherald.R;

public class FragmentB extends Fragment {
	private ExpandableListView elv;
	private TextView txtGpa;
	private Button btnUpdate,btnCalc,btnRemoveOptional;
	private final int SUCCESS = 1,FAILED = 0;
	private Bitmap bitmap;
	private ImageView imageView;
	private AlertDialog dialog;
	private int vercode;
	private View view ;
	private HttpClient client;
	ProgressDialog progress;
    private Handler handler = new Handler(){
		
    	@Override
		public void handleMessage(Message msg) {
			Log.w("handle","msg");
    		
			switch (msg.what){
				case FAILED:
					bitmap = null;
					onLoadImageFailed();
					break;
				case SUCCESS:
					bitmap = (Bitmap) msg.obj;
					onLoadImage();
					break;
				default:
					break;
			}
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved)
	{
		return inflater.inflate(R.layout.gpa_frag_b, group, false);
	}
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		progress = new ProgressDialog(getActivity());
		progress.setTitle("正在获取数据");
		progress.setIndeterminate(true);//圈圈而不是进度条
		progress.setCancelable(false);
		txtGpa = (TextView)getActivity().findViewById(R.id.txt_gpa);
		elv = (ExpandableListView)getActivity().findViewById(R.id.eList);
		final GpaAdapter adapter = new GpaAdapter(getActivity(),progress);
		elv.setAdapter(adapter);
		btnUpdate = (Button)getActivity().findViewById(R.id.btn_update);
		btnUpdate.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setIcon(R.drawable.ic_launcher);
				builder.setTitle("请输入验证码");
				LayoutInflater inflater = getActivity().getLayoutInflater();
				view = inflater.inflate(R.layout.gpa_veryfiy_code, null);
				imageView = (ImageView) view.findViewById(R.id.imageView);
				getveryfyCode();
				imageView.setImageBitmap(bitmap);
				builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						EditText edtVercode= (EditText)view.findViewById(R.id.edt_veryfy_code);
						try {
							vercode = Integer.parseInt(edtVercode.getText()
									.toString());
						} catch (Exception e) {
							// TODO: handle exception
							vercode  = 0;
						}
						Toast.makeText(getActivity(), "正在更新", Toast.LENGTH_SHORT).show();
						
						progress.show();
						adapter.update(vercode,client);
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				builder.setView(view);
				
				dialog = builder.create();
				dialog.setCancelable(false);
				dialog.show();
				
				//TODO 更新时的动画
			}
		});
		btnCalc = (Button)getActivity().findViewById(R.id.btn_calc);
		btnCalc.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				txtGpa.setText(String.format("所选绩点为:%.2f", adapter.getGpaInfo().calcAverage()));
			}
			
		});
		
		btnRemoveOptional = (Button)getActivity().findViewById(R.id.btn_remove_optional);
		btnRemoveOptional.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				adapter.removeOptional();
			}
		});
		
	}
	private void getveryfyCode(){
		final String url = "http://xk.urp.seu.edu.cn/studentService/getCheckCode";
    	new Thread(){
    		@Override
    		public void run() {
    			try {
					client = new DefaultHttpClient();
					HttpGet get = new HttpGet(url);
					HttpResponse response = client.execute(get);
					if (response.getStatusLine().getStatusCode() != 200) {
						throw new Exception();
					}
					InputStream is = response.getEntity().getContent();
					Message msg = handler.obtainMessage(SUCCESS,
							BitmapFactory.decodeStream(is));
					handler.sendMessage(msg);
					is.close();
				} catch (Exception e) {
					handler.obtainMessage(FAILED).sendToTarget();
				}
    		}
  	}.start();
}
	private void onLoadImage(){
		imageView.setImageBitmap(bitmap);
	}
	private void onLoadImageFailed() {
		Toast.makeText(getActivity(), "获取验证码失败",Toast.LENGTH_SHORT).show();
		dialog.cancel();
	}
}
