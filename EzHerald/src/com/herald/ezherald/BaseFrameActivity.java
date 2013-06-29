package com.herald.ezherald;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.WindowManager;

import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.mainframe.MainMenuFragment;
import com.herald.ezherald.mainframe.SecondMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.herald.ezherald.R;

/**
 * �����Ŀ�ܣ���Ҫ���������Ҳ�Ĳ໬�˵������е���˵���Ĳ���
 * 
 * @author BorisHe
 * 
 */
public class BaseFrameActivity extends SlidingFragmentActivity {
	SlidingMenu menu;
	protected CanvasTransformer mTrans;
	protected Fragment mContentFrag; // �м���ֵ�����
	protected Fragment mMenuFrag; // ���໬�˵�
	protected Fragment mSecondaryMenuFrag; // �Ҳ�໬�˵�
	//protected int mContentResId;

	protected void initTransformer() {
		/*
		 * ��ʼ��Transformerʵ��Slidingmenu���л�Ч��
		 */
		mTrans = new CanvasTransformer() {

			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				// TODO Auto-generated method stub
				float scale = (float) (percentOpen * 0.25 + 0.75);
				canvas.scale(scale, scale, canvas.getWidth() / 2,
						canvas.getHeight() / 2);
			}
		};
	}
	
	public void SetBaseFrameActivity(Fragment contentFragment)
	{
		/*
		 * @prarm contentFragment
		 * 			  : ���ݵ�Fragment
		 * */
		//mContentResId = contentResourceId;
		mContentFrag = contentFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		InitBaseFrame();
	}



	public void InitBaseFrame() {
		/**
		 * ��ʼ��������Actovity ��super.OnCreate֮����ʹ��
		 * 
		 * @param contentResourceId
		 *            : ����layout����ԴId
		 * @prarm contentFragment
		 * 			  : ���ݵ�Fragment
		 */
		
		setBehindContentView(R.layout.main_frame_menu);
		setContentView(R.layout.empty_frame_content);
		initTransformer(); // ��ʼ������
		initSlidingMenu(); // ��ʼ���˵�
		
		mMenuFrag = new MainMenuFragment();
		mSecondaryMenuFrag = new SecondMenuFragment();
		
		FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
		t.replace(R.id.main_frame_menu, mMenuFrag); //�л�menu��Fragement
		t.replace(R.id.empty_frame_content, mContentFrag); //�л����ݵ�Fragement
		t.replace(R.id.main_frame_second_menu, mSecondaryMenuFrag);//�л�2ndmenu��Fragement
		t.commit();
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void initSlidingMenu() {
		/**
		 * ��ʼ���໬�˵�
		 */
		WindowManager wMng = getWindowManager();
		Display disp = wMng.getDefaultDisplay();
		int screenWidth = disp.getWidth();
		int shadowWidth = (int) (0.05 * screenWidth); // �˵���Ӱ���ֿ���
		int behindOffset = (int) (0.4 * screenWidth); // �˵�֮�����ݵ���ʾ����

		menu = getSlidingMenu();
		menu.setMode(SlidingMenu.LEFT_RIGHT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setFadeDegree(0.35f);

		// menu.setShadowWidth(shadowWidth);
		// menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffset(behindOffset);
		// menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setBehindScrollScale(0.0f);
		menu.setBehindCanvasTransformer(mTrans);
		menu.setSecondaryMenu(R.layout.main_frame_second_menu);
	}
	
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		
		getSupportMenuInflater().inflate(R.menu.main, menu); 

		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * �ϲ�Titleλ�õİ�ť�����Ӧ
		 */
		switch(item.getItemId()){
		case R.id.action_settings:
			//Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
			menu.showSecondaryMenu();
			break;
		case android.R.id.home:
			menu.toggle(true); //����˳���ͼ��󣬻ᵯ��/�ջز���˵�
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void KillMyself()
	{
		finish();
	}
}