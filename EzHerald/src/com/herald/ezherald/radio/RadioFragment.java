package com.herald.ezherald.radio;


import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;
import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.IDCardAccountActivity;
import com.herald.ezherald.account.UserAccount;


public class RadioFragment extends SherlockFragment {
	private static enum RadioType{
		DEMAND_SONG,FORCAST,
	};
	private Context context;
	private LayoutInflater inflater;
	private ViewGroup container;
	private Bundle savedInstanceState;
	
	private class Listener implements ActionBar.TabListener{
		Fragment frag;
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
			if (tab.getPosition() == RadioType.DEMAND_SONG.ordinal() ) {
				frag = new RadioDemandSongFragment();
			}
			if(tab.getPosition() ==  RadioType.FORCAST.ordinal() ) {
				frag = new RadioForcastFragment();
			}
			ft.replace(android.R.id.content, frag);
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
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		 Bundle savedInstanceState) {	
		 super.onCreateView(inflater, container, savedInstanceState);
		 context = getSherlockActivity();
		 this.inflater = inflater;
		 this.container = container;
		 this.savedInstanceState = savedInstanceState;
		 ActionBar bar = getSherlockActivity().getSupportActionBar();
		 bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		 bar.setTitle("广播台");
		 ActionBar.Tab tab1 = bar.newTab().setText("我要点歌").setTabListener(new Listener());
		 ActionBar.Tab tab2 = bar.newTab().setText("广播预报").setTabListener(new Listener());
		 bar.addTab(tab1);
		 bar.addTab(tab2);
		 return inflater.inflate(R.layout.radio_fragment_main,container,false);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		UserAccount user = Authenticate.getIDcardUser(context);
		if(user == null ){
			Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
			//getActivity().setContentView(setNotLoginView(inflater, container, savedInstanceState));
			Intent intent = new Intent();
			intent.setClass(context, IDCardAccountActivity.class);
			startActivity(intent);
		}
		super.onResume();
	}
}

