package com.herald.ezherald.srtp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.UserAccount;

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
    private  static final String URL="";



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
                if(father instanceof FragmentA){
                    UserAccount user = Authenticate.getTyxUser(context);
                    String name = user.getUsername();
                    String password = user.getPassword();
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
