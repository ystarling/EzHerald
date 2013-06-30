package com.herald.ezherald.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.R;

public class ClubListActivity extends SherlockActivity {
	
	private ListView listView;
	private Context context;
	
	@SuppressLint("NewApi")
	@Override 
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acti_club_list);
		
		context = this;
		
		listView = (ListView) findViewById(R.id.acti_club_list);
		
		ClubListAdapter adapter = new ClubListAdapter(this);
		ClubItem [] clubs  = {new ClubItem("","��ȭ��",true),
				new ClubItem("","ѧ����",false)};
		adapter.setClubList(clubs);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Toast.makeText(context, ""+position+"   "+id, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(context,ClubDetailActivity.class);
				startActivity(intent);
			}
			
		});
		
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		
		
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem muItem)
	{
		switch(muItem.getItemId())
		{
		case android.R.id.home:
			finish();
			return true;
		}
		return false;
	}
	

}