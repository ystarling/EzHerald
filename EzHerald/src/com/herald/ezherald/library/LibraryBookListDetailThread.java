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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.herald.ezherald.R;
import com.herald.ezherald.library.LibraryActivityReserve.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LibraryBookListDetailThread extends Thread{

	Bundle bundle;
	JSONObject json;
	JSONObject json_detail;
	Activity activity; 
	Context context;
	MyHandler myHandler= new MyHandler();
	
	ProgressBar pro1;
	public LibraryBookListDetailThread(Bundle bundle,Activity ac, Context cn) {
		
		this.bundle=bundle;
		this.activity=ac;
		this.context=cn;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			DefaultHttpClient client=new DefaultHttpClient();
//			List<NameValuePair> list = new ArrayList<NameValuePair>();
//			NameValuePair pair1=new BasicNameValuePair("book_callNumber",bundle.getString("loc_callNumber"));
//			list.add(pair1);
//			UrlEncodedFormEntity entity=new UrlEncodedFormEntity(list,"UTF-8");
//			
//			HttpPost post=new HttpPost("");
//			post.setEntity(entity);
			
			LibraryUrl url=new LibraryUrl();
			String url_vaue="?marc_no="+bundle.getString("loc_marc_no");
			Log.d("查书号：",bundle.getString("loc_marc_no"));
			HttpGet get=new HttpGet(url.getLIBRARY_SEARCH_DETAIL()+url_vaue);
			HttpResponse response=client.execute(get);
			InputStream is=response.getEntity().getContent();
			BufferedReader br=new BufferedReader(new InputStreamReader(is,"UTF-8"));
			
			String line=null;
			StringBuffer sb=new StringBuffer();
			while((line=br.readLine()) != null){
				sb.append(line);
			}
			json=new JSONObject(sb.toString());
			
			ShowMsg(json);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void ShowMsg(JSONObject json){
		Message msg=Message.obtain();
		msg.obj=json;
		msg.setTarget(myHandler);
		msg.sendToTarget();
	}
	
	class MyHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			JSONObject json1=(JSONObject) msg.obj;
			String author=null,press=null, price = null;
			try {
					json_detail=json1.getJSONObject("detail");
					author=json_detail.getString("题名/责任者:").toString();
					press=json_detail.getString("出版发行项:").toString();
					price=json_detail.getString("ISBN及定价:").toString();
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			pro1=(ProgressBar)activity.findViewById(R.id.libr_circleBookProgressBar);
			pro1.setIndeterminate(false);
			pro1.setVisibility(View.VISIBLE); 
			
			TextView textView1=(TextView)activity.findViewById(R.id.libr_book_listdetailA_name);
			textView1.setText(author);
			
			TextView textView2=(TextView)activity.findViewById(R.id.libr_book_listdetailA_press);
			textView2.setText(press);
			
			TextView textView3=(TextView)activity.findViewById(R.id.libr_book_listdetailA_prize);
			textView3.setText(price);
			
			ListView listview=(ListView) activity.findViewById(R.id.libr_book_listViewB);
			bookDetailAdapter adapter = null;
			try {
				adapter = new bookDetailAdapter(context,json1);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			listview.setAdapter(adapter);
			
			pro1.setVisibility(View.GONE); 
			
			
		}
		
	}
	
	public class bookDetailAdapter extends BaseAdapter{
		
		LayoutInflater inflater;
		JSONArray jsonarray;
		public bookDetailAdapter(Context c,JSONObject mjson) throws JSONException {
				
			this.inflater=LayoutInflater.from(c);
			this.jsonarray=mjson.getJSONArray("stores");
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return jsonarray.length();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(convertView==null){
				convertView=inflater.inflate(R.layout.library_book_listview_b, null);
				holder=new ViewHolder();
				
				holder.text1=(TextView) convertView.findViewById(R.id.libr_book_suoshuhao);
				holder.text2=(TextView) convertView.findViewById(R.id.libr_book_barcode);
				holder.text3=(TextView) convertView.findViewById(R.id.libr_book_year_title);
				holder.text4=(TextView) convertView.findViewById(R.id.libr_book_campus);
				holder.text5=(TextView) convertView.findViewById(R.id.libr_book_collection);
				holder.text6=(TextView) convertView.findViewById(R.id.libr_book_status);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			
			List<HashMap<String,String>> data=new ArrayList<HashMap<String,String>>();
			
			/******设置对应的动态数组数据*********/
			Log.d("jsonArray length():",jsonarray.length()+"");
			
			for(int i=0;i<jsonarray.length();i++){
			
			HashMap<String,String> map=new HashMap<String,String>();
			String libr_callNumber = null,libr_barcode = null,libr_year = null,libr_campus=null,libr_collection=null,libr_bookStatus=null;
			
			try {
				
				JSONObject obj= jsonarray.getJSONObject(i);
				libr_callNumber = obj.getString("call_no");
				libr_barcode= obj.getString("barcode");
				libr_year = obj.getString("years");
				libr_campus = obj.getString("campus");
				libr_collection = obj.getString("room");
				libr_bookStatus = obj.getString("lendable");
				
				map.put("libr_callNumber", libr_callNumber);
				map.put("libr_barcode", libr_barcode);
				map.put("libr_year", libr_year);
				map.put("libr_campus", libr_campus);
				map.put("libr_collection", libr_collection);
				map.put("libr_bookStatus", libr_bookStatus);
				
				data.add(map);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			
			holder.text1.setText("索书号: "+data.get(position).get("libr_callNumber").toString());
			holder.text2.setText("条形码："+data.get(position).get("libr_barcode").toString());
			holder.text3.setText("年卷期："+data.get(position).get("libr_year").toString());
			holder.text4.setText("校区： "+data.get(position).get("libr_campus").toString());
			holder.text5.setText("馆藏地： "+data.get(position).get("libr_collection").toString());
			holder.text6.setText("书刊状态："+data.get(position).get("libr_bookStatus").toString());
			
			return convertView;
		}
		
	}
	
	public class ViewHolder{
		public TextView text1;
		public TextView text2;
		public TextView text3;
		public TextView text4;
		public TextView text5;
		public TextView text6;
	}
}
