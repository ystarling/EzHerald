package com.herald.ezherald.library;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.herald.ezherald.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class LibraryActivityNewsThread extends Thread {

	Activity activity;
	Context context;
	JSONArray jsonarray;
	private MyHandle myHandler = new MyHandle();// 初始化Handler

	public LibraryActivityNewsThread(Activity ac, Context cn) {
		this.activity = ac;
		this.context = cn;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			NameValuePair pair1 = new BasicNameValuePair("libr_news", "news");
			list.add(pair1);

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,
					"UTF-8");

			HttpPost post = new HttpPost("http://10.0.2.2:8080/EzHerald/abc");
			post.setEntity(entity);

			HttpResponse response = client.execute(post);
			InputStream isr = response.getEntity().getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(isr,
					"gbk"));

			String line = null;
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			jsonarray = new JSONArray(sb.toString());

			String[] BookName = new String[jsonarray.length()];
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject json = jsonarray.getJSONObject(i);
				BookName[i] = json.getString("book_name");
				Log.d("id:", "" + BookName[i]);
			}
			ShowMsg(jsonarray);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ShowMsg(JSONArray e) {
		Message msg = Message.obtain();
		msg.obj = e;
		msg.setTarget(myHandler);
		msg.sendToTarget();
	}

	public class MyHandle extends Handler {

		@Override
		public void handleMessage(Message msg) {

			JSONArray json1 = (JSONArray) msg.obj;
			ListView listview = (ListView) activity
					.findViewById(R.id.libr_news_list);
			NewsMyAdapter myAdapter = new NewsMyAdapter(context, json1);
			listview.setAdapter(myAdapter);

			listview.setOnItemClickListener(new OnItemClickListener() {

				Bundle bundle = null;
				JSONObject json = null;
				String loc_name = null;

				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int position, long id) {

					try {
						JSONObject json = jsonarray.getJSONObject(position);
						loc_name = json.getString("book_name");

					} catch (Exception e) {

					}

					bundle = new Bundle();
					bundle.putString("loc_name", loc_name);

					Intent intent = new Intent(activity,
							LibraryBookListDetail.class);

					intent.putExtras(bundle);
					activity.startActivity(intent);
				}
			});
		}

	}

	/************** set BaseAdapter ********************/

	public class NewsMyAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		JSONArray jsonarray;

		public NewsMyAdapter(Context c, JSONArray ar) {
			this.inflater = LayoutInflater.from(c);
			this.jsonarray = ar;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return jsonarray.length();
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.library_news_list_item,
						null);

				holder = new ViewHolder();

				holder.libr_news = (TextView) convertView
						.findViewById(R.id.libr_listitem_news_name);
				convertView.setTag(holder);// 绑定ViewHolder对象
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

			/****** 设置对应的动态数组数据 *********/
			Log.d("jsonArray length():", jsonarray.length() + "");

			for (int i = 0; i < jsonarray.length(); i++) {

				HashMap<String, String> map = new HashMap<String, String>();

				String news;

				try {

					JSONObject obj = jsonarray.getJSONObject(i);
					news = obj.getString("libr_news");

					map.put("libr_news", news);

					data.add(map);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			holder.libr_news.setText(data.get(position).get("libr_news")
					.toString());

			return convertView;
		}

	}

	public class ViewHolder {
		public TextView libr_news;
	}

}
