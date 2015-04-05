package com.herald.ezherald.srtp;

import android.content.Context;

import com.herald.ezherald.mainframe.MainContentGridItemObj;
import com.herald.ezherald.mainframe.MainContentInfoGrabber;

/**
 * Created by Administrator on 2014/12/7.
 */
public class SrtpGrabber implements MainContentInfoGrabber {
    private Score score;
    public SrtpGrabber(Context context){
        score=new Score(context);
    }
    @Override
    public MainContentGridItemObj GrabInformationObject() {
        MainContentGridItemObj item = new MainContentGridItemObj();
        int SrtpScore=score.getScore();
        String updatetime=score.getUpdateTime();
        if(SrtpScore >= 0)
            item.setContent1(String.format("已获得Srtp学分",SrtpScore));
        else
            item.setContent1("没有srtp学分数据");
        if(updatetime!=null)
            item.setContent2(updatetime);
        else
        item.setContent2("未更新");

        return item;
    }
}
