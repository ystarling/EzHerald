package com.herald.ezherald.treehole;



import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by lenovo on 2014/11/5.
 */
public  class TreeholeFragment extends SherlockFragment{
    View treeholeview=null;
    TextView tv_show;//显示树洞内容
    Button bt_opentreehole;
    Button bt_writesecret;
    String str_holecontent="";
    TreeholeInfo treeholeInfo;

    ListView lv_listshow;//列表控件显示树洞内容
    JSONArray jsonarray_holecontent;
    ArrayList<String> item_id=new ArrayList<String>();
    ArrayList<String> item_content=new ArrayList<String>();
    ArrayList<String> item_time=new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved){
        // TODO Auto-generated method stub
        treeholeview = inflater.inflate(R.layout.treehole_activity_main, group, false);
        return treeholeview;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        init();//在初始化函数中进行树洞开始页面的初始化

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //获取树洞消息
        treeholeInfo=new TreeholeInfo(getActivity(),this);
        //treeholeInfo用于发送和接收消息，此处用于与当前Fragment和Activity绑定
        tv_show=(TextView)getActivity().findViewById(R.id.showholetv);
        if(str_holecontent=="")
            tv_show.setText("暂未获取到树洞内容");
        tv_show.setMovementMethod(ScrollingMovementMethod.getInstance());//实现多行滚动


        bt_opentreehole = (Button)getActivity().findViewById(R.id.openholebtn);//更新内容的按钮响应函数
        bt_opentreehole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updata();//点击后更新树洞内容
            }
        });

        lv_listshow=(ListView)getActivity().findViewById(R.id.listview_treehole);//存放树洞内容的listview
        lv_listshow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(item_id.get(position)!=null)
                {
                    Toast.makeText(getActivity(),"你点击了第"+new Integer(position).toString()+"项",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //发送树洞消息
        bt_writesecret=(Button)getActivity().findViewById(R.id.writesecret);
        bt_writesecret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(getActivity(),TreeholeSendActivity.class);
//                startActivity(intent);
                send();
            }
        });




    }

    public boolean send()
    {
        treeholeInfo.Send("试一试");
        return true;
    }

    public boolean updata()
    {
        //用于更新树洞内容的函数
        treeholeInfo.Updata();
        return true;

    }

    public void UpdataOnSuccess()
    {
        Toast.makeText(getActivity(),"获取树洞内容成功！",Toast.LENGTH_SHORT).show();
        str_holecontent=treeholeInfo.getHoleContent();

        try {
            jsonarray_holecontent=treeholeInfo.getTreeholeArray();

            for(int i=0;i<jsonarray_holecontent.length();i++)
            {
                JSONObject json=(JSONObject)jsonarray_holecontent.opt(i);
                //json.getString("id")+"\n"+json.getString("content")+"\n"+json.getString("createTime")+"\n\n\n";
                item_id.add(json.getString("id"));
                item_content.add(json.getString("content"));
                item_time.add(json.getString("createTime"));
            }

//            tv_show.setText();

            List<Map<String,Object>> listitems=new ArrayList<Map<String, Object>>();

            for(int i=0;i<jsonarray_holecontent.length();i++)
            {
                Map<String,Object> map= new HashMap<String, Object>();
                map.put("content",item_content.get(i));
                map.put("createTime",item_time.get(i));
                listitems.add(map);
            }

            SimpleAdapter adapter=new SimpleAdapter(getActivity(),listitems,R.layout.treehole_listitem,
            new String[]{"content","createTime"},
                    new int[]{R.id.tv_treehole_item_content,R.id.tv_treehole_item_createTime});

            lv_listshow.setAdapter(adapter);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    public void UpdataOnFailed()
    {
        Toast.makeText(getActivity(),"获取树洞内容失败！",Toast.LENGTH_SHORT).show();
        tv_show.setText(str_holecontent);
    }

    public void SendOnSuccess()
    {

    }

    public void SendOnFailed()
    {

    }




}





