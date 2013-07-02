package com.herald.ezherald.activity;


import android.annotation.SuppressLint;
import android.app.ActionBar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.R;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class ClubDetailActivity extends SlidingFragmentActivity implements ActionBar.OnNavigationListener {
	
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	ClubDetailIntroFragment fragmentIntro;
	ClubDetailActisFragment fragmentActi;
	ClubDetailPhotosFragment fragmentPhotos;
	ClubDetailIntroFragment fragment;
	
	
	@SuppressLint("NewApi")
	@Override 
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setBehindContentView(R.layout.main_frame_menu);
		setContentView(R.layout.acti_club_detail);
		//setHasOptionsMenu(true);
		
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		
		SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(this,
				R.array.acti_club_detail_action_spinner, android.R.layout.simple_spinner_dropdown_item);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setListNavigationCallbacks(spinnerAdapter, this);
		
		fragmentManager = this.getSupportFragmentManager(); 
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentIntro = new ClubDetailIntroFragment();
		fragmentActi  = new ClubDetailActisFragment();
		fragmentPhotos = new ClubDetailPhotosFragment();
		
		//fragmentTransaction.add(R.id.acti_club_detail_frag_container, fragmentIntro);
		//fragmentTransaction.commit();
		//fragment = (ClubDetailIntroFragment) fragmentManager.findFragmentById(R.id.acti_club_acti_frag);
		
		
	}
	
	@Override 
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem muItem)
	{
		switch(muItem.getItemId() )
		{
		case android.R.id.home:
			finish();
			break;
		default:
			return false;
				
		}
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		switch(itemPosition)
		{
		case 0:
			Toast.makeText(this, ""+itemPosition, Toast.LENGTH_SHORT).show();
			fragmentManager.beginTransaction().replace(R.id.acti_club_detail_frag_container, fragmentIntro).commit();
			return true;
		case 1:
			Toast.makeText(this, ""+itemPosition, Toast.LENGTH_SHORT).show();
			fragmentManager.beginTransaction().replace(R.id.acti_club_detail_frag_container, fragmentActi).commit();
			return true;
		case 2:
			Toast.makeText(this, ""+itemPosition, Toast.LENGTH_SHORT).show();
			fragmentManager.beginTransaction().replace(R.id.acti_club_detail_frag_container, fragmentPhotos).commit();
			return true;
			
		}
		// TODO Auto-generated method stub
		return false;
	}

}
