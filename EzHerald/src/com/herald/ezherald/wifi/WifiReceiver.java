package com.herald.ezherald.wifi;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiReceiver extends BroadcastReceiver {
	public static String SEU_WLAN = "seu-wlan";

	@Override
	public void onReceive(Context context, Intent intent) {
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
			// enabled, disabled, enabling, disabling
			// WifiManager manager = (WifiManager)
			// context.getSystemService(Context.WIFI_SERVICE);
			// if(manager.isWifiEnabled() ){
			//
			// WifiInfo current = manager.getConnectionInfo();
			// Log.v("current","got current");
			// Log.v("current",current==null?"no connection":(current.getSSID()==null?"no ssid":current.getSSID()));
			// if(current!=null &&
			// SEU_WLAN.equals(current.getSSID())){//Connected
			// return ;
			// //TODO check if login ?
			// }
			// List<ScanResult> scanResults = manager.getScanResults();
			// boolean hasSeu = false;
			// if(scanResults == null)
			// scanResults = Collections.emptyList();
			// for(ScanResult net:scanResults){
			// String ssid = net.SSID;
			// Log.v("ssid",ssid);
			// if(ssid.equals(SEU_WLAN)){
			// hasSeu = true;
			// }
			// }
			// if(hasSeu){
			// WifiConfiguration conf = new WifiConfiguration();
			// conf.SSID = "\""+SEU_WLAN+"\"";
			// conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			// manager.addNetwork(conf);
			// List<WifiConfiguration> list = manager.getConfiguredNetworks();
			// for( WifiConfiguration i : list ) {
			// if(i.SSID != null && i.SSID.equals("\""+SEU_WLAN+"\"")) {
			// manager.disconnect();
			// Log.v("wifi","disconnecting");
			// manager.enableNetwork(i.networkId, true);
			// manager.reconnect();
			// Log.v("wifi","connecting");
			// break;
			// }
			// }
			// }
			// }else{
			// Log.v("wifi","wifi is closed");
			// }
			//
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
				if (current != null && SEU_WLAN.equals(current.getSSID())) {// Connected
					if (checkLogin() == true) {
						// TODO may be nothing
					} else {
						// TODO try login
					}
				}
			}
		}

	}

	private boolean checkLogin() {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet("http://www.baidu.com");// TODO any other web?
		HttpResponse response;
		try {
			response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				return true;
			} else {// should be 302 when site redirected
				return false;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}
}
