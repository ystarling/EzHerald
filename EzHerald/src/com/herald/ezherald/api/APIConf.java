package com.herald.ezherald.api;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xie on 12/12/2014.
 */
public class APIConf {
    public String url;
    public List<NameValuePair> args = new ArrayList<NameValuePair>();;
    public int connectTimeout=5000;
    public int socketTimeout=5000;
    public void addArgs(NameValuePair... args){
        for (NameValuePair arg : args){
            this.args.add(arg);
        }
    }

    public APIConf(String url){
        this.url = url;
    }
}
