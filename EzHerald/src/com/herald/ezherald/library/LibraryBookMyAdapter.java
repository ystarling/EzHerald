package com.herald.ezherald.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.herald.ezherald.R;

/************** set BaseAdapter ********************/

public class LibraryBookMyAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	List<JSONArray> dataList = new ArrayList<JSONArray>();
	Context mycontext;
	//int CountOfScroll;
	int length;
	public LibraryBookMyAdapter(Context c) {
		this.inflater = LayoutInflater.from(c);
		//this.CountOfScroll = count;
		for(Iterator<JSONArray> i=dataList.iterator();i.hasNext();){
			JSONArray json2 = i.next();
			length=json2.length();
		}
	}

	public void setData(List dl) {
		dataList = dl;
	}

	public void addData(JSONArray dl) {
		dataList.add(dl);
	}
	
	public void setLength(int num){
		length=num;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.library_book_list_item,
					null);

			holder = new ViewHolder();

			holder.libr_name = (TextView) convertView
					.findViewById(R.id.libr_listitem_book_name);
			holder.libr_author = (TextView) convertView
					.findViewById(R.id.libr_listitem_book_author);
			holder.libr_press = (TextView) convertView
					.findViewById(R.id.libr_listitem_book_press);
			// holder.libr_date=(TextView)
			// convertView.findViewById(R.id.libr_listitem_book_date);
			holder.libr_callNumber = (TextView) convertView
					.findViewById(R.id.libr_listitem_book_callNumber);
			holder.libr_ducumentType = (TextView) convertView
					.findViewById(R.id.libr_listitem_book_documentType);
			holder.libr_storenum = (TextView) convertView
					.findViewById(R.id.libr_listitem_book_store_num);
			holder.libr_landable_num = (TextView) convertView
					.findViewById(R.id.libr_listitem_book_landable_num);

			convertView.setTag(holder);// 绑定ViewHolder对象
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String libr_name = null, libr_author = null, libr_press = null, libr_date = null, libr_callNumber = null, libr_ducumentType = null;
		String libr_marc_no = null;// 查看详情必填
		String libr_store_num = null;
		String libr_landable_num = null;

		//List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		for (Iterator<JSONArray> i = dataList.iterator(); i.hasNext();) {
			JSONArray json2 = i.next();
			int h=1;
			h++;
			Log.e("jjjjjjjjjjjjjjjjjjjjj",h+"");
			List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			
			/****** 设置对应的动态数组数据 *********/
			
			for (int j = 0; j < length; j++) {

				HashMap<String, String> map = new HashMap<String, String>();

				try {

					JSONObject obj = json2.getJSONObject(j);
					libr_name = obj.getString("title");
					libr_author = obj.getString("author");
					libr_press = obj.getString("publisher");
					// libr_date = obj.getString("book_date");
					libr_callNumber = obj.getString("isbn");
					libr_ducumentType = obj.getString("doctype");
					libr_marc_no = obj.getString("marc_no");
					libr_store_num = obj.getString("store_num");
					libr_landable_num = obj.getString("lendable_num");

					map.put("libr_name", libr_name);
					map.put("libr_author", libr_author);
					map.put("libr_press", libr_press);
					// map.put("libr_date", libr_date);
					map.put("libr_callNumber", libr_callNumber);
					map.put("libr_ducumentType", libr_ducumentType);
					map.put("libr_marc_no", libr_marc_no);
					map.put("libr_store_num", libr_store_num);
					map.put("libr_landable_num", libr_landable_num);
					holder.libr_name.setText(libr_name);
					data.add(map);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//int toalnum = 20 * CountOfScroll + position;
//			holder.libr_name.setText(toalnum + 1 + ".  "
//					+ data.get(position).get("libr_name").toString());
			holder.libr_name.setText( ".  "
					+ data.get(position).get("libr_name").toString());
			holder.libr_author.setText(data.get(position).get("libr_author")
					.toString());
			holder.libr_press.setText(data.get(position).get("libr_press")
					.toString());
			// holder.libr_date.setText(data.get(position).get("libr_date").toString());
			holder.libr_callNumber.setText(data.get(position)
					.get("libr_callNumber").toString());
			holder.libr_ducumentType.setText(data.get(position)
					.get("libr_ducumentType").toString());
			holder.libr_storenum.setText("馆藏副本："
					+ data.get(position).get("libr_store_num").toString());
			holder.libr_landable_num.setText("可借副本："
					+ data.get(position).get("libr_landable_num").toString());

		}
		return convertView;

	}

	public class ViewHolder {
		public TextView libr_name;
		public TextView libr_author;
		public TextView libr_press;
		// public TextView libr_date;
		public TextView libr_callNumber;
		public TextView libr_ducumentType;
		public TextView libr_storenum;
		public TextView libr_landable_num;
		// public Button libr_button_reserve;
	}

}
