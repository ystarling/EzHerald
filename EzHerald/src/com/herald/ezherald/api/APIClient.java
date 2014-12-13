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

import java.io.IOException;


/**
 * Created by xie on 12/12/2014.
 */

public class APIClient {
    private final String API_URL = "http://herald.seu.edu.cn/";
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


    public void doRequest() {

        new Thread() {
            @Override
            public void run() {
                try {
                    APIAccount account = new APIAccount(context);
                    if (!account.isUUIDValid()) {
                        failHandler.onFail(Err.NOT_LOGIN, "user not login");
                        return;
                    }

                    HttpClient client = new DefaultHttpClient();
                    HttpPost request = new HttpPost(API_URL + conf.url);
                    HttpParams params = new BasicHttpParams();

                    params.setParameter("uuid", account.uuid);

                    for (NameValuePair arg : conf.args) {
                        params.setParameter(arg.getName(), arg.getValue());
                    }

                    request.setParams(params);

                    HttpResponse response = client.execute(request);
                    if (response.getStatusLine().getStatusCode() != 200) {
                        Err err = Err.getErrFromHttpCode(response.getStatusLine().getStatusCode());
                        failHandler.onFail(err, "error status");
                        return;
                    }
                    String results = EntityUtils.toString(response.getEntity());
                    successHandler.onSuccess(results);
                } catch (IOException e) {
                    e.printStackTrace();
                    failHandler.onFail(Err.IO_EXCEPTION, e.getMessage());
                }
            }
        }.start();
    }
}

