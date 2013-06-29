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
 * 基本的框架，主要包含了左右侧的侧滑菜单，还有点击菜单项的操作
 * 
 * @author BorisHe
 * 
 */
public class BaseFrameActivity extends SlidingFragmentActivity {
	SlidingMenu menu;
	protected CanvasTransformer mTrans;
	protected Fragment mContentFrag; // 中间呈现的内容
	protected Fragment mMenuFrag; // 左侧侧滑菜单
	protected Fragment mSecondaryMenuFrag; // 右侧侧滑菜单
	//protected int mContentResId;

	protected void initTransformer() {
		/*
		 * 初始化Transformer实现Slidingmenu的切换效果
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
		 * 			  : 内容的Fragment
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
		 * 初始化这个框架Actovity 在super.OnCreate之后再使用
		 * 
		 * @param contentResourceId
		 *            : 内容layout的资源Id
		 * @prarm contentFragment
		 * 			  : 内容的Fragment
		 */
		
		setBehindContentView(R.layout.main_frame_menu);
		setContentView(R.layout.empty_frame_content);
		initTransformer(); // 初始化动画
		initSlidingMenu(); // 初始化菜单
		
		mMenuFrag = new MainMenuFragment();
		mSecondaryMenuFrag = new SecondMenuFragment();
		
		FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
		t.replace(R.id.main_frame_menu, mMenuFrag); //切换menu的Fragement
		t.replace(R.id.empty_frame_content, mContentFrag); //切换内容的Fragement
		t.replace(R.id.main_frame_second_menu, mSecondaryMenuFrag);//切换2ndmenu的Fragement
		t.commit();
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void initSlidingMenu() {
		/**
		 * 初始化侧滑菜单
		 */
		WindowManager wMng = getWindowManager();
		Display disp = wMng.getDefaultDisplay();
		int screenWidth = disp.getWidth();
		int shadowWidth = (int) (0.05 * screenWidth); // 菜单阴影遮罩宽度
		int behindOffset = (int) (0.4 * screenWidth); // 菜单之外内容的显示宽度

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
		 * 上侧Title位置的按钮点击相应
		 */
		switch(item.getItemId()){
		case R.id.action_settings:
			//Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
			menu.showSecondaryMenu();
			break;
		case android.R.id.home:
			menu.toggle(true); //点击了程序图标后，会弹出/收回侧面菜单
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void KillMyself()
	{
		finish();
	}
}
