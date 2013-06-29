package com.herald.ezherald.exercise;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.herald.ezherald.R;

public class ExerciseActivity extends SherlockFragmentActivity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(false){
			//TODO 未登陆显示登陆界面
		}else{
			setContentView(R.layout.exercise_activity_main);
			ActionBar bar = getSupportActionBar();
			bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			ActionBar.Tab tab1 = bar.newTab();
			ActionBar.Tab tab2 = bar.newTab();
			ActionBar.Tab tab3 = bar.newTab();
			tab1.setText("跑操通知");
			tab2.setText("跑操次数");
			tab3.setText("天天快跑");
			tab1.setTabListener(new MyTabListener());
			tab2.setTabListener(new MyTabListener());
			tab3.setTabListener(new MyTabListener());
			bar.addTab(tab1);
			bar.addTab(tab2);
			bar.addTab(tab3);
		} 
	}
	    
	private class MyTabListener implements ActionBar.TabListener
    {

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if(tab.getPosition()==0)
			{
				FragmentA frag = new FragmentA();
				ft.replace(android.R.id.content, frag);
			}
			else
			{
				//FragmentB frag = new FragmentB();
				//ft.replace(android.R.id.content, frag);
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}
    }
	
}
