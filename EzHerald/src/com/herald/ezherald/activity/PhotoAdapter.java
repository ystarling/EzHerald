package com.herald.ezherald.activity;

import java.util.ArrayList;
import java.util.List;

import com.herald.ezherald.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class PhotoAdapter extends BaseAdapter {
	
	Context context;
	List<Integer> photoList;
	
	public PhotoAdapter(Context c)
	{
		context = c;
		photoList = new ArrayList<Integer>();
		photoList.add(R.drawable.ic_app);
		photoList.add(R.drawable.ic_app);
		photoList.add(R.drawable.ic_app);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return photoList.size();
	}

	@Override
	public Integer getItem(int arg0) {
		// TODO Auto-generated method stub
		return photoList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ImageView imageView;
		imageView= new ImageView(context);
		imageView.setImageResource(photoList.get(position));
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		int [] size = new int[2];
		//size = (int [])
		imageView.setLayoutParams(new Gallery.LayoutParams(250,300));
		return imageView;

	}

}
