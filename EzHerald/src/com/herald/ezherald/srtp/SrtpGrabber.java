package com.herald.ezherald.srtp;

import com.herald.ezherald.mainframe.MainContentGridItemObj;
import com.herald.ezherald.mainframe.MainContentInfoGrabber;

/**
 * Created by Administrator on 2014/12/7.
 */
public class SrtpGrabber implements MainContentInfoGrabber {
    @Override
    public MainContentGridItemObj GrabInformationObject() {
        int times =1;
        int remain =2;
        MainContentGridItemObj item = new MainContentGridItemObj();
        if(times >= 0)
            item.setContent1(String.format("已打卡%d次", times));
        else
            item.setContent1("没有跑操数据");
        item.setContent2(String.format("剩余%d天上课", remain>=0?remain:0));
        return item;
    }
}
