package com.herald.ezherald.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;


/**
 * Created by xie on 12/12/2014.
 *
 */

public class APIClient {
    private final String API_URL = "http://121.248.63.105/";
    public APIConf conf;
    public SuccessHandler successHandler;
    public FailHandler failHandler;
    private Context context;
    private boolean useCache,returnedData;
    private APICache apiCache;
    private String cachedData;

    //
    public void setCacheExpireTime(long time){
        apiCache.expire = time;
    }

    public long getCacheExpireTime(){
        return apiCache.expire;
    }


    public APIClient(Context context) {
        this.context = context;
        apiCache = new APICache(context);
        useCache = false;
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

    //当缓存可用时立即从返回一个结果。然后联网请求，如果数据有更新再回掉一次
    public void requestWithCache(){
        useCache = true;
        doRequest();
    }
    //直接联网返回结果
    public void requestWithoutCache(){
        useCache = false;
        doRequest();
    }
    //直接从缓存取结果，先要判断缓存是不是有效
    public void readFromCache(){
        cachedData = apiCache.readFormCache(conf);
        if(cachedData != null){
            Log.d("Client","read cache success");
            successHandler.onSuccess(cachedData);
            returnedData = true;
        }else{
            returnedData = false;
            Log.d("Client","read cache failed");
        }
    }

    public boolean isCacheAvailable(){
        return apiCache.readFormCache(conf) != null;
    }

    private void doRequest() {
        if(useCache){
            readFromCache();
        }

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
                        status = com.herald.ezherald.api.Status.NOT_LOGIN;
                        success = false;
                        result = "";
                        return null;
                    }
                    Log.d("Client", "check user ok");

                    OkHttpClient client = new OkHttpClient();
                    client.setConnectTimeout(conf.connectTimeout, TimeUnit.MILLISECONDS);
                    client.setReadTimeout(conf.socketTimeout,TimeUnit.MILLISECONDS);
                    Request.Builder builder = new Request.Builder();

                    FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
                    for(NameValuePair data : conf.args){
                        formEncodingBuilder.add(data.getName(),data.getValue());
                    }
                    Log.d("Client", "add param ok,start to execute request");
                    RequestBody body = formEncodingBuilder.build();
                    Request request = builder.url(API_URL + conf.url).post(body).build();
                    Response response = client.newCall(request).execute();
                    if(!response.isSuccessful()){
                        success = false;
                        int code = response.code();
                        if(code != HttpStatus.SC_OK){
                            Log.d("Client","error status code "+code);
                            status = com.herald.ezherald.api.Status.getErrFromHttpCode(code);
                            return null;
                        }
                        throw new Exception(response.toString());
                    }
                    Log.d("Client","request success");
                    success = true;
                    result = response.body().string();

                } catch(ConnectTimeoutException e){
                    Log.d("Client","Connection time out");
                    e.printStackTrace();
                    success = false;
                    status = com.herald.ezherald.api.Status.TIMEOUT;
                    return null;
                } catch (SocketTimeoutException e){
                    Log.d("Client","socket time out");
                    e.printStackTrace();
                    success = false;
                    status = com.herald.ezherald.api.Status.TIMEOUT;
                    return null;
                } catch (IOException e) {
                    Log.d("Client","IO_EXCEPTION");
                    e.printStackTrace();
                    success = false;
                    status = com.herald.ezherald.api.Status.IO_EXCEPTION;
                    return null;
                } catch (Exception e){
                    Log.d("Client","UNKNOWN EXCEPTION:"+e.getMessage());
                    e.printStackTrace();
                    success = false;
                    status = com.herald.ezherald.api.Status.UNKNOWN;
                    return null;
                } finally {
                    Log.d("Client","Finished");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void ignore) {
                if(success){
                    apiCache.writeToCache(conf,result);
                    if(!returnedData)
                        successHandler.onSuccess(result);
                }else{
                    if(!returnedData)
                        failHandler.onFail(status,result);

                }
            }
        }.execute();



    }

}

