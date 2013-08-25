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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author frankiewei
 * ����ItemView,�Զ���View.���㸴��.
 */
public class ViewPagerItemView extends FrameLayout {

	/**
	 * ͼƬ��ImageView.
	 */
	private ImageView mAlbumImageView;
	
	/**
	 * ͼƬ���ֵ�TextView.
	 */
	private TextView mALbumNameTextView;
	
	private ProgressBar mALbumProgress;
	
	/**
	 * ͼƬ��Bitmap.
	 */
	private Bitmap mBitmap;
	
	/**
	 * Ҫ��ʾͼƬ��JSONOBject��.
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
	
	//��ʼ��View.
	private void setupViews(){
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.acti_club_detail_photo_item, null);
		
		mAlbumImageView = (ImageView)view.findViewById(R.id.album_imgview);
		mALbumNameTextView = (TextView)view.findViewById(R.id.album_name); 
		mALbumProgress = (ProgressBar) view.findViewById(R.id.acti_activity_pic_progress);
		addView(view);
	}

	/**
	 * ������ݣ����ⲿ����.
	 * @param object
	 */
	public void setData(JSONObject object){
		this.mObject = object;
		try {
//			int resId = object.getInt("resid");
			String post_add = object.getString("pic_add");
			String name = object.getString("name");
			//ʵս�����ͼƬ��ʱӦ������һ���߳�ȥ��ͼƬ�첽,��Ȼ��UI�߳̿���.
			new RequestImage().execute(post_add);
//			mAlbumImageView.setImageResource(resId);
			mALbumNameTextView.setText(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
		
	/**
	 * �����ڴ����.�ⲿ����.
	 */
	public void recycle(){
		mAlbumImageView.setImageBitmap(null);
		if ((this.mBitmap == null) || (this.mBitmap.isRecycled()))
			return;
		this.mBitmap.recycle();
		this.mBitmap = null;
	}
	
	
	/**
	 * ���¼���.�ⲿ����.
	 */
	public void reload(){
		//			int resId = mObject.getInt("resid");
		//ʵս�����ͼƬ��ʱӦ������һ���߳�ȥ��ͼƬ�첽,��Ȼ��UI�߳̿���.
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
