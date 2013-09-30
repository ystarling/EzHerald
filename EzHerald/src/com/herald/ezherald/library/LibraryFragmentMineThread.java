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
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.herald.ezherald.R;
import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.UserAccount;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LibraryFragmentMineThread extends Thread{
	
	private ProgressBar pro1;
	protected static final int STOP_NOTIFIER = 000;  
	protected static final int THREADING_NOTIFIER = 111;  
	 
	Activity activity;
	Context context;
	JSONArray jsonarray;
	private MyHandle myHandler=new MyHandle();//初始化Handler
	
	public LibraryFragmentMineThread( Activity ac, Context cn){
		this.activity=ac;
		this.context=cn;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{

			DefaultHttpClient client=new DefaultHttpClient();
			List<NameValuePair> list=new ArrayList<NameValuePair>();
			
			UserAccount LibrAccount = Authenticate.getIDcardUser(context);
			
			NameValuePair pair1=new BasicNameValuePair("username",LibrAccount.getUsername());
			list.add(pair1);
			
			NameValuePair pair2=new BasicNameValuePair("password",LibrAccount.getPassword());
			list.add(pair2);	
			
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,"UTF-8");
			
			
//			// 设置网络超时参数
//			HttpParams httpParams = client.getParams();
//			HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
//			HttpConnectionParams.setSoTimeout(httpParams, 5000);
			
			LibraryUrl url=new LibraryUrl();
			
			HttpPost post=new HttpPost(url.getLIBRARY_MINE_BOOKS());
			post.setEntity(entity);
			
			HttpResponse response=client.execute(post);

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
		 	Log.d("书名:",""+BookName[i]);
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
			pro1=(ProgressBar)activity.findViewById(R.id.libr_circleProgressBar);
			pro1.setIndeterminate(false);
			pro1.setVisibility(View.VISIBLE);  

			JSONArray json1=(JSONArray) msg.obj;
			ListView listview=(ListView)activity.findViewById(R.id.libr_mine_list);
			MineBookMyAdapter myAdapter=new MineBookMyAdapter(context,json1);
			listview.setAdapter(myAdapter);
			pro1.setVisibility(View.GONE); 
//			listview.setOnItemClickListener(new OnItemClickListener() {
//				
//				
//				Bundle bundle=null;
//				JSONObject json=null;
//				String loc_name=null;
//				String loc_author=null;
//				String loc_press=null;
//				//String loc_date=null;
//				String loc_callNumber=null;
//				String loc_documentType=null;
//				String loc_marc_no=null;
//				@Override
//				public void onItemClick(AdapterView<?> arg0,
//						View view, int position, long id) {
//					
//					try{
//						json=jsonarray.getJSONObject(position);
//						loc_name=json.getString("title");
//						loc_author=json.getString("author");
//						loc_press=json.getString("publisher");
//						//loc_date=json.getString("book_date");
//						loc_callNumber=json.getString("isbn");
//						loc_documentType=json.getString("doctype");
//						loc_marc_no=json.getString("marc_no");
//					}catch(Exception e){
//						
//					}
//					
//					bundle=new Bundle();
//					bundle.putString("loc_name", loc_name);
//					bundle.putString("loc_author", loc_author);
//					bundle.putString("loc_press", loc_press);
//					//bundle.putString("loc_date", loc_date);
//					bundle.putString("loc_callNumber", loc_callNumber);
//					bundle.putString("loc_documentType", loc_documentType);
//					bundle.putString("loc_marc_no",loc_marc_no);
//					
//					Intent intent=new Intent(activity,LibraryBookListDetail.class);
//					
//					intent.putExtras(bundle);
//					activity.startActivity(intent);
//				}
//			});
		}
		
	}
	
	
/**************set BaseAdapter********************/
	
	public class MineBookMyAdapter extends BaseAdapter{
		
		private LayoutInflater inflater;
		JSONArray jsonarray;
		
		public MineBookMyAdapter(Context c,JSONArray ar){
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
				convertView=inflater.inflate(R.layout.library_mine_list_item, null);
				
				holder=new ViewHolder();
				
				holder.barCode=(TextView) convertView.findViewById(R.id.libr_mine_barcode);
				holder.title=(TextView) convertView.findViewById(R.id.libr_mine_title);
				holder.author=(TextView) convertView.findViewById(R.id.libr_mine_author);
				holder.borrow_date=(TextView) convertView.findViewById(R.id.libr_mine_borrow_date);
				holder.remand_date=(TextView) convertView.findViewById(R.id.libr_mine_remand_date);
				holder.renew_num=(TextView) convertView.findViewById(R.id.libr_mine_renew_num);
				holder.collection=(TextView) convertView.findViewById(R.id.libr_mine_collection);
				holder.attachment=(TextView)convertView.findViewById(R.id.libr_mine_attachment);
				
				convertView.setTag(holder);//绑定ViewHolder对象
			}
			else{
				holder=(ViewHolder)convertView.getTag();
			}
			
			String libr_barcode = null,libr_title = null,libr_author = null,libr_borrow_date=null,libr_remand_date=null,libr_renew_num=null;
			String libr_marc_no=null;//查看详情必填
			String libr_collection=null;
			String libr_attachment=null;
			
			List<HashMap<String,String>> data=new ArrayList<HashMap<String,String>>();
			
			/******设置对应的动态数组数据*********/
			Log.d("jsonArray length():",jsonarray.length()+"");
			
			for(int i=0;i<jsonarray.length();i++){
			
			HashMap<String,String> map=new HashMap<String,String>();
			
			
			try {
				JSONObject obj= jsonarray.getJSONObject(i);
				
				libr_barcode = obj.getString("barcode");
				libr_title=obj.getString("title");
				libr_author= obj.getString("author");
				libr_borrow_date = obj.getString("render_date");
				libr_remand_date = obj.getString("due_date");
				libr_renew_num=obj.getString("renew_time");
				libr_collection=obj.getString("place");
				libr_attachment=obj.getString("adjunct");
				
				map.put("libr_barcode", libr_barcode);
				map.put("libr_title", libr_title);
				map.put("libr_author", libr_author);
				map.put("libr_borrow_date", libr_borrow_date);
				map.put("libr_remand_date", libr_remand_date);
				map.put("libr_renew_num", libr_renew_num);
				map.put("libr_collection", libr_collection);
				map.put("libr_attachment",libr_attachment);
				
				data.add(map);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			holder.barCode.setText(position+1+".  条形码："+data.get(position).get("libr_barcode").toString());
			holder.title.setText("题名："+data.get(position).get("libr_title").toString());
			holder.author.setText("责任者："+data.get(position).get("libr_author").toString());
			holder.borrow_date.setText("借阅日期："+data.get(position).get("libr_borrow_date").toString());
			holder.remand_date.setText("应还日期："+data.get(position).get("libr_remand_date").toString());
			holder.renew_num.setText("续借次数："+data.get(position).get("libr_renew_num").toString());
			holder.collection.setText("馆藏地："+data.get(position).get("libr_collection").toString());
			holder.attachment.setText("附件："+data.get(position).get("libr_attachment").toString());
			
			return convertView;
		}
		
	}
	
	public class ViewHolder{
		public TextView barCode;
		public TextView title;
		public TextView author;
		public TextView borrow_date;
		public TextView remand_date;
		public TextView renew_num;
		public TextView collection;
		public TextView attachment;
		public Button renew_btn;
	}
	
	

}
