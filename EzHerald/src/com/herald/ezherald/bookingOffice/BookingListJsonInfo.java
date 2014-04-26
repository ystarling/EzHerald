package com.herald.ezherald.bookingOffice;

import java.net.URL;

/**
 * Created by medition on 2014/4/26.
 */
public class BookingListJsonInfo {
    public String mId;
    public  String mCaption;
    public  URL mPosterUrl;
    public  int mNumber;
    public String mActivity_time;
    public String mDeadline;
    public BookingListJsonInfo(String id, String caption, URL posterUrl, int number, String activity_time, String deadline){
        mId = id;
        mCaption = caption;
        mPosterUrl = posterUrl;
        mNumber = number;
        mActivity_time = activity_time;
        mDeadline = deadline;
    }

    public BookingListJsonInfo(){

    }



}




