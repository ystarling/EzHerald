package com.herald.ezherald.gpa;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;

public class GpaFragment extends SherlockFragment {
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved){
		// TODO Auto-generated method stub
		super.onCreate(saved);
		ActionBar bar = getSherlockActivity().getSupportActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        int x = bar.getTabCount();
        if(bar.getTabCount()<2) {
        	 ActionBar.Tab tab1 = bar.newTab();
             ActionBar.Tab tab2 = bar.newTab();
             ActionBar.Tab tab3 = bar.newTab();
//             tab1.setText("绩点政策");
             tab2.setText("成绩查询");
             tab3.setText("成绩分析");
             tab1.setTabListener(new MyTabListener());
             tab2.setTabListener(new MyTabListener());
             tab3.setTabListener(new MyTabListener());
//             bar.addTab(tab1);
             bar.addTab(tab2); 
             bar.addTab(tab3); 
        }
        return inflater.inflate(R.layout.gpa_activity_main,group,false);
	    }
	    
	    private class MyTabListener implements ActionBar.TabListener
	    {

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				if(tab.getPosition() == -1 )
				{
					FragmentA frag = new FragmentA();
					ft.replace(android.R.id.content, frag);
				}
				else if(tab.getPosition() == 0)
				{
					FragmentB frag = new FragmentB();
					ft.replace(android.R.id.content, frag);
				} 
				else 
				{
					FragmentC frag = new FragmentC();
					ft.replace(android.R.id.content, frag);
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
