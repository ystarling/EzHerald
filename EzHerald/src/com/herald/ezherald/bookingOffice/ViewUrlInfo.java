package com.herald.ezherald.bookingOffice;


import android.view.View;

import java.net.URL;

/**
 * Created by medition on 2014/4/19.
 */
public class ViewUrlInfo {
     View mView;
     URL  mUrl;
     int mId;
    public ViewUrlInfo(View view, URL url, int id){
        mView = view;
        mUrl = url;
        mId = id;
    }

}
