package com.herald.ezherald.library;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;
import com.herald.ezherald.library.LibraryFragmentThread.BookMyAdapter;


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
	ListView listview;
	Activity activity;
	View view;
	Context context;
	BookMyAdapter adapter;
	boolean isLastRow;
	int lastItem;
	int CountOfScroll;
	
	public void onCreate(Bundle save){
		super.onCreate(save);
		
		setRetainInstance(true);
		
		//librservice=new HeraldWebServicesFactoryImpl("http://herald.seu.edu.cn/ws").getLibraryService();
		
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library_fragment_main);
		activity=getActivity();
		activity.setTitle("图书搜索");
		context=getActivity();
		
		
   	
		view = inflater.inflate(R.layout.library_fragment_main,null);
		
		libr_search_text=(EditText) view.findViewById(R.id.libr_search_text);
		ImageView libr_search_button=(ImageView) view.findViewById(R.id.libr_search_button);
		
		
		/************set search button click***********/
		
		libr_search_button.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				libr_search_value=libr_search_text.getText().toString();
				
				/********隐藏软键盘*************/
				InputMethodManager m=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);   
				m.hideSoftInputFromWindow(libr_search_text.getWindowToken(), 0);
				
				if(libr_search_value.isEmpty())
				{
					Toast toast=Toast.makeText(getActivity(), "你什么东西都没写啊", Toast.LENGTH_SHORT);
					toast.show();
				}else{
					
					//View loadview=(View) activity.findViewById(R.layout.library_fragment_loadmore);
					//listview.addFooterView(loadview);
					listview = (ListView) activity.findViewById(R.id.libr_search_listView);
					
					listview.setOnScrollListener(new OnScrollListener() {
						
						@Override
						public void onScrollStateChanged(AbsListView view, int scrollState) {
							// TODO Auto-generated method stub
						        //当滚到最后一行且停止滚动时，执行加载
							       if (isLastRow==true && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
							            //加载元素
										Toast toast1 = Toast.makeText(activity,"加载更多",Toast.LENGTH_SHORT);
										toast1.show();
										CountOfScroll=CountOfScroll+1;
										
										listview.setSelection(lastItem - 1);
										LibraryFragmentThread th=new LibraryFragmentThread(libr_search_value,getActivity(), context,listview, CountOfScroll);
										Log.e("输入内容：",libr_search_value);
										th.start();
							        }
							       isLastRow = false;
						}
						
						@Override
						public void onScroll(AbsListView view, int firstVisibleItem,
								int visibleItemCount, int totalItemCount) {
							// TODO Auto-generated method stub
							Log.e("firstVisibleItem",""+firstVisibleItem);
							if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0&&totalItemCount==20) {
								  isLastRow = true;
							 }
							Log.e("isLastRow",isLastRow+"");
							
							lastItem = firstVisibleItem + visibleItemCount;
							
						}
					});
					
					LibraryFragmentThread th=new LibraryFragmentThread(libr_search_value,getActivity(), context,listview,0);
					Log.e("输入内容：",libr_search_value);
					th.start();
					}
				}
		});
		
		return view ;
		
	}


	private void setContentView(int libraryFragmentMain) {
		// TODO Auto-generated method stub
		
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
