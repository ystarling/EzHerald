package com.herald.ezherald;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.academic.AcademicActivity;
import com.herald.ezherald.account.AccountActivity;
import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.IDCardAccountActivity;
import com.herald.ezherald.account.LibAccountActivity;
import com.herald.ezherald.account.TyxAccountActivity;
import com.herald.ezherald.account.UserAccount;
import com.herald.ezherald.curriculum.CurriculumActivity;
import com.herald.ezherald.emptyclassroom.EmptyClassroomActivity;
import com.herald.ezherald.exercise.ExerciseActivity;
import com.herald.ezherald.freshman.FreshmanActivity;
import com.herald.ezherald.gpa.GPAActivity;
import com.herald.ezherald.library.LibraryActivity;
import com.herald.ezherald.settingframe.SettingsActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.tendcloud.tenddata.TCAgent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * 基本的框架，主要包含了左右侧的侧滑菜单，还有点击菜单项的操作
 * 
 * @author BorisHe
 * @updated 20130630
 * 
 */
public class BaseFrameActivity extends SlidingFragmentActivity implements View.OnClickListener {
	//protected SlidingMenu resideMenu;
    protected ResideMenu resideMenu;
    protected ResideMenuItem[] menuItems;
    protected ResideMenu setting;
    protected ResideMenuItem[] settingItems;
    protected ArrayList<String> targetName;
    protected ArrayList<Class> targetClass;


	protected CanvasTransformer mTrans;
	protected Fragment mContentFrag; // 中间呈现的内容
	protected Fragment mMenuFrag; // 左侧侧滑菜单
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
		//initSlidingMenu(); // 初始化菜单,应该只在 onresume中调用，否则会使得背景变成透明

		//mMenuFrag = new MainMenuFragment();
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
        if(resideMenu==null){
            resideMenu = new ResideMenu(this);
        }

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> set = pref.getStringSet("activity",new TreeSet<String>());

        ArrayList<Integer> menuIcon = new ArrayList<Integer>();
        ArrayList<String> menuName = new ArrayList<String>();
        targetClass = new ArrayList<Class>();
        targetName = new ArrayList<String>();

        targetName.add("MainActivity");
        targetClass.add(MainActivity.class);
        menuIcon.add(R.drawable.main_menu_ic_mainframe);
        menuName.add("主界面");

        menuIcon.add(R.drawable.main_2ndmenu_ic_accsetting);
        menuName.add("功能选择");
        targetName.add("addModel");
        targetClass.add(SettingsActivity.class);

        for(String activity:set) {
            if(activity.equals("curriculum")){
                menuIcon.add(R.drawable.main_menu_ic_curriculum);
                menuName.add("课表查询");
                targetName.add("Curriculum");
                targetClass.add(CurriculumActivity.class);
            } else if(activity.equals("library")) {
                menuIcon.add(R.drawable.main_menu_ic_library);
                menuName.add("图书查询");
                targetName.add("Library");
                targetClass.add(LibraryActivity.class);
            } else if(activity.equals("gpa")){
                menuIcon.add(R.drawable.main_menu_ic_gpa);
                menuName.add("绩点查询");
                targetName.add("GPA");
                targetClass.add(GPAActivity.class);
            } else if(activity.equals("exercise")) {
                menuIcon.add(R.drawable.main_menu_ic_exercise);
                menuName.add("跑操查询");
                targetName.add("Exercise");
                targetClass.add(ExerciseActivity.class);
            } else if(activity.equals("academic")) {
                menuIcon.add(R.drawable.main_menu_ic_academic);
                menuName.add("教务信息");
                targetName.add("Academic");
                targetClass.add(AcademicActivity.class);
            } else if(activity.equals("freshman")) {
                menuIcon.add(R.drawable.main_menu_ic_freshman);
                menuName.add("新生指南");
                targetName.add("Freshman");
                targetClass.add(FreshmanActivity.class);
            }else if(activity.equals("emptyclassroom")){
                menuIcon.add(R.drawable.main_menu_ic_emptcls);
                menuName.add("空教室");
                targetName.add("EmptyClassRoom");
                targetClass.add(EmptyClassroomActivity.class);
            }
        }



        int backgroundId = pref.getInt("background",0);
        String img = "menu_background_"+backgroundId;
        try {
            Field field = R.drawable.class.getField(img);
            int id = field.getInt(field);
            resideMenu.setBackground(id);
        } catch (Exception e) {
            resideMenu.setBackground(R.drawable.menu_background_0);
        }



        resideMenu.setShadowVisible(false);
        resideMenu.attachToActivity(this);
        menuItems = new ResideMenuItem[menuIcon.size()];

        resideMenu.clearLeftMenuItems();
        resideMenu.clearRightMenuItems();

        for(int i=0;i<menuIcon.size();++i) {
            menuItems[i] = new ResideMenuItem(this,menuIcon.get(i),menuName.get(i));
            menuItems[i].setOnClickListener(this);
            resideMenu.addMenuItem(menuItems[i], ResideMenu.DIRECTION_LEFT);
        }

        int[] settingIcon = new int[]{
                R.drawable.main_2ndmenu_ic_account,
                R.drawable.main_2ndmenu_ic_account,
                R.drawable.main_2ndmenu_ic_account,
                R.drawable.main_2ndmenu_ic_setting,
                R.drawable.main_2ndmenu_ic_setting

        };
        String[] settingName = new String[]{
                "一卡通账户",
                "体育系账户",
                "图书馆账户",
                "程序设置",
                "账户设置"

        };

