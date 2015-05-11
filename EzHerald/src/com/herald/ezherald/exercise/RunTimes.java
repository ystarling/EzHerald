package com.herald.ezherald.exercise;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.UserAccount;
import com.herald.ezherald.api.APIAccount;
import com.herald.ezherald.api.APICache;
import com.herald.ezherald.api.APIClient;
import com.herald.ezherald.api.APIFactory;
import com.herald.ezherald.api.FailHandler;
import com.herald.ezherald.api.Status;
import com.herald.ezherald.api.SuccessHandler;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 *
 * @author xie
 * 跑操次数的信息
 */
public class RunTimes {
	public static final boolean DEBUG = false;//TODO Only for debug must be removed
	private int    times; //查到的跑操次数
	private int    adjustTimes;//修正的次数
	private float  rate;//排名比例
	private int    remainDays;//本学期剩余天数
	private String startDate;//本学期开始的日期
	private String averageRunTime;//平均打卡时间
	private int    adviceTime;//推荐每周跑操天数
	private String updateTime;//更新时间
	private Fragment father;//上一级fragment
	
	
	private String timesAndRateXml;//次数与比例的xml
	
	
	public static final int    DEFAULT_TIMES = 0;
	public static final int    DEFAULT_ADJUST_TIMES = 0;
	public static final float  DEFAULT_RATE = -1;
	public static final int    DEFAULT_REMAIN_DAYS = -1;  
	public static final String DEFAULT_START_DATE = null;
	public static final String DEFAULT_AVERAGE_RUN_TIME=null;
	public static final int    DEFAULT_ADVICE_TIME=-1;
	public static final String DEFAULT_UPDATE_TIME = null;
	private static final int SUCCESS = 1;
	private static final int FAILED  = 0;
	
	private SharedPreferences pref;
    JSONObject json;
	private Editor editor;
	//private Activity activity;
	
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
	private Context context;
	public int getTimes() {
		return times;
	}
	protected void onFiled() {
		// TODO Auto-generated method stub
		//Toast.makeText(activity, "更新失败", Toast.LENGTH_SHORT).show();
		if(father == null)
			return ;
		if(father instanceof FragmentB){
			((FragmentB) father).onFailed();
		}else if(father instanceof FragmentC){
			((FragmentC) father).onFailed();
		}
	}
	protected void onSuccess() {
		
		save();
		if(father == null)
			return ;
		if(father instanceof FragmentB){
			((FragmentB) father).onSuccess();
		}else if(father instanceof FragmentC){
			((FragmentC) father).onSuccess();
		}
	}
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
	public int getRemainDays() {
		return remainDays;
	}
	public void setRemainDays(int RemainDays) {
		this.remainDays = RemainDays;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public int getAdjustTimes() {
		return adjustTimes;
	}
	public void setAdjustTimes(int adjustTimes) {
		if (adjustTimes<100) {
			this.adjustTimes = adjustTimes;
		}
	}
	public String getAverageRunTime() {
		return averageRunTime;
	}
	public void setAverageRunTime(String averageRunTime) {
		this.averageRunTime = averageRunTime;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public int getAdviceTime() {
		
		return adviceTime;
	}
	public void setAdviceTime(int adviceTime) {
		this.adviceTime = adviceTime;
	}
	
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
 /**
 * 空构造函数，不读取shared的数据
 */
	public RunTimes() {
		
	}

	public RunTimes(Context context,Fragment father){
		this(context);
		this.father=father;
	}
	
	/**
	 * @param context 调用者的Activity
	 * 构造时会尝试从sharedPreference读取数据
	 */
	public RunTimes(Context context){
		this.context = context;
		pref = context.getSharedPreferences("RunTimes", Context.MODE_PRIVATE);
		//pref = activity.getApplication().getSharedPreferences("RunTimes", 0);
		setTimes(pref.getInt("Times", DEFAULT_TIMES));
		setAdjustTimes(pref.getInt("AdjustTimes",DEFAULT_ADJUST_TIMES));
		setRate(pref.getFloat("Rate", DEFAULT_RATE));
		setStartDate(pref.getString("StartDate", DEFAULT_START_DATE));
		setAverageRunTime(pref.getString("AverageRunTime", DEFAULT_AVERAGE_RUN_TIME));
		setUpdateTime(pref.getString("UpdateTime",DEFAULT_UPDATE_TIME));
		setRemainDays(pref.getInt("RemainDays", DEFAULT_REMAIN_DAYS));
		setAdviceTime(calcAdviceTime());
	}
	/**
	 * @return Boolean 是否已经有数据
	 */
	public boolean isSet(){
		return times != DEFAULT_TIMES && rate  !=DEFAULT_RATE 
			&& startDate != DEFAULT_START_DATE && averageRunTime!=DEFAULT_AVERAGE_RUN_TIME 
			&& updateTime!= DEFAULT_UPDATE_TIME ;
	}															  
	/**
	 * 更新数据
	 */
	public void update(){
        APIAccount apiAccount = new APIAccount(context);
//        apiAccount.isUUIDValid();
        APIClient client= APIFactory.getAPIClient(context,"api/pe",new SuccessHandler() {
            @Override
            public void onSuccess(String data) {
                try {
                    json = new JSONObject(data);
                    dealJson(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new FailHandler() {
            @Override
            public void onFail(Status status, String message) {
                Log.d("error",message);}
	});
        client.addUUIDToArg();
        if(client.isCacheAvailable()){
            Log.w("step","hascache");
            client.readFromCache();
        }
        else{
            Log.w("step","nocache");
            client.requestWithCache();
        }
        }

    public void dealJson(JSONObject jsonArray){
        try{
            String obj = json.getString("content");
            setTimes(Integer.parseInt(obj));
            Log.d("times",obj);

            Calendar calendar = Calendar.getInstance();
            String today = String.format("%d-%d-%d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
            setUpdateTime(today);
            //设置学期结束时间为7月1号，并获取间隔天数
            String end = "2015-07-01";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            int days = (int)((sdf.parse(end).getTime()-sdf.parse(today).getTime())/(24*60*60*1000));
            setRemainDays(days);

            Log.d("remainday",String.valueOf(days));

            setAdviceTime(calcAdviceTime());
            onSuccess();
        } catch (JSONException e) {
            e.printStackTrace();
            onFiled();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
	/**
	 * 保存数据到sharedPreference
	 */
	public void save(){
		editor = pref.edit();
		editor.putInt("Times", getTimes());
		editor.putInt("AdjustTimes", getAdjustTimes());
		editor.putFloat("Rate", getRate());
		editor.putString("StartDate",getStartDate());
		editor.putString("AverageRunTime", getAverageRunTime());
		editor.putInt("RemainDays", getRemainDays());
		editor.commit();
	}
	
	
	/**
	 * @return 建议每周跑操时间,向上取整
	 */
	private int calcAdviceTime(){
		if(times == DEFAULT_ADVICE_TIME)
			return -1;
		int remainWeeks = remainDays/5;
		int remainTimes = 45 - times;
		int advice;
		if (remainWeeks>0) {
			advice = remainTimes/remainWeeks;
			if( advice*remainWeeks <remainTimes)
				advice++;
		}else if(remainDays != 0){
			advice = remainTimes;
		}else{
			advice = 0;
		}
		return advice;
	}
	
	public void clear() {
		setTimes(0);
		save();
	}
}
