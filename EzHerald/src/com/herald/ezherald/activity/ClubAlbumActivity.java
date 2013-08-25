package com.herald.ezherald.activity;

import java.util.ArrayList;
import java.util.List;

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
	
	private String[] pic_adds;
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
		
		Bundle bundle = this.getIntent().getExtras();
		pic_adds = bundle.getStringArray("pic_adds");
		
		setupViews();
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("相册");
		
	}
	
	private void setupViews(){    
    	//初始化JSonArray,给ViewPageAdapter提供数据源用.
    	mJsonArray = new JSONArray();
    	for(int i = 0;i<pic_adds.length; i++){
    		JSONObject object = new JSONObject();
    		try {
				object.put("pic_add", pic_adds[i % ALBUM_RES.length]);
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
