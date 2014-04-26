package com.herald.ezherald.bookingOffice;


import android.R.drawable;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.herald.ezherald.R;

//import org.apache.commons.httpclient.util.HttpURLConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


class ViewHolder{
    public ImageView bookingImage;
    public TextView booking_en_word;
    public TextView booking_cn_word;

}
public class BookingListAdapter extends BaseAdapter{
    private LayoutInflater mInflater = null;
    private Context mContext = null;



    private final String imageUrl = "http://t12.baidu.com/it/u=3412601765,4237021788&fm=55&s=DE3C0EC602E30AA4299734360300C04A&w=121&h=81&img.JPEG";
    public BookingListAdapter(Context context){
    	super();
        mContext = context;
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


        GetImageViaUrl requestImage = new GetImageViaUrl(mContext);
        try {
            requestImage.execute(new ViewUrlInfo(holder.bookingImage,new URL(imageUrl),-1));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

		holder.booking_cn_word.setText("cn_word for test!");
		holder.booking_en_word.setText("en_word for test!");
		return convertView;
	}





}


