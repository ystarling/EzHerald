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
	private List<RoomPair> roomPairList = null;
	private Context context = null;
	
	public RoomAdapter(Context c)
	{
		context = c;
		roomList = new ArrayList<String>();
		roomPairList = new ArrayList<RoomPair>();
	}
	
	public void setRoomList(List<String> rl){
		roomList = rl;
	}
	public void setRoomPairs(List<RoomPair> rp)
	{
		roomPairList = rp;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return roomPairList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return roomPairList.get(position);
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
		
//		String room = roomList.get(position);
		RoomPair pair = roomPairList.get(position);
//		TextView roomView = (TextView) view.findViewById(R.id.emproom_list_item);
//		roomView.setText(room);
		TextView roomViewLeft = (TextView) view.findViewById(R.id.emproom_list_item_left);
		TextView roomViewRight = (TextView) view.findViewById(R.id.emproom_list_item_right);
		roomViewLeft.setText(pair.getRoom1());
		roomViewRight.setText(pair.getRoom2());
		
		return view;
	}

}
