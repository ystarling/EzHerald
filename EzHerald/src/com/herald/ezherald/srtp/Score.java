package com.herald.ezherald.srtp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.UserAccount;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2014/12/12.
 */
public class Score {
    private Fragment father;
    private SharedPreferences pref;
    private Editor editor;

    private int score;
    private String updateTime;
    private Context context;

    public static final int SUCCESS = 1;
    public static final int FAILED  = 0;
    public static  final int DEFAULT_SCORE=0;
    public static final String DEFAULT_UPDATETIME=null;
    private  static final String URL="http://121.248.63.105/service/srtp";

    JSONArray json;
    JSONObject json_detail;
    private MyHandler myHandler=new MyHandler();
    String name="hello";

   public  void setScore(int score){
       this.score=score;
   }
    public int getScore(){
        return  score;
    }

    public void setUpdateTime(String updateTime){
        this.updateTime=updateTime;
    }

    public  String getUpdateTime(){
        return  updateTime;
    }

    class MyHandler extends  Handler{
        @Override
        public void handleMessage(Message msg)
        {
            try{
                // JSONArray jsonarray;
                // jsonarray=json.getJSONArray();
                JSONObject obj = json.getJSONObject(0);
                // json_detail = json.getJSONObject("");
                name=obj.getString("name");
            }
            catch (JSONException e1){
                Toast toast1 = Toast.makeText(context, "解析错误...",
                        Toast.LENGTH_LONG);
                toast1.show();
            }

            super.handleMessage(msg);
        }
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case SUCCESS:
                    onSuccess();
                    break;
                case FAILED:
                    onFiled();
                    break;
            }
        }
    };

    protected void onFiled() {
        // TODO Auto-generated method stub
        //Toast.makeText(activity, "更新失败", Toast.LENGTH_SHORT).show();
        if(father == null)
            return ;
        if(father instanceof FragmentB){
            ((FragmentB) father).onFailed();
        }

    }
    protected void onSuccess() {
        save();
        if(father == null)
            return ;
        if(father instanceof FragmentB){
            ((FragmentB) father).onSuccess();
        }
    }

    public Score(){

    }

    public Score(Context context){
        this.context=context;
        pref = context.getSharedPreferences("Score", Context.MODE_PRIVATE);
        setUpdateTime(pref.getString("UpdateTime", DEFAULT_UPDATETIME));
        setScore(pref.getInt("Score",DEFAULT_SCORE));

    }

    public Score(Context context,Fragment father){
        this.context=context;
        this.father=father;
    }

    public boolean isSet() {
        return score!=DEFAULT_SCORE&&updateTime!=DEFAULT_UPDATETIME;
    }

    public void update(final UserAccount user){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                if(father instanceof FragmentA) {
                    UserAccount user = Authenticate.getTyxUser(context);
                    String name = user.getUsername();
                    String password = user.getPassword();
                    try {
                        HttpClient httpClient = new DefaultHttpClient();
                        //DefaultHttpClient client = new DefaultHttpClient();
                        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
                        params.add(new BasicNameValuePair("number", "213131592"));
                        HttpPost postMethod = new HttpPost(URL);

                        postMethod.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
                        HttpParams httpParams = httpClient.getParams();
                        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
                        HttpConnectionParams.setSoTimeout(httpParams, 5000);

                        HttpResponse response = httpClient.execute(postMethod);
                        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            //v1.setText("chengg");
                            Toast toast1 = Toast.makeText(context,"网络请求错误...",
                                    Toast.LENGTH_LONG);
                            toast1.show();
                        }

                        InputStream is = response.getEntity().getContent();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is,
                                "UTF-8"));
                        String line = null;
                        StringBuffer sb = new StringBuffer();
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        json = new JSONArray(sb.toString());
                        Message msg = Message.obtain();
                        // msg.obj = json;
                        msg.setTarget(myHandler);
                        msg.sendToTarget();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                    else{


                }
                }
                catch (Exception e){
                    e.printStackTrace();
                    handler.obtainMessage(FAILED).sendToTarget();
                }

            }
        }).start();

    }


    public void save(){
        editor = pref.edit();
        editor.putInt("Score",getScore());
        editor.putString("UpdateTime",getUpdateTime());
        editor.commit();
    }

    public void clear() {
        setScore(0);
        save();
    }
}
