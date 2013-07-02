package com.herald.ezherald.mainframe;

import java.util.ArrayList;
import java.util.List;

import com.herald.ezherald.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * 第一次使用时的引导界面
 * @since 20130702
 * @author BorisHe
 *
 */
public class MainGuideActivity extends Activity 
	implements OnClickListener, OnPageChangeListener
{
	private ViewPager mViewPager;
	private MainGuideViewPagerAdapter mViewPagerAdapter;
	private List<View> mViews;
	
	//引导界面图片资源
	private static final int[] image_res = {
		R.drawable.main_frame_welcome_0, R.drawable.main_frame_welcome_1, 
		R.drawable.main_frame_welcome_2, R.drawable.main_frame_welcome_end
	};
	
	//底部小点的View
	private ImageView[] mDots;
	
	//当前位置
	private int mCurrentIndex;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_frame_guide);
		
		mViews = new ArrayList<View>();
		
		LinearLayout.LayoutParams mParams = 
				new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		//初始化引导图片列表
		for(int imgid : image_res){
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(mParams);
			iv.setImageResource(imgid);
			mViews.add(iv);
		}
		
		mViewPager = (ViewPager)findViewById(R.id.main_frame_guide_viewpager);
		//初始化Adapter
		mViewPagerAdapter = new MainGuideViewPagerAdapter(mViews);
		mViewPager.setAdapter(mViewPagerAdapter);
		
		//绑定Listener回调
		mViewPager.setOnPageChangeListener(this);
		
		//初始化底部的小点
		initDots();
	}

	/**
	 * 初始化底下的小点
	 */
	private void initDots() {
		LinearLayout ll = (LinearLayout)findViewById(R.id.main_frame_guide_linearlayout);
		mDots = new ImageView[image_res.length];
		//循环取得小点的图片
		//TODO:5张图片以上怎么办>!!!!
		for(int i=0; i < image_res.length; i++){
			mDots[i] = (ImageView)ll.getChildAt(i);
			mDots[i].setEnabled(true); //默认设置为灰色
			mDots[i].setOnClickListener(this);
			mDots[i].setTag(i); //位置tag,方便读出与当前位置对应
			mDots[i].setVisibility(View.VISIBLE);
		}
		//最后一页不在滑动范围内，不显示小点
		mDots[image_res.length-1].setVisibility(View.GONE);
		
		mCurrentIndex = 0;
		mDots[mCurrentIndex].setEnabled(false);
	}
	
	/**
	 * 设置当前的引导页
	 * @param position 位置
	 */
	private void setCurrView(int position){
		if(position<0 || position >= image_res.length){
			return;
		}
		mViewPager.setCurrentItem(position);
	}
	
	/**
	 * 设置当前引导的小点为选中状态
	 * @param position
	 */
	private void setCurrDot(int position){
		if (position < 0 || position > image_res.length - 1 || mCurrentIndex == position) {
            return;
        }
		
		mDots[position].setEnabled(false);
		mDots[mCurrentIndex].setEnabled(true);
		
		mCurrentIndex = position;
	}
	

	//当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		//Log.d("MainGuideActivity", "PageScrollStateChanged arg0 = " + arg0);
	}
	
	//当当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		Log.d("MainGuideActivity", "PageScrolled arg0 = " + arg0
				+ "; arg1 = " + arg1 + "; arg2 = " + arg2);
		//滑到末页的时候关闭Activity
		if(arg0 == image_res.length-2 && arg2 > 800)
			finish();
	}
	
	//当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0) {
		Log.d("MainGuideActivity", "PageSelected arg0 = " + arg0);
		setCurrDot(arg0);
	}

	//小点的OnClick..
	@Override
	public void onClick(View v) {
		int position = (Integer)v.getTag();
		Log.d("MainGuideActivity", "onClick, position = " + position);
		setCurrView(position);
		setCurrDot(position);
	}
	
}
