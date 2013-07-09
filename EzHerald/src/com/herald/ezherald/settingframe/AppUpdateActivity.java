package com.herald.ezherald.settingframe;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.seu.herald.ws.api.AndroidClientUpdateService;
import cn.edu.seu.herald.ws.api.HeraldWebServicesFactory;
import cn.edu.seu.herald.ws.api.impl.HeraldWebServicesFactoryImpl;
import cn.edu.seu.herald.ws.api.update.Update;

import com.herald.ezherald.R;

/**
 * @author xie
 *
 */
public class AppUpdateActivity extends Activity {
	private int newVersion = 2;
	private boolean isForce = false;
	private String uri = "http://herald.seu.edu.cn/index/ez.apk" ;
	private String description = "test";
	private ProgressDialog progress;
	private int down = 0;
	private long fullSize;//���°����ܴ�С 
	private final String fileName ="ezHErald"+ newVersion+".apk"; 
	private final int SUCCESS = 1;
	private final int FAILED  = 0;
	private final int DOING   = 2;
	private final boolean DEBUG = true;
	
	private Handler mhandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
				case SUCCESS:
					onDownloadFinish(); 
					break;
				case FAILED:
					onFailed();
					break;
				case DOING:
					showProgress((Long) msg.obj);
					Log.w("download",""+(Integer) msg.obj);
					break;
				default:
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_update_main);
		

		if(DEBUG || checkUpdate() ){
			update();
		}else{
			this.finish();
		}
	}
	/**
	 * @return boolean �Ƿ����°汾
	 */
	public boolean checkUpdate(){
		try {
			//TODO use API ,API��û��ʵ��
			final String HERALD_WS_BASE_URI = "http://herald.seu.edu.cn/ws";
			HeraldWebServicesFactory factory = new HeraldWebServicesFactoryImpl(
					HERALD_WS_BASE_URI);
			AndroidClientUpdateService updateService = factory
					.getAndroidClientUpdateService();
			Update update = updateService.getNewVersion();
			newVersion = Integer.valueOf(update.getVersion());
			if (newVersion > getVersionCode()) {
				uri = update.getUri();
				description = update.getInfo();
				//isForce = update.getForece(); 
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(this, "������ʧ��", Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	
	public void update(){
		if(isForce){
			download();
			return ;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("�µİ汾,�Ƿ�����");
		LayoutInflater inflater = this.getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.app_update,null);
		builder.setView(dialogView);
		builder.setPositiveButton("����",new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int id) {
				// TODO Auto-generated method stub

				download();
			}

		} );
		builder.setNegativeButton("�´���˵", new OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				AppUpdateActivity.this.finish();
			}
			
		});
		
		AlertDialog dialog  = builder.create();
		
		TextView txt_version = (TextView) dialogView.findViewById(R.id.txt_version);
		txt_version.setText("�汾�ţ�"+newVersion);
		TextView txt_description = (TextView) dialogView.findViewById(R.id.txt_description);
		txt_description.setText(description);
		dialog.setCancelable(false);
		dialog.show();
	}

	/**
	 * @return int ���صİ汾�� manifest����
	 */
	public int getVersionCode(){
		PackageManager manager= this.getPackageManager();
		int versionCode=-1;
		try {
			versionCode = manager.getPackageInfo(this.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			Log.e("nameNotFound",e.getMessage());
		}
		return versionCode;
	}
	/**
	 * ��һ���߳�ִ������
	 */
	private void download(){
		progress = new ProgressDialog(AppUpdateActivity.this);
		progress.setTitle("��������...");
		progress.setMessage("���Ժ�");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
		progress.setCancelable(false);
		progress.show();
		new Thread(){
			@Override
			public void run(){
				HttpClient client = new DefaultHttpClient();  
	            HttpGet get = new HttpGet(uri);  
	            HttpResponse response;  
	            try {  
	                response = client.execute(get);  
	                HttpEntity entity = response.getEntity();  
	                InputStream is = entity.getContent();  
	                fullSize = entity.getContentLength();
	                FileOutputStream fileOutputStream = null;  
	                if (is != null) {  
	                    File file = new File(  
	                            Environment.getExternalStorageDirectory(),fileName);  
	                    fileOutputStream = new FileOutputStream(file);  
	                    byte[] buf = new byte[1024];  
	                    int ch = -1;  
	                    while ((ch = is.read(buf)) != -1) {  
	                        fileOutputStream.write(buf, 0, ch);
	                        down += ch;
	                        mhandler.obtainMessage(DOING,Long.valueOf(down) ).sendToTarget();
	                    }  
	                }  
	                fileOutputStream.flush();  
	                if (fileOutputStream != null) {  
	                    fileOutputStream.close();  
	                }  
	                mhandler.obtainMessage(SUCCESS).sendToTarget();
	            } catch (ClientProtocolException e) {  
	            	mhandler.obtainMessage(FAILED).sendToTarget();
	            } catch (IOException e) {   
	            	mhandler.obtainMessage(FAILED).sendToTarget();
	            }  
			}
		}.start();
	}
	/**
	 * ���سɹ�ʱ�����
	 */
	private void onDownloadFinish(){
        progress.cancel();  
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), fileName)),
                "application/vnd.android.package-archive");
        startActivity(intent);
        
	}
	/**
	 * ����ʧ�ܵ�ʱ�����
	 */
	private void onFailed(){
		progress.cancel();
		Toast.makeText(AppUpdateActivity.this, "����ʧ��,��������", Toast.LENGTH_SHORT).show();
	}
	/**
	 * @param p ���� ��λbyte
	 * ��ʾ�����˶���
	 */
	private void showProgress(long p){
		progress.setMessage(String.format("������ %.3f", (double)p/fullSize));//TODO need tobe tested
	}
}
