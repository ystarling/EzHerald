package com.herald.ezherald.api;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;


/**
 * Created by xie on 12/12/2014.
 */

public class APIClient {
    private final String API_URL = "http://herald.seu.edu.cn/";
    public static final int ERR_NOT_LOGIN = -1;
    public static final int ERR_NET_ERR = -2;


    public APIConf conf;
    public SuccessHandler successHandler;
    public FailHandler failHandler;
    private Context context;
    public APIClient(Context context){
        this.context = context;
    }

    public void addArg(String key,String value){
        conf.args.add(new BasicNameValuePair(key,value));
    }


    public void setUrl(String url)
    {
        conf.url=url;
    }//2015.4.2  By he 添加一个改URL的函数

    public void doRequest(){

        new Thread(){
            @Override
            public void run(){
                try{

                    APIAccount account = new APIAccount (context);
                    if(account.uuid == null || account.uuid.equals("")){
                        failHandler.onFail(ERR_NOT_LOGIN,"login First");
                        return ;
                    }

                    HttpClient client = new DefaultHttpClient();
                    HttpPost request = new HttpPost(API_URL+conf.url);
                    HttpParams params = new BasicHttpParams();

                    params.setParameter("uuid",account.uuid);

                    for(NameValuePair arg : conf.args){
                        params.setParameter(arg.getName(),arg.getValue());
                    }

                    request.setParams(params);

                    HttpResponse response = client.execute(request);
                    if(response.getStatusLine().getStatusCode() != 200){
                        //TODO detail error info
                        failHandler.onFail(response.getStatusLine().getStatusCode(),"error status");
                        return ;
                    }
                    String results = EntityUtils.toString( response.getEntity());
                    successHandler.onSuccess(results);
                }catch (Exception e){
                    e.printStackTrace();
                    failHandler.onFail(ERR_NET_ERR,e.getMessage());
                }
            }
        }.start();
    }
}

interface SuccessHandler{
    public void onSuccess(String data);
}
interface FailHandler{
    public void onFail(int errCode,String message);
}
