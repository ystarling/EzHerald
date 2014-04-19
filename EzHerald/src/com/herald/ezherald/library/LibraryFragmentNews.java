/**
 * 
 */
package com.herald.ezherald.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;
 
/**
 * @author BIG_SEA
 *	The fragment of news
 */
public class LibraryFragmentNews extends SherlockFragment{
	
	ListView libr_listView;
	View view;
	SimpleAdapter libr_adapter;
	HashMap<String, Object> map=new HashMap<String ,Object>();
	ArrayList<HashMap<String, Object>> libr_list=new ArrayList<HashMap<String, Object>>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group , Bundle save){
		View view=inflater.inflate(R.layout.library_fragment_news, null);
		Activity ac=getActivity();
		ac.setTitle("图书馆公告");
		
		libr_listView=(ListView) view.findViewById(R.id.libr_news_list);
		
		libr_adapter=new SimpleAdapter(getActivity(),
		libr_get_list_value(),
		R.layout.library_news_list_item,
		new String[]{"name"},
		new int[]{R.id.libr_listitem_news_name,
		});
		libr_listView.setAdapter(libr_adapter);
		
		
		libr_listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Toast th=Toast.makeText(getActivity(), "你点的是",Toast.LENGTH_SHORT);
				th.show();
				Intent intent=new Intent(getActivity(), LibraryActivityNews.class);
				startActivity(intent);
				
			}
			
		});
		
		return view;
		
	}
	
	protected List< HashMap<String, Object> > libr_get_list_value(){
		//HashMap<String, Object> map=new HashMap<String ,Object>();
		for(int i=0;i<=3;i++){
		map.put("name","图书馆办公室招聘勤工助学本科生");
		libr_list.add(map);
		}
		return libr_list;
	}
}
