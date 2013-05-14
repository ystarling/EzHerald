package com.herald.ezherald;

import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.mainframe.MainContentFragment;
import com.herald.ezherald.mainframe.MainMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends SlidingFragmentActivity {
	SlidingMenu menu;
	private CanvasTransformer mTrans;
	private Fragment mContentFrag;
	private MainMenuFragment mMenuFrag;
	
	void initTransformer ()
	{
		/*
		 * 初始化Transformer
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBehindContentView(R.layout.main_frame_menu);
		setContentView(R.layout.empty_frame_content);

		
		initTransformer(); //初始化动画
		initSlidingMenu(); //初始化菜单
		
		mContentFrag = new MainContentFragment();
		mMenuFrag = new MainMenuFragment();
		
		FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
		t.replace(R.id.main_frame_menu, mMenuFrag);
		t.replace(R.id.empty_frame_content, mContentFrag);
		t.commit();
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
		
		//menu = new SlidingMenu(this);
		menu = getSlidingMenu();
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setFadeDegree(0.35f);
		//menu.setShadowWidth(shadowWidth);
		//menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffset(behindOffset);
		//menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setBehindScrollScale(0.0f);
		menu.setBehindCanvasTransformer(mTrans);
		
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockActivity#onCreateOptionsMenu(com.actionbarsherlock.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		getSupportMenuInflater().inflate(R.menu.main, menu); 

		return super.onCreateOptionsMenu(menu);
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockActivity#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.action_settings:
			Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
			break;
		case android.R.id.home:
			menu.toggle(true);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void switchContent(Fragment fragment) {
		mContentFrag = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.empty_frame_content, fragment)
		.commit();
		getSlidingMenu().showContent();
	}

}
