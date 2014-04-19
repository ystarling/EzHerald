package com.herald.ezherald.mainframe;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MainGuideViewPagerAdapter extends PagerAdapter {

	private List<View> mViews; //View的列表
	
	public MainGuideViewPagerAdapter (List<View> views){
		mViews = views;
	}
	
	
	
	@Override
	public void destroyItem(View container, int position, Object object) {
		// 销毁position位置的界面
		//super.destroyItem(container, position, object);
		((ViewPager)container).removeView(mViews.get(position));
	}



	@Override
	public void finishUpdate(View container) {
		// TODO Auto-generated method stub
		//super.finishUpdate(container);
		
	}



	//获得当前View数
	@Override
	public int getCount() {
		if(mViews != null)
			return mViews.size();
		return 0;
	}

	
	//实例化position位置的View
	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager)container).addView(mViews.get(position), 0);
		//return super.instantiateItem(container, position);
		return mViews.get(position);
	}



	//判断是否由对象生成View
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return (arg0 == arg1);
	}



	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
		//super.restoreState(state, loader);
	}



	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		//return super.saveState();
		return null;
	}



	@Override
	public void startUpdate(View container) {
		//super.startUpdate(container);
	}
	
	
}
