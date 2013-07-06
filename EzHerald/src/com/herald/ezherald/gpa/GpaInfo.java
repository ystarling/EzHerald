package com.herald.ezherald.gpa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GpaInfo {
	public static final boolean DEBUG = false;
	private GpaDbModel gpaDbModel;
	private List<Record> records;
	private final int SUCCESS = 0;
	private final int FAILED  =  1;
	private GpaAdapter adapter;
	private String result;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what){
				case SUCCESS:
					result = (String) msg.obj;
					onSuccess();
					break;
				case FAILED:
					onFailed();
					break;
				default:
					break;
				
			}
		};
	};
	public List<Record> getRecords() {
		return records;
	}
	protected void onSuccess() {
		// TODO 解析HTML，更新records
//		Log.w("res",result);
		Document document = Jsoup.parse(result);
		Elements trs = document.select("tr[onMouseOver=this.style.backgroundColor=\'#bbbbbb\']");
		records = new ArrayList<Record>();
		for(Element tr:trs){
			Log.w("re",tr.child(4).text().trim());
			Record temp = new Record();
			temp.setSemester(tr.child(1).text().trim());
			temp.setName(rtrim(tr.child(3).text().trim()));
			temp.setCredit(Float.parseFloat(rtrim(tr.child(4).text())));
			
			temp.setScore(rtrim(tr.child(5).text().trim()));
			temp.setScoreType(rtrim(tr.child(6).text().trim()));
			temp.setExtra(tr.child(7).text().isEmpty()?null:rtrim(tr.child(7).text()));
			records.add(temp);
		}
		save();
		adapter.updateFinished(true);
		
	}
	protected void onFailed() {
		// TODO Auto-generated method stub
		gpaDbModel.open();
		records = gpaDbModel.all();
		adapter.updateFinished(false);
	}
	public GpaInfo(Context context ,GpaAdapter adapter ){
		this(context);
		this.adapter = adapter;
	}
	public GpaInfo(Context context){
		
		
		try {
			gpaDbModel = new GpaDbModel(context);
			gpaDbModel.open();
			records = gpaDbModel.all();
		} catch (Exception e) {
			// TODO: handle exception
			records = null;
		}
		gpaDbModel.close();
	}
	/**
	 * @return 算出的所有绩点
	 */
	public float calcAverage(){
		float totalGrade=0,totalCredit=0;
		for(Record r:records){
			if(r.isSelected() && r.getPoint() > 0 ) {
				totalGrade  +=r.getPoint() * r.getCredit();
				totalCredit +=r.getCredit();
			}
		}
		return totalGrade/totalCredit;
	}
	public void update(final int vercode,final HttpClient client) {
		if( DEBUG ) {
			records = new ArrayList<Record>();
			records.add(new Record("高数","78",5.0f,"12-13-2","首修",null,false));
			records.add(new Record("大物","良",4.0f,"12-13-1","首修",null,false));
			records.add(new Record("物理实验","通过",2.0f,"12-13-1","首修",null,true));
			records.add(new Record("艺术鉴赏","98",5.0f,"12-13-3","首修","人文类",false));
			records.add(new Record("离散数学","68",5.0f,"12-13-2","首修",null,false));
			records.add(new Record("c++","100",2.5f,"12-13-2","首修",null,true));
		} else {
			//TODO update gpa info
			new Thread(){
				@Override
				public void run() {
					HttpClient httpclient = client;
				    HttpPost httppost = new HttpPost("http://xk.urp.seu.edu.cn/studentService/system/login.action");
				    
				    try {
				        // Add your data
				        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				        nameValuePairs.add(new BasicNameValuePair("userName", "213123363"));
				        nameValuePairs.add(new BasicNameValuePair("password", "213123363"));
				        nameValuePairs.add(new BasicNameValuePair("vercode", ""+vercode));
				        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				        // Execute HTTP Post Request
				        HttpResponse response = httpclient.execute(httppost);
				        if(response.getStatusLine().getStatusCode() == 200){
				        	String url = "http://xk.urp.seu.edu.cn/studentService/cs/stuServe/studentExamResultQuery.action";
				        	HttpResponse response2 = httpclient.execute(new HttpGet(url));
				        	if(response2.getStatusLine().getStatusCode() != 200){
				        		throw new IOException();
				        	}
				        	String result2= EntityUtils.toString(response2.getEntity());
				        	Message msg = handler.obtainMessage(SUCCESS, result2);
				        	handler.sendMessage(msg);
				        }else{
				        	handler.obtainMessage(FAILED).sendToTarget();
				        }
				        
				    } catch (ClientProtocolException e) {
				    	handler.obtainMessage(FAILED).sendToTarget();
				    } catch (IOException e) {
				    	handler.obtainMessage(FAILED).sendToTarget();
				    } 
				};
			}.start();
		}
	}
	public void save(){
		gpaDbModel.open();
		gpaDbModel.update(records);
		gpaDbModel.close();
	}
	public void selectionChanged(Record r,boolean newState){
		gpaDbModel.open();
		gpaDbModel.changeSelection(r.getName(),newState);
		gpaDbModel.close();
		//TODO 
	}
	public void removeOptional() {
		// TODO Auto-generated method stub
		gpaDbModel.open();
		for(Record r:records){
			if(r.getExtra() != null )
				r.setSelected(false);
				gpaDbModel.changeSelection(r.getName(), false);
		}
		gpaDbModel.close();
	}
	private String rtrim(String s){
		return s.substring(0,s.length()-1);
	}
}