        settingItems = new ResideMenuItem[settingName.length];
       /* rightMenu = new ResideMenu(this);
        rightMenu.setBackground(R.drawable.menu_background_0);
        rightMenu.setShadowVisible(false);
        rightMenu.attachToActivity(this);*/

        for (int i=0;i<settingName.length;i++){

            settingItems[i] = new ResideMenuItem(this,settingIcon[i],settingName[i]);
            settingItems[i].setOnClickListener(this);
            resideMenu.addMenuItem(settingItems[i], ResideMenu.DIRECTION_RIGHT);

        }

        setSettingMenuColor();



    }


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		/*
		 * 上侧Title位置的按钮点击相应
		 */
		switch (item.getItemId()) {

		case R.id.action_settings:
            resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
			return true;
		case R.id.mainframe_menu_item_exit:
			openConfirmDialog();
			return true;
		case android.R.id.home:
			//resideMenu.toggle(true); // 点击了程序图标后，会弹出/收回侧面菜单
            if(resideMenu.isOpened()){
                resideMenu.closeMenu();
            }else{
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
			return true;
        default:
            return super.onOptionsItemSelected(item);
		}

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
				//resideMenu.showMenu();
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
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
    private void setSettingMenuColor(){
        String OKColor = "#FFFFFF",notOKColor="#FD7734";
        UserAccount user = null;
        for (int i=0;i<settingItems.length;i++) {
            switch (i) {
                case 0:
                    user = new Authenticate().getIDcardUser(this);
                    if (user == null) {
                        settingItems[i].setTextColor(notOKColor);
                    } else {
                        settingItems[i].setTextColor(OKColor);
                    }
                    break;
                case 1:
                    user = new Authenticate().getTyxUser(this);
                    if (user == null) {
                        settingItems[i].setTextColor(notOKColor);
                    } else {
                        settingItems[i].setTextColor(OKColor);
                    }
                    break;
                case 2:
                    user = new Authenticate().getLibUser(this);
                    if (user == null) {
                        settingItems[i].setTextColor(notOKColor);
                    } else {
                        settingItems[i].setTextColor(OKColor);
                    }
                    break;


            }
        }
    }
	@Override
	protected void onResume() {
        super.onResume();
        // 统计模块
        TCAgent.onResume(this);
        //Toast.makeText(this, "Resume!", Toast.LENGTH_SHORT).show();

        initSlidingMenu();
        setSettingMenuColor();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
       // Toast.makeText(this, "Resume!", Toast.LENGTH_SHORT).show();


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
        resideMenu.closeMenu();
    }

    private void runRightMenuModel(int position){
        String menuTarget = "Unknown";
        Intent intent = null;
        switch (position) {

             //case 0; 有bug，跳过0
             //intent.setClass(this, AccountActivity.class);
               // menuTarget = "Account";
               //intent.setClass(this, SettingActivity.class);
               // menuTarget = "Setting";
               // break;
            case 0:
                //intent.setClass(this, AccountActivity.class);
                // menuTarget = "Account";
                intent = new Intent();
                intent.setClass(this, IDCardAccountActivity.class);
                menuTarget = "IDCard";
                break;
            case 1:
                //intent.setClass(this, MainActivity.class);
               // menuTarget = "MainActivity";
                intent = new Intent();
                intent.setClass(this, TyxAccountActivity.class);
                menuTarget = "TyxAccount";
                break;
            case 2:
                //intent.setClass(this, MainActivity.class);
                // menuTarget = "MainActivity";
                intent = new Intent();
                intent.setClass(this, LibAccountActivity.class);
                menuTarget = "LibAccount";
                break;
            case 3:
                //intent.setClass(this, MainActivity.class);
                // menuTarget = "MainActivity";
                intent = new Intent();
                intent.setClass(this, SettingsActivity.class);
                menuTarget = "Setting";
                break;
            case 4:
                //intent.setClass(this, MainActivity.class);
                // menuTarget = "MainActivity";
                intent = new Intent();
                intent.setClass(this, AccountActivity.class);
                menuTarget = "Account";
                break;

        }
        TCAgent.onEvent(this, "设置菜单点击", menuTarget);

        if (intent != null) {
            intent.putExtra(KEY_SHOWED_UPDATE, true);
            startActivity(intent);
        }
    }

    private void runLeftMenuModel(int position) {

        if(targetClass.get(position).getSimpleName().equals(getLocalClassName())) {
            resideMenu.closeMenu();
            return ;
        }



        Intent intent = new Intent();
        intent.setClass(this,targetClass.get(position));
        String menuTarget = targetName.get(position);
        TCAgent.onEvent(this, "主菜单点击", menuTarget);
        if(targetClass.get(position).equals(SettingsActivity.class)){
            intent.putExtra(SettingsActivity.SHOW_ACTIVITY_SELECTION,true);
        }
        if (intent != null) {
            intent.putExtra(KEY_SHOWED_UPDATE, true);
            startActivity(intent);
            if(position != 0 && !targetClass.get(position).equals(SettingsActivity.class))
                killMyself();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.default_herald_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


}
