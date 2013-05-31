package com.herald.ezherald.gpa;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

import com.actionbarsherlock.app.SherlockActivity;
import com.herald.ezherald.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;

public class GPAActivity extends SherlockActivity {

	SlidingMenu menu;
	private CanvasTransformer mTrans;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gpa_activity_main);
		
		
	}
	
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockActivity#onCreateOptionsMenu(com.actionbarsherlock.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		
		getSupportMenuInflater().inflate(R.menu.main, menu); 

		return super.onCreateOptionsMenu(menu);
	}
	
	void initTransformer ()
	{
		/*
		 * 初始化Transformer实现Slidingmenu的切换效果
		 */
		mTrans = new CanvasTransformer() {
			
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				// TODO Auto-generated method stub
				float scale = (float) (percentOpen*0.25 + 0.75);
				canvas.scale(scale, scale, canvas.getWidth()/2, canvas.getHeight()/2);
			}
		};
	}
	
	private void initSlidingMenu() {
		/*
		 * 初始化SlidingMenu
		 */
		WindowManager wMng = getWindowManager();
		Display disp = wMng.getDefaultDisplay();
		int screenWidth = disp.getWidth();
		int shadowWidth = (int) (0.05 * screenWidth); //菜单阴影遮罩宽度
		int behindOffset= (int) (0.4 * screenWidth); //菜单之外内容的显示宽度
		
		menu = new SlidingMenu(this);
		//menu = getSlidingMenu();
		menu.setMode(SlidingMenu.LEFT_RIGHT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setFadeDegree(0.35f);
		
		//menu.setShadowWidth(shadowWidth);
		//menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffset(behindOffset);
		//menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setBehindScrollScale(0.0f);
		menu.setBehindCanvasTransformer(mTrans);
		menu.setSecondaryMenu(R.layout.main_frame_second_menu);
	}

}
