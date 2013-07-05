package com.herald.ezherald.exercise;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;
import cn.edu.seu.herald.ws.api.CurriculumService;
import cn.edu.seu.herald.ws.api.HeraldWebServicesFactory;
import cn.edu.seu.herald.ws.api.exercise.Broadcast;
import cn.edu.seu.herald.ws.api.exercise.ObjectFactory;
import cn.edu.seu.herald.ws.api.impl.HeraldWebServicesFactoryImpl;
/**
 * @author xie
 * ����ϵ���˵���ٲ�����Ϣ
 */
public class RenrenInfo{
	public static final boolean DEBUG = true;//TODO��just for debug,must be removed before release 
	
	private String info;
	private String date;
	private SharedPreferences pref;
	public Activity activity;
	
	/**
	 * @param activity �����ߵ�activity
	 * ����ʱ���sharedPreference���Զ�����
	 */
	public RenrenInfo(Activity activity){
		this.activity = activity;
		try {
			pref = activity.getApplication().getSharedPreferences("Renren", 0);
			setInfo(pref.getString("info", null));
			setDate(pref.getString("date", null));
		} catch (Exception e) {
			setInfo(null);
			setDate(null);
		}
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
		}
		try {
			Log.w("update","updating renren");
			//TODO
			
			save();
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(activity, "����ʧ��", Toast.LENGTH_SHORT).show();
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