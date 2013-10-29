package com.herald.ezherald.library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;
import com.herald.ezherald.library.LibraryFragmentThread.MyHandle2;
import com.herald.ezherald.library.LibraryFragmentThread.MyHandle3;

/*
 * @author BIG_SEA
 *	第一个fragment，搜索界面
 */
public class LibraryFragment extends SherlockFragment {

	protected static final String TAG = null;
	EditText libr_search_text;
	String libr_search_value = null;
	SimpleAdapter libr_adapter;
	ListView libr_listView;
	ListView listview;
	Activity activity;
	View view;
	Context context;

	Boolean isLastRow=false;
	int lastItem;
	// ListView底部View
	private View moreView;
	Button moreButton;
	private Handler handler;
	// 设置一个最大的数据条数，超过即不再加载
	private int MaxDateNum;
	// 最后可见条目的索引
	private int lastVisibleIndex;
	public ArrayList<HashMap<String, String>> list;
	SimpleAdapter mSimpleAdapter;

	private ProgressDialog dialog1;
	private int CountOfScroll = 1;
	private int CountOfScroll_two= 2;
	private String TestSearchValue = "";// determine loadmore or restart
	private int jsonarraycount = 0;
	private mHandler myHandler= new mHandler();
	// private MyHandle myHandler = new MyHandle();
	public void onCreate(Bundle save) {
		super.onCreate(save);

		setRetainInstance(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		activity.setTitle("图书搜索");
		context = getActivity();

		view = inflater.inflate(R.layout.library_fragment_main, null);
		
		dialog1 = new ProgressDialog(activity);
		dialog1.setCanceledOnTouchOutside(false);
		dialog1.setMessage("加载中...");
		
		libr_search_text = (EditText) view.findViewById(R.id.libr_search_text);
		ImageView libr_search_button = (ImageView) view
				.findViewById(R.id.libr_search_button);

		listview = (ListView) view.findViewById(R.id.libr_search_listView);
		moreView = inflater.inflate(R.layout.library_moredata, null);

		moreButton = (Button) moreView.findViewById(R.id.bt_load);

		handler = new Handler();

		list = new ArrayList<HashMap<String, String>>();

		mSimpleAdapter = new SimpleAdapter(activity, list,
				R.layout.library_book_list_item, new String[] { "libr_title",
						"libr_name", "libr_press", "libr_callNumber",
						"libr_ducumentType", "libr_store_num",
						"libr_landable_num" }, new int[] {
						R.id.libr_listitem_book_name,
						R.id.libr_listitem_book_author,
						R.id.libr_listitem_book_press,
						R.id.libr_listitem_book_callNumber,
						R.id.libr_listitem_book_documentType,
						R.id.libr_listitem_book_store_num,
						R.id.libr_listitem_book_landable_num });

		// 加上底部View，注意要放在setAdapter方法前
		listview.addFooterView(moreView);
		listview.setAdapter(mSimpleAdapter);

		/************ set search button click ***********/

		libr_search_button.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CountOfScroll_two= 2;
				libr_search_value = libr_search_text.getText().toString();

				/******** 隐藏软键盘 *************/
				InputMethodManager m = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				m.hideSoftInputFromWindow(libr_search_text.getWindowToken(), 0);

				if (libr_search_value.isEmpty()) {
					Toast toast = Toast.makeText(getActivity(), "搜索关键词为空",
							Toast.LENGTH_SHORT);
					toast.show();
				} else {

					CountOfScroll = 1;

					LibraryFragmentThread th1 = new LibraryFragmentThread(
							libr_search_value, activity, CountOfScroll,
							LibraryFragment.this);
					Log.e("CountOfScroll", CountOfScroll + "");
					th1.start();
//					TestSearchValue = libr_search_value;
//					
//					dialog1.show();
//					handler.postDelayed(new Runnable() {
//
//						@Override
//						public void run() {
//							if (jsonarraycount != 20) {
//								moreButton.setVisibility(View.GONE);
//							} else {
//
//								moreButton.setVisibility(View.VISIBLE);
//							}
//							listview.setAdapter(mSimpleAdapter);
//							mSimpleAdapter.notifyDataSetChanged();// 通知listView刷新数据
//							
//							dialog1.cancel();
//							Log.e("list5", list + "");
//						}
//					}, 0);
//					
//					
//					CountOfScroll++;

//					listview.setOnScrollListener(new OnScrollListener() {
//						@Override
//						public void onScroll(AbsListView view,
//								int firstVisibleItem, int visibleItemCount,
//								int totalItemCount) {
//							// TODO Auto-generated method stub
//							if (firstVisibleItem + visibleItemCount == totalItemCount
//									&& totalItemCount > 0
//									&& totalItemCount == 20) {
//								isLastRow = true;
//							}
//
//							lastItem = firstVisibleItem + visibleItemCount;
//
//						}
//
//						@Override
//						public void onScrollStateChanged(AbsListView view,
//								int scrollState) {
//							// TODO Auto-generated method stub
//							// 当滚到最后一行且停止滚动时，执行加载
//							if (isLastRow == true
//									&& scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
//								// 加载元素
//								Toast toast1 = Toast.makeText(activity, "加载更多",
//										Toast.LENGTH_SHORT);
//								toast1.show();
//								/********* 设置编号问题 *************/
//								if (!TestSearchValue.equals(libr_search_value)) {
//									CountOfScroll = 0;
//								}
//
//							}
//							isLastRow = false;
//						}
//
//					});

				}
				
			}
			
		});
		/************ loadmore button *****************/
		//moreButton.setVisibility(View.VISIBLE);
		moreButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				moreButton.setVisibility(View.GONE);// 按钮不可见

				LibraryFragmentThread th = new LibraryFragmentThread(
						libr_search_value, activity, CountOfScroll_two,
						LibraryFragment.this);
				Log.e("CountOfScroll", CountOfScroll_two + "");
				th.start();

