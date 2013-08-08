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
public class libraryFragmentMain extends SherlockFragment{
	
	ActionBar.Tab libr_tab1;
	ActionBar.Tab libr_tab2;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved){
		
		View view=inflater.inflate(R.layout.library_fragment_tab_select, null);
		
		ActionBar bar = getSherlockActivity(). getSupportActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		libr_tab1=bar.newTab();
		libr_tab2=bar.newTab();
		
		libr_tab1.setText("搜索");
		libr_tab2.setText("资讯");
		
		libr_tab1.setTabListener(new libraryFragmentTabSelect());
		libr_tab2.setTabListener(new libraryFragmentTabSelect());
		
		bar.addTab(libr_tab1);
		bar.addTab(libr_tab2);
		
		return view;
		
	}
}
