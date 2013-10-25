package com.herald.ezherald.exercise;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.UserAccount;

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
	private Fragment father;//上一级fragment
	
	private String timesAndRateXml;//次数与比例的xml
	
	
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
	private static final String REMAIN_DAYS_URL = "http://herald.seu.edu.cn/ws/exercise/remain";
	private static final String RUNTIMES_URL = "http://herald.seu.edu.cn/ws/exercise/runtimes";
	
	private SharedPreferences pref;
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
	 * @param activity 调用者的Activity
	 * 构造时会尝试从sharedPreference读取数据
	 */
	public RunTimes(Context context){
		this.context = context; 
		pref = context.getSharedPreferences("RunTimes", 0);
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
	public void update(final UserAccount user){
		if(DEBUG){//一些测试数据
			setTimes(19);
			setRate((float)0.23);
			setStartDate("2013-06-01");
			//setRemainDays(calcRemainDays());
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
						if(father instanceof FragmentB){
							UserAccount user = Authenticate.getTyxUser(context);
							String name = user.getUsername();
							String password = user.getPassword();
							
							
							DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
							DocumentBuilder builder = factory.newDocumentBuilder();
							Document document = builder.parse(RUNTIMES_URL+"?username="+name+"&password="+password);
							Node timesNode = document.getElementsByTagName("times").item(0);
							//Node rateNode = document.getElementsByTagName("rate").item(0);
							
							setTimes(Integer.parseInt(timesNode.getTextContent()));
						}else if(father instanceof FragmentC){
							HttpClient client= new DefaultHttpClient();
							HttpGet remainDaysGet = new HttpGet(REMAIN_DAYS_URL);
							HttpResponse response = client.execute(remainDaysGet);
							if(response.getStatusLine().getStatusCode() != 200){
								throw new Exception("net error");
							}
							String result = EntityUtils.toString(response.getEntity());
							int remDays = Integer.parseInt( result);
							setRemainDays(remDays);
							setAdviceTime(calcAdviceTime());
						}
						Message msg = handler.obtainMessage(SUCCESS);
			        	handler.sendMessage(msg);
					}catch(Exception e){
						e.printStackTrace();
						handler.obtainMessage(FAILED).sendToTarget();
					}
				}
			}.start();
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
		//TODO calculate the advise time
		int remainWeeks = remainDays/5;
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
