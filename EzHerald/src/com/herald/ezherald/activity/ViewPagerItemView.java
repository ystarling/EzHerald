package com.herald.ezherald.activity;


import org.json.JSONException;
import org.json.JSONObject;

import com.herald.ezherald.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
		addView(view);
	}

	/**
	 * ������ݣ����ⲿ����.
	 * @param object
	 */
	public void setData(JSONObject object){
		this.mObject = object;
		try {
			int resId = object.getInt("resid");
			String name = object.getString("name");
			//ʵս�����ͼƬ��ʱӦ������һ���߳�ȥ��ͼƬ�첽,��Ȼ��UI�߳̿���.
			mAlbumImageView.setImageResource(resId);
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
		try {
			int resId = mObject.getInt("resid");
			//ʵս�����ͼƬ��ʱӦ������һ���߳�ȥ��ͼƬ�첽,��Ȼ��UI�߳̿���.
			mAlbumImageView.setImageResource(resId);
		}catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
