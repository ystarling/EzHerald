package com.herald.ezherald;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.academic.AcademicActivity;
import com.herald.ezherald.account.AccountActivity;
import com.herald.ezherald.activity.ActiActivity;
import com.herald.ezherald.bookingOffice.BookingActivity;
import com.herald.ezherald.curriculum.CurriculumActivity;
import com.herald.ezherald.emptyclassroom.EmptyClassroomActivity;
import com.herald.ezherald.exercise.ExerciseActivity;
import com.herald.ezherald.freshman.FreshmanActivity;
import com.herald.ezherald.gpa.GPAActivity;
import com.herald.ezherald.library.LibraryActivity;
import com.herald.ezherald.mainframe.MainMenuFragment;
import com.herald.ezherald.radio.RadioActivity;
import com.herald.ezherald.settingframe.SettingActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.tendcloud.tenddata.TCAgent;

/**
 * 基本的框架，主要包含了左右侧的侧滑菜单，还有点击菜单项的操作
 * 
 * @author BorisHe
 * @updated 20130630
 * 
 */
public class BaseFrameActivity extends SlidingFragmentActivity implements View.OnClickListener {
	//protected SlidingMenu menu;
    protected ResideMenu menu;
    protected ResideMenuItem[] menuItems;
    protected ResideMenu setting;
    protected ResideMenuItem[] settingItems;


	protected CanvasTransformer mTrans;
	protected Fragment mContentFrag; // 中间呈现的内容
	protected Fragment mMenuFrag; // 左侧侧滑菜单
	//protected Fragment mSecondaryMenuFrag; // 右侧侧滑菜单
	private long mExitTime;
    private static final String KEY_SHOWED_UPDATE = "showedUpdate"; //此次运行已经显示过更新了
    //protected static final String DOMAIN = "http://herald.seu.edu.cn";
	
	// protected int mContentResId;

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

	public void SetBaseFrameActivity(Fragment contentFragment) {
		/*
		 * @prarm contentFragment : 内容的Fragment
		 */
		// mContentResId = contentResourceId;
		mContentFrag = contentFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TCAgent.setReportUncaughtExceptions(true);//开启远程异常捕获
		InitBaseFrame();
		getSupportActionBar().setIcon(R.drawable.ic_app); //修改程序内部的图标
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
	}

