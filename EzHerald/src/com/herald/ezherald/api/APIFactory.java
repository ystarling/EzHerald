package com.herald.ezherald.api;

import android.content.Context;

/**
 * Created by xie on 12/12/2014.
 */
public class APIFactory {

    public static APIClient getAPIClient(Context context,String name,SuccessHandler successHandler,FailHandler failHandler){

        APIClient client = new APIClient(context);
        client.conf = new APIConf(name);
        if(successHandler!=null)
            client.successHandler = successHandler;
        else
            client.successHandler = new DefaultSuccessHandler();

        if(failHandler!=null)
            client.failHandler = failHandler;
        else
            client.failHandler = new DefaultFailedHandler();


        return client;
    }
}
class DefaultSuccessHandler implements SuccessHandler{

    @Override
    public void onSuccess(String data) {
        //do nothing
    }
}
class DefaultFailedHandler implements FailHandler{

    @Override
    public void onFail(Status status, String message) {
        //do nothing
    }
}