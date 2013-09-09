package com.herald.ezherald.library;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.herald.ezherald.R;

public class LibraryBookListDetail extends SherlockActivity{
	
	
	ArrayList<Map<String, Object>> arraylist=new ArrayList<Map<String, Object>>();
	protected String[] string;;
	protected int[] libr_int;
	
	ListView libr_listView;
	SimpleAdapter libr_adapter;
	
	ListView libr_listView2;
	LinearLayout myLinearLayout;
	View view;
	public void onCreate(Bundle saveInstanceSate){
		super.onCreate(saveInstanceSate);
		setContentView(R.layout.library_activity_book);
		setTitle("搜索结果");
		
		TextView textView1=(TextView)findViewById(R.id.libr_book_listdetailA_name);
		textView1.setText("姜戎");
		
		TextView textView2=(TextView)findViewById(R.id.libr_book_listdetailA_press);
		textView2.setText("东南大学出版社");
		
		TextView textView3=(TextView)findViewById(R.id.libr_book_listdetailA_prize);
		textView3.setText("2000");
		
		
		libr_listView2=(ListView) findViewById(R.id.libr_book_listViewB);
		bookAdapter mAdapter=new bookAdapter(this);
		libr_listView2.setAdapter(mAdapter);
		
		
		
//		/************ create GridView about library some information **********/
//		
//		GridView libr_gridview=(GridView) findViewById(R.id.libr_book_grid);
//		ArrayList<HashMap<String, Object>> libr_gridItem=new ArrayList<HashMap<String,Object>>();
//		for(int i=0;i<16;i++){
//			HashMap<String, Object> map=new HashMap<String, Object>();
//			map.put("ItemText","NO"+String.valueOf(i));
//			libr_gridItem.add(map);
//		}
//		SimpleAdapter simpleItem=new SimpleAdapter(this,
//				libr_gridItem,
//				R.layout.library_book_grid1,
//				new String[]{"ItemText"},
//				new int[]{R.id.libr_book_listdetail_suoshu});
//		libr_gridview.setAdapter(simpleItem);
		
	}

		public class bookAdapter extends BaseAdapter{
			
			private LayoutInflater inflater;
			
			public bookAdapter(Context c){
				this.inflater=LayoutInflater.from(c);
			}
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return libr_get_book().size();
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
				
				holder.text1.setText("索书号: "+libr_get_book().get(position).get("soushuhao").toString());
				holder.text2.setText("条形码："+libr_get_book().get(position).get("barCode").toString());
				holder.text3.setText("年卷期："+libr_get_book().get(position).get("year_title").toString());
				holder.text4.setText("校区： "+libr_get_book().get(position).get("campus").toString());
				holder.text5.setText("馆藏地： "+libr_get_book().get(position).get("collection").toString());
				holder.text6.setText("书刊状态："+libr_get_book().get(position).get("status").toString());
				
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
		
		protected List< Map<String,Object>> libr_get_book(){
			
			ArrayList<Map<String, Object>> libr_list=new ArrayList<Map<String,Object>>();
			Map<String ,Object> map=new HashMap<String, Object>();
			for(int i=1;i<=2;i++){
				map.put("soushuhao", "123456789");
				map.put("barCode", "123456789");
				map.put("year_title", "--");
				map.put("campus", "九龙湖校区");
				map.put("collection", "中文保存本阅览室");
				map.put("status", "借出-应还日期：2013-09-18");
				
				libr_list.add(map);
			}
			return libr_list;
			
		}

		
}
