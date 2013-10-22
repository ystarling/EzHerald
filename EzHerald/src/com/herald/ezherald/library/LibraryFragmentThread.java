package com.herald.ezherald.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.herald.ezherald.R;
import com.herald.ezherald.library.LibraryBookListDetailThread.MyHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class LibraryFragmentThread extends Thread{

	private ProgressDialog dialog1;
	Activity activity;
	String Book_Search_Value;
	Context context;
	JSONArray jsonarray;
	ListView listview;
	int CountOfScroll;
	boolean isLastRow = false;
	
	private MyHandle myHandler = new MyHandle();// 初始化Handler
	private MyHandle2 myHandler2 = new MyHandle2();

	public LibraryFragmentThread(String search_value, Activity ac, Context cn,ListView listview,int num) {
		this.Book_Search_Value = search_value;
		this.activity = ac;
		this.context = cn;
		this.CountOfScroll=num;
		this.listview=listview;
		dialog1 = new ProgressDialog(context);
		dialog1.setCanceledOnTouchOutside(false);
		dialog1.setMessage("加载中...");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ShowMsg2("ProgressDialog");
			HttpResponse response = null;
			try {
				DefaultHttpClient client = new DefaultHttpClient();
				// List<NameValuePair> list=new ArrayList<NameValuePair>();
				// NameValuePair pair1=new
				// BasicNameValuePair("strText",Book_Search_Value);
				// list.add(pair1);
				// NameValuePair pair2=new
				// BasicNameValuePair("strText",Book_Search_Value);
				// list.add(pair2);

				// UrlEncodedFormEntity entity = new
				// UrlEncodedFormEntity(list,"UTF-8");
				String url_value = "?strText=" + Book_Search_Value + "&page="
						+ CountOfScroll;
				LibraryUrl url = new LibraryUrl();
				HttpGet get;

				get = new HttpGet(url.getLIBRARY_SEARCH_URL() + url_value);
				response = client.execute(get);

			} catch (Exception ex) {
				Log.d("Notwoking", ex.getLocalizedMessage());
				if (!ex.getLocalizedMessage().isEmpty()) {
					ShowMsg2("NetWorkingError");
				}
				throw new IOException("Error connecting");
			}

			// HttpConnectionParams.setSoTimeout(httpParams, 5000);

			InputStream isr = response.getEntity().getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(isr,
					"UTF-8"));

			String line = null;
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			
			jsonarray = new JSONArray(sb.toString());
			
			String[] BookName = new String[jsonarray.length()];
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject json = jsonarray.getJSONObject(i);
				BookName[i] = json.getString("title");
				Log.d("id:", "" + BookName[i]);
			}
			ShowMsg(jsonarray);

		} catch (Exception e) {
			Log.e("Error",e.getMessage());
			ShowMsg2("ValueError");
		}
	}

	public void ShowMsg(JSONArray e) {
		Message msg = Message.obtain();
		msg.obj = e;
		msg.setTarget(myHandler);
		msg.sendToTarget();
	}

	public void ShowMsg2(String e) {
		Message msg = Message.obtain();
		msg.obj = e;
		msg.setTarget(myHandler2);
		msg.sendToTarget();
	}

	public class MyHandle2 extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			String va = (String) msg.obj;

			if (va == "ProgressDialog") {
				dialog1.show();
			}
			if(va=="NetWorkingError"){
				Toast toast1 = Toast.makeText(activity, "网络请求错误...",
						Toast.LENGTH_LONG);
				toast1.show();
				dialog1.cancel();
			}
			if(va=="ValueError"){
				Toast toast1 = Toast.makeText(activity, "网络请求错误...",
						Toast.LENGTH_LONG);
				toast1.show();
				dialog1.cancel();
			}
			super.handleMessage(msg);
		}

	}

	public class MyHandle extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

//			ListView listview = (ListView) activity
//					.findViewById(R.id.libr_search_listView);
//			
//			//View loadview=(View) activity.findViewById(R.layout.library_fragment_loadmore);
//			//listview.addFooterView(loadview);
//			
//			listview.setOnScrollListener(new OnScrollListener() {
//				
//				@Override
//				public void onScrollStateChanged(AbsListView view, int scrollState) {
//					// TODO Auto-generated method stub
//				        //当滚到最后一行且停止滚动时，执行加载
//					       if (isLastRow && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
//					            //加载元素
//								Toast toast1 = Toast.makeText(activity,"执行加载",Toast.LENGTH_SHORT);
//								toast1.show();
//					
//					           isLastRow = false;
//					        }
//				}
//				
//				@Override
//				public void onScroll(AbsListView view, int firstVisibleItem,
//						int visibleItemCount, int totalItemCount) {
//					// TODO Auto-generated method stub
//					
//					if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
//						  isLastRow = true;
//					 }
//					
//				}
//			});
			
			JSONArray json1 = (JSONArray) msg.obj;
			try {

				if (json1.isNull(0)) {
					SetRemind();
					dialog1.cancel();
					Log.e("无内容", "无内容");
				} else {

					BookMyAdapter myAdapter = new BookMyAdapter(context,CountOfScroll);
					myAdapter.jsonarray=json1;
					myAdapter.mycontext=context;
					listview.setAdapter(myAdapter);
					if (dialog1.isShowing()) {
						dialog1.cancel();
					}
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			listview.setOnItemClickListener(new OnItemClickListener() {

				Bundle bundle = null;
				JSONObject json = null;
				String loc_name = null;
				String loc_author = null;
				String loc_press = null;
				// String loc_date=null;
				String loc_callNumber = null;
				String loc_documentType = null;
				String loc_marc_no = null;

				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int position, long id) {

					try {
						json = jsonarray.getJSONObject(position);
						loc_name = json.getString("title");
						loc_author = json.getString("author");
						loc_press = json.getString("publisher");
						// loc_date=json.getString("book_date");
						loc_callNumber = json.getString("isbn");
						loc_documentType = json.getString("doctype");
						loc_marc_no = json.getString("marc_no");
					} catch (Exception e) {

					}

					bundle = new Bundle();
					bundle.putString("loc_name", loc_name);
					bundle.putString("loc_author", loc_author);
					bundle.putString("loc_press", loc_press);
					// bundle.putString("loc_date", loc_date);
					bundle.putString("loc_callNumber", loc_callNumber);
					bundle.putString("loc_documentType", loc_documentType);
					bundle.putString("loc_marc_no", loc_marc_no);

					Intent intent = new Intent(activity,
							LibraryBookListDetail.class);

					intent.putExtras(bundle);
					activity.startActivity(intent);
				}
			});
		}

		public void SetRemind() {
			Toast toast1 = Toast.makeText(activity, "该图书不存在!",
					Toast.LENGTH_SHORT);
			toast1.show();
		}

	}

