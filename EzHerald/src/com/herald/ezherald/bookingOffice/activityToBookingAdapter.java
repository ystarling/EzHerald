package com.herald.ezherald.bookingOffice;


import android.R.drawable;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.herald.ezherald.R;


class ViewHolder{
	public ImageView bookingImage;
	public TextView booking_en_word;
	public TextView booking_cn_word;
}
public class activityToBookingAdapter extends BaseAdapter{
    private LayoutInflater mInflater = null;
    public activityToBookingAdapter(Context context){
    	super();
    	mInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.booking_listview_subview,null);
			holder.bookingImage = (ImageView)convertView.findViewById(R.id.booking_imageview);
			holder.booking_cn_word = (TextView)convertView.findViewById(R.id.booking_cn_word);
			holder.booking_en_word = (TextView)convertView.findViewById(R.id.booking_en_word);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.bookingImage.setImageResource(drawable.spinner_background);
		holder.booking_cn_word.setText("cn_word for test!");
		holder.booking_en_word.setText("en_word for test!");
		return convertView;
	}

}