	public void InitBaseFrame() {
		/**
		 * 初始化这个框架Actovity 在super.OnCreate之后再使用
		 * 
		 * @param contentResourceId
		 *            : 内容layout的资源Id
		 * @prarm contentFragment : 内容的Fragment
		 */

		setBehindContentView(R.layout.main_frame_menu);
		setContentView(R.layout.empty_frame_content);
		initTransformer(); // 初始化动画
		initSlidingMenu(); // 初始化菜单

		mMenuFrag = new MainMenuFragment();
		//mSecondaryMenuFrag = new SecondMenuFragment();

		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();
		//t.replace(R.id.main_frame_menu, mMenuFrag); // 切换menu的Fragement
		t.replace(R.id.empty_frame_content, mContentFrag); // 切换内容的Fragement
		//t.replace(R.id.main_frame_second_menu, mSecondaryMenuFrag);// 切换2ndmenu的Fragement
		t.commit();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
    private void initSlidingMenu(){
        menu = new ResideMenu(this);
        menu.setBackground(R.drawable.menu_background);
        menu.setShadowVisible(false);
        menu.attachToActivity(this);

        int[] menuIcon = new int[]{
                R.drawable.main_menu_ic_mainframe,
                R.drawable.main_menu_ic_curriculum,
                R.drawable.main_menu_ic_activity,
                R.drawable.main_menu_ic_library,
                R.drawable.main_menu_ic_gpa,
                R.drawable.main_menu_ic_exercise,
                R.drawable.main_menu_ic_academic,
                R.drawable.main_menu_ic_freshman,
                R.drawable.main_menu_ic_emptcls,
        }; // 图标(icon)

        String[] menuName = getResources().getStringArray(R.array.main_menu_items);
        menuItems = new ResideMenuItem[menuIcon.length];
        for(int i=0;i<menuIcon.length;++i) {
            menuItems[i] = new ResideMenuItem(this,menuIcon[i],menuName[i]);
            menuItems[i].setOnClickListener(this);
            menu.addMenuItem(menuItems[i],ResideMenu.DIRECTION_LEFT);
        }

        int[] settingIcon = new int[]{
                R.drawable.main_menu_ic_mainframe,
                R.drawable.main_menu_ic_curriculum,
                R.drawable.main_menu_ic_curriculum
        };
        String[] settingName = getResources().getStringArray(R.array.second_menu_items);
        settingItems = new ResideMenuItem[settingName.length];
        for (int i=0;i<settingName.length;i++){
            settingItems[i] = new ResideMenuItem(this,settingIcon[i],settingName[i]);
            settingItems[i].setOnClickListener(this);
            menu.addMenuItem(settingItems[i],ResideMenu.DIRECTION_RIGHT);
        }





    }

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {

		getSupportMenuInflater().inflate(R.menu.menu_main_frame, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * 上侧Title位置的按钮点击相应
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
			//menu.toggle(true); // 点击了程序图标后，会弹出/收回侧面菜单
            if(menu.isOpened()){
                menu.closeMenu();
            }else{
                menu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 退出程序确认
	 */
	private void openConfirmDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("\n真的要退出么...\n");
		builder.setPositiveButton("嗯..", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 确认按钮
				finish();
			}
		});
		builder.setNegativeButton("不要嘛~", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getBaseContext(), "太好了太好了~", Toast.LENGTH_SHORT).show();
			}
		});
		builder.show();
	}

	public void killMyself() {
		finish();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// 两次返回键退出
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if((System.currentTimeMillis() - mExitTime) > 2000){
				//Object mHelperUtils;
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
				//menu.showMenu();
                menu.openMenu(ResideMenu.DIRECTION_LEFT);
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
		// 统计模块
		TCAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 统计模块
		TCAgent.onResume(this);
	}


    @Override
    public void onClick(View v) {
        for(int i=0;i<menuItems.length;++i) {
            if(v == menuItems[i]) {
                runLeftMenuModel(i);
                break;
            }
        }
        for(int i=0;i<settingItems.length;++i){
            if(v==settingItems[i]){
                runRightMenuModel(i);
                break;
            }
        }
        menu.closeMenu();
    }

    private void runRightMenuModel(int position){
        String menuTarget = "Unknown";
        Intent intent = new Intent();
        switch (position) {
            case 0:
                //intent.setClass(this, AccountActivity.class);
               // menuTarget = "Account";
               intent.setClass(this, AccountActivity.class);
                menuTarget = "Accouont";
                break;
            case 1:
                //intent.setClass(this, AccountActivity.class);
                // menuTarget = "Account";
                intent.setClass(this, SettingActivity.class);
                menuTarget = "Setting";
                break;
            case 2:
                //intent.setClass(this, MainActivity.class);
               // menuTarget = "MainActivity";
                intent.setClass(this, AccountActivity.class);
                menuTarget = "Accouont";
                break;

        }
        TCAgent.onEvent(this, "主菜单点击", menuTarget);

        if (intent != null) {
            intent.putExtra(KEY_SHOWED_UPDATE, true);
            startActivity(intent);
            if(position != 0)
                killMyself();
        }
    }

    private void runLeftMenuModel(int position) {
        String menuTarget = "Unknown";
        Intent intent = new Intent();
        switch (position) {
            case 0:
                intent.setClass(this, AccountActivity.class);
                menuTarget = "Account";
                break;
            case 1:
                intent.setClass(this, MainActivity.class);
                menuTarget = "MainActivity";
                break;
            case 2:
                intent.setClass(this, CurriculumActivity.class);
                menuTarget = "Curriculum";
                break;
            case 3:
                intent.setClass(this, ActiActivity.class);
                menuTarget = "Activity";
                break;
            case 4:
                intent.setClass(this, LibraryActivity.class);
                menuTarget = "Library";
                break;
            case 5:
                intent.setClass(this, GPAActivity.class);
                menuTarget = "GPA";
                break;
            case 6:
                intent.setClass(this, ExerciseActivity.class);
                menuTarget = "Exercise";
                break;
            case 7:
                intent.setClass(this, AcademicActivity.class);
                menuTarget = "Academic";
                break;
            case 8:
                intent.setClass(this, FreshmanActivity.class);
                menuTarget = "CampusGuide";
                break;
            case 9:
                intent.setClass(this, EmptyClassroomActivity.class);
                menuTarget = "EmptyClass";
                break;

            case 10:
                intent.setClass(this, RadioActivity.class);
                menuTarget = "Radio";
                break;
            case 11:
                intent.setClass(this, BookingActivity.class);
                menuTarget = "bookingOffice";
                break;

        }
        TCAgent.onEvent(this, "主菜单点击", menuTarget);

        if (intent != null) {
            intent.putExtra(KEY_SHOWED_UPDATE, true);
            startActivity(intent);
            if(position != 0)
                killMyself();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return menu.dispatchTouchEvent(ev);
    }
}
