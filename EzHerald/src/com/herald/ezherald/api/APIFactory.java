package com.herald.ezherald.api;

import android.content.Context;

/**
 * Created by xie on 12/12/2014.
 */
public class APIFactory {

    public static APIClient getAPIClient(Context context,String name,SuccessHandler successHandler,FailHandler failHandler){

        APIClient client = new APIClient(context);
        client.conf = new APIConf(name);
        client.successHandler = successHandler;
        client.failHandler = failHandler;
        return client;
    }
}
