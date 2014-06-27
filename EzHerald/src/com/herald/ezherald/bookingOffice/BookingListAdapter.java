package com.herald.ezherald.bookingOffice;


import android.R.drawable;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
    public ImageView bookingIcon;
    public TextView booking_caption;
    public TextView booking_is_order;
    public TextView booking_activity_time;
    public TextView booking_deadline;

}
public class BookingListAdapter extends BaseAdapter{
    private LayoutInflater mInflater = null;
    private Context mContext = null;
    private BookingDBAdapter dbAdapter;
    private int DBSize;
    private int startPosition = 0;
    String[] columns = {"id","caption","posterUrl","number","activity_time","deadline"};
    private Cursor cursor;
    public void getDbInfo(){
        dbAdapter = new BookingDBAdapter(mContext);
        dbAdapter.mDB = dbAdapter.mDBHelper.getReadableDatabase();
        cursor = dbAdapter.mDB.query(false,BookingDBAdapter.TABLE_BOOKING_LIST,columns,null,null,null,null,"activity_time",null);
        cursor.moveToFirst();
        DBSize = cursor.getCount();
      }

    private final String imageUrl = "http://t12.baidu.com/it/u=3412601765,4237021788&fm=55&s=DE3C0EC602E30AA4299734360300C04A&w=121&h=81&img.JPEG";
    public BookingListAdapter(Context context){
    	super();
        mContext = context;
    	mInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        getDbInfo();
    }



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
        return DBSize;
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
		ViewHolder holder = new ViewHolder();
        cursor.moveToPosition(position);
        if(convertView == null) {

            convertView = mInflater.inflate(R.layout.booking_listview_subview, null);
            holder.bookingIcon = (ImageView) convertView.findViewById(R.id.booking_icon);
            holder.booking_activity_time = (TextView) convertView.findViewById(R.id.booking_activity_time);
            holder.booking_caption = (TextView) convertView.findViewById(R.id.booking_caption);
            holder.booking_deadline = (TextView) convertView.findViewById(R.id.booking_deadline);
            holder.booking_is_order = (TextView) convertView.findViewById(R.id.booking_is_order);
            convertView.setTag(holder);
            }
        else{
            holder = (ViewHolder)convertView.getTag();
        }
            holder.booking_caption.setText(cursor.getString(1));
            holder.booking_activity_time.setText(cursor.getString(4));
            holder.booking_deadline.setText(cursor.getString(5));
            holder.booking_is_order.setText("[未预定]");


		return convertView;
	}





}


