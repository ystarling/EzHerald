package com.herald.ezherald.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import cn.edu.seu.herald.ws.api.CurriculumService;
import cn.edu.seu.herald.ws.api.HeraldWebServicesFactory;
import cn.edu.seu.herald.ws.api.LibraryService;
import cn.edu.seu.herald.ws.api.curriculum.Curriculum;
import cn.edu.seu.herald.ws.api.impl.HeraldWebServicesFactoryImpl;
import cn.edu.seu.herald.ws.api.library.Book;
import cn.edu.seu.herald.ws.api.library.Booklist;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;


	/*
	 * @author BIG_SEA
	 *	第一个fragment，搜索界面
	 */
public class LibraryFragment extends SherlockFragment{
	
	protected static final String TAG = null;
	EditText libr_search_text;
	String libr_search_value=null;
	SimpleAdapter libr_adapter;
	ListView libr_listView;
	Activity activity;
	View view;
	Context context;
	//Booklist booklist=new Booklist();
	//LibraryService librservice=null;
	//Book librbook=new Book();
	//List<Book> booklist2=new ArrayList<Book>();
	
	
	public void onCreate(Bundle save){
		super.onCreate(save);
		setRetainInstance(true);
		//librservice=new HeraldWebServicesFactoryImpl("http://herald.seu.edu.cn/ws").getLibraryService();
		
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		activity=getActivity();
		activity.setTitle("图书搜索");
		context=getActivity();
		
		
//		try{
//			String HERALD_WS_URI = "http://herald.seu.edu.cn/ws";
//	        HeraldWebServicesFactory factory =new HeraldWebServicesFactoryImpl(HERALD_WS_URI
//	        CurriculumService curriculumService = factory.getCurriculumService();
//	        Curriculum curriculum = curriculumService.getCurriculum("213100434");
//	        Log.e("test","curriculum.cardNumber=" +
//	                curriculum.getCardNumber());
//	        System.out.println("curriculum.term=" + curriculum.getTerm());
//	        System.out.println("curriculum.courses[0].name=" +
//	                curriculum.getCourses().getCourses().get(0).getName());
//	        System.out.println("curriculum.courses[0].lecturer=" +
//	                curriculum.getCourses().getCourses().get(0).getLecturer());
//	        System.out.println("curriculum.timeTable.schedule[0].day=" +
//	                curriculum.getTimeTable().getSchedules().get(0).getDay());
//
//		}catch(Exception e){
//            StackTraceElement[] ste=e.getStackTrace();  
//            for(int i=0;i<ste.length;i++){  
//                Log.e("tag", ste[i].toString());  
//		}
//		}
        
		
		view = inflater.inflate(R.layout.library_fragment_main,null);
		
		libr_search_text=(EditText) view.findViewById(R.id.libr_search_text);
		ImageView libr_search_button=(ImageView) view.findViewById(R.id.libr_search_button);
		
		/***********set a android search enter*************/
		
		libr_search_text.setOnEditorActionListener(new OnEditorActionListener(){

			@Override
			public boolean onEditorAction(TextView libr_view, int keyCode, KeyEvent event) {
				if(event.getAction()==KeyEvent.ACTION_DOWN ){

					libr_search_value=libr_search_text.getText().toString();
					
					if(libr_search_value.isEmpty())
					{
						Toast toast1=Toast.makeText(getActivity(), "你什么东西都没写啊", Toast.LENGTH_SHORT);
						toast1.show();
					}else{
						
						LibraryFragmentThread th=new LibraryFragmentThread(libr_search_value,getActivity(), context);
						Log.e("输入内容：",libr_search_value);
						th.start();
					}
					
					
					}
				
//				}
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
				}else{
				
					LibraryFragmentThread th=new LibraryFragmentThread(libr_search_value,getActivity(), context);
					Log.e("输入内容：",libr_search_value);
					th.start();
					}
				}
		});
		
		return view ;
		
	}
	
	
//	/**************set BaseAdapter********************/
//	
//	public class librMyAdapter extends BaseAdapter{
//		
//		private LayoutInflater inflater;
//		
//		public librMyAdapter(Context c){
//			this.inflater=LayoutInflater.from(c);
//		}
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return ;
//		}
//
//		@Override
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//		
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			// TODO Auto-generated method stub
//			ViewHolder holder;
//			if(convertView==null){
//				convertView=inflater.inflate(R.layout.library_book_list_item, null);
//				
//				holder=new ViewHolder();
//				
//				//holder.libr_photo=(ImageView) convertView.findViewById(R.id.libr_listitem_book_img);
//				holder.libr_name=(TextView) convertView.findViewById(R.id.libr_listitem_book_name);
//				holder.libr_author=(TextView) convertView.findViewById(R.id.libr_listitem_book_author);
//				holder.libr_press=(TextView) convertView.findViewById(R.id.libr_listitem_book_press);
//				holder.libr_date=(TextView) convertView.findViewById(R.id.libr_listitem_book_date);
//				holder.libr_button_reserve=(Button) convertView.findViewById(R.id.libr_listitem_book_reserve);
//				convertView.setTag(holder);//绑定ViewHolder对象
//			}
//			else{
//				holder=(ViewHolder)convertView.getTag();
//			}
//			
//			/******设置对应的动态数组数据*********/
//			 //holder.libr_photo.setImageResource(R.drawable.seu);
//			 holder.libr_name.setText(.get(position).get("name").toString());
//			 holder.libr_author.setText(.get(position).get("author").toString());
//			 holder.libr_press.setText(.get(position).get("press").toString());
//			 holder.libr_date.setText(.get(position).get("date").toString());
//			 holder.libr_button_reserve.setText("预约");
//			 
//			 holder.libr_button_reserve.setOnClickListener(new OnClickListener(){
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					
//					Toast th=Toast.makeText(getActivity(),"你點的是",Toast.LENGTH_SHORT);
//					th.show();
//					Intent intent=new Intent(getActivity(), LibraryActivityReserve.class);
//					startActivity(intent);
//				}
//				 
//			 });
//			 return convertView;
//		}
//		
//	}
	
//	public class ViewHolder{
//		//public ImageView libr_photo;
//		public TextView libr_name;
//		public TextView libr_author;
//		public TextView libr_press;
//		public TextView libr_date;
//		public TextView libr_callNumber;
//		public TextView libr_ducumentType;
//		public Button libr_button_reserve;
//	}
}
