package com.herald.ezherald.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.layout;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.herald.ezherald.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
/**
 * @author      zhangyu
* @since      版本
* 类的简单介绍
 */

public class LibraryActivity extends SherlockActivity {
	EditText libr_search_text;
	String libr_search_value=null;
	ListView libr_listView;
	SimpleAdapter libr_adapter;
	List libr_list=new ArrayList<Map<String, Object>>();
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library_activity_main);
		setTitle("图书搜索");

		libr_search_text=(EditText) findViewById(R.id.libr_search_text);
		Button libr_search_button=(Button) findViewById(R.id.libr_search_button);
		
		libr_search_button.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				libr_search_value=libr_search_text.getText().toString();
				if(libr_search_value.isEmpty())
				{
					Toast toast=Toast.makeText(LibraryActivity.this, "你什么东西都没写啊", Toast.LENGTH_SHORT);
					toast.show();
				}
				else
				{	
				System.out.print(libr_search_value);
				libr_listView = (ListView) findViewById(R.id.libr_search_listView);
				libr_adapter=new SimpleAdapter(LibraryActivity.this,libr_get_list_value(), 
						R.layout.library_book_list_item, new String[]{"img","name","author","press","date"},
						new int[]{R.id.libr_listitem_book_img,R.id.libr_listitem_book_name,R.id.libr_listitem_book_author,R.id.libr_listitem_book_press,R.id.libr_listitem_book_date});
			
				libr_listView.setAdapter(libr_adapter);
				}
			}
		});
		//public void onItemClick(AdapterView<?> parent, View view, int position, long id){
			
		//}
		
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

