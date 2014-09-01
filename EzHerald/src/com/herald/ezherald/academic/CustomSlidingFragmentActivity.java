package com.herald.ezherald.academic;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.R;
import com.herald.ezherald.mainframe.MainMenuFragment;
import com.herald.ezherald.mainframe.SecondMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class CustomSlidingFragmentActivity extends SlidingFragmentActivity {

	SlidingMenu menu;
	private CanvasTransformer mTrans;
	private Fragment mContentFrag;
	private Fragment mMenuFrag;
	private Fragment mSecondaryMenuFrag;

	public void SetContentFrag(Fragment fragment) {
		mContentFrag = fragment;
	}

	void initTransformer() {
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBehindContentView(R.layout.main_frame_menu);
		setContentView(R.layout.empty_frame_content); // 设置空白的底层Frame容器

		initTransformer(); // 初始化动画
		initSlidingMenu(); // 初始化菜单

		// mContentFrag = new MainContentFragment();
		// mContentFrag = new AcademicFragment();
		mMenuFrag = new MainMenuFragment();
		mSecondaryMenuFrag = new SecondMenuFragment();

		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();
		t.replace(R.id.main_frame_menu, mMenuFrag); // 切换menu的Fragement
		t.replace(R.id.empty_frame_content, mContentFrag); // 切换内容的Fragement
		t.replace(R.id.main_frame_second_menu, mSecondaryMenuFrag);// 切换2ndmenu的Fragement
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
		int shadowWidth = (int) (0.05 * screenWidth); // 菜单阴影遮罩宽度
		int behindOffset = (int) (0.4 * screenWidth); // 菜单之外内容的显示宽度

		// resideMenu = new SlidingMenu(this);
		menu = getSlidingMenu();
		menu.setMode(SlidingMenu.LEFT_RIGHT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setFadeDegree(0.35f);

		// resideMenu.setShadowWidth(shadowWidth);
		// resideMenu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffset(behindOffset);
		// resideMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setBehindScrollScale(0.0f);
		menu.setBehindCanvasTransformer(mTrans);
		menu.setSecondaryMenu(R.layout.main_frame_second_menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.actionbarsherlock.app.SherlockActivity#onCreateOptionsMenu(com.
	 * actionbarsherlock.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {

		getSupportMenuInflater().inflate(R.menu.menu_main_frame, menu);

		return super.onCreateOptionsMenu(menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockActivity#onOptionsItemSelected(com.
	 * actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * 上侧Title位置的按钮点击相应
		 */
		switch (item.getItemId()) {
		case R.id.action_settings:
			Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
			break;
		case android.R.id.home:
			menu.toggle(true); // 点击了程序图标后，会弹出/收回侧面菜单
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void switchContent(Fragment fragment) {
		/*
		 * 切换content碎片内容
		 * 
		 * @param fragment 传入的要替换的碎片
		 */
		mContentFrag = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.empty_frame_content, fragment).commit();
		getSlidingMenu().showContent();
	}

}
