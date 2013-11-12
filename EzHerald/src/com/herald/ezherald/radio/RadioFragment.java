package com.herald.ezherald.radio;


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


public class RadioFragment extends SherlockFragment {
	private static enum RadioType{
		DEMAND_SONG,FORCAST,
	};
	
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
		 ActionBar bar = getSherlockActivity().getSupportActionBar();
		 bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		 bar.setTitle("广播台");
		 ActionBar.Tab tab1 = bar.newTab().setText("我要点歌").setTabListener(new Listener());
		 ActionBar.Tab tab2 = bar.newTab().setText("广播预报").setTabListener(new Listener());
		 bar.addTab(tab1);
		 bar.addTab(tab2);
		 return inflater.inflate(R.layout.radio_fragment_main,container,false);
	}
}

