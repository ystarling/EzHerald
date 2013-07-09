package com.herald.ezherald.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;

public class LibraryFragment extends SherlockFragment{
	
	EditText libr_search_text;
	String libr_search_value=null;
	ListView libr_listView;
	SimpleAdapter libr_adapter;
	Activity context;
	View view;
	
	ArrayList<Map<String, Object>> libr_list=new ArrayList<Map<String, Object>>();
	
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
				libr_listView = (ListView) view.findViewById(R.id.libr_search_listView);
				libr_adapter=new SimpleAdapter(getActivity(),libr_get_list_value(), 
						R.layout.library_book_list_item, new String[]{"img","name","author","press","date"},
						new int[]{R.id.libr_listitem_book_img,R.id.libr_listitem_book_name,R.id.libr_listitem_book_author,R.id.libr_listitem_book_press,R.id.libr_listitem_book_date});
			
				libr_listView.setAdapter(libr_adapter);
				
				//设置ListView响应事件

				libr_listView.setOnItemClickListener(new OnItemClickListener(){

					@Override
					public void onItemClick(AdapterView<?> arg0, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Toast th=Toast.makeText(getActivity(), "你点的是",Toast.LENGTH_SHORT);
						th.show();
						Intent intent=new Intent(getActivity(), LibraryActivityData.class);
						startActivity(intent);
						
					}
					
				});
				}
			}
		});
		return view ;
		
	}
	
	protected List< Map<String, Object> > libr_get_list_value(){
		Map<String, Object> map=new HashMap<String ,Object>();
		map.put("img",R.drawable.seu);
		map.put("name","好书名");
		map.put("author","责任者："+"李文正");
		map.put("press","出版社："+"东南大学");
		map.put("date","日期："+"东南大学出版社");
		libr_list.add(map);
		map.put("name","书名好");
		map.put("author","责任者："+"李文正");
		map.put("press","出版社："+"东南大学");
		map.put("date","日期："+"东南大学出版社");
		libr_list.add(map);
		return libr_list;
	}
}
