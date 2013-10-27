package com.herald.ezherald.emptyclassroom;

import java.util.ArrayList;
import java.util.List;

import com.herald.ezherald.R;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class RoomAdapter extends BaseAdapter {
	
	private List<String> roomList = null;
	private Context context = null;
	
	public RoomAdapter(Context c)
	{
		context = c;
		roomList = new ArrayList<String>();
	}
	
	public void setRoomList(List<String> rl){
		roomList = rl;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return roomList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return roomList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(R.layout.emproom_item, null);
		
		String room = roomList.get(position);
		TextView roomView = (TextView) view.findViewById(R.id.emproom_list_item);
		roomView.setText(room);
		
		return view;
	}

}
