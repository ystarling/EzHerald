package com.herald.ezherald.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;

public class ClubDetailPhotosFragment extends SherlockFragment{
	
	private ClubAlbumAdapter albumAdapter;
	private Activity context;
	private Gallery gallery;
	private ImageView imageView;
	
	private View v;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

	}
	
	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		v = inflater.inflate(R.layout.acti_club_detail_album_list, null);
		context = getActivity();	
		albumAdapter = new ClubAlbumAdapter(context);

		ListView listView = (ListView) v.findViewById(R.id.acti_club_detail_album_list);
		List<ClubAlbum> albumList = new ArrayList<ClubAlbum>();
		albumList.add(new ClubAlbum("校庆演出",""));
		albumList.add(new ClubAlbum("南京赛区决赛",""));
		albumAdapter.setAlbumList(albumList);
		listView.setAdapter(albumAdapter);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				context.startActivity(new Intent(context,ClubAlbumActivity.class));
			}
			
		});
		
		
		return v;
	}
	
	
	
	
	
	

}










