package com.herald.ezherald.exercise;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.herald.ezherald.account.UserAccount;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import cn.edu.seu.herald.ws.api.HeraldWebServicesFactory;
import cn.edu.seu.herald.ws.api.MorningExerciseService;
import cn.edu.seu.herald.ws.api.exercise.ObjectFactory;
import cn.edu.seu.herald.ws.api.exercise.RunTimesData;
import cn.edu.seu.herald.ws.api.impl.HeraldWebServicesFactoryImpl;

/**
 * @author xie
 * 跑操次数的信息
 */
public class RunTimes {
	public static final boolean DEBUG = false;//TODO Only for debugmust be removed
	private int    times; //查到的跑操次数
	private int    adjustTimes;//修正的次数
	private float  rate;//排名比例
	private int    remainDays;//本学期剩余天数
	private String startDate;//本学期开始的日期
	private String averageRunTime;//平均打卡时间
	private int    adviceTime;//推荐每周跑操天数
	private String updateTime;//更新时间
	
	public static final int    DEFAULT_TIMES = -999;
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
	private Editor editor;
	private Activity activity;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case SUCCESS:
				onSuccess((Integer)msg.obj);
				break;
			case FAILED:
				onFiled();
				break;
			}
		}
	};
	public int getTimes() {
		return times;
	}
	protected void onFiled() {
		// TODO Auto-generated method stub
		Toast.makeText(activity, "更新失败", Toast.LENGTH_SHORT).show();
	}
	protected void onSuccess(int result) {
		// TODO Auto-generated method stub
		setTimes(result);
		save();
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
	public RunTimes(){
		
	}
	/**
	 * @param activity 调用者的Activity
	 * 构造时会尝试从sharedPreference读取数据
	 */
	public RunTimes(Activity activity){
		this.activity = activity; 
		pref = activity.getApplication().getSharedPreferences("RunTimes", 0);
		
		setTimes(pref.getInt("Times", DEFAULT_TIMES));
		setAdjustTimes(pref.getInt("AdjustTimes",DEFAULT_ADJUST_TIMES));
		setRate(pref.getFloat("Rate", DEFAULT_RATE));
		setStartDate(pref.getString("StartDate", DEFAULT_START_DATE));
		setAverageRunTime(pref.getString("AverageRunTime", DEFAULT_AVERAGE_RUN_TIME));
		setUpdateTime(pref.getString("UpdateTime",DEFAULT_UPDATE_TIME));
		setRemainDays(calcRemainDays());
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
	public void update(UserAccount user){
		if(DEBUG){//一些测试数据
			setTimes(19);
			setRate((float)0.23);
			setStartDate("2013-06-01");
			setRemainDays(calcRemainDays());
			setAdviceTime(calcAdviceTime());
			setAverageRunTime("07:00");
			DateFormat fmt = SimpleDateFormat.getDateTimeInstance();
			setUpdateTime(fmt.format(new Date()));
			save();
			return;
		}else{
			new Thread(){
				@Override
				public void run(){
					try{
						// Web服务地址
						final String HERALD_WS_BASE_URI = "http://herald.seu.edu.cn/ws";
						// 构造Web服务工厂
						HeraldWebServicesFactory factory = new HeraldWebServicesFactoryImpl(HERALD_WS_BASE_URI);
						// 获取某个特定的Web服务
						MorningExerciseService morningExerciseService = factory.getMorningExerciseService();
						RunTimesData runTimesData = morningExerciseService.getRunTimesData(user.getUsername(), user.getPassword());
						
						int result = runTimesData.getTimes().intValue();
						//result.setTimes(runTimesData.getTimes().intValue());
						Message msg = handler.obtainMessage(SUCCESS, result);
			        	handler.sendMessage(msg);
					}catch(Exception e){
						handler.obtainMessage(FAILED).sendToTarget();
					}
				}
			}.start();
		}
		/*
		try {
			setAdjustTimes(pref.getInt("AdjustTimes",DEFAULT_ADJUST_TIMES));
			ObjectFactory factory = new ObjectFactory();
			RunTimesData runTimesData = factory.createRunTimesData();
			setTimes(runTimesData.getTimes().intValue());
			setRate(runTimesData.getRate().floatValue());
			setRemainDays(calcRemainDays());
			setAdviceTime(calcAdviceTime());
			//TODO start time,average TIme
            //TODO 更新所有信息
			save();
			throw new Exception();
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(activity, "更新失败", Toast.LENGTH_SHORT).show();
		}
		*/
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
		editor.commit();
	}
	/**
	 * @return 本学期剩余的天数
	 * 根据本学期开始日期和当前日期返回剩余天数
	 */
	private int calcRemainDays(){
		final int TOTAL_DAYS = 16*7;
		Calendar today = Calendar.getInstance();
		Calendar start = Calendar.getInstance();
		SimpleDateFormat fmt = new SimpleDateFormat("yy-MM-dd");
		Date date=null;
		try {
			if(startDate == null)
				throw new ParseException("string is null",0);
			date = fmt.parse(startDate);
			start.setTime(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int diffDays = 0;
		if(today.get(Calendar.YEAR)== start.get(Calendar.YEAR)){//同一年
			diffDays = today.get(Calendar.DAY_OF_YEAR)-start.get(Calendar.DAY_OF_YEAR);
		}else{
			Calendar endOfYear =  Calendar.getInstance();
			endOfYear.set(Calendar.YEAR,start.get(Calendar.YEAR));
			endOfYear.set(Calendar.MONTH,Calendar.DECEMBER);
			endOfYear.set(Calendar.DATE,31);
			diffDays = endOfYear.get(Calendar.DAY_OF_YEAR)-start.get(Calendar.DAY_OF_YEAR)+today.get(Calendar.DAY_OF_YEAR);
			Log.w("endOfYear",""+endOfYear.get(Calendar.DAY_OF_YEAR));
		}
		return TOTAL_DAYS-diffDays;//总天数减去已经过去的天数
	}

	/**
	 * @return 建议每周跑操时间,向上取整
	 */
	private int calcAdviceTime(){
		//TODO calculate the advise time
		int remainWeeks = remainDays/7;
		int remainTimes = 45 - times;
		int advice;
		if (remainWeeks>0) {
			advice = remainTimes/remainWeeks;
			if( advice*remainWeeks <remainTimes)
				advice++;
		}else if(remainDays == 0){
			advice = remainTimes;
		}else{
			advice = 0;
		}
		return advice;
	}
}
