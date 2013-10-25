package com.herald.ezherald.exercise;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * @author xie
 * ����ϵ���˵���ٲ�����Ϣ
 */
public class RenrenInfo{
	public static final boolean DEBUG = false;//TODO��just for debug,must be removed before release 
	private final String URL = "https://api.renren.com/v2/status/list?access_token=";
	private final int SUCCESS = 1,FAILED = 0;
	private String info;
	private String date;
	private String message;//������ȡ������Ϣ
	private SharedPreferences pref;
	private FragmentA father;
	public Context context;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch (msg.what){
			case SUCCESS:
				message = (String)msg.obj;
				onSuccess();
				break;
			case FAILED:
				onFailed();
			}
		}
	};
	/**
	 * @param activity �����ߵ�activity
	 * ����ʱ���sharedPreference���Զ�����
	 */
	public RenrenInfo(Context context,FragmentA father){
		this.context = context;
		this.father = father;
		try {
			pref = context.getSharedPreferences("Renren", 0);
			setInfo(pref.getString("info", null));
			setDate(pref.getString("date", null));
		} catch (Exception e) {
			setInfo(null);
			setDate(null);
		}
	}
	protected void onSuccess() {
		// TODO Auto-generated method stub
		try {
			//String today = android.text.format.DateFormat.format("yyyy-m-d",new Date()).toString();
			//Date  date = new Date();
			Calendar calendar = Calendar.getInstance();
			String today = String.format("%d-%d-%d", calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DATE));
			JSONObject json = new JSONObject(message);
			JSONArray array = json.getJSONArray("response");
			setInfo("");
			for(int i=0;i<array.length();i++){
				JSONObject object = (JSONObject) array.opt(i);
				String date = object.getString("createTime");
				date = date.split(" ")[0];
				if(date.equals(today)){
					String info = object.getString("content");
					setInfo(getInfo()+info+"\n\n\n");
					setDate(date);
					save();
				}else{
					break;
				}
			}
			if(getInfo()==null||getInfo().equals("")){//û���ҵ�����ģ���ʾ����һ��
				String info = "\n\n����û���ܲ���Ϣ\n\n";
				setInfo(info);
				setDate(today);
				save();
			}
			father.onSuccess();
			DateFormat fmt = SimpleDateFormat.getDateTimeInstance(); 
			setDate(fmt.format(new Date()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		save();
	}
	protected void onFailed() {
		// TODO Auto-generated method stub
		//Toast.makeText(activity, "������Ϣ����ʧ��", Toast.LENGTH_SHORT).show();
		father.onFailed();
	}
	/**
	 * ��������
	 */
	public void update(){
		if(DEBUG){//һЩ��������
			setInfo("���������ܲ١�20 ~ 30�档����ת�硣û�𴲵ĸ�λ���ǸϽ������ٰܲɡ�");
			DateFormat fmt = SimpleDateFormat.getDateTimeInstance(); 
			setDate(fmt.format(new Date()));
			save();
			return ;
		}else{
			new Thread(){
				@Override
	    		public void run() {
					try {
						Log.w("update","updating renren");
						HttpClient client = new DefaultHttpClient();
						String refreshurl = "https://graph.renren.com/oauth/token?grant"+URLEncoder.encode("_")+"type=refresh"+URLEncoder.encode("_")+"token&refresh"+URLEncoder.encode("_")+"token="+URLEncoder.encode("241511|0.NvNrQow4rEchFtbdaCUtdyA5dWLgRgDh.365328826.1379088139819")+"&client"+URLEncoder.encode("_")+"id=241511&client"+URLEncoder.encode("_")+"secret=8d970a6e9e3249e9afffd2fdba73f018";
						HttpGet refresh = new HttpGet(refreshurl);
						HttpResponse response = client.execute(refresh);
						String message = EntityUtils.toString(response.getEntity());
						JSONObject json  = new JSONObject(message);
						String token = json.getString("access_token");
						token = URLEncoder.encode(token);
						HttpGet get = new HttpGet(URL+token+"&ownerId=601258593");
						response = client.execute(get);
						message = EntityUtils.toString(response.getEntity());
						if (response.getStatusLine().getStatusCode() !=  200) {
							throw new Exception(response.toString());
						}
						
						Message msg = handler.obtainMessage(SUCCESS,
								message);
						handler.sendMessage(msg);
					} catch (Exception e) {
						
						e.printStackTrace();
						handler.obtainMessage(FAILED).sendToTarget();
					}
				}
			}.start();
		}
		
	}
	
	/**
	 * �������ݵ�sharedPreference;
	 */
	public void save(){
		Editor editor = pref.edit();
		editor.putString("info", getInfo());
		editor.putString("date", getDate());
		editor.commit();
	}
	
	/**
	 * @return boolean �����Ƿ�Ϊ��
	 */
	public boolean isSet(){
		if(info == null || date == null)
			return false;
		return true;
	}
	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}