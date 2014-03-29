package com.herald.ezherald.wifi;

import java.util.Collections;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class WifiReceiver extends BroadcastReceiver {
	public static String SEU_WLAN = "seu-wlan";
	private final int LOGIN = 1;
	private final int NOT_LOGIN = 0;
	private Context context;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case NOT_LOGIN:onNotLogin();break;
			case LOGIN:break;
			}
		}

		private void onNotLogin() {
			// TODO Auto-generated method stub
			Intent it = new Intent(context,LoginDialogActivity.class);
			it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(it);
		}
	};
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		String action = intent.getAction();
		Log.v("action", action);
		if (action.equals("android.net.wifi.SCAN_RESULTS")) {
			final WifiManager manager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo current = manager.getConnectionInfo();
			if (current == null || !SEU_WLAN.equals(current.getSSID())) {
				List<ScanResult> scanResults = manager.getScanResults();
				boolean hasSeu = false;
				if (scanResults == null)
					scanResults = Collections.emptyList();
				for (ScanResult net : scanResults) {
					String ssid = net.SSID;
					Log.v("ssid", ssid);
					if (ssid.equals(SEU_WLAN)) {
						hasSeu = true;
					}
				}
				if (hasSeu) {
					Intent it = new Intent(context, LinkDialogActivity.class);
					it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					// context.startActivity(it);
				}

			}

		} else if (action.equals("android.net.wifi.WIFI_STATE_CHANGED")) {
			
		} else if (action.equals("android.net.wifi.STATE_CHANGE")) {
			WifiManager manager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			if (manager.isWifiEnabled()) {

				WifiInfo current = manager.getConnectionInfo();
				Log.v("current", "got current");
				Log.v("current",
						current == null ? "no connection"
								: (current.getSSID() == null ? "no ssid"
										: current.getSSID()));
				Log.v("supplicantState",current.getSupplicantState().toString());
				if (current != null && SEU_WLAN.equals(current.getSSID()) && current.getSupplicantState() == SupplicantState.COMPLETED) {// Connected
					checkLogin();
					//					if (checkLogin() == true) {
//						// TODO may be nothing
//					} else {
//						Intent it = new Intent(context,LoginDialogActivity.class);
//						it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						context.startActivity(it);
//					}
				}
			}
		}

	}

	public void checkLogin() {
		/*
		 * Example json
		 * {"login":"\u8be5IP\u5730\u5740\u5df2\u6210\u529f\u767b\u5f55","login_username":"213xxxxxx","login_time":2,"login_ip":"223.xxx.xxx.xxx","login_location":"\u672a\u77e5\u4f4d\u7f6e"}
		 * {"notlogin":"\u65b0\u7528\u6237\u767b\u5f55","login_ip":"223.xxx.xxx.xxx","login_location":"\u672a\u77e5\u4f4d\u7f6e"}
		 */
		new Thread(){
			@Override
			public void run(){
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet("https://w.seu.edu.cn/portal/init.php");
				HttpResponse response;
				try {
					response = client.execute(get);
					if (response.getStatusLine().getStatusCode() != 200) {
						handler.obtainMessage(0).sendToTarget();//return false;
					} else {
						String msg = EntityUtils.toString(response.getEntity());
						while(!msg.startsWith("{")){
							msg = msg.substring(1);
						}
						JSONObject json = new JSONObject(msg);
						if(json.has("login")){
							handler.obtainMessage(LOGIN).sendToTarget();;
						}else{
							handler.obtainMessage( NOT_LOGIN).sendToTarget();
						}
					}
				} catch( Exception e){
					e.printStackTrace();
					handler.obtainMessage( NOT_LOGIN).sendToTarget();
				}

			}
		}.start();
	}
}
