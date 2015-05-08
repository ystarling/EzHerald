package com.herald.ezherald.srtp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.UserAccount;
import com.herald.ezherald.api.APIAccount;
import com.herald.ezherald.api.APIClient;
import com.herald.ezherald.api.APIFactory;
import com.herald.ezherald.api.FailHandler;
import com.herald.ezherald.api.Status;
import com.herald.ezherald.api.SuccessHandler;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2014/12/12.
 */
public class Score {
    private Fragment father=new FragmentA();
    private SharedPreferences pref;
    private Editor editor;

    private double score;
    private String updateTime;
    private Context context;

    public static final int SUCCESS = 1;
    public static final int FAILED  = 0;
    public static  final String DEFAULT_SCORE="0";
    public static  final double DEFAULT_SCORE_A=0;
    public static final String DEFAULT_RESULT=null;
    public static final String DEFAULT_UPDATETIME=null;


    JSONObject json;
    String result=null;
    ArrayList project = new ArrayList();
    public  void setScore(double score){
        this.score=score;
    }
    public double getScore(){
        return  score;
    }

    public void setUpdateTime(String updateTime){
        this.updateTime=updateTime;
    }

    public  String getUpdateTime(){
        return  updateTime;
    }

    public ArrayList getProject(){
        return project;
    }
    protected void onFiled() {
        // TODO Auto-generated method stub
        //Toast.makeText(activity, "更新失败", Toast.LENGTH_SHORT).show();
        if(father == null)
            return ;
        else if(father instanceof FragmentB){
            ((FragmentB) father).onFailed();
        }
        else if(father instanceof FragmentA){
            ((FragmentA)father).onFailed();
        }

    }
    protected void onSuccess() {
       save();
        if(father == null)
            return ;
        if(father instanceof FragmentB){
            ((FragmentB) father).onSuccess();
        }
        else if(father instanceof FragmentA){
            ((FragmentA)father).onSuccess();
        }
    }

    public Score(){

    }

    public Score(Context context){
        this.context=context;
        pref = context.getSharedPreferences("Srtp", Context.MODE_PRIVATE);
        result = pref.getString("result", DEFAULT_RESULT);
   //     Log.w("result",result);

//        setUpdateTime(pref.getString("UpdateTime", DEFAULT_UPDATETIME));
//        setScore(Double.parseDouble(pref.getString("Score",DEFAULT_SCORE)));

    }

    public Score(Context context,Fragment father){
        //调用另一个构造函数，读取数据
        //this(context);
        this.context=context;
        this.father=father;
        if(result!=null) {
            try {
                json = new JSONObject(result);
                init(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //dealJson(json);

        }

    }

    public boolean isSet() {
        Log.w("score",String.valueOf(score));
        return score!=DEFAULT_SCORE_A&&updateTime!=DEFAULT_UPDATETIME;
    }


    public void save(){
        editor = pref.edit();
        editor.putString("result",result);
        editor.commit();
    }

    public void clear() {
        setScore(0);
        //如果不把project内容清空的话，那么下次再更新会导致这里有重复的
        project.clear();
        //save();
    }


    public void getScoreFromApi(){
        clear();
        APIAccount apiAccount=new APIAccount(context);
        apiAccount.isUUIDValid();
        APIClient client= APIFactory.getAPIClient(context,"api/srtp",new SuccessHandler() {
            @Override
            public void onSuccess(String data) {
                try {
                    result=data;
                    json = new JSONObject(data);
                    dealJson(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new FailHandler() {
            @Override
            public void onFail(Status status, String message) {
                Log.d("error",message);
            }
        });
        client.addUUIDToArg();
        //cache可以使用
        if(client.isCacheAvailable()){
            client.readFromCache();
        }
        else{
            client.requestWithCache();
        }
        //cache不可使用
        //client.doRequest();
    }
    public void init(JSONObject jsonArray){
        try{
            JSONArray obj = json.getJSONArray("content");
            JSONObject nameMessage = obj.getJSONObject(0);
            String name=nameMessage.getString("name");
            double score_get = Double.parseDouble(nameMessage.getString("total"));
            setScore(score_get);

            for(int i =1;i<obj.length();i++){
                JSONObject type = obj.getJSONObject(i);
                project.add(type.getString("credit"));
                project.add(type.getString("project"));
                project.add(type.getString("date"));
            }

            Calendar calendar = Calendar.getInstance();
            String today = String.format("%d-%d-%d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
            setUpdateTime(today);
        }
        catch (JSONException e1){
            Toast toast1 = Toast.makeText(context, "解析错误...",
                    Toast.LENGTH_LONG);
            toast1.show();
            e1.printStackTrace();
        }
    }
    public void dealJson(JSONObject jsonArray) {
        init(jsonArray);
        onSuccess();
    }
}