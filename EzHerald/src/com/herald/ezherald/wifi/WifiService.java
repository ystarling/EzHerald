package com.herald.ezherald.wifi;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.UserAccount;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xie on 7/2/2014.
 */
public class WifiService extends Service{
    public static String SEU_WLAN = "\"seu-wlan\"";
    private static final int LOGIN = 0,NOT_LOGIN = 1, NET_ERR = 2;
    private static final String LOGIN_URL = "https://w.seu.edu.cn/portal/login.php";
    private Timer timer;
    private Context context;
    private Handler handler = new Handler();
    private boolean running;
    private String errorMessage;
    private Handler loginHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOGIN:
                    WifiFloatWindowManager.getWindow(context).changeToLoginMode();
                    break;
                case NOT_LOGIN:
                    WifiFloatWindowManager.getWindow(context).changeToNotLoginMode();
                    final UserAccount user = Authenticate.getIDcardUser(context);

                    if( user!=null && (errorMessage==null || errorMessage.equals("")) ){
                        new Thread(){
                            @Override
                            public void run() {
                                HttpClient client = new DefaultHttpClient();
                                HttpPost post = new HttpPost(LOGIN_URL);
                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("username",user.getUsername()));
                                params.add(new BasicNameValuePair("password",user.getPassword()));
                                try{
                                    post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                                    HttpResponse response = client.execute(post);
                                    if(response.getStatusLine().getStatusCode() == 200){
                                        HttpEntity entity = response.getEntity();
                                        String result = EntityUtils.toString(entity);
                                        while(!result.startsWith("{")){
                                            result = result.substring(1);
                                        }
                                        Log.v("res",result);
                                        JSONObject json = new JSONObject(result);
                                        if(json.has("success")){
                                            WifiFloatWindowManager.getWindow(context).changeToLoginMode();
                                        }else{
                                            errorMessage = json.getString("error");
                                            WifiFloatWindowManager.getWindow(context).changeToLoginFailedMode(errorMessage);
                                        }
                                    }

                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
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
        //WifiFloatWindowManager.getWindow(context);
        if(timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new Task(),0,1000);
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
            if(running){
                return ;
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    running = true;
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
                            WifiFloatWindowManager.removeWindow(context);
                        }
                    }else{
                        WifiFloatWindowManager.removeWindow(context);
                    }
                }
            });

        }
    }
}
