package com.herald.ezherald.treehole;



import android.os.Bundle;
import android.view.View;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;


import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;


/**
 * Created by lenovo on 2014/11/5.
 */
public  class TreeholeFragment extends SherlockFragment{
    View treeholeview=null;
    Button opentreehole;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved){
        // TODO Auto-generated method stub
        treeholeview = inflater.inflate(R.layout.treehole_activity_main, group, false);
        return treeholeview;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();//在初始化函数中进行树洞开始页面的初始化
    }

    public void init()//初始化函数
    {

//        opentreehole=(Button)getSherlockActivity().findViewById(R.id.openholebtn);
//        opentreehole.setOnClickListener(new View.OnClickListener() {
//                  public void onClick(View v) {
//                      Toast.makeText(getActivity(),"正在加载树洞",Toast.LENGTH_SHORT);
//                  }
//              });
    }

//    public void openholebtnclick(View v)
//    {
//        Toast.makeText(getActivity().getApplicationContext(),"正在加载树洞",Toast.LENGTH_SHORT);
//    }

}





