package com.herald.ezherald.library;

import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;

/***********设置tab，分为搜索，查询，个人信息***********/
public class LibraryFragmentMain extends SherlockFragment{
	
	ActionBar.Tab libr_tab1;
	ActionBar.Tab libr_tab2;
	ActionBar.Tab libr_tab3;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved){
		
		View view=inflater.inflate(R.layout.library_fragment_tab_select, null);
		
		ActionBar bar = getSherlockActivity(). getSupportActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		libr_tab1=bar.newTab();
		libr_tab2=bar.newTab();
		libr_tab3=bar.newTab();
		
		libr_tab1.setText("搜索");
		libr_tab2.setText("公告");
		libr_tab3.setText("我的借阅");
		
		
		libr_tab1.setTabListener(new LibraryFragmentTabSelect());
		libr_tab2.setTabListener(new LibraryFragmentTabSelect());
		libr_tab3.setTabListener(new LibraryFragmentTabSelect());
		
		bar.addTab(libr_tab1);
		bar.addTab(libr_tab2);
		bar.addTab(libr_tab3);
		
		return view;
		
	}
}
