package com.herald.ezherald.treehole;

import android.app.Fragment;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2015/1/30.用于获取树洞消息
 */
public class TreeholeInfo {
    private final int SUCCESS = 1, FAILED = 0;
    private final String URL_refresh = "https://api.renren.com/v2/status/list?access_token=";
    private final String URL_send ="";
    private String message;//联网读取到的消息
    private String Str_hole="";
    private TreeholeFragment updata_father;
    private TreeholeSendFragment send_father;
    public Context context;
    private Handler handler_updata;
    private Handler handler_send;
    private JSONArray jsonArray_treehole = new JSONArray();//消息列表的数组

    public TreeholeInfo(final Context context,TreeholeFragment father)
    {
        this.updata_father=father;
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

        handler_send=new Handler()
        {
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


    public TreeholeInfo(final Context context,TreeholeSendFragment father)
    {
        this.send_father=father;
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
//                        SendOnSuccess();
                        SendOnSuccess();
                        break;
                    case FAILED:
//                        SendOnFailed();
                        SendOnFailed();
                }
                super.handleMessage(msg);
            }
        };//设置更新内容的处理函数
    }

    public String getAccess_token()//会先用refreshtoken刷新access_token然后返回最新的
            //涉及网络操作，需要放在线程里
    {
        String token="";
        try
        {
            Log.d("get_token","treehole token_refresh");
            String tag="https://graph.renren.com/oauth/token?grant" + URLEncoder.encode("_") + "type=refresh" + URLEncoder.encode("_") + "token&refresh" + URLEncoder.encode("_") + "token=" + URLEncoder.encode("474942|0.xZB5tloyOPX9teApedFwhTiKWE1WvZni.557314787.1422879755817") + "&client" + URLEncoder.encode("_") + "id=f938a4efbec34888b7dd7863162ac726&client" + URLEncoder.encode("_") + "secret=128c4d85ae194092a610cdb157d51a99";
            HttpClient client_th_updata=new DefaultHttpClient();
            HttpGet get_th_updata=new HttpGet(tag);
            HttpResponse response_th_updata=client_th_updata.execute(get_th_updata);
            message = EntityUtils.toString(response_th_updata.getEntity());
            JSONObject json=new JSONObject(message);
            token = json.getString("access_token");
            return token;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return token;
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
                    String token=getAccess_token();
                    HttpClient client_th_updata=new DefaultHttpClient();
                    HttpGet get = new HttpGet(URL_refresh + URLEncoder.encode(token) + "&ownerId=601880046&pageSize=20&pageNumber=1");
                    HttpResponse response_th_updata = client_th_updata.execute(get);
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
        if(updata_father!=null)
            updata_father.UpdataOnSuccess();
    }

    public void UpdataOnFailed()
    {
        if(updata_father!=null)
        updata_father.str_holecontent="Updata Error";
    }


    public void Send(String str_send)//传进来的str_send参数将会发送到主页上
    {
        //发送消息的线程
        final String str_content=str_send;
        Thread thread_send=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("Send", "treehole send");
                    String token=getAccess_token();
                    HttpClient httpClient=new DefaultHttpClient();
                    HttpPost httpPost=new HttpPost("https://api.renren.com/restserver.do");
                    List<NameValuePair> params=new ArrayList<NameValuePair>();
//                  人人网公有参数传递：
                    params.add(new BasicNameValuePair("v","1.0"));
                    params.add(new BasicNameValuePair("format","json"));
                    params.add(new BasicNameValuePair("access_token",token));
//                  setStatus的私有参数
                    params.add(new BasicNameValuePair("method","status.set"));
                    params.add(new BasicNameValuePair("status",str_content));
//                  关于公共主页的参数
                    params.add(new BasicNameValuePair("page_id","601880046"));

                    httpPost.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
                    HttpResponse httpResponse=httpClient.execute(httpPost);
                    JSONObject jsonObject=new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                    String result=jsonObject.getString("result").toString();
                    Message msg;
                    if(result.equals("1"))
                    {
                        msg=handler_send.obtainMessage(SUCCESS,result);
                    }
                    else
                    {
                        String error_msg=jsonObject.getString("error_msg");
                        msg=handler_send.obtainMessage(FAILED,error_msg);
                        Log.e("Treehole send error",error_msg);
                    }
                    handler_send.sendMessage(msg);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    handler_send.obtainMessage(FAILED);
                }

            }
        });
        thread_send.start();

    }

    public void SendOnSuccess()
    {
        Toast.makeText(context,"发送成功！",Toast.LENGTH_SHORT).show();
        if(send_father!=null)
            send_father.SendOnSuccess();
    }

    public void SendOnFailed()
    {
        Toast.makeText(context,"发送失败！",Toast.LENGTH_SHORT).show();
        if(send_father!=null)
            send_father.SendOnFailed();

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