//				TestSearchValue = libr_search_value;
//				CountOfScroll_two++;
//				
//				dialog1.show();
//				handler.postDelayed(new Runnable() {
//
//					@Override
//					public void run() {
//						
//						if(dialog1.isShowing()){
//						dialog1.cancel();
//						}
//						if (jsonarraycount != 20) {
//							moreButton.setVisibility(View.GONE);
//						} else {
//
//							moreButton.setVisibility(View.VISIBLE);
//						}
//						mSimpleAdapter.notifyDataSetChanged();// 通知listView刷新数据
//
//					}
//				}, 0);
				
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {

			Bundle bundle = null;
			JSONObject json = null;
			String loc_name = null;
			String loc_author = null;
			String loc_press = null;
			// String loc_date=null;
			String loc_callNumber = null;
			String loc_documentType = null;
			String loc_marc_no = null;

			HashMap<String, String> map = new HashMap<String, String>();

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {

				try {
					map = list.get(position);
					// Toast toast=Toast.makeText(context, list+"   "+position,
					// Toast.LENGTH_LONG);
					// toast.show();
					loc_name = map.get("libr_title").toString();
					loc_author = map.get("libr_name").toString();
					// loc_press = map.get("libr_publisher").toString();
					// loc_date=json.getString("book_date");
					// loc_callNumber = map.get("libr_isbn").toString();
					// loc_documentType = map.get("libr_doctype").toString();
					loc_marc_no = map.get("libr_marc_no").toString();
				} catch (Exception e) {

				}

				bundle = new Bundle();
				bundle.putString("loc_name", loc_name);
				bundle.putString("loc_author", loc_author);
				// bundle.putString("loc_press", loc_press);
				// bundle.putString("loc_date", loc_date);
				// bundle.putString("loc_callNumber", loc_callNumber);
				// bundle.putString("loc_documentType", loc_documentType);
				bundle.putString("loc_marc_no", loc_marc_no);

				Intent intent = new Intent(activity,
						LibraryBookListDetail.class);

				intent.putExtras(bundle);
				activity.startActivity(intent);
			}
		});

		return view;

	}

	public void LoadMoreData(JSONArray jsonarray,int num) {

		String libr_name = null, libr_author = null, libr_press = null, libr_date = null, libr_callNumber = null, libr_ducumentType = null;
		String libr_marc_no = null;// view detail request
		String libr_store_num = null;
		String libr_landable_num = null;
		JSONObject json = null;
		// Log.e("LoadMoreData", jsonarray + "");
		jsonarraycount = isFilled(jsonarray);

		for (int i = 0; i < jsonarray.length(); i++) {
			HashMap<String, String> map1 = new HashMap<String, String>();

			try {
				json = jsonarray.getJSONObject(i);
				libr_name = json.getString("title");
				libr_author = json.getString("author");
				libr_press = json.getString("publisher");
				// libr_date = json.getString("book_date");
				libr_callNumber = json.getString("isbn");
				libr_ducumentType = json.getString("doctype");
				libr_marc_no = json.getString("marc_no");
				libr_store_num = json.getString("store_num");
				libr_landable_num = json.getString("lendable_num");

			} catch (Exception e) {
				Log.e("error", "传来失败");
			}
			map1.put("libr_title", ((num-1)*20 +i+1)+ " " + libr_name);
			map1.put("libr_name", libr_author);
			map1.put("libr_press", libr_press);
			map1.put("libr_callNumber", libr_callNumber);
			map1.put("libr_ducumentType", libr_ducumentType);
			map1.put("libr_marc_no", libr_marc_no);
			map1.put("libr_store_num", "馆藏副本 : " + libr_store_num);
			map1.put("libr_landable_num", "可借副本 : " + libr_landable_num);
			list.add(map1);
		}
		
		TestSearchValue = libr_search_value;
		CountOfScroll_two++;
		
		dialog1.show();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				
				if(dialog1.isShowing()){
				dialog1.cancel();
				}
				if (jsonarraycount != 20) {
					moreButton.setVisibility(View.GONE);
				} else {

					moreButton.setVisibility(View.VISIBLE);
				}
				mSimpleAdapter.notifyDataSetChanged();// 通知listView刷新数据

			}
		}, 0);
	}

	public void SetData(JSONArray jsonarray,int num) {

		String libr_name = null, libr_author = null, libr_press = null, libr_date = null, libr_callNumber = null, libr_ducumentType = null;
		String libr_marc_no = null;// view detail request
		String libr_store_num = null;
		String libr_landable_num = null;
		JSONObject json = null;
		ShowMsg(jsonarray);
		jsonarraycount = isFilled(jsonarray);
		list.clear();// empty list，not allowed list=local_list
		
		for (int i = 0; i < jsonarray.length(); i++) {
			HashMap<String, String> map1 = new HashMap<String, String>();

			try {
				json = jsonarray.getJSONObject(i);
				libr_name = json.getString("title");
				libr_author = json.getString("author");
				libr_press = json.getString("publisher");
				// libr_date = json.getString("book_date");
				libr_callNumber = json.getString("isbn");
				libr_ducumentType = json.getString("doctype");
				libr_marc_no = json.getString("marc_no");
				libr_store_num = json.getString("store_num");
				libr_landable_num = json.getString("lendable_num");

			} catch (Exception e) {
				Log.e("error", "传来失败");
			}

			map1.put("libr_title", ((num-1)*20 +i+1)+ " . " + libr_name);
			Log.e("libr_title",libr_name);

			map1.put("libr_title", ((num-1)*20 +i+1)+ " " + libr_name);

			map1.put("libr_name", libr_author);
			map1.put("libr_press", libr_press);
			map1.put("libr_callNumber", libr_callNumber);
			map1.put("libr_ducumentType", libr_ducumentType);
			map1.put("libr_marc_no", libr_marc_no);
			map1.put("libr_store_num", "馆藏副本 : " + libr_store_num);
			map1.put("libr_landable_num", "可借副本 : " + libr_landable_num);
			list.add(map1);
		}
		TestSearchValue = libr_search_value;
		
		dialog1.show();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (jsonarraycount != 20) {
					moreButton.setVisibility(View.GONE);
				} else {

					moreButton.setVisibility(View.VISIBLE);
				}
				listview.setAdapter(mSimpleAdapter);
				mSimpleAdapter.notifyDataSetChanged();// 通知listView刷新数据
				
				dialog1.cancel();
				Log.e("list5", list + "");
			}
		}, 0);
		
		
		CountOfScroll++;
	}

	public int isFilled(JSONArray jsonarray) {
		if (jsonarray.length() == 20) {
			return 20;
		} else {
			return jsonarray.length();
		}
	}
	public void SetRemind() {
		Toast toast1 = Toast.makeText(activity, "该图书不存在!",
				Toast.LENGTH_SHORT);
		toast1.show();
	}
	public void ShowMsg(JSONArray json){
		Message m=new Message();
		m.obj=json;
		m.setTarget(myHandler);
		m.sendToTarget();
		
	}
	public class mHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			JSONArray json=(JSONArray) msg.obj;
			if(json.isNull(0)){
				SetRemind();
			}
			super.handleMessage(msg);
		}
		
	}
	// public void ShowMsg(Map e) {
	// Message msg = Message.obtain();
	// msg.obj = e;
	// msg.setTarget(myHandler);
	// msg.sendToTarget();
	// }
	//
	// public class MyHandle extends Handler {
	//
	// @Override
	// public void handleMessage(Message msg) {
	// // TODO Auto-generated method stub
	// HashMap<String, String> map = new HashMap<String, String>();
	// map = (HashMap<String, String>) msg.obj;
	// list.add(map);
	// super.handleMessage(msg);
	// }
	//
	// }
	// /**************set BaseAdapter********************/
	//
	// public class librMyAdapter extends BaseAdapter{
	//
	// private LayoutInflater inflater;
	//
	// public librMyAdapter(Context c){
	// this.inflater=LayoutInflater.from(c);
	// }
	// @Override
	// public int getCount() {
	// // TODO Auto-generated method stub
	// return ;
	// }
	//
	// @Override
	// public Object getItem(int position) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// // TODO Auto-generated method stub
	// return 0;
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// // TODO Auto-generated method stub
	// ViewHolder holder;
	// if(convertView==null){
	// convertView=inflater.inflate(R.layout.library_book_list_item, null);
	//
	// holder=new ViewHolder();
	//
	// //holder.libr_photo=(ImageView)
	// convertView.findViewById(R.id.libr_listitem_book_img);
	// holder.libr_name=(TextView)
	// convertView.findViewById(R.id.libr_listitem_book_name);
	// holder.libr_author=(TextView)
	// convertView.findViewById(R.id.libr_listitem_book_author);
	// holder.libr_press=(TextView)
	// convertView.findViewById(R.id.libr_listitem_book_press);
	// holder.libr_date=(TextView)
	// convertView.findViewById(R.id.libr_listitem_book_date);
	// holder.libr_button_reserve=(Button)
	// convertView.findViewById(R.id.libr_listitem_book_reserve);
	// convertView.setTag(holder);//绑定ViewHolder对象
	// }
	// else{
	// holder=(ViewHolder)convertView.getTag();
	// }
	//
	// /******设置对应的动态数组数据*********/
	// //holder.libr_photo.setImageResource(R.drawable.seu);
	// holder.libr_name.setText(.get(position).get("name").toString());
	// holder.libr_author.setText(.get(position).get("author").toString());
	// holder.libr_press.setText(.get(position).get("press").toString());
	// holder.libr_date.setText(.get(position).get("date").toString());
	// holder.libr_button_reserve.setText("预约");
	//
	// holder.libr_button_reserve.setOnClickListener(new OnClickListener(){
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	//
	// Toast th=Toast.makeText(getActivity(),"你點的是",Toast.LENGTH_SHORT);
	// th.show();
	// Intent intent=new Intent(getActivity(), LibraryActivityReserve.class);
	// startActivity(intent);
	// }
	//
	// });
	// return convertView;
	// }
	//
	// }

	// public class ViewHolder{
	// //public ImageView libr_photo;
	// public TextView libr_name;
	// public TextView libr_author;
	// public TextView libr_press;
	// public TextView libr_date;
	// public TextView libr_callNumber;
	// public TextView libr_ducumentType;
	// public Button libr_button_reserve;
	// }
}
