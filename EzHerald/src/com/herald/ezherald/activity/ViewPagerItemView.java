package com.herald.ezherald.activity;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import com.herald.ezherald.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author frankiewei
 * 相册的ItemView,自定义View.方便复用.
 */
public class ViewPagerItemView extends FrameLayout {

	/**
	 * 图片的ImageView.
	 */
	private ImageView mAlbumImageView;
	
	/**
	 * 图片名字的TextView.
	 */
	private TextView mALbumNameTextView;
	
	private ProgressBar mALbumProgress;
	
	/**
	 * 图片的Bitmap.
	 */
	private Bitmap mBitmap;
	
	/**
	 * 要显示图片的JSONOBject类.
	 */
	private JSONObject mObject;
	
	
	public ViewPagerItemView(Context context){
		super(context);
		setupViews();
	}
	
	public ViewPagerItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}
	
	//初始化View.
	private void setupViews(){
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.acti_club_detail_photo_item, null);
		
		mAlbumImageView = (ImageView)view.findViewById(R.id.album_imgview);
		mALbumNameTextView = (TextView)view.findViewById(R.id.album_name); 
		mALbumProgress = (ProgressBar) view.findViewById(R.id.acti_activity_pic_progress);
		addView(view);
	}

	/**
	 * 填充数据，共外部调用.
	 * @param object
	 */
	public void setData(JSONObject object){
		this.mObject = object;
		try {
//			int resId = object.getInt("resid");
			String post_add = object.getString("pic_add");
			String name = object.getString("name");
			//实战中如果图片耗时应该令其一个线程去拉图片异步,不然把UI线程卡死.
			new RequestImage().execute(post_add);
//			mAlbumImageView.setImageResource(resId);
			mALbumNameTextView.setText(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
		
	/**
	 * 这里内存回收.外部调用.
	 */
	public void recycle(){
		mAlbumImageView.setImageBitmap(null);
		if ((this.mBitmap == null) || (this.mBitmap.isRecycled()))
			return;
		this.mBitmap.recycle();
		this.mBitmap = null;
	}
	
	
	/**
	 * 重新加载.外部调用.
	 */
	public void reload(){
		//			int resId = mObject.getInt("resid");
		//实战中如果图片耗时应该令其一个线程去拉图片异步,不然把UI线程卡死.
//			mAlbumImageView.setImageResource(resId);
		mAlbumImageView.setImageBitmap(mBitmap);
	}
	
	
	private class RequestImage extends AsyncTask<String,Integer,Bitmap>
	{
		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			InputStream in = null;
			Bitmap bitmap = null;
			int response = -1;
//			String url_str = "http://herald.seu.edu.cn/herald_league/Uploads/ActivityPost/m_l_post_add/"+params[0];
			String url_str = "http://static.dayandcarrot.net/temp/aoi_sora.jpg";
			try {
				URL url = new URL(url_str);
				URLConnection conn = url.openConnection();
				if(!(conn instanceof HttpURLConnection) )
				{
					throw new IOException("Not a HTTP Connection");
				}
				else
				{
					HttpURLConnection httpConn = (HttpURLConnection) conn;
					httpConn.setAllowUserInteraction(false);
					httpConn.setInstanceFollowRedirects(true);
					httpConn.setRequestMethod("GET");
					httpConn.connect();
					response = httpConn.getResponseCode();
					if (response == HttpURLConnection.HTTP_OK)
					{
						in = httpConn.getInputStream();
						bitmap = BitmapFactory.decodeStream(in);
						in.close();
						return bitmap;
					}
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Bitmap result)
		{
			mBitmap = result;
			mAlbumImageView.setImageBitmap(mBitmap);
			mALbumProgress.setVisibility(View.GONE);
		}
		
	}
	
	
}
