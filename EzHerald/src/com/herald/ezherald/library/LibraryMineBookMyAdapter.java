package com.herald.ezherald.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.herald.ezherald.R;

///**************set BaseAdapter********************/
//
//public class LibraryMineBookMyAdapter extends BaseAdapter{
//	
//	private LayoutInflater inflater;
//	JSONArray jsonarray;
//	Context context;
//	public String barcode;
//	Activity activity;
//	public LibraryMineBookMyAdapter(Activity activity,Context c,JSONArray ar){
//		this.inflater=LayoutInflater.from(c);
//		this.jsonarray=ar;
//		this.context=c;
//		this.activity=activity;
//	}
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return jsonarray.length();
//	}
//	
//	@Override
//	public boolean isEnabled(int position) {
//		// TODO Auto-generated method stub
//		return true;
//	}
//	@Override
//	public Object getItem(int position) {
//		// TODO Auto-generated method stub
//		return position;
//	}
//
//	@Override
//	public long getItemId(int position) {
//		// TODO Auto-generated method stub
//		return position;
//	}
//	
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		
//		ViewHolder holder;
//		if(convertView==null){
//			convertView=inflater.inflate(R.layout.library_mine_list_item, null);
//			
//			holder=new ViewHolder();
//			
//			holder.barCode=(TextView) convertView.findViewById(R.id.libr_mine_barcode);
//			holder.title=(TextView) convertView.findViewById(R.id.libr_mine_title);
//			holder.author=(TextView) convertView.findViewById(R.id.libr_mine_author);
//			holder.borrow_date=(TextView) convertView.findViewById(R.id.libr_mine_borrow_date);
//			holder.remand_date=(TextView) convertView.findViewById(R.id.libr_mine_remand_date);
//			holder.renew_num=(TextView) convertView.findViewById(R.id.libr_mine_renew_num);
//			holder.collection=(TextView) convertView.findViewById(R.id.libr_mine_collection);
//			holder.attachment=(TextView)convertView.findViewById(R.id.libr_mine_attachment);
//			holder.renew_btn=(Button)convertView.findViewById(R.id.libr_mine_renew_btn);
//			convertView.setTag(holder);//绑定ViewHolder对象
//		}
//		else{
//			holder=(ViewHolder)convertView.getTag();
//			}
//		
//		String libr_barcode = null,libr_title = null,libr_author = null,libr_borrow_date=null,libr_remand_date=null,libr_renew_num=null;
//		String libr_marc_no=null;//查看详情必填
//		String libr_collection=null;
//		String libr_attachment=null;
//		
//		
//		List<HashMap<String,String>> data=new ArrayList<HashMap<String,String>>();
//		
//		/******设置对应的动态数组数据*********/
//		Log.d("jsonArray length():",jsonarray.length()+"");
//		
//		for(int i=0;i<jsonarray.length();i++){
//		
//		HashMap<String,String> map=new HashMap<String,String>();
//		
//		
//		try {
//			JSONObject obj= jsonarray.getJSONObject(i);
//			
//			libr_barcode = obj.getString("barcode");
//			libr_title=obj.getString("title");
//			libr_author= obj.getString("author");
//			libr_borrow_date = obj.getString("render_date");
//			libr_remand_date = obj.getString("due_date");
//			libr_renew_num=obj.getString("renew_time");
//			libr_collection=obj.getString("place");
//			libr_attachment=obj.getString("adjunct");
//			
//			map.put("libr_barcode", libr_barcode);
//			map.put("libr_title", libr_title);
//			map.put("libr_author", libr_author);
//			map.put("libr_borrow_date", libr_borrow_date);
//			map.put("libr_remand_date", libr_remand_date);
//			map.put("libr_renew_num", libr_renew_num);
//			map.put("libr_collection", libr_collection);
//			map.put("libr_attachment",libr_attachment);
//			
//			data.add(map);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//		holder.barCode.setText(position+1+".  条形码："+data.get(position).get("libr_barcode").toString());
//		barcode=data.get(position).get("libr_barcode").toString();
//		holder.title.setText("题名："+data.get(position).get("libr_title").toString());
//		holder.author.setText("责任者："+data.get(position).get("libr_author").toString());
//		holder.borrow_date.setText("借阅日期："+data.get(position).get("libr_borrow_date").toString());
//		holder.remand_date.setText("应还日期："+data.get(position).get("libr_remand_date").toString());
//		holder.renew_num.setText("续借次数："+data.get(position).get("libr_renew_num").toString());
//		holder.collection.setText("馆藏地："+data.get(position).get("libr_collection").toString());
//		holder.attachment.setText("附件："+data.get(position).get("libr_attachment").toString());
//		holder.renew_btn.setText("续借");
//		
//		//ShareSaved(data.get(0).get("libr_borrow_date").toString());//存储
//		holder.renew_btn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				LibraryFragmentMineRenewThread th=new LibraryFragmentMineRenewThread(activity, context,barcode);
//				th.start();
//			}
//		});
//		
//		return convertView;
//	}
//
//
//	public class ViewHolder{
//		public TextView barCode;
//		public TextView title;
//		public TextView author;
//		public TextView borrow_date;
//		public TextView remand_date;
//		public TextView renew_num;
//		public TextView collection;
//		public TextView attachment;
//		public Button renew_btn;
//	}
//
//}