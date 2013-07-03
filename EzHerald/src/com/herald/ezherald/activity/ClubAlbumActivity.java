package com.herald.ezherald.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.R;

public class ClubAlbumActivity extends SherlockActivity {
	
	private static final int ALBUM_RES[] = {  
        R.drawable.acti_pic1,R.drawable.acti_pic2,R.drawable.acti_pic3,  
    }; 
	private static final int ALBUM_COUNT = ALBUM_RES.length ;
	private ViewPager mViewPager; 
	private ViewPagerAdapter mViewPagerAdapter;
	private JSONArray mJsonArray;
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acti_club_detail_photos);
		
		setupViews();
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("���");
		
	}
	
	private void setupViews(){    
    	//��ʼ��JSonArray,��ViewPageAdapter�ṩ����Դ��.
    	mJsonArray = new JSONArray();
    	for(int i = 0;i<ALBUM_COUNT; i++){
    		JSONObject object = new JSONObject();
    		try {
				object.put("resid", ALBUM_RES[i % ALBUM_RES.length]);
				object.put("name", "Album " + i);
	    		mJsonArray.put(object);
			} catch (JSONException e) {
				e.printStackTrace();
			}
    		
    	}    	
    	mViewPager = (ViewPager)this.findViewById(R.id.viewpager);
    	mViewPagerAdapter = new ViewPagerAdapter(this, mJsonArray);
    	mViewPager.setAdapter(mViewPagerAdapter);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case android.R.id.home:
			finish();
			return true;
		}
		return false;
		
	}
	

}