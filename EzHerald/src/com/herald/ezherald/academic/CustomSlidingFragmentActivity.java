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
import com.herald.ezherald.mainframe.MainContentFragment;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBehindContentView(R.layout.main_frame_menu);
		setContentView(R.layout.empty_frame_content); // ���ÿհ׵ĵײ�Frame����

		initTransformer(); // ��ʼ������
		initSlidingMenu(); // ��ʼ���˵�

		// mContentFrag = new MainContentFragment();
		// mContentFrag = new AcademicFragment();
		mMenuFrag = new MainMenuFragment();
		mSecondaryMenuFrag = new SecondMenuFragment();

		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();
		t.replace(R.id.main_frame_menu, mMenuFrag); // �л�menu��Fragement
		t.replace(R.id.empty_frame_content, mContentFrag); // �л����ݵ�Fragement
		t.replace(R.id.main_frame_second_menu, mSecondaryMenuFrag);// �л�2ndmenu��Fragement
		t.commit();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void initSlidingMenu() {
		/*
		 * ��ʼ��SlidingMenu
		 */
		WindowManager wMng = getWindowManager();
		Display disp = wMng.getDefaultDisplay();
		int screenWidth = disp.getWidth();
		int shadowWidth = (int) (0.05 * screenWidth); // �˵���Ӱ���ֿ��
		int behindOffset = (int) (0.4 * screenWidth); // �˵�֮�����ݵ���ʾ���

		// menu = new SlidingMenu(this);
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
		 * �ϲ�Titleλ�õİ�ť�����Ӧ
		 */
		switch (item.getItemId()) {
		case R.id.action_settings:
			Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
			break;
		case android.R.id.home:
			menu.toggle(true); // ����˳���ͼ��󣬻ᵯ��/�ջز���˵�
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void switchContent(Fragment fragment) {
		/*
		 * �л�content��Ƭ����
		 * 
		 * @param fragment �����Ҫ�滻����Ƭ
		 */
		mContentFrag = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.empty_frame_content, fragment).commit();
		getSlidingMenu().showContent();
	}

}
