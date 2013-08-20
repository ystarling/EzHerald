package com.herald.ezherald.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
	 *	��һ��fragment����������
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
	
	
	LibraryBooksList librBookList=new LibraryBooksList();//�������
	
	ArrayList<Map<String, Object>> libr_list=new ArrayList<Map<String, Object>>();
	
	public void onCreate(Bundle save){
		super.onCreate(save);
		setRetainInstance(true);
		

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		context=getActivity();
		context.setTitle("ͼ������");

        
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
						Toast toast1=Toast.makeText(getActivity(), "��ʲô������ûд��", Toast.LENGTH_SHORT);
						toast1.show();
					}
					else
					{
					try{
					librservice.search(libr_search_value);//�����û���������
					librservice.getBookDetails(librbook);//��ȡ����
					}
					catch(Exception e){
						e.printStackTrace();
					}
					
					libr_listView = (ListView) view.findViewById(R.id.libr_search_listView);
					libr_adapter=new SimpleAdapter(getActivity(),libr_get_list_value(), 
							R.layout.library_book_list_item,
							new String[]{"img","name","author","press","date","reserve"},
							new int[]{R.id.libr_listitem_book_img,
							R.id.libr_listitem_book_name,
							R.id.libr_listitem_book_author,
							R.id.libr_listitem_book_press,
							R.id.libr_listitem_book_date,
							R.id.libr_listitem_book_reserve
							});
				
					libr_listView.setAdapter(libr_adapter);
					
					
					//����ListView��Ӧ�¼�
					
					libr_listView.setOnItemClickListener(new OnItemClickListener(){

						@Override
						public void onItemClick(AdapterView<?> arg0, View view,
								int position, long id) {
							// TODO Auto-generated method stub
							Toast th=Toast.makeText(getActivity(), "������",Toast.LENGTH_SHORT);
							th.show();
							Intent intent=new Intent(getActivity(), LibraryActivityData.class);
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
					Toast toast=Toast.makeText(getActivity(), "��ʲô������ûд��", Toast.LENGTH_SHORT);
					toast.show();
				}
				else
				{
				try{
				librservice.search(libr_search_value);//�����û���������
				librservice.getBookDetails(librbook);//��ȡ����
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
				libr_listView = (ListView) view.findViewById(R.id.libr_search_listView);
				libr_adapter=new SimpleAdapter(getActivity(),libr_get_list_value(), 
						R.layout.library_book_list_item,
						new String[]{"img","name","author","press","date","reserve"},
						new int[]{R.id.libr_listitem_book_img,
						R.id.libr_listitem_book_name,
						R.id.libr_listitem_book_author,
						R.id.libr_listitem_book_press,
						R.id.libr_listitem_book_date,
						R.id.libr_listitem_book_reserve
						});
			
				libr_listView.setAdapter(libr_adapter);
				
				
				//����ListView��Ӧ�¼�
				
				libr_listView.setOnItemClickListener(new OnItemClickListener(){

					@Override
					public void onItemClick(AdapterView<?> arg0, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Toast th=Toast.makeText(getActivity(), "������",Toast.LENGTH_SHORT);
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
		//map.put("img",R.drawable.seu);
		//for(int i=0;i<=3;i++){
		
		map.put("img",R.drawable.seu);
		

		if(librbook.getName()==null){
			map.put("name","null");
			map.put("author","�����ߣ�"+"null");
			map.put("press","�����磺"+"null");
			map.put("date","���ڣ�"+"null");
			map.put("reserve","ԤԼ");
			Toast toast=Toast.makeText(getActivity(), "ͼ��ݷ�����δ��������", Toast.LENGTH_LONG);
			toast.show();
		}else
		{
			map.put("name",librbook.getName());
			map.put("author","�����ߣ�"+librbook.getAuthor());
			map.put("press","�����磺"+librbook.getPress());
			map.put("date","���ڣ�"+librbook.getIsbn());
			map.put("reserve","ԤԼ");
		}
	
		/*
		map.put("name",librBookList.BookName());
		map.put("author","�����ߣ�"+librBookList.BookAuthor());
		map.put("press","�����磺"+librBookList.BookPress());
		map.put("date","���ڣ�"+librBookList.BookDate());
		map.put("reserve","ԤԼ");
		*/
		libr_list.add(map);
		
		//}
		return libr_list;
	}
	
}
