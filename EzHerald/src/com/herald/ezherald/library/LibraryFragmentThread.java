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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class LibraryFragmentThread extends Thread{
	
	Activity activity;
	String Book_Search_Value;
	Context context;
	JSONArray jsonarray;
	
	private MyHandle myHandler=new MyHandle();//��ʼ��Handler
	
	public LibraryFragmentThread(String search_value, Activity ac, Context cn){
		this.Book_Search_Value=search_value;
		this.activity=ac;
		this.context=cn;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			DefaultHttpClient client=new DefaultHttpClient();
//			List<NameValuePair> list=new ArrayList<NameValuePair>();
//			NameValuePair pair1=new BasicNameValuePair("strText",Book_Search_Value);
//			list.add(pair1);
//			NameValuePair pair2=new BasicNameValuePair("strText",Book_Search_Value);
//			list.add(pair2);
			
//			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,"UTF-8");
			String url_value="?strText="+Book_Search_Value+"&page="+"1";
			LibraryUrl url=new LibraryUrl();
			HttpGet get = new HttpGet(url.getLIBRARY_SEARCH_URL()+url_value);
			HttpResponse response=client.execute(get);

			InputStream isr=response.getEntity().getContent();
			BufferedReader br=new BufferedReader(new InputStreamReader(isr,"UTF-8"));
			
			String line=null;
			StringBuffer sb=new StringBuffer();
			while((line=br.readLine())!=null){
				sb.append(line);
			}
	       
			jsonarray=new JSONArray(sb.toString());
			
	        String[] BookName=new String[jsonarray.length()];
	        for(int i=0;i<jsonarray.length();i++){
			JSONObject json=jsonarray.getJSONObject(i);
			BookName[i] = json.getString("title");
		 	Log.d("id:",""+BookName[i]);
	        }
	        ShowMsg(jsonarray);
			
 		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void ShowMsg(JSONArray e){
		Message msg=Message.obtain();
		msg.obj=e;
		msg.setTarget(myHandler);
		msg.sendToTarget();
	}
	
	
	public class MyHandle extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			
 
			
			JSONArray json1=(JSONArray) msg.obj;
			ListView listview=(ListView)activity.findViewById(R.id.libr_search_listView);
			BookMyAdapter myAdapter=new BookMyAdapter(context,json1);
			listview.setAdapter(myAdapter);
			
			listview.setOnItemClickListener(new OnItemClickListener() {
				
				
				Bundle bundle=null;
				JSONObject json=null;
				String loc_name=null;
				String loc_author=null;
				String loc_press=null;
				//String loc_date=null;
				String loc_callNumber=null;
				String loc_documentType=null;
				String loc_marc_no=null;
				@Override
				public void onItemClick(AdapterView<?> arg0,
						View view, int position, long id) {
					
					try{
						json=jsonarray.getJSONObject(position);
						loc_name=json.getString("title");
						loc_author=json.getString("author");
						loc_press=json.getString("publisher");
						//loc_date=json.getString("book_date");
						loc_callNumber=json.getString("isbn");
						loc_documentType=json.getString("doctype");
						loc_marc_no=json.getString("marc_no");
					}catch(Exception e){
						
					}
					
					bundle=new Bundle();
					bundle.putString("loc_name", loc_name);
					bundle.putString("loc_author", loc_author);
					bundle.putString("loc_press", loc_press);
					//bundle.putString("loc_date", loc_date);
					bundle.putString("loc_callNumber", loc_callNumber);
					bundle.putString("loc_documentType", loc_documentType);
					bundle.putString("loc_marc_no",loc_marc_no);
					
					Intent intent=new Intent(activity,LibraryBookListDetail.class);
					
					intent.putExtras(bundle);
					activity.startActivity(intent);
				}
			});
		}
		
	}
	
	
