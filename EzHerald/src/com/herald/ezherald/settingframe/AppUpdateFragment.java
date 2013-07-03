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
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;

/**
 * @author xie
 *
 */
public class AppUpdateFragment extends SherlockFragment {
	private int newVersion;
	private String uri;
	private String description;
	private ProgressDialog progress;
	private final String fileName ="ezHerald"+ newVersion+".apk"; 
	/**
	 * @return boolean 是否有新版本
	 */
	public boolean checkUpdate(){
		//TODO use API
		return true;
	}
	
	public void update(){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("新的版本,是否升级");
		LayoutInflater inflater = getActivity().getLayoutInflater();
		builder.setView(inflater.inflate(R.layout.app_update,null));
		TextView txt_version = (TextView) getActivity().findViewById(R.id.txt_version);
		txt_version.setText("版本号："+newVersion);
		TextView txt_description = (TextView) getActivity().findViewById(R.id.txt_description);
		txt_description.setText(description);
		builder.setPositiveButton("更新",new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int id) {
				// TODO Auto-generated method stub
				progress = new ProgressDialog(getActivity());
				progress.setTitle("正在下载...");
				progress.setMessage("请稍后");
				progress.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
				progress.show();
				download();
			}

		} );
		builder.setNegativeButton("下次再说", new OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
			
		});
		builder.show();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
if(checkUpdate()){
			update();
		}else{
			getActivity().finish();
		}
	}

	/**
	 * @return int 本地的版本号 manifest里面
	 */
	public int getVersionCode(){
		PackageManager manager= getActivity().getPackageManager();
		int versionCode=-1;
		try {
			versionCode = manager.getPackageInfo(getActivity().getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			Log.e("nameNotFound",e.getMessage());
		}
		return versionCode;
	}
	private void download(){
		
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
	                FileOutputStream fileOutputStream = null;  
	                if (is != null) {  
	                    File file = new File(  
	                            Environment.getExternalStorageDirectory(),fileName);  
	                    fileOutputStream = new FileOutputStream(file);  
	                    byte[] buf = new byte[1024];  
	                    int ch = -1;  
	                    while ((ch = is.read(buf)) != -1) {  
	                        fileOutputStream.write(buf, 0, ch);
	                    }  
	                }  
	                fileOutputStream.flush();  
	                if (fileOutputStream != null) {  
	                    fileOutputStream.close();  
	                }  
	                onDownloadFinish();  
	            } catch (ClientProtocolException e) {  
	                e.printStackTrace();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
			}
		}.start();
	}
	private void onDownloadFinish(){
        progress.cancel();  
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), fileName)),
                "application/vnd.android.package-archive");
        startActivity(intent);
	}
}
