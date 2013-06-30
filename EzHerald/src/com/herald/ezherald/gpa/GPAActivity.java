package com.herald.ezherald.gpa;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.herald.ezherald.R;

public class GPAActivity extends SherlockFragmentActivity {
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gpa_activity_main);
		ActionBar bar = getSupportActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ActionBar.Tab tab1 = bar.newTab();
		ActionBar.Tab tab2 = bar.newTab();
		ActionBar.Tab tab3 = bar.newTab();
		tab1.setText("¼¨µãÕþ²ß");
		tab2.setText("Fragment B");
		tab3.setText("Fragment C");
		tab1.setTabListener(new MyTabListener());
		tab2.setTabListener(new MyTabListener());
		tab3.setTabListener(new MyTabListener());
		bar.addTab(tab1);
		bar.addTab(tab2);
		bar.addTab(tab3);
	}

	private class MyTabListener implements ActionBar.TabListener {

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (tab.getPosition() == 0) {
				FragmentA frag = new FragmentA();
				ft.replace(android.R.id.content, frag);
			} else {
				// FragmentB frag = new FragmentB();
				// ft.replace(android.R.id.content, frag);
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
