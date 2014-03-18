package com.herald.ezherald.wifi;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

public class LinkDialogActivity extends Activity {
	private static String SEU_WLAN = WifiReceiver.SEU_WLAN;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new Builder(this);
		final WifiManager manager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		builder.setCancelable(false);
		builder.setTitle("发现seu-wlan");
		builder.setMessage("是否连接?");
		builder.setPositiveButton("是", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				WifiConfiguration conf = new WifiConfiguration();
				conf.SSID = "\""+SEU_WLAN+"\"";
				conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
				int id = manager.addNetwork(conf);
				boolean sd = manager.saveConfiguration();
				boolean ed = manager.enableNetwork(id, true);
				Log.v("connect id",String.valueOf(id));
				Log.v("connect sd",String.valueOf(sd));
				Log.v("connect ed",String.valueOf(ed));
				LinkDialogActivity.this.finish();
			}
		});
		builder.setNegativeButton("否", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				LinkDialogActivity.this.finish();
			}
		});
		builder.show();
		super.onCreate(savedInstanceState);
	}
}
