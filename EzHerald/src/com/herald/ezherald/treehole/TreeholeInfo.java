package com.herald.ezherald.treehole;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by lenovo on 2015/1/30.用于获取树洞消息
 */
public class TreeholeInfo {
    private final int SUCCESS = 1, FAILED = 0;
    private final String URL_refresh = "https://api.renren.com/v2/status/list?access_token=";
    private String message;//联网读取到的消息
    private String Str_hole="";
    private TreeholeFragment father;
    public Context context;
    private Handler handler_updata;
    private Handler handler_send;
    private JSONArray jsonArray_treehole = new JSONArray();//消息列表的数组

    public TreeholeInfo(final Context context,TreeholeFragment father)
    {
        this.father=father;
        this.context=context;
        handler_updata=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case SUCCESS:
                        UpdataOnSuccess();
                        break;
                    case FAILED:
                        UpdataOnFailed();
                }
                super.handleMessage(msg);
            }
        };//更新内容的处理函数

        handler_send=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case SUCCESS:
                        SendOnSuccess();
                        break;
                    case FAILED:
                        SendOnFailed();
                }
                super.handleMessage(msg);
            }
        };//设置更新内容的处理函数
    }

    public void Updata()
    {
        Thread thread_updata=new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Log.d("Updata","treehole updata");
//                    String tag="https://graph.renren.com/oauth/token?grant" + URLEncoder.encode("_") + "type=refresh" + URLEncoder.encode("_") + "token&refresh" + URLEncoder.encode("_") + "token=" + URLEncoder.encode("241511|0.NvNrQow4rEchFtbdaCUtdyA5dWLgRgDh.365328826.1379088139819") + "&client" + URLEncoder.encode("_") + "id=241511&client" + URLEncoder.encode("_") + "secret=8d970a6e9e3249e9afffd2fdba73f018";
//                    String tag = "https://graph.renren.com/oauth/authorize?client" + URLEncoder.encode("_") + "id=f938a4efbec34888b7dd7863162ac726&redirect" + URLEncoder.encode("_") + "uri=http://page.renren.com/601880046?id=601880046&response" + URLEncoder.encode("_") + "type=code" ;
//                    String tag="https://graph.renren.com/oauth/token?grant_type=authorization"+ URLEncoder.encode("_") +"code&client"+ URLEncoder.encode("_") +"id=f938a4efbec34888b7dd7863162ac726&redirect"+ URLEncoder.encode("_") +"uri=http://page.renren.com/601880046?id=601880046&client"+ URLEncoder.encode("_") +"secret=128c4d85ae194092a610cdb157d51a99&code=Mj630DFSMVS4JGnuQr2ekVnFcsSkpslU";
                    String tag="https://graph.renren.com/oauth/token?grant" + URLEncoder.encode("_") + "type=refresh" + URLEncoder.encode("_") + "token&refresh" + URLEncoder.encode("_") + "token=" + URLEncoder.encode("474942|0.xZB5tloyOPX9teApedFwhTiKWE1WvZni.557314787.1422879755817") + "&client" + URLEncoder.encode("_") + "id=f938a4efbec34888b7dd7863162ac726&client" + URLEncoder.encode("_") + "secret=128c4d85ae194092a610cdb157d51a99";
                    HttpClient client_th_updata=new DefaultHttpClient();
                    HttpGet get_th_updata=new HttpGet(tag);
                    HttpResponse response_th_updata=client_th_updata.execute(get_th_updata);
                    message = EntityUtils.toString(response_th_updata.getEntity());


                    JSONObject json=new JSONObject(message);
                    String token = json.getString("access_token");
                    token = URLEncoder.encode(token);
                    HttpGet get = new HttpGet(URL_refresh + token + "&ownerId=601880046&pageSize=20&pageNumber=1");
                    response_th_updata = client_th_updata.execute(get);
                    message = EntityUtils.toString(response_th_updata.getEntity());


                    Message msg=handler_updata.obtainMessage(SUCCESS,message);
                    handler_updata.sendMessage(msg);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    handler_updata.obtainMessage(FAILED);
                }

            }
        });
        thread_updata.start();

    }


    public void UpdataOnSuccess()
    {
        Str_hole="";
        try
        {
            JSONObject json = new JSONObject(message);



            JSONArray array=json.getJSONArray("response");

            for(int i=0;i<array.length();i++) {
                JSONObject json_content=(JSONObject)array.opt(i);
                //String 方式传递
                Str_hole=Str_hole+json_content.getString("content")+"\n";
                Str_hole=Str_hole+json_content.getString("createTime")+"\n\n\n";

                //JSONARRAY方式传递
//                JSONObject jsonArray_treehole_item =new JSONObject();
//                String jsonbox;
//                jsonbox=json_content.getString("id");
//                jsonArray_treehole_item.put("id",jsonbox);
//                jsonbox=json_content.getString("content");
//                jsonArray_treehole_item.put("content",jsonbox);
//                jsonbox=json_content.getString("createTime");
//                jsonArray_treehole_item.put("createTime",jsonbox);
                String jsonbox="{\"id\": "+json_content.getString("id")+"," +
                        "\"content\": \""+json_content.getString("content")+"\"," +
                        "\"createTime\": \""+json_content.getString("createTime")+"\"" +
                        "}";
                JSONObject jsonItem=new JSONObject(jsonbox);
                jsonArray_treehole.put(jsonItem);
        }




        }
        catch (Exception e)
        {
            e.printStackTrace();
            UpdataOnFailed();
            return;
        }

        father.UpdataOnSuccess();

    }

    public void UpdataOnFailed()
    {
        father.str_holecontent="Error";
        father.UpdataOnFailed();

    }


    public void Send(String str_send)
    {

    }

    public void SendOnSuccess()
    {

    }

    public void SendOnFailed()
    {

    }

    public String getHoleContent()
    {
        return Str_hole;
    }

    public JSONArray getTreeholeArray()
    {
        return jsonArray_treehole;
    }

    public void Clean()
    {
        Str_hole="";
        message="";
        jsonArray_treehole=null;
    }


}
