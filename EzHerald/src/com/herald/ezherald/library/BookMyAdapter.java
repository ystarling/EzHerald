package com.herald.ezherald.library;

import java.util.ArrayList;
import java.util.HashMap;
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

public class BookMyAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	JSONArray jsonarray;
	Context mycontext;
	int CountOfScroll;
	public BookMyAdapter(Context c,int count) {
		this.inflater = LayoutInflater.from(c);
		this.CountOfScroll=count;
		//this.jsonarray = ar;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return jsonarray.length();
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

			convertView.setTag(holder);// ��ViewHolder����
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String libr_name = null, libr_author = null, libr_press = null, libr_date = null, libr_callNumber = null, libr_ducumentType = null;
		String libr_marc_no = null;// �鿴�������
		String libr_store_num = null;
		String libr_landable_num = null;

		List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

		/****** ���ö�Ӧ�Ķ�̬�������� *********/
		Log.d("jsonArray length():", jsonarray.length() + "");

		for (int i = 0; i < jsonarray.length(); i++) {

			HashMap<String, String> map = new HashMap<String, String>();

			try {

				JSONObject obj = jsonarray.getJSONObject(i);
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

				data.add(map);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int toalnum=20*CountOfScroll+position;
		holder.libr_name.setText(toalnum + 1 + ".  "
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
		holder.libr_storenum.setText("�ݲظ�����"
				+ data.get(position).get("libr_store_num").toString());
		holder.libr_landable_num.setText("�ɽ踱����"
				+ data.get(position).get("libr_landable_num").toString());
		
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
