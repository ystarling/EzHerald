package com.herald.ezherald;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.mainframe.MainMenuFragment;
import com.herald.ezherald.mainframe.SecondMenuFragment;
import com.herald.ezherald.settingframe.SettingActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.tendcloud.tenddata.TCAgent;
import com.herald.ezherald.R;

/**
 * �����Ŀ�ܣ���Ҫ���������Ҳ�Ĳ໬�˵������е���˵���Ĳ���
 * 
 * @author BorisHe
 * @updated 20130630
 * 
 */
public class BaseFrameActivity extends SlidingFragmentActivity {
	protected SlidingMenu menu;
	protected CanvasTransformer mTrans;
	protected Fragment mContentFrag; // �м���ֵ�����
	protected Fragment mMenuFrag; // ���໬�˵�
	//protected Fragment mSecondaryMenuFrag; // �Ҳ�໬�˵�
	private long mExitTime;
	
	
	// protected int mContentResId;

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

	public void SetBaseFrameActivity(Fragment contentFragment) {
		/*
		 * @prarm contentFragment : ���ݵ�Fragment
		 */
		// mContentResId = contentResourceId;
		mContentFrag = contentFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TCAgent.setReportUncaughtExceptions(true);//����Զ���쳣����
		InitBaseFrame();
		getSupportActionBar().setIcon(R.drawable.ic_app); //�޸ĳ����ڲ���ͼ��
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
	}

	public void InitBaseFrame() {
		/**
		 * ��ʼ��������Actovity ��super.OnCreate֮����ʹ��
		 * 
		 * @param contentResourceId
		 *            : ����layout����ԴId
		 * @prarm contentFragment : ���ݵ�Fragment
		 */

		setBehindContentView(R.layout.main_frame_menu);
		setContentView(R.layout.empty_frame_content);
		initTransformer(); // ��ʼ������
		initSlidingMenu(); // ��ʼ���˵�

		mMenuFrag = new MainMenuFragment();
		//mSecondaryMenuFrag = new SecondMenuFragment();

		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();
		t.replace(R.id.main_frame_menu, mMenuFrag); // �л�menu��Fragement
		t.replace(R.id.empty_frame_content, mContentFrag); // �л����ݵ�Fragement
		//t.replace(R.id.main_frame_second_menu, mSecondaryMenuFrag);// �л�2ndmenu��Fragement
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
		int shadowWidth = (int) (0.05 * screenWidth); // �˵���Ӱ���ֿ��
		int behindOffset = (int) (0.4 * screenWidth); // �˵�֮�����ݵ���ʾ���

		menu = getSlidingMenu();
		//menu.setMode(SlidingMenu.LEFT_RIGHT);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setFadeDegree(0.35f);

		menu.setShadowWidth(shadowWidth);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffset(behindOffset);
		
		// menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setBehindScrollScale(1.0f);
		menu.setBehindCanvasTransformer(mTrans);
		//menu.setSecondaryMenu(R.layout.main_frame_second_menu);
		//menu.setSecondaryShadowDrawable(R.drawable.shadowright);

	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {

		getSupportMenuInflater().inflate(R.menu.menu_main_frame, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * �ϲ�Titleλ�õİ�ť�����Ӧ
		 */
		switch (item.getItemId()) {
		case R.id.action_settings:
			// Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
			Intent i = new Intent();
			i.setClass(getBaseContext(), SettingActivity.class);
			String localActivityName = getLocalClassName();
			if(localActivityName.contains("Main")){
				MainActivity mainActivity = (MainActivity)this;
				mainActivity.needRefreshContent = true;
			}
			startActivity(i);
			break;
		case R.id.mainframe_menu_item_exit:
			openConfirmDialog();
			break;
		case android.R.id.home:
			menu.toggle(true); // ����˳���ͼ��󣬻ᵯ��/�ջز���˵�
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * �˳�����ȷ��
	 */
	private void openConfirmDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("\n���Ҫ�˳�ô...\n");
		builder.setPositiveButton("��..", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ȷ�ϰ�ť
				finish();
			}
		});
		builder.setNegativeButton("��Ҫ��~", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getBaseContext(), "̫����̫����~", Toast.LENGTH_SHORT).show();
			}
		});
		builder.show();
	}

	public void KillMyself() {
		finish();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// ���η��ؼ��˳�
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if((System.currentTimeMillis() - mExitTime) > 2000){
				//Object mHelperUtils;
				Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
				menu.showMenu();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// ͳ��ģ��
		TCAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ͳ��ģ��
		TCAgent.onResume(this);
	}
	
	
}
