package com.herald.ezherald.gpa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.herald.ezherald.account.UserAccount;

public class GpaInfo {
	public static final boolean DEBUG = false;
	private GpaDbModel gpaDbModel;
	private List<Record> records;
	private final int SUCCESS = 0;
	private final int FAILED = 1;
	private GpaAdapter adapter;
	private String result;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
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
		adapter.onLoadFinished();
		adapter.onDealing(0, 100);
		
		gpaDbModel.open();
		gpaDbModel.clear();
		gpaDbModel.close();
		
		
		Document document = Jsoup.parse(result);
		Elements trs = document
				.select("tr[onMouseOver=this.style.backgroundColor=\'#bbbbbb\']");
		int count = trs.size(), i = 0;
		records = new ArrayList<Record>();
		for (Element tr : trs) {
			Log.w("re", tr.child(4).text().trim());
			Record temp = new Record();
			temp.setSemester(tr.child(1).text().trim());
			temp.setName(rtrim(tr.child(3).text().trim()));
			temp.setCredit(Float.parseFloat(rtrim(tr.child(4).text())));

			temp.setScore(rtrim(tr.child(5).text().trim()));
			temp.setScoreType(rtrim(tr.child(6).text().trim()));
			temp.setExtra(tr.child(7).text().isEmpty() ? null : rtrim(tr.child(
					7).text()));
			records.add(temp);
			adapter.onDealing(++i, count);
		}
		save();
		adapter.updateFinished(true);

	}

	protected void onFailed() {
		gpaDbModel.open();
		records = gpaDbModel.all();
		adapter.updateFinished(false);
	}

	public GpaInfo(Context context, GpaAdapter adapter) {
		this(context);
		this.adapter = adapter;
	}

	public GpaInfo(Context context) {

		try {
			gpaDbModel = new GpaDbModel(context);
			gpaDbModel.open();
			records = gpaDbModel.all();
		} catch (Exception e) {
			records = null;
		}
		gpaDbModel.close();
	}

	/**
	 * @return 算出的平均绩点
	 * @throws Exception 
	 */
	public float calcAverage() throws Exception {
		float totalGrade = 0, totalCredit = 0;
		Map<String,Float> gradeMap = new HashMap<String,Float>();
		for(Record r:records) {
			if(r.isSelected()){
				if(gradeMap.containsKey(r.getName())){
					Float grades = gradeMap.get(r.getName());
					if(r.getPoint()*r.getCredit()>grades) {
						gradeMap.put(r.getName(), r.getPoint()*r.getCredit());
					}
				}else{
					gradeMap.put(r.getName(), r.getPoint()*r.getCredit());
					totalCredit+=r.getCredit();
				}
			}
		}
		
		for(Float r:gradeMap.values()){
			totalGrade += r;
		}
		if(totalCredit == 0) {
			throw new Exception("No course is selected");
		}
		return totalGrade / totalCredit;
	}

	public void update(final int vercode, final HttpClient client,
			final UserAccount user) {
		new Thread() {
			@Override
			public void run() {
				HttpClient httpclient = client;
				HttpPost httppost = new HttpPost(
						"http://xk.urp.seu.edu.cn/studentService/system/login.action");

				try {
					// Add your data
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							2);
					nameValuePairs.add(new BasicNameValuePair("userName", user
							.getUsername()));
					nameValuePairs.add(new BasicNameValuePair("password", user
							.getPassword()));
					nameValuePairs.add(new BasicNameValuePair("vercode", ""
							+ vercode));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					// Execute HTTP Post Request
					HttpResponse response = httpclient.execute(httppost);

					if (response.getStatusLine().getStatusCode() == 200) {
						String url = "http://xk.urp.seu.edu.cn/studentService/cs/stuServe/studentExamResultQuery.action";
						HttpResponse response2 = httpclient
								.execute(new HttpGet(url));
						if (response2.getStatusLine().getStatusCode() != 200) {
							throw new IOException();
						}
						String result2 = EntityUtils.toString(response2
								.getEntity());
						Message msg = handler.obtainMessage(SUCCESS, result2);
						handler.sendMessage(msg);
					} else {
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

	public void save() {
		gpaDbModel.open();
		gpaDbModel.update(records);
		gpaDbModel.close();
	}

	public void selectionChanged(Record r, boolean newState) {
		gpaDbModel.open();
		gpaDbModel.changeSelection(r.getName(), newState);
		gpaDbModel.close();
		// TODO
	}

	public void removeOptional() {
		// TODO Auto-generated method stub
		gpaDbModel.open();
		for (Record r : records) {
			if (!r.getExtra().equals("")) {
				r.setSelected(false);//更新显示的数据
				gpaDbModel.changeSelection(r.getName(), false);//更新数据库
			}
		}
		gpaDbModel.close();
	}

	/**
	 * 去除最右边的一个异常字符，原生的trim不起作用
	 * 
	 * @param s
	 * @return
	 */
	private String rtrim(String s) {
		return s.substring(0, s.length() - 1);
	}

	public void selectAll() {
		// TODO Auto-generated method stub
		gpaDbModel.open();
		for(Record r:records){
			if(r.isSelected() == false){
				r.setSelected(true);//更新显示的数据
				gpaDbModel.changeSelection(r.getName(), true);//更新数据库
			}
			
		}
		gpaDbModel.close();
	}
}
