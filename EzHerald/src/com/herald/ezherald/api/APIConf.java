package com.herald.ezherald.api;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xie on 12/12/2014.
 */
public class APIConf {
    public String url;
    public List<BasicNameValuePair> args = new ArrayList<BasicNameValuePair>();

    public void addArgs(BasicNameValuePair... args){
        for (BasicNameValuePair arg : args){
            this.args.add(arg);
        }
    }

    public APIConf(String url){
        this.url = url;
    }
}
