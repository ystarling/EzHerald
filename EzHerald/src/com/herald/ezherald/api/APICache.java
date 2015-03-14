package com.herald.ezherald.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by X on 2015/3/14.
 */
public class APICache {
    public Context context;
    public long expire = 60 * 5 ; //缓存过期时间（秒）
    final String TIME = "TIME",DATA = "DATA",API_CACHE_PREFIX = "API_CACHE_";
    public APICache(Context context){
        this.context = context;
    }

    private long getCurrentTime(){
        return System.currentTimeMillis()/(long)1000000;
    }

    public String readFormCache(APIConf apiConf){
        String hash = getCacheName(apiConf);
        long now = getCurrentTime();
        SharedPreferences sharedPreferences = context.getSharedPreferences(hash, Context.MODE_PRIVATE);
        long old = sharedPreferences.getLong(TIME, -1);
        if(old < 0 || now - old > expire)
            return null;
        String data = sharedPreferences.getString(DATA,null);
        return data;
    }

    public void writeToCache(APIConf apiConf,String data){
        String hash = getCacheName(apiConf);
        long now = getCurrentTime();
        SharedPreferences sharedPreferences = context.getSharedPreferences(hash, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(TIME, now);
        editor.putString(DATA, data);
        editor.commit();
    }

    private String getCacheName(APIConf apiConf){
        String str = apiConf.toString(),result = "";
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            result = hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if(result.equals("")){
            Log.e("API cache","hash is empty");
        }
        return API_CACHE_PREFIX+result;
    }
}
