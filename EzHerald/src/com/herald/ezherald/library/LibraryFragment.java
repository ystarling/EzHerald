package com.herald.ezherald.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;



import cn.edu.seu.herald.ws.api.LibraryService;
import cn.edu.seu.herald.ws.api.impl.HeraldWebServicesFactoryImpl;
import cn.edu.seu.herald.ws.api.library.Book;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;

import com.herald.ezherald.library.LibraryBooksList;


	/*
	 * @author BIG_SEA
	 *	第一个fragment，搜索界面
	 */
public class LibraryFragment extends SherlockFragment{
	
	EditText libr_search_text;
	String libr_search_value=null;
	SimpleAdapter libr_adapter;
	ListView libr_listView;
	Activity context;
	View view;
	

	LibraryService librservice=new HeraldWebServicesFactoryImpl("http://herald.seu.edu.cn/ws").getLibraryService();

	Book librbook=new Book();
	
	
	LibraryBooksList librBookList=new LibraryBooksList();//获得内容
	
	public void onCreate(Bundle save){
		super.onCreate(save);
		setRetainInstance(true);

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		context=getActivity();
		context.setTitle("图书搜索");

        
		view = inflater.inflate(R.layout.library_fragment_main,null);
		
		libr_search_text=(EditText) view.findViewById(R.id.libr_search_text);
		Button libr_search_button=(Button) view.findViewById(R.id.libr_search_button);
		
		/***********set a android search enter*************/
		
		libr_search_text.setOnEditorActionListener(new OnEditorActionListener(){

			@Override
			public boolean onEditorAction(TextView libr_view, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==KeyEvent.ACTION_DOWN ){

					// TODO Auto-generated method stub
					libr_search_value=libr_search_text.getText().toString();
					
					if(libr_search_value.isEmpty())
					{
						Toast toast1=Toast.makeText(getActivity(), "你什么东西都没写啊", Toast.LENGTH_SHORT);
						toast1.show();
					}
					else
					{
					try{
						librservice.search(libr_search_value);//传入用户输入内容
						librservice.getBookDetails(librbook);//获取内容
					}
					catch(Exception e){
						e.printStackTrace();
					}
					libr_listView = (ListView) view.findViewById(R.id.libr_search_listView);
					librMyAdapter mAdapter=new librMyAdapter(getActivity());
					mAdapter.notifyDataSetChanged();
					libr_listView.setAdapter(mAdapter);
					view.requestLayout();
					
					
					
					//设置ListView响应事件
					
					libr_listView.setOnItemClickListener(new OnItemClickListener(){

						@Override
						public void onItemClick(AdapterView<?> arg0, View view,
								int position, long id) {
							// TODO Auto-generated method stub
							Toast th=Toast.makeText(getActivity(), "你点的是",Toast.LENGTH_SHORT);
							th.show();
							Intent intent=new Intent(getActivity(), LibraryBookListDetail.class);
							startActivity(intent);
							
						}
						
					});
					}
				
				}
				return true;
			}
			
		});
		
		/************set search button click***********/
		
		libr_search_button.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				libr_search_value=libr_search_text.getText().toString();
				
				if(libr_search_value.isEmpty())
				{
					Toast toast=Toast.makeText(getActivity(), "你什么东西都没写啊", Toast.LENGTH_SHORT);
					toast.show();
				}
				else
				{
				try{
					librservice.search(libr_search_value);//传入用户输入内容
					librservice.getBookDetails(librbook);//获取内容
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
				libr_listView = (ListView) view.findViewById(R.id.libr_search_listView);
				librMyAdapter mAdapter=new librMyAdapter(getActivity());
				mAdapter.notifyDataSetChanged();
				libr_listView.setAdapter(mAdapter);
				view.requestLayout();
				
				/**********simpleAdapter********/
				
				
				//设置ListView响应事件
				
				libr_listView.setOnItemClickListener(new OnItemClickListener(){

					@Override
					public void onItemClick(AdapterView<?> arg0, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Toast th=Toast.makeText(getActivity(), "你点的是",Toast.LENGTH_SHORT);
						th.show();
						Intent intent=new Intent(getActivity(), LibraryBookListDetail.class);
						startActivity(intent);
						
					}
					
				});
				
				}
			}
			
		});
		
		return view ;
		
	}
	
	protected List< Map<String, Object> > libr_get_list_value(){
		
		ArrayList<Map<String, Object>> libr_list=new ArrayList<Map<String, Object>>();
		Map<String, Object> map=new HashMap<String ,Object>();
		//map.put("img",R.drawable.seu);
		for(int i=0;i<=3;i++){
		
		map.put("img",R.drawable.seu);
		map.put("name"," "+libr_search_value);
		map.put("author","责任者："+librbook.getAuthor());
		map.put("press","出版社："+librbook.getPress());
		map.put("date","日期："+librbook.getIsbn());
		map.put("reserve","预约");	
		
//		map.put("name",librBookList.BookName());
//		map.put("author","责任者："+librBookList.BookAuthor());
//		map.put("press","出版社："+librBookList.BookPress());
//		map.put("date","日期："+librBookList.BookDate());
//		map.put("reserve","预约");
		
		libr_list.add(map);
		}
		return libr_list;
	}
	
	/**************set BaseAdapter********************/
	
	public class librMyAdapter extends BaseAdapter{
		
		private LayoutInflater inflater;
		
		public librMyAdapter(Context c){
			this.inflater=LayoutInflater.from(c);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return libr_get_list_value().size();
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
				convertView=inflater.inflate(R.layout.library_book_list_item, null);
				
				holder=new ViewHolder();
				
				holder.libr_photo=(ImageView) convertView.findViewById(R.id.libr_listitem_book_img);
				holder.libr_name=(TextView) convertView.findViewById(R.id.libr_listitem_book_name);
				holder.libr_author=(TextView) convertView.findViewById(R.id.libr_listitem_book_author);
				holder.libr_press=(TextView) convertView.findViewById(R.id.libr_listitem_book_press);
				holder.libr_date=(TextView) convertView.findViewById(R.id.libr_listitem_book_date);
				holder.libr_button_reserve=(Button) convertView.findViewById(R.id.libr_listitem_book_reserve);
				convertView.setTag(holder);//绑定ViewHolder对象
			}
			else{
				holder=(ViewHolder)convertView.getTag();
			}
			
			/******设置对应的动态数组数据*********/
			 holder.libr_photo.setImageResource(R.drawable.seu);
			 holder.libr_name.setText(libr_get_list_value().get(position).get("name").toString());
			 holder.libr_author.setText(libr_get_list_value().get(position).get("author").toString());
			 holder.libr_press.setText(libr_get_list_value().get(position).get("press").toString());
			 holder.libr_date.setText(libr_get_list_value().get(position).get("date").toString());
			 holder.libr_button_reserve.setText("预约");
			 
			 holder.libr_button_reserve.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					Toast th=Toast.makeText(getActivity(), "你点的是",Toast.LENGTH_SHORT);
					th.show();
					Intent intent=new Intent(getActivity(), LibraryActivityReserve.class);
					startActivity(intent);
				}
				 
			 });
			 return convertView;
		}
		
	}
	
	public class ViewHolder{
		public ImageView libr_photo;
		public TextView libr_name;
		public TextView libr_author;
		public TextView libr_press;
		public TextView libr_date;
		public Button libr_button_reserve;
	}
}