//	/************** set BaseAdapter ********************/
//
//	public class BookMyAdapter extends BaseAdapter {
//
//		private LayoutInflater inflater;
//		JSONArray jsonarray;
//		Context mycontext;
//		public BookMyAdapter(Context c) {
//			this.inflater = LayoutInflater.from(c);
//			//this.jsonarray = ar;
//		}
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return jsonarray.length();
//		}
//
//		@Override
//		public boolean isEnabled(int position) {
//			// TODO Auto-generated method stub
//			return true;
//		}
//
//		@Override
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return position;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//
//			ViewHolder holder;
//			if (convertView == null) {
//				convertView = inflater.inflate(R.layout.library_book_list_item,
//						null);
//
//				holder = new ViewHolder();
//
//				holder.libr_name = (TextView) convertView
//						.findViewById(R.id.libr_listitem_book_name);
//				holder.libr_author = (TextView) convertView
//						.findViewById(R.id.libr_listitem_book_author);
//				holder.libr_press = (TextView) convertView
//						.findViewById(R.id.libr_listitem_book_press);
//				// holder.libr_date=(TextView)
//				// convertView.findViewById(R.id.libr_listitem_book_date);
//				holder.libr_callNumber = (TextView) convertView
//						.findViewById(R.id.libr_listitem_book_callNumber);
//				holder.libr_ducumentType = (TextView) convertView
//						.findViewById(R.id.libr_listitem_book_documentType);
//				holder.libr_storenum = (TextView) convertView
//						.findViewById(R.id.libr_listitem_book_store_num);
//				holder.libr_landable_num = (TextView) convertView
//						.findViewById(R.id.libr_listitem_book_landable_num);
//
//				convertView.setTag(holder);// 绑定ViewHolder对象
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//
//			String libr_name = null, libr_author = null, libr_press = null, libr_date = null, libr_callNumber = null, libr_ducumentType = null;
//			String libr_marc_no = null;// 查看详情必填
//			String libr_store_num = null;
//			String libr_landable_num = null;
//
//			List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
//
//			/****** 设置对应的动态数组数据 *********/
//			Log.d("jsonArray length():", jsonarray.length() + "");
//
//			for (int i = 0; i < jsonarray.length(); i++) {
//
//				HashMap<String, String> map = new HashMap<String, String>();
//
//				try {
//
//					JSONObject obj = jsonarray.getJSONObject(i);
//					libr_name = obj.getString("title");
//					libr_author = obj.getString("author");
//					libr_press = obj.getString("publisher");
//					// libr_date = obj.getString("book_date");
//					libr_callNumber = obj.getString("isbn");
//					libr_ducumentType = obj.getString("doctype");
//					libr_marc_no = obj.getString("marc_no");
//					libr_store_num = obj.getString("store_num");
//					libr_landable_num = obj.getString("lendable_num");
//
//					map.put("libr_name", libr_name);
//					map.put("libr_author", libr_author);
//					map.put("libr_press", libr_press);
//					// map.put("libr_date", libr_date);
//					map.put("libr_callNumber", libr_callNumber);
//					map.put("libr_ducumentType", libr_ducumentType);
//					map.put("libr_marc_no", libr_marc_no);
//					map.put("libr_store_num", libr_store_num);
//					map.put("libr_landable_num", libr_landable_num);
//
//					data.add(map);
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			int toalnum=20*CountOfScroll+position;
//			holder.libr_name.setText(toalnum + 1 + ".  "
//					+ data.get(position).get("libr_name").toString());
//			holder.libr_author.setText(data.get(position).get("libr_author")
//					.toString());
//			holder.libr_press.setText(data.get(position).get("libr_press")
//					.toString());
//			// holder.libr_date.setText(data.get(position).get("libr_date").toString());
//			holder.libr_callNumber.setText(data.get(position)
//					.get("libr_callNumber").toString());
//			holder.libr_ducumentType.setText(data.get(position)
//					.get("libr_ducumentType").toString());
//			holder.libr_storenum.setText("馆藏副本："
//					+ data.get(position).get("libr_store_num").toString());
//			holder.libr_landable_num.setText("可借副本："
//					+ data.get(position).get("libr_landable_num").toString());
//			
//			return convertView;
//			
//		}
//
//	}
//
//	public class ViewHolder {
//		public TextView libr_name;
//		public TextView libr_author;
//		public TextView libr_press;
//		// public TextView libr_date;
//		public TextView libr_callNumber;
//		public TextView libr_ducumentType;
//		public TextView libr_storenum;
//		public TextView libr_landable_num;
//		// public Button libr_button_reserve;
//	}

}