/**************set BaseAdapter********************/
	
	public class BookMyAdapter extends BaseAdapter{
		
		private LayoutInflater inflater;
		JSONArray jsonarray;
		
		public BookMyAdapter(Context c,JSONArray ar){
			this.inflater=LayoutInflater.from(c);
			this.jsonarray=ar;
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
			if(convertView==null){
				convertView=inflater.inflate(R.layout.library_book_list_item, null);
				
				holder=new ViewHolder();
				
				holder.libr_name=(TextView) convertView.findViewById(R.id.libr_listitem_book_name);
				holder.libr_author=(TextView) convertView.findViewById(R.id.libr_listitem_book_author);
				holder.libr_press=(TextView) convertView.findViewById(R.id.libr_listitem_book_press);
				//holder.libr_date=(TextView) convertView.findViewById(R.id.libr_listitem_book_date);
				holder.libr_callNumber=(TextView) convertView.findViewById(R.id.libr_listitem_book_callNumber);
				holder.libr_ducumentType=(TextView) convertView.findViewById(R.id.libr_listitem_book_documentType);
				holder.libr_storenum=(TextView) convertView.findViewById(R.id.libr_listitem_book_store_num);
				holder.libr_landable_num=(TextView)convertView.findViewById(R.id.libr_listitem_book_landable_num);
				
				convertView.setTag(holder);//��ViewHolder����
			}
			else{
				holder=(ViewHolder)convertView.getTag();
			}
			
			String libr_name = null,libr_author = null,libr_press = null,libr_date=null,libr_callNumber=null,libr_ducumentType=null;
			String libr_marc_no=null;//�鿴�������
			String libr_store_num=null;
			String libr_landable_num=null;
			
			List<HashMap<String,String>> data=new ArrayList<HashMap<String,String>>();
			
			/******���ö�Ӧ�Ķ�̬��������*********/
			Log.d("jsonArray length():",jsonarray.length()+"");
			
			for(int i=0;i<jsonarray.length();i++){
			
			HashMap<String,String> map=new HashMap<String,String>();
			
			
			try {
				
				JSONObject obj= jsonarray.getJSONObject(i);
				libr_name = obj.getString("title");
				libr_author= obj.getString("author");
				libr_press = obj.getString("publisher");
				//libr_date = obj.getString("book_date");
				libr_callNumber = obj.getString("isbn");
				libr_ducumentType = obj.getString("doctype");
				libr_marc_no=obj.getString("marc_no");
				libr_store_num=obj.getString("store_num");
				libr_landable_num=obj.getString("lendable_num");
				
				map.put("libr_name", libr_name);
				map.put("libr_author", libr_author);
				map.put("libr_press", libr_press);
				//map.put("libr_date", libr_date);
				map.put("libr_callNumber", libr_callNumber);
				map.put("libr_ducumentType", libr_ducumentType);
				map.put("libr_marc_no", libr_marc_no);
				map.put("libr_store_num", libr_store_num);
				map.put("libr_landable_num",libr_landable_num);
				
				data.add(map);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			holder.libr_name.setText(position+1+".  "+data.get(position).get("libr_name").toString());
			holder.libr_author.setText(data.get(position).get("libr_author").toString());
			holder.libr_press.setText(data.get(position).get("libr_press").toString());
			//holder.libr_date.setText(data.get(position).get("libr_date").toString());
			holder.libr_callNumber.setText(data.get(position).get("libr_callNumber").toString());
			holder.libr_ducumentType.setText(data.get(position).get("libr_ducumentType").toString());
			holder.libr_storenum.setText("�ݲظ�����"+data.get(position).get("libr_store_num").toString());
			holder.libr_landable_num.setText("�ɽ踱����"+data.get(position).get("libr_landable_num").toString());
			return convertView;
		}
		
	}
	
	public class ViewHolder{
		public TextView libr_name;
		public TextView libr_author;
		public TextView libr_press;
		//public TextView libr_date;
		public TextView libr_callNumber;
		public TextView libr_ducumentType;
		public TextView libr_storenum;
		public TextView libr_landable_num;
		//public Button libr_button_reserve;
	}
	
	
	

}