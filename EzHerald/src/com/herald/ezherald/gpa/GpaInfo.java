package com.herald.ezherald.gpa;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.herald.ezherald.account.UserAccount;
import com.herald.ezherald.api.APIClient;
import com.herald.ezherald.api.APIFactory;
import com.herald.ezherald.api.FailHandler;
import com.herald.ezherald.api.Status;
import com.herald.ezherald.api.SuccessHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        try{
            JSONObject json  = new JSONObject(result);
            JSONArray content = json.getJSONArray("content");
            records = new ArrayList<Record>();
            for(int i=1;i<content.length();i++){//ignore the first
                Record record  = new Record((content.getJSONObject(i)));
                records.add(record);
            }
        }catch(JSONException e){

            e.printStackTrace();
        }
		save();
		adapter.updateFinished(true);

	}

	protected void onFailed() {
		gpaDbModel.open();
		records = gpaDbModel.all();
        gpaDbModel.close();
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
		Map<String,Double> gradeMap = new HashMap<String,Double>();
		for(Record r:records) {
			if(r.isSelected()){
				if(gradeMap.containsKey(r.getName())){
					double grades = gradeMap.get(r.getName());
					if(r.getPoint()*r.getCredit()>grades) {
						gradeMap.put(r.getName(), r.getPoint()*r.getCredit());
					}
				}else{
					gradeMap.put(r.getName(), r.getPoint()*r.getCredit());
					totalCredit+=r.getCredit();
				}
			}
		}
		
		for(double r:gradeMap.values()){
			totalGrade += r;
		}
		if(totalCredit == 0) {
			throw new Exception("No course is selected");
		}
		return totalGrade / totalCredit;
	}
    public void update(Context context){
        APIClient client = APIFactory.getAPIClient(context,"api/gpa",new SuccessHandler() {
            @Override
            public void onSuccess(String data) {
                Message msg = handler.obtainMessage(SUCCESS, data);
                handler.sendMessage(msg);
            }
        },new FailHandler() {
            @Override
            public void onFail(Status err, String message) {
                handler.obtainMessage(FAILED).sendToTarget();
            }
        });
        client.requestWithoutCache();
    }

	public void update(final UserAccount user, final ProgressDialog progress) {
        final String URL = "http://herald.seu.edu.cn/herald_web_service/gpa/gpa";
		new Thread() {
			@Override
			public void run() {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(URL);

				try {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							2);
					nameValuePairs.add(new BasicNameValuePair("username", user
							.getUsername()));
					nameValuePairs.add(new BasicNameValuePair("password", user
							.getPassword()));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					// Execute HTTP Post Request
					HttpResponse response = httpclient.execute(httppost);
					if (response.getStatusLine().getStatusCode() == 200) {

                        HttpEntity entity = response.getEntity();
                        String result = EntityUtils.toString(entity);
						Message msg = handler.obtainMessage(SUCCESS, result);
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
