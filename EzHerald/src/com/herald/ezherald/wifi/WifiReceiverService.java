package com.herald.ezherald.wifi;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

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

public class WifiReceiverService extends Service {
    private static final int NET_ERR = 1;
    private static final int LOGIN = 2;
    private static final int NOT_LOGIN = 3;
    public static String SEU_WLAN = "\"seu-wlan\"";
    private static final String LOGIN_URL = "https://w.seu.edu.cn/portal/login.php";
    private Context context;
    private static Thread thread;
    private boolean showWindow;
    private Handler loginHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            thread = null;
            switch (msg.what){
                case LOGIN:
                    if(showWindow){
                        WifiFloatWindowManager.getWindow(context).changeToLoginMode();
                    }else{
                        Toast.makeText(context, "SEU-WLAN已成功登陆", Toast.LENGTH_LONG).show();
                    }
                    break;
                case NOT_LOGIN:
                    tryLogin();
                    break;
                default:
                    break;

            }
        }

        private void tryLogin() {
            final UserAccount user = Authenticate.getIDcardUser(context);
            if(user == null){//未登陆
                return ;
            }
            new Thread(){
                @Override
                public void run() {
                    Looper.prepare();
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
                            Log.v("res", result);
                            JSONObject json = new JSONObject(result);
                            if(json.has("success")){
                                if(showWindow){
                                    WifiFloatWindowManager.getWindow(context).changeToLoginMode();
                                }else{
                                    Toast.makeText(context,"SEU-WLAN登陆成功",Toast.LENGTH_LONG).show();
                                }
                            }else{
                                String errorMessage = json.getString("error");
                                if(showWindow){
                                    WifiFloatWindowManager.getWindow(context).changeToLoginFailedMode(errorMessage);
                                }else{
                                    Toast.makeText(context,errorMessage,Toast.LENGTH_LONG).show();
                                }

                            }
                        }

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplicationContext();
        SharedPreferences pref = context.getSharedPreferences("com.herald.ezherald_preferences", Context.MODE_PRIVATE);
        Boolean wifi_auto_connect = pref.getBoolean("wifi_auto_connect", false);
        if(!wifi_auto_connect){
            return super.onStartCommand(intent, flags, startId);
        }
        if(pref.getBoolean("wifi_float_window",false) ){
            showWindow = true;
        }

        WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if(!manager.isWifiEnabled()){//wifi关闭了
            WifiFloatWindowManager.removeWindow(context);
        }else{
            WifiInfo current = manager.getConnectionInfo();
            if(current==null || !SEU_WLAN.equals(current.getSSID())){//wifi没连接或者连的不是seu-wlan
                WifiFloatWindowManager.removeWindow(context);
            }else{
                if(thread == null){
                    thread = new Thread(){
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
                    };
                    thread.start();
                }

            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
