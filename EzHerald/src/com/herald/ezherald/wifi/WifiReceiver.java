package com.herald.ezherald.wifi;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WifiReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("WIFI_RECEIVER", "receive");
        Intent service = new Intent(context,WifiReceiverService.class);
        context.startService(service);
    }


}
