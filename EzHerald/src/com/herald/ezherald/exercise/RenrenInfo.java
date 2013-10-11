package com.herald.ezherald.exercise;

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

import android.app.Activity;
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
	private static final String url = "https://api.renren.com/v2/status/list?access_token=241511%7c6.e9d163eb32a823d37609a396abe20618.2592000.1381683600-365328826&ownerId=601258593"; // ��|������д��%7c
	private final int SUCCESS = 1,FAILED = 0;
	private String info;
	private String date;
	private String message;//������ȡ������Ϣ
	private SharedPreferences pref;
	private FragmentA father;
	public Activity activity;
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
	public RenrenInfo(Activity activity,FragmentA father){
		this.activity = activity;
		this.father = father;
		try {
			pref = activity.getApplication().getSharedPreferences("Renren", 0);
			setInfo(pref.getString("info", null));
			setDate(pref.getString("date", null));
		} catch (Exception e) {
			setInfo(null);
			setDate(null);
		}
	}
	protected void onSuccess() {
		// TODO Auto-generated method stub
		//TODO ��ʾ��bug��������
		/*final String[] target= {"��ٲ���","�ܲ��粥��","�ܲ���Ϣ"};
		
		Document document = Jsoup.parse(message);
		Elements feeds = document.getElementsByClass("list");
		Elements lists  = feeds.get(0).children();
		for(int i=0;i<lists.size();i++){
			Element feed = lists.get(i);
			String data = feed.text();
			for (int j=0;j<target.length;j++){
				if(data.indexOf(target[j])!=-1){
					int end = data.lastIndexOf("�ظ�");
					setInfo(data.substring(0, end-1));//�ַ���֮��Ķ������õ�
					DateFormat fmt = SimpleDateFormat.getDateTimeInstance();
					setDate(fmt.format(new Date()));//����ʱ��
					save();
					father.onSuccess();
					return;
				}
			}
		}
		*/
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
				String info = ((JSONObject) array.opt(0)).getString("content");
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
						HttpGet get = new HttpGet(url);
						HttpResponse response = client.execute(get);
						if (response.getStatusLine().getStatusCode() !=  200) {
							throw new Exception();
						}
						String message = EntityUtils.toString(response.getEntity());
						Message msg = handler.obtainMessage(SUCCESS,
								message);
						handler.sendMessage(msg);
					} catch (Exception e) {
						// TODO: handle exception
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