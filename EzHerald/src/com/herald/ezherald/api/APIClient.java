package com.herald.ezherald.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


/**
 * Created by xie on 12/12/2014.
 */

public class APIClient {
    private final String API_URL = "http://121.248.63.105/";
    public APIConf conf;
    public SuccessHandler successHandler;
    public FailHandler failHandler;
    private Context context;

    public APIClient(Context context) {
        this.context = context;
    }

    public void addArg(String key, String value) {
        conf.args.add(new BasicNameValuePair(key, value));
    }
    public void addAPPIDToArg(){
        addArg("appid",new APPID().getAPPID());
    }

    //调用前要先手动检查uuid的有效性
    public void addUUIDToArg(){
        addArg("uuid",new APIAccount(context).uuid);
    }

    public void doRequest() {
        new AsyncTask<Void,Void,Void>(){

            private boolean success;
            private String result;
            private com.herald.ezherald.api.Status status;
            @Override
            protected Void doInBackground(Void... params) {
                Log.d("Client", "Request start");
                try {
                    APIAccount account = new APIAccount(context);
                    if (!account.isUUIDValid() && !conf.url.equals("uc/auth")) {
                        //failHandler.onFail(com.herald.ezherald.api.Status.NOT_LOGIN, "user not login");
                        status = com.herald.ezherald.api.Status.NOT_LOGIN;
                        success = false;
                        result = "";
                        return null;
                    }
                    Log.d("Client","check user ok");
                    HttpParams httpParameters = new BasicHttpParams();

                    int timeoutConnection = 3000;
                    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                    int timeoutSocket = 5000;
                    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                    HttpClient client = new DefaultHttpClient(httpParameters);

                    HttpPost request = new HttpPost(API_URL + conf.url);

                    Log.d("Client","request uri"+request.getURI().toString());

                    HttpEntity entity = new UrlEncodedFormEntity(conf.args,"UTF-8");
                    request.setEntity(entity);
                    Log.d("Client","add param ok,start to execute request");
                    HttpResponse response = client.execute(request);
                    int statusCode = response.getStatusLine().getStatusCode();
                    result = EntityUtils.toString(response.getEntity());
                    status = com.herald.ezherald.api.Status.getErrFromHttpCode(statusCode);
                    if (statusCode != HttpStatus.SC_OK) {
                        Log.d("Client","error status code "+statusCode);
                        success = false;
                        return null;
                    }
                    Log.d("Client","status ok");

                    success = true;
                } catch (IOException e) {
                    Log.d("Client","IO_EXCEPTION");
                    e.printStackTrace();
                    success = false;
                    status = com.herald.ezherald.api.Status.IO_EXCEPTION;
                    return null;
                }finally {
                    Log.d("Client","Finished");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void ignore) {
                if(success){
                    successHandler.onSuccess(result);
                }else{
                    failHandler.onFail(status,result);
                }
            }
        }.execute();



    }

}

