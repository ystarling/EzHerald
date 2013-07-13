package com.herald.ezherald.library;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.herald.ezherald.R;

public class LibraryActivityData extends SherlockActivity{
	
	
	ArrayList<Map<String, Object>> arraylist=new ArrayList<Map<String, Object>>();
	protected String[] string;;
	protected int[] libr_int;
	Fragment mContentFragment;
	
	ListView libr_listView;
	SimpleAdapter libr_adapter;
	LinearLayout myLinearLayout;
	public void onCreate(Bundle saveInstanceSate){
		super.onCreate(saveInstanceSate);
		setContentView(R.layout.library_activity_book);
		setTitle("搜索结果显示");
		//TextView libr_book_name=(TextView) findViewById(R.id.libr_book_name);
//		TextView libr_text1=(TextView) findViewById(R.id.libr_name);
//		TextView libr_book_name=(TextView) findViewById(R.id.libr_book_listdetail_name);
//		TextView libr_text2=(TextView) findViewById(R.id.libr_press);
//		TextView libr_book_press=(TextView) findViewById(R.id.libr_book_listdetail_press);
//		TextView libr_text3=(TextView) findViewById(R.id.libr_prize);
//		TextView libr_book_prize=(TextView) findViewById(R.id.libr_book_listdetail_prize);
		
		
//		ListView libr_book_list1=(ListView)findViewById(R.id.libr_book_listview);
//		ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,libr_get_List());
//		libr_book_list1.setAdapter(arrayAdapter);
		
		
		
		
		/************ create GridView about library some information **********/
		
		GridView libr_gridview=(GridView) findViewById(R.id.libr_book_grid);
		ArrayList<HashMap<String, Object>> libr_gridItem=new ArrayList<HashMap<String,Object>>();
		for(int i=0;i<16;i++){
			HashMap<String, Object> map=new HashMap<String, Object>();
			map.put("ItemText","NO"+String.valueOf(i));
			libr_gridItem.add(map);
		}
		SimpleAdapter simpleItem=new SimpleAdapter(this,
				libr_gridItem,
				R.layout.library_book_grid1,
				new String[]{"ItemText"},
				new int[]{R.id.libr_book_listdetail_suoshu});
		libr_gridview.setAdapter(simpleItem);
		
	}
	
		
//		public List<String> libr_get_List(){
//			List<String> mylist=new ArrayList<String>();
//			mylist.add("索书号");
//			mylist.add("条形码");
//			mylist.add("年卷号");
//			return mylist;
//		}
	
		protected String[] libr_get_string(){
			string=new String[]{"name","press","prize"};
			return string;
			}
		
		protected int[] libr_get_int(){
			
			//libr_int=new int[]{R.id.libr_book_listdetail_name,R.id.libr_book_listdetail_press,R.id.libr_book_listdetail_prize};
			return libr_int;
		}
		
		protected List<Map<String,Object>> libr_get_list_value(){
			Map<String, Object> map=new HashMap<String ,Object>();
			map.put("name","题名/责任者:");
			map.put("press","出版发行项:");
			map.put("prize","ISBN及定价:");
			arraylist.add(map);
			return arraylist;
		}
}
