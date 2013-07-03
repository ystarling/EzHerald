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
 * ��һ��ʹ��ʱ����������
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
	
	//��������ͼƬ��Դ
	private static final int[] image_res = {
		R.drawable.main_frame_welcome_0, R.drawable.main_frame_welcome_1, 
		R.drawable.main_frame_welcome_2, R.drawable.main_frame_welcome_end
	};
	
	//�ײ�С���View
	private ImageView[] mDots;
	
	//��ǰλ��
	private int mCurrentIndex;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_frame_guide);
		
		mViews = new ArrayList<View>();
		
		LinearLayout.LayoutParams mParams = 
				new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		//��ʼ������ͼƬ�б�
		for(int imgid : image_res){
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(mParams);
			iv.setImageResource(imgid);
			mViews.add(iv);
		}
		
		mViewPager = (ViewPager)findViewById(R.id.main_frame_guide_viewpager);
		//��ʼ��Adapter
		mViewPagerAdapter = new MainGuideViewPagerAdapter(mViews);
		mViewPager.setAdapter(mViewPagerAdapter);
		
		//��Listener�ص�
		mViewPager.setOnPageChangeListener(this);
		
		//��ʼ���ײ���С��
		initDots();
	}

	/**
	 * ��ʼ�����µ�С��
	 */
	private void initDots() {
		LinearLayout ll = (LinearLayout)findViewById(R.id.main_frame_guide_linearlayout);
		mDots = new ImageView[image_res.length];
		//ѭ��ȡ��С���ͼƬ
		//TODO:5��ͼƬ������ô��>!!!!
		for(int i=0; i < image_res.length; i++){
			mDots[i] = (ImageView)ll.getChildAt(i);
			mDots[i].setEnabled(true); //Ĭ������Ϊ��ɫ
			mDots[i].setOnClickListener(this);
			mDots[i].setTag(i); //λ��tag,��������뵱ǰλ�ö�Ӧ
			mDots[i].setVisibility(View.VISIBLE);
		}
		//���һҳ���ڻ�����Χ�ڣ�����ʾС��
		mDots[image_res.length-1].setVisibility(View.GONE);
		
		mCurrentIndex = 0;
		mDots[mCurrentIndex].setEnabled(false);
	}
	
	/**
	 * ���õ�ǰ������ҳ
	 * @param position λ��
	 */
	private void setCurrView(int position){
		if(position<0 || position >= image_res.length){
			return;
		}
		mViewPager.setCurrentItem(position);
	}
	
	/**
	 * ���õ�ǰ������С��Ϊѡ��״̬
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
	

	//������״̬�ı�ʱ����
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		//Log.d("MainGuideActivity", "PageScrollStateChanged arg0 = " + arg0);
	}
	
	//����ǰҳ�汻����ʱ����
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		Log.d("MainGuideActivity", "PageScrolled arg0 = " + arg0
				+ "; arg1 = " + arg1 + "; arg2 = " + arg2);
		//����ĩҳ��ʱ��ر�Activity
		if(arg0 == image_res.length-2 && arg2 > 800)
			finish();
	}
	
	//���µ�ҳ�汻ѡ��ʱ����
	@Override
	public void onPageSelected(int arg0) {
		Log.d("MainGuideActivity", "PageSelected arg0 = " + arg0);
		setCurrDot(arg0);
	}

	//С���OnClick..
	@Override
	public void onClick(View v) {
		int position = (Integer)v.getTag();
		Log.d("MainGuideActivity", "onClick, position = " + position);
		setCurrView(position);
		setCurrDot(position);
	}
	
}
