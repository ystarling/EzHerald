package com.herald.ezherald.library;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import android.content.Context;
import android.util.Log;

import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.UserAccount;
import com.herald.ezherald.mainframe.MainContentGridItemObj;
import com.herald.ezherald.mainframe.MainContentInfoGrabber;

public class LibraryContentGrabber implements MainContentInfoGrabber {

	public String content1="图书馆服务异常";
	public String content2="";
	Context context = null;
	private JSONArray jsonarray;
	public int BookCount;
	public LibraryContentGrabber(Context cn) {
		this.context = cn;
	}

	@Override
	public MainContentGridItemObj GrabInformationObject() {
		// TODO Auto-generated method stub
		return provide();
	}

	public MainContentGridItemObj provide() {
		try {
			HttpResponse response = null;
			UserAccount LibrAccount = Authenticate.getLibUser(context);
			if (LibrAccount == null) {
				content1 = "用户未登录图书馆模块";
				Log.e("Nologin", "1111");
			} else {
				try {
					DefaultHttpClient client = new DefaultHttpClient();
					List<NameValuePair> list = new ArrayList<NameValuePair>();
					Log.d("context", context + "");

					NameValuePair pair1 = new BasicNameValuePair("username",
							LibrAccount.getUsername());
					list.add(pair1);
					NameValuePair pair2 = new BasicNameValuePair("password",
							LibrAccount.getPassword());
					list.add(pair2);

					// NameValuePair pair1=new
					// BasicNameValuePair("username","71111229");
					// list.add(pair1);
					// NameValuePair pair2=new
					// BasicNameValuePair("password","213113709");
					// list.add(pair2);

					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
							list, "UTF-8");

					// 设置网络超时参数
					HttpParams httpParams = client.getParams();
					HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
					HttpConnectionParams.setSoTimeout(httpParams, 5000);

					LibraryUrl url = new LibraryUrl();
					HttpPost post = new HttpPost(url.getLIBRARY_MINE_BOOKS());
					post.setEntity(entity);

					response = client.execute(post);

					InputStream isr = response.getEntity().getContent();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(isr, "UTF-8"));

					String line = null;
					StringBuffer sb = new StringBuffer();
					while ((line = br.readLine()) != null) {
						sb.append(line);
					}

					jsonarray = new JSONArray(sb.toString());
					int count = 0;
					if (jsonarray.isNull(0)) {
						content1 = "还没有借书";
					} else {
						for(int i=0;i<jsonarray.length();i++){
							String date1=jsonarray.getJSONObject(0).getString("due_date").toString();
							String date2=jsonarray.getJSONObject(i).getString("due_date").toString();
							Log.e("content1", date1);
							Log.e("content"+i, date2);
							if(date1.equals(date2)){
								count++;
								Log.e("count", count+"");
							}
						}
						content1 = "最近需要归还日期："
								+ jsonarray.getJSONObject(0)
										.getString("due_date").toString();
						content2=jsonarray.getJSONObject(0).getString("title").toString()+" 等"+count+"本书";
					}

					// }

				} catch (Exception ex) {
					Log.d("Networking", ex.getMessage());
					if (!ex.getMessage().isEmpty()) {

						content1 = "网络异常";
					}
				}
			}

			// String[] BookName = new String[jsonarray.length()];
			// for (int i = 0; i < jsonarray.length(); i++) {
			// JSONObject json = jsonarray.getJSONObject(i);
			// BookName[i] = json.getString("title");
			// Log.d("书名:", "" + BookName[i]);
			// }

		} catch (Exception ex) {
			 Log.d("LibraryMineRemandThread:", ex.getMessage());
		}
		
		MainContentGridItemObj item = new MainContentGridItemObj();
		item.setContent1(content1);

		item.setContent2(content2);
		return item;
	}

	// public class LibraryMineRemandThread extends Thread{
	// private Context context;
	// private JSONArray jsonarray;
	//
	// public LibraryMineRemandThread(Context cn){
	// this.context=cn;
	// }
	// @Override
	// public void run() {
	// // TODO
	// try{
	// HttpResponse response=null;
	// try{
	// DefaultHttpClient client=new DefaultHttpClient();
	// List<NameValuePair> list=new ArrayList<NameValuePair>();
	//
	// UserAccount LibrAccount = Authenticate.getLibUser(context);
	// NameValuePair pair1=new
	// BasicNameValuePair("username",LibrAccount.getUsername());
	// list.add(pair1);
	// NameValuePair pair2=new
	// BasicNameValuePair("password",LibrAccount.getPassword());
	// list.add(pair2);
	//
	// //NameValuePair pair1=new BasicNameValuePair("username","71111229");
	// //list.add(pair1);
	// //NameValuePair pair2=new BasicNameValuePair("password","213113709");
	// //list.add(pair2);
	//
	// UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,"UTF-8");
	//
	//
	// // 设置网络超时参数
	// HttpParams httpParams = client.getParams();
	// HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
	// HttpConnectionParams.setSoTimeout(httpParams, 5000);
	//
	// LibraryUrl url=new LibraryUrl();
	//
	// HttpPost post=new HttpPost(url.getLIBRARY_MINE_BOOKS());
	// post.setEntity(entity);
	//
	// response=client.execute(post);
	//
	// }catch(Exception ex){
	// Log.d("Networking",ex.getMessage());
	// if(!ex.getMessage().isEmpty()){
	// content1="网络异常";
	// }
	// }
	// InputStream isr=response.getEntity().getContent();
	// BufferedReader br=new BufferedReader(new InputStreamReader(isr,"UTF-8"));
	//
	// String line=null;
	// StringBuffer sb=new StringBuffer();
	// while((line=br.readLine())!=null){
	// sb.append(line);
	// }
	//
	// jsonarray=new JSONArray(sb.toString());
	// if(jsonarray.isNull(0)){
	// content1="还没有借书";
	// }else{
	// content1="最近需要归还"+jsonarray.getJSONObject(0).getString("libr_remand_date").toString();
	// }
	//
	// String[] BookName=new String[jsonarray.length()];
	// for(int i=0;i<jsonarray.length();i++){
	// JSONObject json=jsonarray.getJSONObject(i);
	// BookName[i] = json.getString("title");
	// Log.d("书名:",""+BookName[i]);
	// }
	//
	// }catch(Exception ex){
	// Log.d("LibraryMineRemandThread:",ex.getMessage());
	// }
	//
	// }
	//
	// }
}
