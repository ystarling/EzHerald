package com.herald.ezherald.api;

/**
 * Created by xie on 12/12/2014.
 */
public class APPID {
    static {
        System.loadLibrary("appid");

    }
    public static native String getAPPID();
}
