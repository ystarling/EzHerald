package com.herald.ezherald.settingframe;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.herald.ezherald.R;

/**
 * @author xie
 *
 */
public class AppUpdateActivity extends Activity {
	private String newVersion = "x";
	private boolean isForce = false;
	private String uri = "http://herald.seu.edu.cn/index/ez.apk" ;
	private String description = "test";
	private ProgressDialog progress;
	private int down = 0;
	private long fullSize;//更新包的总大小 
	private final String fileName ="ezHErald"+ newVersion+".apk"; 
	private final int SUCCESS = 1;
	private final int FAILED  = 0;
	private final int DOING   = 2;
	private final String checkUrl = "http://herald.seu.edu.cn/ws/update";
	boolean needUpdate;
	boolean mIsCalledInSetting = false;
	private boolean mForceStopOperation = false;
	private long mExitTime = 0;
	
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
					//Log.w("download",""+(Long) msg.obj);
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
	
	    needUpdate = false;
		//checkUpdate();
		
		Intent intent = getIntent();
		if(intent != null)
			mIsCalledInSetting = intent.getBooleanExtra("isCalledInSetting", false);
		if(mIsCalledInSetting){
			Toast.makeText(this, "正在检测更新...", Toast.LENGTH_SHORT).show();
		}
		
		new AppUpdateAsyncTask().execute(this);

	}
	
	
	private class AppUpdateAsyncTask extends AsyncTask<Context, Void, Boolean>{
		Context mContext;
		
		@Override
		protected Boolean doInBackground(Context... params) {
			// 检测更新
			mContext = params[0];
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(checkUrl);
				Node versionNode = document.getElementsByTagName("version").item(0);
				Node uriNode = document.getElementsByTagName("uri").item(0);
				Node infoNode = document.getElementsByTagName("info").item(0);
				Node forceNode = document.getElementsByTagName("force").item(0);
				String version = versionNode.getTextContent();
				uri = uriNode.getTextContent();
				isForce = forceNode.getTextContent().equals("true");
				description = infoNode.getTextContent().replace("##NL##", "\n");
				newVersion = version;
				String currentVersionName = getVersionName();
				
				if(isNewVersion(currentVersionName, newVersion)){
					return true;
				}
			} catch (ParserConfigurationException e){
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}

		/**
		 * 判断是否是新版本
		 * 版本号按数字、字母序排列
		 * 版本号必须严格遵守 A.B.C的格式，不允许另外加‘.’
		 * @param currentVersionName 
		 * @param newVersion
		 * @return
		 */
		private boolean isNewVersion(String currentVersionName,
				String newVersion) {
			String[] currVersions = currentVersionName.split("\\.");
			String[] newVersions = newVersion.split("\\.");
			for(int i=0; i<newVersions.length; i++){
				if(currVersions[i].compareTo(newVersions[i]) < 0){
					return true;
				}
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(mContext == null)
				return;
			if(result == false){
				if(mIsCalledInSetting)
					Toast.makeText(mContext, "未检测到更新", Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
			
			update();
		}
		
	}
	
//	/**
//	 * @return boolean 是否有新版本
//	 */
//	public void checkUpdate(){
//		new Thread(){
//			@Override
//			public void run() {
//				running = true;
//				try {
//					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//					DocumentBuilder builder = factory.newDocumentBuilder();
//					Document document = builder.parse(checkUrl);
//					Node versionNode = document.getElementsByTagName("version").item(0);
//					Node uriNode = document.getElementsByTagName("uri").item(0);
//					Node infoNode = document.getElementsByTagName("info").item(0);
//					Node forceNode = document.getElementsByTagName("force").item(0);
//					String version = versionNode.getTextContent();
//					uri = uriNode.getTextContent();
//					isForce = forceNode.getTextContent().equals("true");
//					description = infoNode.getTextContent();
//					newVersion = version;
//					
//					if(!newVersion.equals(getVersionName())){
//						needUpdate = true;
//					}
//					
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				running = false;
//			}
//		}.start();
//		
//	}
	
	public void update(){
		if(isForce){
			download();
			return ;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("新的版本,是否升级");
		LayoutInflater inflater = this.getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.app_update,null);
		builder.setView(dialogView);
		builder.setPositiveButton("更新",new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int id) {
				download();
			}

		} );
		builder.setNegativeButton("下次再说", new OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				AppUpdateActivity.this.finish();
			}
			
		});
		
		AlertDialog dialog  = builder.create();
		
		TextView txt_version = (TextView) dialogView.findViewById(R.id.txt_version);
		txt_version.setText("版本号："+newVersion);
		TextView txt_description = (TextView) dialogView.findViewById(R.id.txt_description);
		txt_description.setText(description);
		dialog.setCancelable(false);
		dialog.show();
	}

	/**
	 * @return int 本地的版本号 manifest里面
	 */
	public String getVersionName(){
		PackageManager manager= this.getPackageManager();
		String versionCode= "";
		try {
			versionCode = manager.getPackageInfo(this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			Log.e("nameNotFound",e.getMessage());
		}
		return versionCode;
	}
	
	/**
	 * 开一个线程执行下载
	 */
	private void download(){
		progress = new ProgressDialog(AppUpdateActivity.this);
		progress.setTitle("正在下载...");
		progress.setMessage("请稍候");
		//progress.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
		progress.setCancelable(false);
		progress.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				Log.w("KEYCODE", ""+keyCode);
				Log.w("type=",""+ event.getAction());
				if(keyCode != KeyEvent.KEYCODE_BACK || event.getAction() != KeyEvent.ACTION_UP)
					return false;
				
				if(System.currentTimeMillis() - mExitTime > 2000){
					Toast.makeText(getApplicationContext(), "再按一次返回键停止下载", Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();					 
				} else {
					mForceStopOperation = true;
					Toast.makeText(getApplicationContext(), "下载已停止...", Toast.LENGTH_SHORT).show();
					progress.cancel();
				}
				return true;
			}
		});
		
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
	                    byte[] buf = new byte[10240];  
	                    int ch = -1;  
	                    while ((ch = is.read(buf)) != -1) {
	                    	if(mForceStopOperation){
	                    		fileOutputStream.close();
	                    		file.delete();
	                    		mhandler.obtainMessage(FAILED).sendToTarget();
	                    		return;
	                    	}
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
	 * 下载成功时候调用
	 */
	private void onDownloadFinish(){
        progress.cancel();  
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), fileName)),
                "application/vnd.android.package-archive");
        
        startActivity(intent);
        //Toast.makeText(this, "下载文件已放置于SD卡根目录", Toast.LENGTH_LONG).show();
        finish();
	}
	/**
	 * 下载失败的时候调用
	 */
	private void onFailed(){
		progress.cancel();
		Toast.makeText(AppUpdateActivity.this, "下载失败,请检查网络", Toast.LENGTH_SHORT).show();
		finish();
	}
	/**
	 * @param p 进度 单位byte
	 * 显示下载了多少
	 */
	private void showProgress(Long p){
		String downloadPersentage = "已下载" + p + "/" + fullSize;
		double presentage = p.doubleValue()/fullSize * 100.0f;
		downloadPersentage += ("\n" + String.format("%.1f", presentage) + "%");
		
		progress.setMessage(downloadPersentage);
	}
}
