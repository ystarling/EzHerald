package com.herald.ezherald.activity;

import java.util.ArrayList;
import java.util.List;

import com.herald.ezherald.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ClubAlbumAdapter extends BaseAdapter {
	
	private Context context;
	private List<ClubAlbum> albumList;
	
	public ClubAlbumAdapter(Context c)
	{
		context = c;
		albumList = new ArrayList<ClubAlbum>();
	}
	
	public void setAlbumList(ClubAlbum [] albumArr)
	{
		albumList.clear();
		for(int loop = 0; loop < albumArr.length; ++ loop )
		{
			albumList.add(albumArr[loop]);
		}
	}
	
	public void setAlbumList(List<ClubAlbum> cl)
	{
		albumList.clear();
		albumList.addAll(cl);
	}
	
	
	
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return albumList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return albumList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(context).inflate(R.layout.acti_club_detail_album_listitem	, null);
		TextView tvName = (TextView) convertView.findViewById(R.id.acti_club_detail_album_list_name);
		ImageView ivAlbumFace = (ImageView) convertView.findViewById(R.id.acti_club_detail_album_list_item_face);
		ClubAlbum album = albumList.get(position);
		tvName.setText(album.getAlbumName());
		ivAlbumFace.setImageResource(R.drawable.ic_launcher);
		
		return convertView;
	}
	
	

}


