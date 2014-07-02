package com.herald.ezherald.wifi;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by xie on 7/2/2014.
 */
public class WifiService extends Service{
    public static String SEU_WLAN = "\"seu-wlan\"";
    private static final int LOGIN = 0,NOT_LOGIN = 1, NET_ERR = 2;
    private Timer timer;
    private Context context;
    private Handler handler = new Handler();
    private Handler loginHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOGIN:
                    WifiFloatWindowManager.createWindow(context).changeToLoginMode();
                    break;
                case NOT_LOGIN:
                    WifiFloatWindowManager.createWindow(context).changeToNotloginMode();
                    break;
                case NET_ERR:
                default:
                    break;
            }
        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //WifiFloatWindowManager.createWindow(context);
        if(timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new Task(),0,500);
        }
        context = getApplicationContext();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
    }

    private class Task extends TimerTask {

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
                    if(manager.isWifiEnabled()){
                        WifiInfo current = manager.getConnectionInfo();
                        if(current!=null && SEU_WLAN.equals(current.getSSID())){
                            new Thread(){
                                @Override
                                public void run() {
                                    HttpClient client = new DefaultHttpClient();
                                    HttpGet get = new HttpGet("https://w.seu.edu.cn/portal/init.php");
                                    HttpResponse response;
                                    try {
                                        response = client.execute(get);
                                        if (response.getStatusLine().getStatusCode() != 200) {
                                            loginHandler.obtainMessage(NET_ERR).sendToTarget();
                                        } else {
                                            String msg = EntityUtils.toString(response.getEntity());
                                            while(!msg.startsWith("{")){
                                                msg = msg.substring(1);
                                            }
                                            JSONObject json = new JSONObject(msg);
                                            if(json.has("login")){
                                                loginHandler.obtainMessage(LOGIN).sendToTarget();
                                            }else{
                                                loginHandler.obtainMessage(NOT_LOGIN).sendToTarget();
                                            }
                                        }
                                    } catch( Exception e){
                                        e.printStackTrace();
                                        loginHandler.obtainMessage(NET_ERR).sendToTarget();
                                    }
                                }
                            }.start();
                        }else{
                            WifiFloatWindowManager.removeSmallWindow(context);
                        }
                    }else{
                        WifiFloatWindowManager.removeSmallWindow(context);
                    }
                }
            });

        }
    }
}
